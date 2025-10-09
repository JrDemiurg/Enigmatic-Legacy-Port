package net.jrdemiurge.enigmaticlegacy.event;

import net.jrdemiurge.enigmaticlegacy.Config;
import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.enchanting.EnchantmentLevelSetEvent;

@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID)
public class EnchantmentLevelSetHandler {
    @SubscribeEvent
    public static void onEnchantmentLevelSet(EnchantmentLevelSetEvent event) {
        BlockPos pos = event.getPos();
        Vec3 c = Vec3.atCenterOf(pos);
        int radius = 16;

        AABB box = new AABB(c, c).inflate(radius);

        boolean shouldBoost = false;

        for (Player player : event.getLevel().getEntitiesOfClass(Player.class, box))
            if (ModUtils.isTheCursedOne(player)) {
                shouldBoost = true;
            }

        if (shouldBoost) {
            event.setEnchantLevel(event.getEnchantLevel() + Config.ENCHANTING_BONUS.get());
        }
    }
}
