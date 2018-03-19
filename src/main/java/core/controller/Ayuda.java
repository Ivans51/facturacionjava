package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Ayuda extends ManagerFXML implements Initializable {

    public JFXButton btnManual, btnBackup, btnSalir;
    public HBox successBackup;
    public Label lblMessageBackup;
    public AnchorPane anchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionBackup(ActionEvent actionEvent) {
        BackupBaseDato backupBaseDato = new BackupBaseDato("facturacion", "root", "", Ayuda.class);
        backupBaseDato.backupdbMsyql();
        new AlertUtil(Estado.EXITOSA, "Se creó el respaldo en un archivo .sql", closeAlert -> {
            cerrarStage(closeAlert);
            successBackup.setVisible(true);
            /*String value = "Se generó el respaldo correctamente, en la ruta: \n" +
                    "1) Abre su servidor y gestor de base de datos (XAMPP o WAMP)\n" +
                    "2) Seleccione \"Bases de datos\\\" y cree una con el de \"facturación\"\n" +
                    "3) Seleccione import y luego elija el archivo generado";*/
            String value = "Se generó el respaldo correctamente, en la ruta: " + backupBaseDato.getSavePath();
            lblMessageBackup.setText(value);
            /*try {
                Desktop.getDesktop().open(new File(backupBaseDato.getSavePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        });
    }

    public void actionManual(ActionEvent actionEvent) {
        if (Desktop.isDesktopSupported()) try {
            File myFile = new File("Manual.pdf");
            Desktop.getDesktop().open(myFile);
        } catch (IOException ex) {
            System.out.println("No se pudo crear");
        }
    }

    public void actionSalir(MouseEvent actionEvent) {
        cambiarEscena(Route.InicioInfo, anchorPane);
    }
}
