package com.sxilverr.ftbquestsentityvis.neoforge;

import com.sxilverr.ftbquestsentityvis.FTBQuestsEntityVisualization;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

@Mod(FTBQuestsEntityVisualization.MODID)
public final class FTBQuestsEntityVisualizationNeoForge {
    public FTBQuestsEntityVisualizationNeoForge(IEventBus modBus, ModContainer container) {
        FTBQuestsEntityVisualization.init();
        container.registerConfig(ModConfig.Type.CLIENT, NeoForgeClientConfig.SPEC, "ftb-quests-entity-vis/client.toml");
        modBus.addListener(this::onConfig);
    }

    private void onConfig(ModConfigEvent event) {
        if (event.getConfig().getSpec() == NeoForgeClientConfig.SPEC) {
            NeoForgeClientConfig.sync();
        }
    }
}
