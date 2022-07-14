package com.jrblanco.boccantabria.model

import java.time.LocalDate

data class ItemBoc(
    var id: String? = null,
    var organismo: String? = null,
    var descripcion: String? = null,
    var fecha: String? = null,
    var enlace: String? = null,
    var favorito:Boolean = false
)
