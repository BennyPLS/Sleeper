package me.bennypls.sleeper;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class Resting {

    private final JavaPlugin plugin;
    private final Configuration configuration;
    private static final long SUNRISE_TIME = 23850;
    private static final long NIGHTFALL_TIME = 12000;
    public Resting(JavaPlugin plugin, Configuration configuration) {
        this.plugin = plugin;
        this.configuration = configuration;
    }


    public boolean skipNight(Player player) {
        World world = player.getWorld();

        long timeDiff = world.getTime() - SUNRISE_TIME;

        if (timeDiff < NIGHTFALL_TIME) {
            return false;
        }

        executeCommand(configuration.getSkipNightMessage());

        if (configuration.isAnimated()) {
            skipNightAnimation(world);
        } else {
            world.setTime(23850);
        }

        return true;
    }

    public void skipNightAnimation(World world) {
        var totalIntervals = world.getTime() - SUNRISE_TIME / configuration.getAnimationSpeed();

        for (int i = 0; i < totalIntervals; i++) {
            Bukkit.getScheduler().runTaskLater(
                plugin,
                () -> world.setTime(world.getTime() + configuration.getAnimationSpeed()),
                (long) configuration.getAnimationInterval() * i
            );
        }
    }

    public int getTotalNecessaryToSkip() {
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        int totalPlayers = 0;

        for (Player player : players) {
            if (!configuration.getIgnoredPlayers().contains(player.getName())) {
                totalPlayers++;
            }
        }

        double necessaryPlayersToSkip = totalPlayers * configuration.getPercentageNecessaryToSleep();

        return necessaryPlayersToSkip <= 0 ? 1 : (int) Math.ceil(necessaryPlayersToSkip);
    }

    public void executeCommand(String command) {
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
    }

}
