# TextTimer

Definicion del proyecto tener una manera de hacer o mostrar timers en el mundo de manera grafica y elegante
Crea el plan para crear un plugin de estilo TIMER, pero lo haremos en DISPLAY_TEXT

Deberá tener las siguientes caracteristicas:

## Comandos
/tt create {id} - crea un display
/tt remove {id} - borra el display seleccionado
/tt reload - recarga la configuracion del plugin y los mensajes
/tt tphere {id} tphere - mueve el displaytext a la posicion del jugado
/tt edit {id} {param} - abre el menú para editar
    Con edit podremos editar parametros:

    - `move {int|float}x{int|float}y{int|float}z` - para mover el display a una posicion (ejemplo `move 3x-6y-3z` mueve el display relativo a la posicion inicial, se - puede usar solo `3x`, `3x-3z`, `6y-3.5z` por porner ejemplos)
    - `billboard {billboard type}` - determina el billboard del displaytext (buscar cuales son)
    - `size {int|float}x{int|float}y{int|float}z` - setea el tamaño del textdisplay con la misma mecanica de move. podremos mover solo en un eje o combinar
    - `rotation {int|float}w{int|float}p` - esto indica para mover en yaw o ptich con la misma mecanica del parametro move, podremos mover o solo pitch o solo yaw o ambas
    - `render {int}` define el rango de render del text display
/tt timer (MM:SS) - podremos iniciar el timer en en el display, que se actializará cada segundo hasta llegar a 00:00 (ejemplo /tttimer 3m2s, eso son 3 minutos 2 segudos, pero podremos usar solo segundo o solo minutos (2s o 3m) y hará el calculo)

Importante: el timer comienza con todos los display definidos por el plugin mediante sus ID asi que deberá leer cuales son los que tiene, todos los timers serán de color blanco

Cosas a considerar:
se debe de crear en config.yml la configuracion de todos los parametros definidos anteriormente con itentaciones y con estructura coherente

Los mensajes iran en messages.yml pero no tendran ningun color, si usa variables, puede incluirlas por ejemplo una variable del ID del text display

deberá tener un formato general al principio de la config para el texto del timer, por si lo queremos cambiar "por defecto es `<white>{timer}</white>`" y determinar si este tendrá fondo o no y que color será ese fondo y su opacidad

usa context7 para docs de Java, Paper/Spigotmc, CommandAPI para para mejorar