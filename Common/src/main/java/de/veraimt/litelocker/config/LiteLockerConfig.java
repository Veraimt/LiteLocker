package de.veraimt.litelocker.config;


public class LiteLockerConfig {
    private boolean enableAutoLocking = true;

    private boolean canRemoveSignCreator = false;

    public boolean getEnableAutoLocking() {
        return enableAutoLocking;
    }

    public void setEnableAutoLocking(boolean enableAutoLocking) {
        this.enableAutoLocking = enableAutoLocking;
    }

    public boolean getCanRemoveSignCreator() {
        return canRemoveSignCreator;
    }

    public void setCanRemoveSignCreator(boolean canRemoveSignCreator) {
        this.canRemoveSignCreator = canRemoveSignCreator;
    }
}
