package com.sxilverr.ftbquestsentityvis.client;

import dev.ftb.mods.ftblibrary.util.CustomComponentParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Map;

@Environment(EnvType.CLIENT)
public final class EntityComponentParser implements CustomComponentParser {
    @Override
    public Component parse(String string, Map<String, String> properties) {
        if (!properties.containsKey("entity")) {
            return null;
        }
        return MutableComponent.create(EntityComponent.fromProperties(properties));
    }
}
