package com.sxilverr.ftbquestsentityvis.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class QuestSizeContext {
    private static final ThreadLocal<Float> OVERRIDE = new ThreadLocal<>();

    private QuestSizeContext() {
    }

    public static void push(float size) {
        OVERRIDE.set(size);
    }

    public static void pop() {
        OVERRIDE.remove();
    }

    public static float resolveSize(float taskSize) {
        Float v = OVERRIDE.get();
        return v != null ? v.floatValue() : taskSize;
    }
}
