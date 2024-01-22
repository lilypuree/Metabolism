package lilypuree.metabolism.registration;

import lilypuree.metabolism.core.MetabolismEffect;
import lilypuree.metabolism.core.environment.AdvancedLocationCheck;
import lilypuree.metabolism.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.function.Supplier;

public class Registration {
    
    public static Supplier<LootItemConditionType> ADVANCED_LOCATION_CHECK = Services.PLATFORM
            .register(BuiltInRegistries.LOOT_CONDITION_TYPE, "advanced_location_check", ()->new LootItemConditionType(new AdvancedLocationCheck.Serializer()));
    public static Supplier<MobEffect> METABOLISM_EFFECT = Services.PLATFORM
            .register(BuiltInRegistries.MOB_EFFECT, "metabolism", MetabolismEffect::new);
    
    public static void init(){
        
    }
    
}
