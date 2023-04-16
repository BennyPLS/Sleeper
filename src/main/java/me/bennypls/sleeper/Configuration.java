package me.bennypls.sleeper;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * <h1>
 * Configuration
 *
 * <p>
 * The Configuration class represents the plugin configuration file for the Sleeper plugin.
 *
 * <p>
 * It allows to load and retrieve values from the configuration file, as well as adding or removing ignored players.
 */
public final class Configuration {
    /** The JavaPlugin instance that this Configuration is associated with. */
    private final JavaPlugin plugin;
    /** The FileConfiguration instance that holds the configuration values. */
    private final FileConfiguration configuration;
    /** The list of players that should be ignored when calculating necessary players to sleep. */
    private List<String> ignoredPlayers;
    /** The percentage of players that need to sleep in order to skip the night. */
    private double percentageNecessaryToSleep;
    /** Whether the sleeping animation should be played when players sleep. */
    private boolean isAnimated;
    /** The speed of the sleeping animation. */
    private int animationSpeed;
    /** The interval at which the sleeping animation should be played. */
    private int animationInterval;
    /** The message that should be displayed to players when they sleep. */
    private String restMessage;
    /** The message that should be displayed to players when the night is skipped. */
    private String skipNightMessage;

    /**
     * <h1>
     * Configuration Constructor
     *
     * <p>
     * Constructs a new Configuration object with the given JavaPlugin and FileConfiguration instances.
     *
     * @param plugin        The JavaPlugin instance that this Configuration is associated with.
     * @param configuration The FileConfiguration instance that holds the configuration values.
     */
    public Configuration(JavaPlugin plugin, FileConfiguration configuration) {
        this.plugin = plugin;
        this.configuration = configuration;

        loadConfiguration();
    }

    /**
     * <h1>
     * Reload
     *
     * <p>
     * Reloads the configuration values from the configuration file.
     */
    public void reload() {
        loadConfiguration();
    }

    /**
     * <h1>
     * Load Configuration
     *
     * <p>
     * Loads the configuration values from the configuration file.
     */
    private void loadConfiguration() {
        percentageNecessaryToSleep = configuration.getDouble("percentage-necessary-to-sleep", 0.25);
        ignoredPlayers = configuration.getStringList("ignored-players");
        isAnimated = configuration.getBoolean("is-animated", true);
        animationSpeed = configuration.getInt("animation-speed", 125);
        animationInterval = configuration.getInt("animation-interval", 1);
        restMessage = configuration.getString("rest-message", "title @a actionbar {\"text\":\"{actual} / {necessary} Players to skip night.\"}");
        skipNightMessage = configuration.getString("skip-night-message", "say Players skipped the night");
    }

    /**
     * <h1>
     * getIgnoredPlayers
     *
     * <p>
     * Returns the list of ignored players.
     *
     * @return The list of ignored players.
     */
    public List<String> getIgnoredPlayers() {
        return ignoredPlayers;
    }

    /**
     * <h1>
     * addIgnoredPlayer
     *
     * <p>
     * Adds a player to the list of ignored players.
     *
     * @param playerName The name of the player to add.
     * @return True if the player was added successfully, false otherwise.
     */
    public boolean addIgnoredPlayer(String playerName) {
        if (getIgnoredPlayers().contains(playerName)) {
            plugin.getLogger().warning("The player with name - " + playerName + " - is already in the ignored list.");
            return false;
        }

        plugin.getLogger().info("The player - " + playerName + " - has been added to the ignored list.");
        ignoredPlayers.add(playerName);
        configuration.set("ignored-players", ignoredPlayers);
        return true;
    }

    /**
     * <h1>
     * removeIgnoredPlayer
     *
     * <p>
     * Removes a player from the ignored list.
     *
     * @param playerName the name of the player to remove from the ignored list.
     * @return true if the player was removed successfully, false if the player was not in the ignored list.
     */
    public boolean removeIgnoredPlayer(String playerName) {
        if (!getIgnoredPlayers().contains(playerName)) {
            plugin.getLogger().warning("The player with name - " + playerName + " - not found in ignored list.");
            return false;
        }

        plugin.getLogger().info("The player - " + playerName + " - has been removed from the ignored list.");
        ignoredPlayers.remove(playerName);
        configuration.set("ignored-players", ignoredPlayers);
        return true;
    }

    /**
     * <h1>
     * getPercentageNecessaryToSleep
     * <p>
     * Returns the percentage of players that need to be sleeping for the night to be skipped.
     *
     * @return the percentage of players necessary to sleep.
     */
    public double getPercentageNecessaryToSleep() {
        return percentageNecessaryToSleep;
    }


    /**
     * <h1>
     * isAnimated
     *
     * <p>
     * Returns whether the animation is animated or not
     *
     * @return isAnimated true if the animation is animated, false otherwise
     */
    public boolean isAnimated() {
        return isAnimated;
    }

    /**
     * <h1>
     * getAnimationSpeed
     *
     * <p>
     * Returns the speed of the animation
     *
     * @return animationSpeed the speed of the animation
     */
    public int getAnimationSpeed() {
        return animationSpeed;
    }

    /**
     * <h1>
     * getAnimationInterval
     *
     * <p>
     * Returns the interval between animation frames
     *
     * @return animationInterval the interval between animation frames
     */
    public int getAnimationInterval() {
        return animationInterval;
    }

    /**
     * <h1>
     * getRestMessage
     *
     * <p>
     * Returns the message to display when resting
     *
     * @return restMessage the message to display when resting
     */
    public String getRestMessage() {
        return restMessage;
    }

    /**
     * <h1>
     * getSkipNightMessage
     *
     * <p>
     * Returns the message to display when skipping the night
     *
     * @return skipNightMessage the message to display when skipping the night
     */
    public String getSkipNightMessage() {
        return skipNightMessage;
    }
}
