package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.duck.ITaskIconVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import com.sxilverr.ftbquestsentityvis.duck.SilhouetteMode;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Task.class)
public abstract class TaskMixin implements ITaskIconVisOptions {
    @Unique private static final String ftbquestsentityvis$KEY_ROOT = "ftbquestsentityvis_icon";
    @Unique private static final String ftbquestsentityvis$KEY_ENABLED = "enabled";
    @Unique private static final String ftbquestsentityvis$KEY_ENTITY = "entity";
    @Unique private static final String ftbquestsentityvis$KEY_SIZE = "size";
    @Unique private static final String ftbquestsentityvis$KEY_OFFSET_X = "offset_x";
    @Unique private static final String ftbquestsentityvis$KEY_OFFSET_Y = "offset_y";
    @Unique private static final String ftbquestsentityvis$KEY_ROTATION = "rotation";
    @Unique private static final String ftbquestsentityvis$KEY_SPIN_MODE = "spin_mode";
    @Unique private static final String ftbquestsentityvis$KEY_IDLE_MODE = "idle_mode";
    @Unique private static final String ftbquestsentityvis$KEY_WALK_MODE = "walk_mode";
    @Unique private static final String ftbquestsentityvis$KEY_SILHOUETTE_MODE = "silhouette_mode";
    @Unique private static final String ftbquestsentityvis$KEY_USE_AS_QUEST_ICON = "use_as_quest_icon";

    @Unique private boolean ftbquestsentityvis$iconEnabled = false;
    @Unique private ResourceLocation ftbquestsentityvis$iconEntity = null;
    @Unique private float ftbquestsentityvis$iconSize = 1.0F;
    @Unique private float ftbquestsentityvis$iconOffsetX = 0.0F;
    @Unique private float ftbquestsentityvis$iconOffsetY = 0.0F;
    @Unique private float ftbquestsentityvis$iconRotation = 0.0F;
    @Unique private OverrideMode ftbquestsentityvis$iconSpinMode = OverrideMode.USE_GLOBAL;
    @Unique private OverrideMode ftbquestsentityvis$iconIdleMode = OverrideMode.USE_GLOBAL;
    @Unique private OverrideMode ftbquestsentityvis$iconWalkMode = OverrideMode.USE_GLOBAL;
    @Unique private SilhouetteMode ftbquestsentityvis$iconSilhouetteMode = SilhouetteMode.NONE;
    @Unique private boolean ftbquestsentityvis$iconUseAsQuestIcon = false;
    @Unique private boolean ftbquestsentityvis$iconDirty = false;

    @Override public boolean ftbquestsentityvis$getIconEntityEnabled() { return ftbquestsentityvis$iconEnabled; }
    @Override public void ftbquestsentityvis$setIconEntityEnabled(boolean enabled) { this.ftbquestsentityvis$iconEnabled = enabled; this.ftbquestsentityvis$iconDirty = true; }

    @Override public ResourceLocation ftbquestsentityvis$getIconEntityId() { return ftbquestsentityvis$iconEntity; }
    @Override public void ftbquestsentityvis$setIconEntityId(ResourceLocation id) { this.ftbquestsentityvis$iconEntity = id; this.ftbquestsentityvis$iconDirty = true; }

    @Override public float ftbquestsentityvis$getIconVisSize() { return ftbquestsentityvis$iconSize; }
    @Override public void ftbquestsentityvis$setIconVisSize(float size) { this.ftbquestsentityvis$iconSize = size; this.ftbquestsentityvis$iconDirty = true; }

    @Override public float ftbquestsentityvis$getIconVisOffsetX() { return ftbquestsentityvis$iconOffsetX; }
    @Override public void ftbquestsentityvis$setIconVisOffsetX(float offset) { this.ftbquestsentityvis$iconOffsetX = offset; this.ftbquestsentityvis$iconDirty = true; }

    @Override public float ftbquestsentityvis$getIconVisOffsetY() { return ftbquestsentityvis$iconOffsetY; }
    @Override public void ftbquestsentityvis$setIconVisOffsetY(float offset) { this.ftbquestsentityvis$iconOffsetY = offset; this.ftbquestsentityvis$iconDirty = true; }

    @Override public float ftbquestsentityvis$getIconVisRotation() { return ftbquestsentityvis$iconRotation; }
    @Override public void ftbquestsentityvis$setIconVisRotation(float rotation) { this.ftbquestsentityvis$iconRotation = rotation; this.ftbquestsentityvis$iconDirty = true; }

    @Override public OverrideMode ftbquestsentityvis$getIconSpinMode() { return ftbquestsentityvis$iconSpinMode; }
    @Override public void ftbquestsentityvis$setIconSpinMode(OverrideMode mode) { this.ftbquestsentityvis$iconSpinMode = mode; this.ftbquestsentityvis$iconDirty = true; }

    @Override public OverrideMode ftbquestsentityvis$getIconIdleMode() { return ftbquestsentityvis$iconIdleMode; }
    @Override public void ftbquestsentityvis$setIconIdleMode(OverrideMode mode) { this.ftbquestsentityvis$iconIdleMode = mode; this.ftbquestsentityvis$iconDirty = true; }

    @Override public OverrideMode ftbquestsentityvis$getIconWalkMode() { return ftbquestsentityvis$iconWalkMode; }
    @Override public void ftbquestsentityvis$setIconWalkMode(OverrideMode mode) { this.ftbquestsentityvis$iconWalkMode = mode; this.ftbquestsentityvis$iconDirty = true; }

    @Override public SilhouetteMode ftbquestsentityvis$getIconSilhouetteMode() { return ftbquestsentityvis$iconSilhouetteMode; }
    @Override public void ftbquestsentityvis$setIconSilhouetteMode(SilhouetteMode mode) { this.ftbquestsentityvis$iconSilhouetteMode = mode; this.ftbquestsentityvis$iconDirty = true; }

    @Override public boolean ftbquestsentityvis$getIconUseAsQuestIcon() { return ftbquestsentityvis$iconUseAsQuestIcon; }
    @Override public void ftbquestsentityvis$setIconUseAsQuestIcon(boolean useAsQuestIcon) { this.ftbquestsentityvis$iconUseAsQuestIcon = useAsQuestIcon; this.ftbquestsentityvis$iconDirty = true; }

    @Override public boolean ftbquestsentityvis$isIconDirty() { return ftbquestsentityvis$iconDirty; }
    @Override public void ftbquestsentityvis$setIconDirty(boolean dirty) { this.ftbquestsentityvis$iconDirty = dirty; }

    @Inject(method = "writeData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$writeData(CompoundTag nbt, HolderLookup.Provider provider, CallbackInfo ci) {
        if (!ftbquestsentityvis$iconEnabled && ftbquestsentityvis$iconEntity == null) {
            return;
        }
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(ftbquestsentityvis$KEY_ENABLED, ftbquestsentityvis$iconEnabled);
        if (ftbquestsentityvis$iconEntity != null) {
            tag.putString(ftbquestsentityvis$KEY_ENTITY, ftbquestsentityvis$iconEntity.toString());
        }
        tag.putFloat(ftbquestsentityvis$KEY_SIZE, ftbquestsentityvis$iconSize);
        tag.putFloat(ftbquestsentityvis$KEY_OFFSET_X, ftbquestsentityvis$iconOffsetX);
        tag.putFloat(ftbquestsentityvis$KEY_OFFSET_Y, ftbquestsentityvis$iconOffsetY);
        tag.putFloat(ftbquestsentityvis$KEY_ROTATION, ftbquestsentityvis$iconRotation);
        tag.putString(ftbquestsentityvis$KEY_SPIN_MODE, ftbquestsentityvis$iconSpinMode.name());
        tag.putString(ftbquestsentityvis$KEY_IDLE_MODE, ftbquestsentityvis$iconIdleMode.name());
        tag.putString(ftbquestsentityvis$KEY_WALK_MODE, ftbquestsentityvis$iconWalkMode.name());
        tag.putString(ftbquestsentityvis$KEY_SILHOUETTE_MODE, ftbquestsentityvis$iconSilhouetteMode.name());
        tag.putBoolean(ftbquestsentityvis$KEY_USE_AS_QUEST_ICON, ftbquestsentityvis$iconUseAsQuestIcon);
        nbt.put(ftbquestsentityvis$KEY_ROOT, tag);
    }

    @Inject(method = "readData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$readData(CompoundTag nbt, HolderLookup.Provider provider, CallbackInfo ci) {
        if (!nbt.contains(ftbquestsentityvis$KEY_ROOT)) {
            ftbquestsentityvis$iconEnabled = false;
            ftbquestsentityvis$iconEntity = null;
            ftbquestsentityvis$iconDirty = true;
            return;
        }
        CompoundTag tag = nbt.getCompound(ftbquestsentityvis$KEY_ROOT);
        ftbquestsentityvis$iconEnabled = tag.getBoolean(ftbquestsentityvis$KEY_ENABLED);
        ftbquestsentityvis$iconEntity = tag.contains(ftbquestsentityvis$KEY_ENTITY)
                ? ResourceLocation.tryParse(tag.getString(ftbquestsentityvis$KEY_ENTITY)) : null;
        ftbquestsentityvis$iconSize = tag.contains(ftbquestsentityvis$KEY_SIZE) ? tag.getFloat(ftbquestsentityvis$KEY_SIZE) : 1.0F;
        ftbquestsentityvis$iconOffsetX = tag.contains(ftbquestsentityvis$KEY_OFFSET_X) ? tag.getFloat(ftbquestsentityvis$KEY_OFFSET_X) : 0.0F;
        ftbquestsentityvis$iconOffsetY = tag.contains(ftbquestsentityvis$KEY_OFFSET_Y) ? tag.getFloat(ftbquestsentityvis$KEY_OFFSET_Y) : 0.0F;
        ftbquestsentityvis$iconRotation = tag.contains(ftbquestsentityvis$KEY_ROTATION) ? tag.getFloat(ftbquestsentityvis$KEY_ROTATION) : 0.0F;
        ftbquestsentityvis$iconSpinMode = OverrideMode.fromName(tag.getString(ftbquestsentityvis$KEY_SPIN_MODE));
        ftbquestsentityvis$iconIdleMode = OverrideMode.fromName(tag.getString(ftbquestsentityvis$KEY_IDLE_MODE));
        ftbquestsentityvis$iconWalkMode = OverrideMode.fromName(tag.getString(ftbquestsentityvis$KEY_WALK_MODE));
        ftbquestsentityvis$iconSilhouetteMode = SilhouetteMode.fromName(tag.getString(ftbquestsentityvis$KEY_SILHOUETTE_MODE));
        ftbquestsentityvis$iconUseAsQuestIcon = tag.getBoolean(ftbquestsentityvis$KEY_USE_AS_QUEST_ICON);
        ftbquestsentityvis$iconDirty = true;
    }

    @Inject(method = "writeNetData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$writeNetData(RegistryFriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeBoolean(ftbquestsentityvis$iconEnabled);
        boolean hasEntity = ftbquestsentityvis$iconEntity != null;
        buf.writeBoolean(hasEntity);
        if (hasEntity) {
            buf.writeUtf(ftbquestsentityvis$iconEntity.toString());
        }
        buf.writeFloat(ftbquestsentityvis$iconSize);
        buf.writeFloat(ftbquestsentityvis$iconOffsetX);
        buf.writeFloat(ftbquestsentityvis$iconOffsetY);
        buf.writeFloat(ftbquestsentityvis$iconRotation);
        buf.writeUtf(ftbquestsentityvis$iconSpinMode.name());
        buf.writeUtf(ftbquestsentityvis$iconIdleMode.name());
        buf.writeUtf(ftbquestsentityvis$iconWalkMode.name());
        buf.writeUtf(ftbquestsentityvis$iconSilhouetteMode.name());
        buf.writeBoolean(ftbquestsentityvis$iconUseAsQuestIcon);
    }

    @Inject(method = "readNetData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$readNetData(RegistryFriendlyByteBuf buf, CallbackInfo ci) {
        ftbquestsentityvis$iconEnabled = buf.readBoolean();
        ftbquestsentityvis$iconEntity = buf.readBoolean() ? ResourceLocation.tryParse(buf.readUtf()) : null;
        ftbquestsentityvis$iconSize = buf.readFloat();
        ftbquestsentityvis$iconOffsetX = buf.readFloat();
        ftbquestsentityvis$iconOffsetY = buf.readFloat();
        ftbquestsentityvis$iconRotation = buf.readFloat();
        ftbquestsentityvis$iconSpinMode = OverrideMode.fromName(buf.readUtf());
        ftbquestsentityvis$iconIdleMode = OverrideMode.fromName(buf.readUtf());
        ftbquestsentityvis$iconWalkMode = OverrideMode.fromName(buf.readUtf());
        ftbquestsentityvis$iconSilhouetteMode = SilhouetteMode.fromName(buf.readUtf());
        ftbquestsentityvis$iconUseAsQuestIcon = buf.readBoolean();
        ftbquestsentityvis$iconDirty = true;
    }
}
