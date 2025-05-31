package Shared;
import java.io.File;
import java.io.Serializable;

public class Message implements Serializable {
    String sender;
    String message;
    public SerializableFile file = null;

    public Message(String sender, String message, File file) {
        this.sender = sender;
        this.message = message;
        this.file = new SerializableFile(file);
    }

    public Message(String username, String msg) {
        this.sender = username;
        this.message = msg;
    }

    public Boolean hasFile() {
        return file != null;
    }

    public String getUsername() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getFileData() {
        return file.data;
    }
}
