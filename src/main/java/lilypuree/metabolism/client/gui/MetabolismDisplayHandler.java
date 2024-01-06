package lilypuree.metabolism.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.client.ClientHandler;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.metabolism.Metabolism;
import lilypuree.metabolism.util.Anchor;
import net.minecraft.Util;
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

public class MetabolismDisplayHandler extends Screen {
    public static final MetabolismDisplayHandler INSTANCE = new MetabolismDisplayHandler(Component.empty());
    private static final ResourceLocation TEXTURE = new ResourceLocation(MetabolismMod.MOD_ID, "textures/gui/hud.png");

    private float lastWarmth = Float.MAX_VALUE;
    private float lastProgress = -1;
    private int animationFrame = 0;
    private float animationFrameTime = 0.0F;
    private boolean displayWarmth = false;
    private long warmthDisplayTime = 0L;
    private long heatDisplayTime = 0L;


    private MetabolismDisplayHandler(Component title) {
        super(title);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.DEBUG_TEXT.type() || !Config.CLIENT.metabolismOverlayShow()) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || player.getAbilities().invulnerable) return;

        Metabolism metabolism = ClientHandler.getClientMetabolism(mc);
        render(event.getGuiGraphics(), mc, metabolism);
    }


    public void render(GuiGraphics graphics, Minecraft mc, Metabolism metabolism) {
        RenderSystem.enableBlend();
        Anchor anchor = Config.CLIENT.metabolismOverlayAnchor();
        int posX = anchor.getX(mc.getWindow().getGuiScaledWidth(), 25, 5) + Config.CLIENT.metabolismOverlayOffsetX();
        int posY = anchor.getY(mc.getWindow().getGuiScaledHeight(), 81, 5) + Config.CLIENT.metabolismOverlayOffsetY();

        graphics.blit(TEXTURE, posX, posY, 231, 0, 25, 81); //background
        graphics.blit(TEXTURE, posX + 3, posY + 60, 18, 9, 9, 9); //food icon
        graphics.blit(TEXTURE, posX + 13, posY + 60, 27, 9, 9, 9); //hydration icon

        renderBar(graphics, posX + 3, posY + 11, 186, Mth.floor(metabolism.getFood()));
        renderBar(graphics, posX + 13, posY + 11, 196, Mth.floor(metabolism.getHydration()));


        float progress = metabolism.getProgress();
        if (lastProgress > progress && lastWarmth < metabolism.getWarmth()) {
            displayWarmth = true;
            warmthDisplayTime = Util.getMillis();
        }
        if (displayWarmth) {
            graphics.blit(TEXTURE, posX + 1, posY + 3, 207, 3, 23, 62); //entire arrow
            graphics.blit(TEXTURE, posX + 8, posY + 1, 0, 0, 9, 9); //warmth icon
            displayWarmth = (Util.getMillis() - warmthDisplayTime) < 1000L;
        } else if (progress > 0) {
            int length = Mth.ceil(progress * 56);
            graphics.blit(TEXTURE, posX + 1, posY + 64, 207, 64, 23, 1); //starting tip
            graphics.blit(TEXTURE, posX + 1, posY + 64 - length, 207, 8, 23, length); //bars
        }
        lastProgress = progress;
        lastWarmth = metabolism.getWarmth();

        float heat = metabolism.getHeat();
        if (heat != 0) {
            if (Util.getMillis() - heatDisplayTime > animationFrameTime) {
                heatDisplayTime = Util.getMillis();
                animationFrame = (animationFrame + 1) % 8;
                animationFrameTime = 50 * metabolism.drainDuration() / 8;
            }
            int animationU = animationFrame * 7;
            if (heat > 0) {
                graphics.blit(TEXTURE, posX + 8, posY + 71, 0, 9, 9, 9); //heat icon
                graphics.blit(TEXTURE, posX + 17, posY + 64, animationU, 64, 7, 14); //heat arrows
            } else {
                graphics.blit(TEXTURE, posX + 8, posY + 71, 0, 18, 9, 9); //heat icon
                graphics.blit(TEXTURE, posX + 1, posY + 64, animationU, 48, 7, 14); //heat arrows
            }
        } else {
            animationFrame = 0;
            animationFrameTime = 0.0F;
        }

        RenderSystem.disableBlend();
    }

    private void renderBar(GuiGraphics graphics, int posX, int posY, int uOffset, int level) {
        posY += 40;
        int I = level % 10;
        int D = (level / 10) % 10;
        int C = level / 100;
        for (int i = 1; i < 10; i++) {
            int flag = i <= I ? 1 : 0;
            flag += i <= D ? 2 : 0;
            flag += i <= C ? 4 : 0;
            if (flag == 0) break;

            int vOffset = (flag - 1) * 6;
            graphics.blit(TEXTURE, posX, posY, uOffset, vOffset, 9, 6);
            posY -= 5;
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
