package lilypuree.metabolism;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class MetabolismTags {
    public static TagKey<Biome> biomeTag(String path) {
        if (path != null)
            return TagKey.create(Registries.BIOME, new ResourceLocation(MetabolismMod.MOD_ID, path));
        return null;
    }

    public static final TagKey<Biome> HOT_BIOMES = biomeTag("hot_biomes");
    public static final TagKey<Biome> COLD_BIOMES = biomeTag("cold_biomes");
}
