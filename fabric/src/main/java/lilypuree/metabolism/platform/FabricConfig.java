package lilypuree.metabolism.platform;

import eu.midnightdust.lib.config.MidnightConfig;
import lilypuree.metabolism.platform.services.MetabolismClientConfig;
import lilypuree.metabolism.platform.services.MetabolismServerConfig;
import lilypuree.metabolism.util.Anchor;

public class FabricConfig extends MidnightConfig implements MetabolismServerConfig, MetabolismClientConfig {

    //Server Config
    @Server
    @Comment(category = "text", centered = true)
    public static Comment server;
    
    @Server
    @Entry(category = "numbers")
    public static boolean preciseFeedback = false;

    @Override
    public boolean preciseFeedback() {
        return preciseFeedback;
    }
    

//Client Config

    @Client
    @Comment(category = "text", centered = true)
    public static Comment client;
    
    @Client
    @Comment(category = "text")
    public static Comment debug;
    @Client

    @Entry(category = "text")
    public static boolean debugShowOverlay = false;
    @Client

    @Entry(category = "text")
    public static Anchor debugOverlayAnchor = Anchor.TOP_RIGHT;
    @Client

    @Entry(category = "numbers")
    public static float debugOverlayTextScale = 0.75f;
    @Client


    @Comment(category = "metabolism hud")
    public static Comment metabolismHud;
    @Client

    @Entry(category = "text")
    public static boolean metabolismOverlayShow = true;
    @Client
    @Entry(category = "text")
    public static Anchor metabolismOverlayAnchor = Anchor.BOTTOM_LEFT;
    @Client
    @Entry(category = "numbers")
    public static int metabolismOverlayOffsetX = 0;
    @Client
    @Entry(category = "numbers")
    public static int metabolismOverlayOffsetY = 0;
    @Client
    @Entry(category = "numbers")
    public static float metabolismOverlayTextScale = 0.75F;
    @Client

    @Comment(category = "text")
    public static Comment tooltip;
    @Client
    @Entry(category = "text")
    public static boolean showToolTip = true;
    @Client
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
