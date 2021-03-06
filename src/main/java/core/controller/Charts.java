package core.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class Charts implements Initializable {

    public PieChart chartPie;
    public BarChart<String, Number> chartBar;
    public CategoryAxis x;
    public NumberAxis y;
    // public Label lblChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chartBar.getData().clear();
        setBarChart(Graficos.servicios);
        chartPie.getData().clear();
        setPieChart(Graficos.servicios);
    }

    private void setBarChart(HashMap<String, Integer> servicios) {
        x.setLabel("Servicios");
        x.setTickLabelRotation(90);
        y.setLabel("Cantidad");
        // chartBar.setTitle("Servicios");

        servicios.forEach((s, s2) -> {
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
        /*for (final PieChart.Data data : chartPie.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                        double total = 0;
                        for (PieChart.Data d : chartPie.getData())
                            total += d.getPieValue();
                        lblChart.setTranslateX(e.getSceneX());
                        lblChart.setTranslateY(e.getSceneY());
                        String text = String.format("%.1f%%", 100 * data.getPieValue() / total);
                        lblChart.setText(text);
                    }
            );
        }*/
    }

    private List<PieChart.Data> getPieChart(HashMap<String, Integer> charts) {
        List<PieChart.Data> datas = new ArrayList<>();
        charts.forEach((s, s2) -> {
            PieChart.Data data = new PieChart.Data(s, s2);
            datas.add(data);
        });
        return datas;
    }

}
