package net.osnixer.kits.kit;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.osnixer.kits.config.implementation.PluginConfig;
import net.osnixer.kits.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class KitEditInventory {

    private final PluginConfig config;
    private final KitRepository kitRepository;

    public KitEditInventory(PluginConfig config, KitRepository kitRepository) {
        this.config = config;
        this.kitRepository = kitRepository;
    }

    public void openKitEdit(Player player, Kit kit) {
        Gui gui = Gui
                .gui()
                .title(ChatUtil.mini(this.config.global.kitEditTitle.replace("{KIT}", kit.getName())))
                .rows(this.config.global.kitEditRows)
                .create();

        for (ItemStack itemStack : kit.getItems()) {
            gui.addItem(new GuiItem(itemStack));
        }

        gui.setCloseGuiAction(event -> {
            this.kitRepository.saveKit(kit.getName(), kit.getName(), kit.getDuration(), kit.getPermission(), Arrays.asList(gui.getInventory().getContents()), kit.getCommands(), kit.getGuiItem(), kit.getGuiSlot());
        });

        gui.open(player);
    }
}
