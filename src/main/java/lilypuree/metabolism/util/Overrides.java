package lilypuree.metabolism.util;

import lilypuree.metabolism.MetabolismMod;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class Overrides {

    public static void changeStackSize(Item item, int stackSize) {
        if (stackSize > 0) {
            ObfuscationReflectionHelper.setPrivateValue(Item.class, item, stackSize, "f_41370_");
        }
    }

    public static void changeFastEating(Item item, boolean fastEating) {
        FoodProperties properties = item.getFoodProperties();
        if (properties != null)
            ObfuscationReflectionHelper.setPrivateValue(FoodProperties.class, properties, fastEating, "f_38727_");
    }

    public static void changeEatWhenFull(Item item, boolean eatWhenFull) {
        FoodProperties properties = item.getFoodProperties();
        if (properties != null)
            ObfuscationReflectionHelper.setPrivateValue(FoodProperties.class, properties, eatWhenFull, "f_38726_");
    }
}
