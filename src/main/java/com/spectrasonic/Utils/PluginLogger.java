package com.spectrasonic.Utils;

import org.bukkit.Bukkit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class PluginLogger {

    // Método privado para enviar mensajes con colores usando Adventure API
    private static void log(NamedTextColor color, String message) {
        if (message == null)
            return;

        Component component = Component.text(message).color(color);
        Bukkit.getConsoleSender().sendMessage(component);
    }

    // Mensajes informativos (blanco)
    public static void info(String message) {
        log(NamedTextColor.WHITE, message);
    }

    // Mensajes de éxito (verde)
    public static void success(String message) {
        log(NamedTextColor.GREEN, message);
    }

    // Mensajes de advertencia (amarillo)
    public static void warning(String message) {
        log(NamedTextColor.YELLOW, message);
    }

    // Mensajes de error (rojo)
    public static void error(String message) {
        log(NamedTextColor.RED, message);
    }

    // Mensajes de configuración (azul)
    public static void config(String message) {
        log(NamedTextColor.BLUE, message);
    }

    // Mensajes críticos (rojo oscuro)
    public static void severe(String message) {
        log(NamedTextColor.DARK_RED, message);
    }

    // Mensajes críticos con excepción
    public static void severe(String message, Throwable throwable) {
        log(NamedTextColor.DARK_RED, message + ": " + throwable.getMessage());
    }

    // Mensajes de depuración (gris)
    public static void fine(String message) {
        log(NamedTextColor.GRAY, message);
    }
}