package lilypuree.metabolism.core.metabolite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lilypuree.metabolism.core.MetabolismConstants;
import lilypuree.metabolism.mixin.FoodPropertiesAccessor;
import lilypuree.metabolism.mixin.ItemAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public record Metabolite(float food, float hydration, float warmth, int amplifier, Modifier modifier) {
    public static final Metabolite NONE = new Metabolite(0, 0, 0, 0, null);
    public static final Codec<Metabolite> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    Codec.FLOAT.optionalFieldOf("food", 0.0F).forGetter(Metabolite::food),
                    Codec.FLOAT.optionalFieldOf("hydration", 0.0F).forGetter(Metabolite::hydration),
                    Codec.FLOAT.optionalFieldOf("warmth", 0.0F).forGetter(Metabolite::warmth),
                    Codec.INT.optionalFieldOf("amplifier", 0).forGetter(Metabolite::amplifier),
                    Modifier.CODEC.optionalFieldOf("modifier", Modifier.NONE).forGetter(Metabolite::modifier)
            ).apply(inst, Metabolite::new));


    public static Metabolite createVanilla(float nutrition, float saturationMod) {
        float effectiveQuality = nutrition + nutrition * saturationMod * 2.0F;
        int size = Mth.floor(effectiveQuality * MetabolismConstants.OTHER_FOOD_MULTIPLIER);
        return new Metabolite(size, size, size, 0, null);
    }

    public int getEffectTicks() {
        return Mth.ceil(warmth / MetabolismConstants.metabolismSpeed(amplifier));
    }

    public static record Modifier(int stackSize, boolean eatWhenFull, boolean fastEating) {
        public static final Modifier NONE = new Modifier(-1, false, false);
        public static final Codec<Modifier> CODEC = RecordCodecBuilder.create(inst ->
                inst.group(
                        Codec.INT.optionalFieldOf("stack_size", -1).forGetter(Modifier::stackSize),
                        Codec.BOOL.optionalFieldOf("eat_when_full", false).forGetter(Modifier::eatWhenFull),
                        Codec.BOOL.optionalFieldOf("fast_eating", false).forGetter(Modifier::fastEating)
                ).apply(inst, Modifier::new));

        public void apply(Item item) {
            if (stackSize > 0)
                ((ItemAccessor) item).setMaxStackSize(stackSize);
            FoodProperties foodProperties = item.getFoodProperties();
            if (foodProperties != null){
                ((FoodPropertiesAccessor)foodProperties).setCanAlwaysEat(eatWhenFull);
                ((FoodPropertiesAccessor)foodProperties).setFastFood(fastEating);
            }
        }

        public static Modifier fromItem(Item item) {
            int stackSize = item.getMaxStackSize();
            FoodProperties properties = item.getFoodProperties();
            boolean alwaysEat = properties != null && properties.canAlwaysEat();
            boolean fastFood = properties != null && properties.isFastFood();
            return new Modifier(stackSize, alwaysEat, fastFood);
        }
    }
}
