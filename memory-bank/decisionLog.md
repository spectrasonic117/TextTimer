# Decision Log

## Decision: Permisos simplificados
- Solo se usa CommandPermission.OP o "texttimer.admin"
- No hay permisos granulares por comando

## Decision: Rotación usa w/p en vez de y/p
- `w` = yaw, `p` = pitch
- Formato: 6w-3.5p (misma mecánica relativa que move)
- Evita confusión con eje Y de coordenadas

## Decision: Reload actualiza displays existentes
- Al ejecutar /tt reload se recargan config y messages
- Se recorren todos los displays guardados y se actualizan sus propiedades
- Aplica cambios de posición, rotación, tamaño, billboard, rango
- También actualiza config global (formato timer, fondo, opacidad)

## Decision: Timer aplica a todos los displays registrados
- Al iniciar timer con /tt timer <formato>, todos los displays del plugin muestran el timer
- Color blanco por defecto, formato configurable en config.yml
