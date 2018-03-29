package core.controller;

import core.conexion.MyBatisConnection;
import core.dao.UsuarioDAO;
import core.util.ManagerFXML;
import core.util.Route;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class DialogLogin extends ManagerFXML implements Initializable {

    public ProgressIndicator indicatorConnect;
    public AnchorPane anchorPane;
    private UsuarioDAO usuarioDao = new UsuarioDAO(MyBatisConnection.getSqlSessionFactory());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task task = taskWorker();
        indicatorConnect.progressProperty().unbind();
        indicatorConnect.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(event -> {
            abrirStage(Route.Login, "Inversiones Todo Frio C.A.", indicatorConnect, null);
        });
        new Thread(() -> usuarioDao.selectStart()).start();
        new Thread(task).start();
    }

    private Task taskWorker() {
        int seconds = 15;
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < seconds; i++) {
                    // updateProgress(i+1, seconds);
                    Thread.sleep(1000);
                }
                return true;
            }
        };
    }
}
