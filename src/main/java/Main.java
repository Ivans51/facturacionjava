import core.util.ManagerFXML;
import core.util.Route;
import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new ManagerFXML().abrirStageStyle(Route.DialogLogin, "Inicio de Sesión", Modality.NONE,
                null, false, StageStyle.TRANSPARENT, null);
    }
}