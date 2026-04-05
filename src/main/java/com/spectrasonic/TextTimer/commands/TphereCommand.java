package com.spectrasonic.TextTimer.commands;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.Utils.MessageUtils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import org.bukkit.entity.Player;
import dev.jorel.commandapi.executors.CommandArguments;

// Comando /tt tphere <id> - Mueve un display a la posición del jugador
public class TphereCommand {

    public static void register(Main plugin) {
        new CommandAPICommand("tt")
                .withSubcommand(new CommandAPICommand("tphere")
                        .withArguments(createIdArgument(plugin))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((Player player, CommandArguments args) -> {
                            handleTphere(plugin, player, (String) args.get("id"));
                        }))
                .register(plugin);
    }

    private static void handleTphere(Main plugin, Player player, String id) {
        if (!plugin.getDisplayManager().hasDisplay(id)) {
            String msg = plugin.getMessagesManager().getMessage("commands.tphere.not-found");
            MessageUtils.denyMessage(player, msg.replace("{prefix}", "").replace("{id}", id));
            return;
        }

        plugin.getDisplayManager().teleportDisplay(id, player.getLocation());

        String msg = plugin.getMessagesManager().getMessage("commands.tphere.success");
        MessageUtils.successMessage(player, msg.replace("{prefix}", "").replace("{id}", id));
    }

    // Argumento con sugerencias de IDs existentes
    private static StringArgument createIdArgument(Main plugin) {
        return (StringArgument) new StringArgument("id")
                .replaceSuggestions(ArgumentSuggestions.strings(
                        info -> plugin.getDisplayManager().getDisplayIds().toArray(new String[0])));
    }
}
