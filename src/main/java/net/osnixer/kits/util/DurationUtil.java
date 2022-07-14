package net.osnixer.kits.util;

import java.time.Duration;

public final class DurationUtil {

    public static String format(Duration duration) {
        if (duration.isNegative()) {
            return "0s";
        }

        return Duration.ofSeconds(duration.getSeconds()).toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    private DurationUtil() {

    }
}
