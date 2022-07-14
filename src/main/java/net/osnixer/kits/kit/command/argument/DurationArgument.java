package net.osnixer.kits.kit.command.argument;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import net.osnixer.kits.config.implementation.MessagesConfig;
import panda.std.Result;

import java.time.Duration;
import java.util.Locale;

public class DurationArgument implements OneArgument<Duration> {

    private final MessagesConfig messages;

    public DurationArgument(MessagesConfig messages) {
        this.messages = messages;
    }

    @Override
    public Result<Duration, ?> parse(LiteInvocation invocation, String argument) {
        return Result.attempt(Exception.class, () -> Duration.parse("PT" + argument.toUpperCase(Locale.ROOT)))
                .mapErr(e -> this.messages.argument.wrongTimeFormat);
    }

}
