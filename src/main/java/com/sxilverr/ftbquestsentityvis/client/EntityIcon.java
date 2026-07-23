package com.sxilverr.ftbquestsentityvis.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
//? if <1.21.1
import org.joml.Matrix4f;
import com.sxilverr.ftbquestsentityvis.Config;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.function.BooleanSupplier;

@Environment(EnvType.CLIENT)
public class EntityIcon extends Icon {
    private static final long SPIN_PERIOD_MS = 15000L;
    private static final long ANIM_TICK_INTERVAL_MS = 50L;
    private static final float WALK_ANIM_SPEED = 0.6F;

    private final ResourceLocation entityId;
    private final float sizeMultiplier;
    private final float offsetX;
    private final float offsetY;
    private final float rotationOffset;
    private final OverrideMode spinMode;
    private final OverrideMode idleMode;
    private final OverrideMode walkMode;
    private final BooleanSupplier silhouetteCheck;
    private final String nbt;

    private Entity cachedEntity;
    private Level cachedLevel;
    private boolean creationFailed;
    private long lastAnimTickMs;

    public EntityIcon(ResourceLocation entityId, float sizeMultiplier, float offsetX, float offsetY,
                      float rotationOffset, OverrideMode spinMode, OverrideMode idleMode, OverrideMode walkMode) {
        this(entityId, sizeMultiplier, offsetX, offsetY, rotationOffset, spinMode, idleMode, walkMode, null, "");
    }

    public EntityIcon(ResourceLocation entityId, float sizeMultiplier, float offsetX, float offsetY,
                      float rotationOffset, OverrideMode spinMode, OverrideMode idleMode, OverrideMode walkMode,
                      BooleanSupplier silhouetteCheck) {
        this(entityId, sizeMultiplier, offsetX, offsetY, rotationOffset, spinMode, idleMode, walkMode, silhouetteCheck, "");
    }

    public EntityIcon(ResourceLocation entityId, float sizeMultiplier, float offsetX, float offsetY,
                      float rotationOffset, OverrideMode spinMode, OverrideMode idleMode, OverrideMode walkMode,
                      BooleanSupplier silhouetteCheck, String nbt) {
        this.entityId = entityId;
        this.sizeMultiplier = sizeMultiplier;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.rotationOffset = rotationOffset;
        this.spinMode = spinMode;
        this.idleMode = idleMode;
        this.walkMode = walkMode;
        this.silhouetteCheck = silhouetteCheck;
        this.nbt = nbt == null ? "" : nbt.trim();
    }

    private Entity getEntity() {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level == null) {
            return null;
        }
        if (cachedLevel != level) {
            cachedLevel = level;
            cachedEntity = null;
            creationFailed = false;
            lastAnimTickMs = 0L;
        }
        if (cachedEntity != null) {
            return cachedEntity;
        }
        if (creationFailed) {
            return null;
        }
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityId);
        if (type == null) {
            creationFailed = true;
            return null;
        }
        try {
            Entity created = type.create(level);
            if (created != null) {
                applyNbt(created);
                cachedEntity = created;
                return created;
            }
        } catch (Throwable ignored) {
        }
        creationFailed = true;
        return null;
    }

    private void applyNbt(Entity entity) {
        if (nbt.isEmpty()) {
            return;
        }
        try {
            CompoundTag tag = TagParser.parseTag(nbt);
            entity.load(tag);
            entity.setPos(0.0D, 0.0D, 0.0D);
            entity.setDeltaMovement(0.0D, 0.0D, 0.0D);
        } catch (Throwable ignored) {
        }
    }

    private Icon fallbackIcon() {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityId);
        SpawnEggItem egg = type != null ? SpawnEggItem.byId(type) : null;
        return ItemIcon.getItemIcon(egg != null ? egg : Items.SPAWNER);
    }

    private void advanceAnimations(Entity entity) {
        boolean idle = idleMode.resolve(Config.idleAnimation);
        boolean walk = walkMode.resolve(Config.walkAnimation);
        if (!idle && !walk) {
            return;
        }
        long now = System.currentTimeMillis();
        if (lastAnimTickMs == 0L) {
            lastAnimTickMs = now;
            return;
        }
        long elapsed = now - lastAnimTickMs;
        if (elapsed < ANIM_TICK_INTERVAL_MS) {
            return;
        }
        long ticks;
        if (elapsed > ANIM_TICK_INTERVAL_MS * 4L) {
            ticks = 1L;
            lastAnimTickMs = now;
        } else {
            ticks = elapsed / ANIM_TICK_INTERVAL_MS;
            lastAnimTickMs += ticks * ANIM_TICK_INTERVAL_MS;
        }
        LivingEntity living = entity instanceof LivingEntity le ? le : null;
        for (long i = 0; i < ticks; i++) {
            if (idle) {
                entity.tickCount++;
            }
            if (walk && living != null) {
                living.walkAnimation.update(WALK_ANIM_SPEED, 1.0F);
            }
        }
    }

    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        Entity entity = getEntity();
        if (entity == null) {
            fallbackIcon().draw(graphics, x, y, w, h);
            return;
        }

        advanceAnimations(entity);

        float effectiveSize = QuestSizeContext.resolveSize(sizeMultiplier);
        float bbHeight = Math.max(entity.getBbHeight(), 0.1F);
        float bbWidth = Math.max(entity.getBbWidth(), 0.1F);
        float scale = Math.min(h / bbHeight, w / bbWidth) * Math.max(effectiveSize, 0.01F);
        if (scale <= 0.0F) {
            fallbackIcon().draw(graphics, x, y, w, h);
            return;
        }

        double cx = x + w / 2.0 + offsetX * w;
        double cy = y + h / 2.0 + bbHeight * scale / 2.0 - offsetY * h;

        boolean spinning = spinMode.resolve(Config.mobsSpin);
        float spinSpeed = (float) (double) Config.spinSpeed;
        float spin = spinning
                ? (System.currentTimeMillis() % SPIN_PERIOD_MS) / (float) SPIN_PERIOD_MS * 360.0F * spinSpeed
                : 0.0F;
        float yaw = spin + rotationOffset;
        float tilt = (float) (double) Config.tiltDegrees;

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(cx, cy, 64.0);
        //? if >=1.21.1 {
        /*pose.scale(scale, scale, -scale);*/
        //?} else {
        Matrix4f poseMatrix = pose.last().pose();
        float axisX = (float) Math.sqrt(poseMatrix.m00() * poseMatrix.m00()
                + poseMatrix.m10() * poseMatrix.m10()
                + poseMatrix.m20() * poseMatrix.m20());
        float axisZ = (float) Math.sqrt(poseMatrix.m02() * poseMatrix.m02()
                + poseMatrix.m12() * poseMatrix.m12()
                + poseMatrix.m22() * poseMatrix.m22());
        float depthFix = axisZ > 1.0E-5F ? axisX / axisZ : 1.0F;
        pose.scale(scale, scale, -scale * depthFix);
        //?}
        pose.mulPose(Axis.XP.rotationDegrees(180.0F + tilt));
        pose.mulPose(Axis.YP.rotationDegrees(yaw));

        LivingEntity living = entity instanceof LivingEntity le ? le : null;
        float prevYRot = entity.getYRot();
        float prevXRot = entity.getXRot();
        float prevYBodyRot = 0.0F;
        float prevYHeadRotO = 0.0F;
        float prevYHeadRot = 0.0F;
        if (living != null) {
            prevYBodyRot = living.yBodyRot;
            prevYHeadRotO = living.yHeadRotO;
            prevYHeadRot = living.yHeadRot;
            living.yBodyRot = 0.0F;
            living.yHeadRot = 0.0F;
            living.yHeadRotO = 0.0F;
        }
        entity.setYRot(0.0F);
        entity.setXRot(0.0F);

        boolean silhouette = silhouetteCheck != null && silhouetteCheck.getAsBoolean();
        int packedLight = silhouette || Config.fullBright ? LightTexture.FULL_BRIGHT : 15728640;

        //? if <1.21.1 {
        float[] prevShaderColor = RenderSystem.getShaderColor().clone();
        //?}

        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadow(false);
        try {
            //? if >=1.21.1 {
            /*if (silhouette) {
                RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
            }*/
            //?} else {
            float channel = silhouette ? 0.0F : 1.0F;
            RenderSystem.setShaderColor(channel, channel, channel, 1.0F);
            //?}
            RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, pose, graphics.bufferSource(), packedLight));
            graphics.flush();
        } catch (Throwable ignored) {
        } finally {
            //? if >=1.21.1 {
            /*if (silhouette) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }*/
            //?} else {
            RenderSystem.setShaderColor(prevShaderColor[0], prevShaderColor[1], prevShaderColor[2], prevShaderColor[3]);
            //?}
            dispatcher.setRenderShadow(true);
            pose.popPose();
            Lighting.setupFor3DItems();
            entity.setYRot(prevYRot);
            entity.setXRot(prevXRot);
            if (living != null) {
                living.yBodyRot = prevYBodyRot;
                living.yHeadRotO = prevYHeadRotO;
                living.yHeadRot = prevYHeadRot;
            }
            GuiHelper.setupDrawing();
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EntityIcon other
                && other.entityId.equals(entityId)
                && Float.compare(other.sizeMultiplier, sizeMultiplier) == 0
                && Float.compare(other.offsetX, offsetX) == 0
                && Float.compare(other.offsetY, offsetY) == 0
                && Float.compare(other.rotationOffset, rotationOffset) == 0
                && other.spinMode == spinMode
                && other.idleMode == idleMode
                && other.walkMode == walkMode
                && other.nbt.equals(nbt);
    }

    @Override
    public String toString() {
        return "entity:" + entityId + (nbt.isEmpty() ? "" : nbt);
    }

    @Override
    public int hashCode() {
        int h = entityId.hashCode();
        h = h * 31 + Float.hashCode(sizeMultiplier);
        h = h * 31 + Float.hashCode(offsetX);
        h = h * 31 + Float.hashCode(offsetY);
        h = h * 31 + Float.hashCode(rotationOffset);
        h = h * 31 + spinMode.ordinal();
        h = h * 31 + idleMode.ordinal();
        h = h * 31 + walkMode.ordinal();
        h = h * 31 + nbt.hashCode();
        return h;
    }
}
