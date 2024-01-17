package lilypuree.metabolism.util;


public enum Anchor {
    TOP_LEFT(Anchor.Horizontal.LEFT, Anchor.Vertical.TOP),
    TOP_CENTER(Anchor.Horizontal.CENTER, Anchor.Vertical.TOP),
    TOP_RIGHT(Anchor.Horizontal.RIGHT, Anchor.Vertical.TOP),
    CENTER_LEFT(Anchor.Horizontal.LEFT, Anchor.Vertical.CENTER),
    CENTER(Anchor.Horizontal.CENTER, Anchor.Vertical.CENTER),
    CENTER_RIGHT(Anchor.Horizontal.RIGHT, Anchor.Vertical.CENTER),
    BOTTOM_LEFT(Anchor.Horizontal.LEFT, Anchor.Vertical.BOTTOM),
    BOTTOM_CENTER(Anchor.Horizontal.CENTER, Anchor.Vertical.BOTTOM),
    BOTTOM_RIGHT(Anchor.Horizontal.RIGHT, Anchor.Vertical.BOTTOM);

    private final Horizontal horizontal;
    private final Vertical vertical;

    Anchor(Horizontal horizontal, Vertical vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public int getX(int scaledScreenWidth, int elementWidth, int margin) {
        return this.horizontal.getX(scaledScreenWidth, elementWidth, margin);
    }

    public int getY(int scaledScreenHeight, int elementHeight, int margin) {
        return this.vertical.getY(scaledScreenHeight, elementHeight, margin);
    }

    public Horizontal getHorizontal() {
        return this.horizontal;
    }

    public Vertical getVertical() {
        return this.vertical;
    }

    public enum Vertical {
        TOP {
            public int getY(int scaledScreenHeight, int elementHeight, int margin) {
                return margin;
            }
        },
        CENTER {
            public int getY(int scaledScreenHeight, int elementHeight, int margin) {
                return (scaledScreenHeight - elementHeight) / 2;
            }
        },
        BOTTOM {
            public int getY(int scaledScreenHeight, int elementHeight, int margin) {
                return scaledScreenHeight - elementHeight - margin;
            }
        };

        Vertical() {
        }

        public abstract int getY(int var1, int var2, int var3);
    }

    public enum Horizontal {
        LEFT {
            public int getX(int scaledScreenWidth, int elementWidth, int margin) {
                return margin;
            }
        },
        CENTER {
            public int getX(int scaledScreenWidth, int elementWidth, int margin) {
                return (scaledScreenWidth - elementWidth) / 2;
            }
        },
        RIGHT {
            public int getX(int scaledScreenWidth, int elementWidth, int margin) {
                return scaledScreenWidth - elementWidth - margin;
            }
        };

        Horizontal() {
        }

        public abstract int getX(int var1, int var2, int var3);
    }
}
