package lilypuree.metabolism.mixin;

import lilypuree.metabolism.core.FoodDataDuck;
import lilypuree.metabolism.core.metabolite.Metabolite;
import lilypuree.metabolism.core.metabolite.Metabolites;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class ForgeFoodDataMixin implements FoodDataDuck {
    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true, remap = false)
    public void onEat(Item item, ItemStack stack, LivingEntity entity, CallbackInfo ci) {
        if (item.isEdible()) {
            Metabolite metabolite = Metabolites.getMetabolite(stack, entity);
            if (metabolite != Metabolite.NONE) {
                getMetabolism().eat(entity, metabolite);
                ci.cancel();
            }
        }
    }
}
