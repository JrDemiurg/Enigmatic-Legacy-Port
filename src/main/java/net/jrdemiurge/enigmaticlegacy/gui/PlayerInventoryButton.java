package net.jrdemiurge.enigmaticlegacy.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.theillusivec4.curios.client.gui.CuriosScreen;

@OnlyIn(Dist.CLIENT)
public abstract class PlayerInventoryButton extends AbstractWidget {
    protected final AbstractContainerScreen<?> parentGui;

    protected final ResourceLocation texture;
    protected final int u, v, texW, texH, yDiffTex;

    protected boolean isRecipeBookVisible = false;

    public interface OnPress { void onPress(PlayerInventoryButton b); }
    private final OnPress onPress;

    public PlayerInventoryButton(AbstractContainerScreen<?> gui,
                                 int x, int y, int w, int h,
                                 int u, int v, int yDiffTex,
                                 ResourceLocation texture,
                                 OnPress onPress) {
        super(x, y, w, h, CommonComponents.EMPTY);
        this.parentGui = gui;
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.yDiffTex = yDiffTex;
        this.texW = 256;
        this.texH = 256;
        this.onPress = onPress;
        this.active = true;
        this.visible = true;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.active && this.visible && this.isMouseOver(mouseX, mouseY)) {
            this.onPress.onPress(this);
        }
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.active = true;

        if (this.parentGui instanceof InventoryScreen || this.parentGui instanceof CuriosScreen) {
            boolean lastVisible = this.isRecipeBookVisible;

            if (this.parentGui instanceof InventoryScreen inv) {
                this.isRecipeBookVisible = inv.getRecipeBookComponent().isVisible();
            } else if (this.parentGui instanceof CuriosScreen curios) {
                this.isRecipeBookVisible = curios.getRecipeBookComponent().isVisible();
            }

            if (lastVisible != this.isRecipeBookVisible) {
                Tuple<Integer, Integer> offsets = this.getOffsets(false);
                this.setPosition(this.parentGui.getGuiLeft() + offsets.getA(),
                        this.parentGui.getGuiTop() + offsets.getB());
            }
        } else if (this.parentGui instanceof CreativeModeInventoryScreen creative) {
            if (!creative.isInventoryOpen()) {
                this.active = false;
                return;
            }
        }

        if (!this.beforeRender(g, mouseX, mouseY, partialTick)) {
            return;
        }

        int v0 = this.v + (this.isHoveredOrFocused() ? this.yDiffTex : 0);
        g.blit(this.texture, this.getX(), this.getY(), this.u, v0, this.getWidth(), this.getHeight(), this.texW, this.texH);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput out) {

    }

    protected abstract boolean beforeRender(GuiGraphics g, int mouseX, int mouseY, float partialTick);
    public abstract Tuple<Integer, Integer> getOffsets(boolean creative);
}

