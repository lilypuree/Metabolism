package lilypuree.metabolism.client;

import lilypuree.metabolism.client.ClientHandler;
import lilypuree.metabolism.client.gui.DebugOverlay;
import lilypuree.metabolism.client.gui.MetabolismDisplayHandler;
import lilypuree.metabolism.client.gui.tooltip.MetaboliteComponent;
import lilypuree.metabolism.client.gui.tooltip.MetaboliteToolTip;
import lilypuree.metabolism.client.gui.tooltip.ToolTipOverlayHandler;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.core.Metabolism;
import lilypuree.metabolism.network.FabricNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricNetwork.init();
        
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            DebugOverlay.INSTANCE.renderTick(graphics);
        });
        ClientTickEvents.START_CLIENT_TICK.register(mc -> {
            DebugOverlay.INSTANCE.clientTick();
        });
        ItemTooltipCallback.EVENT.register((hoveredStack, context, lines) -> {
            // When hoveredStack or tooltip is null an unknown exception occurs.
            // If ModConfig.INSTANCE is null then we're probably still in the init phase
            if (hoveredStack == null || lines == null || Config.CLIENT == null)
                return;

            MetaboliteToolTip metaboliteToolTip = ToolTipOverlayHandler.getToolTip(hoveredStack);
            if (metaboliteToolTip != null)
                lines.add(new MetaboliteComponent(metaboliteToolTip));
        });
    }
}
