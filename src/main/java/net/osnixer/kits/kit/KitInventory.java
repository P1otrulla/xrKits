package net.osnixer.kits.kit;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.osnixer.kits.config.implementation.MessagesConfig;
import net.osnixer.kits.config.implementation.PluginConfig;
import net.osnixer.kits.user.User;
import net.osnixer.kits.user.UserService;
import net.osnixer.kits.util.ChatUtil;
import net.osnixer.kits.util.DurationUtil;
import net.osnixer.kits.util.ItemUtil;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class KitInventory {

    private final PluginConfig config;
    private final MessagesConfig messages;
    private final KitRepository kitRepository;
    private final UserService userService;
    private final AudienceProvider audienceProvider;
    private final Server server;

    public KitInventory(PluginConfig config, MessagesConfig messages, KitRepository kitRepository, UserService userService, AudienceProvider audienceProvider, Server server) {
        this.config = config;
        this.messages = messages;
        this.kitRepository = kitRepository;
        this.userService = userService;
        this.audienceProvider = audienceProvider;
        this.server = server;
    }

    public void openInventory(Player player) {
        Optional<User> userOptional = this.userService.findUser(player.getUniqueId());

        if (!userOptional.isPresent()) {
            player.kickPlayer("user not found");

            throw new IllegalStateException("user not found");
        }

        User user = userOptional.get();

        Gui gui = Gui
                .gui()
                .title(ChatUtil.mini(this.config.global.kitTitle))
                .rows(this.config.global.kitRows)
                .disableAllInteractions()
                .create();

        if (this.config.close.enabled) {
            ItemBuilder close = ItemBuilder.from(this.config.close.material)
                    .name(ChatUtil.mini(this.config.close.name))
                    .lore(ChatUtil.mini(this.config.close.lore))
                    .flags(ItemFlag.values());

            gui.setItem(this.config.close.slot, close.asGuiItem(event -> event.getWhoClicked().closeInventory()));
        }

        if (this.config.filler.enabled) {
            ItemBuilder filler = ItemBuilder.from(this.config.filler.material)
                    .name(ChatUtil.mini(this.config.filler.name))
                    .lore(ChatUtil.mini(this.config.filler.lore))
                    .flags(ItemFlag.values());

            gui.getFiller().fill(filler.asGuiItem());
        }

        this.config.decorate.forEach((slot, material) -> {
            ItemBuilder decorate = ItemBuilder.from(material)
                    .name(Component.text(" "));

            gui.setItem(slot, decorate.asGuiItem());
        });

        for (Kit kit : this.kitRepository.getKits()) {
            if (this.config.global.onlyPermissionKits) {
                if (!player.hasPermission(kit.getPermission())) {
                    continue;
                }
            }

            ItemBuilder guiItem = ItemBuilder.from(kit.getGuiItem());
            guiItem.flags(ItemFlag.values());

            gui.setItem(kit.getGuiSlot(), guiItem.asGuiItem(event -> {
                KitAction kitAction = this.config.global.actions.get(event.getClick());

                if (kitAction == KitAction.PICKUP) {
                    this.preparePickUpKit(player, user, kit);
                }
                else if (kitAction == KitAction.PREVIEW) {
                    this.previewKit(player, kit);
                }
            }));

        }

        gui.open(player);
    }

    private void previewKit(Player player, Kit kit) {
        Gui gui = Gui
                .gui()
                .title(ChatUtil.mini(this.config.preview.title.replace("{KIT}", kit.getDisplayName())))
                .rows(this.config.preview.rows)
                .disableAllInteractions()
                .create();

        for (ItemStack itemStack : kit.getItems()) {
            ItemBuilder item = ItemBuilder.from(itemStack);

            gui.addItem(item.asGuiItem());
        }

        if (this.config.preview.pickUp.enabled) {
            ItemBuilder take = ItemBuilder.from(this.config.preview.pickUp.material)
                    .name(ChatUtil.mini(this.config.preview.pickUp.name))
                    .lore(ChatUtil.mini(this.config.preview.pickUp.lore))
                    .flags(ItemFlag.values());

            gui.setItem(this.config.preview.pickUp.slot, take.asGuiItem(event -> {
                this.preparePickUpKit(player, this.userService.findUser(player.getUniqueId()).get(), kit);
            }));
        }

        if (this.config.preview.back.enabled) {
            ItemBuilder back = ItemBuilder.from(this.config.preview.back.material)
                    .name(ChatUtil.mini(this.config.preview.back.name))
                    .lore(ChatUtil.mini(this.config.preview.back.lore))
                    .flags(ItemFlag.values());

            gui.setItem(this.config.preview.back.slot, back.asGuiItem(event -> this.openInventory((Player) event.getWhoClicked())));
        }

        gui.open(player);

    }

    private void preparePickUpKit(Player player, User user, Kit kit) {
        Audience audience = this.audienceProvider.player(player.getUniqueId());

        if (!player.hasPermission(kit.getPermission())) {
            audience.sendMessage(ChatUtil.mini(this.messages.argument.kitNoPermission.replace("{PERMISSION}", kit.getPermission())));
            player.closeInventory();

            return;
        }

        if (!user.canUseKit(kit) /*&& !player.hasPermission(this.config.global.kitBypassPermission)*/) {
            Duration timeLeft = Duration.between(Instant.now(), user.getLastKitUse(kit));

            audience.sendMessage(ChatUtil.mini(this.messages.argument.kitOnCooldown.replace("{TIME}", DurationUtil.format(timeLeft))));
            player.closeInventory();

            return;
        }

        user.markKitUse(kit);
        kit.getCommands().forEach(command -> this.server.dispatchCommand(this.server.getConsoleSender(), command.replace("{PLAYER}", player.getName())));
        kit.getItems().forEach(item -> ItemUtil.giveItem(player, item));

        audience.sendMessage(ChatUtil.mini(this.messages.kitTaken.replace("{KIT}", kit.getDisplayName())));

        player.closeInventory();
    }
}
