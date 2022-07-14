package com.jrblanco.boccantabria.adapter


import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.jrblanco.boccantabria.R
import com.jrblanco.boccantabria.databinding.ItemBocBinding
import com.jrblanco.boccantabria.model.ItemBoc

class ListadoBocAdapter(val listaBoc:List<ItemBoc>, private val onClickFavorito:(ItemBoc) -> Unit): RecyclerView.Adapter<ListadoBocAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutInflater.inflate(R.layout.item_boc,parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = listaBoc[position]
        holder.render(item,onClickFavorito)
    }

    override fun getItemCount(): Int {
        return listaBoc.size
    }

    class ItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        private val binding = ItemBocBinding.bind(view)

        fun render(itemBocModel: ItemBoc, onClickFavorito:(ItemBoc) -> Unit){

            if (itemBocModel.id?.isBlank() == true) {
                Log.v("TAG","Has llegado al último")
                binding.cardview.visibility = View.INVISIBLE
            } else {
                binding.cardview.visibility = View.VISIBLE
            }

            binding.tvOrganismo.text = itemBocModel.organismo
            binding.tvDescripcion.text = itemBocModel.descripcion
            binding.tvFecha.text = itemBocModel.fecha

            if (itemBocModel.favorito) {
                binding.ivFavorito.setImageResource(R.drawable.ic_estrella_azul)
            } else {
                binding.ivFavorito.setImageResource(R.drawable.ic_estrella_blanca)
            }


            //==================== EVENTOS DEL ITEM ====================

            // Para controlar el click en la estrella
            binding.ivFavorito.setOnClickListener {
                if (itemBocModel.favorito) {
                    binding.ivFavorito.setImageResource(R.drawable.ic_estrella_blanca)
                    itemBocModel.favorito = false
                } else {
                    binding.ivFavorito.setImageResource(R.drawable.ic_estrella_azul)
                    itemBocModel.favorito = true
                }
                onClickFavorito(itemBocModel)

            }

            // Para controlar el CLICK en el Botón de descargar PDF
            binding.btnDownloadPdf.setOnClickListener {
                val urlPdf = Uri.parse(itemBocModel.enlace)
                var intentWeb = Intent(Intent.ACTION_VIEW,urlPdf)
                startActivity(binding.cardview.context,intentWeb,null)
            }
        }

    }
}