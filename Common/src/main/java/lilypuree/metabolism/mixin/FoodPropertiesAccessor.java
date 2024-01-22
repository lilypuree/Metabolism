package lilypuree.metabolism.mixin;

import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FoodProperties.class)
public interface FoodPropertiesAccessor {
    
    @Mutable
    @Accessor(value = "fastFood")
    void setFastFood(boolean fastFood);

    @Mutable
    @Accessor(value = "canAlwaysEat")
    void setCanAlwaysEat(boolean canAlwaysEat);
}
