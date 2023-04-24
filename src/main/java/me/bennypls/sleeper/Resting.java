package me.bennypls.sleeper;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <h1>
 * Resting
 *
 * <p>
 * The Resting class provides functionality for skipping the night in a Bukkit server.
 *
 * <p>
 * It allows players to skip the night with not all the players asleep, and also provides an option for an animated transition to morning.
 */
public class Resting {
    /** The plugin instance that this Resting instance is associated with. */
    private final JavaPlugin plugin;
    /** The configuration for this Resting instance. */
    private final Configuration configuration;
    /**
     * The time at which sunrise occurs in Minecraft. This is a constant value that is used to
     * calculate the time difference between the current time and sunrise time in order to determine
     * the different necessary intervals also determined the length of intervals by the animation speed.
     */
    private static final long SUNRISE_TIME = 23850;
    /**
     * The time at which nightfall occurs in Minecraft.This is a constant value that is used to
     * calculate the time to know if is night.
     */
    private static final long NIGHTFALL_TIME = 13000;
    /** A static variable to know if it is currently playing a skip-night Animation */
    private static final Map<World, Boolean> isSkippingNight = new HashMap<>();

    /**
     * <h1>
     * Resting Constructor
     *
     * <p>
     * Creates a new Resting instance with the given plugin instance and configuration.
     *
     * @param plugin        the plugin instance to associate with this Resting instance.
     * @param configuration the configuration to use for this Resting instance.
     */
    public Resting(JavaPlugin plugin, Configuration configuration) {
        this.plugin = plugin;
        this.configuration = configuration;
    }

    /**
     * <h1>
     * Skip Night
     *
     * <p>
     * Skips the night for the given world if the conditions are meet.
     * The configured message is executed, and an animation may be played
     * if the configuration specifies it.
     * Will not skip the night if {@link Resting#isSkippingNight} is true.
     * <p>
     * Can be forcefully skip night if forced is true;
     *
     * @param world the world to skip the night for.
     * @param forced forcefully skip night ignoring conditions.
     * @return true if the night was skipped, false otherwise.
     */
    public boolean skipNight(World world, boolean forced) {
        if (isSkippingNight.getOrDefault(world, false)) {
            return false;
        }

        if (!canSkipNight(world) && forced) {
            return false;
        }

        executeCommand(configuration.getSkipNightMessage());

        if (configuration.isAnimated()) {
            skipNightAnimation(world);
        } else {
            world.setTime(SUNRISE_TIME);
        }

        return true;
    }

    /**
     * <h1>
     * Can Skip Night
     *
     * <p>
     * Looks if can skip the night for the world if the conditions are met.
     *
     * @param world the world to look if it can skip the night.
     * @return true if the night can be skipped, false otherwise.
     */
    public boolean canSkipNight(World world) {
        if (isNight(world) && isClear(world)) {
            return true;
        }

        if (isClear(world) && !(isNight(world))) {
            return false;
        }

        return configuration.canSkipWeather();
    }

    /**
     * <h1>
     * isNight
     *
     * <p>
     * This is to know if in a world given is night time.
     *
     * @param world The world to know the time.
     * @return true if is night, false otherwise.
     */
    private static boolean isNight(World world) {
        return world.getTime() < NIGHTFALL_TIME;
    }

    /**
     * <h1>
     * isClear
     *
     * <p>
     * Determines whether the weather in the given world is clear, meaning that there is no storm or
     * thunderstorm occurring.
     *
     * @param world the world to check for clear weather.
     * @return true if the weather is clear, false otherwise.
     */
    private static boolean isClear(World world) {
        return !(world.isThundering());
    }

    /**
     * <h1>
     * skipNightAnimation
     *
     * <p>
     * Plays an animation to skip the night for the given world. The animation consists of a series
     * of time changes that gradually advance the time to sunrise. The speed and interval of the
     * animation are determined by the configuration.
     *
     * @param world the world for which to play the skip night animation.
     */
    public void skipNightAnimation(World world) {
        var totalIntervals = Math.abs(world.getTime() - SUNRISE_TIME) / configuration.getAnimationSpeed();
        isSkippingNight.put(world, true);

        for (int i = 0; i < totalIntervals - 1; i++) {
            Bukkit.getScheduler().runTaskLater(
                plugin,
                () -> world.setTime(world.getTime() + configuration.getAnimationSpeed()),
                (long) configuration.getAnimationInterval() * i
            );
        }

        Bukkit.getScheduler().runTaskLater(
            plugin,
            () -> {
                world.setTime(world.getTime() + configuration.getAnimationSpeed());
                if (!isClear(world) && configuration.canSkipWeather()) {
                    world.setClearWeatherDuration(new Random().nextInt(1200, 24000));
                }
                isSkippingNight.put(world, false);
            },
            (long) configuration.getAnimationInterval() * (totalIntervals - 1)
        );
    }

    /**
     * <h1>
     * getTotalNecessaryToSkip
     *
     * <p>
     * Determines the total number of players necessary for sleeping to be skipped for a given world.
     *
     * @param world The world to calculate from.
     * @return the total number of players necessary for sleeping to be skipped
     */
    public int getTotalNecessaryToSkip(World world) {
        Collection<? extends Player> players = world.getPlayers();
        int totalPlayers = 0;

        for (Player player : players) {
            if (!configuration.getIgnoredPlayers().contains(player.getName())) {
                totalPlayers++;
            }
        }

        double necessaryPlayersToSkip = totalPlayers * configuration.getPercentageNecessaryToSleep();

        return necessaryPlayersToSkip <= 0 ? 1 : (int) Math.ceil(necessaryPlayersToSkip);
    }

    /**
     * <h1>
     * Execute Command
     *
     * <p>
     * Executes a command in the Minecraft server console.
     *
     * @param command the command to execute
     */
    public void executeCommand(String command) {
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
    }

}
