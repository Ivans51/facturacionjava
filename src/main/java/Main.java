import core.util.ManagerFXML;
import core.util.Route;
import javafx.application.Application;
import javafx.stage.Stage;

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
        ManagerFXML managerFXML = new ManagerFXML();
        managerFXML.abrirStage(Route.Login, "Inicio de Sesión", null, null);
    }
}