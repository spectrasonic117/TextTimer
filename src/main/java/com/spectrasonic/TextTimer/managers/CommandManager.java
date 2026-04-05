package com.spectrasonic.TextTimer.managers;

import com.spectrasonic.TextTimer.Main;
import lombok.Getter;

@Getter
public class CommandManager {

    private final Main plugin;

    public CommandManager(Main plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    private void registerCommands() {
        // Register commands here
    }
}