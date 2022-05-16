package vip.phantom.api.file;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;

public abstract class Savable {

    public abstract boolean loadFile() throws IOException;

    public abstract boolean saveFile();

    public abstract File getFile();

    public File checkFile(String filename) {
        File file = new File(FileManager.INSTANCE.getRootDirectory(), filename);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.err.println("Couldn't create file: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public JsonObject readFile() throws IOException {
        if (!getFile().exists()) {
            saveFile();
        }
        final StringBuilder stringBuilder = new StringBuilder();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(getFile()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        return getGson().fromJson(stringBuilder.toString(), JsonObject.class);
    }

    public boolean writeFile(JsonObject jsonObject) {
        final BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(getFile()));
            bufferedWriter.write(getGson().toJson(jsonObject));
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Gson getGson() {
        return FileManager.GSON;
    }

}
