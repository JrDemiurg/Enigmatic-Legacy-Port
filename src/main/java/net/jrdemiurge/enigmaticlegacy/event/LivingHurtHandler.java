package net.jrdemiurge.enigmaticlegacy.event;

import net.jrdemiurge.enigmaticlegacy.Config;
import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID)
public class LivingHurtHandler {
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getNewDamage() >= Float.MAX_VALUE)
            return;

        if (event.getEntity() instanceof Player player) {
            if (ModUtils.isTheCursedOne(player)) {
                event.setNewDamage(event.getNewDamage() * (Config.PAIN_MODIFIER.getAsInt() / 100F));
            }
        }

        if (event.getEntity() instanceof Monster || event.getEntity() instanceof EnderDragon) {
            if (event.getSource().getEntity() instanceof Player player) {
                if (ModUtils.isTheCursedOne(player)) {
                    event.setNewDamage(event.getNewDamage() * (Config.MONSTER_DAMAGE_DEBUFF.getAsInt() / 100F));
                }
            }
        }
    }
}
