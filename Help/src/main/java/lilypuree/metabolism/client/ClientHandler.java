package lilypuree.metabolism.client;


import lilypuree.metabolism.core.FoodDataDuck;
import lilypuree.metabolism.core.Metabolism;
import lilypuree.metabolism.core.MetabolismResult;
import lilypuree.metabolism.network.ClientSyncMessage;
import lilypuree.metabolism.network.ProgressSyncMessage;
import lilypuree.metabolism.network.ResultSyncMessage;
import net.minecraft.client.Minecraft;

/**
 * Handles information synced from the server. Only information actually needed by the client should
 * be synced. This information will likely not be updated every tick.
 */
public class ClientHandler {
    private ClientHandler() {
    }

    public static float progress = -1;
    public static MetabolismResult result = MetabolismResult.NONE;

    public static void handleSyncMessage(ClientSyncMessage msg) {
        Metabolism metabolism = getClientMetabolism(Minecraft.getInstance());
        metabolism.syncOnClient(msg);
    }

    public static void handleSyncProgress(ProgressSyncMessage msg) {
        progress = msg.progress();
    }

    public static void handleSyncResult(ResultSyncMessage msg) {
        result = msg.result();
    }


    public static Metabolism getClientMetabolism(Minecraft mc) {
        return ((FoodDataDuck) mc.player.getFoodData()).getMetabolism();       //Cannot use Metabolism.get() due to crash on dedicated server
    }
}
