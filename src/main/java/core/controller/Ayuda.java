package core.controller;

import com.jfoenix.controls.JFXButton;
import core.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

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

    private Desktop desktop = Desktop.getDesktop();
    private BackupBaseDato backupBaseDato = new BackupBaseDato("facturacion", "root", "", Ayuda.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void actionBackup(ActionEvent actionEvent) {
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

    public void actionOpenFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        // Set Initial Directory to Desktop
        String pathname = System.getProperty("user.home") + "\\Desktop";
        fileChooser.setInitialDirectory(new File(backupBaseDato.getFolderPath()));

        // Set extension filter, only PDF files will be shown
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("sql files (*.sql)", "*.sql");
        fileChooser.getExtensionFilters().add(extFilter);
        // fileChooser.getSelectedFile().getAbsolutePath()

        // Show open file dialog
        File file = fileChooser.showOpenDialog(Storage.getStage());
        if (file != null) {
            // desktop.open(file);
            if (backupBaseDato.restoreDB("facturacion", "root", "", file.getAbsolutePath()))
                new AlertUtil(Estado.EXITOSA, "Base de datos restaurada");
        }
    }
}
