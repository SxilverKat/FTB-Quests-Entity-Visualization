package com.sxilverr.ftbquestsentityvis.forge.client;

import com.sxilverr.ftbquestsentityvis.FTBQuestsEntityVisualization;
import com.sxilverr.ftbquestsentityvis.client.EntityComponentParser;
import dev.ftb.mods.ftblibrary.util.client.ClientTextComponentUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FTBQuestsEntityVisualization.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ForgeClientSetup {
    private ForgeClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ClientTextComponentUtils.addCustomParser(new EntityComponentParser()));
    }
}
