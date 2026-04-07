package com.spectrasonic.TextTimer.managers;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.TextTimer.models.TextDisplayData;
import com.spectrasonic.Utils.PluginLogger;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// Gestor de entidades TextDisplay en el mundo
@Getter
public class DisplayManager {

    private final Main plugin;
    private final Map<String, TextDisplay> activeDisplays = new HashMap<>();
    private final NamespacedKey displayKey;

    public DisplayManager(Main plugin) {
        this.plugin = plugin;
        this.displayKey = new NamespacedKey(plugin, "texttimer_id");
    }

    // Crea un nuevo TextDisplay en la ubicación dada
    public TextDisplay createDisplay(String id, Location location) {
        World world = location.getWorld();
        if (world == null)
            return null;

        // Usar spawn() con callback para mejor compatibilidad con el cliente
        TextDisplay display = world.spawn(location, TextDisplay.class, entity -> {
            applyDefaultSettings(entity);
            String defaultText = plugin.getConfigManager().getTimerFormat().replace("{timer}", "00:00");
            entity.text(MiniMessage.miniMessage().deserialize(defaultText));
            entity.getPersistentDataContainer().set(displayKey, PersistentDataType.STRING, id);
            entity.setInterpolationDelay(0);
            entity.setInterpolationDuration(0);
        });

        activeDisplays.put(id, display);

        // Guardar en configuración
        ConfigManager cm = plugin.getConfigManager();
        TextDisplayData data = new TextDisplayData(
                id,
                world.getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                cm.getDefaultBillboard(),
                cm.getDefaultWidth(),
                cm.getDefaultHeight(),
                cm.getDefaultViewRange()
        );
        cm.saveDisplayData(data);

        return display;
    }

    // Elimina un display del mundo y de la configuración
    public void removeDisplay(String id) {
        TextDisplay display = activeDisplays.remove(id);
        if (display != null && !display.isDead()) {
            display.remove();
        }
        plugin.getConfigManager().removeDisplay(id);
    }

    // Obtiene un display por su ID
    public Optional<TextDisplay> getDisplay(String id) {
        TextDisplay display = activeDisplays.get(id);
        if (display == null || !display.isValid() || display.isDead()) {
            activeDisplays.remove(id);
            return Optional.empty();
        }
        return Optional.of(display);
    }

    // Teletransporta un display a una ubicación
    public void teleportDisplay(String id, Location location) {
        getDisplay(id).ifPresent(display -> {
            display.teleport(location);
            forceClientUpdate(display);
            updateConfigLocation(id, location);
        });
    }

    // Mueve un display relativamente desde su posición actual
    public void moveDisplayRelative(String id, double dx, double dy, double dz) {
        getDisplay(id).ifPresent(display -> {
            Location loc = display.getLocation().add(dx, dy, dz);
            display.teleport(loc);
            forceClientUpdate(display);
            updateConfigLocation(id, loc);
        });
    }

    // Establece el billboard de un display
    public void setBillboard(String id, Display.Billboard billboard) {
        getDisplay(id).ifPresent(display -> {
            display.setBillboard(billboard);
            forceClientUpdate(display);
            plugin.getConfigManager().getConfig().set(
                    "displays." + id + ".billboard", billboard.name());
            plugin.getConfigManager().saveConfig();
        });
    }

    // Establece el tamaño del display usando Transformation para un control preciso de escala
    public void setSize(String id, float width, float height) {
        getDisplay(id).ifPresent(display -> {
            display.setBillboard(Display.Billboard.FIXED);

            Transformation scale = new Transformation(
                    new Vector3f(0, 0, 0),
                    new AxisAngle4f(0, 0, 0, 0),
                    new Vector3f(width, height, 1f),
                    new AxisAngle4f(0, 0, 0, 0)
            );
            display.setTransformation(scale);
            forceClientUpdate(display);

            plugin.getConfigManager().getConfig().set("displays." + id + ".width", width);
            plugin.getConfigManager().getConfig().set("displays." + id + ".height", height);
            plugin.getConfigManager().saveConfig();
        });
    }

    // Establece la rotación (yaw/pitch) de un display
    public void setRotation(String id, float yaw, float pitch) {
        getDisplay(id).ifPresent(display -> {
            Location loc = display.getLocation();
            loc.setYaw(yaw);
            loc.setPitch(pitch);
            display.teleport(loc);
            forceClientUpdate(display);
            plugin.getConfigManager().getConfig().set(
                    "displays." + id + ".yaw", yaw);
            plugin.getConfigManager().getConfig().set(
                    "displays." + id + ".pitch", pitch);
            plugin.getConfigManager().saveConfig();
        });
    }

    // Establece el rango de renderizado
    public void setViewRange(String id, float range) {
        getDisplay(id).ifPresent(display -> {
            display.setViewRange(range);
            forceClientUpdate(display);
            plugin.getConfigManager().getConfig().set(
                    "displays." + id + ".view-range", range);
            plugin.getConfigManager().saveConfig();
        });
    }

    // Actualiza el texto de todos los displays activos
    public void updateAllDisplaysText(net.kyori.adventure.text.Component text) {
        activeDisplays.values().forEach(display -> {
            display.text(text);
            forceClientUpdate(display);
        });
    }

    // Forza actualización inmediata al cliente
    private void forceClientUpdate(TextDisplay display) {
        display.setInterpolationDelay(0);
        display.setInterpolationDuration(0);
    }

    // Carga todos los displays desde la configuración al iniciar
    public void loadAllDisplays() {
        ConfigManager cm = plugin.getConfigManager();
        Map<String, TextDisplayData> allData = cm.loadAllDisplays();

        if (allData.isEmpty()) {
            return;
        }

        // Intentar cargar displays inmediatamente
        int loaded = tryLoadDisplays(allData);

        // Si no se pudieron cargar todos, programar carga diferida
        if (loaded < allData.size()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                int retryLoaded = tryLoadDisplays(allData);
                PluginLogger.info("Cargados " + retryLoaded + " displays en retry.");
            }, 20L); // 1 segundo después
        }
    }

    // Intenta cargar todos los displays devuelve la cantidad cargada
    private int tryLoadDisplays(Map<String, TextDisplayData> allData) {
        int loaded = 0;
        for (Map.Entry<String, TextDisplayData> entry : allData.entrySet()) {
            TextDisplayData data = entry.getValue();
            World world = Bukkit.getWorld(data.getWorld());
            if (world == null) {
                PluginLogger.warning(
                        "Mundo no encontrado para display '" + data.getId() + "': " + data.getWorld());
                continue;
            }
            // Solo cargar si el mundo está completamente cargado
            if (!world.isChunkLoaded((int) data.getX() >> 4, (int) data.getZ() >> 4)) {
                if (!world.loadChunk((int) data.getX() >> 4, (int) data.getZ() >> 4, true)) {
                    PluginLogger.warning(
                            "No se pudo cargar el chunk para display '" + data.getId() + "'");
                    continue;
                }
            }
            Location loc = new Location(world, data.getX(), data.getY(), data.getZ(),
                    data.getYaw(), data.getPitch());
            spawnFromData(data, loc);
            loaded++;
        }
        PluginLogger.info("Cargados " + loaded + " displays.");
        return loaded;
    }

    // Actualiza todos los displays con datos de config (para reload)
    public void updateAllDisplays() {
        ConfigManager cm = plugin.getConfigManager();
        for (String id : cm.getDisplayIds()) {
            TextDisplayData data = cm.getDisplayData(id);
            if (activeDisplays.containsKey(id)) {
                updateDisplayFromData(id, data);
            } else {
                World world = Bukkit.getWorld(data.getWorld());
                if (world != null) {
                    Location loc = new Location(world, data.getX(), data.getY(),
                            data.getZ(), data.getYaw(), data.getPitch());
                    spawnFromData(data, loc);
                }
            }
        }
    }

    // Elimina todos los displays del mundo
    public void removeAllDisplays() {
        activeDisplays.values().forEach(display -> {
            if (!display.isDead())
                display.remove();
        });
        activeDisplays.clear();
    }

    // Obtener IDs de displays activos
    public Set<String> getDisplayIds() {
        return Collections.unmodifiableSet(activeDisplays.keySet());
    }

    public boolean hasDisplay(String id) {
        return activeDisplays.containsKey(id);
    }

    // Añade un display al mapa interno (para uso del listener)
    public void addDisplay(String id, TextDisplay display) {
        activeDisplays.put(id, display);
    }

    // --- Métodos privados ---

    // Aplica la configuración por defecto a un TextDisplay
    private void applyDefaultSettings(TextDisplay display) {
        ConfigManager cm = plugin.getConfigManager();
        display.setBillboard(Display.Billboard.valueOf(cm.getDefaultBillboard()));
        display.setDisplayWidth(cm.getDefaultWidth());
        display.setDisplayHeight(cm.getDefaultHeight());
        display.setViewRange(cm.getDefaultViewRange());
        display.setShadowed(cm.isShadowed());
        display.setSeeThrough(cm.isSeeThrough());
        // Configurar fondo
        if (cm.isBackgroundEnabled()) {
            display.setBackgroundColor(cm.getBackgroundColor());
            display.setDefaultBackground(false);
        } else {
            display.setDefaultBackground(false);
            display.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        }
    }

    // Actualiza la configuración de ubicación de un display
    private void updateConfigLocation(String id, Location loc) {
        plugin.getConfigManager().getConfig().set(
                "displays." + id + ".world", loc.getWorld().getName());
        plugin.getConfigManager().getConfig().set(
                "displays." + id + ".x", loc.getX());
        plugin.getConfigManager().getConfig().set(
                "displays." + id + ".y", loc.getY());
        plugin.getConfigManager().getConfig().set(
                "displays." + id + ".z", loc.getZ());
        plugin.getConfigManager().getConfig().set(
                "displays." + id + ".yaw", loc.getYaw());
        plugin.getConfigManager().getConfig().set(
                "displays." + id + ".pitch", loc.getPitch());
        plugin.getConfigManager().saveConfig();
    }

    // Spawnea un display desde datos de configuración
    private void spawnFromData(TextDisplayData data, Location loc) {
        World world = loc.getWorld();
        // Usar spawn() con callback para mejor compatibilidad con el cliente
        TextDisplay display = world.spawn(loc, TextDisplay.class, entity -> {
            applyDefaultSettingsDisplay(entity, data);
            entity.getPersistentDataContainer().set(displayKey, PersistentDataType.STRING, data.getId());
        });
        activeDisplays.put(data.getId(), display);
    }

    // Aplica settings desde TextDisplayData
    private void applyDefaultSettingsDisplay(TextDisplay display, TextDisplayData data) {
        display.setBillboard(Display.Billboard.valueOf(data.getBillboard()));
        display.setDisplayWidth(data.getWidth());
        display.setDisplayHeight(data.getHeight());
        display.setViewRange(data.getViewRange());
        display.text(MiniMessage.miniMessage().deserialize(""));
        // Forzar actualización inmediata al cliente
        display.setInterpolationDelay(0);
        display.setInterpolationDuration(0);
    }

    // Actualiza un display existente con datos frescos de config
    private void updateDisplayFromData(String id, TextDisplayData data) {
        getDisplay(id).ifPresent(display -> {
            World world = Bukkit.getWorld(data.getWorld());
            if (world != null) {
                Location loc = new Location(world, data.getX(), data.getY(),
                        data.getZ(), data.getYaw(), data.getPitch());
                display.teleport(loc);
            }
            display.setBillboard(Display.Billboard.valueOf(data.getBillboard()));
            display.setDisplayWidth(data.getWidth());
            display.setDisplayHeight(data.getHeight());
            display.setViewRange(data.getViewRange());
            applyDefaultSettings(display);
        });
    }
}
