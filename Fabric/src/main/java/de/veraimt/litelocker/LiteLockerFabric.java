package de.veraimt.litelocker;

import net.fabricmc.api.ModInitializer;

public class LiteLockerFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        LiteLocker.init();
    }
}
