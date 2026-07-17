package com.sxilverr.ftbquestsentityvis.fabric.client;

import com.sxilverr.ftbquestsentityvis.Config;
import com.sxilverr.ftbquestsentityvis.FTBQuestsEntityVisualization;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.world.InteractionResult;

@me.shedaniel.autoconfig.annotation.Config(name = FTBQuestsEntityVisualization.MODID)
public final class FabricClientConfig implements ConfigData {
    public boolean mobsSpin = true;

    @ConfigEntry.Gui.Tooltip
    public double spinSpeed = 1.0;

    @ConfigEntry.Gui.Tooltip
    public double tiltDegrees = 15.0;

    public boolean fullBright = true;
    public boolean idleAnimation = true;
    public boolean walkAnimation = false;

    public static void init() {
        AutoConfig.register(FabricClientConfig.class, GsonConfigSerializer::new);
        ConfigHolder<FabricClientConfig> holder = AutoConfig.getConfigHolder(FabricClientConfig.class);
        holder.registerSaveListener((h, config) -> {
            apply(config);
            return InteractionResult.PASS;
        });
        holder.registerLoadListener((h, config) -> {
            apply(config);
            return InteractionResult.PASS;
        });
        apply(holder.getConfig());
    }

    private static void apply(FabricClientConfig config) {
        Config.mobsSpin = config.mobsSpin;
        Config.spinSpeed = config.spinSpeed;
        Config.tiltDegrees = config.tiltDegrees;
        Config.fullBright = config.fullBright;
        Config.idleAnimation = config.idleAnimation;
        Config.walkAnimation = config.walkAnimation;
    }
}
