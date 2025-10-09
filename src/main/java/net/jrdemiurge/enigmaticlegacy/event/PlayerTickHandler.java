package net.jrdemiurge.enigmaticlegacy.event;

import net.jrdemiurge.enigmaticlegacy.Config;
import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.mixin.PlayerAccessor;
import net.jrdemiurge.enigmaticlegacy.stat.ModStats;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID)
public class PlayerTickHandler {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        tickAndAwardStatsEverySecond(player);
        handleInsomniaIfCursed(player);
        extendFireIfCursed(player);
    }

    private static void tickAndAwardStatsEverySecond(Player player) {
        if (player.tickCount % 20 != 0) return;

        ResourceLocation stat = ModUtils.isTheCursedOne(player)
                ? ModStats.TIME_WITH_CURSED_RING.get()
                : ModStats.TIME_WITHOUT_CURSED_RING.get();

        player.awardStat(Stats.CUSTOM.get(stat), 20);
    }

    private static void handleInsomniaIfCursed(Player player) {
        if (Config.DISABLE_INSOMNIA.isTrue()) return;
        if (!player.isSleeping()) return;
        if (!ModUtils.isTheCursedOne(player)) return;

        int sleepTimer = player.getSleepTimer();
        if (sleepTimer == 5) {
            if (player instanceof ServerPlayer) {
                player.sendSystemMessage(
                        Component.translatable("message.enigmaticlegacy.cursed_sleep")
                                .withStyle(ChatFormatting.RED)
                );
            }
        } else if (sleepTimer > 90) {
            ((PlayerAccessor) player).setSleepCounter(90);
        }
    }

    private static void extendFireIfCursed(Player player) {
        if (!player.isOnFire()) return;
        if (!ModUtils.isTheCursedOne(player)) return;

        player.setRemainingFireTicks(player.getRemainingFireTicks() + 2);
    }

}
