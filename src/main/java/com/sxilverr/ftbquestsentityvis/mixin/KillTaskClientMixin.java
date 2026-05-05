package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.ClientStateUtil;
import com.sxilverr.ftbquestsentityvis.client.EntityIcon;
import com.sxilverr.ftbquestsentityvis.duck.IKillTaskTagOption;
import com.sxilverr.ftbquestsentityvis.duck.IKillTaskVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import com.sxilverr.ftbquestsentityvis.duck.SilhouetteMode;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.task.KillTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Function;

@Mixin(KillTask.class)
public abstract class KillTaskClientMixin {
    @Shadow(remap = false)
    private ResourceLocation entity;

    @Inject(method = "getAltIcon", at = @At("HEAD"), cancellable = true, remap = false)
    private void ftbquestsentityvis$replaceWithEntityIcon(CallbackInfoReturnable<Icon> cir) {
        IKillTaskVisOptions opts = (IKillTaskVisOptions) this;
        ResourceLocation visualEntity = ftbquestsentityvis$resolveVisualEntity();
        if (visualEntity == null) {
            return;
        }
        Task self = (Task) (Object) this;
        cir.setReturnValue(new EntityIcon(
                visualEntity,
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

    @Unique
    private ResourceLocation ftbquestsentityvis$resolveVisualEntity() {
        if (entity == null) {
            return null;
        }
        boolean useTag = ((IKillTaskTagOption) this).ftbquestsentityvis$getUseTag();
        if (!useTag) {
            return entity;
        }
        TagKey<EntityType<?>> tag = TagKey.create(Registries.ENTITY_TYPE, entity);
        Optional<EntityType<?>> first = BuiltInRegistries.ENTITY_TYPE.getTag(tag)
                .flatMap(set -> set.stream().findFirst())
                .map(holder -> holder.value());
        return first.map(BuiltInRegistries.ENTITY_TYPE::getKey).orElse(null);
    }

    @Inject(method = "fillConfigGroup", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$addVisConfig(ConfigGroup config, CallbackInfo ci) {
        IKillTaskVisOptions opts = (IKillTaskVisOptions) this;
        IKillTaskTagOption tagOpts = (IKillTaskTagOption) this;
        config.addBool("entity_use_tag", tagOpts.ftbquestsentityvis$getUseTag(),
                tagOpts::ftbquestsentityvis$setUseTag, false);
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
        config.addEnum("entity_vis_silhouette_mode", opts.ftbquestsentityvis$getSilhouetteMode(),
                opts::ftbquestsentityvis$setSilhouetteMode,
                ftbquestsentityvis$silhouetteNameMap(),
                SilhouetteMode.NONE);
        config.addBool("entity_vis_use_as_quest_icon", opts.ftbquestsentityvis$getUseAsQuestIcon(),
                opts::ftbquestsentityvis$setUseAsQuestIcon, false);
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
}
