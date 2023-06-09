package me.bennypls.sleeper;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * <h1>
 * BedListener
 *
 * <p>
 * This is a Bukkit event listener that listens for player bed events.
 *
 * <p>
 * Starts the resting process when a player enters a bed, and stops it when the player leaves,
 * this is for counting the number of players resting and triggers a skip night event if enough
 * players are resting. This number of players is configurable in config.yml and uses a reference to
 * the JavaPlugin and Configuration Classes.
 */
public final class BedListener implements Listener {
    /**
     * The JavaPlugin instance used by this listener.
     */
    private final JavaPlugin plugin;
    /**
     * The Configuration instance used by this listener.
     */
    private final Configuration configuration;
    /**
     * The Resting instance used by this listener.
     */
    private final Resting resting;
    /** The number of players currently resting in a hashmap by world. */
    private final Map<World, Integer> actualPlayersResting = new HashMap<>();

    /**
     * <h1>
     * BedListener Constructor
     * <p>
     * Constructs a new BedListener instance with the specified plugin, configuration, and resting objects.
     *
     * @param plugin  The JavaPlugin instance to use.
     * @param config  The Configuration instance to use.
     * @param resting The Resting instance to use.
     */
    public BedListener(Sleeper plugin, Configuration config, Resting resting) {
        this.plugin = plugin;
        this.configuration = config;
        this.resting = resting;
    }

    /**
     * <h1>
     * Bed Interaction
     * <p>
     * Listens for a PlayerBedEnterEvent and starts the resting process for the player.
     * <p>
     * Tasks the execution of startResting after one 1 tick.
     *
     * @param event the PlayerBedEnterEvent that occurred.
     * @see BedListener#startResting
     */
    @EventHandler
    public void bedInteraction(PlayerBedEnterEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> startResting(event.getPlayer()), 1);
    }

    /**
     * <h1>
     * Start Resting
     *
     * <p>
     * Starts the resting process for the specified player.
     * This will add one to {@link BedListener#actualPlayersResting} if the name of the player
     * isn't in the ignored list and still in the bed.
     *
     * <p>
     * This also comproves if the {@link BedListener#actualPlayersResting} is sufficient number to trigger the event
     * skip night based in the configuration to a min of one player to skip the night.
     *
     * @param player The player to start resting.
     */
    private void startResting(Player player) {
        if (!player.isSleeping()) {
            plugin.getLogger().log(Level.FINER, player.getName() + " is not sleeping.");
            return;
        }

        if (configuration.getIgnoredPlayers().contains(player.getName())) {
            plugin.getLogger().log(Level.FINER, player.getName() + " is being ignored.");
            return;
        }

        var playerWorld = player.getWorld();

        actualPlayersResting.put(playerWorld, actualPlayersResting.getOrDefault(playerWorld, 0) + 1);

        var necessaryToSkip = resting.getTotalNecessaryToSkip(playerWorld);

        resting.executeCommand(configuration.getRestMessage()
                .replace("{playerName}", player.getName())
                .replace("{actual}", String.valueOf(actualPlayersResting.get(playerWorld)))
                .replace("{necessary}", String.valueOf(necessaryToSkip)));

        if (actualPlayersResting.get(playerWorld) >= necessaryToSkip) {
            if (!resting.skipNight(playerWorld, false)) {
                resting.executeCommand(configuration.getCannotSkipNightMessage());
            }
        }
    }

    /**
     * <h1>
     * Stop Resting
     *
     * <p>
     * Listens for a PlayerBedLeaveEvent and removes one player to the counter {@link BedListener#actualPlayersResting}
     *
     * @param event the PlayerBedLeaveEvent that occurred
     */
    @EventHandler
    public void stopResting(PlayerBedLeaveEvent event) {
        var playerWorld = event.getPlayer().getWorld();

        actualPlayersResting.put(playerWorld, actualPlayersResting.get(playerWorld) - 1);
    }

}
