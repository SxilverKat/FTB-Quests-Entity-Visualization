package com.sxilverr.ftbquestsentityvis.client;

import com.sxilverr.ftbquestsentityvis.client.EntityVariants.Variant;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.EnumConfig;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.ButtonListBaseScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;

@Environment(EnvType.CLIENT)
public class VariantConfig extends EnumConfig<Variant> {
    private final ResourceLocation entityId;
    private final List<Variant> options;

    public VariantConfig(ResourceLocation entityId, List<Variant> options, NameMap<Variant> nameMap) {
        super(nameMap);
        this.entityId = entityId;
        this.options = options;
    }

    @Override
    public void onClicked(Widget widget, MouseButton mouseButton, ConfigCallback callback) {
        if (!getCanEdit()) {
            return;
        }
        new ButtonListBaseScreen() {
            {
                setTitle(Component.translatable("ftbquestsentityvis.config.variant"));
                setHasSearchBox(true);
            }

            @Override
            public void addButtons(Panel panel) {
                for (Variant variant : options) {
                    Icon icon = new EntityIcon(entityId, 1.0F, 0.0F, 0.0F, 0.0F,
                            OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL, null, variant.nbt());
                    panel.add(new SimpleTextButton(panel, Component.literal(variant.label()), icon) {
                        @Override
                        public void onClicked(MouseButton button) {
                            playClickSound();
                            VariantConfig.this.setCurrentValue(variant);
                            callback.save(true);
                        }
                    });
                }
            }
        }.openGui();
    }
}
