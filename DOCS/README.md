# Documentación funcional — BOC Cantabria

Descripción detallada de **qué hace la aplicación** y bajo qué reglas: el comportamiento observable,
las decisiones de negocio y los casos límite. No se describe la arquitectura ni la organización del
código; para eso está `CLAUDE.md` en la raíz.

## Índice

| Documento | Contenido |
|---|---|
| [01 — Visión funcional](01-vision-funcional.md) | Qué es la app, para quién, qué problema resuelve y qué **no** hace |
| [02 — Secciones y fuente de datos](02-secciones-y-fuente-de-datos.md) | Las 19 secciones del BOC, sus feeds y el volumen real de datos |
| [03 — Ciclo de datos](03-ciclo-de-datos.md) | Cuándo descarga, qué descarga y cómo convierte el XML en fichas |
| [04 — Pantallas y flujos](04-pantallas-y-flujos.md) | Recorrido completo: qué ve el usuario y qué pasa al tocar cada elemento |
| [05 — Favoritos](05-favoritos.md) | Reglas de marcado, persistencia y comportamiento del archivo personal |
| [06 — Configuración y tema](06-configuracion-y-tema.md) | Tema oscuro, la pantalla de ajustes y el arranque especial que provoca |
| [07 — Conectividad y errores](07-conectividad-y-errores.md) | Qué pasa sin internet, con red mala o con datos inesperados |
| [08 — Reglas y casos borde](08-reglas-y-casos-borde.md) | Comportamientos no evidentes, límites y discrepancias entre lo esperado y lo real |

## Verificación

Los datos concretos de estos documentos (número de entradas por sección, formato de fechas, forma de
los títulos, tipo de contenido de los enlaces) se comprobaron contra los feeds reales de
`cantabria.es` y contra `boc.cantabria.es` el **27 de agosto de 2026**. Donde un comportamiento
depende del contenido del feed y este puede cambiar, se indica expresamente.
