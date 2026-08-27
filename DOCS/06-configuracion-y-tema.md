# 06 — Configuración y tema

Se llega con el **engranaje** de la barra superior. La pantalla sustituye al listado y tiene dos
bloques.

## Bloque 1 — Interruptor *Tema Oscuro*

Alterna entre el aspecto claro y el oscuro de toda la aplicación.

- El cambio se aplica **inmediatamente**, sin confirmar y sin reiniciar la app a mano.
- El estado se guarda al momento en las preferencias del dispositivo y **se recuerda entre sesiones**:
  al volver a abrir la app se aplica antes de dibujar nada, de modo que no hay parpadeo de tema.
- Es un ajuste **independiente del tema del sistema**. La app no sigue automáticamente el modo
  oscuro de Android: manda siempre lo que diga este interruptor. Un dispositivo en modo oscuro
  mostrará la app en claro si el interruptor está apagado.
- El valor por defecto en una instalación nueva es **apagado** (tema claro).

### El arranque especial que provoca

Cambiar el tema obliga a Android a recrear la pantalla. La app marca ese cambio para reconocer, en el
arranque siguiente, que no se trata de una apertura normal. Cuando lo detecta:

- **No muestra el diálogo *Cargando datos***, porque el usuario acaba de estar usando la app y no
  procede enseñarle una pantalla de carga.
- **Se salta también la pausa de bienvenida de 1 segundo**, que solo se aplica en aperturas normales.
- Aun así **relanza la descarga completa de los 19 feeds en segundo plano**, de forma silenciosa.
- **No pinta ningún listado al terminar.**

El efecto visible para el usuario es que, tras mover el interruptor, se queda en la pantalla de
Configuración con el tema ya cambiado, y **la zona de contenido queda sin listado** hasta que pulse
*Principal*, *Favoritos* o elija una sección en el menú lateral. La marca de "vengo de cambiar el
tema" se consume en ese arranque, así que la siguiente apertura vuelve a comportarse con normalidad.

Es decir: cambiar el tema cuesta una descarga completa de ~745 KB que no se llega a mostrar.

## Bloque 2 — Listado de fuentes RSS

Debajo del interruptor hay una lista desplazable, **solo informativa**, con las 19 secciones del BOC
y la URL del feed de cada una.

- Cada tarjeta muestra el nombre completo de la sección y su URL.
- **Las URLs son pulsables**: se detectan como enlaces web y al tocarlas se abren en el navegador,
  donde se ve el XML crudo del feed.
- **No se puede editar, añadir, quitar ni desactivar ninguna fuente.** Es transparencia sobre el
  origen de los datos, no configuración.

## Lo que NO es configurable

Todo lo demás está fijado en la app:

- La sección que se muestra al arrancar (siempre Disposiciones Generales).
- Qué secciones se descargan (siempre las 19, sin poder limitarlas para ahorrar datos).
- Cuándo se descarga y con qué frecuencia.
- El número de entradas por sección (lo fija el origen en 100).
- El formato de la fecha.
- Cualquier tipo de aviso o notificación.
