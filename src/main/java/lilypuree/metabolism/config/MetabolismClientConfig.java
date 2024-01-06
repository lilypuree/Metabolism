package lilypuree.metabolism.config;

import lilypuree.metabolism.util.Anchor;

public interface MetabolismClientConfig {

    // DEBUG

    boolean debugShowOverlay();

    Anchor debugOverlayAnchor();

    float debugOverlayTextScale();

    // ENERGY BAR

    boolean metabolismOverlayShow();

    Anchor metabolismOverlayAnchor();

    int metabolismOverlayOffsetX();

    int metabolismOverlayOffsetY();

    float metabolismOverlayTextScale();

    // TOOLTIPS

    boolean showToolTip();

    boolean alwaysShowToolTip();


    void reload();
}
