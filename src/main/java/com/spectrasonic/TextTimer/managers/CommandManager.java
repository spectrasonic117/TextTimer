package com.spectrasonic.TextTimer.managers;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.TextTimer.commands.TextTimerCommand;
import lombok.Getter;

// Registro centralizado de todos los comandos del plugin
@Getter
public class CommandManager {

    private final Main plugin;

    public CommandManager(Main plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    private void registerCommands() {
        TextTimerCommand.register(plugin);
    }
}
