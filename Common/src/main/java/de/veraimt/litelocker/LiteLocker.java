package de.veraimt.litelocker;

import de.veraimt.litelocker.config.ConfigLoader;
import de.veraimt.litelocker.config.LiteLockerConfig;
import de.veraimt.litelocker.platform.services.IPlatformHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LiteLocker {

    public static final String MOD_ID = "litelocker";
    public static final String MOD_NAME = "LiteLocker";
    public static final Logger LOGGER = LogManager.getLogger();
    private static final IPlatformHelper PLATFORM = IPlatformHelper.PLATFORM;
    private static final ConfigLoader CONFIG_LOADER = new ConfigLoader();

    public static LiteLockerConfig config;

    public static void init() {
        LOGGER.info("Loading {} on {} Modding Platform", MOD_NAME, PLATFORM.getPlatformName());

        config = CONFIG_LOADER.loadConfig();
    }
}