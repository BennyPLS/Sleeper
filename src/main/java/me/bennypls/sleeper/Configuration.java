package me.bennypls.sleeper;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Configuration {
    private final JavaPlugin plugin;
    private final FileConfiguration configuration;
    private List<String> ignoredPlayers;
    private double percentageNecessaryToSleep;
    private boolean isAnimated;
    private int animationSpeed;
    private int animationInterval;
    private String restMessage;
    private String skipNightMessage;

    public Configuration(JavaPlugin plugin, FileConfiguration configuration) {
        this.plugin = plugin;
        this.configuration = configuration;

        loadConfigurations();
    }

    public void reload() {
        loadConfigurations();
    }

    private void loadConfigurations() {
        percentageNecessaryToSleep = configuration.getDouble("percentage-necessary-to-sleep", 0.2);
        ignoredPlayers = configuration.getStringList("ignored-players");
        isAnimated = configuration.getBoolean("is-animated", true);
        animationSpeed = configuration.getInt("animation-speed", 100);
        animationInterval = configuration.getInt("animation-interval", 5);
        restMessage = configuration.getString("rest-message", "");
        skipNightMessage = configuration.getString("skip-night-message", "");
    }

    public List<String> getIgnoredPlayers() {
        return ignoredPlayers;
    }

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

    public double getPercentageNecessaryToSleep() {
        return percentageNecessaryToSleep;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    public int getAnimationInterval() {
        return animationInterval;
    }

    public String getRestMessage() {
        return restMessage;
    }

    public String getSkipNightMessage() {
        return skipNightMessage;
    }
}
