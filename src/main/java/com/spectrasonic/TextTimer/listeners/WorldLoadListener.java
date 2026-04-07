package com.spectrasonic.TextTimer.listeners;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.TextTimer.models.TextDisplayData;
import com.spectrasonic.Utils.PluginLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.Color;

// Listener para cargar displays cuando un mundo se carga
public class WorldLoadListener implements Listener {

    private final Main plugin;
    private final NamespacedKey displayKey;

    public WorldLoadListener(Main plugin) {
        this.plugin = plugin;
        this.displayKey = new NamespacedKey(plugin, "texttimer_id");
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        String worldName = world.getName();

        // Cargar displays que estén en este mundo
        var configManager = plugin.getConfigManager();
        for (String id : configManager.getDisplayIds()) {
            TextDisplayData data = configManager.getDisplayData(id);

            // Si el display ya está activo, skip
            if (plugin.getDisplayManager().hasDisplay(id)) {
                continue;
            }

            // Si el display pertenece a este mundo, cargarlo
            if (data.getWorld().equalsIgnoreCase(worldName)) {
                // Cargar chunk si es necesario
                if (!world.isChunkLoaded((int) data.getX() >> 4, (int) data.getZ() >> 4)) {
                    if (!world.loadChunk((int) data.getX() >> 4, (int) data.getZ() >> 4, true)) {
                        PluginLogger.warning("No se pudo cargar el chunk para display '" + id + "'");
                        continue;
                    }
                }

                Location loc = new Location(world, data.getX(), data.getY(), data.getZ(),
                        data.getYaw(), data.getPitch());

                // Spawn del display usando spawn() con callback
                world.spawn(loc, TextDisplay.class, display -> {
                    applyDefaultSettings(display);
                    display.setBillboard(Display.Billboard.valueOf(data.getBillboard()));
                    display.setDisplayWidth(data.getWidth());
                    display.setDisplayHeight(data.getHeight());
                    display.setViewRange(data.getViewRange());
                    display.text(MiniMessage.miniMessage().deserialize(""));
                    display.getPersistentDataContainer().set(displayKey, PersistentDataType.STRING, data.getId());
                    // Forzar actualización inmediata al cliente
                    display.setInterpolationDelay(0);
                    display.setInterpolationDuration(0);

                    plugin.getDisplayManager().addDisplay(id, display);
                    PluginLogger.info("Display '" + id + "' cargado con el mundo.");
                });
            }
        }
    }

    // Aplica la configuración por defecto a un TextDisplay
    private void applyDefaultSettings(TextDisplay display) {
        var cm = plugin.getConfigManager();
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
}
