package de.veraimt.litelocker.events;

public abstract class Events {

    public static void registerEvents() {
        InteractionEventsHandlers.register();
    }
}
