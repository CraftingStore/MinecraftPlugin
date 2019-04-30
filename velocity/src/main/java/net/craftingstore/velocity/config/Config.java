package net.craftingstore.velocity.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;

public class Config extends File {

    private JSONObject config;

    /**
     * Create a new instance of Config, specifying a file name and Plugin.
     *
     * @param name the file name, including .yml
     */
    public Config(File dir, String name) {
        super(dir, name);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!exists()) {
            try {
                if (getClass().getClassLoader().getResource(name) != null) {
                    Files.copy(getClass().getClassLoader().getResourceAsStream(name), toPath());
                } else {
                    createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JSONParser parser = new JSONParser();
        try {
            this.config = (JSONObject) parser.parse(new FileReader(this));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getConfig() {
        return config;
    }

    /**
     * Save all changes to the disk.
     */
    public void saveConfig() {
        try {
            PrintWriter pw = new PrintWriter(this);
            pw.write(this.config.toJSONString());
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
