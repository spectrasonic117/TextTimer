# Propuestas de Mecánicas para TextTimer

---

## Análisis del Proyecto

**Funcionalidades actuales:**
- Crear/editar/eliminar TextDisplays
- Iniciar temporizadores con formato flexible (`1m30s`, `30s`, etc.)
- Configuración de apariencia (billboard, tamaño, fondo, visibilidad)
- Sonidos al iniciar/finalizar timer
- Persistencia de displays en configuración
- Sistema de comandos con subcomandos bien organizado

---

## Propuestas de Mecánicas Nuevas

### 🔥 Prioridad Alta

#### 1. Múltiples Temporizadores Simultáneos
En lugar de un solo timer global, permitir múltiples timers independientes con IDs propios.
```
/tt timer start <id> <tiempo>
/tt timer stop <id>
/tt timer status
```
Cada display podría vincularse a un timer específico.

#### 2. Temporizador Cíclico (Loop)
El timer se reinicia automáticamente al terminar.
```yaml
timer:
  loop: true
  loop-delay: 5  # segundos entre reinicios
```

#### 3. Eventos de Tiempo (Timer Milestones)
Ejecutar acciones en tiempos específicos durante la cuenta regresiva.
```yaml
timer:
  milestones:
    - time: 30
      action: "sound: BLOCK_NOTE_BLOCK_PLING"
      message: "<yellow>30 segundos restantes!</yellow>"
    - time: 10
      action: "sound: UI_BUTTON_CLICK"
```

---

### 🎨 Personalización Visual

#### 4. Texto Personalizado Estático
Poder mostrar texto que no sea un timer.
```
/tt settext <id> <texto>
```
Útil para letreros dinámicos.

#### 5. Animaciones de Texto
Múltiples frames de texto que cambian periódicamente.
```yaml
display:
  animation:
    enabled: true
    frames:
      - "<red>⚠</red>"
      - "<yellow>⚠</yellow>"
    interval: 0.5  # segundos
```

#### 6. Texto con Efectos Visuales
- Texto parpadeante
- Gradiente de color
- Texto con borde (outline)
- Texto con sombra personalizada

---

### ⚙️ Funcionalidades Avanzadas

#### 7. Display que Sigue a Entidades
El display se teletransporta automáticamente a un jugador o entidad.
```
/tt follow <id> <player|entity> [offset]
```
Útil para timers de arena/JC.

#### 8. Temporizador con Comandos Automáticos
Ejecutar comandos al iniciar, finalizar o en hitos.
```yaml
timer:
  on-start:
    - "broadcast §a¡El juego comienza!"
  on-end:
    - "execute at @a run scoreboard players add @s points 1"
```

#### 9. Contador Progresivo
En lugar de cuenta regresiva, contar hacia arriba.
```
/tt timer countup <id> [formato]
```

#### 10. Programación de Timers
Iniciar timers en horarios específicos.
```
/tt schedule <id> <cron> <tiempo>
```
Ejemplo: `/tt schedule arena1 "0 12 * * *" 5m` (a las 12:00 todos los días)

---

### 📊 Gestión y Organización

#### 11. Sistema de Grupos
Agrupar displays para controlarlos en conjunto.
```
/tt group create <nombre>
/tt group add <nombre> <id>
/tt group timer <nombre> start <tiempo>
```

#### 12. Presets de Displays
Guardar y cargar configuraciones de appearance.
```yaml
presets:
  lobby-timer:
    width: 4.0
    height: 1.0
    billboard: CENTER
    background:
      enabled: true
      color: "#1a1a2e"
```

#### 13. Clonar Displays
Duplicar un display existente con nuevo ID.
```
/tt clone <id-origen> <id-destino>
```

---

### 🎯 Calidad de Vida (QoL)

#### 14. Vista Previa en Chat
Al crear un display, mostrar preview del texto.
```
/tt create <id> --preview "texto de ejemplo"
```

#### 15. Importar/Exportar Configuración
Compartir configuraciones de displays entre servidores.
```
/tt export <archivo>
/tt import <archivo>
```

#### 16. Timer Visual en Barras de Boss
Mostrar el timer también en la bossbar del jugador.
```
/tt bossbar <id> enable [color] [style]
```

---

## Orden Recomendado de Implementación

1. **Múltiples timers simultáneos** - La más solicitada usualmente
2. **Texto personalizado estático** - Permite usar el plugin para más casos
3. **Timer cíclico** - Para minigames automáticos
4. **Eventos de tiempo** - Añade interactividad

---

*Documento generado automáticamente.*
