package net.jrdemiurge.enigmaticlegacy;

import net.jrdemiurge.enigmaticlegacy.entities.ModEntites;
import net.jrdemiurge.enigmaticlegacy.item.ModCreativeModeTabs;
import net.jrdemiurge.enigmaticlegacy.item.ModItems;
import net.jrdemiurge.enigmaticlegacy.stat.ModStats;
import net.jrdemiurge.enigmaticlegacy.util.SoulArchive;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(EnigmaticLegacy.MOD_ID)
public class EnigmaticLegacy {
    public static final String MOD_ID = "enigmaticlegacy";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EnigmaticLegacy(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModStats.register(modEventBus);
        ModEntites.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModStats::onCommonSetup);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        SoulArchive.initialize(event.getServer());
    }
}
