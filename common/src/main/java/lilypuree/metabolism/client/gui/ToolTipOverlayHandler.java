package lilypuree.metabolism.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.core.MetabolismResult;
import lilypuree.metabolism.core.metabolite.Metabolite;
import lilypuree.metabolism.core.metabolite.Metabolites;
import lilypuree.metabolism.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.lwjgl.glfw.GLFW;

public class ToolTipOverlayHandler {
    public static final ToolTipOverlayHandler INSTANCE = new ToolTipOverlayHandler();

    public static class MetaboliteToolTipRenderer implements ClientTooltipComponent {
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
            if (!shouldShowTooltip(metaboliteToolTip.itemStack, mc.player))
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

    public static class MetaboliteToolTip implements TooltipComponent {
        ItemStack itemStack;
        private final String foodText;
        private final String hydrationText;
        private String warmthText = null;

        public MetaboliteToolTip(ItemStack itemStack, Metabolite metabolite) {
            this.itemStack = itemStack;
            float food = metabolite.food();
            float hydration = metabolite.hydration();
            if (itemStack.is(Items.CAKE)) {
                food *= 7;
                hydration *= 7;
            }
            foodText = String.format("x%.1f", food);
            hydrationText = String.format("x%.1f", hydration);

            if (metabolite.warmth() > 0)
                warmthText = String.format("x%.1f over %.1fs", metabolite.warmth(), metabolite.getEffectTicks() / 20.0F);
            else if (metabolite.warmth() < 0)
                warmthText = String.format("x%.1f", metabolite.warmth());

        }
    }

    public static ToolTipOverlayHandler.MetaboliteToolTip getToolTip(ItemStack hoveredStack) {
        Minecraft mc = Minecraft.getInstance();
        if (shouldShowTooltip(hoveredStack, mc.player)) {
            Metabolite metabolite = Metabolites.getMetabolite(hoveredStack, mc.player);
            if (metabolite != Metabolite.NONE)
                return new MetaboliteToolTip(hoveredStack, metabolite);
        }
        return null;
    }

    private static boolean shouldShowTooltip(ItemStack hoveredStack, Player player) {
        if (hoveredStack.isEmpty())
            return false;

        boolean shouldShowTooltip = (Config.CLIENT.showToolTip() && isShiftKeyDown()) || Config.CLIENT.alwaysShowToolTip();
        if (!shouldShowTooltip)
            return false;

        if (hoveredStack.is(Items.POTION) && PotionUtils.getPotion(hoveredStack) == Potions.WATER) {
            return true;
        } else
            return hoveredStack.is(Items.CAKE) || Services.PLATFORM.getFoodProperties(hoveredStack, player) != null;
    }

    private static boolean isShiftKeyDown() {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }
}
