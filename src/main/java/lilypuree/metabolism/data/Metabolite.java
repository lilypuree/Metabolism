package lilypuree.metabolism.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Metabolite(float warmth, float energy, float food) {
    public static final Codec<Metabolite> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.FLOAT.fieldOf("warmth").forGetter(Metabolite::warmth),
                    Codec.FLOAT.fieldOf("energy").forGetter(Metabolite::energy),
                    Codec.FLOAT.fieldOf("food").forGetter(Metabolite::food)
            ).apply(inst, Metabolite::new));

    public static final Metabolite DEFAULT = new Metabolite(0.0F, 0.0F, 0.0F);
}
