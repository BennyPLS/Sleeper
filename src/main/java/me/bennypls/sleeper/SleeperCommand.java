package me.bennypls.sleeper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * <h1>
 * SleeperCommand
 *
 * <p>
 * The SleeperCommand class implements the CommandExecutor interface and handles the commands for the Sleeper plugin.
 *
 * <p>
 * It has sub-commands such as skip, reload, ignored, and help.
 * Skip allows administrators to skip the night and advance to sunrise.
 * Reload allows administrators to reload the configuration of the sleeping plugin.
 * Ignored allows administrators to add or remove players from a list of ignored players.
 * Help provides general help or help on a specific sub-command.
 */
public class SleeperCommand implements CommandExecutor {
    /** An instance of the Resting class that is used to skip the night. */
    private final Resting resting;
    /** An instance of the Configuration class that is used to manage the plugin's configuration. */
    private final Configuration configuration;
    /** A string containing general help information for the Sleeper plugin. */
    private static final String GENERAL_HELP = """
        This is a command for managing the sleeper plugin.
                            
        The command has several sub-commands, each of which performs a different action.
        The sub-commands are :
        · skip      · allows administrators to skip the night and advance to sunrise.
        · reload    · allows administrators to reload the configuration of the sleeping plugin.
        · ignored   · allows administrators to add or remove players from a list of ignored players.
        · help      · get help for the general command or especific sub-command.
            
        For specific help type (/sleeper help [sub-command] )
        """;
    /** A string containing help information for the skip sub-command. */
    private static final String SKIP_HELP = """
        This is a sub-command of sleeper that allows administrators skip a night and advance to sunrise.
        There needs to be at least 1000 ticks of time difference between the sunrise time (23850) to execute.
                
        WARNING : DO NOT EXECUTE MULTIPLE TIMES RAPIDLY WITH ANIMATION ENABLED CAN CAUSE PROBLEMS.
        """;
    /** A string containing help information for the reload sub-command. */
    private static final String RELOAD_HELP = """
        This is a sub-command of sleeper that allows administrators to hot reload the sleeper configuration.
        """;
    /** A string containing help information for the ignored sub-command. */
    private static final String IGNORED_HELP = """
        This is a sub-command of sleeper that allows administrators to add or remove players from a list of ignored players.
                
        Usage : /sleeper ignored [add | remove] (PLAYER USERNAME)
                
        These players will not be counted towards the total number of players needed to sleep through the night.
        """;

    /**
     * <h1>
     * SleeperCommand Constructor
     *
     * <p>
     * Constructs a new SleeperCommand with the specified Resting and Configuration instances.
     *
     * @param resting       the Resting instance to use for skipping the night
     * @param configuration the Configuration instance to use for managing the plugin's configuration
     */
    public SleeperCommand(Resting resting, Configuration configuration) {
        this.resting = resting;
        this.configuration = configuration;
    }

    /**
     * <h1>
     * onCommand
     *
     * <p>
     * Executes the specified command with the given and arguments.
     *
     * @param sender  the CommandSender that sent the command
     * @param command the Command that was executed
     * @param label   the label used to execute the command
     * @param args    the arguments used to execute the command
     * @return true if the command was executed successfully, false otherwise
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("This command need at least 1 argument.");
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "skip" -> {
                return skip(sender, args);
            }

            case "reload" -> {
                return reload(sender, args);
            }

            case "ignored" -> {
                return ignoredPlayers(sender, args);
            }

            case "help" -> {
                return help(sender, args);
            }

            default -> {
                sender.sendMessage("Unknown sub-command.");
                return false;
            }
        }
    }

    /**
     * <h1>
     * skip
     *
     * <p>
     * Allows a player to skip the night or day and going to the next sunrise.
     *
     * @param sender the CommandSender executing the command
     * @param args   the arguments passed to the command
     * @return true if the player is able to skip the night, false otherwise
     */
    private boolean skip(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length != 1) {
            player.sendMessage("Unnecessary arguments");
            return false;
        }

        resting.skipNight(player.getWorld());

        return true;
    }

    /**
     * <h1>
     * reload
     *
     * <p>
     * Reloads the configuration file.
     *
     * @param sender the CommandSender executing the command
     * @param args   the arguments passed to the command
     * @return true if the configuration was successfully reloaded, false otherwise
     */
    private boolean reload(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Unnecessary arguments");
            return false;
        }

        sender.sendMessage("Sleeper : Starting configuration reload...");
        configuration.reload();
        sender.sendMessage("Sleeper : Reload completed!");

        return true;
    }

    /**
     * <h1>
     * ignoredPlayers
     *
     * <p>
     * This method manages the sub-command ignored that allows the sender to add or remove players to/from the ignored list.
     *
     * @param sender the CommandSender who executed the command
     * @param args   the array of arguments passed with the command
     * @return true if the command was executed successfully, false otherwise
     */
    public boolean ignoredPlayers(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("This command needs 3 arguments");
            return false;
        }

        Player target = sender.getServer().getPlayer(args[2]);

        if (target == null) {
            sender.sendMessage(sender.getServer().getOnlinePlayers().toString());
            sender.sendMessage("The player does not exist");
            return false;
        }

        switch (args[1].toLowerCase()) {
            case "add" -> {
                return add(sender, target.getName());
            }

            case "remove" -> {
                return remove(sender, target.getName());
            }

            default -> {
                sender.sendMessage(args[1] + " is not a valid action.");
                return false;
            }
        }
    }

    /**
     * <h1>
     * add
     *
     * <p>
     * This method adds the specified player to the ignored list and returns true if successful, false otherwise.
     *
     * @param sender     the CommandSender who executed the command
     * @param playerName the name of the player to add to the ignored list
     * @return true if the player was added successfully, false otherwise
     */
    private boolean add(CommandSender sender, String playerName) {
        boolean success = configuration.addIgnoredPlayer(playerName);

        if (success) {
            sender.sendMessage("The player - " + playerName + " - has been added to the ignored list.");
        } else {
            sender.sendMessage("The player with name - " + playerName + " - is already in the ignored list.");
        }

        return success;
    }

    /**
     * <h1>
     * remove
     *
     * <p>
     * This method removes the specified player from the ignored list and returns true if successful, false otherwise.
     *
     * @param sender     the CommandSender who executed the command
     * @param playerName the name of the player to remove from the ignored list
     * @return true if the player was removed successfully, false otherwise
     */
    private boolean remove(CommandSender sender, String playerName) {
        boolean success = configuration.removeIgnoredPlayer(playerName);

        if (success) {
            sender.sendMessage("The player - " + playerName + " - has been removed from the ignored list.");
        } else {
            sender.sendMessage("The player with name - " + playerName + " - not found in ignored list.");
        }

        return success;
    }

    /**
     * <h1>
     * help
     *
     * <p>
     * This class method handles the sub-command help that provides information on the available sub-commands and their usage.
     *
     * @param sender the CommandSender who executed the command
     * @param args   the array of arguments passed with the command
     * @return true if the command was executed successfully, false otherwise
     */
    private boolean help(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(GENERAL_HELP);
            return true;
        }

        switch (args[1]) {
            case "skip" -> {
                sender.sendMessage(SKIP_HELP);
                return true;
            }

            case "reload" -> {
                sender.sendMessage(RELOAD_HELP);
                return true;
            }

            case "ignored" -> {
                sender.sendMessage(IGNORED_HELP);
                return true;
            }

            default -> {
                sender.sendMessage("This is not a valid sub-command to get help of.");
                return false;
            }
        }

    }
}
