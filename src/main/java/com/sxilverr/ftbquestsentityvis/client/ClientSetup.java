package com.sxilverr.ftbquestsentityvis.client;

import com.sxilverr.ftbquestsentityvis.FTBQuestsEntityVisualization;
import dev.ftb.mods.ftblibrary.util.client.ClientTextComponentUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = FTBQuestsEntityVisualization.MODID, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ClientTextComponentUtils.addCustomParser(new EntityComponentParser()));
    }
}
