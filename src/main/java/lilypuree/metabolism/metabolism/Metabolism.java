package lilypuree.metabolism.metabolism;

import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.data.Environment;
import lilypuree.metabolism.data.EnvironmentEffect;
import lilypuree.metabolism.data.Metabolite;
import lilypuree.metabolism.network.ClientSyncMessage;
import lilypuree.metabolism.network.Network;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.network.NetworkDirection;

public class Metabolism {
    private static final double LN_10 = Math.log(10);

    //VARIABLES
    private float maxWarmth;
    private float maxFood;
    private float warmth;
    private float heat;
    private float energy;
    private float food;

    /**
     * the amount of time it takes to reach approximately 90% of the given heat target, in ticks
     * changing this will vary how fast effects will cause heat changes to the player
     */
    private int adaptationTicks;
    private float heatCoefficent;

    //SYNCING
    private float lastSentWarmth;
    private float lastSentHeat;
    private float lastSentEnergy;
    private float lastSentFood;

    //TICKING
    private int warmthTick = 0;
    private int damageTick = 0;

    public Metabolism() {
        this.maxWarmth = MetabolismConstants.MAX_WARMTH;
        this.maxFood = MetabolismConstants.MAX_CAPACITY;
        this.warmth = maxWarmth;
        this.heat = 0.0F;
        this.food = MetabolismConstants.START_FOOD;
        this.energy = MetabolismConstants.START_ENERGY;
        this.setAdaptationTicks(MetabolismConstants.ADAPTATION_TICKS);
    }

    public void tick(Player player) {
        if (player.level().isClientSide) return;
        warmthTick++;
        damageTick++;

        if (warmthTick >= MetabolismConstants.WARMTH_TICK_COUNT) {
            EnvironmentEffect.Combined effect = Environment.get().getCurrentEffect((ServerLevel) player.level(), player);
            applyHeatTarget(effect.getCombinedHeatTarget());
            warm(effect.getCombinedWarmthEffect());

            boolean regen = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
            if (regen && player.isHurt() && warmth > 0) {
                player.heal(1.0F);
                warmth = Math.max(0.0F, warmth - 1.0F);
            }
            warmthTick = 0;
        }

        if (damageTick >= MetabolismConstants.DAMAGE_TICK_COUNT) {
            if (canBeHurt(player))
                causeDamage(player);
            damageTick = 0;
        }

        boolean changed = warmth != lastSentWarmth || heat != lastSentHeat || food != lastSentFood || energy != lastSentEnergy;
        if (changed && player instanceof ServerPlayer sPlayer) {
            ClientSyncMessage msg = new ClientSyncMessage(heat, warmth, food, energy);
            Network.channel.sendTo(msg, sPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            lastSentWarmth = warmth;
            lastSentHeat = heat;
            lastSentFood = food;
            lastSentEnergy = energy;
        }
    }

    private void causeDamage(Player player) {
        if (heat > 0) {
            if (energy > 0)
                energy = Math.max(0.0F, energy - calculateDrain());
            else
                player.hurt(player.damageSources().starve(), 1.0F);

        } else if (heat < 0) {
            if (food > 0)
                food = Math.max(0.0F, food - calculateDrain());
            else
                player.hurt(player.damageSources().starve(), 1.0F);
        }

        if (heat == maxWarmth) {
            player.hurt(player.damageSources().inFire(), 1.0F);
        } else if (heat == -maxWarmth) {
            player.hurt(player.damageSources().freeze(), 1.0F);
        }
    }

    private void applyHeatTarget(float heatTarget) {
        if (Math.abs(heat - heatTarget) < 1E-2) {
            heat = heatTarget;
        } else if (heat > 0) {
            //HOT
            heat = Mth.clamp(heat + heatChange(heatTarget, energy > 0), 0.0F, maxWarmth);
        } else if (heat < 0) {
            //COLD
            heat = Mth.clamp(heat + heatChange(heatTarget, food > 0), -maxWarmth, 0.0F);
        } else if (heatTarget != 0) {
            //MILD
            heat = Mth.clamp(heatChange(heatTarget, false), -maxWarmth, maxWarmth);
        }
    }

    private float heatChange(float heatTarget, boolean feedback) {
        float diff = feedback ? heatTarget - heat : heatTarget;
        if (Config.SERVER.preciseFeedback()) {
            return diff * heatCoefficent + diff * heatCoefficent * heatCoefficent / 2;
        } else {
            return diff * heatCoefficent;
        }
    }


    public void consumeFood(float amount) {
        float consumed = Math.min(amount, food);

        this.food -= consumed;
        this.energy += consumed;
    }

    public void consumeEnergy(float amount) {
        float consumed = Math.min(amount, energy);

        this.energy -= consumed;
//        this.warm(consumed * MetabolismConstants.ENERGY_WARMTH_CONVERSION_RATE);
    }

    public void eat(Metabolite metabolite) {
        this.warm(metabolite.warmth());
        this.food += metabolite.food();
        this.energy += metabolite.energy();
    }

    public void eat(float foodLevel, float saturationLevelModifier) {
        this.food += foodLevel * MetabolismConstants.OTHER_FOOD_MULTIPLIER;
    }

    public void peacefulWarmth() {
        if (heat > 0)
            this.heat = Math.max(heat - 1, 0);
        else if (heat < 0)
            this.heat = Math.min(heat + 1, 0);

        this.warm(1.0F);
    }

    public void warm(float amount) {
        warmth = Mth.clamp(warmth + amount, 0.0F, maxWarmth - Mth.abs(heat));
    }

    public float getWarmth() {
        return warmth;
    }

    public float getHeat() {
        return heat;
    }

    public float getEnergy() {
        return energy;
    }

    public float getFood() {
        return food;
    }

    private float calculateDrain() {
        return Mth.abs(heat) * MetabolismConstants.DRAIN_COEFFICIENT;
    }

    private void setAdaptationTicks(int ticks) {
        this.adaptationTicks = ticks;
        this.heatCoefficent = (float) (LN_10 * MetabolismConstants.WARMTH_TICK_COUNT / adaptationTicks);
    }


    private boolean canBeHurt(Player player) {
        Difficulty difficulty = player.level().getDifficulty();
        return player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL;
    }

    public boolean needsFood() {
        return food < maxFood;
    }

    public CompoundTag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("maxWarmth", maxWarmth);
        nbt.putFloat("maxFood", maxFood);
        nbt.putFloat("warmth", warmth);
        nbt.putFloat("heat", heat);
        nbt.putFloat("energy", energy);
        nbt.putFloat("food", food);
        return nbt;
    }

    public void readNBT(CompoundTag nbt) {
        maxWarmth = nbt.getFloat("maxWarmth");
        maxFood = nbt.getFloat("maxFood");
        warmth = nbt.getFloat("warmth");
        heat = nbt.getFloat("heat");
        energy = nbt.getFloat("energy");
        food = nbt.getFloat("food");
    }

    public void syncOnClient(ClientSyncMessage msg) {
        heat = msg.heat;
        warmth = msg.warmth;
        energy = msg.energy;
        food = msg.food;
    }


    public float getMaxWarmth() {
        return maxWarmth;
    }
}
