package de.veraimt.litelocker.platform;

import de.veraimt.litelocker.config.ConfigLoader;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class FabricPlatformHelper {

    public String getPlatformName() {
        return "Fabric";
    }

    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve(ConfigLoader.CONFIG_FILE_NAME).toFile();
    }
}