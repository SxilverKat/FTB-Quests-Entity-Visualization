package com.sxilverr.ftbquestsentityvis.forge;

import com.sxilverr.ftbquestsentityvis.Config;
import net.minecraftforge.common.ForgeConfigSpec;

public final class ForgeClientConfig {
    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.BooleanValue MOBS_SPIN;
    private static final ForgeConfigSpec.DoubleValue SPIN_SPEED;
    private static final ForgeConfigSpec.DoubleValue TILT_DEGREES;
    private static final ForgeConfigSpec.BooleanValue FULL_BRIGHT;
    private static final ForgeConfigSpec.BooleanValue IDLE_ANIMATION;
    private static final ForgeConfigSpec.BooleanValue WALK_ANIMATION;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        MOBS_SPIN = builder.comment("Do mobs spin?").define("mobsSpin", true);
        SPIN_SPEED = builder.comment("Spin speed multiplier.").defineInRange("spinSpeed", 1.0, 0.0, 10.0);
        TILT_DEGREES = builder.comment("Camera tilt in degrees.").defineInRange("tiltDegrees", 15.0, -90.0, 90.0);
        FULL_BRIGHT = builder.comment("Render mobs at full brightness?").define("fullBright", true);
        IDLE_ANIMATION = builder.comment("Should mobs play idle animation?").define("idleAnimation", true);
        WALK_ANIMATION = builder.comment("Should mobs play walk animation?").define("walkAnimation", false);
        SPEC = builder.build();
    }

    private ForgeClientConfig() {
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
