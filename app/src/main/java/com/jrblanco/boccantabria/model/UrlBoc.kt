package com.jrblanco.boccantabria.model

class UrlBoc(var seccion:String? = null, var url:String? = null) {
    companion object {
        val listadoRSS = listOf(
            UrlBoc("1 Disposiciones Generales","https://www.cantabria.es/o/BOC/feed/6802081"),
            UrlBoc("2.1 Autoridades y Personal: Nombramientos, Ceses Y Otras Situaciones","https://www.cantabria.es/o/BOC/feed/6802084"),
            UrlBoc("2.2 Autoridades y Personal: Cursos, Oposiciones y Concursos","https://www.cantabria.es/o/BOC/feed/6802085"),
            UrlBoc("2.3 Autoridades y Personal: Otros","https://www.cantabria.es/o/BOC/feed/6802086"),
            UrlBoc("3 Contratación Administrativa","https://www.cantabria.es/o/BOC/feed/6802087"),
            UrlBoc("4.1 Economía y Hacienda: Actuaciones en Materia Presupuestaria","https://www.cantabria.es/o/BOC/feed/6802089"),
            UrlBoc("4.2 Economía y Hacienda: Actuaciones en Materia Fiscal","https://www.cantabria.es/o/BOC/feed/6802090"),
            UrlBoc("4.3 Economía y Hacienda: Actuaciones en materia de Seguridad Social","https://www.cantabria.es/o/BOC/feed/6802091"),
            UrlBoc("4.4 Economía y Hacienda: Otros","https://www.cantabria.es/o/BOC/feed/6802092"),
            UrlBoc("5 Expropiación Forzosa","https://www.cantabria.es/o/BOC/feed/6802094"),
            UrlBoc("6 Subvenciones y Ayudas","https://www.cantabria.es/o/BOC/feed/6802095"),
            UrlBoc("7.1 Otros Anuncios: Urbanismo","https://www.cantabria.es/o/BOC/feed/6802097"),
            UrlBoc("7.2 Otros Anuncios: Medio Ambiente y Energía","https://www.cantabria.es/o/BOC/feed/6802098"),
            UrlBoc("7.3 Otros Anuncios: Estatutos y Convenios Colectivos","https://www.cantabria.es/o/BOC/feed/6802099"),
            UrlBoc("7.4 Otros Anuncios: Particulares","https://www.cantabria.es/o/BOC/feed/6802100"),
            UrlBoc("7.5 Otros Anuncios: Varios","https://www.cantabria.es/o/BOC/feed/6802301"),
            UrlBoc("8.1 Procedimientos Judiciales: Subastas","https://www.cantabria.es/o/BOC/feed/7479572"),
            UrlBoc("8.2 Procedimientos Judiciales: Otros Anuncios","https://www.cantabria.es/o/BOC/feed/6802303"),
            UrlBoc("9 Elecciones","https://www.cantabria.es/o/BOC/feed/7293890")
        )
    }
}