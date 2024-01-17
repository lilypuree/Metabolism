package lilypuree.metabolism.platform;

import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.platform.services.MetabolismServerConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeServerConfig implements MetabolismServerConfig {
    public final ForgeConfigSpec.BooleanValue preciseFeedback;

    public ForgeServerConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        preciseFeedback = builder.comment("enable more precise heat feedback").define("result.heat.preciseFeedback", false);

        MetabolismMod.SERVER_SPEC = builder.build();
    }

    @Override
    public boolean preciseFeedback() {
        return preciseFeedback.get();
    }

    @Override
    public void reload() {

    }
}
