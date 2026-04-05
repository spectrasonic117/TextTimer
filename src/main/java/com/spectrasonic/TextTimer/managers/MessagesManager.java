package com.spectrasonic.TextTimer.managers;

import com.spectrasonic.TextTimer.Main;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

// Gestor de mensajes del plugin, carga desde messages.yml
@Getter
public class MessagesManager {

    private final Main plugin;
    private FileConfiguration messagesConfig;
    private File messagesFile;

    public MessagesManager(Main plugin) {
        this.plugin = plugin;
        loadMessages();
    }

    // Carga inicial del archivo de mensajes
    public void loadMessages() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            try (InputStream in = plugin.getResource("messages.yml")) {
                if (in != null) {
                    Files.copy(in, messagesFile.toPath());
                }
            } catch (IOException e) {
                plugin.getLogger().severe("No se pudo copiar messages.yml: " + e.getMessage());
            }
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    // Recarga solo la configuración, sin copiar el archivo
    public void reloadMessages() {
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    // Obtiene un mensaje por clave con fallback en formato MiniMessage
    public String getMessage(String key) {
        return messagesConfig.getString(key, "<red>Message not found: " + key + "</red>");
    }
}
