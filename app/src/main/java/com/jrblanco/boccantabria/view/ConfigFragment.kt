package com.jrblanco.boccantabria.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.jrblanco.boccantabria.adapter.ListadoBocAdapter
import com.jrblanco.boccantabria.adapter.ListadoUrlBocAdapter
import com.jrblanco.boccantabria.databinding.FragmentConfigBinding
import com.jrblanco.boccantabria.databinding.FragmentListadoBocBinding
import com.jrblanco.boccantabria.model.UrlBoc

class ConfigFragment : Fragment() {

    private lateinit var binding: FragmentConfigBinding

    private var listaUrl = ArrayList<UrlBoc>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentConfigBinding.inflate(layoutInflater)

        val pref: SharedPreferences? = context?.getSharedPreferences("com.jrblanco.temaoscuro",0)

        val activado = pref?.getBoolean("valor",false)

        binding.swTemaOscuro.isChecked = activado!!

        binding.swTemaOscuro.setOnClickListener {
            if (binding.swTemaOscuro.isChecked) {
                AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_YES))
                pref?.edit()?.putBoolean("valor",true)?.apply()

            } else {
                AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_NO))
                pref?.edit()?.putBoolean("valor",false)?.apply()
            }
            pref?.edit()?.putBoolean("cambiadotema",true)?.apply()
        }


        initRecyclerView()

        return binding.root
    }


    private fun initRecyclerView() {
        binding.rvUrlBOC.layoutManager = LinearLayoutManager(context)
        binding.rvUrlBOC.adapter = ListadoUrlBocAdapter(UrlBoc.listadoRSS)
    }


}