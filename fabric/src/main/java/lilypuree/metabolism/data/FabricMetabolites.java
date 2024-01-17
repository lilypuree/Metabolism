package lilypuree.metabolism.data;

import lilypuree.metabolism.Constants;
import lilypuree.metabolism.core.metabolite.Metabolites;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricMetabolites extends Metabolites implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(Constants.MOD_ID, getName());
    }
}
