package lilypuree.metabolism.platform;

import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.platform.services.MetabolismServerConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeServerConfig implements MetabolismServerConfig {
    public final ForgeConfigSpec.BooleanValue preciseFeedback;
    public final ForgeConfigSpec.BooleanValue disableHeat;
    public final ForgeConfigSpec.BooleanValue convertResources;

    public ForgeServerConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        preciseFeedback = builder.comment("enable more precise heat feedback").define("heat.preciseFeedback", false);
        disableHeat = builder.comment("set the heat target to 0").define("heat.disable", false);
        convertResources = builder.comment("enable food/hydration conversion").define("metabolization.convertResources", true);

        MetabolismMod.SERVER_SPEC = builder.build();
    }

    @Override
    public boolean preciseFeedback() {
        return preciseFeedback.get();
    }
    
    @Override
    public boolean disableHeat() {
        return disableHeat.get();
    }

    @Override
    public boolean convertResources() {
        return convertResources.get();
    }

    @Override
    public void reload() {

    }
}
