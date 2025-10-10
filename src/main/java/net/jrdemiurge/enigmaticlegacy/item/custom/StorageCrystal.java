package net.jrdemiurge.enigmaticlegacy.item.custom;

import java.util.Collection;

import javax.annotation.Nullable;

import net.jrdemiurge.enigmaticlegacy.util.ExperienceHelper;
import net.jrdemiurge.enigmaticlegacy.util.ItemNBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StorageCrystal extends Item implements IPermanentCrystal {

	public StorageCrystal(Properties properties) {
		super(properties);
	}

	public ItemStack storeDropsOnCrystal(Collection<ItemEntity> drops, Player player, @Nullable ItemStack embeddedSoulCrystal) {
		ItemStack crystal = new ItemStack(this);
		CompoundTag crystalNBT = ItemNBTHelper.getNBT(crystal);
		int counter = 0;

		HolderLookup.Provider registries = player.level().registryAccess();

		for (ItemEntity drop : drops) {
			ItemStack dropStack = drop.getItem();

			CompoundTag nbt = (CompoundTag) dropStack.save(registries, new CompoundTag());
			crystalNBT.put("storedStack" + counter, nbt);

			counter++;
		}

		if (embeddedSoulCrystal != null) {
			CompoundTag embeddedTag = (CompoundTag) embeddedSoulCrystal.save(registries, new CompoundTag());
			crystalNBT.put("embeddedSoul", embeddedTag);
		}

		ItemNBTHelper.setInt(crystal, "storedStacks", counter);

		int exp = ExperienceHelper.getPlayerXP(player);
		ExperienceHelper.drainPlayerXP(player, exp);

		ItemNBTHelper.setInt(crystal, "storedXP", exp);
		ItemNBTHelper.setBoolean(crystal, "isStored", true);

		return crystal;
	}

	public static ItemStack retrieveDropsFromCrystal(ItemStack crystal, Player player, ItemStack retrieveSoul) {
		CompoundTag crystalNBT = ItemNBTHelper.getNBT(crystal);
		int counter = crystalNBT.getInt("storedStacks")-1;
		int exp = crystalNBT.getInt("storedXP");

		HolderLookup.Provider registries = player.level().registryAccess();

		for (int c = counter; c >= 0; c--) {
			CompoundTag tag = crystalNBT.getCompound("storedStack" + c);

			// вместо ItemStack.of(tag):
			ItemStack stack = ItemStack.parseOptional(registries, tag);

			if (!stack.isEmpty()) {
				if (!player.getInventory().add(stack)) {
					ItemEntity drop = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), stack);
					player.level().addFreshEntity(drop);
				}
			}
			crystalNBT.remove("storedStack" + c);
		}

		ExperienceHelper.addPlayerXP(player, exp);

		if (retrieveSoul != null) {
			SoulCrystal.retrieveSoulFromCrystal(player, retrieveSoul);
		} else {
			player.level().playSound(null, BlockPos.containing(player.position()), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.0f);
		}

		ItemNBTHelper.setBoolean(crystal, "isStored", false);
		ItemNBTHelper.setInt(crystal, "storedStacks", 0);
		ItemNBTHelper.setInt(crystal, "storedXP", 0);

		return crystal;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		playerIn.startUsingItem(handIn);

		if (!worldIn.isClientSide) {}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
	}

}