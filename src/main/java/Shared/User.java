package Shared;
import Utils.*;

import java.io.IOException;

public class User {
    private String username;
    private String password;

    public User(String username, String password) throws IOException {
        this.username = username;
        this.password = password;

        FileCreationState createUserDirectory = FileManager.createFile("src/main/resources/Client/" + username , "");

        String[] folders = {"Files"};
        for (String folder : folders) {
            FileCreationState createUserDirectoryInside = FileManager.createFile("src/main/resources/Client/" + username + "/" + folder , "");

        }

        Console.print("Creating user directory: " + createUserDirectory.name());
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }
}