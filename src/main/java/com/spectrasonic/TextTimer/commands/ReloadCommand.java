package com.spectrasonic.TextTimer.commands;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.Utils.MessageUtils;
import com.spectrasonic.Utils.PluginLogger;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import org.bukkit.command.CommandSender;

// Comando /tt reload - Recarga config, mensajes y actualiza displays
public class ReloadCommand {

    public static void register(Main plugin) {
        new CommandAPICommand("tt")
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission(CommandPermission.OP)
                        .executes((CommandSender sender, dev.jorel.commandapi.CommandArguments args) -> {
                            handleReload(plugin, sender);
                        })
                )
                .register(plugin);
    }

    private static void handleReload(Main plugin, CommandSender sender) {
        try {
            // Recargar configuración
            plugin.getConfigManager().loadConfig();

            // Recargar mensajes
            plugin.getMessagesManager().reloadMessages();

            // Actualizar todos los displays con los datos frescos de config
            plugin.getDisplayManager().updateAllDisplays();

            PluginLogger.success("Configuración recargada y displays actualizados.");

            String msg = plugin.getMessagesManager().getMessage("commands.reload.success");
            MessageUtils.successMessage(sender, msg.replace("{prefix}", ""));
        } catch (Exception e) {
            PluginLogger.severe("Error al recargar configuración: " + e.getMessage());
            String msg = plugin.getMessagesManager().getMessage("commands.reload.error");
            MessageUtils.warningMessage(sender, msg.replace("{prefix}", ""));
        }
    }
}
