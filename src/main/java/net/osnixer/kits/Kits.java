package net.osnixer.kits;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.adventure.platform.LiteBukkitAdventurePlatformFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.osnixer.kits.config.ConfigManager;
import net.osnixer.kits.config.implementation.KitDataConfig;
import net.osnixer.kits.config.implementation.MessagesConfig;
import net.osnixer.kits.config.implementation.PluginConfig;
import net.osnixer.kits.database.DatabaseService;
import net.osnixer.kits.kit.Kit;
import net.osnixer.kits.kit.KitEditInventory;
import net.osnixer.kits.kit.KitInventory;
import net.osnixer.kits.kit.KitRepository;
import net.osnixer.kits.kit.KitRepositoryImpl;
import net.osnixer.kits.kit.command.KitCommand;
import net.osnixer.kits.kit.command.KitEditCommand;
import net.osnixer.kits.kit.command.KitReloadCommand;
import net.osnixer.kits.kit.command.argument.DurationArgument;
import net.osnixer.kits.kit.command.argument.KitArgument;
import net.osnixer.kits.user.UserController;
import net.osnixer.kits.user.UserRepositoryImpl;
import net.osnixer.kits.user.UserService;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public final class Kits extends JavaPlugin {

    private static Kits instance;

    private ConfigManager configManager;
    private PluginConfig config;
    private MessagesConfig messages;

    private DatabaseService databaseService;
    private UserRepositoryImpl userRepositoryImpl;

    private KitRepository kitRepository;
    private KitInventory kitInventory;
    private KitEditInventory kitEditInventory;

    private UserService userService;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        instance = this;

        final Server server = this.getServer();

        AudienceProvider audienceProvider = BukkitAudiences.create(this);

        this.configManager = new ConfigManager(this.getDataFolder());
        this.config = new PluginConfig();
        this.messages = new MessagesConfig();

        KitDataConfig kitData = new KitDataConfig();

        this.configManager.load(this.config);
        this.configManager.load(this.messages);
        this.configManager.load(kitData);

        this.databaseService = new DatabaseService(this.config, this.getDataFolder(), this.getLogger());
        this.databaseService.connect();

        this.userRepositoryImpl = new UserRepositoryImpl(this.databaseService, this.getLogger());
        this.kitRepository = new KitRepositoryImpl(this.configManager);

        this.userService = new UserService();

        this.kitInventory = new KitInventory(this.config, this.messages, this.kitRepository, this.userService, audienceProvider, kitData, configManager, server);
        this.kitEditInventory = new KitEditInventory(this.config, this.kitRepository);

        this.liteCommands = LiteBukkitAdventurePlatformFactory.builder(server, "xrkits", audienceProvider)
                .commandInstance(
                        new KitCommand(this.kitInventory),
                        new KitEditCommand(this.kitEditInventory, this.kitRepository, this.messages),
                        new KitReloadCommand(this.configManager, this.messages)
                )

                .argument(Kit.class, new KitArgument(this.kitRepository, this.messages))
                .argument(Duration.class, new DurationArgument(this.messages))
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual(this.messages.argument.onlyPlayer))

                .register();

        this.userRepositoryImpl.loadAllUsers().forEach(user -> this.userService.addUser(user));

        this.getServer().getPluginManager().registerEvents(new UserController(this.userService, this.userRepositoryImpl), this);

        Metrics metrics = new Metrics(this, 16014);

        metrics.addCustomChart(new SingleLineChart("used_kits", kitData::getUsedKits));
        metrics.addCustomChart(new SingleLineChart("total_kits", () -> this.kitRepository.getKits().size()));
    }

    @Override
    public void onDisable() {
        this.databaseService.close();
    }

    public Kits getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public PluginConfig getPluginConfig() {
        return this.config;
    }

    public MessagesConfig getMessages() {
        return this.messages;
    }

    public UserRepositoryImpl getDatabaseRepository() {
        return this.userRepositoryImpl;
    }

    public KitRepository getKitRepository() {
        return this.kitRepository;
    }

    public DatabaseService getDatabaseService() {
        return this.databaseService;
    }

    public KitInventory getKitInventory() {
        return this.kitInventory;
    }

    public KitEditInventory getKitEditInventory() {
        return this.kitEditInventory;
    }

    public UserService getUserService() {
        return this.userService;
    }

    public LiteCommands<CommandSender> getLiteCommands() {
        return this.liteCommands;
    }
}
