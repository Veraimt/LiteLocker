package de.veraimt.litelocker.config;

import java.util.ArrayList;
import java.util.List;

public class LiteLockerConfig {
    private List<String> exclude = new ArrayList<>();
    private boolean enableAutoLocking = true;
    private List<String> autoLockingExclude = new ArrayList<>();
    private List<String> autoLockingInclude = new ArrayList<>();



    public List<String> getExclude() {
        return exclude;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
    }

    public boolean getEnableAutoLocking() {
        //TODO Activate when functional
        //return enableAutoLocking;
        return false;
    }

    public void setEnableAutoLocking(boolean enableAutoLocking) {
        this.enableAutoLocking = enableAutoLocking;
    }

    public List<String> getAutoLockingExclude() {
        return autoLockingExclude;
    }

    public void setAutoLockingExclude(List<String> autoLockingExclude) {
        this.autoLockingExclude = autoLockingExclude;
    }

    public List<String> getAutoLockingInclude() {
        return autoLockingInclude;
    }

    public void setAutoLockingInclude(List<String> autoLockingInclude) {
        this.autoLockingInclude = autoLockingInclude;
    }
}
