package Utils;

import java.io.File;
import java.io.IOException;

public class FileManager {

    public static FileCreationState createFile(String path, String name) throws IOException {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean dirCreated = dir.mkdirs();
            if (!dirCreated) {
                return FileCreationState.DIRECTORY_CREATION_FAILED;
            }
        }

        if (!dir.isDirectory()) {
            return FileCreationState.INVALID_PATH;
        }

        if (name == null || name.trim().isEmpty()) {

            Console.print("File created Successfully : " + dir.getAbsolutePath() , Console.Color.GREEN);

            return FileCreationState.DIRECTORY_CREATED_SUCCESSFULLY;
        }

        File file = new File(dir, name);

        if (file.exists()) {
            return FileCreationState.FILE_ALREADY_EXISTS;
        }

        boolean created = file.createNewFile();
        if (created) {
            return FileCreationState.FILE_CREATED_SUCCESSFULLY;
        } else {
            throw new IOException("Unknown error occurred while creating file.");
        }
    }
}
