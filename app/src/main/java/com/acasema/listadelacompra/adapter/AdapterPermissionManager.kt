package com.acasema.listadelacompra.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.data.model.Permissions

class AdapterPermissionManager(val context: Context): RecyclerView.Adapter<AdapterPermissionManager.Viewholder>() {

    private var list: MutableList<Permissions> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_permissionslist,parent,false)

        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bindItems(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<Permissions>){
        this.list.clear()
        this.list.addAll(list)
        this.notifyDataSetChanged()
    }
    fun getList() :List<Permissions> {
        return list

    }
    private fun delete(position: Int){
        list.removeAt(position)
        this.notifyDataSetChanged()
    }

    private fun update(position: Int, permissions : Permissions){
        list[position] = permissions
        this.notifyDataSetChanged()
    }

    fun addList(email: String, owner: String, shopingList: String) {
        list.add(Permissions(email, owner, Permissions.PermissionsType.observer, shopingList))
        this.notifyDataSetChanged()
    }

    inner class Viewholder(view: View): RecyclerView.ViewHolder(view){

        fun bindItems(data: Permissions, position: Int){
            val tvName: TextView = itemView.findViewById(R.id.tvEmail)
            val btnPermission: Button = itemView.findViewById(R.id.btnPermission)
            val ibtnDelete: ImageButton = itemView.findViewById(R.id.ibtnDelete)

            tvName.text = data.tosomeone
            btnPermission.text = context.resources.getStringArray(R.array.permissions)[data.permissions.ordinal]

            btnPermission.setOnClickListener{
                when (data.permissions){
                    Permissions.PermissionsType.observer ->{
                        data.permissions = Permissions.PermissionsType.buyer
                    }
                    Permissions.PermissionsType.buyer ->{
                        data.permissions = Permissions.PermissionsType.editor
                    }
                    Permissions.PermissionsType.editor ->{
                        data.permissions = Permissions.PermissionsType.observer
                    }
                }
                update(position, data)
            }

            ibtnDelete.setOnClickListener {
                delete(position)
            }
        }
    }
}