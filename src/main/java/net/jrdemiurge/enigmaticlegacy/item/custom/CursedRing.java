package net.jrdemiurge.enigmaticlegacy.item.custom;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.jrdemiurge.enigmaticlegacy.Config;
import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.mixin.EnderManAccessor;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

public class CursedRing extends Item implements ICurioItem {
    public CursedRing(Properties properties) {
        super(properties);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attributeMap = ArrayListMultimap.create();

        attributeMap.put(Attributes.ARMOR, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(EnigmaticLegacy.MOD_ID, "cursed_ring.armor"), (double) -Config.ARMOR_DEBUFF.get() / 100, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributeMap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(EnigmaticLegacy.MOD_ID, "cursed_ring.armor_toughness"), (double) -Config.ARMOR_DEBUFF.get() / 100, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributeMap.put(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(EnigmaticLegacy.MOD_ID, "cursed_ring.block_interaction_range"), Config.BLOCK_INTERACTION_RANGE_BONUS.getAsDouble(), AttributeModifier.Operation.ADD_VALUE));

        return attributeMap;
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, Item.TooltipContext context, ItemStack stack) {
        tooltips.clear();
        return tooltips;
    }

    @Override
    public boolean canUnequip(SlotContext context, ItemStack stack) {
        return context.entity() instanceof Player player && player.isCreative();
    }

    @Override
    public boolean canEquipFromUse(SlotContext context, ItemStack stack) {
        return false;
    }

    // TODO Сделать проклятый мир
    /*@Override
    public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
        SuperpositionHandler.setCurrentWorldCursed(false);
    }*/

    // TODO Сделать проклятый мир
    /*@Override
    public void onEquip(SlotContext context, ItemStack prevStack, ItemStack stack) {
        if (context.entity() instanceof ServerPlayer player) {
            CursedRingEquippedTrigger.INSTANCE.trigger(player);
        }

        SuperpositionHandler.setCurrentWorldCursed(true);
    }*/

    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, boolean recentlyHit, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }

    // TODO ХЗ что это
/*    public boolean isItemDeathPersistent(ItemStack stack) {
        return stack.getItem().equals(this) || stack.getItem().equals(EnigmaticItems.ENIGMATIC_AMULET);
    }*/

    @Override
    public void curioTick(SlotContext context, ItemStack stack) {
        if (context.entity().level().isClientSide || !(context.entity() instanceof Player))
            return;

        Player player = (Player) context.entity();

        if (player.isCreative() || player.isSpectator())
            return;

        RandomSource random = player.getRandom();

        List<LivingEntity> genericMobs = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(Config.NEUTRAL_ANGER_RANGE.getAsDouble()));
        List<EnderMan> endermen = player.level().getEntitiesOfClass(EnderMan.class, player.getBoundingBox().inflate(Config.ENDERMAN_RANDOMPORT_RANGE.getAsDouble()));

        for (EnderMan enderman : endermen) {
            if (random.nextDouble() <= (0.002 * Config.ENDERMAN_RANDOMPORT_FREQUENCY.getAsDouble())) {
                if (((EnderManAccessor) enderman).callTeleportTowards(player) && player.hasLineOfSight(enderman)) {
                    enderman.setTarget(player);
                }
            }
        }

        // TODO потом ещзё против пиглинов но сначала их поведение надо прочекать
        for (LivingEntity checkedEntity : genericMobs) {
            double visibility = player.getVisibilityPercent(checkedEntity);
            double angerDistance = Math.max(Config.NEUTRAL_ANGER_RANGE.getAsDouble() * visibility, Config.NEUTRAL_X_RAY_RANGE.getAsDouble());

            if (checkedEntity.distanceToSqr(player.getX(), player.getY(), player.getZ()) > angerDistance * angerDistance) {
                continue;
            }

            if (checkedEntity instanceof NeutralMob neutral) {

                ResourceLocation mobId = BuiltInRegistries.ENTITY_TYPE.getKey(checkedEntity.getType());
                List<? extends String> whitelist = Config.NEUTRAL_ANGER_WHITELIST.get();
                if (!whitelist.contains(mobId.toString())) {
                    continue;
                }

                if (neutral instanceof TamableAnimal tamable && tamable.isTame()) {
                    continue;
                }
                if (neutral instanceof IronGolem golem && golem.isPlayerCreated()) {
                    continue;
                }

                if (neutral.getTarget() == null || !neutral.getTarget().isAlive()) {
                    if (player.hasLineOfSight(checkedEntity) || player.distanceTo(checkedEntity) <= Config.NEUTRAL_X_RAY_RANGE.getAsDouble()) {
                        neutral.setTarget(player);
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public double getAngerRange() {
        return Config.NEUTRAL_ANGER_RANGE.getAsDouble();
    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack curio) {
        return defaultInstance.getFortuneLevel(slotContext, lootContext) + Config.FORTUNE_BONUS.getAsInt();
    }

    @Override
    public int getLootingLevel(SlotContext slotContext, @Nullable LootContext lootContext, ItemStack stack) {
        return defaultInstance.getLootingLevel(slotContext, lootContext) + Config.LOOTING_BONUS.getAsInt();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) {
            ModUtils.indicateWorthyOnesOnly(tooltipComponents);
        } else {
            ModUtils.indicateCursedOnesOnly(tooltipComponents);
        }

    }

}
