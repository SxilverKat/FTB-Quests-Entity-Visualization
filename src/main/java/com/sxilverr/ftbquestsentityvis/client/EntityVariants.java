package com.sxilverr.ftbquestsentityvis.client;

import com.sxilverr.ftbquestsentityvis.duck.OverrideMode;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public final class EntityVariants {
    public record Variant(String label, String nbt) {
    }

    private static final String[] DYE_LABELS = {
            "White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray",
            "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"
    };

    private EntityVariants() {
    }

    public static void addNbtControls(ConfigGroup config, ResourceLocation entityId, String current, Consumer<String> setNbt) {
        String original = current == null ? "" : current.trim();

        config.addString("nbt", original, v -> {
            String nv = v == null ? "" : v.trim();
            if (!nv.equals(original)) {
                setNbt.accept(nv);
            }
        }, "").setNameKey("ftbquestsentityvis.config.nbt");

        List<Variant> variants = forEntity(entityId);
        if (variants.isEmpty()) {
            return;
        }

        List<Variant> options = new ArrayList<>();
        options.add(new Variant("Default", ""));
        options.addAll(variants);

        Variant selected = null;
        for (Variant option : options) {
            if (option.nbt().equals(original)) {
                selected = option;
                break;
            }
        }
        if (selected == null) {
            selected = new Variant("Custom", original);
            options.add(selected);
        }

        Variant def = selected;
        NameMap<Variant> nameMap = NameMap.of(def, options)
                .nameKey(Variant::label)
                .icon(v -> new EntityIcon(entityId, 1.0F, 0.0F, 0.0F, 0.0F,
                        OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL, OverrideMode.USE_GLOBAL, null, v.nbt()))
                .create();
        config.add("variant", new VariantConfig(entityId, options, nameMap), selected, v -> {
            if (!v.nbt().equals(original)) {
                setNbt.accept(v.nbt());
            }
        }, def)
                .setNameKey("ftbquestsentityvis.config.variant");
    }

    public static List<Variant> forEntity(ResourceLocation entityId) {
        if (entityId == null) {
            return List.of();
        }
        List<Variant> list = new ArrayList<>(specificVariants(entityId));
        Entity probe = createProbe(entityId);
        if (probe instanceof LivingEntity) {
            list.add(nametag("Dinnerbone", "Dinnerbone"));
            list.add(nametag("Grumm", "Grumm"));
            if (probe instanceof AgeableMob) {
                list.add(new Variant("Baby", "{Age:-24000}"));
            }
        }
        return list;
    }

    private static List<Variant> specificVariants(ResourceLocation entityId) {
        if (!"minecraft".equals(entityId.getNamespace())) {
            return List.of();
        }
        return switch (entityId.getPath()) {
            case "sheep" -> sheepVariants();
            case "shulker" -> intVariants("Color", DYE_LABELS);
            case "rabbit" -> rabbitVariants();
            case "horse" -> intVariants("Variant", "White", "Creamy", "Chestnut", "Brown", "Black", "Gray", "Dark Brown");
            case "llama", "trader_llama" -> intVariants("Variant", "Creamy", "White", "Brown", "Gray");
            case "parrot" -> intVariants("Variant", "Red", "Blue", "Green", "Cyan", "Gray");
            case "axolotl" -> intVariants("Variant", "Lucy", "Wild", "Gold", "Cyan", "Blue");
            case "fox" -> stringVariants("Type", "red", "snow");
            case "mooshroom" -> stringVariants("Type", "red", "brown");
            case "cat" -> catVariants();
            case "panda" -> pandaVariants();
            case "villager", "zombie_villager" -> villagerVariants();
            case "slime", "magma_cube" -> intVariants("Size", "Tiny", "Small", "Medium", "Large");
            case "pufferfish" -> intVariants("PuffState", "Deflated", "Half Puffed", "Fully Puffed");
            case "snow_golem" -> List.of(new Variant("With Pumpkin", ""), new Variant("Sheared", "{Pumpkin:0b}"));
            case "creeper" -> List.of(new Variant("Normal", ""), new Variant("Charged", "{powered:1b}"));
            default -> List.of();
        };
    }

    private static Entity createProbe(ResourceLocation entityId) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return null;
        }
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(entityId);
        if (type == null) {
            return null;
        }
        try {
            return type.create(level);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Variant nametag(String label, String name) {
        return new Variant(label, "{CustomName:'\"" + name + "\"'}");
    }

    private static List<Variant> intVariants(String key, String... labels) {
        List<Variant> list = new ArrayList<>(labels.length);
        for (int i = 0; i < labels.length; i++) {
            list.add(new Variant(labels[i], "{" + key + ":" + i + "}"));
        }
        return list;
    }

    private static List<Variant> stringVariants(String key, String... values) {
        List<Variant> list = new ArrayList<>(values.length);
        for (String value : values) {
            list.add(new Variant(capitalize(value), "{" + key + ":\"" + value + "\"}"));
        }
        return list;
    }

    private static List<Variant> sheepVariants() {
        List<Variant> list = new ArrayList<>(intVariants("Color", DYE_LABELS));
        list.add(nametag("jeb_", "jeb_"));
        return list;
    }

    private static List<Variant> rabbitVariants() {
        List<Variant> list = new ArrayList<>();
        String[] labels = {"Brown", "White", "Black", "Black & White", "Gold", "Salt & Pepper"};
        for (int i = 0; i < labels.length; i++) {
            list.add(new Variant(labels[i], "{RabbitType:" + i + "}"));
        }
        list.add(new Variant("The Killer Bunny", "{RabbitType:99}"));
        list.add(nametag("Toast", "Toast"));
        return list;
    }

    private static List<Variant> catVariants() {
        String[] types = {"tabby", "black", "red", "siamese", "british_shorthair",
                "calico", "persian", "ragdoll", "white", "jellie", "all_black"};
        List<Variant> list = new ArrayList<>(types.length);
        for (String type : types) {
            list.add(new Variant(capitalize(type), "{variant:\"minecraft:" + type + "\"}"));
        }
        return list;
    }

    private static List<Variant> pandaVariants() {
        String[] genes = {"normal", "lazy", "worried", "playful", "brown", "weak", "aggressive"};
        List<Variant> list = new ArrayList<>(genes.length);
        for (String gene : genes) {
            list.add(new Variant(capitalize(gene), "{MainGene:\"" + gene + "\",HiddenGene:\"" + gene + "\"}"));
        }
        return list;
    }

    private static List<Variant> villagerVariants() {
        String[] professions = {"none", "armorer", "butcher", "cartographer", "cleric", "farmer",
                "fisherman", "fletcher", "leatherworker", "librarian", "mason", "nitwit",
                "shepherd", "toolsmith", "weaponsmith"};
        List<Variant> list = new ArrayList<>(professions.length);
        for (String profession : professions) {
            list.add(new Variant(capitalize(profession),
                    "{VillagerData:{profession:\"minecraft:" + profession + "\",level:1,type:\"minecraft:plains\"}}"));
        }
        return list;
    }

    private static String capitalize(String value) {
        String cleaned = value.replace('_', ' ');
        String[] words = cleaned.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return sb.toString();
    }
}
