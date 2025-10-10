package net.jrdemiurge.enigmaticlegacy.event;

import net.jrdemiurge.enigmaticlegacy.EnigmaticLegacy;
import net.jrdemiurge.enigmaticlegacy.gui.EnderChestInventoryButton;
import net.jrdemiurge.enigmaticlegacy.network.EnderRingKeyPayload;
import net.jrdemiurge.enigmaticlegacy.util.QuarkHelper;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.util.Tuple;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import net.minecraft.resources.ResourceLocation;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = EnigmaticLegacy.MOD_ID)
public class ScreenEventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onScreenInit(net.neoforged.neoforge.client.event.ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();

        if (screen instanceof InventoryScreen
                || screen instanceof CreativeModeInventoryScreen
                || screen instanceof CuriosScreen) {

            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
            boolean isCreative = screen instanceof CreativeModeInventoryScreen;

            // === Ender Chest Button ===
            EnderChestInventoryButton enderButton = new EnderChestInventoryButton(
                    gui, 0, 0, 20, 18,
                    0, 0, 19,
                    ResourceLocation.fromNamespaceAndPath(EnigmaticLegacy.MOD_ID, "textures/gui/ender_chest_button.png"),
                    b -> PacketDistributor.sendToServer(new EnderRingKeyPayload(true))
            );

            Tuple<Integer, Integer> enderOffsets = enderButton.getOffsets(isCreative);
            enderButton.setPosition(gui.getGuiLeft() + enderOffsets.getA(),
                    gui.getGuiTop() + enderOffsets.getB());

            // Добавляем виджет (оба способа рабочие, обычно хватает первого)
            event.addListener(enderButton);
            // либо: gui.addRenderableWidget(enderButton);

            // === Совместимость с Quark mini-кнопками ===
            if (QuarkHelper.getMiniButtonClass() != null) {
                event.getListenersList().forEach(listener -> {
                    if (QuarkHelper.getMiniButtonClass().isInstance(listener)) {
                        Button btn = (Button) listener;
                        btn.setY(btn.getY() - 22);
                    }
                });
            }
        }
    }
}
