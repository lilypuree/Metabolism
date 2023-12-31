package lilypuree.metabolism.client;

import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.metabolite.Metabolite;
import net.minecraft.world.item.Item;

import java.util.Map;

public class ClientMetabolites {
    private static Map<Item, Metabolite> metabolites;

    public static void setClientMetabolites(Map<Item, Metabolite> data) {
        metabolites = data;
        MetabolismMod.LOGGER.debug("Loaded MetabolismData on the client.");
    }

    public static Map<Item, Metabolite> getClientMetabolites() {
        return metabolites;
    }
}
