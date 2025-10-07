package net.jrdemiurge.enigmaticlegacy.event;

import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.stat.ModStats;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.minecraft.resources.ResourceLocation;
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

        if (player.tickCount % 20 != 0) return;

        ResourceLocation stat = ModUtils.isTheCursedOne(player)
                ? ModStats.TIME_WITH_CURSED_RING.get()
                : ModStats.TIME_WITHOUT_CURSED_RING.get();

        player.awardStat(Stats.CUSTOM.get(stat), 20);
    }
}
