package com.spectrasonic.TextTimer.managers;

import com.spectrasonic.TextTimer.Main;
import lombok.Getter;

@Getter
public class EventManager {

    private final Main plugin;

    public EventManager(Main plugin) {
        this.plugin = plugin;
        registerEvents();
    }

    private void registerEvents() {
        // Register events here
    }

}