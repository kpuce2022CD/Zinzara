package com.example.smarthomeforstroke

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class RecyclerAdapter(val context: Context, val itemList: ArrayList<DeviceItem>) :
    RecyclerView.Adapter<RecyclerAdapter.Holder>() {

    interface OnItemClickListener{
        fun onItemClick(v:View, data: DeviceItem, pos : Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        return Holder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(itemList[position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val deviceName = itemView?.findViewById<TextView>(R.id.device_name)

        fun bind (device: DeviceItem, context: Context) {
            deviceName!!.text = device.devNum + "번 조명"

            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,device,pos)
                }
            }
        }
    }

}
