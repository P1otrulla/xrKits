package net.osnixer.kits.kit;

import net.osnixer.kits.config.ConfigManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class KitRepositoryImpl implements KitRepository {

    private final KitsConfiguration kitsConfiguration = new KitsConfiguration();

    private final ConfigManager configManager;

    public KitRepositoryImpl(ConfigManager configManager) {
        this.configManager = configManager;
        this.configManager.load(this.kitsConfiguration);
    }

    @Override
    public Optional<Kit> getKit(String name) {
        return Optional.ofNullable(this.kitsConfiguration.kits.get(name));
    }

    @Override
    public void saveKit(String name, String displayName, Duration duration, String permission, List<ItemStack> items, List<String> commands, ItemStack guiItem, int guiSlot) {
        items = items.stream()
                .filter(Objects::nonNull)
                .filter(item -> item.getType() != Material.AIR)
                .collect(Collectors.toList());

        Map<String, KitsConfiguration.KitConfig> copy = new HashMap<>(this.kitsConfiguration.kits);

        copy.put(name, new KitsConfiguration.KitConfig(name, displayName, duration, permission, items, commands, guiItem, guiSlot));

        this.kitsConfiguration.kits = Collections.unmodifiableMap(copy);
        this.configManager.save(this.kitsConfiguration);
    }

    @Override
    public Collection<Kit> getKits() {
        return Collections.unmodifiableCollection(this.kitsConfiguration.kits.values());
    }

    @Override
    public void deleteKit(String name) {
        Map<String, KitsConfiguration.KitConfig> copy = new HashMap<>(this.kitsConfiguration.kits);

        copy.remove(name);

        this.kitsConfiguration.kits = Collections.unmodifiableMap(copy);
        this.configManager.save(this.kitsConfiguration);
    }

}
