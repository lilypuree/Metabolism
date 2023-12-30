package lilypuree.metabolism.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;

import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.data.Metabolite;
import lilypuree.metabolism.data.Metabolites;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.event.TooltipOverlayEvent;
import squeek.appleskin.api.food.FoodValues;

public class ToolTipOverlayHandler {
    public static final ToolTipOverlayHandler INSTANCE = new ToolTipOverlayHandler();

    public static void register(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(MetaboliteToolTip.class, MetaboliteToolTipRenderer::new);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation(MetabolismMod.MOD_ID, "textures/gui/hud.png");

    public static class MetaboliteToolTipRenderer implements ClientTooltipComponent {
        MetaboliteToolTip metaboliteToolTip;

        MetaboliteToolTipRenderer(MetaboliteToolTip toolTip) {
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

            guiGraphics.blit(TEXTURE, offsetX, y, 18, 9, 9, 9);
            renderString(guiGraphics, font, offsetX + 9, y, 0.75f, metaboliteToolTip.foodText);

            offsetX += 18 + font.width(metaboliteToolTip.foodText);

            guiGraphics.blit(TEXTURE, offsetX, y, 27, 9, 9, 9);
            renderString(guiGraphics, font, offsetX + 9, y, 0.75f, metaboliteToolTip.hydrationText);

            if (metaboliteToolTip.warmthText != null) {
                offsetX = x;
                y += 10;

                guiGraphics.blit(TEXTURE, offsetX, y, 18, 0, 9, 9);
                guiGraphics.blit(TEXTURE, offsetX, y, 0, 0, 9, 9);
                renderString(guiGraphics, font, offsetX + 9, y, 0.75f, metaboliteToolTip.warmthText);
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

        MetaboliteToolTip(ItemStack itemStack, Metabolite metabolite) {
            this.itemStack = itemStack;
            foodText = String.format("x%.1f", metabolite.food());
            hydrationText = String.format("x%.1f", metabolite.hydration());
            if (metabolite.warmth() > 0)
                warmthText = String.format("x%.1f over %.1fs", metabolite.getEffectiveWarmth(), metabolite.getEffectTicks() / 20.0F);
        }
    }




    @SubscribeEvent
    public void gatherTooltips(RenderTooltipEvent.GatherComponents event) {
        if (event.isCanceled())
            return;

        ItemStack hoveredStack = event.getItemStack();
        Minecraft mc = Minecraft.getInstance();
        if (!shouldShowTooltip(hoveredStack, mc.player))
            return;

        Metabolite metabolite = Metabolites.getMetabolite(hoveredStack, mc.player);
        if (metabolite == Metabolite.NONE)
            return;

        MetaboliteToolTip toolTip = new MetaboliteToolTip(hoveredStack, metabolite);
        event.getTooltipElements().add(Either.right(toolTip));
    }

    private static boolean shouldShowTooltip(ItemStack hoveredStack, Player player) {
        if (hoveredStack.isEmpty())
            return false;

        boolean shouldShowTooltip = (Config.CLIENT.showToolTip() && isShiftKeyDown()) || Config.CLIENT.alwaysShowToolTip();
        if (!shouldShowTooltip)
            return false;

        return hoveredStack.getFoodProperties(player) != null;
    }

    private static boolean isShiftKeyDown() {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }


}
