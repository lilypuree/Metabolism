package lilypuree.metabolism.data;

import com.google.gson.*;
import lilypuree.metabolism.metabolism.Metabolism;
import lilypuree.metabolism.mixin.LocationCheckAccessor;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.Position;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class EnvironmentEffect {
    private final ResourceLocation name;
    protected final LootItemCondition condition;
    protected final float warmthEffect;
    protected final float heatTarget;
    protected final float nightMultiplier;
    protected final float range;
    protected final boolean isAdditive;
    protected final boolean isResistance;


    public EnvironmentEffect(ResourceLocation name, LootItemCondition condition, float warmthEffect, float heatTarget, float nightMultiplier, float range, boolean isAdditive, boolean isResistance) {
        this.name = name;
        this.condition = condition;
        this.warmthEffect = warmthEffect;
        this.heatTarget = heatTarget;
        this.nightMultiplier = nightMultiplier;
        this.range = range;
        this.isAdditive = isAdditive;
        this.isResistance = isResistance;
    }

    public float getWarmthEffect(boolean isNight) {
        return isNight ? warmthEffect * nightMultiplier : warmthEffect;
    }

    public float getHeatTarget(boolean isNight) {
        return isNight ? heatTarget * nightMultiplier : heatTarget;
    }

    public boolean isRanged() {
        return range > 0;
    }

    public boolean canApply(LootContext context) {
        return this.condition.test(context);
    }

    public boolean canApplyRanged(ServerLevel level, Position pos, float distToPlayer) {
        if (condition instanceof LocationCheck location && range >= distToPlayer) {
            LocationPredicate predicate = ((LocationCheckAccessor) location).getPredicate();
            return predicate.matches(level, pos.x(), pos.y(), pos.z());
        } else return false;
    }


    public static class Combined {
        private boolean isNight;
        private float additiveWarmthEffect = 0;
        private float warmthEffect = 0;
        private float additiveHeatTarget = 0;
        private float heatTarget = 0;
        private float coldTarget = 0;
        private float heatResistance = 0;
        private float coldResistance = 0;
        private float additiveHeatResistance = 0;
        private float additiveColdResistance = 0;

        public Combined(boolean isNight) {
            this.isNight = isNight;
        }

        public void addEffect(EnvironmentEffect effect) {
            float effectHeatTarget = effect.getHeatTarget(isNight);


            if (effect.isAdditive) {
                additiveWarmthEffect += effect.getWarmthEffect(isNight);

                if (effect.isResistance) {
                    if (effectHeatTarget > 0)
                        additiveColdResistance += effectHeatTarget;
                    else if (effectHeatTarget < 0)
                        additiveHeatResistance += effectHeatTarget;
                } else
                    additiveHeatTarget += effectHeatTarget;
            } else {
                warmthEffect = Math.max(warmthEffect, effect.getWarmthEffect(isNight));

                if (effect.isResistance) {
                    if (effectHeatTarget > 0)
                        coldResistance = Math.max(coldResistance, effectHeatTarget);
                    else if (effectHeatTarget < 0)
                        heatResistance = Math.min(heatResistance, effectHeatTarget);
                } else {
                    if (effectHeatTarget > 0)
                        heatTarget = Math.max(heatTarget, effectHeatTarget);
                    else if (effectHeatTarget < 0)
                        coldTarget = Math.min(coldTarget, effectHeatTarget);
                }
            }
        }

        public float getCombinedHeatTarget() {
            float combinedHeatTarget = heatTarget + coldTarget + additiveHeatTarget;
            if (combinedHeatTarget == 0)
                return 0;
            else if (combinedHeatTarget > 0) { //HOT
                return Math.max(combinedHeatTarget + additiveHeatResistance, 0);
            } else { //COLD
                return Math.min(combinedHeatTarget + additiveColdResistance, 0);
            }
        }

        public float getCombinedWarmthEffect() {
            return warmthEffect + additiveWarmthEffect;
        }
    }

    public static EnvironmentEffect deserialize(ResourceLocation location, JsonElement json, ResourceManager resourceManager) throws JsonParseException {
        JsonObject jsonObject = GsonHelper.convertToJsonObject(json, "environment effect");
        float warmthEffect = GsonHelper.getAsFloat(jsonObject, "warmth_effect", 0.0F);
        float heatTarget = GsonHelper.getAsFloat(jsonObject, "heat_target", 0.0F);
        float nightMultiplier = GsonHelper.getAsFloat(jsonObject, "night_multiplier", 1.0F);
        float range = GsonHelper.getAsFloat(jsonObject, "range", 0.0F);
        boolean isAdditive = GsonHelper.getAsBoolean(jsonObject, "is_additive", false);
        boolean isResistance = GsonHelper.getAsBoolean(jsonObject, "is_resistance", false);
        LootItemCondition condition;
        if (!jsonObject.has("conditions")) {
            throw new JsonSyntaxException("No conditions defined for environment effect");
        } else {
            JsonElement element = GsonHelper.getNonNull(jsonObject, "conditions");
            condition = LootDataType.PREDICATE.deserialize(location, element, resourceManager)
                    .orElseThrow(() -> new JsonParseException("Environment effect condition malformed"));
            if (range > 0 && !(condition instanceof LocationCheck))
                throw new JsonSyntaxException("Ranged Environment effect needs a location predicate");
        }
        return new EnvironmentEffect(location, condition, warmthEffect, heatTarget, nightMultiplier, range, isAdditive, isResistance);
    }
}
