package net.jrdemiurge.enigmaticlegacy;

import net.jrdemiurge.enigmaticlegacy.client.renderers.PermanentItemRenderer;
import net.jrdemiurge.enigmaticlegacy.entities.ModEntites;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = EnigmaticLegacy.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID, value = Dist.CLIENT)
public class EnigmaticLegacyClient {
    public EnigmaticLegacyClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntites.PERMANENT_ITEM_ENTITY.get(), renderManager -> new PermanentItemRenderer(renderManager, Minecraft.getInstance().getItemRenderer()));
    }
}
