package lilypuree.metabolism.config;

import lilypuree.metabolism.util.Anchor;

public interface MetabolismClientConfig {

    // DEBUG

    boolean debugShowOverlay();

    Anchor debugOverlayAnchor();

    float debugOverlayTextScale();

    // ENERGY BAR

    boolean energyBarShow();

    Anchor energyBarAnchor();

    int energyBarOffsetX();

    int energyBarOffsetY();

    float energyBarTextScale();

    // TOOLTIPS

    boolean showToolTip();

    boolean alwaysShowToolTip();


    void reload();
}
