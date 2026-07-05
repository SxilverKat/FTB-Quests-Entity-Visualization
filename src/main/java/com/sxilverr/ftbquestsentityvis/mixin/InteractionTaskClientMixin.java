package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.ClientStateUtil;
import com.sxilverr.ftbquestsentityvis.client.EntityIcon;
import com.sxilverr.ftbquestsentityvis.duck.IKillTaskVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
