package com.sxilverr.ftbquestsentityvis.neoforge.client;

import com.sxilverr.ftbquestsentityvis.FTBQuestsEntityVisualization;
import com.sxilverr.ftbquestsentityvis.client.EntityComponentParser;
import dev.ftb.mods.ftblibrary.util.client.ClientTextComponentUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = FTBQuestsEntityVisualization.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class NeoForgeClientSetup {
    private NeoForgeClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ClientTextComponentUtils.addCustomParser(new EntityComponentParser()));
    }
}
