package lilypuree.metabolism.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.client.ClientHandler;
import lilypuree.metabolism.metabolism.Metabolism;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WarmthDisplayHandler extends Screen {
    public static final WarmthDisplayHandler INSTANCE = new WarmthDisplayHandler(Component.empty());

    private static final ResourceLocation TEXTURE = new ResourceLocation(MetabolismMod.MOD_ID, "textures/gui/hud.png");


    private WarmthDisplayHandler(Component pTitle) {
        super(pTitle);
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onFoodDraw(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() != VanillaGuiOverlay.FOOD_LEVEL.type() ||
                Minecraft.getInstance().options.hideGui ||
                !getGui().shouldDrawSurvivalElements()
        ) return;

        Minecraft mc = Minecraft.getInstance();

        event.setCanceled(true);
        mc.getProfiler().push("metabolismRenderWarmth");
        renderWarmth(event.getGuiGraphics(), mc);

        mc.getProfiler().pop();
    }

    private void renderWarmth(GuiGraphics graphics, Minecraft mc) {
        RenderSystem.enableBlend();
        int left = mc.getWindow().getGuiScaledWidth() / 2 + 91;
        int top = mc.getWindow().getGuiScaledHeight() - getGui().rightHeight;
        int tickCount = getGui().getGuiTicks();
        getGui().rightHeight += 10;

        Metabolism metabolism = ClientHandler.getClientMetabolism(mc);
        int maxOrbs = Mth.floor(metabolism.getMaxWarmth() / 2);
        float heat = metabolism.getHeat();
        int heatOrColdSpriteV = heat > 0 ? 9 : 18;
        int warmth = Mth.floor(metabolism.getWarmth());
        int heatThreshold = Mth.floor(metabolism.getMaxWarmth() - Math.abs(heat));
        for (int orb = 0; orb < maxOrbs; ++orb) {
            int idx = orb * 2 + 1;
            int x = left - orb * 8 - 9;
            int y = top;

            graphics.blit(TEXTURE, x, top, 18, 0, 9, 9);


            if (idx < warmth)
                graphics.blit(TEXTURE, x, y, 0, 0, 9, 9);
            else if (idx == warmth)
                graphics.blit(TEXTURE, x, y, 9, 0, 9, 9);


            if (idx == heatThreshold)
                graphics.blit(TEXTURE, x, y, 9, heatOrColdSpriteV, 9, 9);
            else if (idx > heatThreshold)
                graphics.blit(TEXTURE, x, y, 0, heatOrColdSpriteV, 9, 9);
        }
        RenderSystem.disableBlend();
    }


    public static ForgeGui getGui() {
        return (ForgeGui) Minecraft.getInstance().gui;
    }

}
