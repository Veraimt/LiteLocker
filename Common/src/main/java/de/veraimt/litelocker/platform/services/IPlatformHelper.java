package de.veraimt.litelocker.platform.services;

import java.io.File;
import java.util.ServiceLoader;

import static de.veraimt.litelocker.LiteLocker.LOGGER;

public interface IPlatformHelper {

    IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    private static <T> T load(@SuppressWarnings("SameParameterValue") Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    File getConfigFile();
}
