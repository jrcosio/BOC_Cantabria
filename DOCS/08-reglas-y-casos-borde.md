# 08 — Reglas y casos borde

Recopilación de comportamientos reales que no se deducen de la interfaz. Distinguen lo que la app
**parece** hacer de lo que **hace**. Ninguno está corregido en la versión actual.

## 1. La recarga no refresca lo que se ve

**Lo esperado:** pulsar el botón flotante trae los anuncios nuevos y actualiza el listado.

**Lo real:** la descarga se ejecuta entera —con su diálogo de carga y sus ~745 KB— pero los datos
recién traídos se **acumulan** junto a los anteriores en lugar de sustituirlos, y el listado que se
pinta sigue siendo el de la **primera** descarga de la sesión. El resultado es que el usuario espera
la carga completa y ve exactamente lo mismo que antes.

**Afecta a:** el botón flotante de recarga y el regreso a la app desde segundo plano, que hace lo
mismo.

**No afecta a:** la primera apertura en frío, que sí muestra datos actuales. Cerrar la app por
completo y volver a abrirla es hoy la única forma fiable de ver novedades.

**Efecto secundario:** cada recarga deja en memoria una copia más de las 19 secciones, que ya no se
libera mientras la app siga abierta. Una sesión larga con muchas recargas acumula memoria sin
utilidad.

## 2. Desmarcar un favorito antiguo lo duplica

Detallado en [05 — Favoritos](05-favoritos.md#comportamiento-a-tener-en-cuenta-al-desmarcar).

**Resumen:** quitar la estrella a un favorito guardado en una **sesión anterior**, haciéndolo **desde
el listado de una sección**, no lo borra: añade una segunda copia sin marcar, y el anuncio aparece
duplicado en la pestaña *Favoritos*.

**Cómo evitarlo:** gestionar las bajas desde la pestaña *Favoritos*, donde funciona correctamente.

## 3. Cambiar el tema deja la pantalla sin listado

Detallado en [06 — Configuración y tema](06-configuracion-y-tema.md#el-arranque-especial-que-provoca).

Tras mover el interruptor, la zona de contenido queda vacía hasta que el usuario pulse *Principal*,
*Favoritos* o elija sección. Además se lanza una descarga completa que nunca se llega a mostrar.

## 4. Con red pero sin internet, la app se cierra

Detallado en [07 — Conectividad y errores](07-conectividad-y-errores.md#limitación-de-la-comprobación).

La comprobación previa valida que haya una red conectada, no que haya salida a internet. Con un
portal cautivo o un servidor caído, la app supera la comprobación, intenta descargar y termina
cerrándose sin mensaje.

## 5. La sección 8.1 Subastas está vacía y no se avisa

El feed de *Procedimientos Judiciales: Subastas* devuelve cero entradas. La app muestra una pantalla
en blanco bajo la cabecera, indistinguible de un fallo. Es el estado real del origen, no un error de
la app, pero el usuario no tiene forma de saberlo.

## 6. Las descripciones pueden llegar cortadas por dos motivos distintos

- **Por el origen:** el feed publica títulos ya truncados a media frase (`…del Servicio de Juventud
  de`). La app no puede hacer nada.
- **Por la app:** cuando el título contiene un segundo `:`, todo lo posterior se descarta. ~0,5 % de
  las entradas medidas.

En ambos casos el texto completo está en el PDF.

## 7. Girar el dispositivo rehace todo el arranque

Un cambio de orientación recrea la pantalla y dispara el arranque completo: la pausa de 1 segundo, el
diálogo *Cargando datos* y la descarga de los 19 feeds otra vez. Al terminar **se vuelve a
Disposiciones Generales**, se estuviera donde se estuviera.

Para el usuario significa que girar el móvil mientras lee la sección de Subvenciones cuesta una
espera completa y le devuelve a otra sección. Como contrapartida, es de las pocas vías —junto con
abrir la app en frío— por las que sí se ven datos actualizados, porque la memoria se parte de cero.

## 8. El botón *Atrás* siempre sale de la app

No hay historial de navegación. Estando en Configuración, en Favoritos o en cualquier sección, el
botón Atrás cierra la aplicación en lugar de volver a la vista anterior.

## 9. La pausa de 1 segundo del arranque es fija

El segundo que dura la pantalla de bienvenida no espera a que nada esté listo: es un retardo puesto a
propósito y se suma íntegro al tiempo de espera antes de que empiece siquiera la descarga.

## 10. La sección activa no se recuerda

Un usuario que solo consulta *Subvenciones y Ayudas* tiene que navegar hasta ella por el menú lateral
en cada apertura. La app siempre empieza en Disposiciones Generales.

## 11. Se descargan 19 secciones para mostrar una

No hay forma de limitar las secciones que interesan. Quien solo mire oposiciones paga igualmente el
tiempo y los datos de las 18 restantes en cada ciclo de carga.

---

## Resumen por impacto

| # | Comportamiento | Impacto |
|---:|---|---|
| 1 | La recarga no refresca | **Alto** — contradice la función principal del botón |
| 4 | Cierre con red sin internet | **Alto** — pérdida de la sesión sin explicación |
| 2 | Duplicado al desmarcar favoritos | Medio — datos incorrectos, corregible por el usuario |
| 3 | Pantalla vacía tras cambiar el tema | Medio — desconcierta, se resuelve con un toque |
| 7 | Rearranque completo al girar | Medio — espera larga y vuelta a la sección 0 |
| 11 | Descarga de las 19 secciones | Medio — tiempo y datos móviles |
| 5 | Sección vacía sin aviso | Bajo — depende del origen |
| 6 | Descripciones cortadas | Bajo — ~0,5 % de las entradas |
| 8 | Atrás sale de la app | Bajo — se aprende rápido |
| 9 | Pausa fija de 1 s | Bajo — coste constante |
| 10 | Sección no recordada | Bajo — fricción repetida |
