package net.osnixer.kits.kit;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import net.osnixer.kits.config.ReloadableConfig;
import net.osnixer.kits.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class KitsConfiguration implements ReloadableConfig {

    Map<String, KitConfig> kits = Map.of(
            "gracz", new KitConfig("gracz",
                    "Gracz",
                    Duration.ofMinutes(30),
                    "kit.gracz",
                    List.of(new ItemStack(Material.DIAMOND_SWORD)),
                    List.of(),
                    ItemBuilder.from(Material.DIAMOND_SWORD)
                            .name(ChatUtil.mini("<dark_gray>• <dark_green>Kit: <white>Gracz"))
                            .lore(ChatUtil.mini(Arrays.asList("   <dark_gray>» <gray>Kliknij PPM, aby obejrzeć kit!", "   <dark_gray>» <gray>Kliknij LPM, aby odebrać kit!")))
                            .build(),
                    10)
    );

    @Contextual
    static class KitConfig implements Kit {

        String name = "none";
        String displayName = "none";
        Duration duration = Duration.ofHours(1);
        String permission = "kit.none";

        List<ItemStack> items = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        ItemStack guiItem = new ItemStack(Material.STONE);
        int guiSlot = 1;

        KitConfig() {

        }

        KitConfig(String name, String displayName, Duration duration, String permission, List<ItemStack> items, List<String> commands, ItemStack guiItem, int guiSlot) {
            this.name = name;
            this.displayName = displayName;
            this.duration = duration;
            this.permission = permission;
            this.items = items;
            this.commands = commands;
            this.guiItem = guiItem;
            this.guiSlot = guiSlot;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getDisplayName() {
            return this.displayName;
        }

        @Override
        public Duration getDuration() {
            return this.duration;
        }

        @Override
        public String getPermission() {
            return this.permission;
        }

        @Override
        public List<ItemStack> getItems() {
            return this.items;
        }

        @Override
        public List<String> getCommands() {
            return this.commands;
        }

        @Override
        public ItemStack getGuiItem() {
            return this.guiItem;
        }

        @Override
        public int getGuiSlot() {
            return this.guiSlot;
        }

    }

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "kits.yml");
    }

}
