package com.muve.muve_it_driver

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it_driver.model.earninglist.Detail
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class EarningAdapter(val context: Context /*, val positionListener : RecyclerViewItemClickListenerForCardList*/) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mList: ArrayList<Detail>? = ArrayList()

    var clickedPos: Int? =null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_earning_recycler_adapter, parent, false), context
        )


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemViewHolder: ItemViewHolder = holder as ItemViewHolder
        val currentUser: Detail = mList!!.get(position)


        var date: String = currentUser.date
       // var spf = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA)
        var spf = SimpleDateFormat("MM-dd-yyyy", Locale.CANADA)
        val newDate = spf.parse(date)
        spf = SimpleDateFormat("E, MMMM dd")
        date = spf.format(newDate)
        println(date)
        itemViewHolder.tv_time.setText(date /*+ ", " + currentUser.date*/)

       // itemViewHolder.tv_time.setText(currentUser.date)
        itemViewHolder.tv_chrge.setText("$ "+currentUser.charge.toDouble())

        itemViewHolder.itemView.rootView.setOnClickListener {


         //   positionListener.onItemClick(position, currentUser)

        }




    }

    override fun getItemCount(): Int {

        return mList!!.size

    }

    fun setItems(arrOrderList: List<Detail>) {

        mList!!.clear()

        for (i in arrOrderList) {
            mList!!.add(
                Detail(
                    i.charge,
                    i.date
                )
            )
        }

        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: View, context: Context?) :
        RecyclerView.ViewHolder(itemView) {
        var tv_time: TextView
        var tv_chrge: TextView
      //  var iv_click: ImageView

        init {

            tv_time = itemView.findViewById<TextView>(R.id.tv_msgnoti) as TextView
            tv_chrge = itemView.findViewById<TextView>(R.id.tv_timenoti) as TextView

        }
    }
}