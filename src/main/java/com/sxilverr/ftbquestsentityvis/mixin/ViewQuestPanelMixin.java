package com.sxilverr.ftbquestsentityvis.mixin;

import com.sxilverr.ftbquestsentityvis.client.EntityComponent;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.util.client.ImageComponent;
import dev.ftb.mods.ftbquests.client.gui.quests.ViewQuestPanel;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.translation.TranslationKey;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ViewQuestPanel.class)
public abstract class ViewQuestPanelMixin {
    @Unique private static final Icon ftbquestsentityvis$SHOW_ENTITY_ICON = ItemIcon.getItemIcon(Items.ENDER_EYE);

    @Shadow(remap = false) private Quest quest;

    @Shadow(remap = false) private int getCurrentPage() {
        throw new AssertionError();
    }

    @Shadow(remap = false) private void appendToPage(List<String> list, List<String> toAdd, int pageNumber) {
        throw new AssertionError();
    }

    @ModifyArg(
            method = "openEditButtonContextMenu",
            at = @At(value = "INVOKE",
                    target = "Ldev/ftb/mods/ftblibrary/ui/BaseScreen;openContextMenu(Ljava/util/List;)Ldev/ftb/mods/ftblibrary/ui/ContextMenu;",
                    remap = false),
            remap = false)
    private List<ContextMenuItem> ftbquestsentityvis$addShowEntity(List<ContextMenuItem> menu) {
        ContextMenuItem item = new ContextMenuItem(
                Component.translatable("ftbquestsentityvis.show_entity"),
                ftbquestsentityvis$SHOW_ENTITY_ICON,
                b -> ftbquestsentityvis$openEntityEditor(-1, new EntityComponent()));

        int insertAt = -1;
        for (int i = 0; i < menu.size(); i++) {
            Component title = menu.get(i).getTitle();
            if (title != null && title.getContents() instanceof TranslatableContents tc
                    && "ftbquests.gui.image".equals(tc.getKey())) {
                insertAt = i + 1;
                break;
            }
        }
        if (insertAt < 0) {
            int lastSeparator = menu.lastIndexOf(ContextMenuItem.SEPARATOR);
            insertAt = lastSeparator >= 0 ? lastSeparator : menu.size();
        }
        menu.add(insertAt, item);
        return menu;
    }

    @Inject(method = "editImage", at = @At("HEAD"), cancellable = true, remap = false)
    private void ftbquestsentityvis$interceptEntityEdit(int line, ImageComponent component, CallbackInfo ci) {
        if (component instanceof EntityComponent entity) {
            ftbquestsentityvis$openEntityEditor(line, entity);
            ci.cancel();
        }
    }

    @Unique
    private void ftbquestsentityvis$openEntityEditor(int line, EntityComponent component) {
        Panel self = (Panel) (Object) this;
        ConfigGroup group = new ConfigGroup("ftbquestsentityvis", accepted -> {
            self.getGui().openGui();
            if (accepted) {
                String serialized = component.toString();
                quest.modifyTranslatableListValue(TranslationKey.QUEST_DESC, list -> {
                    if (line == -1) {
                        appendToPage(list, List.of(serialized), getCurrentPage());
                    } else {
                        list.set(line, serialized);
                    }
                });
                self.refreshWidgets();
            }
        }) {
            @Override
            public Component getName() {
                return Component.translatable("ftbquestsentityvis.show_entity");
            }
        };

        component.fillConfig(group);

        new EditConfigScreen(group) {
            @Override
            public Component getTitle() {
                return Component.translatable("ftbquestsentityvis.show_entity");
            }
        }.openGui();
    }
}
