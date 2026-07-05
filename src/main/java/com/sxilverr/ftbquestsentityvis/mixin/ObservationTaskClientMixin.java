package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.ClientStateUtil;
import com.sxilverr.ftbquestsentityvis.client.EntityIcon;
import com.sxilverr.ftbquestsentityvis.duck.IKillTaskVisOptions;
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
}
