package me.bennypls.sleeper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SleeperCommand implements CommandExecutor {
    private final Resting resting;
    private final Configuration configuration;

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

    private static final String SKIP_HELP = """
        This is a sub-command of sleeper that allows administrators skip a night and advance to sunrise.
        There needs to be at least 1000 ticks of time difference between the sunrise time (23850) to execute.
        
        WARNING : DO NOT EXECUTE MULTIPLE TIMES RAPIDLY WITH ANIMATION ENABLED CAN CAUSE PROBLEMS.
        """;

    private static final String RELOAD_HELP = """
        This is a sub-command of sleeper that allows administrators to hot reload the sleeper configuration.
        """;

    private static final String IGNORED_HELP = """
        This is a sub-command of sleeper that allows administrators to add or remove players from a list of ignored players.
        
        Usage : /sleeper ignored [add | remove] (PLAYER USERNAME)
        
        These players will not be counted towards the total number of players needed to sleep through the night.
        """;

    public SleeperCommand(Resting resting, Configuration configuration) {
        this.resting = resting;
        this.configuration = configuration;
    }

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
                return false;
            }
        }
    }

    private boolean skip(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length != 1) {
            player.sendMessage("Unnecessary arguments");
            return false;
        }

        if (!resting.skipNight(player)) {
            player.sendMessage("Too close to sunrise, wait more...");
        }

        return true;
    }

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

    private boolean add(CommandSender sender, String playerName) {
        boolean success = configuration.addIgnoredPlayer(playerName);

        if (success) {
            sender.sendMessage("The player - " + playerName + " - has been added to the ignored list.");
        } else {
            sender.sendMessage("The player with name - " + playerName + " - is already in the ignored list.");
        }

        return success;
    }

    private boolean remove(CommandSender sender, String playerName) {
        boolean success = configuration.removeIgnoredPlayer(playerName);

        if (success) {
            sender.sendMessage("The player - " + playerName + " - has been removed from the ignored list.");
        } else {
            sender.sendMessage("The player with name - " + playerName + " - not found in ignored list.");
        }

        return success;
    }

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
