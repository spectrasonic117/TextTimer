package com.spectrasonic.TextTimer.managers;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.TextTimer.commands.CreateCommand;
import com.spectrasonic.TextTimer.commands.EditCommand;
import com.spectrasonic.TextTimer.commands.ReloadCommand;
import com.spectrasonic.TextTimer.commands.RemoveCommand;
import com.spectrasonic.TextTimer.commands.TimerCommand;
import com.spectrasonic.TextTimer.commands.TphereCommand;
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
        CreateCommand.register(plugin);
        RemoveCommand.register(plugin);
        ReloadCommand.register(plugin);
        TphereCommand.register(plugin);
        EditCommand.register(plugin);
        TimerCommand.register(plugin);
    }
}
