# Product Context

## Project Goal
Plugin de Minecraft (Paper 1.21.x) que permite crear y gestionar temporizadores visuales usando entidades TextDisplay. Los timers se muestran en el mundo de forma gráfica y elegante.

## Key Features
- Crear/eliminar displays de texto con IDs únicos
- Mover displays a posición del jugador o con offsets relativos
- Editar parámetros: billboard, tamaño, rotación (yaw/pitch), rango de render
- Sistema de timer con formato flexible (1m, 30s, 1m30s) que actualiza todos los displays registrados
- Formato configurable del timer con MiniMessage
- Fondo configurable (color, opacidad) o sin fondo
- Persistencia de displays en config.yml
- Recarga hot con /tt reload que actualiza displays existentes

## Overall Architecture
- **Paquete base:** `com.spectrasonic.TextTimer`
- **Patrón:** Managers centralizados (CommandManager, DisplayManager, TimerManager, ConfigManager, MessagesManager)
- **Comandos:** CommandAPI con subcomandos bajo /tt
- **Permisos:** CommandPermission.OP o "texttimer.admin"
- **Mensajes:** MiniMessage vía MessageUtils, claves en messages.yml
- **Display.Billboard:** FIXED, VERTICAL, HORIZONTAL, CENTER
- **Rotación:** yaw (w) y pitch (p) en formato relativo (6w-3.5p)
- **Modelos:** TextDisplayData para persistencia, TimerData para estado

## Parámetros de Edición
- `move {x}x{y}y{z}z` - movimiento relativo (ej: 3x-6y-3z, 3x, 6y-3.5z)
- `billboard {type}` - FIXED, VERTICAL, HORIZONTAL, CENTER
- `size {x}x{y}y` - tamaño del display (ej: 2x1)
- `rotation {yaw}w{pitch}p` - rotación relativa (ej: 90w, -10p, 45w-5p)
- `render {int}` - rango de renderizado en bloques

## Formato del Timer
- Default: `<white>{timer}</white>`
- Parser acepta: 30s, 2m, 1m30s
- Display muestra MM:SS o SS según corresponda
