package core.util;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFCreator {

    private Font.FontFamily family = Font.FontFamily.COURIER;
    private BaseColor background = BaseColor.DARK_GRAY;
    private String nameFile;

    public PDFCreator(String nameFile) {
        this.nameFile = nameFile;
    }

    public void createPDF(Elements element) throws IOException, DocumentException {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(nameFile));
        documento.open();
        element.add(documento);
        documento.close();
        System.out.println("Imprisión correcta");
        openPDF();
    }

    /**
     * @param columnWidth - array of a size 520 max
     * @param pdfTablaTwo - Interface to add information to cell
     */
    public PdfPTable setTablePDF(float[] columnWidth, PDFTabla pdfTablaTwo) throws DocumentException, IOException {
        PdfPTable tablaTwo = new PdfPTable(columnWidth.length);
        if (columnWidth.length > 0) {
            tablaTwo.setTotalWidth(columnWidth);
            tablaTwo.setWidthPercentage(100);
            tablaTwo.setLockedWidth(true);
        }
        pdfTablaTwo.addCellTable(tablaTwo);
        return tablaTwo;
    }

    public PdfPTable setTablePDFWithoutBorder(float[] columnWidth, PDFTabla pdfTablaTwo) throws DocumentException, IOException {
        PdfPTable tablaTwo = new PdfPTable(columnWidth);
        if (columnWidth.length > 0) {
            tablaTwo.setTotalWidth(columnWidth);
            tablaTwo.setWidthPercentage(100);
            tablaTwo.setLockedWidth(true);
        }
        tablaTwo.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        pdfTablaTwo.addCellTable(tablaTwo);
        tablaTwo.setTableEvent(new BorderEvent());
        return tablaTwo;
    }

    /**
     * Import! - Add Cell at the setTablePDF and after array elements
     *
     * @param align  - For example: Element.ALIGN_LEFT
     * @param border - For example: Rectangle.NO_BORDER
     */
    public PdfPCell setCellPDF(int align, int border, Element... elements) throws IOException, DocumentException {
        PdfPCell cellLeft = new PdfPCell();
        cellLeft.setVerticalAlignment(align);
        cellLeft.setBorder(border);
        for (Element el : elements)
            cellLeft.addElement(el);
        return cellLeft;
    }

    /**
     * Import! - Add Cell at the Documento
     *
     * @param witdh  - Example: 150
     * @param height - Example: 100
     * @param align  - For example: Element.ALIGN_LEFT
     */
    public Image setImagePDF(String path, float witdh, float height, int align) throws IOException, BadElementException {
        Image img = Image.getInstance(path);
        img.setAlignment(align);
        img.scaleToFit(witdh, height);
        return img;
    }

    /**
     * Import! - Add Cell at the Documento
     *
     * @param align - For example: Element.ALIGN_LEFT
     */
    public Paragraph setParagraph(String value, int align, int paddingTop, int sizeFont, int style) {
        Paragraph paragraph = new Paragraph(value, new Font(family, sizeFont, style, background));
        paragraph.setAlignment(align);
        paragraph.setPaddingTop(paddingTop);
        return paragraph;
    }

    public PdfPCell setStyleCellTable(String value) {
        Font titleFont = FontFactory.getFont(FontFactory.COURIER, 11, BaseColor.DARK_GRAY);
        Paragraph subTitle = new Paragraph(value, titleFont);
        PdfPCell cell = new PdfPCell(subTitle);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    public void openPDF() {
        if (Desktop.isDesktopSupported()) try {
            Desktop.getDesktop().open(new File(this.nameFile));
        } catch (IOException ex) {
            new AlertUtil(Estado.EXITOSA, "No se pudo abrir o crear");
            System.out.println("No se pudo abrir o crear " + ex.getMessage());
        }
    }

    public interface Elements {
        void add(Document documento) throws DocumentException, IOException;
    }

    public interface PDFTabla {
        void addCellTable(PdfPTable tabla) throws IOException, DocumentException;
    }
}