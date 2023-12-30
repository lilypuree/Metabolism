package lilypuree.metabolism.util;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
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

import java.util.Set;

public class AdvancedWeatherCheck implements LootItemCondition {

    final WeatherType weatherType;

    public AdvancedWeatherCheck(WeatherType weatherType) {
        this.weatherType = weatherType;
    }

    @Override
    public LootItemConditionType getType() {
        return Registration.ADVANCED_WEATHER_CHECK.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ORIGIN);
    }

    @Override
    public boolean test(LootContext context) {
        ServerLevel level = context.getLevel();
        Vec3 origin = context.getParam(LootContextParams.ORIGIN);
        BlockPos pos = BlockPos.containing(origin.x, origin.y, origin.z);
        if (canHaveWeather(level, pos)) {
            Holder<Biome> biome = level.getBiome(pos);
            Biome.Precipitation precipitation = biome.value().getPrecipitationAt(pos);
            switch (weatherType) {
                case SUNNY -> {
                    return level.isDay();
                }
                case RAIN -> {
                    return level.isRaining() && precipitation == Biome.Precipitation.RAIN;
                }
                case SNOW -> {
                    return level.isRaining() && precipitation == Biome.Precipitation.SNOW;
                }
                case HEATWAVE -> {
                    return level.isDay() && level.getBiome(pos).is(MetabolismTags.HOT_BIOMES);
                }
            }
        }
        return false;
    }

    public boolean canHaveWeather(ServerLevel level, BlockPos pPos) {
        if (!level.canSeeSky(pPos)) {
            return false;
        } else return level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pPos).getY() <= pPos.getY();
    }


    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<AdvancedWeatherCheck> {
        public void serialize(JsonObject json, AdvancedWeatherCheck value, JsonSerializationContext context) {
            json.addProperty("weather_type", value.weatherType.name());
        }

        /**
         * Deserialize a value by reading it from the JsonObject.
         */
        public AdvancedWeatherCheck deserialize(JsonObject json, JsonDeserializationContext context) {
            String type = json.has("weather_type") ? GsonHelper.getAsString(json, "weather_type") : null;
            return new AdvancedWeatherCheck(WeatherType.fromString(type));
        }
    }
}
