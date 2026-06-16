package com.sxilverr.ftbquestsentityvis.client;

import com.sxilverr.ftbquestsentityvis.FTBQuestsEntityVisualization;
import dev.ftb.mods.ftblibrary.util.client.ClientTextComponentUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FTBQuestsEntityVisualization.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ClientTextComponentUtils.addCustomParser(new EntityComponentParser()));
    }
}
