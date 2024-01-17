package lilypuree.metabolism.registration;

import lilypuree.metabolism.Constants;
import lilypuree.metabolism.platform.Services;
import net.minecraft.world.level.GameRules;

public class MetabolismGameRules {
    public static void init() {
    }

    public static GameRules.Key<GameRules.BooleanValue> RULE_DO_TEMPERATURE = Services.PLATFORM.registerGameRule(Constants.MOD_ID + ":doTemperature", GameRules.Category.PLAYER, true);

}
