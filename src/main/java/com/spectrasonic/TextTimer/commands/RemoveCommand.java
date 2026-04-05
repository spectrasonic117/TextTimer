package com.spectrasonic.TextTimer.commands;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.Utils.MessageUtils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.entity.Player;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.executors.CommandArguments;

// Comando /tt remove <id> - Elimina un TextDisplay
public class RemoveCommand {

    public static void register(Main plugin) {
        new CommandAPICommand("tt")
                .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(createIdArgument(plugin))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((Player player, CommandArguments args) -> {
                            handleRemove(plugin, player, (String) args.get("id"));
                        }))
                .register(plugin);
    }

    private static void handleRemove(Main plugin, Player player, String id) {
        if (!plugin.getDisplayManager().hasDisplay(id)) {
            String msg = plugin.getMessagesManager().getMessage("commands.remove.not-found");
            MessageUtils.denyMessage(player, msg.replace("{prefix}", "").replace("{id}", id));
            return;
        }

        plugin.getDisplayManager().removeDisplay(id);

        String msg = plugin.getMessagesManager().getMessage("commands.remove.success");
        MessageUtils.successMessage(player, msg.replace("{prefix}", "").replace("{id}", id));
    }

    // Argumento con sugerencias de IDs existentes
    private static StringArgument createIdArgument(Main plugin) {
        return (StringArgument) new StringArgument("id")
                .replaceSuggestions(ArgumentSuggestions.strings(
                        info -> plugin.getDisplayManager().getDisplayIds().toArray(new String[0])));
    }
}
