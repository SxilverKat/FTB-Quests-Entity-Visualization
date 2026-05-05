package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.duck.IQuestVisOptions;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Quest.class)
public abstract class QuestMixin implements IQuestVisOptions {
    @Unique private static final String ftbquestsentityvis$KEY_QUEST_SIZE = "entity_vis_size";

    @Unique private float ftbquestsentityvis$questVisSize = 1.0F;

    @Override public float ftbquestsentityvis$getQuestVisSize() { return ftbquestsentityvis$questVisSize; }
    @Override public void ftbquestsentityvis$setQuestVisSize(float size) { this.ftbquestsentityvis$questVisSize = size; }

    @Inject(method = "writeData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$writeData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putFloat(ftbquestsentityvis$KEY_QUEST_SIZE, ftbquestsentityvis$questVisSize);
    }

    @Inject(method = "readData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$readData(CompoundTag nbt, CallbackInfo ci) {
        ftbquestsentityvis$questVisSize = nbt.contains(ftbquestsentityvis$KEY_QUEST_SIZE) ? nbt.getFloat(ftbquestsentityvis$KEY_QUEST_SIZE) : 1.0F;
    }

    @Inject(method = "writeNetData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$writeNetData(FriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeFloat(ftbquestsentityvis$questVisSize);
    }

    @Inject(method = "readNetData", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$readNetData(FriendlyByteBuf buf, CallbackInfo ci) {
        ftbquestsentityvis$questVisSize = buf.readFloat();
    }

    @Inject(method = "fillConfigGroup", at = @At("TAIL"), remap = false)
    private void ftbquestsentityvis$fillConfigGroup(ConfigGroup config, CallbackInfo ci) {
        ConfigGroup appearance = config.getOrCreateSubgroup("appearance");
        appearance.addDouble("entity_vis_size", ftbquestsentityvis$questVisSize,
                v -> ftbquestsentityvis$questVisSize = v.floatValue(),
                1.0D, 0.0D, 10.0D);
    }
}
