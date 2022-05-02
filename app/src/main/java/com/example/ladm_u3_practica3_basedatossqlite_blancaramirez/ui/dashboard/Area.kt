package com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.ui.dashboard

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.AreaModel
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.BaseDatos

class Area(este: Context){
    private var este = este
    var idarea = 0
    var descripcion = ""
    var division = ""
    var cantidad_empleados = 0
    private var err=""

    //FUNCION DE INSERTAR NUEVA AREA
    fun insertar():Boolean{
        val basedatos = BaseDatos(este,"BD_SUBDEPARTAMENTO_AREA", null,1)
        err = ""
        try{
            val tabla = basedatos.writableDatabase
            var datos = ContentValues()

            datos.put("DESCRIPCION",descripcion)
            datos.put("DIVISION",division)
            datos.put("CANTIDAD_EMPLEADOS",cantidad_empleados)

            val respuesta = tabla.insert("AREA",null,datos)
            if(respuesta==-1L) {
                return false
            }
            }catch(err: SQLiteException) {
                this.err = err.message!!
                return false
            }finally {
                basedatos.close()
            }
        return true
    }

    //FUNCION PARA MOSTRAR TODOS LOS DATOS
    fun mostrarAll(): ArrayList<Area>{
        val baseDatos = BaseDatos(este,"BD_SUBDEPARTAMENTO_AREA", null,1)
        err = ""
        var arreglo = ArrayList<Area>()
        try{
            var tabla = baseDatos.readableDatabase
            val SQLSELECT = "SELECT * FROM AREA"

            var cursor = tabla.rawQuery(SQLSELECT,null)
            if(cursor.moveToFirst()){
                do {
                    val area = Area(este)
                    area.idarea = cursor.getInt(0)
                    area.descripcion = cursor.getString(1)
                    area.division = cursor.getString(2)
                    area.cantidad_empleados = cursor.getInt(3)
                    arreglo.add(area)
                }while (cursor.moveToNext())
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
        }finally {
            baseDatos.close()
        }
        return arreglo
    }

    //BUSQUEDA POR ID DE AREA
    fun buscarAreaPorID(IDAREAS: String): Area{
        val basedatos = BaseDatos(este,"BD_SUBDEPARTAMENTO_AREA", null,1)
        err=""
        val area = Area(este)

        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM AREA WHERE IDAREA=?"
            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(IDAREAS))
            if (cursor.moveToFirst()){
                area.descripcion = cursor.getString(1)
                area.division = cursor.getString(2)
                area.cantidad_empleados = cursor.getInt(3)
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return area
    }

    //ELIMINAR REGISTRO
    fun eliminar(IDAREAS: String):Boolean{
        val basedatos = BaseDatos(este,"BD_SUBDEPARTAMENTO_AREA", null,1)
        err=""
        try {
            var tabla = basedatos.writableDatabase
            val resultado = tabla.delete("AREA","IDAREA=?", arrayOf(IDAREAS))
            if(resultado==0){
                return false
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
            return false
        }finally {
            basedatos.close()
        }
        return true
    }

    //ACTUALIZAR DATOS
    fun actualizar(IDAREAS: String):Boolean{
        val basedatos = BaseDatos(este,"BD_SUBDEPARTAMENTO_AREA", null,1)
        err=""
        try {
            val tabla = basedatos.writableDatabase
            val datosActualizados = ContentValues()

            datosActualizados.put("DESCRIPCION",descripcion)
            datosActualizados.put("DIVISION",division)
            datosActualizados.put("CANTIDAD_EMPLEADOS",cantidad_empleados)

            val respuesta = tabla.update("AREA",datosActualizados,"IDAREA=?", arrayOf(IDAREAS))

            if (respuesta==0){
                return false
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
            return false
        }finally {
            basedatos.close()
        }
        return true
    }

    //----------------------------------------------------------------------------
    //BUSCAR POR DIVSION
    fun buscarAreaPorDivision(division: String) : List<AreaModel>{
        val basedatos = BaseDatos(este,"BD_SUBDEPARTAMENTO_AREA", null,1)

        val returnList = ArrayList<AreaModel>()
        val consulta =
            "SELECT * " +
            "FROM AREA " +
            "WHERE DIVISION = ?"

        val tabla = basedatos.writableDatabase
        val cursor = tabla.rawQuery(consulta, arrayOf(division))

        if (cursor.moveToFirst()){
            do {
                val areaId = cursor.getInt(0)
                val description = cursor.getString(1)
                val division = cursor.getString(2)
                val cantidadEmpleados = cursor.getInt(3)

                val nuevaArea = AreaModel(areaId, description, division, cantidadEmpleados)
                returnList.add(nuevaArea)

            } while (cursor.moveToNext())
        }

        cursor.close()
        basedatos.close()
        return  returnList
    }

    //BUSCAR POR DESCRIPCION
    fun buscarAreaPorDescripcion(descripcion: String): ArrayList<AreaModel>{
        val basedatos = BaseDatos(este,"BD_SUBDEPARTAMENTO_AREA", null,1)

        val returnList = ArrayList<AreaModel>()
        val consulta =
            "SELECT * " +
            "FROM AREA " +
            "WHERE DESCRIPCION = ?"

        val tabla = basedatos.writableDatabase
        val cursor = tabla.rawQuery(consulta, arrayOf(descripcion))

        if (cursor.moveToFirst()){
            do {
                val areaId = cursor.getInt(0)
                val description = cursor.getString(1)
                val division = cursor.getString(2)
                val cantidadEmpleados = cursor.getInt(3)

                val nuevaArea = AreaModel(areaId, description, division, cantidadEmpleados)
                returnList.add(nuevaArea)

            } while (cursor.moveToNext())
        }

        cursor.close()
        basedatos.close()
        return  returnList
    }


}