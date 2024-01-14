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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
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
    final TagKey<Biome> biomeTag;

    public AdvancedLocationCheck(Type type, TagKey<Biome> biomeTag) {
        this.type = type;
        this.biomeTag = biomeTag;
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

        boolean matchesBiomeTag = (biomeTag == null) || level.getBiome(pos).is(biomeTag);
        
        return matchesBiomeTag && matchesType(level, pos);
    }

    public boolean matchesType(ServerLevel level, BlockPos pos) {
        if (type == Type.NONE)
            return true;
        else if (hasNoBlocksAbove(level, pos)) {
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
            if (value.biomeTag != null)
                json.addProperty("biome_tag", value.biomeTag.toString());
        }

        /**
         * Deserialize a value by reading it from the JsonObject.
         */
        public AdvancedLocationCheck deserialize(JsonObject json, JsonDeserializationContext context) {
            String type = json.has("type") ? GsonHelper.getAsString(json, "type") : null;
            String biomeTag = json.has("biome_tag") ? GsonHelper.getAsString(json, "biome_tag") : null;
            return new AdvancedLocationCheck(Type.fromString(type), biomeTag(biomeTag));
        }

        public TagKey<Biome> biomeTag(String location) {
            if (location != null)
                return TagKey.create(Registries.BIOME, new ResourceLocation(location));
            return null;
        }
    }

    public enum Type {
        NONE(""), EXPOSED("exposed"), RAINY("rainy"), SNOWY("snowy");

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
                    return NONE;
                } else
                    return locationType.get();
            }
            return NONE;
        }
    }
}
