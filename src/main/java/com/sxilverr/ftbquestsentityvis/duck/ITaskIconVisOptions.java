package com.sxilverr.ftbquestsentityvis.duck;

import net.minecraft.resources.ResourceLocation;

public interface ITaskIconVisOptions {
    boolean ftbquestsentityvis$getIconEntityEnabled();

    void ftbquestsentityvis$setIconEntityEnabled(boolean enabled);

    ResourceLocation ftbquestsentityvis$getIconEntityId();

    void ftbquestsentityvis$setIconEntityId(ResourceLocation id);

    float ftbquestsentityvis$getIconVisSize();

    void ftbquestsentityvis$setIconVisSize(float size);

    float ftbquestsentityvis$getIconVisOffsetX();

    void ftbquestsentityvis$setIconVisOffsetX(float offset);

    float ftbquestsentityvis$getIconVisOffsetY();

    void ftbquestsentityvis$setIconVisOffsetY(float offset);

    float ftbquestsentityvis$getIconVisRotation();

    void ftbquestsentityvis$setIconVisRotation(float rotation);

    OverrideMode ftbquestsentityvis$getIconSpinMode();

    void ftbquestsentityvis$setIconSpinMode(OverrideMode mode);

    OverrideMode ftbquestsentityvis$getIconIdleMode();

    void ftbquestsentityvis$setIconIdleMode(OverrideMode mode);

    OverrideMode ftbquestsentityvis$getIconWalkMode();

    void ftbquestsentityvis$setIconWalkMode(OverrideMode mode);

    SilhouetteMode ftbquestsentityvis$getIconSilhouetteMode();

    void ftbquestsentityvis$setIconSilhouetteMode(SilhouetteMode mode);

    boolean ftbquestsentityvis$getIconUseAsQuestIcon();

    void ftbquestsentityvis$setIconUseAsQuestIcon(boolean useAsQuestIcon);

    boolean ftbquestsentityvis$isIconDirty();

    void ftbquestsentityvis$setIconDirty(boolean dirty);
}
