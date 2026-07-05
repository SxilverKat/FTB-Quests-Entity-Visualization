package com.sxilverr.ftbquestsentityvis;

import net.minecraft.resources.ResourceLocation;

public final class ModUtil {
    private ModUtil() {
    }

    public static ResourceLocation rl(String id) {
        //? if >=1.21.1 {
        /*return ResourceLocation.parse(id);*/
        //?} else {
        return new ResourceLocation(id);
        //?}
    }
}
