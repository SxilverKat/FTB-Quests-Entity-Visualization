package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.ClientStateUtil;
import com.sxilverr.ftbquestsentityvis.client.EntityIcon;
import com.sxilverr.ftbquestsentityvis.duck.IKillTaskVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import com.sxilverr.ftbquestsentityvis.duck.SilhouetteMode;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.task.ObservationTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.BooleanSupplier;

@Mixin(ObservationTask.class)
public abstract class ObservationTaskClientMixin {
    @Shadow(remap = false)
    private String toObserve;

    @Shadow(remap = false)
    public abstract TaskType getType();

    @Unique private static volatile Field ftbquestsentityvis$observeTypeField;

    @Unique
    private String ftbquestsentityvis$observeTypeName() {
        try {
            Field f = ftbquestsentityvis$observeTypeField;
            if (f == null) {
                f = ObservationTask.class.getDeclaredField("observeType");
                f.setAccessible(true);
                ftbquestsentityvis$observeTypeField = f;
            }
            Object value = f.get(this);
            return value instanceof Enum<?> e ? e.name() : null;
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Unique
    private ResourceLocation ftbquestsentityvis$resolveTagFirstEntity(String raw) {
        String stripped = raw.startsWith("#") ? raw.substring(1) : raw;
        ResourceLocation tagId = ResourceLocation.tryParse(stripped);
        if (tagId == null) {
            return null;
        }
        TagKey<EntityType<?>> tag = TagKey.create(Registries.ENTITY_TYPE, tagId);
        Optional<EntityType<?>> first = BuiltInRegistries.ENTITY_TYPE.getTag(tag)
                .flatMap(set -> set.stream().findFirst())
                .map(holder -> holder.value());
        return first.map(BuiltInRegistries.ENTITY_TYPE::getKey).orElse(null);
    }

    public Icon getAltIcon() {
        if (toObserve != null && !toObserve.isEmpty()) {
            String typeName = ftbquestsentityvis$observeTypeName();
            ResourceLocation entityId = null;
            if ("ENTITY_TYPE".equals(typeName)) {
                ResourceLocation rl = ResourceLocation.tryParse(toObserve);
                if (rl != null && BuiltInRegistries.ENTITY_TYPE.containsKey(rl)) {
                    entityId = rl;
                }
            } else if ("ENTITY_TYPE_TAG".equals(typeName)) {
                entityId = ftbquestsentityvis$resolveTagFirstEntity(toObserve);
            }
            if (entityId != null) {
                IKillTaskVisOptions opts = (IKillTaskVisOptions) this;
                Task self = (Task) (Object) this;
                BooleanSupplier silhouette = ClientStateUtil.silhouetteCheck(self, opts.ftbquestsentityvis$getSilhouetteMode());
                return new EntityIcon(
                        entityId,
                        opts.ftbquestsentityvis$getVisSize(),
                        opts.ftbquestsentityvis$getVisOffsetX(),
                        opts.ftbquestsentityvis$getVisOffsetY(),
                        opts.ftbquestsentityvis$getVisRotation(),
                        opts.ftbquestsentityvis$getSpinMode(),
                        opts.ftbquestsentityvis$getIdleMode(),
                        opts.ftbquestsentityvis$getWalkMode(),
                        silhouette
                );
            }
        }
        return getType().getIconSupplier();
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
        config.addEnum("entity_vis_silhouette_mode", opts.ftbquestsentityvis$getSilhouetteMode(),
                opts::ftbquestsentityvis$setSilhouetteMode,
                ftbquestsentityvis$silhouetteNameMap(),
                SilhouetteMode.NONE);
        config.addBool("entity_vis_use_as_quest_icon", opts.ftbquestsentityvis$getUseAsQuestIcon(),
                opts::ftbquestsentityvis$setUseAsQuestIcon, false);
    }

    @Unique
    private static NameMap<OverrideMode> ftbquestsentityvis$overrideNameMap(String key) {
        return NameMap.of(OverrideMode.USE_GLOBAL, OverrideMode.values())
                .nameKey(v -> "ftbquests.task.ftbquests.observation." + key + "." + v.name().toLowerCase())
                .create();
    }

    @Unique
    private static NameMap<SilhouetteMode> ftbquestsentityvis$silhouetteNameMap() {
        return NameMap.of(SilhouetteMode.NONE, SilhouetteMode.values())
                .nameKey(v -> "ftbquests.task.ftbquests.observation.entity_vis_silhouette_mode." + v.name().toLowerCase())
                .create();
    }
}
