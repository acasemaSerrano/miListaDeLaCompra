package com.acasema.listadelacompra.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.data.model.Element
/**
 * autor: acasema (alfonso)
 * clase estándar que deriva de adapter sirve para ver la lista de los elementos
 */
class AdapterListView (val context: Context): RecyclerView.Adapter<AdapterListView.Viewholder>() {

    private var list: List<Element> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_historylist,parent,false)

        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bindItems(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<Element>){
        this.list = list
        this.notifyDataSetChanged()
    }


    inner class Viewholder(view: View): RecyclerView.ViewHolder(view){

        fun bindItems(data: Element){
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
            val tvDate: TextView = itemView.findViewById(R.id.tvDate)

            tvName.text = data.name
            tvAmount.text = data.amount
            tvDate.text = if (data.isBought) data.lastDatePurchased else context.getString(R.string.notBought)

        }
    }
}