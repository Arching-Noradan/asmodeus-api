package space.technological;

import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class Config {
    public static Config loadConfig() {
        try {
            String config_file = FileUtils.readFileToString(new File("./config.json"));
            Config config = new GsonBuilder().create().fromJson(config_file, Config.class);
            return config;
        } catch (Exception a) {
            a.printStackTrace();
            System.err.println("ERROR! Unable to load config. Exiting");
            System.exit(0);
        }
        return new Config();
    }

    public String databaseHost;
    public int databasePort;
    public String databaseName;
    public String databaseUser;
    public String databasePassword;
    public String mailHost;
    public String mailUsername;
    public String mailPassword;
    public String mailSubject;
}
