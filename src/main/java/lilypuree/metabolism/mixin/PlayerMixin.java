package lilypuree.metabolism.mixin;

import lilypuree.metabolism.metabolism.FoodDataDuck;
import lilypuree.metabolism.metabolism.Metabolism;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static lilypuree.metabolism.metabolism.MetabolismConstants.*;

@Mixin(value = Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow
    public abstract boolean isSwimming();

    @Shadow
    @Final
    private Abilities abilities;

    @Shadow
    public abstract FoodData getFoodData();

    @Shadow public abstract void die(DamageSource pCause);

    @Unique
    private boolean exhaustionHandled = false;

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Unique
    private void addProgress(float amount) {
        if (!this.abilities.invulnerable) {
            if (!this.level().isClientSide) {
                getMetabolism().addProgress(amount);
            }
        }
    }

    @Unique
    private void consumeFood(float amount) {
        if (!this.abilities.invulnerable) {
            if (!this.level().isClientSide) {
                getMetabolism().consumeFood(amount);
            }
        }
    }

    @Unique
    private void consumeEnergy(float amount) {
        if (!this.abilities.invulnerable) {
            if (!this.level().isClientSide) {
                getMetabolism().consumeHydration(amount);
            }
        }
    }

    @Unique
    private Metabolism getMetabolism() {
        return ((FoodDataDuck) getFoodData()).getMetabolism();
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;needsFood()Z"))
    public void onAiStep(CallbackInfo ci) {
        if (this.tickCount % 10 == 0)
            getMetabolism().warmIgnoreHeat(1.0F);
    }

    @Inject(method = "jumpFromGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
    public void onJump(CallbackInfo ci) {
        if (this.isSprinting()) {
            consumeEnergy(ENERGY_SPRINT_JUMP);
        } else {
            addProgress(PROGRESS_JUMP);
        }
        exhaustionHandled = true;
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
    public void onActuallyHurt(DamageSource dmgSrc, float dmgAmount, CallbackInfo ci) {
        if (dmgSrc.getFoodExhaustion() > 0) {
            addProgress(PROGRESS_DMG);
        }
        exhaustionHandled = true;
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
    public void onAttack(CallbackInfo ci) {
        addProgress(PROGRESS_ATK); //0.1F
        exhaustionHandled = true;
    }

    @Inject(method = "checkMovementStatistics", at = @At(value = "HEAD"))
    public void onMovement(double x, double y, double z, CallbackInfo ci) {
        if (this.isPassenger()) return;

        if (this.isSwimming()) {
            int dist = Math.round((float) Math.sqrt(x * x + y * y + z * z) * 100.0F);
            if (dist > 0) {
                addProgress(dist * 0.01F * PROGRESS_SWIM);
                exhaustionHandled = true;
            }
        } else if (this.isEyeInFluid(FluidTags.WATER)) {
            int dist = Math.round((float) Math.sqrt(x * x + y * y + z * z) * 100.0F);
            if (dist > 0) {
                addProgress(dist * 0.01F * PROGRESS_WALK);
                exhaustionHandled = true;
            }
        } else if (this.isInWater()) {
            float dist = Math.round((float) Math.sqrt(x * x + z * z) * 100.0F);
            if (dist > 0) {
                addProgress(dist * 0.01F * PROGRESS_WALK);
                exhaustionHandled = true;
            }
        } else if (this.onClimbable()) {
            if (y > 0) {
                addProgress((float) y * PROGRESS_CLIMB);
            }
        } else if (this.onGround()) {
            int dist = Math.round((float) Math.sqrt(x * x + z * z) * 100.0F);
            if (dist > 0) {
                if (this.isSprinting()) {
                    addProgress(dist  * 0.01F * PROGRESS_SPRINT);
                } else if (this.isCrouching()) {
                    addProgress(dist * 0.01F * PROGRESS_CROUCH);
                } else {
                    addProgress(dist * 0.01F * PROGRESS_WALK);
                }
                exhaustionHandled = true;
            }
        } else if (this.isFallFlying()) {
            float dist = Math.round(Math.sqrt(x * x + y * y + z * z));
            consumeEnergy(dist * ENERGY_FOOD_FLY);
            consumeFood(dist * ENERGY_FOOD_FLY);
        }
    }

    @Inject(method = "causeFoodExhaustion", at = @At(value = "HEAD"), cancellable = true)
    public void onCauseFoodExhaustion(CallbackInfo ci) {
        if (exhaustionHandled) {
            ci.cancel();
            exhaustionHandled = false;
        }
    }

    @Inject(method = "tryToStartFallFlying", at = @At("HEAD"), cancellable = true)
    public void onTryToStartFallFlying(CallbackInfoReturnable<Boolean> cir) {
        if (getMetabolism().getHydration() <= 0)
            cir.setReturnValue(false);
    }
}
