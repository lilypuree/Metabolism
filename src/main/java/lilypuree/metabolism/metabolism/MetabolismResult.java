package lilypuree.metabolism.metabolism;

public enum MetabolismResult {
    NONE(false, false),
    WARMING(true, true),
    HEATING(true, true),
    COOLING(true, true),
    FOOD(false, true),
    HYDRATION(true, false);

    final boolean consumeFood;
    final boolean consumeHydration;

    MetabolismResult(boolean consumeFood, boolean consumeHydration) {
        this.consumeFood = consumeFood;
        this.consumeHydration = consumeHydration;
    }
}
