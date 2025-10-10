package net.jrdemiurge.enigmaticlegacy.event;

import net.jrdemiurge.enigmaticlegacy.Config;
import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.entities.custom.PermanentItemEntity;
import net.jrdemiurge.enigmaticlegacy.item.ModItems;
import net.jrdemiurge.enigmaticlegacy.item.custom.StorageCrystal;
import net.jrdemiurge.enigmaticlegacy.util.DimensionalPosition;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.jrdemiurge.enigmaticlegacy.util.SoulArchive;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.jrdemiurge.enigmaticlegacy.item.custom.SoulCrystal;

@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID)
public class LivingDropsHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            DimensionalPosition dimPoint = new DimensionalPosition(player.getX(), player.getY(), player.getZ(), player.level());

            if (!event.getDrops().isEmpty() && !Config.DISABLE_VESSEL.isTrue()) {
                ItemStack soulCrystal = ModUtils.canDropSoulCrystal(player, ModUtils.isTheCursedOne(player)) ? ((SoulCrystal) ModItems.SOUL_CRYSTAL.get()).createCrystalFrom(player) : null;
                ItemStack storageCrystal = ((StorageCrystal) ModItems.STORAGE_CRYSTAL.get()).storeDropsOnCrystal(event.getDrops(), player, soulCrystal);
                PermanentItemEntity droppedStorageCrystal = new PermanentItemEntity(dimPoint.world, dimPoint.getPosX(), dimPoint.getPosY() + 1.5, dimPoint.getPosZ(), storageCrystal);
                droppedStorageCrystal.setOwnerId(player.getUUID());
                dimPoint.world.addFreshEntity(droppedStorageCrystal);
                EnigmaticLegacy.LOGGER.info("Summoned Extradimensional Storage Crystal for " + player.getGameProfile().getName() + " at X: " + dimPoint.getPosX() + ", Y: " + dimPoint.getPosY() + ", Z: " + dimPoint.getPosZ());
                event.getDrops().clear();

                SoulArchive.getInstance().addItem(droppedStorageCrystal);
            } else if (ModUtils.canDropSoulCrystal(player, ModUtils.isTheCursedOne(player))) {
                ItemStack soulCrystal = ((SoulCrystal) ModItems.SOUL_CRYSTAL.get()).createCrystalFrom(player);
                PermanentItemEntity droppedSoulCrystal = new PermanentItemEntity(dimPoint.world, dimPoint.getPosX(), dimPoint.getPosY() + 1.5, dimPoint.getPosZ(), soulCrystal);
                droppedSoulCrystal.setOwnerId(player.getUUID());
                dimPoint.world.addFreshEntity(droppedSoulCrystal);
                EnigmaticLegacy.LOGGER.info("Teared Soul Crystal from " + player.getGameProfile().getName() + " at X: " + dimPoint.getPosX() + ", Y: " + dimPoint.getPosY() + ", Z: " + dimPoint.getPosZ());

                SoulArchive.getInstance().addItem(droppedSoulCrystal);
            }
        }
    }
}
