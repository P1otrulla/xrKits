package net.osnixer.kits.config.implementation;

import net.dzikoysk.cdn.source.Resource;
import net.dzikoysk.cdn.source.Source;
import net.osnixer.kits.config.ReloadableConfig;

import java.io.File;

public class KitDataConfig implements ReloadableConfig {

    @Override
    public Resource resource(File folder) {
        return Source.of(folder, "data" + File.separator + "kits.data");
    }

    public int usedKits = 0;

    public int getUsedKits() {
        return this.usedKits;
    }

    public void addUsedKit() {
        this.usedKits += 1;
    }
}