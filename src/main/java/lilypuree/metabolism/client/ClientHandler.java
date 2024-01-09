package lilypuree.metabolism.client;


import lilypuree.metabolism.metabolism.FoodDataDuck;
import lilypuree.metabolism.metabolism.Metabolism;
import lilypuree.metabolism.metabolism.MetabolismResult;
import lilypuree.metabolism.network.ClientSyncMessage;
import lilypuree.metabolism.network.ProgressSyncMessage;
import lilypuree.metabolism.network.ResultSyncMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Handles information synced from the server. Only information actually needed by the client should
 * be synced. This information will likely not be updated every tick.
 */
public class ClientHandler {
    private ClientHandler() {
    }

    public static float progress = -1;
    public static MetabolismResult result = MetabolismResult.NONE;

    public static void handleSyncMessage(ClientSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        Metabolism metabolism = getClientMetabolism(Minecraft.getInstance());
        metabolism.syncOnClient(msg);
    }

    public static void handleSyncProgress(ProgressSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        progress = msg.progress();
    }

    public static void handleSyncResult(ResultSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
        result = msg.result();
    }


    public static Metabolism getClientMetabolism(Minecraft mc) {
        return ((FoodDataDuck) mc.player.getFoodData()).getMetabolism();       //Cannot use Metabolism.get() due to crash on dedicated server
    }
}
