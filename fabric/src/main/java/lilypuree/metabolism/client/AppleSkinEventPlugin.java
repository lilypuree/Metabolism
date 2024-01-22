package lilypuree.metabolism.client;

import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.HUDOverlayEvent;
import squeek.appleskin.api.event.TooltipOverlayEvent;

public class AppleSkinEventPlugin implements AppleSkinApi {
    @Override
    public void registerEvents() {
        HUDOverlayEvent.Exhaustion.EVENT.register(e -> e.isCanceled = true);
        HUDOverlayEvent.HealthRestored.EVENT.register(e -> e.isCanceled = true);
        HUDOverlayEvent.HungerRestored.EVENT.register(e -> e.isCanceled = true);
        HUDOverlayEvent.Saturation.EVENT.register(e -> e.isCanceled = true);
        TooltipOverlayEvent.Pre.EVENT.register(e ->e.isCanceled = true);
    }
}
