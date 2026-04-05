package com.spectrasonic.TextTimer.commands;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.Utils.MessageUtils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.entity.Player;
import dev.jorel.commandapi.executors.CommandArguments;

// Comando /tt create <id> - Crea un nuevo TextDisplay
public class CreateCommand {

    public static void register(Main plugin) {
        new CommandAPICommand("tt")
                .withSubcommand(new CommandAPICommand("create")
                        .withArguments(new StringArgument("id"))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((Player player, CommandArguments args) -> {
                            handleCreate(plugin, player, (String) args.get("id"));
                        })
                )
                .register(plugin);
    }

    private static void handleCreate(Main plugin, Player player, String id) {
        // Validar ID
        if (id == null || id.trim().isEmpty()) {
            String msg = plugin.getMessagesManager().getMessage("commands.create.invalid-id");
            MessageUtils.warningMessage(player, msg.replace("{prefix}", ""));
            return;
        }

        // Verificar si ya existe
        if (plugin.getDisplayManager().hasDisplay(id)) {
            String msg = plugin.getMessagesManager().getMessage("commands.create.already-exists");
            MessageUtils.denyMessage(player, msg.replace("{prefix}", "").replace("{id}", id));
            return;
        }

        // Crear el display en la posición del jugador
        plugin.getDisplayManager().createDisplay(id, player.getLocation());

        String msg = plugin.getMessagesManager().getMessage("commands.create.success");
        MessageUtils.successMessage(player, msg.replace("{prefix}", "").replace("{id}", id));
    }
}
