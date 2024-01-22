package lilypuree.metabolism.client.gui.tooltip;

import com.mojang.blaze3d.platform.InputConstants;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.core.metabolite.Metabolite;
import lilypuree.metabolism.core.metabolite.Metabolites;
import lilypuree.metabolism.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ToolTipOverlayHandler {


    public static MetaboliteToolTip getToolTip(ItemStack hoveredStack) {
        Minecraft mc = Minecraft.getInstance();
        if (shouldShowTooltip(hoveredStack, mc.player)) {
            Metabolite metabolite = Metabolites.getMetabolite(hoveredStack, mc.player);
            if (metabolite != Metabolite.NONE)
                return new MetaboliteToolTip(hoveredStack, metabolite);
        }
        return null;
    }
    

    static boolean shouldShowTooltip(ItemStack hoveredStack, Player player) {
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
