package lilypuree.metabolism.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lilypuree.metabolism.core.Metabolism;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class MetabolismCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("metabolism").requires(source -> source.hasPermission(2));

        for (ResourceType type : ResourceType.values()) {
            builder.then(Commands.literal(type.name)
                    .then(Commands.literal("get")
                            .then(Commands.argument("targets", EntityArgument.players())
                                    .executes(ctx -> runGet(ctx, type))
                            ).executes(ctx -> getSingle(ctx, ctx.getSource().getPlayer(), type))
                    ).then(Commands.literal("set")
                            .then(Commands.argument("targets", EntityArgument.players())
                                    .then(Commands.argument("amount", IntegerArgumentType.integer())
                                            .executes(ctx -> runSet(ctx, type))
                                    )
                            )
                    ).then(Commands.literal("add")
                            .then(Commands.argument("targets", EntityArgument.players())
                                    .then(Commands.argument("amount", IntegerArgumentType.integer())
                                            .executes(ctx -> runAdd(ctx, type))
                                    )
                            )
                    )
            );
        }
        dispatcher.register(builder);
    }

    private static int runGet(CommandContext<CommandSourceStack> ctx, ResourceType type) throws CommandSyntaxException {
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "targets")) {
            getSingle(ctx, player, type);
        }
        return 1;
    }

    private static int getSingle(CommandContext<CommandSourceStack> ctx, Player player, ResourceType type) {
        Metabolism metabolism = Metabolism.get(player);
        ctx.getSource().sendSuccess(() -> playerNameText(player), true);
        MutableComponent resource = text(type.name, type.getter.apply(metabolism)).withStyle(ChatFormatting.YELLOW);
        ctx.getSource().sendSuccess(() -> resource, true);
        return 1;
    }

    private static int runSet(CommandContext<CommandSourceStack> ctx, ResourceType type) throws CommandSyntaxException {
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "targets")) {
            type.setter.accept(Metabolism.get(player), (float) amount);
        }
        return 1;
    }

    private static int runAdd(CommandContext<CommandSourceStack> ctx, ResourceType type) throws CommandSyntaxException {
        int amount = IntegerArgumentType.getInteger(ctx, "amount");
        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "targets")) {
            Metabolism metabolism = Metabolism.get(player);
            float existing = type.getter.apply(metabolism);
            type.setter.accept(metabolism, existing + amount);
        }
        return 1;
    }

    public static enum ResourceType {
        WARMTH("warmth", Metabolism::getWarmth, Metabolism::setWarmth),
        HEAT("heat", Metabolism::getHeat, Metabolism::setHeat),
        FOOD("food", Metabolism::getFood, Metabolism::setFood),
        HYDRATION("hydration", Metabolism::getHydration, Metabolism::setHydration);

        String name;
        Function<Metabolism, Float> getter;
        BiConsumer<Metabolism, Float> setter;

        ResourceType(String name, Function<Metabolism, Float> getter, BiConsumer<Metabolism, Float> setter) {
            this.name = name;
            this.getter = getter;
            this.setter = setter;
        }
    }

    private static MutableComponent text(String key, Object... args) {
        return Component.translatable("command.metabolism." + key, args);
    }

    private static MutableComponent playerNameText(Player player) {
        return Component.translatable("command.metabolism.playerName",
                player.getName().copy().withStyle(ChatFormatting.ITALIC)).withStyle(ChatFormatting.AQUA);
    }

    private static MutableComponent valueText(double value, double maxValue) {
        return Component.translatable("command.metabolism.valueOverMax",
                String.format("%.1f", value),
                String.format("%.1f", maxValue)
        ).copy().withStyle(ChatFormatting.WHITE);
    }
}
