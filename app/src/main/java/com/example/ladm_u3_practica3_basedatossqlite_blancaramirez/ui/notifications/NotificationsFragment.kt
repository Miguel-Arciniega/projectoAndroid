package com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.ui.notifications

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.R
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.databinding.FragmentNotificationsBinding
import com.example.ladm_u3_practica3_basedatossqlite_blancaramirez.ui.dashboard.Area

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var listaIDs = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mostrarDatos()

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("INFORMACIÓN")
            .setMessage("PARA BUSCAR UN SUBDEPARTAMENTO:\n\n"+"SE SELECCIONA SI ES POR <IDEFICIO O POR DESCRIPCION O POR DIVISIÓN>, Y DESPUES SE ANOTA EL NOMBRE EN EL CAMPO DE TEXTO, DAR CLICK EN BOTON DE BUSCAR Y SE MOSTRARAN LOS RESULTADOS.\n\n"+
                    "PARA INSERTAR:\n\n"+"AL REGISTRAR TENER EN CUENTA QUE LA DESCRIPCIÓN Y LA DIVISIÓN DEBEN DE EXISTIR EN AREA."+
                    "\n\nPARA ELIMINAR Y ACTUALIZAR:"+ "\n\nSE PRESIONA EL SUBDEPARTAMENTO(EN EL LISTVIEW) Y TE DARÁ LAS OPCIONES AUTOMATICAMENTE"
            ).setNeutralButton("ACEPTAR"){d,i->}
            .show()

        binding.insertar.setOnClickListener {
            var subdepartamento = Subdepartamento(requireContext())

            subdepartamento.idedificio = binding.idedificio.text.toString()
            subdepartamento.piso = binding.piso.text.toString()
            subdepartamento.descripcion = binding.descripcion.text.toString().toUpperCase()
            subdepartamento.division = binding.sDivision.text.toString().toUpperCase()

            val result = subdepartamento.insertar()
            if(result){
                Toast.makeText(requireContext(), "TU SUBDEPARTAMENTO SE INSERTO EXITOSAMENTE",Toast.LENGTH_LONG).show()
                mostrarDatos()
                binding.idedificio.setText("")
                binding.piso.setText("")
                binding.descripcion.setText("")
                binding.sDivision.setText("")
            }else{
                AlertDialog.Builder(requireContext())
                    .setTitle("AVISO DE ERROR")
                    .setMessage("TU SUBDEPARTAMENTO NO SE INSERTO, INSERTE LA DESCRIPCIÓN O LA DIVISIÓN CORRECTA")
                    .show()
            }
        }

        //BOTON DE BUSCAR-----------------------------------------------
        binding.buscar.setOnClickListener {
            val spinner = binding.spinner2.selectedItem.toString()
            val textoBuscar = binding.etBuscar.text.toString().toUpperCase()

            if(spinner == "ID EDIFICIO"){

                val listaSubDpto = Subdepartamento(requireContext()).buscarSubDptoPorIdEdificio(textoBuscar)
                binding.lista.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, listaSubDpto)
            }
            if(spinner == "DIVISION"){

                val listaSubDpto = Subdepartamento(requireContext()).buscarSubDptoPorDivision(textoBuscar)
                binding.lista.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, listaSubDpto)
            }
            if(spinner == "DESCRIPCION"){

                val listaSubDpto = Subdepartamento(requireContext()).buscarSubDptoPorDescripcion(textoBuscar)
                binding.lista.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, listaSubDpto)
            }

            //VERIFICAMOS QUE SI MANDEN DATOS A CONSULTAR.
            if (textoBuscar.isBlank()) {
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("AVISO")
                    .setMessage("TIENES QUE PONER ALGUN DATO, PARA PODER CONSULTAR")
                    .setNeutralButton("ACEPTAR") { d, i -> }
                    .show()
            }else{
                binding.etBuscar.setText("")
            }
        }

        binding.btnMostarTodos.setOnClickListener{
            mostrarDatos()
        }

        return root
    }

    fun mostrarDatos(){
        var lista = Subdepartamento(requireContext()).mostrarAll()
        var informacionSubdepartamento = ArrayList<String>()

        listaIDs.clear()
        (0..lista.size-1).forEach {
            var subdepa = lista.get(it)
            informacionSubdepartamento.add("\nID EDIFICIO: "+subdepa.idedificio+"\nPISO: "+subdepa.piso+"\nID AREA: "+subdepa.idArea+"\n")
            listaIDs.add(subdepa.idsubdepto.toString())
        }
        binding.lista.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, informacionSubdepartamento)

        binding.lista.setOnItemClickListener { adapterView, view, indice, l ->
            val idSub = listaIDs.get(indice)
            val subdepartamento = Subdepartamento(requireContext()).mostrarSubdepartamentoID(idSub)

            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("AVISO")
                .setMessage("¿Que acción deseas hacer?")
                .setNegativeButton("ELIMINAR"){d,i->
                    subdepartamento.eliminar()
                    mostrarDatos()
                }
                .setPositiveButton("ACTUALIZAR"){d,i->
                    val modal = Intent(requireContext(), modal_actualizar_subdepartamento::class.java)

                    modal.putExtra("idsubdepartamento", idSub)
                    startActivity(modal)
                }
                .setNeutralButton("CANCELAR"){d,i->}
                .show()
        }
    }

    override fun onResume() {
        mostrarDatos()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}