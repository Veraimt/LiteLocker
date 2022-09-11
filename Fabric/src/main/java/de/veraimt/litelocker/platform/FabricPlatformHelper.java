package de.veraimt.litelocker.platform;

import de.veraimt.litelocker.config.ConfigLoader;
import de.veraimt.litelocker.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve(ConfigLoader.CONFIG_FILE_NAME).toFile();
    }
}
