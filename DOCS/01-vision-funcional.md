# 01 — Visión funcional

## Qué es

Un lector móvil del **Boletín Oficial de Cantabria (BOC)**. La aplicación consulta los feeds RSS
públicos que publica el Gobierno de Cantabria, presenta los anuncios agrupados por las mismas
secciones oficiales del boletín, y permite abrir el PDF oficial de cada anuncio y guardarlo en una
lista personal de favoritos.

No es un cliente oficial del Gobierno de Cantabria: es una app de terceros que consume feeds
públicos. Está publicada en Google Play como `com.jrblanco.boccantabria`.

## Para quién

Perfiles que necesitan vigilar el boletín de forma recurrente y por materias concretas:
opositores (sección 2.2), empresas que licitan (sección 3), gestorías y asesorías (secciones 4 y 6),
técnicos municipales y particulares afectados por urbanismo o expropiaciones (secciones 5 y 7.1).

El caso de uso central es **revisión periódica**: entrar, mirar la sección que interesa, y marcar
con una estrella lo que hay que conservar o leer después.

## Las cuatro cosas que hace

1. **Consultar por sección.** El menú lateral reproduce el índice del BOC (9 apartados, 19 secciones
   finales). Al elegir una sección se muestra el listado de sus anuncios más recientes.
2. **Abrir el documento oficial.** Cada anuncio tiene un botón que abre el PDF publicado en
   `boc.cantabria.es`. La app no incrusta un visor: delega en el sistema, que ofrece el navegador o
   el lector de PDF instalado.
3. **Guardar favoritos.** Un toque en la estrella conserva el anuncio en una lista local y
   permanente. Es la única información que la app almacena entre sesiones junto con la preferencia
   de tema.
4. **Cambiar el tema.** Un interruptor de modo oscuro/claro, independiente del ajuste del sistema.

## Qué NO hace

Importante para fijar expectativas, porque son ausencias deliberadas del diseño actual:

- **No busca ni filtra.** No hay caja de búsqueda, ni filtro por organismo, ni por fecha, ni por
  texto. La única forma de encontrar algo es recorrer visualmente la sección.
- **No notifica.** No hay avisos push, ni trabajo en segundo plano, ni alertas por palabra clave.
  El usuario tiene que abrir la app para enterarse de novedades.
- **No funciona sin conexión para consultar el boletín.** No guarda ninguna caché de los anuncios
  descargados: al cerrar la app se pierden. Sin internet solo son accesibles los favoritos.
- **No ordena ni agrupa.** Respeta exactamente el orden que llega en el feed. No agrupa por fecha ni
  por organismo, no marca lo no leído, no separa lo de hoy de lo de la semana pasada.
- **No cubre el boletín completo.** Cada sección expone como máximo las 100 entradas más recientes
  (límite del feed de origen, no de la app). No hay paginación, ni histórico, ni acceso a números
  anteriores del BOC.
- **No tiene cuentas ni sincronización.** Todo es local al dispositivo. Los favoritos no viajan a
  otro móvil ni se recuperan si se desinstala la app.
- **No permite compartir** un anuncio desde la propia ficha (más allá de lo que ofrezca la app a la
  que se delega el PDF).

## Permisos que usa y para qué

| Permiso | Uso real |
|---|---|
| `INTERNET` | Descargar los feeds RSS de `cantabria.es` |
| `ACCESS_NETWORK_STATE` | Comprobar antes de descargar si hay red, para avisar en vez de fallar |

No pide ubicación, almacenamiento, contactos ni notificaciones.
