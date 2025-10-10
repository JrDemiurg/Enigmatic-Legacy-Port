package net.jrdemiurge.enigmaticlegacy.event;

import net.jrdemiurge.enigmaticlegacy.Config;
import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID)
public class LivingExperienceDropHandler {
    @SubscribeEvent
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();

        int bonusExp = 0;

        if (event.getEntity() instanceof ServerPlayer) {
            if (!Config.DISABLE_VESSEL.isTrue() && !event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                event.setCanceled(true);
            }
        }

        if (player != null && ModUtils.isTheCursedOne(player)) {
            bonusExp += event.getOriginalExperience() * (Config.EXPERIENCE_BONUS.getAsInt() / 100);
        }

        event.setDroppedExperience(event.getDroppedExperience() + bonusExp);
    }
}
