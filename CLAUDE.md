# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Proyecto

App Android nativa (Kotlin, sistema de Vistas clásico + ViewBinding, **sin** Compose ni Jetpack Navigation) que
lee los feeds RSS del Boletín Oficial de Cantabria (`cantabria.es/o/BOC/feed/...`), los muestra por secciones y
permite marcar entradas como favoritas. Publicada en Play Store como `com.jrblanco.boccantabria`.

El código, los comentarios, los identificadores y los KDoc están **en español**. Mantén esa convención al añadir
código nuevo (`listaFavoritos`, `obtenerDatosBoc`, `seccionBOC`...).

## Documentación funcional

`DOCS/` describe con detalle **qué hace la app y bajo qué reglas de negocio** (secciones del BOC,
ciclo de descarga, conversión del RSS, favoritos, tema, errores y casos borde). Consúltala antes de
tocar comportamiento visible al usuario; aquí solo está lo estructural.

## Entorno y comandos

No hay JDK en el `PATH` de esta máquina: el único disponible es el JBR que trae Android Studio. **Todo comando de
Gradle necesita `JAVA_HOME` explícito**, o falla con `Unable to locate a Java Runtime`:

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
```

```bash
./gradlew :app:assembleDebug          # APK debug -> app/build/outputs/apk/debug/
./gradlew :app:assembleRelease        # APK release SIN firmar (no hay signingConfig)
./gradlew :app:bundleRelease          # AAB
./gradlew :app:testDebugUnitTest      # tests JVM (app/src/test)
./gradlew :app:connectedDebugAndroidTest   # tests instrumentados (requiere emulador/dispositivo)
./gradlew :app:lintDebug              # Android Lint -> app/build/reports/lint-results-debug.html
./gradlew clean
```

Un solo test JVM:

```bash
./gradlew :app:testDebugUnitTest --tests "com.jrblanco.boccantabria.ExampleUnitTest.addition_isCorrect"
```

Instalar y lanzar en un dispositivo conectado:

```bash
./gradlew :app:installDebug
adb shell am start -n com.jrblanco.boccantabria/.view.MainActivity
```

Solo hay tests de plantilla (`ExampleUnitTest`, `ExampleInstrumentedTest`); no existe suite real.

## Arquitectura

Una sola `Activity` que actúa como controlador y dueña de todo el estado; los fragments son puramente de
presentación y se intercambian a mano con `supportFragmentManager.replace(R.id.fragmentContainerView, ...)`.

```
view/MainActivity          estado + red + persistencia + navegación
  ├─ view/ListadoBocFragment(List<ItemBoc>)  RecyclerView de entradas del BOC
  └─ view/ConfigFragment                     switch de tema oscuro + listado de URLs RSS
model/UrlBoc               las 19 secciones del BOC, hardcodeadas en el companion `listadoRSS`
model/XmlPullParserHandler XML del RSS -> List<ItemBoc>
model/ItemBoc              modelo de datos (también es lo que se serializa a disco)
adapter/ListadoBocAdapter  ítem + estrella de favorito + botón que abre el PDF por Intent
adapter/ListadoUrlBocAdapter  solo lectura, para ConfigFragment
```

### Índices de sección: acoplamiento a vigilar

`MainActivity.rssBoc` es un `ArrayList<List<ItemBoc>>` rellenado **en el orden de `UrlBoc.listadoRSS`**, y la
sección visible se elige por posición con `seccionBOC`. `controlOpcionesNavigatorDrawer()` traduce cada
`R.id.menu_*` de `menu_drawer_nav_view.xml` a ese índice con un `when` de números literales (0..18).

Añadir, quitar o reordenar una sección obliga a tocar **tres sitios a la vez**: `UrlBoc.listadoRSS`,
`menu_drawer_nav_view.xml` y el `when` de `controlOpcionesNavigatorDrawer()`. No hay nada que detecte la
desincronización: simplemente se muestra la sección equivocada.

### Carga de datos

`obtenerDatosBoc()` comprueba conectividad, abre un `AlertDialog` de carga y lanza un `thread { }` que descarga
las 19 URLs **en serie** con `URL(...).readText()` y las parsea con `XmlPullParserHandler`. No hay corrutinas,
ni Retrofit, ni caché, ni ViewModel: cada `onCreate` y cada `onRestart` re-descarga los 19 feeds enteros.

El parser es un `XmlPullParser` a mano que asume la forma del RSS del BOC: parte `<title>` por `:` (izquierda =
organismo, derecha = descripción) y saca el `id` del `<link>` tomando lo que hay tras el primer `=`. Es frágil
frente a cambios de formato del feed; si un título no trae `:`, `result[1]` lanza `IndexOutOfBoundsException`.

### Persistencia (SharedPreferences, sin base de datos)

- `com.jrblanco.listaFavoritos` / clave `LISTAFAVORITOS`: la lista de favoritos serializada a JSON con Gson.
  Cambiar los campos de `ItemBoc` rompe la deserialización de los favoritos ya guardados en dispositivos.
- `com.jrblanco.temaoscuro` / clave `valor`: booleano del modo oscuro, aplicado con `AppCompatDelegate`.
- `com.jrblanco.temaoscuro` / clave `cambiadotema`: bandera de un solo uso. `ConfigFragment` la pone a `true`
  al cambiar el tema; `MainActivity.cambiadoTema()` la lee y la limpia en `onCreate` para **saltarse la
  animación de carga** cuando el arranque viene de una recreación por cambio de tema.

Un `ItemBoc` se marca favorito en el adapter, sube al fragment por la lambda `onClickFavorito`, y de ahí a la
Activity por la interfaz `ListadoBocFragment.CallbackFavorito` (la Activity la implementa; `onAttach` hace
`context as CallbackFavorito` y peta si no).

## Problemas conocidos en el código actual

No están arreglados a propósito (la app funciona así en producción); tenlos en cuenta antes de tocar esas zonas:

- `rssBoc` **nunca se limpia** antes de recargar, así que cada pulsación del FAB o cada `onRestart` añade otras
  19 listas. El array crece sin límite; los índices siguen apuntando a la primera tanda, que queda obsoleta.
- Dentro del `thread { }`, `cargarFragment()` y `Toast.makeText()` se invocan desde el hilo secundario; solo el
  `dialog.dismiss()` va envuelto en `runOnUiThread`.
- `MainActivity.onCreate` hace `Thread.sleep(1000)` en el hilo principal antes de `super.onCreate()`.
- `ListadoBocFragment` tiene constructor con argumentos: el sistema no puede recrearlo tras un cambio de
  configuración o al restaurar estado.
- `notifyDataSetChanged()` refresca la lista entera en cada toque de estrella.

## Toolchain

AGP 8.10.1 · Gradle 8.11.1 · Kotlin 2.0.21 · JDK 17 (source/target y `jvmTarget`) · compileSdk 36 ·
minSdk 25 · targetSdk 36 (Android 16).

AGP 8.9.1 es el mínimo para `compileSdk 36`; por eso 8.10.1. Gradle 8.11.1 sirve para AGP 8.9 y 8.10, pero AGP
8.11+ exigiría subir Gradle a 8.13.

### Edge-to-edge

Con `targetSdk 35+` el sistema dibuja la app de borde a borde y **`android:statusBarColor` y
`android:navigationBarColor` se ignoran** (en `targetSdk 36` ya no hay forma de desactivarlo). Consecuencias en
esta UI, verificadas en emulador API 37:

- El `DrawerLayout` tiene `fitsSystemWindows="true"`, así que la parte superior se resuelve sola: la ActionBar
  queda bajo la barra de estado y la cabecera del `NavigationView` ocupa correctamente esa franja.
- La franja de la barra de estado **ya no se pinta del color primario**: se ve negra fuera del menú lateral.
  Es cosmético y queda pendiente. No se arregla desde el layout: `android.R.id.content` empieza por debajo de
  la ActionBar, así que pintar el `DrawerLayout` no llega a esa zona (y además tiñe el fondo de toda la app).
  Habría que actuar sobre el fondo del decor.
- El hueco de la barra de navegación **hay que reservarlo a mano**: lo hace
  `MainActivity.ajustarBarrasDelSistema()`, que aplica el inset inferior como `padding` del `BottomAppBar`.
  Sin eso, la barra de gestos o los tres botones se superponen a "Principal" y "Favoritos". El listener se
  engancha en `android.R.id.content` y reenvía los insets con `ViewCompat.onApplyWindowInsets` para no romper
  el reparto que hace el `DrawerLayout`.

El proyecto usa Groovy DSL (`.gradle`, no `.gradle.kts`) y declara versiones literales en `build.gradle`: no hay
version catalog (`libs.versions.toml`).
