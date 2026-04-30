package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.duck.IKillTaskVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftbquests.quest.task.ObservationTask;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ObservationTask.class)
public abstract class ObservationTaskMixin implements IKillTaskVisOptions {
    @Unique private static final String ftbquestsentityvis$KEY_SIZE = "entity_vis_size";
    @Unique private static final String ftbquestsentityvis$KEY_OFFSET_X = "entity_vis_offset_x";
    @Unique private static final String ftbquestsentityvis$KEY_OFFSET_Y = "entity_vis_offset_y";
    @Unique private static final String ftbquestsentityvis$KEY_ROTATION = "entity_vis_rotation";
    @Unique private static final String ftbquestsentityvis$KEY_SPIN_MODE = "entity_vis_spin_mode";
    @Unique private static final String ftbquestsentityvis$KEY_IDLE_MODE = "entity_vis_idle_mode";
    @Unique private static final String ftbquestsentityvis$KEY_WALK_MODE = "entity_vis_walk_mode";

    @Unique private float ftbquestsentityvis$visSize = 1.0F;
    @Unique private float ftbquestsentityvis$visOffsetX = 0.0F;
    @Unique private float ftbquestsentityvis$visOffsetY = 0.0F;
    @Unique private float ftbquestsentityvis$visRotation = 0.0F;
    @Unique private OverrideMode ftbquestsentityvis$spinMode = OverrideMode.USE_GLOBAL;
    @Unique private OverrideMode ftbquestsentityvis$idleMode = OverrideMode.USE_GLOBAL;
    @Unique private OverrideMode ftbquestsentityvis$walkMode = OverrideMode.USE_GLOBAL;

    @Override public float ftbquestsentityvis$getVisSize() { return ftbquestsentityvis$visSize; }
    @Override public void ftbquestsentityvis$setVisSize(float size) { this.ftbquestsentityvis$visSize = size; }

    @Override public float ftbquestsentityvis$getVisOffsetX() { return ftbquestsentityvis$visOffsetX; }
    @Override public void ftbquestsentityvis$setVisOffsetX(float offset) { this.ftbquestsentityvis$visOffsetX = offset; }

    @Override public float ftbquestsentityvis$getVisOffsetY() { return ftbquestsentityvis$visOffsetY; }
    @Override public void ftbquestsentityvis$setVisOffsetY(float offset) { this.ftbquestsentityvis$visOffsetY = offset; }

    @Override public float ftbquestsentityvis$getVisRotation() { return ftbquestsentityvis$visRotation; }
    @Override public void ftbquestsentityvis$setVisRotation(float rotation) { this.ftbquestsentityvis$visRotation = rotation; }

    @Override public OverrideMode ftbquestsentityvis$getSpinMode() { return ftbquestsentityvis$spinMode; }
    @Override public void ftbquestsentityvis$setSpinMode(OverrideMode mode) { this.ftbquestsentityvis$spinMode = mode; }

    @Override public OverrideMode ftbquestsentityvis$getIdleMode() { return ftbquestsentityvis$idleMode; }
    @Override public void ftbquestsentityvis$setIdleMode(OverrideMode mode) { this.ftbquestsentityvis$idleMode = mode; }

    @Override public OverrideMode ftbquestsentityvis$getWalkMode() { return ftbquestsentityvis$walkMode; }
    @Override public void ftbquestsentityvis$setWalkMode(OverrideMode mode) { this.ftbquestsentityvis$walkMode = mode; }

    @Inject(method = "writeData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$writeData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putFloat(ftbquestsentityvis$KEY_SIZE, ftbquestsentityvis$visSize);
        nbt.putFloat(ftbquestsentityvis$KEY_OFFSET_X, ftbquestsentityvis$visOffsetX);
        nbt.putFloat(ftbquestsentityvis$KEY_OFFSET_Y, ftbquestsentityvis$visOffsetY);
        nbt.putFloat(ftbquestsentityvis$KEY_ROTATION, ftbquestsentityvis$visRotation);
        nbt.putString(ftbquestsentityvis$KEY_SPIN_MODE, ftbquestsentityvis$spinMode.name());
        nbt.putString(ftbquestsentityvis$KEY_IDLE_MODE, ftbquestsentityvis$idleMode.name());
        nbt.putString(ftbquestsentityvis$KEY_WALK_MODE, ftbquestsentityvis$walkMode.name());
    }

    @Inject(method = "readData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$readData(CompoundTag nbt, CallbackInfo ci) {
        ftbquestsentityvis$visSize = nbt.contains(ftbquestsentityvis$KEY_SIZE) ? nbt.getFloat(ftbquestsentityvis$KEY_SIZE) : 1.0F;
        ftbquestsentityvis$visOffsetX = nbt.contains(ftbquestsentityvis$KEY_OFFSET_X) ? nbt.getFloat(ftbquestsentityvis$KEY_OFFSET_X) : 0.0F;
        ftbquestsentityvis$visOffsetY = nbt.contains(ftbquestsentityvis$KEY_OFFSET_Y) ? nbt.getFloat(ftbquestsentityvis$KEY_OFFSET_Y) : 0.0F;
        ftbquestsentityvis$visRotation = nbt.contains(ftbquestsentityvis$KEY_ROTATION) ? nbt.getFloat(ftbquestsentityvis$KEY_ROTATION) : 0.0F;
        ftbquestsentityvis$spinMode = nbt.contains(ftbquestsentityvis$KEY_SPIN_MODE) ? OverrideMode.fromName(nbt.getString(ftbquestsentityvis$KEY_SPIN_MODE)) : OverrideMode.USE_GLOBAL;
        ftbquestsentityvis$idleMode = nbt.contains(ftbquestsentityvis$KEY_IDLE_MODE) ? OverrideMode.fromName(nbt.getString(ftbquestsentityvis$KEY_IDLE_MODE)) : OverrideMode.USE_GLOBAL;
        ftbquestsentityvis$walkMode = nbt.contains(ftbquestsentityvis$KEY_WALK_MODE) ? OverrideMode.fromName(nbt.getString(ftbquestsentityvis$KEY_WALK_MODE)) : OverrideMode.USE_GLOBAL;
    }

    @Inject(method = "writeNetData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$writeNetData(FriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeFloat(ftbquestsentityvis$visSize);
        buf.writeFloat(ftbquestsentityvis$visOffsetX);
        buf.writeFloat(ftbquestsentityvis$visOffsetY);
        buf.writeFloat(ftbquestsentityvis$visRotation);
        buf.writeUtf(ftbquestsentityvis$spinMode.name());
        buf.writeUtf(ftbquestsentityvis$idleMode.name());
        buf.writeUtf(ftbquestsentityvis$walkMode.name());
    }

    @Inject(method = "readNetData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$readNetData(FriendlyByteBuf buf, CallbackInfo ci) {
        ftbquestsentityvis$visSize = buf.readFloat();
        ftbquestsentityvis$visOffsetX = buf.readFloat();
        ftbquestsentityvis$visOffsetY = buf.readFloat();
        ftbquestsentityvis$visRotation = buf.readFloat();
        ftbquestsentityvis$spinMode = OverrideMode.fromName(buf.readUtf());
        ftbquestsentityvis$idleMode = OverrideMode.fromName(buf.readUtf());
        ftbquestsentityvis$walkMode = OverrideMode.fromName(buf.readUtf());
    }
}
