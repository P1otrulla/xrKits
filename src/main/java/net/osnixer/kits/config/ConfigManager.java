package net.osnixer.kits.config;

import net.dzikoysk.cdn.Cdn;
import net.dzikoysk.cdn.CdnFactory;
import net.dzikoysk.cdn.reflect.Visibility;
import net.osnixer.kits.config.composer.DurationComposer;
import net.osnixer.kits.config.composer.ItemStackComposer;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class ConfigManager {

    private final static Cdn CDN = CdnFactory.createYamlLike().getSettings()
            .withComposer(ItemStack.class, new ItemStackComposer())
            .withComposer(Duration.class, new DurationComposer())
            .withMemberResolver(Visibility.PACKAGE_PRIVATE)
            .build();

    private final Set<ReloadableConfig> configs = new HashSet<>();
    private final File dataFolder;

    public ConfigManager(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public <T extends ReloadableConfig> void load(T config) {
        CDN.load(config.resource(this.dataFolder), config)
                .orThrow(RuntimeException::new);

        CDN.render(config, config.resource(this.dataFolder))
                .orThrow(RuntimeException::new);

        this.configs.add(config);
    }

    public <T extends ReloadableConfig> void save(T config) {
        CDN.render(config, config.resource(this.dataFolder))
                .orThrow(RuntimeException::new);
    }

    public void reload() {
        for (ReloadableConfig config : configs) {
            load(config);
        }
    }

}