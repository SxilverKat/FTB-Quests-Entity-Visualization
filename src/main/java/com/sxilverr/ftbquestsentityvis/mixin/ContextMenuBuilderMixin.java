package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.TaskEntityIconScreen;
import com.sxilverr.ftbquestsentityvis.duck.IKillTaskVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.ITaskIconVisOptions;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftbquests.client.gui.ContextMenuBuilder;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(ContextMenuBuilder.class)
public abstract class ContextMenuBuilderMixin {
    @Unique private static final Icon ftbquestsentityvis$SHOW_ENTITY_ICON = ItemIcon.getItemIcon(Items.ENDER_EYE);
    @Unique private static final Icon ftbquestsentityvis$REMOVE_ICON = ItemIcon.getItemIcon(Items.BARRIER);

    @Shadow(remap = false) @Final private QuestObjectBase object;
    @Shadow(remap = false) @Final private QuestScreen screen;

    @ModifyArg(
            method = "openContextMenu",
            at = @At(value = "INVOKE",
                    target = "Ldev/ftb/mods/ftblibrary/ui/BaseScreen;openContextMenu(Ljava/util/List;)Ldev/ftb/mods/ftblibrary/ui/ContextMenu;",
                    remap = false),
            remap = false)
    private List<ContextMenuItem> ftbquestsentityvis$addTaskEntityIcon(List<ContextMenuItem> menu) {
        if (!(object instanceof Task task)) {
            return menu;
        }

        List<ContextMenuItem> items = new ArrayList<>();
        if (object instanceof IKillTaskVisOptions) {
            items.add(new ContextMenuItem(
                    Component.translatable("ftbquestsentityvis.edit_entity_icon"),
                    ftbquestsentityvis$SHOW_ENTITY_ICON,
                    b -> TaskEntityIconScreen.openEntityTask(screen, task)));
        } else {
            ITaskIconVisOptions opts = (ITaskIconVisOptions) (Object) task;
            if (opts.ftbquestsentityvis$getIconEntityEnabled()) {
                items.add(new ContextMenuItem(
                        Component.translatable("ftbquestsentityvis.edit_entity_icon"),
                        ftbquestsentityvis$SHOW_ENTITY_ICON,
                        b -> TaskEntityIconScreen.openGeneric(screen, task)));
                items.add(new ContextMenuItem(
                        Component.translatable("ftbquestsentityvis.remove_entity_icon"),
                        ftbquestsentityvis$REMOVE_ICON,
                        b -> TaskEntityIconScreen.removeGeneric(screen, task)));
            } else {
                items.add(new ContextMenuItem(
                        Component.translatable("ftbquestsentityvis.show_entity_as_icon"),
                        ftbquestsentityvis$SHOW_ENTITY_ICON,
                        b -> TaskEntityIconScreen.openGeneric(screen, task)));
            }
        }

        List<ContextMenuItem> copy = new ArrayList<>(menu);
        copy.addAll(ftbquestsentityvis$insertIndex(copy), items);
        return copy;
    }

    @Unique
    private static int ftbquestsentityvis$insertIndex(List<ContextMenuItem> menu) {
        int idx = ftbquestsentityvis$indexOfKey(menu, "ftbquests.gui.use_as_quest_icon");
        if (idx < 0) {
            idx = ftbquestsentityvis$indexOfKey(menu, "ftbquests.gui.edit");
        }
        if (idx >= 0) {
            return idx + 1;
        }
        int separator = menu.indexOf(ContextMenuItem.SEPARATOR);
        return separator >= 0 ? separator + 1 : menu.size();
    }

    @Unique
    private static int ftbquestsentityvis$indexOfKey(List<ContextMenuItem> menu, String key) {
        for (int i = 0; i < menu.size(); i++) {
            Component title = menu.get(i).getTitle();
            if (title != null && title.getContents() instanceof TranslatableContents tc && key.equals(tc.getKey())) {
                return i;
            }
        }
        return -1;
    }
}
