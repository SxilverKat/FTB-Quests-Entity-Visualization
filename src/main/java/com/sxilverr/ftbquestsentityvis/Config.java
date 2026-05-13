package com.sxilverr.ftbquestsentityvis;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    public static final ModConfigSpec CLIENT_SPEC;

    public static final ModConfigSpec.BooleanValue MOBS_SPIN;
    public static final ModConfigSpec.DoubleValue SPIN_SPEED;
    public static final ModConfigSpec.DoubleValue TILT_DEGREES;
    public static final ModConfigSpec.BooleanValue FULL_BRIGHT;
    public static final ModConfigSpec.BooleanValue IDLE_ANIMATION;
    public static final ModConfigSpec.BooleanValue WALK_ANIMATION;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        MOBS_SPIN = builder.comment("Do mobs spin?").define("mobsSpin", true);
        SPIN_SPEED = builder.comment("Spin speed multiplier.").defineInRange("spinSpeed", 1.0, 0.0, 10.0);
        TILT_DEGREES = builder.comment("Camera tilt in degrees.").defineInRange("tiltDegrees", 15.0, -90.0, 90.0);
        FULL_BRIGHT = builder.comment("Render mobs at full brightness?").define("fullBright", true);
        IDLE_ANIMATION = builder.comment("Should mobs play idle animation?").define("idleAnimation", true);
        WALK_ANIMATION = builder.comment("Should mobs play walk animation?").define("walkAnimation", false);
        CLIENT_SPEC = builder.build();
    }

    private Config() {
    }
}
