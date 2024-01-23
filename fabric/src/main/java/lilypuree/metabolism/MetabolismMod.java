package lilypuree.metabolism;

import eu.midnightdust.lib.config.MidnightConfig;
import lilypuree.metabolism.command.MetabolismCommand;
import lilypuree.metabolism.core.metabolite.Metabolites;
import lilypuree.metabolism.data.FabricEnvironments;
import lilypuree.metabolism.data.FabricMetabolites;
import lilypuree.metabolism.platform.FabricConfig;
import lilypuree.metabolism.registration.Registration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class MetabolismMod implements ModInitializer {
    @Override
    public void onInitialize() {
        Registration.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            MetabolismCommand.register(dispatcher);
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricMetabolites());
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricEnvironments());
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(Metabolites::syncMetabolites);
        MidnightConfig.init(Constants.MOD_ID, FabricConfig.class);
    }
}
