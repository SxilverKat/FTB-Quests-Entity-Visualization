package com.sxilverr.ftbquestsentityvis.duck;

public enum OverrideMode {
    USE_GLOBAL,
    FORCE_ON,
    FORCE_OFF;

    public boolean resolve(boolean globalValue) {
        return switch (this) {
            case USE_GLOBAL -> globalValue;
            case FORCE_ON -> true;
            case FORCE_OFF -> false;
        };
    }

    public static OverrideMode fromName(String name) {
        try {
            return OverrideMode.valueOf(name);
        } catch (IllegalArgumentException e) {
            return USE_GLOBAL;
        }
    }
}
