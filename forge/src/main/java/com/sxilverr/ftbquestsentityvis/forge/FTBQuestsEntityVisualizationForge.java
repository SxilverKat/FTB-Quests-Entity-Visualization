package com.sxilverr.ftbquestsentityvis.forge;

import com.sxilverr.ftbquestsentityvis.FTBQuestsEntityVisualization;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FTBQuestsEntityVisualization.MODID)
public final class FTBQuestsEntityVisualizationForge {
    public FTBQuestsEntityVisualizationForge() {
        FTBQuestsEntityVisualization.init();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onConfig);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeClientConfig.SPEC, FTBQuestsEntityVisualization.MODID + "/client.toml");
    }

    private void onConfig(ModConfigEvent event) {
        if (event.getConfig().getSpec() == ForgeClientConfig.SPEC) {
            ForgeClientConfig.sync();
        }
    }
}
