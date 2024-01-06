package lilypuree.metabolism.command;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;

public class ModCommands {

    public static void registerAll(RegisterCommandsEvent event) {
        MetabolismCommand.register(event.getDispatcher(), event.getBuildContext());
    }

    static MutableComponent playerNameText(Player player) {
        return Component.translatable("command.metabolism.playerName",
                player.getName().copy().withStyle(ChatFormatting.ITALIC)).withStyle(ChatFormatting.AQUA);
    }

    static MutableComponent valueText(double value, double maxValue) {
        return Component.translatable("command.metabolism.valueOverMax",
                String.format("%.1f", value),
                String.format("%.1f", maxValue)
        ).copy().withStyle(ChatFormatting.WHITE);
    }
}
