package com.spectrasonic.TextTimer.commands;

import com.spectrasonic.TextTimer.Main;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TextTimerCommand {

    public static void register(Main plugin) {
        new CommandAPICommand("texttimer")
                .withAliases("tt")
                // Subcomando create
                .withSubcommand(new CommandAPICommand("create")
                        .withArguments(new StringArgument("id"))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((Player player, CommandArguments args) -> {
                            String id = (String) args.get("id");
                            CreateCommand.handleCreate(plugin, player, id);
                        })
                )
                // Subcomando edit
                .withSubcommand(new CommandAPICommand("edit")
                        .withArguments(EditCommand.createIdArgument(plugin))
                        .withArguments(new StringArgument("param")
                                .replaceSuggestions(ArgumentSuggestions.strings("move", "billboard", "size", "rotation", "render")))
                        .withArguments(new StringArgument("value")
                                .replaceSuggestions(ArgumentSuggestions.strings(info -> {
                                    String param = (String) info.previousArgs().get("param");
                                    if (param != null && param.equalsIgnoreCase("billboard")) {
                                        return new String[]{"FIXED", "VERTICAL", "HORIZONTAL", "CENTER"};
                                    }
                                    return new String[0];
                                })))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((Player player, CommandArguments args) -> {
                            String id = (String) args.get("id");
                            String param = (String) args.get("param");
                            String value = (String) args.get("value");
                            EditCommand.handleEdit(plugin, player, id, param, value);
                        })
                )
                // Subcomando reload
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission(CommandPermission.OP)
                        .executes((CommandSender sender, CommandArguments args) -> {
                            ReloadCommand.handleReload(plugin, sender);
                        })
                )
                // Subcomando remove
                .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(RemoveCommand.createIdArgument(plugin))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((Player player, CommandArguments args) -> {
                            String id = (String) args.get("id");
                            RemoveCommand.handleRemove(plugin, player, id);
                        })
                )
                // Subcomando tphere
                .withSubcommand(new CommandAPICommand("tphere")
                        .withArguments(TphereCommand.createIdArgument(plugin))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((Player player, CommandArguments args) -> {
                            String id = (String) args.get("id");
                            TphereCommand.handleTphere(plugin, player, id);
                        })
                )
                // Subcomando timer
                .withSubcommand(new CommandAPICommand("timer")
                        .withArguments(new StringArgument("time")
                                .replaceSuggestions(ArgumentSuggestions.strings(TimerCommand.TIME_SUGGESTIONS)))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((Player player, CommandArguments args) -> {
                            String timeStr = (String) args.get("time");
                            TimerCommand.handleTimer(plugin, player, timeStr);
                        })
                )
                .register(plugin);
    }
}
