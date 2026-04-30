package com.sxilverr.ftbquestsentityvis.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sxilverr.ftbquestsentityvis.Config;
import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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

    private Entity cachedEntity;
    private Level cachedLevel;
    private boolean creationFailed;
    private long lastAnimTickMs;

    public EntityIcon(ResourceLocation entityId, float sizeMultiplier, float offsetX, float offsetY,
                      float rotationOffset, OverrideMode spinMode, OverrideMode idleMode, OverrideMode walkMode) {
        this.entityId = entityId;
        this.sizeMultiplier = sizeMultiplier;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.rotationOffset = rotationOffset;
        this.spinMode = spinMode;
        this.idleMode = idleMode;
        this.walkMode = walkMode;
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
                cachedEntity = created;
                return created;
            }
        } catch (Throwable ignored) {
        }
        creationFailed = true;
        return null;
    }

    private Icon fallbackIcon() {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityId);
        SpawnEggItem egg = type != null ? SpawnEggItem.byId(type) : null;
        return ItemIcon.getItemIcon(egg != null ? egg : Items.SPAWNER);
    }

    private void advanceAnimations(Entity entity) {
        boolean idle = idleMode.resolve(Config.IDLE_ANIMATION.get());
        boolean walk = walkMode.resolve(Config.WALK_ANIMATION.get());
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
        long ticks = Math.min(elapsed / ANIM_TICK_INTERVAL_MS, 20L);
        lastAnimTickMs += ticks * ANIM_TICK_INTERVAL_MS;
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

        float bbHeight = Math.max(entity.getBbHeight(), 0.1F);
        float bbWidth = Math.max(entity.getBbWidth(), 0.1F);
        float scale = Math.min(h / bbHeight, w / bbWidth) * Math.max(sizeMultiplier, 0.01F);
        if (scale <= 0.0F) {
            fallbackIcon().draw(graphics, x, y, w, h);
            return;
        }

        double cx = x + w / 2.0 + offsetX * w;
        double cy = y + h / 2.0 + bbHeight * scale / 2.0 - offsetY * h;

        boolean spinning = spinMode.resolve(Config.MOBS_SPIN.get());
        float spinSpeed = (float) (double) Config.SPIN_SPEED.get();
        float spin = spinning
                ? (System.currentTimeMillis() % SPIN_PERIOD_MS) / (float) SPIN_PERIOD_MS * 360.0F * spinSpeed
                : 0.0F;
        float yaw = spin + rotationOffset;
        float tilt = (float) (double) Config.TILT_DEGREES.get();

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(cx, cy, 64.0);
        pose.scale(scale, scale, -scale);
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

        int packedLight = Config.FULL_BRIGHT.get() ? LightTexture.FULL_BRIGHT : 15728640;

        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadow(false);
        try {
            RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, pose, graphics.bufferSource(), packedLight));
            graphics.flush();
        } catch (Throwable ignored) {
        } finally {
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
                && other.walkMode == walkMode;
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
        return h;
    }
}
