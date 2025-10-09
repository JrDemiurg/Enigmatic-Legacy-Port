package net.jrdemiurge.enigmaticlegacy;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // TODO Возможно для понятности стоит переименовать damage received multiplier bonus и снизить на 100%
    public static final ModConfigSpec.IntValue PAIN_MODIFIER = BUILDER
            .comment("Defines how much damage bearers of the ring receive from any source. Measured as percentage.")
            .defineInRange("PainModifier", 200, 100, Integer.MAX_VALUE);

    // TODO Впервые такое вижу, что кольцо усиливает отбрасывание
    public static final ModConfigSpec.IntValue KNOCKBACK_DEBUFF = BUILDER
            .comment("How much knockback bearers of the ring take, measured in percents.")
            .defineInRange("KnockbackDebuff", 200, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue MONSTER_DAMAGE_DEBUFF = BUILDER
            .comment("How much damage monsters receive from bearers of the ring will be decreased, in percents.")
            .defineInRange("MonsterDamageDebuff", 50, 0, 100);

    public static final ModConfigSpec.IntValue ARMOR_DEBUFF = BUILDER
            .comment("How much less effective armor will be for those who bear the ring. Measured as percetage.")
            .defineInRange("ArmorDebuff", 30, 0, 100);

    public static final ModConfigSpec.IntValue FORTUNE_BONUS = BUILDER
            .comment("How many bonus Fortune levels ring provides")
            .defineInRange("FortuneBonus", 1, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue LOOTING_BONUS = BUILDER
            .comment("How many bonus Looting levels ring provides")
            .defineInRange("LootingBonus", 1, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLOCK_INTERACTION_RANGE_BONUS = BUILDER
            .comment("How many bonus Block Interaction Range ring provides")
            .defineInRange("BlockInteractionRangeBonus", 2.5, 0, Integer.MAX_VALUE);

    // TODO возможно минимальный показатель надо изменить до 0 если это бонус а не множитель
    public static final ModConfigSpec.IntValue EXPERIENCE_BONUS = BUILDER
            .comment("How much experience will drop from mobs to bearers of the ring, measured in percents.")
            .defineInRange("ExperienceBonus", 400, 100, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue ENCHANTING_BONUS = BUILDER
            .comment("How much additional Enchanting Power ring provides in Enchanting Table.")
            .defineInRange("EnchantingBonus", 10, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue DISABLE_INSOMNIA = BUILDER
            .comment("Set to true to prevent curse of insomnia from actually doing anything.")
            .define("DisableInsomnia", false);

    // TODO стоит изменить максимальное значение
    public static final ModConfigSpec.DoubleValue ENDERMAN_RANDOMPORT_FREQUENCY = BUILDER
            .comment("Allows to adjust how frequently Endermen will try to randomly teleport to player bearing the ring, even "
                    + "if they can't see the player and are not angered yet. Lower value = less probability of this happening.")
            .defineInRange("EndermenRandomportFrequency", 1D, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue ENDERMAN_RANDOMPORT_RANGE = BUILDER
            .comment("Range in which Endermen can try to randomly teleport to bearers of the ring.")
            .defineInRange("EndermenRandomportRange", 32D, 0, 120);

    // TODO Надо также сделать конфиг на список мобов
    public static final ModConfigSpec.DoubleValue NEUTRAL_ANGER_RANGE = BUILDER
            .comment("Range in which neutral creatures are angered against bearers of the ring.")
            .defineInRange("NeutralAngerRange", 24D, 0, 120);

    public static final ModConfigSpec.DoubleValue NEUTRAL_X_RAY_RANGE = BUILDER
            .comment("Range in which neutral creatures can see and target bearers of the ring even if they can't directly see them.")
            .defineInRange("NeutralXRayRange", 4D, 0, 120);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> NEUTRAL_ANGER_WHITELIST = BUILDER
            .comment("A list of neutral mobs that will be aggressive towards the player.")
            .defineListAllowEmpty("NeutralAngerWhitelist", List.of("minecraft:iron_golem", "minecraft:enderman"), () -> "", Config::validateEntityName);

    public static final ModConfigSpec.DoubleValue SUPER_CURSED_TIME = BUILDER
            .comment("A fraction of time the player should bear the Seven Curses to use Abyssal Artifacts.")
            .defineInRange("SuperCursedTime", 0.995, 0, 1);

/*    public static final ModConfigSpec.BooleanValue ULTRA_HARDCORE = BUILDER
            .comment("If true, Ring of the Seven Curses will be equipped into player's ring slot right away when "
                    + "entering a new world, instead of just being added to their inventory.")
            .define("UltraHardcore", false);*/

    public static final ModConfigSpec.BooleanValue AUTO_EQUIP = BUILDER
            .comment("If true, Ring of the Seven Curses will be equipped into player's ring slot right away when "
                    + "it enters their inventory.")
            .define("AutoEquip", false);

    // Это я добавлять не собираюсь
    /*public static final ModConfigSpec.BooleanValue ENABLE_SPECIAL_DROPS = BUILDER
            .comment("Set to false to disable ALL special drops that can be obtained from vanilla mobs when "
                    + "bearing Ring of the Seven Curses.")
            .define("EnableSpecialDrops", true);*/

/*
    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");
*/

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateEntityName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ENTITY_TYPE.containsKey(ResourceLocation.parse(itemName));
    }
}
