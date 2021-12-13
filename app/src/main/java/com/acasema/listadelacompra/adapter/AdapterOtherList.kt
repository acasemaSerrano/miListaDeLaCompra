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
import com.acasema.listadelacompra.data.model.Permissions

class AdapterOtherList (val context: Context): RecyclerView.Adapter<AdapterOtherList.Viewholder>() {

    private var list: List<Permissions> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_otherlist,parent,false)
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bindItems(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<Permissions>){
        this.list = list
        this.notifyDataSetChanged()
    }

    inner class Viewholder(view: View): RecyclerView.ViewHolder(view){
        /**
         * se podría simplificar con binder pero me parecía pocos campos
         */
        fun bindItems(data: Permissions) {
            val tvListName: TextView = itemView.findViewById(R.id.tvListName)
            val tvOwner: TextView = itemView.findViewById(R.id.tvOwner)
            val tvPermissionType: TextView = itemView.findViewById(R.id.tvPermissionType)

            tvListName.text = data.shopingList
            tvOwner.text = data.owner
            tvPermissionType.text = context.resources.getStringArray(R.array.permissions)[data.permissions.ordinal]
            itemView.setOnClickListener{

                val bundle = bundleOf()
                bundle.putSerializable(context.getString(R.string.KEY_BUNDLE_PERMISSION), data)

                Navigation.findNavController(itemView).navigate(R.id.action_otherListsFragment_to_otherShopingListFragment, bundle)
            }
        }
    }
}