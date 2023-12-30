package lilypuree.metabolism.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lilypuree.metabolism.metabolism.MetabolismConstants;
import net.minecraft.util.Mth;
import net.minecraft.world.food.FoodConstants;

public record Metabolite(float food, float hydration, float warmth, int amplifier) {
    public static final Metabolite NONE = new Metabolite(0, 0, 0, 0);
    public static final Codec<Metabolite> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.FLOAT.fieldOf("food").forGetter(Metabolite::food),
                    Codec.FLOAT.fieldOf("hydration").forGetter(Metabolite::hydration),
                    Codec.FLOAT.optionalFieldOf("warmth", 0.0F).forGetter(Metabolite::warmth),
                    Codec.INT.optionalFieldOf("amplifier", 0).forGetter(Metabolite::amplifier)
            ).apply(inst, Metabolite::new));


    public static Metabolite createVanilla(float nutrition, float saturationMod) {
        float healthIncrease = nutrition * saturationMod * 8.0F / FoodConstants.EXHAUSTION_HEAL;
        return new Metabolite(nutrition / 2, nutrition / 2, healthIncrease, 1);
    }

    public int getEffectTicks() {
        return Mth.floor(warmth * MetabolismConstants.METABOLISM_CYCLES * MetabolismConstants.BASE_TICK_COUNT);
    }

    public float getEffectiveWarmth() {
        return warmth * (amplifier + 1);
    }
}
