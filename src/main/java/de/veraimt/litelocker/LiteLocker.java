package de.veraimt.litelocker;

import de.veraimt.litelocker.config.ConfigLoader;
import de.veraimt.litelocker.config.LiteLockerConfig;
import de.veraimt.litelocker.events.Events;
import de.veraimt.litelocker.platform.FabricPlatformHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LiteLocker implements ModInitializer {

    public static final String MOD_ID = "litelocker";
    public static final String MOD_NAME = "LiteLocker";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final FabricPlatformHelper PLATFORM = new FabricPlatformHelper();
    private static final ConfigLoader CONFIG_LOADER = new ConfigLoader();

    public static LiteLockerConfig config;
    private static MinecraftServer server;

    @Override
    public void onInitialize() {
        LOGGER.info("Loading {} on {}", MOD_NAME, PLATFORM.getPlatformName());

        config = CONFIG_LOADER.loadConfig();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            LiteLocker.server = server;
        });

        Events.registerEvents();
    }

    public static MinecraftServer getServer() {
        return server;
    }
}