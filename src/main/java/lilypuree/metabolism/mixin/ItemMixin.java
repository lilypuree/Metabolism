package lilypuree.metabolism.mixin;

import lilypuree.metabolism.data.Metabolite;
import lilypuree.metabolism.data.Metabolites;
import lilypuree.metabolism.metabolism.FoodDataDuck;
import lilypuree.metabolism.metabolism.Metabolism;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startUsingItem(Lnet/minecraft/world/InteractionHand;)V"), cancellable = true)
    public void onUse(Level pLevel, Player pPlayer, InteractionHand pUsedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        Metabolism metabolism = ((FoodDataDuck) pPlayer.getFoodData()).getMetabolism();
        Metabolite metabolite = Metabolites.getMetabolite(itemStack, pPlayer);
      
        if (!metabolism.canEat(metabolite))
            cir.setReturnValue(InteractionResultHolder.fail(itemStack));
    }
}
