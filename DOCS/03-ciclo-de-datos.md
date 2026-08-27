# 03 — Ciclo de datos

## Cuándo se descarga

La app **no descarga bajo demanda por sección**. Siempre baja las **19 secciones enteras de golpe**,
y lo hace en tres momentos:

| Disparador | Indicador de carga | Muestra el listado al terminar |
|---|---|---|
| **Apertura en frío** (la app no estaba en memoria) | Sí, diálogo *Cargando datos* | Sí |
| **Volver a primer plano** (estaba en segundo plano) | Sí, diálogo *Cargando datos* | Sí |
| **Botón flotante de recarga** | Sí, diálogo *Cargando datos* | Sí |
| **Girar el dispositivo** | Sí, diálogo *Cargando datos* | Sí, pero vuelve a la sección 0 |
| **Regreso tras cambiar el tema** | **No**, carga silenciosa | **No** |

El último caso es deliberado: al cambiar entre tema claro y oscuro el sistema recrea la pantalla, y
la app detecta que viene de ahí para no repetir la animación de carga que el usuario ya vio.
Se explica en [06 — Configuración y tema](06-configuracion-y-tema.md).

No hay descarga programada, ni refresco automático por tiempo, ni al deslizar hacia abajo
(*pull-to-refresh*). Cambiar de sección en el menú lateral **no** dispara ninguna descarga: solo
cambia qué lista ya descargada se pinta.

## Cómo se descarga

Las 19 secciones se piden **una detrás de otra, en serie**, siguiendo el orden de la tabla de
[02 — Secciones](02-secciones-y-fuente-de-datos.md). No hay descargas en paralelo. Cada respuesta se
convierte a fichas antes de pedir la siguiente.

Consecuencias prácticas:

- La duración total es la **suma** de las 19 peticiones. En red móvil lenta el diálogo de carga puede
  estar visible bastante tiempo.
- El diálogo de carga **no se puede cancelar**: no tiene botón y no se cierra tocando fuera. Hasta
  que no acaban las 19 descargas no se devuelve el control.
- El progreso no es informativo: es un indicador circular indeterminado con el texto *Cargando datos*
  y *boc.cantabria.es*. No dice por qué sección va ni cuánto falta.
- Se consumen **~745 KB** en cada ciclo completo, incluidas las 18 secciones que el usuario quizá no
  va a mirar.
- No hay tiempo de espera propio ni reintentos: se usan los del sistema.

## Cómo se convierte cada anuncio

De cada `<item>` del RSS la app construye una ficha con seis datos. Estas son las reglas exactas:

### Organismo y descripción — se parten del título

El `<title>` se corta por el **primer** carácter `:`.

- **Organismo** = el texto anterior a los dos puntos.
- **Descripción** = el texto posterior, quitando el primer espacio.

Ejemplo real:

```
Título del feed : "AYUNTAMIENTO DE SUANCES: Aprobación definitiva de la modificación…"
   → Organismo   : "AYUNTAMIENTO DE SUANCES"
   → Descripción : "Aprobación definitiva de la modificación…"
```

**Regla especial del organismo desconocido.** Algunos anuncios llegan del origen con el organismo
literalmente vacío, en la forma `null: Notificación de sentencia…`. En ese caso la app sustituye el
organismo por el texto fijo:

```
MANCOMUNIDAD, CONCEJU U OTRO
```

No es un caso teórico: el 27 de agosto de 2026 había una entrada así en la sección 8.2
(*Procedimientos Judiciales: Otros Anuncios*).

**Límite del corte.** El corte se aplica al primer `:` pero solo se conserva el **segundo** trozo, de
modo que si la descripción contiene más dos puntos, todo lo que va después del segundo se pierde.
Ocurre hoy en varias secciones. Ejemplo real de la sección 2.1:

```
Título  : "AYUNTAMIENTO DE CAMARGO: Resolución de Alcaldía 2012, de delegación de funciones
           para autorización de matrimonio civil el 3 de agosto de 2026, a las 12:00 horas."
Muestra : "Resolución de Alcaldía 2012, … a las 12"
Se pierde: "00 horas."
```

Se midieron **9 entradas afectadas** sobre las 1.809 disponibles ese día (~0,5 %). El efecto es una
descripción cortada de forma abrupta, nunca un error visible.

### Enlace e identificador

- **Enlace** = el `<link>` tal cual. Es lo que se abre al pulsar *Descargar PDF*.
- **Identificador** = lo que hay **después del primer `=`** del enlace.

```
https://boc.cantabria.es/boces/verAnuncioAction.do?idAnuBlob=439686  →  id = "439686"
```

Ese identificador es la clave con la que se reconocen los favoritos entre descargas.

### Fecha

El `<pubDate>` se guarda y se muestra **sin ninguna transformación**: la ficha enseña literalmente
`2026-08-26`. La app no parsea la fecha, no la convierte a formato español, no calcula antigüedad y
no ordena por ella.

### Marca de favorito

Al construir cada ficha se compara su identificador con los de la lista de favoritos guardada en el
dispositivo. Si coincide, la ficha nace ya marcada y aparece con la estrella azul. Es lo que hace que
los favoritos sigan reconociéndose después de cerrar y abrir la app. Ver
[05 — Favoritos](05-favoritos.md).

## Qué se conserva y qué no

- Los anuncios descargados viven **solo en memoria**. Al cerrar la app se pierden por completo y la
  siguiente apertura vuelve a descargarlo todo.
- **No hay caché en disco**, ni base de datos, ni fichero temporal de los feeds.
- **El orden del feed se respeta íntegro.** La app no reordena. En la práctica el origen entrega las
  entradas de más reciente a más antigua, pero eso es una característica del feed, no una garantía
  de la app.
- No se eliminan duplicados ni se detectan anuncios ya vistos.
