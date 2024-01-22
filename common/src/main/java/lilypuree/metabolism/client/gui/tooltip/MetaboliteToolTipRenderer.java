package lilypuree.metabolism.client.gui.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import lilypuree.metabolism.client.gui.MetabolismDisplayHandler;
import lilypuree.metabolism.core.MetabolismResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class MetaboliteToolTipRenderer implements ClientTooltipComponent {
    MetaboliteToolTip metaboliteToolTip;

    public MetaboliteToolTipRenderer(MetaboliteToolTip toolTip) {
        this.metaboliteToolTip = toolTip;
    }

    @Override
    public int getHeight() {
        // hunger + spacing + warmth + arbitrary spacing,
        // for some reason 3 extra looks best
        if (metaboliteToolTip.warmthText == null) return 9 + 3;
        else return 9 + 1 + 9 + 3;
    }

    @Override
    public int getWidth(Font font) {
        int width = 27 + font.width(metaboliteToolTip.foodText) + font.width(metaboliteToolTip.hydrationText);
        if (metaboliteToolTip.warmthText == null) {
            return width + 2;
        } else {
            int warmthWidth = 9 + font.width(metaboliteToolTip.warmthText);
            return Math.max(warmthWidth, width) + 2;
        }
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        if (!ToolTipOverlayHandler.shouldShowTooltip(metaboliteToolTip.itemStack, mc.player))
            return;

        Screen gui = mc.screen;
        if (gui == null)
            return;

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int offsetX = x;

        MetabolismDisplayHandler.renderIcon(guiGraphics, offsetX, y, MetabolismResult.FOOD);
        renderString(guiGraphics, font, offsetX + 9, y + 1, 0.75f, metaboliteToolTip.foodText);

        offsetX += 18 + font.width(metaboliteToolTip.foodText);

        MetabolismDisplayHandler.renderIcon(guiGraphics, offsetX, y, MetabolismResult.HYDRATION);
        renderString(guiGraphics, font, offsetX + 9, y + 1, 0.75f, metaboliteToolTip.hydrationText);

        if (metaboliteToolTip.warmthText != null) {
            offsetX = x;
            y += 10;

            MetabolismDisplayHandler.renderIcon(guiGraphics, offsetX, y, MetabolismResult.NONE);
            MetabolismDisplayHandler.renderIcon(guiGraphics, offsetX, y, MetabolismResult.WARMING);
            renderString(guiGraphics, font, offsetX + 9, y + 1, 0.75f, metaboliteToolTip.warmthText);
        }

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        // reset to drawHoveringText state
        RenderSystem.disableDepthTest();
    }

    private void renderString(GuiGraphics graphics, Font font, int x, int y, float textScale, String string) {
        if (textScale > 0) {
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            poseStack.translate(x, y, 0);
            poseStack.scale(textScale, textScale, textScale);
            graphics.drawString(font, string, 2, 1, 0xFFAAAAAA);
            poseStack.popPose();
        }
    }
}
