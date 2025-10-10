package net.jrdemiurge.enigmaticlegacy.event;

import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.item.custom.SoulCrystal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID)
public class PlayerCloneHandler {
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player newPlayer = event.getEntity();

        SoulCrystal.updatePlayerSoulMap(newPlayer);
    }
}
