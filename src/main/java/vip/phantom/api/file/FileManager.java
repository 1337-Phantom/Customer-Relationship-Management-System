package vip.phantom.api.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static final FileManager INSTANCE = new FileManager();

    @Getter
    private File rootDirectory;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private List<Savable> fileList = new ArrayList<>();

    public FileManager() {
        rootDirectory = new File(System.getenv("APPDATA"), "CustomerRelationshipManagement");
        if (!rootDirectory.exists()) {
            if (rootDirectory.mkdirs()) {
                System.out.println("Created directory.");
            } else {
                System.err.println("Couldn't create the directory. Please try again.");
            }
        }
    }

    public void addFile(Savable savable) {
        fileList.add(savable);
    }

    public void loadAllFiles() {
        for (Savable savable : fileList) {
            try {
                savable.loadFile();
            } catch (IOException e) {
                System.err.println("Error at File: " + savable.getFile().getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    public void saveAllFiles() {
        System.out.println("Saving all files.");
        fileList.forEach(Savable::saveFile);
    }
}
