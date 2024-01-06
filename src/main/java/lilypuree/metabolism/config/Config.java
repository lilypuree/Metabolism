package lilypuree.metabolism.config;

import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.util.Anchor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = MetabolismMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    public static ForgeConfigSpec CLIENT_SPEC;
    public static ForgeConfigSpec SERVER_SPEC;
    public static MetabolismClientConfig CLIENT;
    public static MetabolismServerConfig SERVER;

    static {
        Pair<ClientConfig, ForgeConfigSpec> pCli = new Builder().configure(ClientConfig::new);
        Pair<ServerConfig, ForgeConfigSpec> pCom = new Builder().configure(ServerConfig::new);
        CLIENT_SPEC = pCli.getRight();
        CLIENT = pCli.getLeft();

        SERVER_SPEC = pCom.getRight();
        SERVER = pCom.getLeft();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    }

    private static class ServerConfig implements MetabolismServerConfig {
        public final BooleanValue preciseFeedback;

        public ServerConfig(Builder builder) {
            preciseFeedback = builder.comment("enable more precise heat feedback").define("progress.heat.preciseFeedback", false);
        }

        @Override
        public boolean preciseFeedback() {
            return preciseFeedback.get();
        }

        @Override
        public void reload() {

        }
    }

    private static class ClientConfig implements MetabolismClientConfig {
        public final BooleanValue debugShowOverlay;
        public final EnumValue<Anchor> debugOverlayAnchor;
        public final DoubleValue debugOverlayTextScale;

        public final BooleanValue metabolismOverlayShow;
        public final EnumValue<Anchor> metabolismOverlayAnchor;
        public final IntValue metabolismOverlayOffsetX;
        public final IntValue metabolismOverlayOffsetY;
        public final DoubleValue metabolismTextScale;

        public final BooleanValue showToolTip;
        public final BooleanValue alwaysShowToolTip;


        public ClientConfig(Builder builder) {
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
}
