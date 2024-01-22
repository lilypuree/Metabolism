package lilypuree.metabolism.client.gui.tooltip;

import lilypuree.metabolism.core.metabolite.Metabolite;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MetaboliteToolTip implements TooltipComponent {
    ItemStack itemStack;
    final String foodText;
    final String hydrationText;
    String warmthText = null;

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
