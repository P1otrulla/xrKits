package net.osnixer.kits.kit.command;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.suggestion.Suggest;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.osnixer.kits.config.implementation.MessagesConfig;
import net.osnixer.kits.kit.Kit;
import net.osnixer.kits.kit.KitEditInventory;
import net.osnixer.kits.kit.KitRepository;
import net.osnixer.kits.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Section(route = "kitedit")
@Permission("xrKits.edit")
public class KitEditCommand {

    private final KitEditInventory kitEditInventory;
    private final KitRepository kitRepository;
    private final MessagesConfig messages;

    public KitEditCommand(KitEditInventory kitEditInventory, KitRepository kitRepository, MessagesConfig messages) {
        this.kitEditInventory = kitEditInventory;
        this.kitRepository = kitRepository;
        this.messages = messages;
    }

    @Execute(route = "create")
    public String create(

            @Arg @Name("name") @Suggest("name")
            String name,

            @Arg @Name("permission") @Suggest("kit.name")
            String permission,

            @Arg @Name("time") @Suggest({"4h", "30m"})
            Duration duration,

            @Arg @Name("icon") @Suggest("DIAMOND_PICKAXE")
            Material icon,

            @Arg @Name("slot") @Suggest("8")
            int slot
    ) {
        Optional<Kit> kitOptional = this.kitRepository.getKit(name);

        if (kitOptional.isPresent()) {
            return this.messages.argument.kitAlreadyExists;
        }

        this.kitRepository.saveKit(name, name, duration, permission, new ArrayList<>(), new ArrayList<>(), new ItemStack(icon), slot);

        return this.messages.kitEdit.create
                .replace("{KIT}", name)
                .replace("{PERMISSION}", permission)
                .replace("{TIME}", duration.toString().split("PT")[1].toLowerCase(Locale.ROOT))
                .replace("{ICON}", icon.name())
                .replace("{SLOT}", String.valueOf(slot));
    }

    @Execute(route = "setIcon")
    public String setIcon(Player player, @Arg @Name("kit") Kit kit) {
        ItemStack hand = player.getInventory().getItemInHand();

        this.kitRepository.saveKit(kit.getName(), kit.getName(), kit.getDuration(), kit.getPermission(), kit.getItems(), kit.getCommands(), hand, kit.getGuiSlot());

        return this.messages.kitEdit.icon.replace("{ICON}", hand.getType().name());
    }

    @Execute(route = "setDuration")
    public String setDuration(@Arg @Name("kit") Kit kit, @Arg @Name("time") Duration duration) {
        this.kitRepository.saveKit(kit.getName(), kit.getName(), duration, kit.getPermission(), kit.getItems(), kit.getCommands(), kit.getGuiItem(), kit.getGuiSlot());

        return this.messages.kitEdit.duration.replace("{TIME}", duration.toString().split("PT")[1].toLowerCase(Locale.ROOT));
    }

    @Execute(route = "setPermission")
    public String setPermission(@Arg @Name("kit") Kit kit, @Arg @Name("permission") String permission) {
        this.kitRepository.saveKit(kit.getName(), kit.getName(), kit.getDuration(), permission, kit.getItems(), kit.getCommands(), kit.getGuiItem(), kit.getGuiSlot());

        return this.messages.kitEdit.permission.replace("{PERMISSION}", permission);
    }

    @Execute(route = "setCommands")
    public String setCommands(@Arg @Name("kit") Kit kit, @Joiner @Name("commands") String commands) {
        this.kitRepository.saveKit(kit.getName(), kit.getName(), kit.getDuration(), kit.getPermission(), kit.getItems(), Arrays.asList(commands.split(",")), kit.getGuiItem(), kit.getGuiSlot());

        return this.messages.kitEdit.commands.replace("{COMMANDS}", commands);
    }

    @Execute(route = "setGuiName")
    public String setGuiName(@Arg @Name("kit") Kit kit, @Joiner @Name("name") String name) {
        ItemBuilder icon = ItemBuilder.from(kit.getGuiItem());

        icon.name(ChatUtil.mini(name));

        this.kitRepository.saveKit(kit.getName(), name, kit.getDuration(), kit.getPermission(), kit.getItems(), kit.getCommands(), kit.getGuiItem(), kit.getGuiSlot());

        return this.messages.kitEdit.guiName.replace("{NAME}", name);
    }

    @Execute(route = "setLore")
    public String setLore(@Arg @Name("kit") Kit kit, @Joiner @Name("lore") String lore) {
        ItemBuilder icon = ItemBuilder.from(kit.getGuiItem());

        icon.lore(ChatUtil.mini(Arrays.stream(lore.split("\\{NL}")).collect(Collectors.toList())));

        this.kitRepository.saveKit(kit.getName(), kit.getName(), kit.getDuration(), kit.getPermission(), kit.getItems(), kit.getCommands(), icon.build(), kit.getGuiSlot());

        return this.messages.kitEdit.lore.replace("{LORE}", lore);
    }

    @Execute(route = "setItems")
    public String setItems(Player player, @Arg @Name("kit") Kit kit) {
        this.kitEditInventory.openKitEdit(player, kit);

        return this.messages.kitEdit.items;
    }

    @Execute(route = "setSlot")
    public String setSlot(@Arg @Name("kit") Kit kit, @Arg @Name("slot") int slot) {
        this.kitRepository.saveKit(kit.getName(), kit.getName(), kit.getDuration(), kit.getPermission(), kit.getItems(), kit.getCommands(), kit.getGuiItem(), slot);

        return this.messages.kitEdit.slot.replace("{SLOT}", String.valueOf(slot));
    }

    @Execute(route = "setDisplayName")
    public String setDisplayName(@Arg @Name("kit") Kit kit, @Joiner @Name("display-name") String displayName) {
        this.kitRepository.saveKit(kit.getName(), displayName, kit.getDuration(), kit.getPermission(), kit.getItems(), kit.getCommands(), kit.getGuiItem(), kit.getGuiSlot());

        return this.messages.kitEdit.displayName.replace("{DISPLAY_NAME}", displayName);
    }

    @Execute(route = "delete")
    public String delete(@Arg @Name("kit") Kit kit) {
        this.kitRepository.deleteKit(kit.getName());

        return this.messages.kitEdit.delete.replace("{NAME}", kit.getName());
    }
}
