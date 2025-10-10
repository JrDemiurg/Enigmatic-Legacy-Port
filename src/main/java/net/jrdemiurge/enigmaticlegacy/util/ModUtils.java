package net.jrdemiurge.enigmaticlegacy.util;

import net.jrdemiurge.enigmaticlegacy.Config;
import net.jrdemiurge.enigmaticlegacy.item.ModItems;
import net.jrdemiurge.enigmaticlegacy.item.custom.SoulCrystal;
import net.jrdemiurge.enigmaticlegacy.stat.ModStats;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableBoolean;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModUtils {
    private static long lastRequestMs = 0L;
    private static final long COOLDOWN_MS = 5000L;

    public static boolean hasCurio(LivingEntity livingEntity, Item curio) {
        return CuriosApi.getCuriosInventory(livingEntity)
                .map(handler -> !handler.findCurios(curio).isEmpty())
                .orElse(false);
    }

    public static boolean isTheCursedOne(Player player) {
        return ModUtils.hasCurio(player, ModItems.CURSED_RING.get());
    }

    public static boolean isTheWorthyOne(Player player) {
        if (isTheCursedOne(player)) {
            return ModUtils.getSufferingFraction(player) >= Config.SUPER_CURSED_TIME.getAsDouble();
        } else
            return false;
    }

    public static double getSufferingFraction(Player player) {
        StatsCounter stats = null;
        if (player instanceof ServerPlayer serverPlayer) {
            stats = serverPlayer.getStats();
        } else if (player.level().isClientSide) {
            stats = getClientStats();
        }

        if (stats == null) return 0.0;
        int withTicks = stats.getValue(Stats.CUSTOM.get(ModStats.TIME_WITH_CURSED_RING.get()));
        int withoutTicks = stats.getValue(Stats.CUSTOM.get(ModStats.TIME_WITHOUT_CURSED_RING.get()));

        long total = (long) withTicks + (long) withoutTicks;
        if (total == 0L) return 0.0;

        return (double) withTicks / (double) total;
    }

    @OnlyIn(Dist.CLIENT)
    private static StatsCounter getClientStats() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        return player != null ? player.getStats() : null;
    }

    public static String getSufferingTime(@Nullable Player player) {
        String text = "";

        double ringPercent = 100 * getSufferingFraction(player);

        ringPercent = roundToPlaces(ringPercent, 1);
        if (ringPercent - Math.floor(ringPercent) == 0) {
            text += ((int) ringPercent) + "%";
        } else {
            text += ringPercent + "%";
        }

        return text;
    }

    public static double roundToPlaces(double value, int places) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    // TODO надо проверять работу на сервере
    @OnlyIn(Dist.CLIENT)
    public static void indicateCursedOnesOnly(List<Component> list) {
        ChatFormatting format;

        if (Minecraft.getInstance().player != null) {
            format = ModUtils.isTheCursedOne(Minecraft.getInstance().player) ? ChatFormatting.GOLD : ChatFormatting.DARK_RED;
        } else {
            format = ChatFormatting.DARK_RED;
        }

        list.add(Component.translatable("tooltip.enigmaticlegacy.cursedOnesOnly1").withStyle(format));
        list.add(Component.translatable("tooltip.enigmaticlegacy.cursedOnesOnly2").withStyle(format));
    }

    @OnlyIn(Dist.CLIENT)
    public static void indicateWorthyOnesOnly(List<Component> list) {
        ChatFormatting format = ChatFormatting.DARK_RED;
        Player player = Minecraft.getInstance().player;

        ModUtils.requestIfStale();

        if (player != null) {
            format = ModUtils.isTheWorthyOne(Minecraft.getInstance().player) ? ChatFormatting.GOLD : ChatFormatting.DARK_RED;
        }

        double requiredCurse = ModUtils.roundToPlaces(100 * Config.SUPER_CURSED_TIME.getAsDouble(), 1);

        list.add(Component.translatable("tooltip.enigmaticlegacy.worthyOnesOnly1"));
        list.add(Component.translatable("tooltip.enigmaticlegacy.worthyOnesOnly2",
                        Component.literal(requiredCurse + "%").withStyle(ChatFormatting.GOLD))
                .withStyle(format));
        list.add(Component.translatable("tooltip.enigmaticlegacy.worthyOnesOnly3"));
        list.add(Component.translatable("tooltip.enigmaticlegacy.void"));
        list.add(Component.translatable("tooltip.enigmaticlegacy.worthyOnesOnly4")
                .withStyle(format).append(Component.literal(" " + ModUtils.getSufferingTime(player))
                        .withStyle(ChatFormatting.LIGHT_PURPLE)));
    }

    @OnlyIn(Dist.CLIENT)
    public static void requestIfStale() {
        long now = System.currentTimeMillis();
        if (now - lastRequestMs < COOLDOWN_MS) return;

        Minecraft mc = Minecraft.getInstance();
        ClientPacketListener conn = mc.getConnection();
        if (conn == null) return;

        conn.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
        lastRequestMs = now;
    }

    public static boolean tryForceEquip(LivingEntity entity, ItemStack curio) {
        if (!(curio.getItem() instanceof ICurioItem))
            throw new IllegalArgumentException("I fear for now this only works with ICurioItem");

        MutableBoolean equipped = new MutableBoolean(false);
        ICurioItem item = (ICurioItem) curio.getItem();

        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {
            if (!entity.level().isClientSide) {
                Map<String, ICurioStacksHandler> curios = handler.getCurios();

                cycle: for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                    IDynamicStackHandler stackHandler = entry.getValue().getStacks();

                    for (int i = 0; i < stackHandler.getSlots(); i++) {
                        ItemStack present = stackHandler.getStackInSlot(i);
                        Set<String> tags = CuriosApi.getCuriosHelper().getCurioTags(curio.getItem());
                        String id = entry.getKey();

                        SlotContext context = new SlotContext(id, entity, i, false, entry.getValue().isVisible());

                        if (present.isEmpty() && (tags.contains(id) || tags.contains("curio")) && item.canEquip(context, curio)) {
                            stackHandler.setStackInSlot(i, curio);
                            equipped.setTrue();
                            break cycle;
                        }
                    }
                }

            }
        });

        return equipped.booleanValue();
    }

    public static void setPersistentInteger(Player player, String tag, int value) {
        ModUtils.setPersistentTag(player, tag, IntTag.valueOf(value));
    }

    public static void setPersistentTag(Player player, String tag, Tag value) {
        CompoundTag data = player.getPersistentData();
        CompoundTag persistent;

        if (!data.contains(Player.PERSISTED_NBT_TAG)) {
            data.put(Player.PERSISTED_NBT_TAG, (persistent = new CompoundTag()));
        } else {
            persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
        }

        persistent.put(tag, value);
    }

    public static int getPersistentInteger(Player player, String tag, int expectedValue) {
        Tag theTag = ModUtils.getPersistentTag(player, tag, IntTag.valueOf(expectedValue));
        return theTag instanceof IntTag ? ((IntTag) theTag).getAsInt() : expectedValue;
    }

    public static Tag getPersistentTag(Player player, String tag, Tag expectedValue) {
        CompoundTag data = player.getPersistentData();
        CompoundTag persistent;

        if (!data.contains(Player.PERSISTED_NBT_TAG)) {
            data.put(Player.PERSISTED_NBT_TAG, (persistent = new CompoundTag()));
        } else {
            persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
        }

        if (persistent.contains(tag))
            return persistent.get(tag);
        else
            //persistent.put(tag, expectedValue);
            return expectedValue;
    }

    public static boolean isAbsolute(DamageSource source) {
        return source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    public static boolean canDropSoulCrystal(Player player, boolean hadRing) {
        if (isAffectedBySoulLoss(player, hadRing)) {
            // TODO OmniconfigHandler.maxSoulCrystalLoss.getValue()
            int maxCrystalLoss = 9;
            return SoulCrystal.getLostCrystals(player) < maxCrystalLoss;
        } else
            return false;
    }

    public static boolean isAffectedBySoulLoss(Player player, boolean hadRing) {
        // TODO OmniconfigHandler.soulCrystalsMode.getValue()
        int dropMode = 0;
        boolean keepInventory = player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);

        if (dropMode == 0)
            return hadRing;
        else if (dropMode == 1)
            return (hadRing || keepInventory);
        else if (dropMode == 2)
            return true;
        else
            return false;
    }
}
