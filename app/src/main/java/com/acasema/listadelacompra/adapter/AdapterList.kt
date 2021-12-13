package com.acasema.listadelacompra.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.data.model.ShopingList

/**
 * clase estándar que deriva de adapter
 */
class AdapterList(val context:Context): RecyclerView.Adapter<AdapterList.Viewholder>() {

    private var list: List<ShopingList> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_lists,parent,false)

        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bindItems(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    public fun setList(list: List<ShopingList>){
        this.list = list
        this.notifyDataSetChanged()
    }


    inner class Viewholder(view: View): RecyclerView.ViewHolder(view){
        /**
         * se podría simplificar con binder pero me parecía pocos campos
         */
        fun bindItems(data: ShopingList) {
            val tvListName: TextView = itemView.findViewById(R.id.tvListName)
            val tvState: TextView = itemView.findViewById(R.id.tvState)

            tvListName.text = data.name
            tvState.text =
                if (data.online == true) context.getString(R.string.online) else context.getString(R.string.offline)

            itemView.setOnClickListener{

                val bundle = bundleOf()
                bundle.putString(context.getString(R.string.KEY_BUNDLE_LISTNAME), data.name)
                bundle.putBoolean(context.getString(R.string.KEY_BUNDLE_ONLINE), data.online!!)


                Navigation.findNavController(itemView).navigate(R.id.action_listOfListFragment_to_listViewfragment, bundle)
            }
        }
    }
}