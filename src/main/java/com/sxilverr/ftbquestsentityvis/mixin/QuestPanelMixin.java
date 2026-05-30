package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.ShowEntityScreen;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestPanel;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(QuestPanel.class)
public abstract class QuestPanelMixin {
    @Unique private static final Icon ftbquestsentityvis$SHOW_ENTITY_ICON = ItemIcon.getItemIcon(Items.ENDER_EYE);

    @Shadow(remap = false) @Final private QuestScreen questScreen;
    @Shadow(remap = false) protected double questX;
    @Shadow(remap = false) protected double questY;

    @ModifyArg(
            method = "mousePressed",
            at = @At(value = "INVOKE",
                    target = "Ldev/ftb/mods/ftbquests/client/gui/quests/QuestScreen;openContextMenu(Ljava/util/List;)Ldev/ftb/mods/ftblibrary/ui/ContextMenu;",
                    remap = false),
            remap = false)
    private List<ContextMenuItem> ftbquestsentityvis$addShowEntity(List<ContextMenuItem> menu) {
        Chapter chapter = ((QuestScreenAccessor) (Object) questScreen).ftbquestsentityvis$getSelectedChapter();
        if (chapter == null) {
            return menu;
        }

        final double qx = questX;
        final double qy = questY;
        ContextMenuItem item = new ContextMenuItem(
                Component.translatable("ftbquestsentityvis.show_entity"),
                ftbquestsentityvis$SHOW_ENTITY_ICON,
                b -> ShowEntityScreen.openCreate(questScreen, chapter, qx, qy));

        Component killName = TaskTypes.KILL.getDisplayName();
        int insertAt = -1;
        for (int i = 0; i < menu.size(); i++) {
            if (killName.equals(menu.get(i).getTitle())) {
                insertAt = i + 1;
                break;
            }
        }
        if (insertAt >= 0) {
            menu.add(insertAt, item);
        } else {
            int separator = menu.indexOf(ContextMenuItem.SEPARATOR);
            if (separator >= 0) {
                menu.add(separator, item);
            } else {
                menu.add(item);
            }
        }
        return menu;
    }
}
