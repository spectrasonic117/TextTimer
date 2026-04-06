package com.spectrasonic.TextTimer.managers;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.TextTimer.models.TextDisplayData;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

// Gestor de configuración central del plugin
@Getter
public class ConfigManager {

    private final Main plugin;
    private FileConfiguration config;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    // Carga la configuración desde disco
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public void saveConfig() {
        plugin.saveConfig();
    }

    // --- Configuración global del display ---

    public String getTimerFormat() {
        return config.getString("default-display.timer-format", "<white>{timer}</white>");
    }

    public String getEndText() {
        return config.getString("timer.end-text", "<red>00:00</red>");
    }

    public boolean isBackgroundEnabled() {
        return config.getBoolean("default-display.background.enabled", true);
    }

    public Color getBackgroundColor() {
        String hex = config.getString("default-display.background.color", "#000000");
        int opacity = config.getInt("default-display.background.opacity", 128);
        int rgb = Integer.parseInt(hex.replace("#", ""), 16);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return Color.fromARGB(opacity, r, g, b);
    }

    public float getDefaultViewRange() {
        return (float) config.getDouble("default-display.visibility.view-range", 16.0);
    }

    public boolean isSeeThrough() {
        return config.getBoolean("default-display.visibility.see-through", false);
    }

    public String getDefaultBillboard() {
        return config.getString("default-display.appearance.billboard", "CENTER");
    }

    public float getDefaultWidth() {
        return (float) config.getDouble("default-display.appearance.width", 2.0);
    }

    public float getDefaultHeight() {
        return (float) config.getDouble("default-display.appearance.height", 0.5);
    }

    public boolean isShadowed() {
        return config.getBoolean("default-display.appearance.shadowed", false);
    }

    public String getStartSound() {
        return config.getString("timer.start-sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
    }

    public String getEndSound() {
        return config.getString("timer.end-sound", "ENTITY_WITHER_DEATH");
    }

    // --- Gestión de displays individuales ---

    // Obtiene los IDs de todos los displays guardados
    public Set<String> getDisplayIds() {
        ConfigurationSection section = config.getConfigurationSection("displays");
        if (section == null) return Collections.emptySet();
        return section.getKeys(false);
    }

    // Verifica si existe un display con el ID dado
    public boolean hasDisplay(String id) {
        return config.contains("displays." + id);
    }

    // Carga los datos de un display desde la configuración
    public TextDisplayData getDisplayData(String id) {
        String base = "displays." + id;
        return new TextDisplayData(
                id,
                config.getString(base + ".world", "world"),
                config.getDouble(base + ".x", 0),
                config.getDouble(base + ".y", 64),
                config.getDouble(base + ".z", 0),
                (float) config.getDouble(base + ".yaw", 0),
                (float) config.getDouble(base + ".pitch", 0),
                config.getString(base + ".billboard", getDefaultBillboard()),
                (float) config.getDouble(base + ".width", getDefaultWidth()),
                (float) config.getDouble(base + ".height", getDefaultHeight()),
                (float) config.getDouble(base + ".view-range", getDefaultViewRange())
        );
    }

    // Guarda los datos de un display en la configuración
    public void saveDisplayData(TextDisplayData data) {
        String base = "displays." + data.getId();
        config.set(base + ".world", data.getWorld());
        config.set(base + ".x", data.getX());
        config.set(base + ".y", data.getY());
        config.set(base + ".z", data.getZ());
        config.set(base + ".yaw", data.getYaw());
        config.set(base + ".pitch", data.getPitch());
        config.set(base + ".billboard", data.getBillboard());
        config.set(base + ".width", data.getWidth());
        config.set(base + ".height", data.getHeight());
        config.set(base + ".view-range", data.getViewRange());
        saveConfig();
    }

    // Elimina un display de la configuración
    public void removeDisplay(String id) {
        config.set("displays." + id, null);
        saveConfig();
    }

    // Carga todos los displays guardados como mapa de datos
    public Map<String, TextDisplayData> loadAllDisplays() {
        Map<String, TextDisplayData> map = new LinkedHashMap<>();
        for (String id : getDisplayIds()) {
            map.put(id, getDisplayData(id));
        }
        return map;
    }
    
    // Proporciona acceso directo al FileConfiguration para ediciones directas
    public FileConfiguration getConfig() {
        return config;
    }
}
