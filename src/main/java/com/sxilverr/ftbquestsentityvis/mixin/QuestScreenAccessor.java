package com.sxilverr.ftbquestsentityvis.mixin;

import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.quest.Chapter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(QuestScreen.class)
public interface QuestScreenAccessor {
    @Accessor(value = "selectedChapter", remap = false)
    Chapter ftbquestsentityvis$getSelectedChapter();
}
