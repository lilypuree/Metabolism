package lilypuree.metabolism.client;


import lilypuree.metabolism.metabolism.Metabolism;
import lilypuree.metabolism.network.ClientSyncMessage;
import lilypuree.metabolism.network.ProgressSyncMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Handles information synced from the server. Only information actually needed by the client should
 * be synced. This information will likely not be updated every tick.
 */
public class ClientHandler {
    private ClientHandler() {
    }

    public static void handleSyncMessage(ClientSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        Metabolism metabolism = getClientMetabolism(Minecraft.getInstance());
        metabolism.syncOnClient(msg);
    }

    public static void handleSyncProgress(ProgressSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        Metabolism metabolism = getClientMetabolism(Minecraft.getInstance());
        metabolism.syncProgress(msg);
    }

    public static Metabolism getClientMetabolism(Minecraft mc) {
        return Metabolism.get(mc.player);
    }
}
