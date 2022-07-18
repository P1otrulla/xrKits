package net.osnixer.kits.config.implementation;

import com.google.common.collect.ImmutableMap;
import com.j256.ormlite.stmt.query.In;
import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import net.osnixer.kits.config.ReloadableConfig;
import net.osnixer.kits.database.DatabaseType;
import net.osnixer.kits.kit.KitAction;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PluginConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "config.yml");
    }

    public Database database = new Database();

    @Description(" ")
    public Global global = new Global();

    @Description(" ")
    public Preview preview = new Preview();

    @Description(" ")
    public Filler filler = new Filler();

    @Description({ " ", "# Dekoracje w gui", "# slot: item" })
    public Map<Integer, Material> decorate = ImmutableMap.<Integer, Material>builder()
            .put(0, Material.RED_STAINED_GLASS_PANE)
            .put(8, Material.RED_STAINED_GLASS_PANE)
            .put(18, Material.RED_STAINED_GLASS_PANE)
            .put(26, Material.RED_STAINED_GLASS_PANE)
            .build();

    @Description(" ")
    public Close close = new Close();

    @Contextual
    public static class Database {

        @Description("# Types: MYSQL, SQLITE")
        public DatabaseType type = DatabaseType.SQLITE;
        public String host = "localhost";
        public int port = 3306;
        public String base = "zamowienia";
        public String user = "osnixer";
        public String pass = "pass";
        public int poolSize = 16;

    }
    
    @Contextual
    public static class Global {

        public String kitTitle = "<dark_green>Kity:";
        public int kitRows = 3;

        @Description(" ")
        public boolean onlyPermissionKits = true;

        @Description(" ")
        public String kitBypassPermission = "kit.bypass";

        @Description(" ")
        public String kitEditTitle = "<dark_green>Edycja kitu: <white>{KIT}";
        public int kitEditRows = 5;

        @Description({ " ",
                "# Interakcje kliknięcia w kit",
                "# np. RIGHT: PICKUP - klikniecie prawym odbiera kit",
                "# ",
                "# KitActions:",
                "# PICKUP - odbieranie zestawu",
                "# PREVIEW - podglad zestawu",
                "# ",
                "# ClickType: ",
                "# https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/inventory/ClickType.html" })
        public Map<ClickType, KitAction> actions = ImmutableMap.<ClickType, KitAction>builder()
                .put(ClickType.RIGHT, KitAction.PICKUP)
                .put(ClickType.LEFT, KitAction.PREVIEW)
                .build();
    }

    @Contextual
    public static class Filler {

        public boolean enabled = true;
        public Material material = Material.GRAY_STAINED_GLASS_PANE;
        public String name = " ";
        public List<String> lore = new ArrayList<>();

    }

    @Contextual
    public static class Close {

        public boolean enabled = true;
        public Material material = Material.BARRIER;
        public String name = "<red>Zamknij";
        public int slot = 22;
        public List<String> lore = new ArrayList<>();

    }

    @Contextual
    public static class Preview {

        public String title = "<dark_green>Podglad kitu: <white>{KIT}";
        public int rows = 4;

        @Description(" ")
        public PickUp pickUp = new PickUp();

        @Description(" ")
        public Back back = new Back();

        @Contextual
        public static class Back {

            public boolean enabled = true;
            public Material material = Material.OAK_FENCE_GATE;
            public String name = "<dark_green>Wróc do menu: <white>Kity";
            public int slot = 35;
            public List<String> lore = new ArrayList<>();
        }

        @Contextual
        public static class PickUp {

            public boolean enabled = true;
            public Material material = Material.LIME_DYE;
            public String name = "<green>Odbierz kit!";
            public List<String> lore = new ArrayList<>();
            public int slot = 34;

        }

    }
}
