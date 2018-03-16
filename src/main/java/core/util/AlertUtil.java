package core.util;

import core.controller.AlertDialog;
import core.model.AlertModel;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class AlertUtil extends ManagerFXML {

    private int eleccion;
    private String title;
    private AlertDialog.Close close;

    public AlertUtil(int eleccion, String title) {
        this.eleccion = eleccion;
        this.title = title;
        dilog();
    }

    public AlertUtil(int eleccion, String title, AlertDialog.Close close) {
        this.eleccion = eleccion;
        this.title = title;
        this.close = close;
        dilog();
    }

    private void dilog() {
        abrirStageStyle(Route.DialogAlert, "", Modality.NONE, null,
                false, StageStyle.TRANSPARENT, this::elegir);
    }

    private void elegir() {
        AlertModel alertModel = new AlertModel(eleccion, title);
        AlertDialog dialog = ManagerFXML.getFxmlLoader().getController();
        dialog.initData(alertModel, close);
    }
}