package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.EntityIcon;
import com.sxilverr.ftbquestsentityvis.duck.IKillTaskVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.task.KillTask;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KillTask.class)
public abstract class KillTaskClientMixin {
    @Shadow(remap = false)
    private ResourceLocation entity;

    @Inject(method = "getAltIcon", at = @At("HEAD"), cancellable = true, remap = false)
    private void ftbquestsentityvis$replaceWithEntityIcon(CallbackInfoReturnable<Icon> cir) {
        IKillTaskVisOptions opts = (IKillTaskVisOptions) this;
        cir.setReturnValue(new EntityIcon(
                entity,
                opts.ftbquestsentityvis$getVisSize(),
                opts.ftbquestsentityvis$getVisOffsetX(),
                opts.ftbquestsentityvis$getVisOffsetY(),
                opts.ftbquestsentityvis$getVisRotation(),
                opts.ftbquestsentityvis$getSpinMode(),
                opts.ftbquestsentityvis$getIdleMode(),
                opts.ftbquestsentityvis$getWalkMode()
        ));
    }

    @Inject(method = "fillConfigGroup", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$addVisConfig(ConfigGroup config, CallbackInfo ci) {
        IKillTaskVisOptions opts = (IKillTaskVisOptions) this;
        config.addDouble("entity_vis_size", opts.ftbquestsentityvis$getVisSize(),
                v -> opts.ftbquestsentityvis$setVisSize(v.floatValue()),
                1.0D, 0.0D, 10.0D);
        config.addDouble("entity_vis_offset_x", opts.ftbquestsentityvis$getVisOffsetX(),
                v -> opts.ftbquestsentityvis$setVisOffsetX(v.floatValue()),
                0.0D, -2.0D, 2.0D);
        config.addDouble("entity_vis_offset_y", opts.ftbquestsentityvis$getVisOffsetY(),
                v -> opts.ftbquestsentityvis$setVisOffsetY(v.floatValue()),
                0.0D, -2.0D, 2.0D);
        config.addDouble("entity_vis_rotation", opts.ftbquestsentityvis$getVisRotation(),
                v -> opts.ftbquestsentityvis$setVisRotation(v.floatValue()),
                0.0D, -180.0D, 180.0D);
        config.addEnum("entity_vis_spin_mode", opts.ftbquestsentityvis$getSpinMode(),
                opts::ftbquestsentityvis$setSpinMode,
                ftbquestsentityvis$overrideNameMap("entity_vis_spin_mode"),
                OverrideMode.USE_GLOBAL);
        config.addEnum("entity_vis_idle_mode", opts.ftbquestsentityvis$getIdleMode(),
                opts::ftbquestsentityvis$setIdleMode,
                ftbquestsentityvis$overrideNameMap("entity_vis_idle_mode"),
                OverrideMode.USE_GLOBAL);
        config.addEnum("entity_vis_walk_mode", opts.ftbquestsentityvis$getWalkMode(),
                opts::ftbquestsentityvis$setWalkMode,
                ftbquestsentityvis$overrideNameMap("entity_vis_walk_mode"),
                OverrideMode.USE_GLOBAL);
    }

    private static NameMap<OverrideMode> ftbquestsentityvis$overrideNameMap(String key) {
        return NameMap.of(OverrideMode.USE_GLOBAL, OverrideMode.values())
                .nameKey(v -> "ftbquests.task.ftbquests.kill." + key + "." + v.name().toLowerCase())
                .create();
    }
}
