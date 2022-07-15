package com.jrblanco.boccantabria.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jrblanco.boccantabria.adapter.ListadoBocAdapter
import com.jrblanco.boccantabria.databinding.FragmentListadoBocBinding
import com.jrblanco.boccantabria.model.ItemBoc
import kotlin.ClassCastException

class ListadoBocFragment(private val listaBoc:List<ItemBoc>) : Fragment() {

    private lateinit var binding: FragmentListadoBocBinding

    internal lateinit var callbackFavorito:CallbackFavorito  //

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListadoBocBinding.inflate(layoutInflater)

        initRecyclerViewBoc()

        return binding.root
    }

    /**
     * Iniciamos el recyclerview diciendoles que es de tipo linear y
     * vinculando el adapter a nuestra clase Adapter.
     */
    private fun initRecyclerViewBoc() {
        binding.rvBOC.layoutManager = LinearLayoutManager(context)
        binding.rvBOC.adapter = ListadoBocAdapter(listaBoc) { onItemSelected(it) }

    }

    fun onItemSelected(item:ItemBoc) {
        callbackFavorito.onFavorito(item)
        binding.rvBOC.adapter?.notifyDataSetChanged()  //Actualiza el recyclerview
    }

    /**
     * Interface para el intercambio de informacioón con el Activity
     */
    interface CallbackFavorito{
        fun onFavorito(i:ItemBoc)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callbackFavorito = context as CallbackFavorito
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString())
        }
    }
}