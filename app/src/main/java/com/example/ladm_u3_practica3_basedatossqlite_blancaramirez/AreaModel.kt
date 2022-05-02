package com.example.ladm_u3_practica3_basedatossqlite_blancaramirez

class AreaModel
    (var idArea: Int, var descripcion: String?, var division: String?, var cantidadEmpelados: Int) {

    // toString
    override fun toString(): String {
        return "ID AREA: $idArea\n" +
                "Descripcion: $descripcion\n" +
                "Division: $division\n" +
                "# Empleados: $cantidadEmpelados"
    }
}