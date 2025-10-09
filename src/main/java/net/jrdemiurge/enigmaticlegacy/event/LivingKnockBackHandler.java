package net.jrdemiurge.enigmaticlegacy.event;

import net.jrdemiurge.enigmaticlegacy.Config;
import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID)
public class LivingKnockBackHandler {
    @SubscribeEvent
    public static void onLivingKnockback(LivingKnockBackEvent event) {
        if (event.getEntity() instanceof Player player && ModUtils.isTheCursedOne(player)) {
            event.setStrength(event.getStrength() * (float) Config.KNOCKBACK_DEBUFF.getAsInt() / 100F);
        }
    }
}
