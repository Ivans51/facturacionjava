package core.controller;

import com.jfoenix.controls.JFXButton;
import core.conexion.MyBatisConnection;
import core.dao.*;
import core.util.*;
import core.vo.Factura;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Graficos extends ManagerFXML implements Initializable {

    public JFXButton btnSalir, btnConsultar;
    public AnchorPane anchorPane;
    public ComboBox<String> cTime;
    public DatePicker datePickerUno, datePickerDos;

    public PieChart chartPie;
    public BarChart<String, Number> chartBar;
    public CategoryAxis x;
    public NumberAxis y;

    private AuditoriaDAO auditoriaDAO = new AuditoriaDAO(MyBatisConnection.getSqlSessionFactory());
    private ServiciosDAO serviciosDAO = new ServiciosDAO(MyBatisConnection.getSqlSessionFactory());
    private ClienteDAO clienteDAO = new ClienteDAO(MyBatisConnection.getSqlSessionFactory());
    private FacturaDAO facturaDAO = new FacturaDAO(MyBatisConnection.getSqlSessionFactory());
    private UsuarioDAO usuarioDAO = new UsuarioDAO(MyBatisConnection.getSqlSessionFactory());
    private String comboReportes = "";
    private String[] rangoTiempo = {"Dia", "Mes", "Rango"};
    private String[] clientesA = {"Cédula", "Nombres", "Apellidos", "Direccion", "Teléfono"};
    private String[] usuariosA = {"Cédula", "Nombre", "Correo", "Fecha", "Status"};
    private String[] facturasA = {"IdFactura", "Servicios", "FechaPago", "IVA", "Total"};
    private String[] serviciosA = {"Id", "Nombre", "Precio", "Fecha", "TiempoE"};
    private String[] auditoriasA = {"Id", "Fecha", "Hora", "Accion", "Usuario"};
    private List<String> tipos = new ArrayList<>();
    private String comboTime = "Dia";
    private HashMap<String, Double> servicios = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePickerDos.setVisible(false);
        stateButton();
        setCombo();
        // setDatePicker();
    }

    private void stateButton() {
        // Deshabilitar botones
        switch (Storage.getUsuario().getStatus()) {
            case Estado.TECNICO:
                tipos.add("Factura");
                comboReportes = "Factura";
                // lblTotal.setVisible(true);
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
                //lblTotal.setVisible(true);
                break;
        }
    }

    private void setCombo() {
        cTime.setItems(FXCollections.observableArrayList(rangoTiempo));
        cTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            comboTime = newValue;
            datePickerDos.setVisible(newValue.equals("Rango"));
            // setTableFacturaTime(newValue);
        });
    }

    private void setDatePicker() {
        datePickerUno.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("Dia".equals(comboTime) || "Mes".equals(comboTime))
                setTableFacturaTime();
        });
    }

    public void actionConsultar(ActionEvent actionEvent) {
        if (comboTime != null && !"".equals(comboTime) && datePickerUno.getValue() != null) {
            servicios.clear();
            chartBar.getData().clear();
            chartPie.getData().clear();
            setTableFacturaTime();
            setBarChart("Servicios", "Cantidad", "Servicios", "", servicios);
            setPieChart("Servicios", servicios);
        } else
            new AlertUtil(Estado.EXITOSA, "Faltan valores por seleccionar");
    }

    private void setTableFacturaTime() {
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
                        factura.setFecha_pago(Date.valueOf(timeOne));
                        factura.setDuracion(String.valueOf(Date.valueOf(timeTwo)));
                        setValuesServicios(facturaDAO.selectByDia(factura));
                    }
                    break;
                case "Mes":
                    factura.setCliente_cedula(timeOne.getMonthValue());
                    factura.setUsuario_cedula(timeOne.getYear());
                    setValuesServicios(facturaDAO.selectByDia(factura));
                    break;
            }
            /*lblTotal.setVisible(true);
            lblTotal.setText("Totales: " + String.format("%1$,.2f", totales));*/
        }
    }

    private void setValuesServicios(List<Factura> facturaList) {
        facturaList.forEach(it -> {
            int occurance = StringUtils.countMatches(it.getServicios(), ",") + 1;
            String arrPair[] = it.getServicios().split(",", occurance);
            for (int i = 0; i < occurance; i++) {
                String[] strings = arrPair[i].split("x", 2);
                Double value = Double.valueOf(strings[1]);
                if (servicios.get(strings[0]) != null)
                    servicios.merge(strings[0], servicios.get(strings[0]) + value, Double::sum);
                else
                    servicios.put(strings[0], value);
            }
        });
    }

    private void setBarChart(String xAxis, String yAxis, String title, String sub, HashMap<String, Double> charts) {
        x.setLabel(xAxis);
        y.setLabel(yAxis);
        chartBar.setTitle(title);
        // series.setName(sub);

        XYChart.Series<String, Number> series = new XYChart.Series();
        /*series.nodeProperty().addListener((ObservableValue<? extends Node> ov, Node oldNode, Node newNode) -> {
            Random random = new Random();
            int nextInt = random.nextInt(256*256*256);
            newNode.setStyle("-fx-bar-fill: " + String.format("#%06x", nextInt));
        });*/
        charts.forEach((s, s2) -> {
            series.getData().add(new XYChart.Data(s, s2));
        });
        chartBar.getData().addAll(series);
    }

    private void setPieChart(String title, HashMap<String, Double> charts) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(getPieChart(charts));
        chartPie.setTitle(title);
        chartPie.setData(pieChartData);
    }

    private List<PieChart.Data> getPieChart(HashMap<String, Double> charts) {
        List<PieChart.Data> datas = new ArrayList<>();
        charts.forEach((s, s2) -> {
            PieChart.Data data = new PieChart.Data(s, s2);
            datas.add(data);
        });
        return datas;
    }

    public void actionImprimir(ActionEvent actionEvent) {
        PrintSelection printSelection = new PrintSelection(comboReportes, auditoriasA, serviciosA, clientesA, facturasA, usuariosA);
        printSelection.printAction((strings, path, name) -> {

        });
    }

    public void actionSalir(ActionEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
