package lilypuree.metabolism.client;

import lilypuree.metabolism.Constants;
import lilypuree.metabolism.core.metabolite.Metabolite;
import lilypuree.metabolism.network.MetabolitesPacket;
import net.minecraft.world.item.Item;

import java.util.Map;

public class ClientMetabolites {
    private static Map<Item, Metabolite> metabolites;

    public static void setClientMetabolites(MetabolitesPacket packet) {
        metabolites = packet.metaboliteMap();
        Constants.LOG.debug("Loaded MetabolismData on the client.");
    }

    public static Map<Item, Metabolite> getClientMetabolites() {
        return metabolites;
    }
}
