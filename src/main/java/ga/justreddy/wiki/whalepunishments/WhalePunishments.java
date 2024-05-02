package ga.justreddy.wiki.whalepunishments;

import ga.justreddy.wiki.whalepunishments.commands.BanCommand;
import ga.justreddy.wiki.whalepunishments.listeners.PlayerLoginListener;
import ga.justreddy.wiki.whalepunishments.loader.LibraryLoader;
import ga.justreddy.wiki.whalepunishments.storage.Storage;
import ga.justreddy.wiki.whalepunishments.storage.flatfile.FlatStorage;
import ga.justreddy.wiki.whalepunishments.storage.readable.JsonStorage;
import ga.justreddy.wiki.whalepunishments.storage.readable.YamlStorage;
import ga.justreddy.wiki.whalepunishments.storage.remote.MongoStorage;
import ga.justreddy.wiki.whalepunishments.storage.remote.SequalStorage;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public final class WhalePunishments extends JavaPlugin {

    private static WhalePunishments instance;

    private final LibraryLoader libraryLoader;
    private Storage storage;
    private BukkitAudiences adventure;

    public WhalePunishments() {
        libraryLoader = new LibraryLoader(this);
    }

    @Override
    public void onLoad() {
        libraryLoader.load();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        // TODO
        this.adventure = BukkitAudiences.create(this);
        saveDefaultConfig();

        String databaseType = getConfig().getString("storage");
        if (databaseType == null) databaseType = "sqlite";
        switch (databaseType.toLowerCase()) {
            case "sqlite", "h2" -> storage = new FlatStorage(databaseType, this);
            case "mysql", "mariadb", "postgresql" -> storage = new SequalStorage(databaseType,
                    getConfig().getString("data.host"),
                    getConfig().getString("data.database"),
                    getConfig().getString("data.username"),
                    getConfig().getString("data.password"),
                    getConfig().getInt("data.port")
            );
            case "mongodb" -> storage = new MongoStorage(getConfig().getString("data.mongo-uri"));
            case "yaml", "yml" -> storage = new YamlStorage(this);
            case "json" -> storage = new JsonStorage(this);
            default -> throw new IllegalArgumentException("Invalid storage type: " + databaseType);
        }

        loadCommands();

        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    private void loadCommands() {
        final BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);
        commandHandler.register(new BanCommand());
        commandHandler.registerBrigadier();
    }

    public static WhalePunishments getInstance() {
        return instance;
    }

    public Storage getStorage() {
        return storage;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }


}
