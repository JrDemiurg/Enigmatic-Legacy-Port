package net.jrdemiurge.enigmaticlegacy.event;

import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.item.custom.SoulCrystal;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID)
public class EntityJoinLevelHandler {
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof ServerPlayer joinedPlayer) {
            SoulCrystal.updatePlayerSoulMap(joinedPlayer);
        }
    }
}
