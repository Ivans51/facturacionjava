package core.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import core.conexion.MyBatisConnection;
import core.dao.ClienteDAO;
import core.dao.FacturaDAO;
import core.dao.ServiciosDAO;
import core.vo.Cliente;
import core.vo.Factura;
import core.vo.Servicios;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.HashMap;

public class CreateReportPDF {

    private FacturaDAO facturaDAO = new FacturaDAO(MyBatisConnection.getSqlSessionFactory());
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());

    private HashMap<String, Integer> totalArt = new HashMap<>();
    private Factura factura;
    private Cliente cliente;

    /**
     * Print PDF though id
     * @param args: args
     */
    public static void main(String[] args) {
        try {
            CreateReportPDF createReportPDF = new CreateReportPDF();
            createReportPDF.setValuesServicios(12);
            createReportPDF.actionImprimir();
            createReportPDF.setReportTecnicoPDF(createReportPDF.getNameFile()[1]);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private void setValuesServicios(int id) {
        factura = facturaDAO.selectById(id);
        cliente = clienteDAO.selectById(factura.getCliente_cedula());
        int occurance = StringUtils.countMatches(factura.getServicios(), ",") + 1;
        String arrPair[] = factura.getServicios().split(",", occurance);
        for (int i = 0; i < occurance; i++) {
            String[] strings = arrPair[i].split("x", 2);
            if (totalArt.get(strings[0].trim()) != null)
                totalArt.merge(strings[0].trim(), Integer.valueOf(strings[1].trim()), Integer::sum);
            else
                totalArt.put(strings[0].trim(), Integer.valueOf(strings[1].trim()));
        }
    }

    private void actionImprimir() {
        try {
            String title = "Inversiones Todo Frío C.A. " +
                    "\nJ-29441763-9 \nDireccion: Avenida los Cedros Cruce C/C Junin Local 105-C Barrio Lourdes Maracay " +
                    "\n Teléfono: 0243-5117088" +
                    "\n Email: Inversionestodofrioca@gmail.com" +
                    "\n " + "Factura Nº: " + factura.getIdfactura();
            String sub = "Factura del día: " + FechaUtil.getDateFormat(factura.getFecha_pago());

            PDFCreator pdfCreator = new PDFCreator("reports/" + getNameFile()[0]);
            pdfCreator.createPDF(documento -> {

                Paragraph cellRight = pdfCreator.setParagraph(title, Element.ALIGN_RIGHT, 10, 12, Font.BOLD);
                Paragraph cellLeft = pdfCreator.setParagraph(sub, Element.ALIGN_LEFT, 10, 12, Font.NORMAL);
                Image image = pdfCreator.setImagePDF("src/main/resources/images/FacturaLogo.png", 150, 100, Element.ALIGN_LEFT);

                PdfPTable tableTitle = pdfCreator.setTablePDF(new float[]{220, 300}, tabla -> {
                    PdfPCell pdfPCellLeft = pdfCreator.setCellPDF(Element.ALIGN_TOP, Rectangle.NO_BORDER, image, cellLeft);
                    PdfPCell pdfPCellRight = pdfCreator.setCellPDF(Element.ALIGN_TOP, Rectangle.NO_BORDER, cellRight);
                    tabla.addCell(pdfPCellLeft);
                    tabla.addCell(pdfPCellRight);
                });
                documento.add(tableTitle);

                documento.add(pdfCreator.setParagraph("Datos del Cliente \n ", Element.ALIGN_LEFT, 10, 12, Font.BOLD));

                PdfPTable tableCliente = pdfCreator.setTablePDFWithoutBorder(new float[]{520}, tabla -> {
                    tabla.addCell("Nombre o razón social: " + cliente.getNombres() + " " + cliente.getApellidos());
                    tabla.addCell("Domicilio fiscal: " + cliente.getDireccion());
                    tabla.addCell("C.I. o RIF: " + cliente.getCedula());
                    tabla.addCell("Teléfono: " + cliente.getTelefono());
                    tabla.addCell("Número de Placa: " + factura.getCliente().getPlaca());
                });
                documento.add(tableCliente);

                PdfPTable tableDetail = pdfCreator.setTablePDF(new float[]{40, 220, 130, 130}, tabla -> {
                    tabla.addCell("Cant.");
                    tabla.addCell("Concepto o Descripción");
                    tabla.addCell("Precio Unitario");
                    tabla.addCell("Total");
                    totalArt.forEach((key, value) -> {
                        Servicios servicios = serviciosDAO.selectByNombre(key);
                        tabla.addCell(pdfCreator.setStyleCellTable(String.valueOf(value)));
                        tabla.addCell(pdfCreator.setStyleCellTable(key));
                        tabla.addCell(pdfCreator.setStyleCellTable(String.format("%1$,.2f", servicios.getPrecio()) + " Bs"));
                        tabla.addCell(pdfCreator.setStyleCellTable(String.format("%1$,.2f", value * servicios.getPrecio()) + " Bs"));
                    });
                });
                documento.add(tableDetail);

                PdfPTable tableEnd = pdfCreator.setTablePDF(new float[]{260, 260}, tabla -> {
                    PdfPTable tableOneTotal = pdfCreator.setTablePDFWithoutBorder(new float[]{260}, tabla1 -> {
                        for (int i = 0; i < 3; i++) tabla1.addCell("");
                    });

                    PdfPTable tableTwoTotal = pdfCreator.setTablePDF(new float[]{130, 130}, tabla2 -> {
                        tabla2.addCell("Subtotal");
                        tabla2.addCell(String.format("%1$,.2f", calcularSubtotal()) + " Bs");

                        tabla2.addCell("IVA");
                        tabla2.addCell(String.format("%1$,.2f", factura.getIVA()) + " Bs");

                        tabla2.addCell("Total a Pagar");
                        tabla2.addCell(String.format("%1$,.2f", factura.getTotal()) + " Bs");
                    });
                    tabla.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                    tabla.addCell(tableOneTotal);
                    tabla.addCell(tableTwoTotal);
                });
                documento.add(tableEnd);

                String value = "Tipo de Pago: " + factura.getForma_pago();
                documento.add(pdfCreator.setParagraph(value, Element.ALIGN_LEFT, 10, 12, Font.BOLD));
            });
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setReportTecnicoPDF(String namePdf) throws IOException, DocumentException {
        String title = "Inversiones Todo Frío C.A. " +
                "\nJ-29441763-9 \nDireccion: Avenida los Cedros Cruce C/C Junin Local 105-C Barrio Lourdes Maracay " +
                "\n Teléfono: 0243-5117088" +
                "\n Email: Inversionestodofrioca@gmail.com" +
                "\n " + "Factura Nº: " + factura.getIdfactura();
        String sub = "Factura del día: " + FechaUtil.getDateFormat(factura.getFecha_pago());

        PDFCreator pdfCreator = new PDFCreator("reports/" + namePdf);
        pdfCreator.createPDF(documento -> {

            Paragraph elementRight = pdfCreator.setParagraph(title, Element.ALIGN_RIGHT, 10, 12, Font.BOLD);
            Paragraph elementsLeft = pdfCreator.setParagraph(sub, Element.ALIGN_LEFT, 10, 12, Font.NORMAL);
            Image image = pdfCreator.setImagePDF("src/main/resources/images/FacturaLogo.png", 150, 100, Element.ALIGN_LEFT);

            PdfPTable tableTitle = pdfCreator.setTablePDF(new float[]{220, 300}, tabla -> {
                PdfPCell pdfPCellLeft = pdfCreator.setCellPDF(Element.ALIGN_TOP, Rectangle.NO_BORDER, image, elementsLeft);
                PdfPCell pdfPCellRight = pdfCreator.setCellPDF(Element.ALIGN_TOP, Rectangle.NO_BORDER, elementRight);
                tabla.addCell(pdfPCellLeft);
                tabla.addCell(pdfPCellRight);
            });
            documento.add(tableTitle);

            documento.add(pdfCreator.setParagraph("Datos del Cliente \n ", Element.ALIGN_LEFT, 10, 12, Font.BOLD));

            PdfPTable tableCliente = pdfCreator.setTablePDFWithoutBorder(new float[]{520}, tabla -> {
                tabla.addCell("Nombre o razón social: " + cliente.getNombres() + " " + cliente.getApellidos());
                tabla.addCell("Domicilio fiscal: " + cliente.getDireccion());
                tabla.addCell("C.I. o RIF: " + cliente.getCedula());
                tabla.addCell("Teléfono: " + cliente.getTelefono());
                tabla.addCell("Número de Placa: " + factura.getCliente().getPlaca());
            });
            documento.add(tableCliente);

            PdfPTable tableDetail = pdfCreator.setTablePDF(new float[]{40, 480}, tabla -> {
                tabla.addCell("Cant.");
                tabla.addCell("Concepto o Descripción");
                totalArt.forEach((key, value) -> {
                    tabla.addCell(pdfCreator.setStyleCellTable(String.valueOf(value)));
                    tabla.addCell(pdfCreator.setStyleCellTable(key));
                });
            });
            documento.add(tableDetail);
        });
    }

    private String[] getNameFile() {
        return factura.getNameFile().split("/", 2);
    }

    private double calcularSubtotal() {
        return factura.getTotal() - (factura.getTotal() * 0.12);
    }
}
