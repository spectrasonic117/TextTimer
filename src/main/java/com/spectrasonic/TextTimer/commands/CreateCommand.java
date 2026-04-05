package com.spectrasonic.TextTimer.commands;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.Utils.MessageUtils;
import org.bukkit.entity.Player;


// Comando /tt create <id> - Crea un nuevo TextDisplay
public class CreateCommand {

    // Registro del subcomando se realiza en TextTimerCommand
    static void handleCreate(Main plugin, Player player, String id) {
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
