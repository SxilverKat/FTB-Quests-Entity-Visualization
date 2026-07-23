package com.sxilverr.ftbquestsentityvis.mixin;
import com.sxilverr.ftbquestsentityvis.ModUtil;

import com.sxilverr.ftbquestsentityvis.duck.IEntityImageVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftbquests.quest.ChapterImage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChapterImage.class)
public abstract class ChapterImageMixin implements IEntityImageVisOptions {
    @Unique private static final ResourceLocation ftbquestsentityvis$DEFAULT_ENTITY = ModUtil.rl("minecraft:pig");

    @Unique private static final String ftbquestsentityvis$KEY_ENABLED = "entity_vis_enabled";
    @Unique private static final String ftbquestsentityvis$KEY_ENTITY = "entity_vis_entity";
    @Unique private static final String ftbquestsentityvis$KEY_SIZE = "entity_vis_size";
    @Unique private static final String ftbquestsentityvis$KEY_OFFSET_X = "entity_vis_offset_x";
    @Unique private static final String ftbquestsentityvis$KEY_OFFSET_Y = "entity_vis_offset_y";
    @Unique private static final String ftbquestsentityvis$KEY_ROTATION = "entity_vis_rotation";
    @Unique private static final String ftbquestsentityvis$KEY_SPIN_MODE = "entity_vis_spin_mode";
    @Unique private static final String ftbquestsentityvis$KEY_IDLE_MODE = "entity_vis_idle_mode";
    @Unique private static final String ftbquestsentityvis$KEY_WALK_MODE = "entity_vis_walk_mode";
    @Unique private static final String ftbquestsentityvis$KEY_SILHOUETTE = "entity_vis_silhouette";
    @Unique private static final String ftbquestsentityvis$KEY_NBT = "entity_vis_nbt";

    @Unique private boolean ftbquestsentityvis$entityVis = false;
    @Unique private ResourceLocation ftbquestsentityvis$entityId = ftbquestsentityvis$DEFAULT_ENTITY;
    @Unique private float ftbquestsentityvis$visSize = 1.0F;
    @Unique private float ftbquestsentityvis$visOffsetX = 0.0F;
    @Unique private float ftbquestsentityvis$visOffsetY = 0.0F;
    @Unique private float ftbquestsentityvis$visRotation = 0.0F;
    @Unique private OverrideMode ftbquestsentityvis$spinMode = OverrideMode.USE_GLOBAL;
    @Unique private OverrideMode ftbquestsentityvis$idleMode = OverrideMode.USE_GLOBAL;
    @Unique private OverrideMode ftbquestsentityvis$walkMode = OverrideMode.USE_GLOBAL;
    @Unique private boolean ftbquestsentityvis$silhouette = false;
    @Unique private String ftbquestsentityvis$nbt = "";
    @Unique private boolean ftbquestsentityvis$iconDirty = true;

    @Override public boolean ftbquestsentityvis$isEntityVis() { return ftbquestsentityvis$entityVis; }
    @Override public void ftbquestsentityvis$setEntityVis(boolean entityVis) { this.ftbquestsentityvis$entityVis = entityVis; this.ftbquestsentityvis$iconDirty = true; }

    @Override public ResourceLocation ftbquestsentityvis$getEntityId() { return ftbquestsentityvis$entityId; }
    @Override public void ftbquestsentityvis$setEntityId(ResourceLocation entityId) { this.ftbquestsentityvis$entityId = entityId; this.ftbquestsentityvis$iconDirty = true; }

    @Override public float ftbquestsentityvis$getVisSize() { return ftbquestsentityvis$visSize; }
    @Override public void ftbquestsentityvis$setVisSize(float size) { this.ftbquestsentityvis$visSize = size; this.ftbquestsentityvis$iconDirty = true; }

    @Override public float ftbquestsentityvis$getVisOffsetX() { return ftbquestsentityvis$visOffsetX; }
    @Override public void ftbquestsentityvis$setVisOffsetX(float offset) { this.ftbquestsentityvis$visOffsetX = offset; this.ftbquestsentityvis$iconDirty = true; }

    @Override public float ftbquestsentityvis$getVisOffsetY() { return ftbquestsentityvis$visOffsetY; }
    @Override public void ftbquestsentityvis$setVisOffsetY(float offset) { this.ftbquestsentityvis$visOffsetY = offset; this.ftbquestsentityvis$iconDirty = true; }

    @Override public float ftbquestsentityvis$getVisRotation() { return ftbquestsentityvis$visRotation; }
    @Override public void ftbquestsentityvis$setVisRotation(float rotation) { this.ftbquestsentityvis$visRotation = rotation; this.ftbquestsentityvis$iconDirty = true; }

    @Override public OverrideMode ftbquestsentityvis$getSpinMode() { return ftbquestsentityvis$spinMode; }
    @Override public void ftbquestsentityvis$setSpinMode(OverrideMode mode) { this.ftbquestsentityvis$spinMode = mode; this.ftbquestsentityvis$iconDirty = true; }

    @Override public OverrideMode ftbquestsentityvis$getIdleMode() { return ftbquestsentityvis$idleMode; }
    @Override public void ftbquestsentityvis$setIdleMode(OverrideMode mode) { this.ftbquestsentityvis$idleMode = mode; this.ftbquestsentityvis$iconDirty = true; }

    @Override public OverrideMode ftbquestsentityvis$getWalkMode() { return ftbquestsentityvis$walkMode; }
    @Override public void ftbquestsentityvis$setWalkMode(OverrideMode mode) { this.ftbquestsentityvis$walkMode = mode; this.ftbquestsentityvis$iconDirty = true; }

    @Override public boolean ftbquestsentityvis$isSilhouette() { return ftbquestsentityvis$silhouette; }
    @Override public void ftbquestsentityvis$setSilhouette(boolean silhouette) { this.ftbquestsentityvis$silhouette = silhouette; this.ftbquestsentityvis$iconDirty = true; }

    @Override public String ftbquestsentityvis$getNbt() { return ftbquestsentityvis$nbt; }
    @Override public void ftbquestsentityvis$setNbt(String nbt) { this.ftbquestsentityvis$nbt = nbt == null ? "" : nbt; this.ftbquestsentityvis$iconDirty = true; }

    @Override public boolean ftbquestsentityvis$isIconDirty() { return ftbquestsentityvis$iconDirty; }
    @Override public void ftbquestsentityvis$setIconDirty(boolean dirty) { this.ftbquestsentityvis$iconDirty = dirty; }

    @Inject(method = "writeData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$writeData(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        if (!ftbquestsentityvis$entityVis) {
            return;
        }
        nbt.putBoolean(ftbquestsentityvis$KEY_ENABLED, true);
        nbt.putString(ftbquestsentityvis$KEY_ENTITY, ftbquestsentityvis$entityId.toString());
        nbt.putFloat(ftbquestsentityvis$KEY_SIZE, ftbquestsentityvis$visSize);
        nbt.putFloat(ftbquestsentityvis$KEY_OFFSET_X, ftbquestsentityvis$visOffsetX);
        nbt.putFloat(ftbquestsentityvis$KEY_OFFSET_Y, ftbquestsentityvis$visOffsetY);
        nbt.putFloat(ftbquestsentityvis$KEY_ROTATION, ftbquestsentityvis$visRotation);
        nbt.putString(ftbquestsentityvis$KEY_SPIN_MODE, ftbquestsentityvis$spinMode.name());
        nbt.putString(ftbquestsentityvis$KEY_IDLE_MODE, ftbquestsentityvis$idleMode.name());
        nbt.putString(ftbquestsentityvis$KEY_WALK_MODE, ftbquestsentityvis$walkMode.name());
        nbt.putBoolean(ftbquestsentityvis$KEY_SILHOUETTE, ftbquestsentityvis$silhouette);
        if (!ftbquestsentityvis$nbt.isEmpty()) {
            nbt.putString(ftbquestsentityvis$KEY_NBT, ftbquestsentityvis$nbt);
        }
    }

    @Inject(method = "readData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$readData(CompoundTag nbt, CallbackInfo ci) {
        ftbquestsentityvis$entityVis = nbt.getBoolean(ftbquestsentityvis$KEY_ENABLED);
        if (ftbquestsentityvis$entityVis) {
            ftbquestsentityvis$entityId = nbt.contains(ftbquestsentityvis$KEY_ENTITY)
                    ? ModUtil.rl(nbt.getString(ftbquestsentityvis$KEY_ENTITY)) : ftbquestsentityvis$DEFAULT_ENTITY;
            ftbquestsentityvis$visSize = nbt.contains(ftbquestsentityvis$KEY_SIZE) ? nbt.getFloat(ftbquestsentityvis$KEY_SIZE) : 1.0F;
            ftbquestsentityvis$visOffsetX = nbt.getFloat(ftbquestsentityvis$KEY_OFFSET_X);
            ftbquestsentityvis$visOffsetY = nbt.getFloat(ftbquestsentityvis$KEY_OFFSET_Y);
            ftbquestsentityvis$visRotation = nbt.getFloat(ftbquestsentityvis$KEY_ROTATION);
            ftbquestsentityvis$spinMode = OverrideMode.fromName(nbt.getString(ftbquestsentityvis$KEY_SPIN_MODE));
            ftbquestsentityvis$idleMode = OverrideMode.fromName(nbt.getString(ftbquestsentityvis$KEY_IDLE_MODE));
            ftbquestsentityvis$walkMode = OverrideMode.fromName(nbt.getString(ftbquestsentityvis$KEY_WALK_MODE));
            ftbquestsentityvis$silhouette = nbt.getBoolean(ftbquestsentityvis$KEY_SILHOUETTE);
            ftbquestsentityvis$nbt = nbt.contains(ftbquestsentityvis$KEY_NBT) ? nbt.getString(ftbquestsentityvis$KEY_NBT) : "";
        }
        ftbquestsentityvis$iconDirty = true;
    }

    @Inject(method = "writeNetData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$writeNetData(FriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeBoolean(ftbquestsentityvis$entityVis);
        if (ftbquestsentityvis$entityVis) {
            buf.writeUtf(ftbquestsentityvis$entityId.toString(), Short.MAX_VALUE);
            buf.writeFloat(ftbquestsentityvis$visSize);
            buf.writeFloat(ftbquestsentityvis$visOffsetX);
            buf.writeFloat(ftbquestsentityvis$visOffsetY);
            buf.writeFloat(ftbquestsentityvis$visRotation);
            buf.writeUtf(ftbquestsentityvis$spinMode.name());
            buf.writeUtf(ftbquestsentityvis$idleMode.name());
            buf.writeUtf(ftbquestsentityvis$walkMode.name());
            buf.writeBoolean(ftbquestsentityvis$silhouette);
            buf.writeUtf(ftbquestsentityvis$nbt, Short.MAX_VALUE);
        }
    }

    @Inject(method = "readNetData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$readNetData(FriendlyByteBuf buf, CallbackInfo ci) {
        ftbquestsentityvis$entityVis = buf.readBoolean();
        if (ftbquestsentityvis$entityVis) {
            ftbquestsentityvis$entityId = ModUtil.rl(buf.readUtf(Short.MAX_VALUE));
            ftbquestsentityvis$visSize = buf.readFloat();
            ftbquestsentityvis$visOffsetX = buf.readFloat();
            ftbquestsentityvis$visOffsetY = buf.readFloat();
            ftbquestsentityvis$visRotation = buf.readFloat();
            ftbquestsentityvis$spinMode = OverrideMode.fromName(buf.readUtf());
            ftbquestsentityvis$idleMode = OverrideMode.fromName(buf.readUtf());
            ftbquestsentityvis$walkMode = OverrideMode.fromName(buf.readUtf());
            ftbquestsentityvis$silhouette = buf.readBoolean();
            ftbquestsentityvis$nbt = buf.readUtf(Short.MAX_VALUE);
        }
        ftbquestsentityvis$iconDirty = true;
    }
}
