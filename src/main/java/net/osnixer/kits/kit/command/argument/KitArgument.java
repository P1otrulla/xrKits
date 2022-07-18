package net.osnixer.kits.kit.command.argument;

import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import net.osnixer.kits.config.implementation.MessagesConfig;
import net.osnixer.kits.kit.Kit;
import net.osnixer.kits.kit.KitRepository;
import panda.std.Result;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class KitArgument implements OneArgument<Kit> {

    private final KitRepository repository;
    private final MessagesConfig messages;

    public KitArgument(KitRepository repository, MessagesConfig messages) {
        this.repository = repository;
        this.messages = messages;
    }

    @Override
    public Result<Kit, ?> parse(LiteInvocation liteInvocation, String argument) {
        Optional<Kit> kitOptional = this.repository.getKit(argument);

        if (!kitOptional.isPresent()) {
            return Result.error(this.messages.argument.kitNoExists);
        }

        return Result.ok(kitOptional.get());
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.repository.getKits()
                .stream()
                .map(Kit::getName)
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }
}
