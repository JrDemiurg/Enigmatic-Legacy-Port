package net.jrdemiurge.enigmaticlegacy.stat;

import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModStats {
    public static final DeferredRegister<ResourceLocation> CUSTOM_STATS =
            DeferredRegister.create(BuiltInRegistries.CUSTOM_STAT, EnigmaticLegacy.MOD_ID);

    public static final Supplier<ResourceLocation> TIME_WITH_CURSED_RING =
            CUSTOM_STATS.register("time_with_cursed_ring",
                    () -> ResourceLocation.fromNamespaceAndPath(EnigmaticLegacy.MOD_ID, "time_with_cursed_ring"));

    public static final Supplier<ResourceLocation> TIME_WITHOUT_CURSED_RING =
            CUSTOM_STATS.register("time_without_cursed_ring",
                    () -> ResourceLocation.fromNamespaceAndPath(EnigmaticLegacy.MOD_ID, "time_without_cursed_ring"));

    public static void onCommonSetup () {
        Stats.CUSTOM.get(ModStats.TIME_WITH_CURSED_RING.get(),    StatFormatter.TIME);
        Stats.CUSTOM.get(ModStats.TIME_WITHOUT_CURSED_RING.get(), StatFormatter.TIME);
    }

    public static void register (IEventBus eventBus) {
        CUSTOM_STATS.register(eventBus);
    }
}
