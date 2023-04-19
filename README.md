# 🛏 Sleeper 🛏

# ⚠ WARNING ⚠

> For certain circumstances, I can't test myself this plugin regularly,
> For that I need FEEDBACK on problems I don't know.
> Feature Request and IDEAS 

## ❮ Introduction ❯

Sleeper is a Spigot plugin that redefines how sleep works in your server, making it easier for all online players to get
in bed quickly and skip through the night.

## ✧ Features ✧

While this plugin may have limited features, its advantage lies in its lightweight nature.

- Skipping of nighttime through partial player-based activation.
- An Ignored List that doesn't count toward the player-based night-skipping
- Skip Night Animation.
- Configuration options for messages, percentage for skip, and animation speed.
- When Skip Night and 

## ⛶ Commands ⛶

There is one command available, which is only permitted for administrators (OPs) by default.
The command is `/sleeper` and it has several sub-commands including `skip`, `reload`, `ignored`, and `help`.

The `skip` sub-command allows administrators to skip the night and advance to sunrise.

The `reload` sub-command allows administrators to reload the configuration of the sleeping plugin.

The `ignored` sub-command enables administrators to add or remove players from a list of ignored players.

Finally, the `help` sub-command provides general assistance or help on a specific sub-command.

## 🗀 Installation 🗀

Here's a concise guide on how to install the Sleeper plugin on a Minecraft server running version 1.19.4:

1. Download the Sleeper plugin.
2. Locate your server's `plugins` folder.
3. Drag and drop the downloaded Sleeper plugin file into the `plugins` folder.
4. Restart your Minecraft server.
5. Verify that the Sleeper plugin has been successfully installed by typing the command `/plugins` in the server console
   or in-game chat.

That's it! The Sleeper plugin should now be up and running on your Minecraft server, ready for you to configure and use
as desired.

## 🖹 Configuration 🖹

The configuration file described above can be found in the `config.yml` file, located inside the `sleeper` folder within
the plugin's directory.

It contains various settings related to the animation, sleep configuration, and messages for the plugin.

The `is-animated` setting is set to true, which means that an animation will be played when players sleep. The
`animation-speed` and `animation-interval` are set to 125 and 1 ticks, respectively.

The `percentage-necessary-to-sleep` setting is set to 0.25, meaning that at least 25% of the players need to be sleeping
for the night to be skipped.

The `ignored-players` setting is empty by default but can be filled with the names of players who should not be counted
towards the percentage needed for sleeping.

The `can-skip-weather` setting is set to true, which means that by default skip night works when is raining.

The `rest-message` setting contains a message that will be executed as a command by the console. This message displays
the number of players who are currently sleeping and the necessary number of players needed to skip the night.

The `skip-night-message` setting contains a message that will be executed as a command by the console when the night is
skipped.The "{playerName}", "{actual}", "{necessary}", are variables, actually are only available in 'rest-message'.

The `cannot-skip-night-message` setting contains a message that will be executed as a command by the console when the night cannot be
skipped.