package lilypuree.metabolism.metabolite;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import lilypuree.metabolism.client.ClientMetabolites;
import lilypuree.metabolism.util.Overrides;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Metabolites extends SimpleJsonResourceReloadListener {
    private static Metabolites currentInstance = null;
    private static Metabolites reloadingInstance = null;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Logger LOGGER = LogManager.getLogger("Metabolites");
    public static final String FOLDER = "metabolites";

    private static final Map<Item, Metabolite.Modifier> originalModifiers = new HashMap<>();
    private ImmutableMap<Item, Metabolite> metaboliteMap;

    public Metabolites() {
        super(GSON, FOLDER);
        if (currentInstance == null)
            currentInstance = this;
        else {
            reloadingInstance = this;
        }
    }

    public static Metabolite getMetabolite(ItemStack item, LivingEntity entity) {
        var map = getMetabolites();
        if (map.containsKey(item.getItem())) {
            return map.get(item.getItem());
        } else {
            FoodProperties properties = item.getFoodProperties(entity);
            if (properties != null)
                return Metabolite.createVanilla(properties.getNutrition(), properties.getSaturationModifier());
            else return Metabolite.NONE;
        }
    }

    public static Map<Item, Metabolite> getMetabolites() {
        return DistExecutor.unsafeRunForDist(
                () -> ClientMetabolites::getClientMetabolites,
                () -> Metabolites::getServerMetaboliteMap
        );
    }

    public static Map<Item, Metabolite> getServerMetaboliteMap() {
        if (currentInstance == null)
            throw new RuntimeException("Tried to access Metabolites too early!");
        return currentInstance.metaboliteMap;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {

        // reload original values for food, so if changes are removed they leave no trace

        originalModifiers.forEach((item, modifier) -> {
            modifier.apply(item);
        });

        ImmutableMap.Builder<Item, Metabolite> builder = ImmutableMap.builder();

        map.entrySet().stream()
                .filter(entry -> ModList.get().isLoaded(entry.getKey().getNamespace()))
                .forEach(entry -> {
                    ResourceLocation location = entry.getKey();
                    Item item = ForgeRegistries.ITEMS.getValue(location);
                    if (item != Items.AIR) {
                        Metabolite metabolite = readMetabolite(entry.getValue(), location);
                        if (metabolite.modifier() != Metabolite.Modifier.NONE) {
                            originalModifiers.put(item, Metabolite.Modifier.fromItem(item));
                            metabolite.modifier().apply(item);
                        }
                        builder.put(item, metabolite);
                    } else
                        LOGGER.warn("defined metabolite for nonexistent item " + location);
                });
        this.metaboliteMap = builder.build();

        LOGGER.debug("Finished parsing metabolites");
        if (this == reloadingInstance) {
            currentInstance = this;
            reloadingInstance = null;
        }
    }

    private Metabolite readMetabolite(JsonElement element, ResourceLocation location) {
        return Metabolite.CODEC.parse(JsonOps.INSTANCE, element)
                .getOrThrow(false, prefix("Metabolite for " + location + ": "));
    }


    private static Consumer<String> prefix(String pre) {
        return s -> LOGGER.error(pre + s);
    }
}
