package lilypuree.metabolism.client.gui;

import com.google.common.collect.ImmutableList;
import lilypuree.metabolism.client.ClientHandler;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.metabolism.Metabolism;
import lilypuree.metabolism.util.Anchor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.List;

public class DebugOverlay extends DebugRenderOverlay {
    private static final String FLOAT_FORMAT = "%.5f";
    public static DebugOverlay INSTANCE = new DebugOverlay();

    public static void init() {
    }

    @Override
    public List<String> getDebugText() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return ImmutableList.of();
        Metabolism metabolism = ClientHandler.getClientMetabolism(Minecraft.getInstance());
        return ImmutableList.of(
                "- Warmth=" + String.format(FLOAT_FORMAT, metabolism.getWarmth()),
                "- Heat=" + String.format(FLOAT_FORMAT, metabolism.getHeat()),
                "- Food=" + String.format(FLOAT_FORMAT, metabolism.getFood()),
                "- Energy=" + String.format(FLOAT_FORMAT, metabolism.getEnergy())
        );
    }


    @Override
    public boolean isHidden() {
        return !Config.CLIENT.debugShowOverlay();
    }

    @Override
    public Anchor getAnchorPoint() {
        return Config.CLIENT.debugOverlayAnchor();
    }

    @Override
    public float getTextScale() {
        return Config.CLIENT.debugOverlayTextScale();
    }
}
