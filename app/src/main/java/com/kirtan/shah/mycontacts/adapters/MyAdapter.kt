package com.kirtan.shah.mycontacts.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.kirtan.shah.mycontacts.R
import com.kirtan.shah.mycontacts.model.Contact

class MyAdapter(val clickHandler: ClickHandler) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var list = listOf<Contact>()

    @SuppressLint("NotifyDataSetChanged")
    fun setContentList(list: List<Contact>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name = view.findViewById<TextView>(R.id.contactname)
        val phoneno = view.findViewById<TextView>(R.id.contactphoneno)
        val namecap=view.findViewById<TextView>(R.id.contactImageTextView)
        val contactcard = view.findViewById<LinearLayout>(R.id.cardLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.phoneno.text = list[position].phone
        if(list[position].name.isNotEmpty())
        {
            holder.namecap.text = list[position].name.substring(0,1).toString()
        }
        else
        {
            holder.namecap.text = "*"
        }

        holder.contactcard.setOnClickListener {
            clickHandler.handleClick(list[position])
        }

    }

    override fun getItemCount(): Int {
        return this.list.size
    }

}