# 05 — Favoritos

Los favoritos son la única información de contenido que la app conserva entre sesiones. Funcionan
como un **archivo personal permanente**, no como una simple marca sobre el listado.

## Qué se guarda

Al marcar un anuncio **no se guarda una referencia, se guarda una copia completa de la ficha**:
identificador, organismo, descripción, fecha y enlace al PDF. Esto tiene una consecuencia funcional
importante y muy útil:

> Un favorito **sobrevive a que el anuncio desaparezca del feed**. Cuando el anuncio sale de la
> ventana de las 100 entradas más recientes de su sección y ya no se puede encontrar navegando, el
> favorito lo sigue mostrando con todos sus datos, y su botón *Descargar PDF* sigue funcionando
> mientras el documento siga publicado en `boc.cantabria.es`.

Es, de hecho, el único mecanismo de histórico que ofrece la app.

## Cómo se marca y desmarca

La estrella de cada ficha alterna el estado y **guarda el cambio en el mismo momento**. No hay botón
de confirmar, ni deshacer, ni mensaje. La estrella cambia de color al instante:

| Estado | Icono |
|---|---|
| Guardado | Estrella **azul** |
| No guardado | Estrella **blanca** |

Se puede marcar y desmarcar tanto desde el listado de una sección como desde la propia lista de
Favoritos. Al desmarcar desde la lista de Favoritos, la entrada desaparece de ella en cuanto se
repinta la lista.

## Cómo se reconocen entre sesiones

Los anuncios que llegan del feed y los favoritos guardados son datos independientes: hay que
emparejarlos en cada descarga. La regla es:

> Durante la conversión de cada anuncio descargado, se compara su **identificador** (el número del
> enlace, p. ej. `439686`) con los identificadores de todos los favoritos guardados. Si coincide
> alguno, la ficha se marca como favorita.

Por tanto la estrella azul aparece correctamente al recorrer las secciones aunque hayan pasado días
desde que se guardó, y el emparejamiento **solo depende del identificador**: aunque el organismo o la
descripción cambiaran en el origen, el favorito se seguiría reconociendo.

## Dónde se almacenan

En las preferencias locales de la aplicación, en un único bloque de texto en formato JSON. No es una
base de datos ni un fichero accesible por el usuario.

Consecuencias:

- Los favoritos son **locales al dispositivo**. No se sincronizan con otro móvil ni con ninguna cuenta.
- **Desinstalar la app los borra.** No hay exportación ni copia manual.
- La app tiene activada la copia de seguridad automática de Android sin reglas de inclusión o
  exclusión propias, de modo que dependen del comportamiento por defecto del sistema y del fabricante.
- **No hay límite** de número de favoritos ni purga automática. La lista crece indefinidamente.

## Disponibilidad sin conexión

Los favoritos se cargan del dispositivo **al abrir la app, antes de cualquier descarga**. Por eso la
pestaña *Favoritos* es plenamente funcional sin internet: se pueden consultar organismo, descripción
y fecha de todo lo guardado. Lo único que necesita conexión es abrir el PDF.

## Comportamiento a tener en cuenta al desmarcar

El emparejamiento entre la ficha mostrada y la copia guardada se hace comparando **todos los campos**
de la ficha, incluido el propio estado de marcado. Como al pulsar la estrella el estado cambia
*antes* de buscar la entrada guardada, hay un caso en el que la coincidencia no se produce:

> **Desmarcar un favorito guardado en una sesión anterior, desde el listado de una sección**, no lo
> elimina: añade una segunda copia del mismo anuncio, esta vez sin marcar. En la pestaña *Favoritos*
> el anuncio aparecerá entonces **duplicado**, una vez con estrella azul y otra con estrella blanca.

Casos en los que **no** ocurre:

- Marcar y desmarcar en la misma sesión, sin cerrar la app: funciona correctamente.
- Desmarcar **desde la propia pestaña Favoritos**: funciona correctamente y elimina la entrada.

Y así se corrige si ya ha pasado: pulsando la estrella sobre la copia duplicada en la pestaña
*Favoritos* se vuelve a dejar una sola entrada.

La forma recomendada de gestionar los favoritos es, por tanto, **quitarlos desde la pestaña
Favoritos**, no desde el listado de la sección.

## Efecto de modificar la ficha en futuras versiones

Como lo que se guarda es la ficha completa serializada, **cambiar los datos que componen un anuncio
en una versión nueva de la app invalida los favoritos ya almacenados en los dispositivos de los
usuarios**: al no poder interpretarse el bloque guardado, la lista de favoritos se pierde. Es una
restricción a respetar en cualquier evolución del contenido de la ficha.
