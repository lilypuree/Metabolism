package lilypuree.metabolism;

import net.minecraft.world.level.GameRules;

public class MetabolismGameRules {
    public static void init() {
    }
    public static GameRules.Key<GameRules.BooleanValue> RULE_DO_TEMPERATURE = registerGameRule(MetabolismMod.MOD_ID + ":doTemperature", GameRules.Category.PLAYER, true);

    private static GameRules.Key<GameRules.BooleanValue> registerGameRule(String name, GameRules.Category category, boolean defaultValue) {
        return GameRules.register(name, category, GameRules.BooleanValue.create(defaultValue));
    }
}
