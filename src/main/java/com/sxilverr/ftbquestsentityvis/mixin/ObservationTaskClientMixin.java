package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.EntityIcon;
import com.sxilverr.ftbquestsentityvis.duck.IKillTaskVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.task.ObservationTask;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(ObservationTask.class)
public abstract class ObservationTaskClientMixin {
    @Shadow(remap = false)
    private String toObserve;

    @Shadow(remap = false)
    public abstract TaskType getType();

    @Unique private static volatile Field ftbquestsentityvis$observeTypeField;

    @Unique
    private boolean ftbquestsentityvis$isEntityType() {
        try {
            Field f = ftbquestsentityvis$observeTypeField;
            if (f == null) {
                f = ObservationTask.class.getDeclaredField("observeType");
                f.setAccessible(true);
                ftbquestsentityvis$observeTypeField = f;
            }
            Object value = f.get(this);
            return value instanceof Enum<?> e && "ENTITY_TYPE".equals(e.name());
        } catch (Throwable ignored) {
            return false;
        }
    }

    public Icon getAltIcon() {
        if (ftbquestsentityvis$isEntityType() && toObserve != null && !toObserve.isEmpty()) {
            ResourceLocation rl = ResourceLocation.tryParse(toObserve);
            if (rl != null && BuiltInRegistries.ENTITY_TYPE.containsKey(rl)) {
                IKillTaskVisOptions opts = (IKillTaskVisOptions) this;
                return new EntityIcon(
                        rl,
                        opts.ftbquestsentityvis$getVisSize(),
                        opts.ftbquestsentityvis$getVisOffsetX(),
                        opts.ftbquestsentityvis$getVisOffsetY(),
                        opts.ftbquestsentityvis$getVisRotation(),
                        opts.ftbquestsentityvis$getSpinMode(),
                        opts.ftbquestsentityvis$getIdleMode(),
                        opts.ftbquestsentityvis$getWalkMode()
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
    }

    @Unique
    private static NameMap<OverrideMode> ftbquestsentityvis$overrideNameMap(String key) {
        return NameMap.of(OverrideMode.USE_GLOBAL, OverrideMode.values())
                .nameKey(v -> "ftbquests.task.ftbquests.observation." + key + "." + v.name().toLowerCase())
                .create();
    }
}
