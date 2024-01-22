package lilypuree.metabolism.platform.services;

import com.google.gson.JsonElement;
import lilypuree.metabolism.network.IMessage;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;
import java.util.function.Supplier;

public interface IPlatformHelper {

    boolean isModLoaded(String modid);
    
    boolean isPhysicalClient();

    void sendToClient(IMessage msg, ResourceLocation channel, ServerPlayer player);

    FoodProperties getFoodProperties(ItemStack stack, LivingEntity entity);

    Optional<LootItemCondition> deserializeLootCondition(ResourceLocation location, JsonElement json, ResourceManager resourceManager);

    GameRules.Key<GameRules.BooleanValue> registerGameRule(String name, GameRules.Category category, boolean defaultValue);

    <I> Supplier<I> register(Registry<I> registry, String name, Supplier<? extends I> sup);
}
