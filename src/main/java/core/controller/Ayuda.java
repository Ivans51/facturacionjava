package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Ayuda extends ManagerFXML implements Initializable {
    public JFXButton btnBackup, btnPDF;
    public Label btnCerrar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionBackup(ActionEvent actionEvent) {
        BackupBaseDato backupBaseDato = new BackupBaseDato("facturacion", "root", "", Ayuda.class);
        backupBaseDato.backupdbtosql();
        new AlertUtil(Estado.EXITOSA, "Se creo el respaldo en un archivo .sql");
    }

    public void actionPDF(ActionEvent actionEvent) {
        if (Desktop.isDesktopSupported()) try {
            File myFile = new File("Manual.pdf");
            Desktop.getDesktop().open(myFile);
        } catch (IOException ex) {
            System.out.println("No se pudo crear");
        }
    }

    public void actionSalir(MouseEvent actionEvent) {
        abrirStage(Route.Inicio, "Home", btnCerrar, null);
    }
}
