package lilypuree.metabolism.platform;

import com.google.gson.JsonElement;
import lilypuree.metabolism.Constants;
import lilypuree.metabolism.network.IMessage;
import lilypuree.metabolism.network.Network;
import lilypuree.metabolism.platform.services.IPlatformHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    public static Map<Registry<?>, DeferredRegister<?>> registries = new HashMap<>();

    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public void sendToClient(IMessage msg, ResourceLocation channel, ServerPlayer player) {
        Network.channel.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    public FoodProperties getFoodProperties(ItemStack stack, LivingEntity entity) {
        return stack.getFoodProperties(entity);
    }

    @Override
    public Optional<LootItemCondition> deserializeLootCondition(ResourceLocation location, JsonElement json, ResourceManager resourceManager) {
        return LootDataType.PREDICATE.deserialize(location, json, resourceManager);
    }

    @Override
    public GameRules.Key<GameRules.BooleanValue> registerGameRule(String name, GameRules.Category category, boolean defaultValue) {
        return GameRules.register(name, category, GameRules.BooleanValue.create(defaultValue));
    }

    @Override
    public <I> Supplier<I> register(Registry<I> registry, String name, Supplier<? extends I> sup) {
        DeferredRegister<I> deferredRegister = (DeferredRegister<I>) registries.computeIfAbsent(registry, reg -> DeferredRegister.create(reg.key(), Constants.MOD_ID));
        return deferredRegister.register(name, sup);
    }
}
