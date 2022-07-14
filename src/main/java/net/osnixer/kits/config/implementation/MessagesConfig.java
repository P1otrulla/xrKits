package net.osnixer.kits.config.implementation;

import net.dzikoysk.cdn.entity.Contextual;
import net.dzikoysk.cdn.entity.Description;
import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import net.osnixer.kits.config.ReloadableConfig;

import java.io.File;

public class MessagesConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "messages.yml");
    }

    public Argument argument = new Argument();

    @Description(" ")
    public KitEdit kitEdit = new KitEdit();

    @Description(" ")
    public String kitReloaded = "<green>Kity zostaly przeladowane!";
    public String kitTaken = "<green>Odebrales kit: <white>{KIT}";

    @Contextual
    public static class Argument {

        public String wrongTimeFormat = "<dark_red>Błąd: <red>Niepoprawny format czasu! <gray>(Przykładowy: 4h30m10s)";
        public String onlyPlayer = "<dark_red>Błąd: <red>Komenda dostępna tylko dla graczy!";
        public String kitNoPermission = "<dark_red>Błąd: <red>Nie masz uprawnien aby odebrać ten kit! <gray>({PERMISSION})";
        public String kitOnCooldown = "<dark_red>Błąd: <red>Juz odebrales ten kit!, mozesz go odebrac za: <gray>{TIME}";
        public String kitNoExists = "<dark_red>Błąd: <red>Taki kit nie istenieje!";
        public String kitAlreadyExists = "<dark_red>Błąd: <red>Taki kit juz istnieje!";

    }

    @Contextual
    public static class KitEdit {

        public String create = "<dark_green>Utworzono kit: <white>{KIT}<dark_green>, z perm: <white>{PERMISSION}<dark_green>, na czas: <white>{TIME}<dark_green>, z ikona: <white>{ICON}<dark_green>, z slotem: <white>{SLOT}";
        public String icon = "<dark_green>Ustawiono ikone kitu na: <white>{ICON}";
        public String duration = "<dark_green>Ustawiono czas kitu na: <white>{TIME}";
        public String permission = "<dark_green>Ustawiono permisje kitu na: <white>{PERMISSION}";
        public String commands = "<dark_green>Ustawiono wykonane komendy kitu na: <white>{COMMANDS}";
        public String lore = "<dark_green>Ustawiono lore kitu na: <white>{LORE}";
        public String guiName = "<dark_green>Ustawiono nazwe itemu w gui na: <white>{NAME}";
        public String items = "<dark_green>Itemy zostaną zapisane po zamknięciu okna edycji kitu!";
        public String slot = "<dark_green>Ustawiono slot kitu na: <white>{SLOT}";
        public String displayName = "<dark_green>Ustawiono displayName kitu na: <white>{DISPLAY_NAME}";
        public String delete = "<dark_green>Usunieto kit: <white>{KIT}";

    }
}
