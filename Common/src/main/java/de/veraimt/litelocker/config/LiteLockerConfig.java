package de.veraimt.litelocker.config;

import java.util.ArrayList;
import java.util.List;

public class LiteLockerConfig {
    private List<String> exclude = new ArrayList<>();
    private boolean enableAutoLocking = true;


    public List<String> getExclude() {
        return exclude;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
    }

    public boolean getEnableAutoLocking() {
        return enableAutoLocking;
    }

    public void setEnableAutoLocking(boolean enableAutoLocking) {
        this.enableAutoLocking = enableAutoLocking;
    }
    
}
