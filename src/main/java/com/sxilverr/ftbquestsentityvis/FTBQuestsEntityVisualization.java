package com.sxilverr.ftbquestsentityvis;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(FTBQuestsEntityVisualization.MODID)
public class FTBQuestsEntityVisualization {
    public static final String MODID = "ftbquestsentityvis";

    public FTBQuestsEntityVisualization() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC, "ftb-quests-entity-vis/client.toml");
    }
}
