package Shared;

import Utils.Console;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

public class SerializableFile implements Serializable {
    String filename;
    byte[] data;

    public SerializableFile(File file)  {
        this.filename = file.getName();
        try {
            this.data = Files.readAllBytes(file.toPath());

        }
        catch (IOException e) {
            Console.print("Error in reading file" , Console.Color.RED);
        }
    }


    public String getFilename() {
        return filename;
    }


}