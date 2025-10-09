package net.jrdemiurge.enigmaticlegacy.event;

import net.jrdemiurge.enigmaticlegacy.Config;
import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID)
public class LivingExperienceDropHandler {
    @SubscribeEvent
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();

        int bonusExp = 0;

        if (player != null && ModUtils.isTheCursedOne(player)) {
            bonusExp += event.getOriginalExperience() * (Config.EXPERIENCE_BONUS.getAsInt() / 100);
        }

        event.setDroppedExperience(event.getDroppedExperience() + bonusExp);
    }
}
