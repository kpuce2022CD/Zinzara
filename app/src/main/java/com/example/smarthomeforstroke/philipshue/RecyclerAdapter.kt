package com.example.smarthomeforstroke

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeforstroke.R
import java.text.SimpleDateFormat
import java.util.*

class RecyclerAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var datas = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val deviceName: TextView = itemView.findViewById(R.id.device_name)

        fun bind(item: String) {

            deviceName.text = item + "번 조명"

            itemView.setOnClickListener {
//                var newIntent = Intent(context, TodoEdit::class.java)  // 리싸이클러뷰 항목 클릭시 ModifyTodo 화면 띄움
//                newIntent.putExtra("Title", item.taskTitle)
//
//                newIntent.putExtra("Dyear", item.dday.year)
//                newIntent.putExtra("Dmonth", item.dday.month)
//                newIntent.putExtra("Ddate", item.dday.date)
//                newIntent.putExtra("Percent",item.percent)
//
//                newIntent.putExtra("ID", item.id)
//                context.startActivity(newIntent)  // 선택한 일정 정보들을 모두 넘겨주고 화면 띄워줌
            }
        }
    }


}
