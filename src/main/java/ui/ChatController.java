package ui;

import Shared.Message;
import Shared.User;
import Socket.Client;
import Utils.Console;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
public class ChatController {
    private User user;
    private Client client;


    @FXML
    private TextField messageField;


    @FXML
    private VBox chatVBox;






    public void showTemporaryMessage(String message) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initStyle(StageStyle.UNDECORATED); // no window borders

        double width = 300;
        double height = 400;
        popup.setWidth(width);
        popup.setHeight(height);

        Label label = new Label(message);
        label.setPrefHeight(height * 0.1);  // 10% of height
        label.setPrefWidth(width);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popup.close());

        VBox root = new VBox(20, label, closeButton);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-border-radius: 10; -fx-background-radius: 10;");

        Scene scene = new Scene(root, width, height);
        popup.setScene(scene);
        popup.centerOnScreen();
        popup.show();
    }




    public void addMessage(String username, String message, String date) {
        HBox messageBox = new HBox(5); // spacing=5

        Label usernameLabel = new Label(username);
        usernameLabel.setTextFill(Color.web("#007691"));  // Changed color to #007691

        Label messageLabel = new Label(message);

        Label dateLabel = new Label(date);
        dateLabel.setContentDisplay(ContentDisplay.BOTTOM);
        dateLabel.setTextAlignment(TextAlignment.CENTER);
        dateLabel.setTextFill(Color.web("#7e7e7e"));
        dateLabel.setFont(new Font(10));
        HBox.setMargin(dateLabel, new Insets(1.5, 0, 0, 0));

        messageBox.getChildren().addAll(usernameLabel, messageLabel, dateLabel);

        messageBox.setPadding(new Insets(5, 5, 5, 5)); // padding 5 on all sides

        chatVBox.getChildren().add(messageBox);
    }

    public void addFileMessage(String username, String fileName, String fileSizeText, String date , Message socketMessage) {
        HBox messageBox = new HBox(10);
        messageBox.setPadding(new Insets(5));
        messageBox.setAlignment(Pos.CENTER_LEFT);
        Label usernameLabel = new Label(username);
        usernameLabel.setTextFill(Color.web("#007691"));
        usernameLabel.setMinWidth(100);
        usernameLabel.setMaxWidth(100);
        VBox fileDetails = new VBox(2);
        fileDetails.setAlignment(Pos.CENTER_LEFT);

        Label fileNameLabel = new Label(fileName);
        fileNameLabel.setFont(Font.font(13));

        HBox sizeBox = new HBox(2);
        Label sizeValue = new Label(fileSizeText);
        sizeValue.setFont(Font.font(10));
        sizeValue.setStyle("-fx-text-fill: gray;");
        Label sizeUnit = new Label("");
        sizeUnit.setFont(Font.font(11));
        sizeUnit.setStyle("-fx-text-fill: gray;");
        sizeBox.getChildren().addAll(sizeValue, sizeUnit);

        fileDetails.getChildren().addAll(fileNameLabel, sizeBox);
        Button downloadButton = new Button("Download");
        downloadButton.setFont(Font.font(11));
        downloadButton.setStyle("-fx-background-radius: 5;");
        downloadButton.setOnAction(event -> {
            handleDownloadFile(event, socketMessage);
        });
        HBox fileBox = new HBox(10);
        fileBox.setAlignment(Pos.CENTER_LEFT);
        fileBox.getChildren().addAll(fileDetails, downloadButton);
        fileBox.setMinWidth(250);
        fileBox.setMaxWidth(250);
        Label dateLabel = new Label(date);
        dateLabel.setTextFill(Color.web("#7e7e7e"));
        dateLabel.setFont(Font.font(10));
        dateLabel.setAlignment(Pos.CENTER_LEFT);
        dateLabel.setMinWidth(60);
        dateLabel.setMaxWidth(60);
        HBox.setMargin(dateLabel, new Insets(1.5, 0, 0, 0));
        messageBox.getChildren().addAll(usernameLabel, fileBox, dateLabel);

        chatVBox.getChildren().add(messageBox);
    }


    public static String getCurrentDateAsString() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return formatter.format(date);
    }


    public void setClient(Client client) {
        this.client = client;

        Console.print("Setting client", Console.Color.GREEN);


        client.setMessageHandler(obj -> {
            Message message = null;

            try {
                message = (Message) obj;
            } catch (Exception e) {
                Console.print("Error getting message in setclient chat controller", Console.Color.RED);
            }


            if (message != null) {
                Message finalMessage = message;
                Platform.runLater(() -> {
                    if (finalMessage.hasFile()) {
                        addFileMessage(finalMessage.getUsername(), finalMessage.file.getFilename(), "", ChatController.getCurrentDateAsString() , finalMessage);
                    }
                    else {
                        addMessage(finalMessage.getUsername(), finalMessage.getMessage(), ChatController.getCurrentDateAsString());
                    }
                });
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void handleSendMessage(ActionEvent actionEvent) {
        Console.print("Button send clicked!" + "\n");
        String msg = messageField.getText();
        if (msg != null && !msg.isEmpty()) {
            Console.print("message sent" + msg + "\n");
            client.sendObject(new Message(user.getUsername(), msg));

            messageField.clear();
        }
    }

    @FXML
    public void handleUploadFile(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();

        File initialDir = new File(System.getProperty("user.dir"));
        if (initialDir.exists()) {
            fileChooser.setInitialDirectory(initialDir);
        }

        fileChooser.setTitle("Choose File to Send");
        File file = fileChooser.showOpenDialog(messageField.getScene().getWindow());

        if (file != null) {
            try {
                Message message = new Message(user.getUsername(), "File", file);
                client.sendObject(message);
            } catch (Exception e) {
                Console.print("Error reading file: " + e.getMessage(), Console.Color.RED);
            }
        }
    }



    @FXML
    public void handleDownloadFile(ActionEvent actionEvent, Message socketMessage) {
        String projectRoot = System.getProperty("user.dir");
        String fixedDirectoryPath = projectRoot + File.separator + "src/main/resources/Client/" + socketMessage.getUsername() + "/Files";
        File fixedDir = new File(fixedDirectoryPath);
        if (!fixedDir.exists()) {
            boolean created = fixedDir.mkdirs();
            if (!created) {
                Console.print("Failed to create directory: " + fixedDirectoryPath, Console.Color.RED);
                return;
            }
        }
        File saveFile = new File(fixedDir, socketMessage.file.getFilename());

        try {
            byte[] fileData = socketMessage.getFileData();

            try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                fos.write(fileData);
            }
            showTemporaryMessage("File Downloaded");
            Console.print("File saved successfully to " + saveFile.getAbsolutePath(), Console.Color.GREEN);

        } catch (Exception e) {
            Console.print("Error saving file: " + e.getMessage(), Console.Color.RED);
        }
    }



}
