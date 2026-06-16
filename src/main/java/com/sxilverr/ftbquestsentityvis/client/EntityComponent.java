package com.sxilverr.ftbquestsentityvis.client;

import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.util.client.ImageComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class EntityComponent extends ImageComponent {
    public static final ResourceLocation DEFAULT_ENTITY = ResourceLocation.parse("minecraft:pig");

    public ResourceLocation entityId = DEFAULT_ENTITY;
    public float size = 1.0F;
    public float offsetX = 0.0F;
    public float offsetY = 0.0F;
    public float rotation = 0.0F;
    public OverrideMode spinMode = OverrideMode.USE_GLOBAL;
    public OverrideMode idleMode = OverrideMode.USE_GLOBAL;
    public OverrideMode walkMode = OverrideMode.USE_GLOBAL;
    public boolean silhouette = false;

    public EntityComponent() {
        setWidth(100);
        setHeight(100);
        setAlign(ImageAlign.CENTER);
        rebuildIcon();
    }

    public static EntityComponent fromProperties(Map<String, String> map) {
        EntityComponent c = new EntityComponent();
        if (map.containsKey("entity")) {
            c.entityId = ResourceLocation.parse(map.get("entity"));
        }
        c.size = parseFloat(map.get("ent_size"), 1.0F);
        c.offsetX = parseFloat(map.get("off_x"), 0.0F);
        c.offsetY = parseFloat(map.get("off_y"), 0.0F);
        c.rotation = parseFloat(map.get("rot"), 0.0F);
        c.spinMode = OverrideMode.fromName(map.getOrDefault("spin", "USE_GLOBAL"));
        c.idleMode = OverrideMode.fromName(map.getOrDefault("idle", "USE_GLOBAL"));
        c.walkMode = OverrideMode.fromName(map.getOrDefault("walk", "USE_GLOBAL"));
        c.silhouette = "true".equals(map.get("silhouette"));
        c.setWidth(parseInt(map.get("width"), 100));
        c.setHeight(parseInt(map.get("height"), 100));
        c.setAlign(ImageAlign.byName(map.getOrDefault("align", "center")));
        c.rebuildIcon();
        return c;
    }

    public void rebuildIcon() {
        setImage(new EntityIcon(entityId, size, offsetX, offsetY, rotation,
                spinMode, idleMode, walkMode, silhouette ? () -> true : null));
    }

    public void fillConfig(ConfigGroup config) {
        List<ResourceLocation> ids = new ArrayList<>(BuiltInRegistries.ENTITY_TYPE.keySet());
        config.addEnum("entity", entityId, v -> entityId = v,
                        NameMap.of(DEFAULT_ENTITY, ids)
                                .nameKey(v -> "entity." + v.getNamespace() + "." + v.getPath())
                                .icon(v -> new EntityIcon(v, 1.0F, 0.0F, 0.0F, 0.0F,
                                        OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL))
                                .create(), DEFAULT_ENTITY)
                .setNameKey("ftbquestsentityvis.config.entity");

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

        config.addBool("silhouette", silhouette, v -> silhouette = v, false)
                .setNameKey("ftbquestsentityvis.config.silhouette");

        config.addInt("width", getWidth(), this::setWidth, 100, 1, 1000)
                .setNameKey("ftbquestsentityvis.config.width");
        config.addInt("height", getHeight(), this::setHeight, 100, 1, 1000)
                .setNameKey("ftbquestsentityvis.config.height");
        config.addEnum("align", getAlign(), this::setAlign, ImageAlign.NAME_MAP, ImageAlign.CENTER)
                .setNameKey("ftbquestsentityvis.config.align");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{entity:");
        sb.append(entityId);
        sb.append(" ent_size:").append(size);
        sb.append(" off_x:").append(offsetX);
        sb.append(" off_y:").append(offsetY);
        sb.append(" rot:").append(rotation);
        sb.append(" spin:").append(spinMode.name());
        sb.append(" idle:").append(idleMode.name());
        sb.append(" walk:").append(walkMode.name());
        sb.append(" width:").append(getWidth());
        sb.append(" height:").append(getHeight());
        sb.append(" align:").append(getAlign().getName());
        if (silhouette) {
            sb.append(" silhouette:true");
        }
        sb.append('}');
        return sb.toString();
    }

    private static NameMap<OverrideMode> overrideNameMap(String key) {
        return NameMap.of(OverrideMode.USE_GLOBAL, OverrideMode.values())
                .nameKey(v -> "ftbquestsentityvis.config." + key + "." + v.name().toLowerCase())
                .create();
    }

    private static float parseFloat(String s, float def) {
        if (s == null) {
            return def;
        }
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static int parseInt(String s, int def) {
        if (s == null) {
            return def;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
