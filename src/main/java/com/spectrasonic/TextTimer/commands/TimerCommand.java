package com.spectrasonic.TextTimer.commands;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.TextTimer.managers.TimerManager;
import com.spectrasonic.Utils.MessageUtils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.entity.Player;

// Comando /tt timer <formato> - Inicia un timer en todos los displays
public class TimerCommand {

    // Sugerencias de formato de tiempo
    private static final String[] TIME_SUGGESTIONS = {"10", "30", "1m", "2m", "1m30s", "5m"};

    
    public static void register(Main plugin) {
        new CommandAPICommand("tt")
                .withSubcommand(new CommandAPICommand("timer")
                        .withArguments(new StringArgument("time")
                                .replaceSuggestions(ArgumentSuggestions.strings(TIME_SUGGESTIONS))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((Player player, dev.jorel.commandapi.CommandArguments args) -> {
                            String timeStr = (String) args.get("time");
                            handleTimer(plugin, player, timeStr);
                        })
                )
                .register(plugin);
    }

    private static void handleTimer(Main plugin, Player player, String timeStr) {
        // Verificar que hay displays disponibles
        if (plugin.getDisplayManager().getDisplayIds().isEmpty()) {
            String msg = plugin.getMessagesManager().getMessage("commands.timer.no-displays");
            MessageUtils.denyMessage(player, msg.replace("{prefix}", ""));
            return;
        }

        // Parsear el tiempo
        int seconds = TimerManager.parseTime(timeStr);
        if (seconds <= 0) {
            String msg = plugin.getMessagesManager().getMessage("commands.timer.invalid-format");
            MessageUtils.denyMessage(player, msg.replace("{prefix}", ""));
            return;
        }

        // Detener timer anterior si existe
        if (plugin.getTimerManager().isActive()) {
            plugin.getTimerManager().stopTimer();
        }

        // Iniciar nuevo timer
        plugin.getTimerManager().startTimer(seconds);
        
        String formatted = TimerManager.formatTime(seconds);
        String msg = plugin.getMessagesManager().getMessage("commands.timer.started");
        MessageUtils.successMessage(player, msg.replace("{prefix}", "").replace("{time}", formatted));
    }
}
