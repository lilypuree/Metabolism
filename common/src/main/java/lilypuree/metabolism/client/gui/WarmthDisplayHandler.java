package lilypuree.metabolism.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import lilypuree.metabolism.Constants;
import lilypuree.metabolism.client.ClientHandler;
import lilypuree.metabolism.core.Metabolism;
import lilypuree.metabolism.registration.Registration;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class WarmthDisplayHandler extends Screen {
    public static final WarmthDisplayHandler INSTANCE = new WarmthDisplayHandler(Component.empty());

    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/hud.png");
    private long lastWarmthTime;
    private int lastWarmth;
    private int displayWarmth;

    private WarmthDisplayHandler(Component pTitle) {
        super(pTitle);
    }
    
    // returns the new value of rightHeight
    public int renderWarmth(Minecraft mc, int width, int height, GuiGraphics guiGraphics, int rightHeight) {
        RenderSystem.enableBlend();

        Player player = (Player) mc.getCameraEntity();
        Metabolism metabolism = ClientHandler.getClientMetabolism(mc);  

        int warmth = Mth.floor(metabolism.getWarmth());
        int tickCount = mc.gui.getGuiTicks();

        if (warmth < this.lastWarmth && player.invulnerableTime > 0) {
            displayWarmth = lastWarmth;
            this.lastWarmthTime = Util.getMillis();
        }

        boolean highlight = isHealthHighlighted(mc.gui) && displayWarmth > warmth;     

        if (Util.getMillis() - this.lastWarmthTime > 1000L) {
            this.displayWarmth = warmth;
            this.lastWarmthTime = Util.getMillis();
        }

        this.lastWarmth = warmth;

        float maxWarmth = Math.max(metabolism.getMaxWarmth(), Math.max(displayWarmth, warmth));
        int warmthRows = Mth.ceil((maxWarmth) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (warmthRows - 2), 3);

        int right = width / 2 + 91;
        int top = height - rightHeight;

        int offSetOrb = -1;
        if (player.hasEffect(Registration.METABOLISM_EFFECT.get())) {
            offSetOrb = tickCount % Mth.ceil(maxWarmth + 5.0F);
        }

        this.renderOrbs(guiGraphics, right, top, rowHeight, offSetOrb, maxWarmth, warmth, displayWarmth, metabolism.getHeat(), highlight);

        RenderSystem.disableBlend();
        
        return rightHeight + 10 + (warmthRows - 1) * rowHeight;
    }

    private void renderOrbs(GuiGraphics graphics, int x, int y, int height, int offsetOrbIndex, float maxWarmth, int warmth, int displayWarmth, float heat, boolean highlight) {
        int maxOrbs = Mth.ceil((double) maxWarmth / 2.0D); //max 10
        int heatThreshold = Mth.floor(maxWarmth - Math.abs(heat));
        OrbType heatOrb = heat > 0 ? OrbType.HOT : OrbType.COLD;

        for (int orb = 0; orb < maxOrbs; ++orb) {
            int row = orb / 10;
            int column = orb % 10;
            int orbX = x - column * 8 - 9;
            int orbY = y - row * height;
            if (orb == offsetOrbIndex) {
                orbY -= 2;
            }

            this.renderOrb(graphics, OrbType.CONTAINER, orbX, orbY, highlight, false);
            int floor = orb * 2;

            if (highlight && floor < displayWarmth) {
                boolean isHalf = floor + 1 == displayWarmth;
                this.renderOrb(graphics, OrbType.WARMTH, orbX, orbY, true, isHalf);
            }

            if (floor < warmth) {
                boolean isHalf = floor + 1 == warmth;
                this.renderOrb(graphics, OrbType.WARMTH, orbX, orbY, false, isHalf);
            }

            if (floor + 2 > heatThreshold) {
                boolean isHalf = floor + 1 == heatThreshold;
                this.renderOrb(graphics, heatOrb, orbX, orbY, false, isHalf);
            }
        }
    }

    private void renderOrb(GuiGraphics pGuiGraphics, OrbType orbType, int x, int y, boolean highlight, boolean isHalfOrb) {
        pGuiGraphics.blit(TEXTURE, x, y, orbType.getX(isHalfOrb, highlight), orbType.getY(), 9, 9);
    }

    private boolean isHealthHighlighted(Gui gui) {
        return gui.healthBlinkTime > (long) gui.getGuiTicks() && (gui.healthBlinkTime - (long) gui.getGuiTicks()) / 3L % 2L == 1L;
    }

    static enum OrbType {
        CONTAINER(true),
        WARMTH(true),
        HOT(false),
        COLD(false);

        private final boolean canBlink;

        OrbType(boolean canBlink) {
            this.canBlink = canBlink;
        }

        public int getX(boolean halfOrb, boolean highlight) {
            int i;
            if (this == CONTAINER) {
                i = highlight ? 1 : 0;
            } else {
                i = (halfOrb ? 1 : 0) + ((canBlink && highlight) ? 2 : 0);
            }
            return 9 + i * 9;
        }

        public int getY() {
            return ordinal() * 9;
        }
    }

}
