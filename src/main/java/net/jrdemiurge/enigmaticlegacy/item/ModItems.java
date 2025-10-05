package net.jrdemiurge.enigmaticlegacy.item;

import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.item.custom.CursedRing;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EnigmaticLegacy.MOD_ID);

    public static final DeferredItem<Item> CURSED_RING = ITEMS.register("cursed_ring",
            () -> new CursedRing(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant()));

    public static void register (IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
