package lilypuree.metabolism.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.metabolism.MetabolismResult;
import lilypuree.metabolism.metabolite.Metabolite;
import lilypuree.metabolism.metabolite.Metabolites;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class ToolTipOverlayHandler {
    public static final ToolTipOverlayHandler INSTANCE = new ToolTipOverlayHandler();

    public static void register(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(MetaboliteToolTip.class, MetaboliteToolTipRenderer::new);
    }
    
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

        MetaboliteToolTip(ItemStack itemStack, Metabolite metabolite) {
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
        
        if (hoveredStack.is(Items.POTION) && PotionUtils.getPotion(hoveredStack) == Potions.WATER) {
            return true;
        } else
            return hoveredStack.is(Items.CAKE) || hoveredStack.getFoodProperties(player) != null;
    }

    private static boolean isShiftKeyDown() {
        long handle = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }


}
