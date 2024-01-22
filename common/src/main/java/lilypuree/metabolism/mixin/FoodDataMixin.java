package lilypuree.metabolism.mixin;

import lilypuree.metabolism.core.FoodDataDuck;
import lilypuree.metabolism.core.Metabolism;
import lilypuree.metabolism.core.metabolite.Metabolite;
import lilypuree.metabolism.core.metabolite.Metabolites;
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

import static lilypuree.metabolism.core.MetabolismConstants.EXHAUSTION_MULTIPLIER;

@Mixin(FoodData.class)
public abstract class FoodDataMixin implements FoodDataDuck {

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

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void onTick(Player player, CallbackInfo ci) {
        this.lastFoodLevel = foodLevel;
        if (player.getAbilities().invulnerable) {
            this.foodLevel = 20;
        } else {
            metabolism.tick(player);
            this.foodLevel = metabolism.getHydration() > 0 ? 10 : 1; //disables sprinting when hydration is zero
        }
        ci.cancel();
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    public void onRead(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("result", CompoundTag.TAG_COMPOUND)) {
            metabolism.readNBT(tag.getCompound("result"));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    public void onWrite(CompoundTag tag, CallbackInfo ci) {
        tag.put("result", metabolism.writeNBT());
    }

    @Inject(method = "addExhaustion", at = @At("HEAD"))
    public void addExhaustion(float exhaustion, CallbackInfo ci) {
        //handles all other exhaustion gains
        metabolism.addProgress(exhaustion * EXHAUSTION_MULTIPLIER);
    }

    @Inject(method = "needsFood", at = @At("HEAD"), cancellable = true)
    public void onNeedsFood(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(metabolism.needsFood());
    }

    @Override
    public Metabolism getMetabolism() {
        return metabolism;
    }
}
