# 04 — Pantallas y flujos

La app tiene **una sola pantalla** con controles permanentes y una zona central que va cambiando de
contenido. No hay pila de navegación: el botón *Atrás* del sistema **no vuelve a la vista anterior**,
sale de la aplicación.

## Controles permanentes

Siempre visibles, sea cual sea el contenido central:

| Control | Posición | Acción |
|---|---|---|
| Icono de hamburguesa | Barra superior, izquierda | Abre el menú lateral de secciones |
| Título *BOC Cantabria* + escudo | Barra superior | Decorativo |
| Icono de engranaje | Barra superior, derecha | Abre la pantalla de Configuración |
| **Principal** | Barra inferior, izquierda | Vuelve al listado de la sección elegida |
| **Favoritos** | Barra inferior, derecha | Muestra el listado de favoritos |
| Botón circular de recarga | Flotante, sobre la barra inferior | Relanza la descarga de los 19 feeds |

Entre *Principal* y *Favoritos* hay un hueco desactivado que solo existe para dejar sitio al botón
flotante; tocarlo no hace nada.

## Arranque

1. Se aplica el tema (claro u oscuro) según lo guardado.
2. Se cargan los favoritos del dispositivo.
3. Se muestra la **pantalla de bienvenida** (fondo azul oscuro con la imagen de la app) durante una
   pausa fija de **1 segundo**. Esta pausa es incondicional: no espera a nada, es un retardo puesto a
   propósito para que el splash se vea.
4. Se comprueba si hay conexión. Sin conexión, el flujo se corta aquí con un aviso — ver
   [07 — Conectividad y errores](07-conectividad-y-errores.md).
5. Aparece el diálogo *Cargando datos* y empieza la descarga de las 19 secciones.
6. Al terminar, se muestra el listado de la **sección 0, Disposiciones Generales**.

Mientras dura el paso 5 la zona central está vacía: el usuario ve el diálogo de carga sobre un fondo
sin contenido. **No hay listado que consultar hasta que las 19 descargas han terminado.**

## Listado de anuncios

Es el contenido central habitual. Consta de una cabecera gráfica fija con la imagen del BOC y, bajo
ella, la lista desplazable de fichas.

Cada ficha es una tarjeta redondeada que muestra:

- **Organismo**, en negrita arriba.
- **Descripción**, bajo la etiqueta *Descripción*.
- **Fecha**, en formato `AAAA-MM-DD`, junto a la etiqueta *Fecha*.
- Botón **Descargar PDF**.
- **Estrella** de favorito: azul si está guardado, blanca si no.

La ficha **no es pulsable en su conjunto**. Solo responden los dos controles: el botón y la estrella.

### Botón *Descargar PDF*

Lanza el enlace del anuncio al sistema operativo, que decide con qué abrirlo (navegador, visor de
PDF, gestor de descargas). La URL devuelve un PDF real, así que el comportamiento típico es que se
abra el documento oficial o se descargue con el nombre que fija el servidor, del tipo
`2026-6695.pdf`.

La app **no descarga el PDF por su cuenta**, no lo guarda, no lo cachea y no muestra un visor propio.
En el caso extremo de un dispositivo sin ninguna aplicación capaz de abrir un enlace web, la app se
cerraría al pulsar el botón, porque no contempla esa posibilidad.

### Estrella de favorito

Alterna el estado del anuncio y guarda el cambio de inmediato. El detalle completo está en
[05 — Favoritos](05-favoritos.md).

Al pulsarla se **repinta la lista entera**, lo que en secciones de 100 entradas puede notarse como un
pequeño parpadeo.

## Menú lateral de secciones

Se abre con la hamburguesa o deslizando desde el borde izquierdo. Muestra una cabecera con el escudo
de Cantabria, el rótulo *BOC — BOLETÍN OFICIAL DE CANTABRIA* y la autoría, y debajo el índice del
boletín agrupado en los 9 apartados oficiales, cada uno con sus subsecciones.

Al elegir una subsección:

1. Se fija esa sección como la activa.
2. Se cierra el menú.
3. Se pinta el listado correspondiente **con los datos ya descargados**. No se vuelve a consultar la
   red.

Si todavía no hay datos descargados, aparece en su lugar el aviso:

> No hay datos para visualizar
> Asegurese de que este dispositivo tiene internet

La sección elegida se mantiene mientras la app siga abierta, y es la que se recupera al pulsar
*Principal*. **No sobrevive al cierre de la app**: al volver a entrar se empieza otra vez por
Disposiciones Generales.

## Barra inferior

**Principal** vuelve a mostrar el listado de la sección activa. Si aún no hay datos, muestra el mismo
aviso *No hay datos para visualizar*.

**Favoritos** muestra la lista de anuncios guardados, con el mismo formato de ficha. Si no hay
ninguno, aparece:

> No hay favoritos para visualizar

y la vista no cambia. Los favoritos se leen del dispositivo, así que **esta pantalla funciona sin
conexión** aunque no se haya podido descargar nada.

## Botón flotante de recarga

Vuelve a lanzar el ciclo completo de descarga de los 19 feeds, con su diálogo de carga. Al terminar
muestra el listado de la sección activa.

> **Comportamiento real a tener en cuenta:** en la versión actual esta recarga descarga los datos
> nuevos pero **sigue mostrando los de la primera descarga**. Es decir, el botón no refresca lo que
> se ve. Lo mismo ocurre al volver a la app desde segundo plano. Está detallado en
> [08 — Reglas y casos borde](08-reglas-y-casos-borde.md).

## Pantalla de Configuración

Se abre con el engranaje de la barra superior y sustituye al listado. Contiene un interruptor de
tema oscuro y un listado informativo de las 19 secciones con sus URLs. Se describe en
[06 — Configuración y tema](06-configuracion-y-tema.md).

Para salir de ella hay que usar *Principal*, *Favoritos* o el menú lateral: **el botón Atrás no
regresa al listado**, cierra la app.
