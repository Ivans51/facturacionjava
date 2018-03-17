package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.ManagerFXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Graficos extends ManagerFXML implements Initializable{


    public JFXButton btnImprimir, btnSalir, btnConsultar;
    public AnchorPane anchorPane;
    public ComboBox<String> cReportes, cTime;
    public DatePicker datePickerUno, datePickerDos;
    public PieChart chartPie;
    public BarChart barChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionImprimir(ActionEvent actionEvent) {

    }

    public void actionSalir(ActionEvent actionEvent) {

    }

    public void actionConsultar(ActionEvent actionEvent) {

    }
}
