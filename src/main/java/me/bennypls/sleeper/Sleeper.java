package me.bennypls.sleeper;

import org.bukkit.plugin.java.JavaPlugin;

public class Sleeper extends JavaPlugin {

    public void onEnable() {
        this.saveDefaultConfig();

        Configuration configuration = new Configuration(this, getConfig());
        Resting resting = new Resting(this, configuration);

        getCommand("sleeper")
                .setExecutor(new SleeperCommand(this, resting, configuration));

        getServer().getPluginManager()
                .registerEvents(new BedListener(this, configuration, resting), this);

    }
}
