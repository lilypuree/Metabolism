package lilypuree.metabolism.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.client.ClientHandler;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.metabolism.Metabolism;
import lilypuree.metabolism.util.Anchor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnergyDisplayHandler extends Screen {
    public static final EnergyDisplayHandler INSTANCE = new EnergyDisplayHandler(Component.empty());
    private static final ResourceLocation TEXTURE = new ResourceLocation(MetabolismMod.MOD_ID, "textures/gui/hud.png");

    private EnergyDisplayHandler(Component title) {
        super(title);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.DEBUG_TEXT.type() || !Config.CLIENT.energyBarShow()) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        Metabolism metabolism = ClientHandler.getClientMetabolism(mc);
        render(event.getGuiGraphics(), mc, Mth.floor(metabolism.getFood()), Mth.floor(metabolism.getEnergy()));
    }

    public void render(GuiGraphics graphics, Minecraft mc, int foodLevel, int energyLevel) {
        Anchor anchor = Config.CLIENT.energyBarAnchor();
        int posX = anchor.getX(mc.getWindow().getGuiScaledWidth(), 21, 5) + Config.CLIENT.energyBarOffsetX();
        int posY = anchor.getY(mc.getWindow().getGuiScaledHeight(), 50, 5) + Config.CLIENT.energyBarOffsetY();

        graphics.blit(TEXTURE, posX, posY, 235, 0, 21, 50);
        renderBar(graphics, posX + 2, posY, 216, foodLevel);
        renderBar(graphics, posX + 12, posY, 226, energyLevel);
    }

    private void renderBar(GuiGraphics graphics, int posX, int posY, int uOffset, int level) {
        posY += 34;
        int I = level % 10;
        int D = (level / 10) % 10;
        int C = level / 100;
        for (int i = 1; i < 10; i++) {
            int flag = i <= I ? 1 : 0;
            flag += i <= D ? 2 : 0;
            flag += i <= C ? 4 : 0;
            if (flag == 0) break;

            int vOffset = (flag - 1) * 4;
            graphics.blit(TEXTURE, posX, posY, uOffset, vOffset, 7, 3);
            posY -= 4;
        }
    }

    private void renderStat(GuiGraphics graphics, Minecraft mc, int posX, int posY, float textScale, int stat) {
        if (textScale > 0) {
            PoseStack stack = graphics.pose();
            stack.pushPose();
            stack.scale(textScale, textScale, 1);
            graphics.drawString(
                    mc.font, String.format("x %d", stat),
                    posX / textScale + 12,
                    posY / textScale + 6,
                    0x00ff00,
                    true);
            stack.popPose();
        }
    }
}
