package com.sxilverr.ftbquestsentityvis.fabric;

import com.sxilverr.ftbquestsentityvis.FTBQuestsEntityVisualization;
import net.fabricmc.api.ModInitializer;

public final class FTBQuestsEntityVisualizationFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FTBQuestsEntityVisualization.init();
    }
}
