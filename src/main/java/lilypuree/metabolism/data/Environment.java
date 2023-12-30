package lilypuree.metabolism.data;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import lilypuree.metabolism.MetabolismMod;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class Environment extends SimpleJsonResourceReloadListener {

    private static Environment currentInstance = null;
    private static Environment reloadingInstance = null;
    private static final Gson GSON = LootDataType.PREDICATE.parser();
    public static final Logger LOGGER = LogManager.getLogger("Environment Effects");
    public static final String FOLDER = "environment_effects";
    private ImmutableSet<EnvironmentEffect> localEffects;
    private ImmutableSet<EnvironmentEffect> rangedEffects;
    private float maxRange = 0.0F;

    public Environment() {
        super(GSON, FOLDER);
        if (currentInstance == null)
            currentInstance = this;
        else
            reloadingInstance = this;
    }

    public static Environment get() {
        if (currentInstance == null)
            throw new RuntimeException("Tried to access Environment too early!");
        return currentInstance;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableSet.Builder<EnvironmentEffect> locals = ImmutableSet.builder();
        ImmutableSet.Builder<EnvironmentEffect> ranged = ImmutableSet.builder();
        map.entrySet().stream()
                .filter(entry -> entry.getKey().getNamespace().equals(MetabolismMod.MOD_ID))
                .forEach(entry -> {
                    try {
                        EnvironmentEffect effect = EnvironmentEffect.deserialize(entry.getKey(), entry.getValue(), resourceManager);
                        if (effect.isRanged()) {
                            ranged.add(effect);
                            if (effect.range > this.maxRange) {
                                this.maxRange = effect.range;
                            }
                        } else
                            locals.add(effect);
                    } catch (JsonParseException exception) {
                        LOGGER.error("Cannot parse environment effect " + entry.getKey(), exception);
                    }
                });
        this.localEffects = locals.build();
        this.rangedEffects = ranged.build();

        LOGGER.debug("Finished parsing environment effects");
        if (this == reloadingInstance) {
            currentInstance = this;
            reloadingInstance = null;
        }
    }

    public EnvironmentEffect.Combined getCurrentEffect(ServerLevel level, Player player) {
        LootParams.Builder builder = (new LootParams.Builder(level)).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.ORIGIN, player.position());
        LootParams params = builder.create(LootContextParamSets.SELECTOR);
        LootContext lootContext = new LootContext.Builder(params).create(null);

        EnvironmentEffect.Combined combined = new EnvironmentEffect.Combined(level.isNight());
        localEffects.stream()
                .filter(effect -> effect.canApply(lootContext))
                .forEach(combined::addEffect);

        applyRangedEffects(level, player, combined);
        return combined;
    }


    private void applyRangedEffects(ServerLevel level, Player player, EnvironmentEffect.Combined combined) {
        AABB range = AABB.ofSize(player.position(), maxRange * 2, maxRange * 2, maxRange * 2);
        BlockPos.betweenClosedStream(range).forEach(pos -> {
            Vec3 position = pos.getCenter();
            float distToPlayer = Mth.sqrt((float) player.position().distanceToSqr(position));
            rangedEffects.stream()
                    .filter(effect -> effect.canApplyRanged(level, position, distToPlayer))
                    .forEach(combined::addEffect);
        });
    }
}
