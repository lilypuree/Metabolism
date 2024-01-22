package lilypuree.metabolism.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lilypuree.metabolism.CommonCallbacks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    
    @WrapOperation(method = "completeUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack onCompleteUsingItem(ItemStack useItem, Level level, LivingEntity entity, Operation<ItemStack> originalOperation) {
        if (entity instanceof Player player) {
            ItemStack copy = useItem.copy();
            ItemStack result = CommonCallbacks.onPlayerItemUse(player, copy);
            if (result != null) {
                originalOperation.call(useItem, level, entity);
                return result;
            }
        }
        return originalOperation.call(useItem, level, entity);
    }
}
