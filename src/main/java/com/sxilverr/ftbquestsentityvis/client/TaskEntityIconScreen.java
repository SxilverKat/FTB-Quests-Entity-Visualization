package com.sxilverr.ftbquestsentityvis.client;
import com.sxilverr.ftbquestsentityvis.ModUtil;

import com.sxilverr.ftbquestsentityvis.duck.IKillTaskVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.ITaskIconVisOptions;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import com.sxilverr.ftbquestsentityvis.duck.SilhouetteMode;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class TaskEntityIconScreen {
    private static final ResourceLocation DEFAULT_ENTITY = ModUtil.rl("minecraft:pig");

    private TaskEntityIconScreen() {
    }

    public static void openGeneric(QuestScreen questScreen, Task task) {
        ITaskIconVisOptions opts = (ITaskIconVisOptions) task;
        Holder holder = new Holder();
        holder.hasEntityPicker = true;
        holder.entityId = opts.ftbquestsentityvis$getIconEntityId() != null ? opts.ftbquestsentityvis$getIconEntityId() : DEFAULT_ENTITY;
        holder.size = opts.ftbquestsentityvis$getIconVisSize();
        holder.offsetX = opts.ftbquestsentityvis$getIconVisOffsetX();
        holder.offsetY = opts.ftbquestsentityvis$getIconVisOffsetY();
        holder.rotation = opts.ftbquestsentityvis$getIconVisRotation();
        holder.spinMode = opts.ftbquestsentityvis$getIconSpinMode();
        holder.idleMode = opts.ftbquestsentityvis$getIconIdleMode();
        holder.walkMode = opts.ftbquestsentityvis$getIconWalkMode();
        holder.silhouetteMode = opts.ftbquestsentityvis$getIconSilhouetteMode();
        holder.useAsQuestIcon = opts.ftbquestsentityvis$getIconUseAsQuestIcon();
        holder.nbt = opts.ftbquestsentityvis$getIconNbt();
        openEditor(questScreen, task, holder, () -> {
            opts.ftbquestsentityvis$setIconEntityId(holder.entityId);
            opts.ftbquestsentityvis$setIconVisSize(holder.size);
            opts.ftbquestsentityvis$setIconVisOffsetX(holder.offsetX);
            opts.ftbquestsentityvis$setIconVisOffsetY(holder.offsetY);
            opts.ftbquestsentityvis$setIconVisRotation(holder.rotation);
            opts.ftbquestsentityvis$setIconSpinMode(holder.spinMode);
            opts.ftbquestsentityvis$setIconIdleMode(holder.idleMode);
            opts.ftbquestsentityvis$setIconWalkMode(holder.walkMode);
            opts.ftbquestsentityvis$setIconSilhouetteMode(holder.silhouetteMode);
            opts.ftbquestsentityvis$setIconUseAsQuestIcon(holder.useAsQuestIcon);
            opts.ftbquestsentityvis$setIconNbt(holder.nbt);
            opts.ftbquestsentityvis$setIconEntityEnabled(true);
            opts.ftbquestsentityvis$setIconDirty(true);
        });
    }

    public static void removeGeneric(QuestScreen questScreen, Task task) {
        ITaskIconVisOptions opts = (ITaskIconVisOptions) task;
        opts.ftbquestsentityvis$setIconEntityEnabled(false);
        opts.ftbquestsentityvis$setIconDirty(true);
        task.clearCachedData();
        //? if >=1.21.1 {
        /*EditObjectMessage.sendToServer(task);*/
        //?} else {
        new EditObjectMessage(task).sendToServer();
        //?}
        questScreen.openGui();
    }

    public static void openEntityTask(QuestScreen questScreen, Task task) {
        IKillTaskVisOptions opts = (IKillTaskVisOptions) task;
        Holder holder = new Holder();
        holder.hasEntityPicker = false;
        holder.size = opts.ftbquestsentityvis$getVisSize();
        holder.offsetX = opts.ftbquestsentityvis$getVisOffsetX();
        holder.offsetY = opts.ftbquestsentityvis$getVisOffsetY();
        holder.rotation = opts.ftbquestsentityvis$getVisRotation();
        holder.spinMode = opts.ftbquestsentityvis$getSpinMode();
        holder.idleMode = opts.ftbquestsentityvis$getIdleMode();
        holder.walkMode = opts.ftbquestsentityvis$getWalkMode();
        holder.silhouetteMode = opts.ftbquestsentityvis$getSilhouetteMode();
        holder.useAsQuestIcon = opts.ftbquestsentityvis$getUseAsQuestIcon();
        holder.nbt = opts.ftbquestsentityvis$getVisNbt();
        holder.entityId = opts.ftbquestsentityvis$getVisEntityId();
        openEditor(questScreen, task, holder, () -> {
            opts.ftbquestsentityvis$setVisSize(holder.size);
            opts.ftbquestsentityvis$setVisOffsetX(holder.offsetX);
            opts.ftbquestsentityvis$setVisOffsetY(holder.offsetY);
            opts.ftbquestsentityvis$setVisRotation(holder.rotation);
            opts.ftbquestsentityvis$setSpinMode(holder.spinMode);
            opts.ftbquestsentityvis$setIdleMode(holder.idleMode);
            opts.ftbquestsentityvis$setWalkMode(holder.walkMode);
            opts.ftbquestsentityvis$setSilhouetteMode(holder.silhouetteMode);
            opts.ftbquestsentityvis$setUseAsQuestIcon(holder.useAsQuestIcon);
            opts.ftbquestsentityvis$setVisNbt(holder.nbt);
        });
    }

    private static void openEditor(QuestScreen questScreen, Task task, Holder holder, Runnable apply) {
        ConfigGroup group = new ConfigGroup("ftbquestsentityvis", accepted -> {
            if (accepted) {
                apply.run();
                task.clearCachedData();
                //? if >=1.21.1 {
                /*EditObjectMessage.sendToServer(task);*/
                //?} else {
                new EditObjectMessage(task).sendToServer();
                //?}
            }
            questScreen.openGui();
        }) {
            @Override
            public Component getName() {
                return Component.translatable("ftbquestsentityvis.edit_entity_icon");
            }
        };

        holder.fillConfig(group);

        new EditConfigScreen(group) {
            @Override
            public Component getTitle() {
                return Component.translatable("ftbquestsentityvis.edit_entity_icon");
            }
        }.openGui();
    }

    private static final class Holder {
        private boolean hasEntityPicker;
        private ResourceLocation entityId = DEFAULT_ENTITY;
        private float size = 1.0F;
        private float offsetX = 0.0F;
        private float offsetY = 0.0F;
        private float rotation = 0.0F;
        private OverrideMode spinMode = OverrideMode.USE_GLOBAL;
        private OverrideMode idleMode = OverrideMode.USE_GLOBAL;
        private OverrideMode walkMode = OverrideMode.USE_GLOBAL;
        private SilhouetteMode silhouetteMode = SilhouetteMode.NONE;
        private boolean useAsQuestIcon = false;
        private String nbt = "";

        private void fillConfig(ConfigGroup config) {
            if (hasEntityPicker) {
                List<ResourceLocation> ids = new ArrayList<>(BuiltInRegistries.ENTITY_TYPE.keySet());
                config.addEnum("entity", entityId, v -> entityId = v,
                                NameMap.of(DEFAULT_ENTITY, ids)
                                        .nameKey(v -> "entity." + v.getNamespace() + "." + v.getPath())
                                        .icon(v -> new EntityIcon(v, 1.0F, 0.0F, 0.0F, 0.0F,
                                                OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL))
                                        .create(), DEFAULT_ENTITY)
                        .setNameKey("ftbquestsentityvis.config.entity");
            }

            EntityVariants.addNbtControls(config, entityId, nbt, v -> nbt = v);

            config.addDouble("size", size, v -> size = v.floatValue(), 1.0D, 0.0D, 10.0D)
                    .setNameKey("ftbquestsentityvis.config.size");
            config.addDouble("offset_x", offsetX, v -> offsetX = v.floatValue(), 0.0D, -2.0D, 2.0D)
                    .setNameKey("ftbquestsentityvis.config.offset_x");
            config.addDouble("offset_y", offsetY, v -> offsetY = v.floatValue(), 0.0D, -2.0D, 2.0D)
                    .setNameKey("ftbquestsentityvis.config.offset_y");
            config.addDouble("rotation", rotation, v -> rotation = v.floatValue(), 0.0D, -180.0D, 180.0D)
                    .setNameKey("ftbquestsentityvis.config.rotation");

            config.addEnum("spin_mode", spinMode, v -> spinMode = v, overrideNameMap("spin_mode"), OverrideMode.USE_GLOBAL)
                    .setNameKey("ftbquestsentityvis.config.spin_mode");
            config.addEnum("idle_mode", idleMode, v -> idleMode = v, overrideNameMap("idle_mode"), OverrideMode.USE_GLOBAL)
                    .setNameKey("ftbquestsentityvis.config.idle_mode");
            config.addEnum("walk_mode", walkMode, v -> walkMode = v, overrideNameMap("walk_mode"), OverrideMode.USE_GLOBAL)
                    .setNameKey("ftbquestsentityvis.config.walk_mode");

            config.addEnum("silhouette_mode", silhouetteMode, v -> silhouetteMode = v, silhouetteNameMap(), SilhouetteMode.NONE)
                    .setNameKey("ftbquestsentityvis.config.silhouette_mode");

            config.addBool("use_as_quest_icon", useAsQuestIcon, v -> useAsQuestIcon = v, false)
                    .setNameKey("ftbquestsentityvis.config.use_as_quest_icon");
        }

        private static NameMap<OverrideMode> overrideNameMap(String key) {
            return NameMap.of(OverrideMode.USE_GLOBAL, OverrideMode.values())
                    .nameKey(v -> "ftbquestsentityvis.config." + key + "." + v.name().toLowerCase())
                    .create();
        }

        private static NameMap<SilhouetteMode> silhouetteNameMap() {
            return NameMap.of(SilhouetteMode.NONE, SilhouetteMode.values())
                    .nameKey(v -> "ftbquestsentityvis.config.silhouette_mode." + v.name().toLowerCase())
                    .create();
        }
    }
}
