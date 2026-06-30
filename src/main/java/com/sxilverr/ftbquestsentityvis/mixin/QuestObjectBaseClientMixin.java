package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.ClientStateUtil;
import com.sxilverr.ftbquestsentityvis.client.EntityIcon;
import com.sxilverr.ftbquestsentityvis.duck.ITaskIconVisOptions;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BooleanSupplier;

@Mixin(QuestObjectBase.class)
public abstract class QuestObjectBaseClientMixin {
    @Unique private EntityIcon ftbquestsentityvis$taskIconCache;

    @Inject(method = "getIcon", at = @At("HEAD"), cancellable = true, remap = false)
    private void ftbquestsentityvis$taskEntityIcon(CallbackInfoReturnable<Icon> cir) {
        if (!(((Object) this) instanceof ITaskIconVisOptions opts)) {
            return;
        }
        if (!opts.ftbquestsentityvis$getIconEntityEnabled()) {
            return;
        }
        ResourceLocation id = opts.ftbquestsentityvis$getIconEntityId();
        if (id == null) {
            return;
        }
        if (ftbquestsentityvis$taskIconCache == null || opts.ftbquestsentityvis$isIconDirty()) {
            Task self = (Task) (Object) this;
            BooleanSupplier silhouette = ClientStateUtil.silhouetteCheck(self, opts.ftbquestsentityvis$getIconSilhouetteMode());
            ftbquestsentityvis$taskIconCache = new EntityIcon(
                    id,
                    opts.ftbquestsentityvis$getIconVisSize(),
                    opts.ftbquestsentityvis$getIconVisOffsetX(),
                    opts.ftbquestsentityvis$getIconVisOffsetY(),
                    opts.ftbquestsentityvis$getIconVisRotation(),
                    opts.ftbquestsentityvis$getIconSpinMode(),
                    opts.ftbquestsentityvis$getIconIdleMode(),
                    opts.ftbquestsentityvis$getIconWalkMode(),
                    silhouette
            );
            opts.ftbquestsentityvis$setIconDirty(false);
        }
        cir.setReturnValue(ftbquestsentityvis$taskIconCache);
    }
}
