package net.jrdemiurge.enigmaticlegacy.gui;

import com.mojang.blaze3d.vertex.PoseStack;

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

        // TODO сюда добавить конфиг на отключение кнопки
        if (!hasRing) {
            this.active = false;
            return false;
        }
        return true;
    }

    // TODO сюда добавить конфиг на положение кнопки
    @Override
    public Tuple<Integer, Integer> getOffsets(boolean creative) {
        int x = creative
                ? 170 + 0
                : 150 + 0;
        int y = creative
                ? 5 + 0
                : 61 + 0;
        return new Tuple<>(x, y);
    }
}

