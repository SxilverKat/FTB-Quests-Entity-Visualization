package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.ClientStateUtil;
import com.sxilverr.ftbquestsentityvis.client.EntityIcon;
import com.sxilverr.ftbquestsentityvis.duck.IKillTaskVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import com.sxilverr.ftbquestsentityvis.duck.SilhouetteMode;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(targets = "questsadditions.tasks.InteractionTask")
public abstract class InteractionTaskClientMixin {
    @Shadow(remap = false) public ResourceLocation entity;

    @Inject(method = "getAltIcon", at = @At("HEAD"), cancellable = true, remap = false)
    private void ftbquestsentityvis$replaceWithEntityIcon(CallbackInfoReturnable<Icon> cir) {
        if (entity == null) {
            return;
        }
        IKillTaskVisOptions opts = (IKillTaskVisOptions) this;
        Task self = (Task) (Object) this;
        cir.setReturnValue(new EntityIcon(
                entity,
                opts.ftbquestsentityvis$getVisSize(),
                opts.ftbquestsentityvis$getVisOffsetX(),
                opts.ftbquestsentityvis$getVisOffsetY(),
                opts.ftbquestsentityvis$getVisRotation(),
                opts.ftbquestsentityvis$getSpinMode(),
                opts.ftbquestsentityvis$getIdleMode(),
                opts.ftbquestsentityvis$getWalkMode(),
                ClientStateUtil.silhouetteCheck(self, opts.ftbquestsentityvis$getSilhouetteMode())
        ));
    }

    @Inject(method = "fillConfigGroup", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$addVisConfig(ConfigGroup config, CallbackInfo ci) {
        IKillTaskVisOptions opts = (IKillTaskVisOptions) this;
        config.addDouble("entity_vis_size", opts.ftbquestsentityvis$getVisSize(),
                        v -> opts.ftbquestsentityvis$setVisSize(v.floatValue()), 1.0D, 0.0D, 10.0D)
                .setNameKey("ftbquests.task.ftbquests.kill.entity_vis_size");
        config.addDouble("entity_vis_offset_x", opts.ftbquestsentityvis$getVisOffsetX(),
                        v -> opts.ftbquestsentityvis$setVisOffsetX(v.floatValue()), 0.0D, -2.0D, 2.0D)
                .setNameKey("ftbquests.task.ftbquests.kill.entity_vis_offset_x");
        config.addDouble("entity_vis_offset_y", opts.ftbquestsentityvis$getVisOffsetY(),
                        v -> opts.ftbquestsentityvis$setVisOffsetY(v.floatValue()), 0.0D, -2.0D, 2.0D)
                .setNameKey("ftbquests.task.ftbquests.kill.entity_vis_offset_y");
        config.addDouble("entity_vis_rotation", opts.ftbquestsentityvis$getVisRotation(),
                        v -> opts.ftbquestsentityvis$setVisRotation(v.floatValue()), 0.0D, -180.0D, 180.0D)
                .setNameKey("ftbquests.task.ftbquests.kill.entity_vis_rotation");
        config.addEnum("entity_vis_spin_mode", opts.ftbquestsentityvis$getSpinMode(),
                        opts::ftbquestsentityvis$setSpinMode,
                        ftbquestsentityvis$overrideNameMap("entity_vis_spin_mode"), OverrideMode.USE_GLOBAL)
                .setNameKey("ftbquests.task.ftbquests.kill.entity_vis_spin_mode");
        config.addEnum("entity_vis_idle_mode", opts.ftbquestsentityvis$getIdleMode(),
                        opts::ftbquestsentityvis$setIdleMode,
                        ftbquestsentityvis$overrideNameMap("entity_vis_idle_mode"), OverrideMode.USE_GLOBAL)
                .setNameKey("ftbquests.task.ftbquests.kill.entity_vis_idle_mode");
        config.addEnum("entity_vis_walk_mode", opts.ftbquestsentityvis$getWalkMode(),
                        opts::ftbquestsentityvis$setWalkMode,
                        ftbquestsentityvis$overrideNameMap("entity_vis_walk_mode"), OverrideMode.USE_GLOBAL)
                .setNameKey("ftbquests.task.ftbquests.kill.entity_vis_walk_mode");
        config.addEnum("entity_vis_silhouette_mode", opts.ftbquestsentityvis$getSilhouetteMode(),
                        opts::ftbquestsentityvis$setSilhouetteMode,
                        ftbquestsentityvis$silhouetteNameMap(), SilhouetteMode.NONE)
                .setNameKey("ftbquests.task.ftbquests.kill.entity_vis_silhouette_mode");
        config.addBool("entity_vis_use_as_quest_icon", opts.ftbquestsentityvis$getUseAsQuestIcon(),
                        opts::ftbquestsentityvis$setUseAsQuestIcon, false)
                .setNameKey("ftbquests.task.ftbquests.kill.entity_vis_use_as_quest_icon");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Redirect(method = "fillConfigGroup", remap = false,
            at = @At(value = "INVOKE",
                    target = "Ldev/ftb/mods/ftblibrary/config/NameMap$Builder;icon(Ljava/util/function/Function;)Ldev/ftb/mods/ftblibrary/config/NameMap$Builder;"))
    private NameMap.Builder ftbquestsentityvis$replaceEntitySelectorIcons(NameMap.Builder builder, Function originalIconFunc) {
        return builder.icon((Function<Object, Icon>) value -> {
            if (value instanceof ResourceLocation rl) {
                return new EntityIcon(rl, 1.0F, 0.0F, 0.0F, 0.0F,
                        OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL);
            }
            return (Icon) originalIconFunc.apply(value);
        });
    }

    private static NameMap<OverrideMode> ftbquestsentityvis$overrideNameMap(String key) {
        return NameMap.of(OverrideMode.USE_GLOBAL, OverrideMode.values())
                .nameKey(v -> "ftbquests.task.ftbquests.kill." + key + "." + v.name().toLowerCase())
                .create();
    }

    private static NameMap<SilhouetteMode> ftbquestsentityvis$silhouetteNameMap() {
        return NameMap.of(SilhouetteMode.NONE, SilhouetteMode.values())
                .nameKey(v -> "ftbquests.task.ftbquests.kill.entity_vis_silhouette_mode." + v.name().toLowerCase())
                .create();
    }
}
