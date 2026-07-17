package com.sxilverr.ftbquestsentityvis.fabric.client;

import com.sxilverr.ftbquestsentityvis.client.EntityComponentParser;
import dev.ftb.mods.ftblibrary.util.client.ClientTextComponentUtils;
import net.fabricmc.api.ClientModInitializer;

public final class FTBQuestsEntityVisualizationFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricClientConfig.init();
        ClientTextComponentUtils.addCustomParser(new EntityComponentParser());
    }
}
