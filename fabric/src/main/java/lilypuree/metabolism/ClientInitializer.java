package lilypuree.metabolism;

import lilypuree.metabolism.client.ClientHandler;
import lilypuree.metabolism.client.gui.DebugOverlay;
import lilypuree.metabolism.client.gui.MetabolismDisplayHandler;
import lilypuree.metabolism.client.gui.ToolTipOverlayHandler;
import lilypuree.metabolism.core.Metabolism;
import lilypuree.metabolism.registration.Registration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (!mc.options.hideGui && player != null && !player.getAbilities().invulnerable) {
                Metabolism metabolism = ClientHandler.getClientMetabolism(mc);
                MetabolismDisplayHandler.INSTANCE.render(graphics, mc, metabolism);
            }
            DebugOverlay.INSTANCE.renderTick(graphics);
        });
        ClientTickEvents.START_CLIENT_TICK.register(mc -> {
            DebugOverlay.INSTANCE.clientTick();
        });        
    }
}
