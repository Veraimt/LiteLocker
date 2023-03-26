package de.veraimt.litelocker.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.veraimt.litelocker.LiteLocker;
import de.veraimt.litelocker.platform.services.IPlatformHelper;

import java.io.*;

import static de.veraimt.litelocker.LiteLocker.LOGGER;

public class ConfigLoader {

    public static final String CONFIG_FILE_NAME = LiteLocker.MOD_ID + ".json";
    private final File configFile = IPlatformHelper.PLATFORM.getConfigFile();

    public LiteLockerConfig loadConfig() {
        if(!configFile.exists()) {
            saveConfig(new LiteLockerConfig());
        }


        Gson gson = new Gson();
        LiteLockerConfig config = null;

        try {
            Reader reader = new BufferedReader(new FileReader(IPlatformHelper.PLATFORM.getConfigFile()));
            config = gson.fromJson(reader, LiteLockerConfig.class);
            reader.close();
        } catch (IOException e) {
            LOGGER.error("Couldn't load config!", e);
        }
        return config;
    }

    public void saveConfig(LiteLockerConfig config) {
        createFileIfNotExists();


        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        try(Writer writer = new FileWriter(IPlatformHelper.PLATFORM.getConfigFile())) {
            gson.toJson(config, writer);
            LOGGER.info("Successfully saved config!");

        } catch (IOException e) {
            LOGGER.error("Couldn't save config!", e);
        }
    }

    private void createFileIfNotExists() {
        if (!configFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
