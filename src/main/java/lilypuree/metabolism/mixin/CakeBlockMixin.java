package lilypuree.metabolism.mixin;

import lilypuree.metabolism.metabolism.FoodDataDuck;
import lilypuree.metabolism.metabolism.Metabolism;
import lilypuree.metabolism.metabolite.Metabolite;
import lilypuree.metabolism.metabolite.Metabolites;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeBlockMixin {
    
    @Redirect(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"))
    private static void onEat(FoodData data, int foodLevelModifier, float saturationLevelModifier, LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        Metabolite metabolite = Metabolites.getMetabolites().get(Items.CAKE);
        ((FoodDataDuck) data).getMetabolism().eat(player, metabolite);
    }
}
