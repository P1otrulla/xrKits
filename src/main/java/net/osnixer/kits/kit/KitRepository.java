package net.osnixer.kits.kit;

import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface KitRepository {

    Optional<Kit> getKit(String name);

    void saveKit(String name, String displayName, Duration duration, String permission, List<ItemStack> items, List<String> commands, ItemStack guiItem, int guiSlot);

    Collection<Kit> getKits();

    void deleteKit(String name);



}
