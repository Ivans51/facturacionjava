package core.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.*;
import core.util.*;
import core.vo.Factura;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class Graficos extends ManagerFXML implements Initializable {

    public JFXButton btnSalir, btnConsultar, btnImprimir;
    public AnchorPane anchorPane;
    public ComboBox<String> cTime;
    public DatePicker datePickerUno, datePickerDos;

    public PieChart chartPie;
    public BarChart<String, Number> chartBar;
    public CategoryAxis x;
    public NumberAxis y;
    public VBox contentChart;

    private FacturaDAO facturaDAO = new FacturaDAO(MyBatisConnection.getSqlSessionFactory());
    private String comboReportes = "";
    private String[] rangoTiempo = {"Dia", "Mes", "Rango"};
    private List<String> tipos = new ArrayList<>();
    private String comboTime = "Dia";
    private HashMap<String, Integer> servicios = new HashMap<>();
    private boolean isFirstTime = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePickerDos.setVisible(false);
        stateButton();
        setCombo();
    }

    private void stateButton() {
        // Deshabilitar botones
        switch (Storage.getUsuario().getStatus()) {
            case Estado.TECNICO:
                tipos.add("Factura");
                comboReportes = "Factura";
                break;
            case Estado.GERENTE:
                tipos.add("Auditoria");
                tipos.add("Servicios");
                tipos.add("Cliente");
                tipos.add("Factura");
                tipos.add("Usuario");
                comboReportes = "Auditoria";
                break;
            case Estado.ASISTENTE:
                tipos.add("Cliente");
                tipos.add("Factura");
                comboReportes = "Factura";
                break;
        }
    }

    private void setCombo() {
        cTime.setItems(FXCollections.observableArrayList(rangoTiempo));
        cTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            comboTime = newValue;
            datePickerDos.setVisible(newValue.equals("Rango"));
        });
    }

    public void actionConsultar(ActionEvent actionEvent) {
        try {
            setConsulta();
        } catch (Myexception myexception) {
            myexception.printStackTrace();
            new AlertUtil(Estado.EXITOSA, myexception.getMessage());
        }
    }

    private void setConsulta() throws Myexception {
        if (comboTime != null && !"".equals(comboTime) && datePickerUno.getValue() != null) {
            clear();
            setTableFacturaTime();
            setBarChart(servicios);
            setPieChart(servicios);
        } else
            new AlertUtil(Estado.EXITOSA, "Faltan valores por seleccionar");
    }

    private void clear() {
        servicios.clear();
        chartBar.getData().clear();
        chartPie.getData().clear();
    }

    private void setTableFacturaTime() throws Myexception {
        LocalDate timeOne = datePickerUno.getValue();
        LocalDate timeTwo = datePickerDos.getValue();
        Factura factura = new Factura();
        if (timeOne != null) {
            switch (comboTime) {
                case "Dia":
                    factura.setIdfactura(timeOne.getDayOfMonth());
                    factura.setCliente_cedula(timeOne.getMonthValue());
                    factura.setUsuario_cedula(timeOne.getYear());
                    setValuesServicios(facturaDAO.selectByDia(factura));
                    break;
                case "Rango":
                    if (timeTwo != null) {
                        if (timeOne.compareTo(timeTwo) > 0)
                            throw new Myexception("La segunda fecha no puede ser menor");
                        factura.setFecha_pago(Date.valueOf(timeOne));
                        factura.setDuracion(String.valueOf(Date.valueOf(timeTwo)));
                        setValuesServicios(facturaDAO.selectByRango(factura));
                    }
                    break;
                case "Mes":
                    factura.setCliente_cedula(timeOne.getMonthValue());
                    factura.setUsuario_cedula(timeOne.getYear());
                    setValuesServicios(facturaDAO.selectByMes(factura));
                    break;
            }
        }
    }

    private void setValuesServicios(List<Factura> facturaList) throws Myexception {
        if (facturaList.size() == 0)
            throw new Myexception("No hay registros");
        facturaList.forEach(it -> {
            int occurance = StringUtils.countMatches(it.getServicios(), ",") + 1;
            String arrPair[] = it.getServicios().split(",", occurance);
            for (int i = 0; i < occurance; i++) {
                String[] strings = arrPair[i].split("x", 2);
                if (servicios.get(strings[0].trim()) != null)
                    servicios.merge(strings[0].trim(), Integer.valueOf(strings[1].trim()), Integer::sum);
                else
                    servicios.put(strings[0].trim(), Integer.valueOf(strings[1].trim()));
            }
        });
    }

    private void setBarChart(HashMap<String, Integer> charts) {
        x.setLabel("Servicios");
        x.setTickLabelRotation(90);
        y.setLabel("Cantidad");
        // chartBar.setTitle("Servicios");

        charts.forEach((s, s2) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(s);
            series.getData().add(new XYChart.Data<>(s, s2));
            chartBar.getData().add(series);
        });
    }

    private void setPieChart(HashMap<String, Integer> charts) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(getPieChart(charts));
        chartPie.setTitle("Servicios");
        chartPie.setData(pieChartData);
    }

    private List<PieChart.Data> getPieChart(HashMap<String, Integer> charts) {
        List<PieChart.Data> datas = new ArrayList<>();
        charts.forEach((s, s2) -> {
            PieChart.Data data = new PieChart.Data(s, s2);
            datas.add(data);
        });
        return datas;
    }

    public void actionImprimir(ActionEvent actionEvent) {
        try {
            String graphPath = new File("reports").getAbsolutePath() + "\\chart.png";
            setGraphReport(graphPath, getNameFile(graphPath));
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private String getNameFile(String graphPath) throws IOException {
        File file = new File(graphPath);
        WritableImage snapshot = contentChart.snapshot(new SnapshotParameters(), null);
        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
        DateTime d = new DateTime();
        return d.getSecondOfDay() + " - " + d.getMinuteOfHour() + "-" + d.getHourOfDay() + "-" + d.getDayOfMonth() + d.getDayOfYear() + ".pdf";
    }

    private void setGraphReport(String graphPath, String nameFile) throws IOException, DocumentException {
        PDFCreator pdfCreator = new PDFCreator("reports/" + nameFile);
        pdfCreator.createPDF(documento -> {
            Paragraph elements = pdfCreator.setParagraph("Listado de graficos", Element.ALIGN_RIGHT, 10, 12, Font.BOLD);
            Paragraph elements1 = pdfCreator.setParagraph("Fecha: " + nameFile, Element.ALIGN_LEFT, 10, 12, Font.NORMAL);
            Image image = pdfCreator.setImagePDF("src/main/resources/images/FacturaLogo.png", 150, 100, Element.ALIGN_LEFT);

            PdfPTable tableTitle = pdfCreator.setTablePDF(new float[]{230, 290}, tabla -> {
                PdfPCell pdfPCellLeft = pdfCreator.setCellPDF(Element.ALIGN_TOP, Rectangle.NO_BORDER, image, elements1);
                PdfPCell pdfPCellRight = pdfCreator.setCellPDF(Element.ALIGN_TOP, Rectangle.NO_BORDER, elements);
                tabla.addCell(pdfPCellLeft);
                tabla.addCell(pdfPCellRight);
            }, false);
            documento.add(tableTitle);

            documento.add(pdfCreator.setImagePDF(graphPath, 500, 300, Element.ALIGN_TOP));
        });
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
