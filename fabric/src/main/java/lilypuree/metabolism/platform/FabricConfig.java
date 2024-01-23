package lilypuree.metabolism.platform;

import eu.midnightdust.lib.config.MidnightConfig;
import lilypuree.metabolism.platform.services.MetabolismClientConfig;
import lilypuree.metabolism.platform.services.MetabolismServerConfig;
import lilypuree.metabolism.util.Anchor;

public class FabricConfig extends MidnightConfig implements MetabolismServerConfig, MetabolismClientConfig { 
    
    //Server Config
    @Server
    @Entry(category = "server")
    public static boolean preciseFeedback = false;
    @Server
    @Entry(category = "server")
    public static boolean disableHeat = false;
    
    @Server
    @Entry(category = "server")
    public static boolean convertResources = true;
    
    @Override
    public boolean preciseFeedback() {
        return preciseFeedback;
    }

    @Override
    public boolean disableHeat() {
        return disableHeat;
    }

    @Override
    public boolean convertResources() {
        return convertResources;
    }

    //Client Config
    
    @Client
    @Entry(category = "debug")
    public static boolean debugShowOverlay = false;
    @Client
    @Entry(category = "debug")
    public static Anchor debugOverlayAnchor = Anchor.TOP_RIGHT;
    @Client
    @Entry(category = "debug")
    public static float debugOverlayTextScale = 0.75f;
 
    @Client
    @Entry(category = "metabolism_hud")
    public static boolean metabolismOverlayShow = true;
    @Client
    @Entry(category = "metabolism_hud")
    public static Anchor metabolismOverlayAnchor = Anchor.BOTTOM_LEFT;
    @Client
    @Entry(category = "metabolism_hud")
    public static int metabolismOverlayOffsetX = 0;
    @Client
    @Entry(category = "metabolism_hud")
    public static int metabolismOverlayOffsetY = 0;
    @Client
    @Entry(category = "metabolism_hud")
    public static float metabolismOverlayTextScale = 0.75F;
    
    @Client
    @Entry(category = "tooltip")
    public static boolean showToolTip = true;
    @Client
    @Entry(category = "tooltip")
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
