package me.bennypls.sleeper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SleeperCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final Resting resting;
    private final Configuration configuration;

    public SleeperCommand(JavaPlugin plugin, Resting resting, Configuration configuration) {
        this.plugin = plugin;
        this.resting = resting;
        this.configuration = configuration;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] commandArguments) {

        if (commandArguments.length == 0) {
            commandSender.sendMessage("This command need at least 1 argument.");
            return false;
        }

        switch (commandArguments[0].toLowerCase()) {
            case "skip" -> {
                return skip(commandSender, commandArguments);
            }

            case "reload" -> {
                return reload(commandSender, commandArguments);
            }

            case "ignored" -> {
                return ignoredPlayers(commandSender, commandArguments);
            }

            default -> {
                return false;
            }
        }
    }

    private boolean skip(CommandSender commandSender, String[] commandArguments) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        if (commandArguments.length != 1) {
            player.sendMessage("Unnecessary arguments");
            return false;
        }

        if (!resting.skipNight(player)) {
            player.sendMessage("It's already day!");
        }

        return true;
    }

    private boolean reload(CommandSender commandSender, String[] commandArguments) {
        if (commandArguments.length != 1) {
            commandSender.sendMessage("Unnecessary arguments");
            return false;
        }

        commandSender.sendMessage("Sleeper : Starting configuration reload...");
        configuration.reload();
        commandSender.sendMessage("Sleeper : Reload completed!");

        return true;
    }

    public boolean ignoredPlayers(CommandSender commandSender, String[] commandArguments) {
        if (commandArguments.length != 3) {
            commandSender.sendMessage("This command needs 3 arguments");
            return false;
        }

        Player target = commandSender.getServer().getPlayer(commandArguments[2]);

        if (target == null) {
            commandSender.sendMessage(commandSender.getServer().getOnlinePlayers().toString());
            commandSender.sendMessage("The player does not exist");
            return false;
        }

        switch (commandArguments[1].toLowerCase()) {
            case "add" -> {
                return add(commandSender, target.getName());
            }

            case "remove" -> {
                return remove(commandSender, target.getName());
            }

            default -> {
                commandSender.sendMessage(commandArguments[1] + " is not a valid action.");
                return false;
            }
        }
    }

    private boolean add(CommandSender commandSender, String playerName) {
        boolean success = configuration.addIgnoredPlayer(playerName);

        if (success) {
            commandSender.sendMessage("The player - " + playerName + " - has been added to the ignored list.");
        } else {
            commandSender.sendMessage("The player with name - " + playerName + " - is already in the ignored list.");
        }

        return success;
    }

    private boolean remove(CommandSender commandSender, String playerName) {
        boolean success = configuration.removeIgnoredPlayer(playerName);

        if (success) {
            commandSender.sendMessage("The player - " + playerName + " - has been removed from the ignored list.");
        } else {
            commandSender.sendMessage("The player with name - " + playerName + " - not found in ignored list.");
        }

        return success;
    }
}
