package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    String title = "SocketChat App";

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/main_scene.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("SocketChat App");
        stage.setScene(scene);
        stage.show();
    }

    public static void run() {
        launch();
    }
}
