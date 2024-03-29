package lilypuree.metabolism.core;

public class MetabolismConstants {
    public static final int MAX_WARMTH = 20;
    public static final float MAX_FOOD = 99;
    public static final float START_HYDRATION = 19.0F;
    public static final float START_FOOD = 19.0F;
    public static final int BASE_TICK_COUNT = 10;
    public static final int ENVIRONMENT_CYCLES = 4;
    public static final int REGEN_CYCLES = 8;
    public static final int DAMAGE_CYCLES = 4;
    public static final int ADAPTATION_TICKS = 1000;
    public static final float DRAIN_COEFFICIENT = 0.04F;
    public static final float CONVERSION_RATIO = 0.5F;

    //how much warmth is generated per tick
    public static float metabolismSpeed(int amplifier) {
        return (amplifier + 2) / 80.0F;
    }

    public static final float ENERGY_SPRINT_JUMP = 0.1F;
    public static final float PROGRESS_JUMP = 0.01F;
    public static final float PROGRESS_DMG = 0.05F;
    public static final float PROGRESS_ATK = 0.05F;
    public static final float FOOD_MINE = 0.005F;
    public static final float PROGRESS_WALK = 0.001F;
    public static final float PROGRESS_CROUCH = 0.002F;
    public static final float PROGRESS_CLIMB = 0.002F;
    public static final float PROGRESS_SWIM = 0.025F;
    public static final float PROGRESS_SPRINT = 0.025F;
    public static final float ENERGY_FOOD_FLY = 0.01F;
    public static final float EXHAUSTION_MULTIPLIER = 1.0F;
    public static final float OTHER_FOOD_MULTIPLIER = 0.333F;

  
}
