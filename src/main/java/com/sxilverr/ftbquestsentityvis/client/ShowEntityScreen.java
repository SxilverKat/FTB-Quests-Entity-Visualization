package com.sxilverr.ftbquestsentityvis.client;

import com.sxilverr.ftbquestsentityvis.duck.IEntityImageVisOptions;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.ChapterImage;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ShowEntityScreen {
    private ShowEntityScreen() {
    }

    public static void openCreate(QuestScreen questScreen, Chapter chapter, double qx, double qy) {
        ChapterImage image = new ChapterImage(chapter);
        IEntityImageVisOptions opts = (IEntityImageVisOptions) (Object) image;
        opts.ftbquestsentityvis$setEntityVis(true);
        image.setPosition(qx, qy);

        ConfigGroup group = new ConfigGroup("ftbquestsentityvis", accepted -> {
            if (accepted) {
                chapter.addImage(image);
                new EditObjectMessage(chapter).sendToServer();
            }
            questScreen.openGui();
        }) {
            @Override
            public Component getName() {
                return Component.translatable("ftbquestsentityvis.show_entity");
            }
        };

        image.fillConfigGroup(group.getOrCreateSubgroup("entity_image"));

        new EditConfigScreen(group) {
            @Override
            public Component getTitle() {
                return Component.translatable("ftbquestsentityvis.show_entity");
            }
        }.openGui();
    }
}
