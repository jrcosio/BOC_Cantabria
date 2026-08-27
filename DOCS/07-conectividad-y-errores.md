# 07 — Conectividad y errores

## Comprobación previa de red

Antes de cada ciclo de descarga la app comprueba si el dispositivo tiene una red activa de tipo
**móvil, Wi-Fi o Ethernet**.

Si no hay ninguna, la descarga **no se intenta** y aparece un aviso bloqueante:

> **Error de Conexión a Internet**
> En este momento es dispositivo no tiene conexión a INTERNET
> [ Aceptar ]

El diálogo no se puede descartar tocando fuera ni con el botón Atrás: hay que pulsar *Aceptar*. Al
aceptarlo la app continúa abierta pero **sin ningún dato descargado**.

### En qué estado queda la app sin conexión

| Acción | Resultado |
|---|---|
| Pulsar *Principal* | Aviso *No hay datos para visualizar* |
| Elegir una sección en el menú lateral | Aviso *No hay datos para visualizar* |
| Pulsar *Favoritos* | **Funciona con normalidad** si hay favoritos guardados |
| Pulsar el botón de recarga | Vuelve a salir el diálogo de error de conexión |
| Abrir Configuración | Funciona; el tema se puede cambiar sin conexión |
| Pulsar *Descargar PDF* en un favorito | Falla al abrirse en el navegador, no en la app |

Al recuperar la conexión no ocurre nada automáticamente: hay que pulsar el botón de recarga o volver
a entrar en la app.

## Limitación de la comprobación

La comprobación mira **si hay una red conectada, no si esa red llega a internet**. Escenarios en los
que la comprobación da positivo pero la descarga no puede funcionar:

- Wi-Fi de hotel, aeropuerto o cafetería con portal cautivo sin aceptar.
- Wi-Fi conectada pero con el router sin salida a internet.
- Datos móviles activos pero sin saldo o con la conexión restringida por el operador.
- Caída o mantenimiento de `cantabria.es`.

En estos casos la app entra en el ciclo de descarga y la petición falla. **La aplicación no gestiona
ese fallo**: no hay mensaje de error, no hay reintento y el diálogo de carga no se cierra por sí
solo. El comportamiento observado es el **cierre inesperado de la aplicación**.

Es la diferencia práctica más relevante entre "sin cobertura" (avisa bien) y "con red pero sin
internet" (se cierra).

## Fallo a mitad del ciclo

Como las 19 secciones se descargan en serie, un fallo de red en la sección 12 interrumpe todo el
ciclo: no se conservan las 11 secciones que sí se habían descargado. No hay resultado parcial ni
recuperación por sección.

## Datos inesperados en el feed

La conversión del XML es tolerante en unos casos y frágil en otros:

| Situación | Comportamiento |
|---|---|
| Feed vacío (`<size>0</size>`) | Listado vacío, **sin mensaje**. Ocurre hoy en *8.1 Subastas* |
| Organismo vacío (`null: …`) | Se sustituye por *MANCOMUNIDAD, CONCEJU U OTRO* |
| Título con varios `:` | La descripción se corta en el segundo `:` |
| XML mal formado | Se ignora el error y se conserva lo leído hasta ese punto |
| **Título sin ningún `:`** | La conversión **falla y la app se cierra** |
| Enlace sin `=` | El identificador pasa a ser la URL completa; el favorito se seguiría emparejando consigo mismo, pero no con la misma entrada de descargas anteriores |

El caso del título sin dos puntos no se da actualmente —se revisaron las 1.809 entradas disponibles
el 27 de agosto de 2026 y **todas** tenían al menos uno—, pero depende enteramente de cómo publique
el origen: es un formato que la app da por garantizado sin comprobarlo.

## Ausencia de estados vacíos

La app distingue "no hay datos descargados" (avisa con un mensaje emergente) de "la sección está
vacía" (no avisa de nada). En el segundo caso el usuario ve la cabecera del BOC y una zona en blanco,
sin poder saber si la sección no tiene anuncios, si falló la descarga o si la app se ha quedado
colgada.
