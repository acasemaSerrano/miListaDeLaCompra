package com.acasema.listadelacompra.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.data.model.Element
import com.acasema.listadelacompra.ui.contract.BuyViewModelContract
import java.time.LocalDate
import java.time.format.DateTimeFormatter
/**
 * autor: acasema (alfonso)
 * clase estándar que deriva de adapter sirve para listar los elementos a comprar
 */
class AdapterListBuy(val context: Context,val viewModel: BuyViewModelContract): RecyclerView.Adapter<AdapterListBuy.Viewholder>() {

    private var list: List<Element> = listOf()
    private var formatters = DateTimeFormatter.ofPattern(context.getString(R.string.dateFormate))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_buylist,parent,false)
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
        notifyDataSetChanged()
    }
    fun getList(): List<Element>{
        return list
    }


    inner class Viewholder(view: View): RecyclerView.ViewHolder(view){
        fun bindItems(data: Element){

            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
            val btBuy: Button = itemView.findViewById(R.id.btBuy)

            tvName.text = data.name
            tvAmount.text = data.amount

            btBuy.setOnClickListener {
                data.isBought = !data.isBought
                if (data.isBought) {
                    itemView.setBackgroundColor(context.getColor(R.color.lotus))
                    data.lastDatePurchased = LocalDate.now().format(formatters)
                } else{
                    itemView.setBackgroundColor(context.getColor(R.color.white))
                    data.lastDatePurchased = null
                }
                viewModel.setBought(data)
            }
        }
    }
}