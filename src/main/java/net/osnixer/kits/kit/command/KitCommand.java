package net.osnixer.kits.kit.command;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import net.osnixer.kits.kit.KitInventory;
import org.bukkit.entity.Player;

@Section(route = "kit", aliases = { "kity", "kits" })
public class KitCommand {

    private final KitInventory kitInventory;

    public KitCommand(KitInventory kitInventory) {
        this.kitInventory = kitInventory;
    }

    @Execute(required = 0)
    public void execute(Player player) {
        this.kitInventory.openInventory(player);
    }

}
