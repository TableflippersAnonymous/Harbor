package xyz.nkomarn.harbor;

import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.harbor.afk.AFKPlusAfkTrackerImpl;
import xyz.nkomarn.harbor.afk.EssentialsAfkTrackerImpl;
import xyz.nkomarn.harbor.afk.FallbackAfkTrackerImpl;
import xyz.nkomarn.harbor.afk.IAfkTracker;
import xyz.nkomarn.harbor.afk.NullAfkTrackerImpl;
import xyz.nkomarn.harbor.command.ForceSkipCommand;
import xyz.nkomarn.harbor.command.HarborCommand;
import xyz.nkomarn.harbor.listener.BedListener;
import xyz.nkomarn.harbor.task.Checker;
import xyz.nkomarn.harbor.util.Config;
import xyz.nkomarn.harbor.util.Messages;
import xyz.nkomarn.harbor.util.Metrics;
import xyz.nkomarn.harbor.util.PlayerManager;

import java.util.Arrays;
import java.util.Locale;

public class Harbor extends JavaPlugin {

    private static Harbor instance;

    private Config config;
    private Checker checker;
    private Messages messages;
    private PlayerManager playerManager;
    private IAfkTracker afkTracker;

    public static Harbor getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        final PluginManager pluginManager = getServer().getPluginManager();
        config = new Config(this);
        checker = new Checker(this);
        messages = new Messages(this);
        afkTracker = initializeAfktracker();
        getLogger().info("Using " + afkTracker.getClass().getSimpleName() + " for AFK detection");
        playerManager = new PlayerManager(this);

        Arrays.asList(
                messages,
                playerManager,
                new BedListener(this)
        ).forEach(listener -> pluginManager.registerEvents(listener, this));

        getCommand("harbor").setExecutor(new HarborCommand(this));
        getCommand("forceskip").setExecutor(new ForceSkipCommand(this));


        if (config.getBoolean("metrics")) {
            new Metrics(this);
        }
    }

    private IAfkTracker initializeAfktracker() {
        if(!config.getBoolean("afk-detection.enabled"))
            return new NullAfkTrackerImpl();
        final String preferredAfkTracker = config.getString("afk-detection.provider");
        switch(preferredAfkTracker.toLowerCase(Locale.ROOT)) {
            case "essentials":
                return new EssentialsAfkTrackerImpl(this);
            case "afkplus":
                return new AFKPlusAfkTrackerImpl(this);
            case "builtin":
                return new FallbackAfkTrackerImpl(this);
            default:
                getLogger().info("No AFK detection provider selected; trying our best...");
            case "auto":
                return IAfkTracker.pickBestAvailable(this);
        }
    }

    @Override
    public void onDisable() {
        for (World world : getServer().getWorlds()) {
            messages.clearBar(world);
        }
    }

    @NotNull
    public String getVersion() {
        return getDescription().getVersion();
    }

    @NotNull
    public Config getConfiguration() {
        return config;
    }

    @NotNull
    public Checker getChecker() {
        return checker;
    }

    @NotNull
    public Messages getMessages() {
        return messages;
    }

    @NotNull
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @NotNull
    public IAfkTracker getAfkTracker() {
        return afkTracker;
    }
}
