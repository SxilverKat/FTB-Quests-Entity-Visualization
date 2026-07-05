package com.sxilverr.ftbquestsentityvis.neoforge;

import com.sxilverr.ftbquestsentityvis.Config;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class NeoForgeClientConfig {
    public static final ModConfigSpec SPEC;

    private static final ModConfigSpec.BooleanValue MOBS_SPIN;
    private static final ModConfigSpec.DoubleValue SPIN_SPEED;
    private static final ModConfigSpec.DoubleValue TILT_DEGREES;
    private static final ModConfigSpec.BooleanValue FULL_BRIGHT;
    private static final ModConfigSpec.BooleanValue IDLE_ANIMATION;
    private static final ModConfigSpec.BooleanValue WALK_ANIMATION;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        MOBS_SPIN = builder.comment("Do mobs spin?").define("mobsSpin", true);
        SPIN_SPEED = builder.comment("Spin speed multiplier.").defineInRange("spinSpeed", 1.0, 0.0, 10.0);
        TILT_DEGREES = builder.comment("Camera tilt in degrees.").defineInRange("tiltDegrees", 15.0, -90.0, 90.0);
        FULL_BRIGHT = builder.comment("Render mobs at full brightness?").define("fullBright", true);
        IDLE_ANIMATION = builder.comment("Should mobs play idle animation?").define("idleAnimation", true);
        WALK_ANIMATION = builder.comment("Should mobs play walk animation?").define("walkAnimation", false);
        SPEC = builder.build();
    }

    private NeoForgeClientConfig() {
    }

    public static void sync() {
        Config.mobsSpin = MOBS_SPIN.get();
        Config.spinSpeed = SPIN_SPEED.get();
        Config.tiltDegrees = TILT_DEGREES.get();
        Config.fullBright = FULL_BRIGHT.get();
        Config.idleAnimation = IDLE_ANIMATION.get();
        Config.walkAnimation = WALK_ANIMATION.get();
    }
}
