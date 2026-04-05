package com.spectrasonic.TextTimer.managers;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.Utils.MessageUtils;
import com.spectrasonic.Utils.SoundUtils;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Gestor del sistema de temporizadores
@Getter
@SuppressWarnings("all")
public class TimerManager {

    private final Main plugin;
    private BukkitTask currentTask;
    private int remainingSeconds;
    private boolean active;

    public boolean isActive() { return active; }

public TimerManager(Main plugin) {
        this.plugin = plugin;
    }

    // Inicia el timer con los segundos dados, actualiza todos los displays
    public void startTimer(int totalSeconds) {
        // Detener timer anterior si existe
        stopTimer();

        remainingSeconds = totalSeconds;
        active = true;

        // Reproducir sonido de inicio
        playStartSound();

        // Enviar mensaje de inicio
        String timeFormatted = formatTime(totalSeconds);
        String msg = plugin.getMessagesManager().getMessage("commands.timer.started")
                .replace("{time}", timeFormatted);
        MessageUtils.sendConsoleMessage(msg.replace("{prefix}", ""));

        // Tarea que se ejecuta cada segundo (20 ticks)
        currentTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (remainingSeconds <= 0) {
                    onTimerEnd();
                    cancel();
                    return;
                }
                // Actualizar texto en todos los displays
                updateDisplaysWithTime(remainingSeconds);
                remainingSeconds--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    // Detiene el timer actual
    public void stopTimer() {
        if (currentTask != null && !currentTask.isCancelled()) {
            currentTask.cancel();
        }
        active = false;
        currentTask = null;
    }

    // Acciones al finalizar el timer
    private void onTimerEnd() {
        active = false;
        currentTask = null;

        // Mostrar texto final
        String endText = plugin.getConfigManager().getEndText();
        plugin.getDisplayManager().updateAllDisplaysText(
                MiniMessage.miniMessage().deserialize(endText));

        // Reproducir sonido de fin
        playEndSound();

        // Mensaje en consola
        String msg = plugin.getMessagesManager().getMessage("commands.timer.finished");
        MessageUtils.sendConsoleMessage(msg.replace("{prefix}", ""));
    }

    // Actualiza todos los displays con el tiempo formateado
    private void updateDisplaysWithTime(int seconds) {
        String formatted = formatTime(seconds);
        String timerFormat = plugin.getConfigManager().getTimerFormat();
        String finalText = timerFormat.replace("{timer}", formatted);
        plugin.getDisplayManager().updateAllDisplaysText(
                MiniMessage.miniMessage().deserialize(finalText));
    }

    // Parsea un string de tiempo como "1m30s", "2m", "30s" a segundos
    public static int parseTime(String input) {
        if (input == null || input.isEmpty())
            return -1;

        int totalSeconds = 0;
        Pattern pattern = Pattern.compile("(\\d+)([ms])");
        Matcher matcher = pattern.matcher(input.toLowerCase());

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            if (unit.equals("m")) {
                totalSeconds += value * 60;
            } else if (unit.equals("s")) {
                totalSeconds += value;
            }
        }

        // Si no coincide con ningún patrón, intentar como número puro (segundos)
        if (totalSeconds == 0) {
            try {
                totalSeconds = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                return -1; // Formato inválido
            }
        }

        return totalSeconds;
    }

    // Formatea segundos a "MM:SS" o "SS"
    public static String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        if (minutes > 0) {
            return String.format("%02d:%02d", minutes, seconds);
        }
        return String.format("%02d", seconds);
    }

    // Reproduce sonido de inicio a todos los jugadores
    private void playStartSound() {
        try {
            Sound sound = Sound.valueOf(plugin.getConfigManager().getStartSound());
            SoundUtils.broadcastSound(sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            // Sonido no encontrado, ignorar silenciosamente
        }
    }

    // Reproduce sonido de fin a todos los jugadores
    private void playEndSound() {
        try {
            Sound sound = Sound.valueOf(plugin.getConfigManager().getEndSound());
            SoundUtils.broadcastSound(sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            // Sonido no encontrado, ignorar silenciosamente
        }
    }
}
