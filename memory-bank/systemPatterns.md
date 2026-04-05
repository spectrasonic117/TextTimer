# System Patterns

## Coding Patterns
- Java 21, Lombok para boilerplate
- Comentarios en español, inline con //
- Sin Javadocs con block comments
- Clases máximo 200 líneas
- SOLID, Clean Code, Clean Architecture

## Utility Classes (INMUTABLES - No modificar)
- `MessageUtils` - sendMessage, successMessage, alertMessage, denyMessage, warningMessage, infoMessage, debugMessage
- `SoundUtils` - playerSound, broadcastSound
- `CommandUtils` - ConsoleCommand, PlayerCommand
- `PluginLogger` - info, success, warning, error, severe, config, fine
- `ItemBuilder` - builder pattern para ItemStacks

## Command Pattern
- CommandAPI con builder pattern, subcomandos bajo /tt
- Registro en CommandManager, lógica separada en handlers
- Permisos: CommandPermission.OP o "texttimer.admin"
- Sugerencias dinámicas con replaceSuggestions

## MessagesManager Pattern
- @Getter en clase, campos: Main plugin, FileConfiguration messagesConfig, File messagesFile
- loadMessages() copia resource si no existe
- reloadMessages() solo reasigna config
- getMessage(key) con fallback MiniMessage

## Config Pattern
- config.yml: settings + persistencia de displays
- messages.yml: mensajes con variables {id}, {time}, etc.
- ConfigManager carga/provee acceso, saveConfig() para persistir

## TextDisplay API
- EntityType.TEXT_DISPLAY para spawn
- text(Component) para establecer texto (Adventure API)
- setBillboard(Display.Billboard) - FIXED, VERTICAL, HORIZONTAL, CENTER
- setDisplayWidth/Height(float) para tamaño
- setViewRange(float) para rango render
- setBackgroundColor(Color) para fondo
- setDefaultBackground(boolean) para fondo por defecto

## Parser de Coordenadas Relativas
- Regex para move/size: (-?\d+\.?\d*)([xyz]) por cada eje
- Regex para rotation: (-?\d+\.?\d*)([wp]) donde w=yaw, p=pitch
- Parser de tiempo: (\d+)([ms]) donde m=minutos, s=segundos
