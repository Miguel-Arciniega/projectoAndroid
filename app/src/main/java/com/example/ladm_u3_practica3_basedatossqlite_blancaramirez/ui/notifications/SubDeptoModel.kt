package com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.ui.notifications

class SubDeptoModel
    (var idSubDepto: Int, var idEdificio: String?, var piso: String?, var idArea: Int) {

    // toString
    override fun toString(): String {
        return "ID EDIFICIO: $idEdificio\n" +
                "PISO: $piso\n" +
                "ID AREA: $idArea"
    }
}