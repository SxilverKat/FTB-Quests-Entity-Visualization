package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.ClientStateUtil;
import com.sxilverr.ftbquestsentityvis.client.EntityIcon;
import com.sxilverr.ftbquestsentityvis.duck.IKillTaskVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.task.KillTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Function;

@Mixin(KillTask.class)
public abstract class KillTaskClientMixin {
    @Shadow(remap = false)
    private ResourceLocation entityTypeId;

    @Shadow(remap = false)
    private TagKey<EntityType<?>> entityTypeTag;

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
        if (entityTypeTag != null) {
            Optional<EntityType<?>> first = BuiltInRegistries.ENTITY_TYPE.getTag(entityTypeTag)
                    .flatMap(set -> set.stream().findFirst())
                    .map(holder -> holder.value());
            return first.map(BuiltInRegistries.ENTITY_TYPE::getKey).orElse(null);
        }
        return entityTypeId;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Redirect(method = "scanEntityTypes", remap = false,
            at = @At(value = "INVOKE",
                    target = "Ldev/ftb/mods/ftblibrary/config/NameMap$Builder;icon(Ljava/util/function/Function;)Ldev/ftb/mods/ftblibrary/config/NameMap$Builder;"))
    private static NameMap.Builder ftbquestsentityvis$replaceEntitySelectorIcons(NameMap.Builder builder, Function originalIconFunc) {
        return builder.icon((Function<Object, Icon>) value -> {
            if (value instanceof ResourceLocation rl) {
                return new EntityIcon(rl, 1.0F, 0.0F, 0.0F, 0.0F,
                        OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL);
            }
            return (Icon) originalIconFunc.apply(value);
        });
    }
}
