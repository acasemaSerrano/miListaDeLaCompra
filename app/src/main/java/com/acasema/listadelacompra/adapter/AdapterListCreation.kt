package com.acasema.listadelacompra.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import androidx.recyclerview.widget.RecyclerView

import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.data.model.Element

class AdapterListCreation (val context: Context): RecyclerView.Adapter<AdapterListCreation.Viewholder>() {

    private var list: MutableList<Element> = mutableListOf()
    private lateinit var shopingListName: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterListCreation.Viewholder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_creationlist,parent,false)

        return Viewholder(v, NameElementEditTextListener(), AmountElementEditTextListener())
    }

    override fun onBindViewHolder(holder: AdapterListCreation.Viewholder, position: Int) {

        holder.nameElementEditTextListener.updatePosition(holder.adapterPosition)
        holder.amountElementEditTextListener.updatePosition(holder.adapterPosition)

        holder.etName.text =Editable.Factory().newEditable(list[holder.adapterPosition].name)
        holder.etAmount.text =Editable.Factory().newEditable(list[holder.adapterPosition].amount)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun getList(): List<Element> {
        return list
    }

    fun setShopingListName(shopingListName : String){
        this.shopingListName = shopingListName
        for (element in list)
            element.shopingListName = shopingListName
    }

    fun addNewElement(){
        list.add(Element("","", false, null,shopingListName))
        reloadView()
    }
    fun removeLastItem(){
        list.removeLast()
        reloadView()
    }

    fun setList(elementList: List<Element>) {
        list.addAll(elementList)
        reloadView()
    }

    fun addElement(nameElement: String) {
        if(list.last().name == "")
            list.last().name = nameElement
        else
            list.add(Element(nameElement,"", false, null,shopingListName))

        reloadView()
    }
    private fun reloadView(){
        this.notifyDataSetChanged()
    }


    inner class Viewholder(view: View ): RecyclerView.ViewHolder(view){

        lateinit var nameElementEditTextListener : NameElementEditTextListener
        lateinit var amountElementEditTextListener : AmountElementEditTextListener
        var etName: EditText = itemView.findViewById(R.id.etName)
        var etAmount: EditText = itemView.findViewById(R.id.etAmount)

        constructor(view: View, nameElementEditTextListener : NameElementEditTextListener,
                    amountElementEditTextListener : AmountElementEditTextListener) : this(view) {

            this.nameElementEditTextListener = nameElementEditTextListener
            this.amountElementEditTextListener = amountElementEditTextListener
            etName.addTextChangedListener(nameElementEditTextListener)
            etAmount.addTextChangedListener(amountElementEditTextListener)
        }
    }
    inner class NameElementEditTextListener : TextWatcher {
        private var position = 0

        fun updatePosition(position: Int) {
            this.position = position
        }

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            list[position].name = charSequence.toString()
        }
        override fun afterTextChanged(editable: Editable) {}
    }
    inner class AmountElementEditTextListener : TextWatcher {
        private var position = 0

        fun updatePosition(position: Int) {
            this.position = position
        }

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
            list[position].amount = charSequence.toString()
        }
        override fun afterTextChanged(editable: Editable) {}
    }

    //solución facil mRecyclerview.setItemViewCacheSize(mDataset.size());

}