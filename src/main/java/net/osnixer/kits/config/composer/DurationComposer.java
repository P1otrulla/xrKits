package net.osnixer.kits.config.composer;

import net.dzikoysk.cdn.serdes.Composer;
import net.dzikoysk.cdn.serdes.SimpleDeserializer;
import net.dzikoysk.cdn.serdes.SimpleSerializer;
import panda.std.Result;

import java.time.Duration;
import java.util.Locale;

public class DurationComposer implements Composer<Duration>, SimpleSerializer<Duration>, SimpleDeserializer<Duration> {

    @Override
    public Result<Duration, Exception> deserialize(String duration) {
        return Result.attempt(Exception.class, () -> Duration.parse("PT" + duration.toUpperCase(Locale.ROOT)));
    }

    @Override
    public Result<String, Exception> serialize(Duration duration) {
        return Result.attempt(Exception.class, () -> duration.toString()
                .substring(2)
                .toLowerCase(Locale.ROOT)
        );
    }

}
