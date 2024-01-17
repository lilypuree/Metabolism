package lilypuree.metabolism.platform;

import eu.midnightdust.lib.config.MidnightConfig;
import lilypuree.metabolism.platform.services.MetabolismClientConfig;
import lilypuree.metabolism.util.Anchor;

public class FabricClientConfig extends MidnightConfig implements MetabolismClientConfig {

    @Comment(category = "text") public static Comment debug;
    @Entry(category = "text")
    public static boolean debugShowOverlay = false;
    @Entry(category = "text")
    public static Anchor debugOverlayAnchor = Anchor.TOP_RIGHT;
    @Entry(category = "numbers")
    public static float debugOverlayTextScale = 0.75f;

    @Comment(category = "metabolism hud") public static Comment metabolismHud;
    @Entry(category = "text")
    public static boolean metabolismOverlayShow = true;
    @Entry(category = "text")
    public static Anchor metabolismOverlayAnchor = Anchor.BOTTOM_LEFT;
    @Entry(category = "numbers")
    public static int metabolismOverlayOffsetX = 0;
    @Entry(category = "numbers")
    public static int metabolismOverlayOffsetY = 0;
    @Entry(category = "numbers")
    public static float metabolismOverlayTextScale = 0.0F;

    @Comment(category = "text") public static Comment tooltip;
    @Entry(category = "text")
    public static boolean showToolTip = true;
    @Entry(category = "text")
    public static boolean alwaysShowToolTip = true;
    
    @Override
    public boolean debugShowOverlay() {
        return debugShowOverlay;
    }

    @Override
    public Anchor debugOverlayAnchor() {
        return debugOverlayAnchor;
    }

    @Override
    public float debugOverlayTextScale() {
        return debugOverlayTextScale;
    }

    @Override
    public boolean metabolismOverlayShow() {
        return metabolismOverlayShow;
    }

    @Override
    public Anchor metabolismOverlayAnchor() {
        return metabolismOverlayAnchor;
    }

    @Override
    public int metabolismOverlayOffsetX() {
        return metabolismOverlayOffsetX;
    }

    @Override
    public int metabolismOverlayOffsetY() {
        return metabolismOverlayOffsetY;
    }

    @Override
    public float metabolismOverlayTextScale() {
        return metabolismOverlayTextScale;
    }

    @Override
    public boolean showToolTip() {
        return showToolTip;
    }

    @Override
    public boolean alwaysShowToolTip() {
        return alwaysShowToolTip;
    }

    @Override
    public void reload() {

    }
}
