package de.veraimt.litelocker;

import de.veraimt.litelocker.platform.services.IPlatformHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiteLocker {

    public static final String MOD_ID = "litelocker";
    public static final String MOD_NAME = "LiteLocker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        LOGGER.info("Loading {} on {} Modding Platform", MOD_NAME, IPlatformHelper.PLATFORM.getPlatformName());

    }
}