package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.EntityIcon;
import com.sxilverr.ftbquestsentityvis.duck.IEntityImageVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.ChapterImage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ChapterImage.class)
public abstract class ChapterImageClientMixin {
    @Unique private static final ResourceLocation ftbquestsentityvis$DEFAULT_ENTITY = new ResourceLocation("minecraft:pig");

    @Shadow(remap = false) private double width;
    @Shadow(remap = false) private double height;
    @Shadow(remap = false) private int order;
    @Shadow(remap = false) private boolean editorsOnly;
    @Shadow(remap = false) @Final private List<String> hover;

    @Unique private EntityIcon ftbquestsentityvis$cachedIcon;

    @Inject(method = "getImage", at = @At("HEAD"), cancellable = true, remap = false)
    private void ftbquestsentityvis$entityImage(CallbackInfoReturnable<Icon> cir) {
        IEntityImageVisOptions opts = (IEntityImageVisOptions) (Object) this;
        if (!opts.ftbquestsentityvis$isEntityVis()) {
            return;
        }
        if (ftbquestsentityvis$cachedIcon == null || opts.ftbquestsentityvis$isIconDirty()) {
            BooleanSupplier silhouette = opts.ftbquestsentityvis$isSilhouette() ? () -> true : null;
            ftbquestsentityvis$cachedIcon = new EntityIcon(
                    opts.ftbquestsentityvis$getEntityId(),
                    opts.ftbquestsentityvis$getVisSize(),
                    opts.ftbquestsentityvis$getVisOffsetX(),
                    opts.ftbquestsentityvis$getVisOffsetY(),
                    opts.ftbquestsentityvis$getVisRotation(),
                    opts.ftbquestsentityvis$getSpinMode(),
                    opts.ftbquestsentityvis$getIdleMode(),
                    opts.ftbquestsentityvis$getWalkMode(),
                    silhouette
            );
            opts.ftbquestsentityvis$setIconDirty(false);
        }
        cir.setReturnValue(ftbquestsentityvis$cachedIcon);
    }

    @Inject(method = "getTitle", at = @At("HEAD"), cancellable = true, remap = false)
    private void ftbquestsentityvis$entityTitle(CallbackInfoReturnable<Component> cir) {
        IEntityImageVisOptions opts = (IEntityImageVisOptions) (Object) this;
        if (opts.ftbquestsentityvis$isEntityVis()) {
            ResourceLocation id = opts.ftbquestsentityvis$getEntityId();
            cir.setReturnValue(Component.translatable("entity." + id.getNamespace() + "." + id.getPath()));
        }
    }

    @Inject(method = "fillConfigGroup", at = @At("HEAD"), cancellable = true, remap = false)
    private void ftbquestsentityvis$entityConfig(ConfigGroup config, CallbackInfo ci) {
        IEntityImageVisOptions opts = (IEntityImageVisOptions) (Object) this;
        if (!opts.ftbquestsentityvis$isEntityVis()) {
            return;
        }

        ftbquestsentityvis$fillEntityConfig(config, opts);
        ci.cancel();
    }

    @Unique
    private void ftbquestsentityvis$fillEntityConfig(ConfigGroup config, IEntityImageVisOptions opts) {
        List<ResourceLocation> ids = new ArrayList<>(BuiltInRegistries.ENTITY_TYPE.keySet());
        config.addEnum("entity", opts.ftbquestsentityvis$getEntityId(), opts::ftbquestsentityvis$setEntityId,
                        NameMap.of(ftbquestsentityvis$DEFAULT_ENTITY, ids)
                                .nameKey(v -> "entity." + v.getNamespace() + "." + v.getPath())
                                .icon(v -> new EntityIcon(v, 1.0F, 0.0F, 0.0F, 0.0F,
                                        OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL))
                                .create(), ftbquestsentityvis$DEFAULT_ENTITY)
                .setNameKey("ftbquestsentityvis.config.entity");

        config.addDouble("size", opts.ftbquestsentityvis$getVisSize(),
                        v -> opts.ftbquestsentityvis$setVisSize(v.floatValue()), 1.0D, 0.0D, 10.0D)
                .setNameKey("ftbquestsentityvis.config.size");
        config.addDouble("offset_x", opts.ftbquestsentityvis$getVisOffsetX(),
                        v -> opts.ftbquestsentityvis$setVisOffsetX(v.floatValue()), 0.0D, -2.0D, 2.0D)
                .setNameKey("ftbquestsentityvis.config.offset_x");
        config.addDouble("offset_y", opts.ftbquestsentityvis$getVisOffsetY(),
                        v -> opts.ftbquestsentityvis$setVisOffsetY(v.floatValue()), 0.0D, -2.0D, 2.0D)
                .setNameKey("ftbquestsentityvis.config.offset_y");
        config.addDouble("rotation", opts.ftbquestsentityvis$getVisRotation(),
                        v -> opts.ftbquestsentityvis$setVisRotation(v.floatValue()), 0.0D, -180.0D, 180.0D)
                .setNameKey("ftbquestsentityvis.config.rotation");

        config.addEnum("spin_mode", opts.ftbquestsentityvis$getSpinMode(), opts::ftbquestsentityvis$setSpinMode,
                        ftbquestsentityvis$overrideNameMap("spin_mode"), OverrideMode.USE_GLOBAL)
                .setNameKey("ftbquestsentityvis.config.spin_mode");
        config.addEnum("idle_mode", opts.ftbquestsentityvis$getIdleMode(), opts::ftbquestsentityvis$setIdleMode,
                        ftbquestsentityvis$overrideNameMap("idle_mode"), OverrideMode.USE_GLOBAL)
                .setNameKey("ftbquestsentityvis.config.idle_mode");
        config.addEnum("walk_mode", opts.ftbquestsentityvis$getWalkMode(), opts::ftbquestsentityvis$setWalkMode,
                        ftbquestsentityvis$overrideNameMap("walk_mode"), OverrideMode.USE_GLOBAL)
                .setNameKey("ftbquestsentityvis.config.walk_mode");

        config.addBool("silhouette", opts.ftbquestsentityvis$isSilhouette(), opts::ftbquestsentityvis$setSilhouette, false)
                .setNameKey("ftbquestsentityvis.config.silhouette");

        config.addDouble("width", width, v -> width = v, 1.0D, 0.0D, Double.POSITIVE_INFINITY)
                .setNameKey("ftbquestsentityvis.config.width");
        config.addDouble("height", height, v -> height = v, 1.0D, 0.0D, Double.POSITIVE_INFINITY)
                .setNameKey("ftbquestsentityvis.config.height");
        config.addInt("order", order, v -> order = v, 0, Integer.MIN_VALUE, Integer.MAX_VALUE)
                .setNameKey("ftbquestsentityvis.config.order");
        config.addBool("dev", editorsOnly, v -> editorsOnly = v, false)
                .setNameKey("ftbquestsentityvis.config.dev");
        config.addList("hover", hover, new StringConfig(), "")
                .setNameKey("ftbquestsentityvis.config.hover");
    }

    @Unique
    private static NameMap<OverrideMode> ftbquestsentityvis$overrideNameMap(String key) {
        return NameMap.of(OverrideMode.USE_GLOBAL, OverrideMode.values())
                .nameKey(v -> "ftbquestsentityvis.config." + key + "." + v.name().toLowerCase())
                .create();
    }
}
