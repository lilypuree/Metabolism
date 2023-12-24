package lilypuree.metabolism.config;

import lilypuree.metabolism.util.Anchor;

public interface MetabolismClientConfig {
    boolean debugShowOverlay();

    Anchor debugOverlayAnchor();

    float debugOverlayTextScale();

    boolean energyBarShow();

    Anchor energyBarAnchor();

    int energyBarOffsetX();

    int energyBarOffsetY();

    float energyBarTextScale();

    void reload();
}
