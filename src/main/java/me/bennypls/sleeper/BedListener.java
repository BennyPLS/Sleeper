package me.bennypls.sleeper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class BedListener implements Listener {
    private final JavaPlugin plugin;
    private final Configuration configuration;
    private final Resting resting;
    private int actualPlayersResting = 0;

    public BedListener(Sleeper plugin, Configuration config, Resting resting) {
        this.plugin = plugin;
        this.configuration = config;
        this.resting = resting;
    }

    @EventHandler
    public void bedInteraction(PlayerBedEnterEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> startResting(event.getPlayer()), 1);
    }

    private void startResting(Player player) {
        if (!player.isSleeping()) {
            plugin.getLogger().info(player.getName() + " is not sleeping.");
            return;
        }

        if (configuration.getIgnoredPlayers().contains(player.getName())) {
            plugin.getLogger().info(player.getName() + " is being ignored.");
            return;
        }

        actualPlayersResting++;

        var necessaryToSkip = resting.getTotalNecessaryToSkip();

        resting.executeCommand(configuration.getRestMessage()
                .replace("{playerName}" , player.getName())
                .replace("{actual}"     , String.valueOf(actualPlayersResting))
                .replace("{necessary}"  , String.valueOf(necessaryToSkip))
        );

        if (actualPlayersResting >= necessaryToSkip) {
            resting.skipNight(player);
        }
    }

    @EventHandler
    public void stopResting(PlayerBedLeaveEvent event) {
        actualPlayersResting--;
    }

}
