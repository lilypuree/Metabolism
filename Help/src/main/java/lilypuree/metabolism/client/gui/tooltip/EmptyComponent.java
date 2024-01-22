package lilypuree.metabolism.client.gui.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;

abstract class EmptyComponent implements Component {

    static List<Component> emptySiblings = new ArrayList<>();

    @Override
    public Style getStyle() {
        return Style.EMPTY;
    }

    @Override
    public ComponentContents getContents() {
        return ComponentContents.EMPTY;
    }

    @Override
    public List<Component> getSiblings() {
        return emptySiblings;
    }
}
