package lilypuree.metabolism.platform;

import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.platform.services.MetabolismClientConfig;
import lilypuree.metabolism.util.Anchor;
import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeClientConfig implements MetabolismClientConfig {
    public final ForgeConfigSpec.BooleanValue debugShowOverlay;
    public final ForgeConfigSpec.EnumValue<Anchor> debugOverlayAnchor;
    public final ForgeConfigSpec.DoubleValue debugOverlayTextScale;

    public final ForgeConfigSpec.BooleanValue metabolismOverlayShow;
    public final ForgeConfigSpec.EnumValue<Anchor> metabolismOverlayAnchor;
    public final ForgeConfigSpec.IntValue metabolismOverlayOffsetX;
    public final ForgeConfigSpec.IntValue metabolismOverlayOffsetY;
    public final ForgeConfigSpec.DoubleValue metabolismTextScale;

    public final ForgeConfigSpec.BooleanValue showToolTip;
    public final ForgeConfigSpec.BooleanValue alwaysShowToolTip;

    public ForgeClientConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        debugShowOverlay = builder.comment("Enable debug overlay").define("debug.overlay.show", false);
        debugOverlayAnchor = builder.comment("Position of the debug overlay").defineEnum("debug.overlay.anchor", Anchor.TOP_RIGHT);
        debugOverlayTextScale = builder.comment("Overlay text size. 1 = standard-sized text").defineInRange("debug.overlay.textscale", 0.75, 0.01, Double.MAX_VALUE);

        metabolismOverlayShow = builder.comment("Make the metabolism hud visible").define("metabolism.overlay.show", true);
        metabolismOverlayAnchor = builder.comment("Position of the metabolism hud").defineEnum("metabolism.overlay.anchor", Anchor.BOTTOM_LEFT);
        metabolismOverlayOffsetX = builder.comment("Fine-tune the metabolism hud position").defineInRange("metabolism.overlay.offsetX", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        metabolismOverlayOffsetY = builder.comment("Fine-tune the metabolism hud position").defineInRange("metabolism.overlay.offsetY", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        metabolismTextScale = builder.comment("Display scale of the metabolism hud text").defineInRange("metabolism.overlay.textscale", 0.6, 0, Double.MAX_VALUE);

        showToolTip = builder.comment("Show metabolite tooltips").define("tooltip.show", true);
        alwaysShowToolTip = builder.comment("Always show metabolite tooltips").define("tooltip.always", true);

        MetabolismMod.CLIENT_SPEC = builder.build();
    }

    @Override
    public boolean debugShowOverlay() {
        return debugShowOverlay.get();
    }

    @Override
    public Anchor debugOverlayAnchor() {
        return debugOverlayAnchor.get();
    }

    @Override
    public float debugOverlayTextScale() {
        return debugOverlayTextScale.get().floatValue();
    }

    @Override
    public boolean metabolismOverlayShow() {
        return metabolismOverlayShow.get();
    }

    @Override
    public Anchor metabolismOverlayAnchor() {
        return metabolismOverlayAnchor.get();
    }

    @Override
    public int metabolismOverlayOffsetX() {
        return metabolismOverlayOffsetX.get();
    }

    @Override
    public int metabolismOverlayOffsetY() {
        return metabolismOverlayOffsetY.get();
    }

    @Override
    public float metabolismOverlayTextScale() {
        return metabolismTextScale.get().floatValue();
    }

    @Override
    public boolean showToolTip() {
        return showToolTip.get();
    }

    @Override
    public boolean alwaysShowToolTip() {
        return alwaysShowToolTip.get();
    }

    @Override
    public void reload() {

    }
}
