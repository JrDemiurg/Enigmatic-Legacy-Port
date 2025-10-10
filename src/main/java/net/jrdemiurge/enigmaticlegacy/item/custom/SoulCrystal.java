package net.jrdemiurge.enigmaticlegacy.item.custom;

import java.util.List;

import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class SoulCrystal extends Item implements IPermanentCrystal {

	public SoulCrystal(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		if (Screen.hasShiftDown()) {
			ModUtils.addLocalizedString(tooltipComponents, "tooltip.enigmaticlegacy.soulCrystal1");
			ModUtils.addLocalizedString(tooltipComponents, "tooltip.enigmaticlegacy.soulCrystal2");
		} else {
			ModUtils.addLocalizedString(tooltipComponents, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	public ItemStack createCrystalFrom(Player player) {
		int lostFragments = getLostCrystals(player);
		setLostCrystals(player, lostFragments + 1);

		return new ItemStack(this);
	}

	public static boolean retrieveSoulFromCrystal(Player player, ItemStack stack) {
		int lostFragments = getLostCrystals(player);

		if (lostFragments > 0) {
			setLostCrystals(player, lostFragments - 1);

			if (!player.level().isClientSide) {
				player.level().playSound(null, BlockPos.containing(player.position()), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.0f);
			}

			return true;
		} else
			return false;
	}

	public static void setLostCrystals(Player player, int lost) {
		// SuperpositionHandler.setCurrentWorldFractured(lost >= 10);
		ModUtils.setPersistentInteger(player, "enigmaticlegacy.lostsoulfragments", lost);
		updatePlayerSoulMap(player);
	}

	public static int getLostCrystals(Player player) {
		return ModUtils.getPersistentInteger(player, "enigmaticlegacy.lostsoulfragments", 0);
	}

	public static void updatePlayerSoulMap(Player player) {
		if (player.level().isClientSide) return;

		AttributeInstance inst = player.getAttribute(Attributes.MAX_HEALTH);
		if (inst == null) return;

		int lostFragments = ModUtils.getPersistentInteger(player, "enigmaticlegacy.lostsoulfragments", 0);
		AttributeModifier attrOld = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(EnigmaticLegacy.MOD_ID, "soul_crystal.max_health"), -0.1F * (lostFragments + 1), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

		inst.removeModifier(attrOld);

		if (lostFragments > 0) {
			AttributeModifier attr = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(EnigmaticLegacy.MOD_ID, "soul_crystal.max_health"), -0.1F * lostFragments, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

			inst.addTransientModifier(attr);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);

		if (retrieveSoulFromCrystal(player, stack)) {
			// TODO сделать это
			/*Vector3 playerCenter = Vector3.fromEntityCenter(player);
			if (!player.level().isClientSide) {
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(playerCenter.x, playerCenter.y, playerCenter.z, 64, player.level().dimension())), new PacketRecallParticles(playerCenter.x, playerCenter.y, playerCenter.z, 48, false));
			}*/

			stack.consume(1, player);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
		} else
			return new InteractionResultHolder<>(InteractionResult.PASS, stack);
	}

}