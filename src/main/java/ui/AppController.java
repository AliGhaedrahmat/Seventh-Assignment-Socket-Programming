package ui;

import Utils.Console;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import Shared.User;

import java.io.IOException;
import Socket.*;

public class AppController {

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private TextField portField;

    @FXML
    private Label server_ip;

    @FXML
    private Label server_port;

    @FXML
    private TextField joinserver_ip;

    @FXML
    private TextField joinserver_port;

    @FXML
    private TextField usernameField;

    @FXML
    public void initialize() {
        // Initialization code if needed
    }

    // Static helper method to load a scene from an FXML path using ActionEvent source
    public void loadScene(String scenePath, ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(scenePath));
        Parent root = loader.load();
        AppController controller = loader.getController();
        controller.setUser(this.user);
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void handleGoToConnectionScene(ActionEvent event) throws IOException {

        User user = new User(usernameField.getText() , "");
        this.user = user;

        loadScene("/ui/connection_scene.fxml", event);
    }

    @FXML
    public void handleCreateServer(ActionEvent event) throws IOException {
        loadScene("/ui/create_server.fxml", event);
    }

    @FXML
    public void handleCreateServer_PORTENTERED(ActionEvent event) throws IOException {
        String portText = portField.getText();
        try {
            int port = Integer.parseInt(portText);
            Server server = new Server(port);
            new Thread(server::run).start();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/server_hosting.fxml"));
            Parent root = loader.load();
            AppController newController = loader.getController();
            Platform.runLater(() -> {
                newController.setServerIp(server.getIp());
                newController.setServerPort(String.valueOf(port));
            });
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (NumberFormatException e) {
            System.out.println("Invalid port number");
        }
    }

    @FXML
    public void handleJoinServerGetIpAndPort(ActionEvent actionEvent) throws IOException {
        loadScene("/ui/join_server.fxml" , actionEvent);
    }

    @FXML
    public void handleJoinServer(ActionEvent event) throws IOException {
        String ip = joinserver_ip.getText();
        String portStr = joinserver_port.getText();
        try {
            Client client = new Client(ip , Integer.parseInt(portStr));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/chatScene.fxml"));
            Parent root = loader.load();

            ChatController chatController = loader.getController();


            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            chatController.setUser(user);
            chatController.setClient(client);

        } catch (NumberFormatException e) {
            Console.print("Invalid port number: " + e.getMessage(), Console.Color.RED);
        } catch (Exception e) {
            Console.print("Failed to connect: " + e.getMessage(), Console.Color.RED);
        }
    }


    public void setServerIp(String ip) {
        server_ip.setText(ip);
    }

    public void setServerPort(String port) {
        server_port.setText(port);
    }

}