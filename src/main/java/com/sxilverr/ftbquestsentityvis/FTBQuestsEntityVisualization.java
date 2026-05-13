package com.sxilverr.ftbquestsentityvis;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(FTBQuestsEntityVisualization.MODID)
public class FTBQuestsEntityVisualization {
    public static final String MODID = "ftbquestsentityvis";

    public FTBQuestsEntityVisualization(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC, "ftb-quests-entity-vis/client.toml");
    }
}
