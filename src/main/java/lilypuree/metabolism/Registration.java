package lilypuree.metabolism;

import lilypuree.metabolism.util.AdvancedWeatherCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class Registration {

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, MetabolismMod.MOD_ID);

    public static final RegistryObject<LootItemConditionType> ADVANCED_WEATHER_CHECK = registerLootCondition("advanced_weather_check", new AdvancedWeatherCheck.Serializer());

    private static RegistryObject<LootItemConditionType> registerLootCondition(String name, Serializer<? extends LootItemCondition> serializer) {
        return LOOT_CONDITIONS.register(name, () -> new LootItemConditionType(serializer));
    }
}
