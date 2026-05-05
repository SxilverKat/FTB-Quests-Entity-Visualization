package com.sxilverr.ftbquestsentityvis.client;

import com.sxilverr.ftbquestsentityvis.duck.SilhouetteMode;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.BooleanSupplier;

@OnlyIn(Dist.CLIENT)
public final class ClientStateUtil {
    private ClientStateUtil() {
    }

    public static BooleanSupplier silhouetteCheck(Task task, SilhouetteMode mode) {
        return () -> shouldRenderSilhouette(task, mode);
    }

    public static boolean shouldRenderSilhouette(Task task, SilhouetteMode mode) {
        if (mode == null || mode == SilhouetteMode.NONE || task == null) {
            return false;
        }
        ClientQuestFile file = ClientQuestFile.INSTANCE;
        if (file == null) {
            return false;
        }
        TeamData data = file.selfTeamData;
        if (data == null) {
            return false;
        }
        try {
            switch (mode) {
                case UNTIL_COMPLETED:
                    return !data.isCompleted(task);
                case UNTIL_AVAILABLE: {
                    Quest quest = task.getQuest();
                    return quest != null && !data.areDependenciesComplete(quest);
                }
                default:
                    return false;
            }
        } catch (Throwable ignored) {
            return false;
        }
    }
}
