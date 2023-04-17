package me.bennypls.sleeper;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * <h1>
 * Sleeper
 *
 * <p>
 * Sleeper is the main class of the Sleeper plugin that extends {@link org.bukkit.plugin.java.JavaPlugin}.
 * It initializes the configuration and the Resting class to handle sleep events.
 * It also registers the SleeperCommand and the BedListener with the server.
 */
public class Sleeper extends JavaPlugin {

    public void onEnable() {
        this.saveDefaultConfig();

        Configuration configuration = new Configuration(this, getConfig());
        Resting resting = new Resting(this, configuration);

        getCommand("sleeper")
            .setExecutor(new SleeperCommand(resting, configuration));

        getServer().getPluginManager()
            .registerEvents(new BedListener(this, configuration, resting), this);
    }
}
