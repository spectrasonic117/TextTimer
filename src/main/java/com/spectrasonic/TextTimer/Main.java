package com.spectrasonic.TextTimer;

import com.spectrasonic.TextTimer.managers.CommandManager;
import com.spectrasonic.TextTimer.managers.ConfigManager;
import com.spectrasonic.TextTimer.managers.DisplayManager;
import com.spectrasonic.TextTimer.managers.EventManager;
import com.spectrasonic.TextTimer.managers.MessagesManager;
import com.spectrasonic.TextTimer.managers.TimerManager;
import com.spectrasonic.Utils.CommandUtils;
import com.spectrasonic.Utils.MessageUtils;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

// Clase principal del plugin TextTimer
@Getter
public final class Main extends JavaPlugin {

    private ConfigManager configManager;
    private CommandManager commandManager;
    private EventManager eventManager;
    private MessagesManager messagesManager;
    private DisplayManager displayManager;
    private TimerManager timerManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Inicializar managers
        configManager = new ConfigManager(this);
        messagesManager = new MessagesManager(this);
        displayManager = new DisplayManager(this);
        timerManager = new TimerManager(this);

        // Cargar displays desde la configuración
        displayManager.loadAllDisplays();

        // Registrar comandos y eventos
        commandManager = new CommandManager(this);
        eventManager = new EventManager(this);

        // Configurar utilidades
        CommandUtils.setPlugin(this);
        MessageUtils.sendStartupMessage(this);
    }

    @Override
    public void onDisable() {
        // Detener timer activo
        if (timerManager != null && timerManager.isActive()) {
            timerManager.stopTimer();
        }

        // Guardar estado de displays
        if (displayManager != null) {
            displayManager.removeAllDisplays();
        }

        MessageUtils.sendShutdownMessage(this);
    }
}
