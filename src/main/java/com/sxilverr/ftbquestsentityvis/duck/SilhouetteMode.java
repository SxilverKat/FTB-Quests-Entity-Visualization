package com.sxilverr.ftbquestsentityvis.duck;

public enum SilhouetteMode {
    NONE,
    UNTIL_AVAILABLE,
    UNTIL_COMPLETED;

    public static SilhouetteMode fromName(String name) {
        try {
            return SilhouetteMode.valueOf(name);
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }
}
