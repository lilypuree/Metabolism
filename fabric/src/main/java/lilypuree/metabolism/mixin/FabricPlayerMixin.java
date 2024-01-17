package lilypuree.metabolism.mixin;

import lilypuree.metabolism.core.FoodDataDuck;
import lilypuree.metabolism.core.metabolite.Metabolite;
import lilypuree.metabolism.core.metabolite.Metabolites;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class FabricPlayerMixin extends LivingEntity {

    protected FabricPlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V"))
    public void eat(FoodData data, Item foodItem, ItemStack food) {
        if (foodItem.isEdible()) {
            Metabolite metabolite = Metabolites.getMetabolite(food, this);
            if (metabolite != Metabolite.NONE) {
                ((FoodDataDuck) data).getMetabolism().eat(this, metabolite);
            }
        }
    }
}
