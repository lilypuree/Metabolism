package lilypuree.metabolism.core;

import lilypuree.metabolism.registration.Registration;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

public class MetabolismEffect extends MobEffect {
    public MetabolismEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xff5600);
    }

    public static class Instance extends MobEffectInstance {
        public Instance(int pDuration, int pAmplifier) {
            super(Registration.METABOLISM_EFFECT.get(), pDuration, pAmplifier, false, false, true);
        }

        private Instance copy() {
            Instance instance = new Instance(duration, getAmplifier());
            instance.hiddenEffect = hiddenEffect;
            return instance;
        }

        @Override
        public boolean update(MobEffectInstance other) {
            MobEffectInstance hiddenEffectToAdd;
            if (other.getAmplifier() == this.getAmplifier()) {
                hiddenEffectToAdd = other.hiddenEffect;
            } else if (this.getAmplifier() > other.getAmplifier()) {
                hiddenEffectToAdd = other;
            } else { //this.amplifier < other.amplifier
                hiddenEffectToAdd = this.copy();
                this.amplifier = other.getAmplifier();
                this.hiddenEffect = other.hiddenEffect;
            }
            this.duration += other.duration;
            if (this.hiddenEffect == null) {
                this.hiddenEffect = hiddenEffectToAdd;
            } else if (hiddenEffectToAdd != null) {
                this.hiddenEffect.update(hiddenEffectToAdd);
            }
            return true;
        }

        @Override
        public int tickDownDuration() {
            this.duration = this.mapDuration(i -> i - 1);
            if (this.hiddenEffect != null && this.duration <= this.hiddenEffect.duration)
                this.duration = 0;
            return this.duration;
        }
    }
}
