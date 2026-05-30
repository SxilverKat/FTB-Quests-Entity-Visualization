package com.sxilverr.ftbquestsentityvis.duck;

import net.minecraft.resources.ResourceLocation;

public interface IEntityImageVisOptions {
    boolean ftbquestsentityvis$isEntityVis();

    void ftbquestsentityvis$setEntityVis(boolean entityVis);

    ResourceLocation ftbquestsentityvis$getEntityId();

    void ftbquestsentityvis$setEntityId(ResourceLocation entityId);

    float ftbquestsentityvis$getVisSize();

    void ftbquestsentityvis$setVisSize(float size);

    float ftbquestsentityvis$getVisOffsetX();

    void ftbquestsentityvis$setVisOffsetX(float offset);

    float ftbquestsentityvis$getVisOffsetY();

    void ftbquestsentityvis$setVisOffsetY(float offset);

    float ftbquestsentityvis$getVisRotation();

    void ftbquestsentityvis$setVisRotation(float rotation);

    OverrideMode ftbquestsentityvis$getSpinMode();

    void ftbquestsentityvis$setSpinMode(OverrideMode mode);

    OverrideMode ftbquestsentityvis$getIdleMode();

    void ftbquestsentityvis$setIdleMode(OverrideMode mode);

    OverrideMode ftbquestsentityvis$getWalkMode();

    void ftbquestsentityvis$setWalkMode(OverrideMode mode);

    boolean ftbquestsentityvis$isSilhouette();

    void ftbquestsentityvis$setSilhouette(boolean silhouette);

    boolean ftbquestsentityvis$isIconDirty();

    void ftbquestsentityvis$setIconDirty(boolean dirty);
}
