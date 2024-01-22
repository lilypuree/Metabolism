package lilypuree.metabolism.mixin;

import lilypuree.metabolism.client.gui.tooltip.MetaboliteComponent;
import lilypuree.metabolism.client.gui.tooltip.MetaboliteToolTip;
import lilypuree.metabolism.client.gui.tooltip.MetaboliteToolTipRenderer;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientTooltipComponent.class)
public interface ClientTooltipComponentMixin {

    @Inject(at = @At("HEAD"),
            method = "create(Lnet/minecraft/util/FormattedCharSequence;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;",
            cancellable = true)
    private static void onCreateFromText(FormattedCharSequence text, CallbackInfoReturnable<ClientTooltipComponent> cir) {
        if (text instanceof MetaboliteComponent metaboliteComponent)
            cir.setReturnValue(new MetaboliteToolTipRenderer(metaboliteComponent.getTooltipComponent()));
    }

    //needed for REI compatibility
    @Inject(at = @At("HEAD"),
            method = "create(Lnet/minecraft/world/inventory/tooltip/TooltipComponent;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;",
            cancellable = true)
    private static void onCreateFromTooltipComponent(TooltipComponent component, CallbackInfoReturnable<ClientTooltipComponent> cir) {
        if (component instanceof MetaboliteToolTip metaboliteToolTip)
            cir.setReturnValue(new MetaboliteToolTipRenderer(metaboliteToolTip));
    }

}
