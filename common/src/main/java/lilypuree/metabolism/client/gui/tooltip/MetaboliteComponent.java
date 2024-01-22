package lilypuree.metabolism.client.gui.tooltip;

import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;

public class MetaboliteComponent extends EmptyComponent implements FormattedCharSequence {

    MetaboliteToolTip tooltip;

    public MetaboliteComponent(MetaboliteToolTip tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public FormattedCharSequence getVisualOrderText() {
        return this;
    }

    @Override
    public boolean accept(FormattedCharSink sink) {
        return StringDecomposer.iterateFormatted(this, getStyle(), sink);
    }

    public MetaboliteToolTip getTooltipComponent() {
        return tooltip;
    }
}
