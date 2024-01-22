package lilypuree.metabolism.client;

import com.mojang.datafixers.util.Either;
import lilypuree.metabolism.Constants;
import lilypuree.metabolism.client.gui.DebugOverlay;
import lilypuree.metabolism.client.gui.MetabolismDisplayHandler;
import lilypuree.metabolism.client.gui.tooltip.ToolTipOverlayHandler;
import lilypuree.metabolism.client.gui.WarmthDisplayHandler;
import lilypuree.metabolism.client.gui.tooltip.MetaboliteToolTip;
import lilypuree.metabolism.client.gui.tooltip.MetaboliteToolTipRenderer;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.core.Metabolism;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEventHandler {
    
    public static void onToolTipRegister(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(MetaboliteToolTip.class, MetaboliteToolTipRenderer::new);
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent event) {
        Minecraft mc = Minecraft.getInstance();
        boolean showGui = !mc.options.hideGui;
        ForgeGui gui = ((ForgeGui) mc.gui);

        boolean shouldDrawSurvivalElements = gui.shouldDrawSurvivalElements();

        if (showGui && shouldDrawSurvivalElements) {
            if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) {
                event.setCanceled(true);
                mc.getProfiler().push("metabolismRenderWarmth");
                gui.rightHeight = WarmthDisplayHandler.INSTANCE.renderWarmth(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), event.getGuiGraphics(), gui.rightHeight);
                mc.getProfiler().pop();
            } else if (event.getOverlay() == VanillaGuiOverlay.DEBUG_TEXT.type() && Config.CLIENT.metabolismOverlayShow()) {
                Player player = mc.player;
                if (player != null && !player.getAbilities().invulnerable) {
                    Metabolism metabolism = ClientHandler.getClientMetabolism(mc);
                    MetabolismDisplayHandler.INSTANCE.render(event.getGuiGraphics(), mc, metabolism);
                }
            }
        }
    }

    @SubscribeEvent
    public static void gatherToolTips(RenderTooltipEvent.GatherComponents event) {
        if (event.isCanceled())
            return;

        var toolTip = ToolTipOverlayHandler.getToolTip(event.getItemStack());
        if (toolTip != null) {
            event.getTooltipElements().add(Either.right(toolTip));
        }
    }

    @SubscribeEvent
    public static void onRenderTick(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == VanillaGuiOverlay.CHAT_PANEL.type()) {
            DebugOverlay.INSTANCE.renderTick(event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            DebugOverlay.INSTANCE.clientTick();
        }
    }
}
