package lilypuree.metabolism.environment;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.MetabolismTags;
import lilypuree.metabolism.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public class AdvancedLocationCheck implements LootItemCondition {

    final Type type;

    public AdvancedLocationCheck(Type type) {
        this.type = type;
    }

    @Override
    public LootItemConditionType getType() {
        return Registration.ADVANCED_LOCATION_CHECK.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ORIGIN);
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin == null) return false;
        ServerLevel level = context.getLevel();
        BlockPos pos = BlockPos.containing(origin.x, origin.y, origin.z);
        if (hasNoBlocksAbove(level, pos)) {
            Holder<Biome> biome = level.getBiome(pos);
            Biome.Precipitation precipitation = biome.value().getPrecipitationAt(pos);

            switch (type) {
                case EXPOSED -> {
                    return true;
                }
                case RAINY -> {
                    return level.isRaining() && precipitation == Biome.Precipitation.RAIN;
                }
                case SNOWY -> {
                    return level.isRaining() && precipitation == Biome.Precipitation.SNOW;
                }
                case HOT_BIOME -> {
                    return level.getBiome(pos).is(MetabolismTags.HOT_BIOMES);
                }
            }
        }
        return false;
    }

    public boolean hasNoBlocksAbove(ServerLevel level, BlockPos pPos) {
        return level.canSeeSky(pPos) && level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pPos).getY() <= pPos.getY();
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<AdvancedLocationCheck> {
        public void serialize(JsonObject json, AdvancedLocationCheck value, JsonSerializationContext context) {
            json.addProperty("type", value.type.name());
        }

        /**
         * Deserialize a value by reading it from the JsonObject.
         */
        public AdvancedLocationCheck deserialize(JsonObject json, JsonDeserializationContext context) {
            String type = json.has("type") ? GsonHelper.getAsString(json, "type") : null;
            return new AdvancedLocationCheck(Type.fromString(type));
        }
    }

    public enum Type {
        EXPOSED("exposed"), RAINY("rainy"), SNOWY("snowy"), HOT_BIOME("hot_biome");
    
        private final String name;
    
        Type(String name) {
            this.name = name;
        }
    
        @Override
        public String toString() {
            return name;
        }
    
        public static Type fromString(String name) {
            if (name != null) {
                Optional<Type> locationType = Arrays.stream(values()).filter(type -> type.name.equals(name)).findAny();
                if (locationType.isEmpty()) {
                    MetabolismMod.LOGGER.warn("invalid location type " + name);
                    return null;
                } else
                    return locationType.get();
            }
            return null;
        }
    }
}
