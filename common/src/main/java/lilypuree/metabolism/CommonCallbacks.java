package lilypuree.metabolism;

import lilypuree.metabolism.core.Metabolism;
import lilypuree.metabolism.core.metabolite.Metabolites;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class CommonCallbacks {

    //returns the resultant itemstack upon use, null if nothing happened.
    public static ItemStack onPlayerItemUse(Player player, ItemStack item) {
        if (item.is(Items.POTION)) {
            if (PotionUtils.getPotion(item) == Potions.WATER) {
                Metabolism.get(player).eat(player, Metabolites.getMetabolites().get(Items.POTION));
            }
        }

        if (item.getItem() instanceof BowlFoodItem || item.getItem() instanceof SuspiciousStewItem) {
            item.shrink(1);
            if (item.isEmpty()) {
                return new ItemStack(Items.BOWL);
            } else {
                if (!player.getAbilities().instabuild) {
                    ItemStack itemstack = new ItemStack(Items.BOWL);
                    if (!player.getInventory().add(itemstack)) {
                        player.drop(itemstack, false);
                    }
                }
                return item;
            }
        }
        return null;
    }
}
