package net.jrdemiurge.enigmaticlegacy.gui;

import net.jrdemiurge.enigmaticlegacy.Config;
import net.jrdemiurge.enigmaticlegacy.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnderChestInventoryButton extends PlayerInventoryButton {
    public EnderChestInventoryButton(AbstractContainerScreen<?> gui,
                                     int x, int y, int w, int h,
                                     int u, int v, int yDiffTex,
                                     ResourceLocation texture,
                                     OnPress onPress) {
        super(gui, x, y, w, h, u, v, yDiffTex, texture, onPress);
    }

    @Override
    protected boolean beforeRender(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        boolean hasRing = ModUtils.isTheCursedOne(Minecraft.getInstance().player);

        if (!hasRing || !Config.INVENTORY_BUTTON_ENABLED.isTrue()) {
            this.active = false;
            return false;
        }
        return true;
    }

    @Override
    public Tuple<Integer, Integer> getOffsets(boolean creative) {
        int x = creative
                ? 170 + Config.BUTTON_OFFSET_X_CREATIVE.getAsInt()
                : 150 + Config.BUTTON_OFFSET_X.getAsInt();
        int y = creative
                ? 5 + Config.BUTTON_OFFSET_Y_CREATIVE.getAsInt()
                : 61 + Config.BUTTON_OFFSET_Y.getAsInt();
        return new Tuple<>(x, y);
    }
}

