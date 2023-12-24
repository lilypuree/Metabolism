package lilypuree.metabolism.util;

import lilypuree.metabolism.MetabolismMod;

import java.util.Arrays;
import java.util.Optional;

public enum WeatherType {
    SUNNY("sunny"), RAIN("rain"), SNOW("snow"), HEATWAVE("heatwave"), BLIZZARD("blizzard");

    private String name;

    WeatherType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static WeatherType fromString(String name) {
        if (name != null) {
            Optional<WeatherType> weatherType = Arrays.stream(values()).filter(type -> type.name.equals(name)).findAny();
            if (weatherType.isEmpty()) {
                MetabolismMod.LOGGER.warn("invalid weather type " + name);
                return null;
            } else
                return weatherType.get();
        }
        return null;
    }
}
