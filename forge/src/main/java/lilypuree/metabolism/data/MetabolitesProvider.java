package lilypuree.metabolism.data;

import com.mojang.logging.LogUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

public class MetabolitesProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final String modid;

    public MetabolitesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modid, ExistingFileHelper existingFileHelper) {
        this.modid = modid;
    }

    @Override
    public String getName() {
        return "Metabolites for mod " + modid;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return null;
    }


}
