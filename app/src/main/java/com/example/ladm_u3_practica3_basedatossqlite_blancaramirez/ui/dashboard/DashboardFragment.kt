package com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.ui.dashboard

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    var listaIDs = ArrayList<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mostrarDatos()

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
        .setTitle("INFORMACIÓN")
        .setMessage("PARA BUSCAR UN AREA:\n\n"+"SE SELECCIONA SI ES POR <DESCRIPCIÓN O POR DIVISIÓN>, Y DESPUES SE ANOTA EL NOMBRE EN EL CAMPO DE TEXTO, DAR CLICK EN BOTON DE BUSCAR Y SE MOSTRARAN LOS RESULTADOS.\n\n"+
                "PARA MOSTRAR DATOS:\n\n"+"DAR CLICK A <MOSTRAR TODAS LAS AREAS> PARA VISUALIZARLAS."+
                "\n\nPARA ELIMINAR Y ACTUALIZAR:"+"\n\nSE PRESIONA EL AREA(EN EL LISTVIEW) Y TE DARÁ LAS OPCIONES AUTOMATICAMENTE"
        ).setNeutralButton("ACEPTAR"){d,i->}
        .show()

        //BOTON DE INSERTAR UN AREA
        binding.insertar.setOnClickListener {
            var area = Area(requireContext())

            area.descripcion = binding.descripcion.text.toString().toUpperCase()
            area.division = binding.division.text.toString().toUpperCase()
            area.cantidad_empleados = binding.cantidadEmpleados.text.toString().toInt()
            //System.out.println("SI ENTRO")

            val resultado = area.insertar()

            if (resultado){
                Toast.makeText(requireContext(),"TU AREA SE INSERTO EXITOSAMENTE", Toast.LENGTH_LONG)
                    .show()
                mostrarDatos()

                //LIMPIAMOS LAS CAJAS DE TEXTO
                binding.descripcion.setText("")
                binding.division.setText("")
                binding.cantidadEmpleados.setText("")
            }else{
                AlertDialog.Builder(requireContext())
                    .setTitle("ERROR")
                    .setMessage("NO SE PUDO INSERTAR TU AREA")
                    .show()
            }
        }

        //PROGRAMAMOS LA BUSQUEDA
        binding.buscar.setOnClickListener {
            val spinner = binding.spinner.selectedItem.toString()
            val textoBuscar = binding.etBuscarChido.text.toString().toUpperCase()

            if(spinner == "DIVISION"){

                val listaAreas = Area(requireContext()).buscarAreaPorDivision(textoBuscar)
                binding.lista.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, listaAreas)
            }
            if(spinner == "DESCRIPCION"){

                val listaAreas = Area(requireContext()).buscarAreaPorDescripcion(textoBuscar)
                binding.lista.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, listaAreas)
            }

            //VERIFICAMOS QUE SI MANDEN DATOS A CONSULTAR.
            if (textoBuscar.isBlank()) {
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("AVISO")
                    .setMessage("TIENES QUE PONER ALGUN DATO, PARA PODER CONSULTAR")
                    .setNeutralButton("ACEPTAR") { d, i -> }
                    .show()
            }else{
                binding.etBuscarChido.setText("")
            }
        }


        //AQUI MOSTRAMOS TODOS LOS REGISTROS ACTUALES.
        binding.mostrar.setOnClickListener {
            mostrarDatos()
            //LIMPIAMOS LAS CAJAS DE TEXTO
            binding.descripcion.setText("")
            binding.division.setText("")
            binding.cantidadEmpleados.setText("")
        }
        return root
    }

    //PARA MOSTRAR DATOS EN EL LISTVIEW
    fun mostrarDatos(){
        var listaArea = Area(requireContext()).mostrarAll()
        var descripcionArea = ArrayList<String>()
        var divisionArea = ArrayList<String>()
        var cantidadEmpleadosArea = ArrayList<Int>()
        var cad = ArrayList<String>()
        var cad2 = ""

        listaIDs.clear()
        (0..listaArea.size-1).forEach{
            val au = listaArea.get(it)
            descripcionArea.add(au.descripcion)
            divisionArea.add(au.division)
            cantidadEmpleadosArea.add(au.cantidad_empleados)
            listaIDs.add(au.idarea.toString())
            cad2="ID AREA:"+listaIDs[it]+" \nDescripción:  "+descripcionArea[it]+" \nDivisión: "+divisionArea[it]+" \n# Empleados: "+cantidadEmpleadosArea[it]
            cad.add(cad2)
        }
        binding.lista.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,cad)
        binding.lista.setOnItemClickListener{adapterView, view, indice, l ->
            val idArea = listaIDs.get(indice)
            val area = Area(requireContext()).buscarAreaPorID(idArea)

            AlertDialog.Builder(requireContext())
                .setTitle("AVISO")
                .setMessage("¿Que accion deseas hacer?")
                .setNegativeButton("ELIMINAR"){d,i->
                    area.eliminar(idArea)
                    mostrarDatos()
                }
                .setPositiveButton("ACTUALIZAR"){d,i->
                    var modal = Intent(requireContext(),ModalActualizar::class.java)
                    modal.putExtra("idarea",idArea)
                    startActivity(modal)
                }
                .setNeutralButton("Cancelar"){d,i->}
                .show()
        }
    }


    override fun onResume(){
        mostrarDatos()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}