package lilypuree.metabolism.compat;

import lilypuree.metabolism.data.Metabolites;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import squeek.appleskin.api.event.HUDOverlayEvent;
import squeek.appleskin.api.event.TooltipOverlayEvent;

public class AppleSkinEventHandler {
    @SubscribeEvent
    public void onPreTooltipEvent(TooltipOverlayEvent.Pre event) {
        if (Metabolites.getMetabolites().containsKey(event.itemStack.getItem())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onHudOverlayEvent(HUDOverlayEvent event) {
        event.setCanceled(true);
    }
    
    
}
