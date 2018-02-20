package core.controller;

import core.model.AlertModel;
import core.util.Estado;
import core.util.ManagerFXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AlertDialog extends ManagerFXML implements Initializable {

    public Label descripcionAlert;
    public Label closeAlert;
    private Close close;
    // public ImageView imagenAlert;

    public void initData(AlertModel alertModel, Close close) {
        elegirMensaje(alertModel.getEleccion(), alertModel.getTitle());
        this.close = close;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void elegirMensaje(int eleccion, String title) {
        if (eleccion == Estado.EXITOSA)
            setInfoAlert(title, "../icons/DeleteMessage_96px.png");
        else if (eleccion == Estado.ERROR)
            setInfoAlert(title, "../icons/Ok_96px.png");
    }

    private void setInfoAlert(String value, String path) {
        descripcionAlert.setText(value);
        // imagenAlert.setImage(getImageFile(path));
    }

    private Image getImageFile(String path) {
        File imageFile = new File(path);
        return new Image(imageFile.toURI().toString());
    }

    public void closeAlert(MouseEvent mouseEvent) {
        if (close != null)
            close.stage(closeAlert);
        else
            cerrarStage(closeAlert);
    }

    public interface Close {
        void stage(Label closeAlert);
    }
}