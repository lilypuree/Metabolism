package lilypuree.metabolism.client;

import lilypuree.metabolism.client.gui.tooltip.MetaboliteComponent;
import me.shedaniel.rei.api.client.entry.renderer.EntryRendererRegistry;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;

import java.util.List;

public class REITooltipPlugin implements REIClientPlugin {

    //Does what Appleskin does to its tooltips, as the handling is same.
    //Replaces MetaboliteComponent text to a custom TooltipComponent
    @Override
    public void registerEntryRenderers(EntryRendererRegistry registry) {
        registry.transformTooltip(VanillaEntryTypes.ITEM, (itemstack, mouse, tooltip) -> {
            if (tooltip == null)
                return null;
            tooltip.entries().replaceAll(entry -> {
                if (entry.isText() && entry.getAsText() instanceof MetaboliteComponent metaboliteComponent){
                    return Tooltip.entry(metaboliteComponent.getTooltipComponent());
                }
                return entry;
            });
            return tooltip;
        });
    }
}
