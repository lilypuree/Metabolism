package lilypuree.metabolism.mixin;

import lilypuree.metabolism.data.Metabolite;
import lilypuree.metabolism.data.Metabolites;
import lilypuree.metabolism.metabolism.FoodDataDuck;
import lilypuree.metabolism.metabolism.Metabolism;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static lilypuree.metabolism.metabolism.MetabolismConstants.EXHAUSTION_MULTIPLIER;

@Mixin(FoodData.class)
public class FoodDataMixin implements FoodDataDuck {

    @Shadow
    private int foodLevel;

    @Shadow
    private int lastFoodLevel;

    @Unique
    private final Metabolism metabolism = new Metabolism();

    @Inject(method = "eat(IF)V", at = @At("HEAD"), cancellable = true)
    public void eat(int foodLevelModifier, float saturationLevelModifier, CallbackInfo ci) {
        metabolism.eat(null, Metabolite.createVanilla(foodLevelModifier, saturationLevelModifier));
        ci.cancel();
    }

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true, remap = false)
    public void onEat(Item item, ItemStack stack, LivingEntity entity, CallbackInfo ci) {
        if (item.isEdible()) {
            Metabolite metabolite = Metabolites.getMetabolite(stack, entity);
            if (metabolite != Metabolite.NONE) {
                metabolism.eat(entity, metabolite);
                ci.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void onTick(Player player, CallbackInfo ci) {
        metabolism.tick(player);
        this.lastFoodLevel = foodLevel;
        this.foodLevel = metabolism.getHydration() > 0 ? 10 : 1;   //enables vanilla sprinting when hydration is positive
        ci.cancel();
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    public void onRead(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("progress", CompoundTag.TAG_COMPOUND)) {
            metabolism.readNBT(tag.getCompound("progress"));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    public void onWrite(CompoundTag tag, CallbackInfo ci) {
        tag.put("progress", metabolism.writeNBT());
    }

    @Inject(method = "addExhaustion", at = @At("HEAD"))
    public void addExhaustion(float exhaustion, CallbackInfo ci) {
        //handles all other exhaustion gains
        metabolism.addProgress(exhaustion * EXHAUSTION_MULTIPLIER);
    }

    @Inject(method = "setFoodLevel", at = @At("HEAD"), cancellable = true)
    public void onSetFood(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "needsFood", at = @At("HEAD"), cancellable = true)
    public void onNeedsFood(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Override
    public Metabolism getMetabolism() {
        return metabolism;
    }
}
