package lilypuree.metabolism;

import lilypuree.metabolism.command.MetabolismCommand;
import lilypuree.metabolism.core.metabolite.Metabolites;
import lilypuree.metabolism.data.FabricEnvironments;
import lilypuree.metabolism.data.FabricMetabolites;
import lilypuree.metabolism.network.FabricNetwork;
import lilypuree.metabolism.registration.MetabolismGameRules;
import lilypuree.metabolism.registration.Registration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;

public class MetabolismMod implements ModInitializer {
    @Override
    public void onInitialize() {
        Registration.init();
        FabricNetwork.init();
        MetabolismGameRules.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            MetabolismCommand.register(dispatcher);
        });
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricMetabolites());
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricEnvironments());
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!player.isSpectator()) {
                ItemStack result = CommonCallbacks.onPlayerItemUse(player, player.getItemInHand(hand));
                if (result != null)
                    return InteractionResultHolder.success(result);
            }
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        });
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(Metabolites::syncMetabolites);
    }
}
