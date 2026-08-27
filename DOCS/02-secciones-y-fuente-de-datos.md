# 02 — Secciones y fuente de datos

## Origen

Toda la información procede de los feeds RSS públicos del Gobierno de Cantabria, con el patrón:

```
https://www.cantabria.es/o/BOC/feed/{idFeed}
```

Cada `idFeed` corresponde a una sección del índice oficial del BOC. Las 19 URLs están **fijadas en
el código**: no se descubren dinámicamente, no se configuran desde ajustes y el usuario no puede
añadir ni quitar secciones. Si el Gobierno de Cantabria cambia un identificador o reorganiza el
índice del boletín, hace falta publicar una versión nueva de la app.

## Catálogo completo de secciones

El orden de esta tabla es el orden interno de la aplicación y determina qué sección se muestra al
pulsar cada entrada del menú lateral.

| # | Sección del BOC | idFeed |
|---:|---|---|
| 0 | 1 — Disposiciones Generales | 6802081 |
| 1 | 2.1 — Autoridades y Personal: Nombramientos, Ceses y Otras Situaciones | 6802084 |
| 2 | 2.2 — Autoridades y Personal: Cursos, Oposiciones y Concursos | 6802085 |
| 3 | 2.3 — Autoridades y Personal: Otros | 6802086 |
| 4 | 3 — Contratación Administrativa | 6802087 |
| 5 | 4.1 — Economía y Hacienda: Actuaciones en Materia Presupuestaria | 6802089 |
| 6 | 4.2 — Economía y Hacienda: Actuaciones en Materia Fiscal | 6802090 |
| 7 | 4.3 — Economía y Hacienda: Actuaciones en materia de Seguridad Social | 6802091 |
| 8 | 4.4 — Economía y Hacienda: Otros | 6802092 |
| 9 | 5 — Expropiación Forzosa | 6802094 |
| 10 | 6 — Subvenciones y Ayudas | 6802095 |
| 11 | 7.1 — Otros Anuncios: Urbanismo | 6802097 |
| 12 | 7.2 — Otros Anuncios: Medio Ambiente y Energía | 6802098 |
| 13 | 7.3 — Otros Anuncios: Estatutos y Convenios Colectivos | 6802099 |
| 14 | 7.4 — Otros Anuncios: Particulares | 6802100 |
| 15 | 7.5 — Otros Anuncios: Varios | 6802301 |
| 16 | 8.1 — Procedimientos Judiciales: Subastas | 7479572 |
| 17 | 8.2 — Procedimientos Judiciales: Otros Anuncios | 6802303 |
| 18 | 9 — Elecciones | 7293890 |

La **sección 0 (Disposiciones Generales) es la que se muestra por defecto** al abrir la app. La
sección elegida se recuerda mientras la app siga viva, pero **no se guarda**: al volver a abrirla se
vuelve a empezar por la 0.

## Forma real de los datos

Cada feed es un RSS 2.0 con esta estructura por entrada:

```xml
<item>
  <title>AYUNTAMIENTO DE SUANCES: Aprobación definitiva de la modificación de la Ordenanza…</title>
  <link>https://boc.cantabria.es/boces/verAnuncioAction.do?idAnuBlob=439686</link>
  <pubDate>2026-08-26</pubDate>
  <categorias>1.Disposiciones Generales|Ayuntamiento de Suances|ORD</categorias>
</item>
```

Detalles que condicionan lo que ve el usuario:

- **`<title>` combina dos datos** separados por dos puntos: el organismo emisor a la izquierda y la
  descripción del anuncio a la derecha. La app los separa y los pinta en líneas distintas.
- **`<pubDate>` no es una fecha RFC-822**, como sería habitual en RSS, sino `AAAA-MM-DD`. Se muestra
  tal cual, sin reformatear ni traducir.
- **`<link>` apunta a un PDF real**, no a una página HTML: el servidor responde con
  `Content-Type: application/pdf` y `Content-disposition: inline; filename=2026-6695.pdf`.
- **`<categorias>` se ignora por completo.** La app no lo lee, aunque contendría el organismo ya
  separado y el tipo de anuncio.
- No hay `<description>`, `<guid>` ni `<author>` por entrada.

## Volumen real

Medición de los 19 feeds el 27 de agosto de 2026:

- **17 de las 19 secciones devuelven exactamente 100 entradas.** El propio feed declara ese tope en
  una etiqueta `<size>`. Es el máximo del origen: no hay forma de pedir más ni de paginar hacia atrás.
- **4.3 Seguridad Social** devolvió 9 entradas. Es una sección de bajo movimiento.
- **8.1 Subastas** devolvió **0 entradas**: el feed existe pero llega vacío (`<size>0</size>`).
  Consultar esa sección en la app muestra una lista vacía, sin ningún mensaje explicativo.
- El conjunto completo de los 19 feeds pesó **~745 KB** de XML. Ese es el consumo de datos de *cada*
  carga completa (ver [03 — Ciclo de datos](03-ciclo-de-datos.md)).

La profundidad temporal es por tanto variable: en secciones con mucho movimiento las 100 entradas
pueden cubrir unas pocas semanas; en secciones tranquilas, años. **Un anuncio que sale de la ventana
de 100 deja de ser accesible desde la app**, salvo que estuviera marcado como favorito.
