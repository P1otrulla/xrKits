package net.osnixer.kits.kit;

import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.List;

public interface Kit {

    String getName();

    String getDisplayName();

    Duration getDuration();

    String getPermission();

    List<ItemStack> getItems();

    List<String> getCommands();

    ItemStack getGuiItem();

    int getGuiSlot();

}
