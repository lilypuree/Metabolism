package lilypuree.metabolism.mixin;

import lilypuree.metabolism.metabolism.Metabolism;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEffect.class)
public class MobEffectMixin {

    @Inject(method = "applyEffectTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"), cancellable = true)
    public void onApplyEffectTick(LivingEntity entity, int amplifier, CallbackInfo ci) {
        Metabolism metabolism = Metabolism.get((Player) entity);
        metabolism.consumeFood(1.0F);
        metabolism.consumeHydration(1.0F);
        ci.cancel();
    }
}
