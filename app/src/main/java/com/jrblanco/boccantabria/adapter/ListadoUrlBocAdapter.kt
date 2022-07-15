package com.jrblanco.boccantabria.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jrblanco.boccantabria.R
import com.jrblanco.boccantabria.databinding.ItemUrlbocBinding
import com.jrblanco.boccantabria.model.UrlBoc

class ListadoUrlBocAdapter(val listaUrl:List<UrlBoc>) : RecyclerView.Adapter<ListadoUrlBocAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutInflater.inflate(R.layout.item_urlboc,parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = listaUrl[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return listaUrl.size
    }

    class ItemViewHolder(val view:View):RecyclerView.ViewHolder(view) {

        private val binding = ItemUrlbocBinding.bind(view)

        fun render(item: UrlBoc) {
            binding.tvSeccion.text = item.seccion
            binding.tvUrl.text = item.url
        }
    }

}