//
// Credits: SilentLib by SilentChaos512
//

package lilypuree.metabolism.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import lilypuree.metabolism.util.Anchor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.Nonnegative;
import java.util.ArrayList;
import java.util.List;

public abstract class DebugRenderOverlay {
    protected static final String SPLITTER = "=";
    private static final int DEFAULT_UPDATE_FREQUENCY = 10;
    private static final int DEFAULT_SPLIT_WIDTH = 100;
    private static final int LINE_HEIGHT = 10;
    private List<String> debugText;
    private int textWidth;
    private int textHeight;

    private int ticksPassed;

    protected DebugRenderOverlay() {
        this.debugText = new ArrayList<>();
        this.ticksPassed = 0;
    }

    public abstract List<String> getDebugText();

    public abstract float getTextScale();

    public Anchor getAnchorPoint() {
        return Anchor.TOP_LEFT;
    }

    public int getMarginSize() {
        return 3;
    }

    @Nonnegative
    public int getUpdateFrequency() {
        return DEFAULT_UPDATE_FREQUENCY;
    }

    @Nonnegative
    public int getSplitWidth() {
        return DEFAULT_SPLIT_WIDTH;
    }

    public abstract boolean isHidden();


    protected void drawLine(GuiGraphics graphics, Font font, String line, int x, int y, int color) {
        String[] array = line.split(SPLITTER);
        if (array.length == 2) {
            graphics.drawString(font, array[0].trim(), x, y, color);
            graphics.drawString(font, array[1].trim(), x + this.getSplitWidth(), y, color);
        } else {
            graphics.drawString(font, line, x, y, color);
        }
    }

    public void renderTick(GuiGraphics graphics) {
        Minecraft mc = Minecraft.getInstance();
        if (!this.isHidden() && !this.debugText.isEmpty() && !mc.isPaused() && !mc.options.renderDebug) {
            float scale = this.getTextScale();
            if (scale > 0.0F) {
                Font font = mc.font;
                PoseStack matrix = graphics.pose();
                matrix.pushPose();
                matrix.scale(scale, scale, 1.0F);
                Window mainWindow = mc.getWindow();
                int x = (int) ((float) this.getAnchorPoint().getX(mainWindow.getGuiScaledWidth(), this.textWidth, this.getMarginSize()) / this.getTextScale());
                int y = (int) ((float) this.getAnchorPoint().getY(mainWindow.getGuiScaledHeight(), this.textHeight, this.getMarginSize()) / this.getTextScale());

                for (String line : debugText) {
                    this.drawLine(graphics, font, line, x, y, 16777215);
                    y += LINE_HEIGHT;
                }
                matrix.popPose();
            }
        }
    }

    public void clientTick() {
        if (this.getUpdateFrequency() == 0 || ++this.ticksPassed % this.getUpdateFrequency() == 0) {
            this.debugText = this.getDebugText();
            Font font = Minecraft.getInstance().font;
            this.textWidth = 0;
            this.textHeight = LINE_HEIGHT * this.debugText.size();
            debugText.forEach(line -> {
                String[] array = line.split(SPLITTER);
                if (array.length == 2) {
                    int width = this.getSplitWidth() + font.width(array[1]);
                    this.textWidth = Math.max(this.textWidth, width);
                } else {
                    this.textWidth = Math.max(this.textWidth, font.width(line));
                }
            });
        }
    }

}
