package com.spectrasonic.TextTimer.managers;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.TextTimer.listeners.WorldLoadListener;
import lombok.Getter;

// Registro centralizado de eventos del plugin
@Getter
public class EventManager {

    private final Main plugin;
    private final WorldLoadListener worldLoadListener;

    public EventManager(Main plugin) {
        this.plugin = plugin;
        this.worldLoadListener = new WorldLoadListener(plugin);
        registerEvents();
    }

    private void registerEvents() {
        // Registrar listeners
        plugin.getServer().getPluginManager().registerEvents(worldLoadListener, plugin);
    }
}
