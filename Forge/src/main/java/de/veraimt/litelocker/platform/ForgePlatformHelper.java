package de.veraimt.litelocker.platform;

import de.veraimt.litelocker.config.ConfigLoader;
import de.veraimt.litelocker.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public File getConfigFile() {
        return FMLPaths.GAMEDIR.get().resolve(ConfigLoader.CONFIG_FILE_NAME).toFile();
    }
}
