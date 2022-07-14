package net.osnixer.kits.kit.command;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.section.Section;
import net.osnixer.kits.config.ConfigManager;
import net.osnixer.kits.config.implementation.MessagesConfig;

@Section(route = "kitreload")
@Permission("xrKits.reload")
public class KitReloadCommand {

    private final ConfigManager configManager;
    private final MessagesConfig messages;

    public KitReloadCommand(ConfigManager configManager, MessagesConfig messages) {
        this.configManager = configManager;
        this.messages = messages;
    }

    @Execute
    String execute() {
        this.configManager.reload();

        return this.messages.kitReloaded;
    }

}
