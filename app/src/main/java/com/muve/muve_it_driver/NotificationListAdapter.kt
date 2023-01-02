package com.muve.muve_it_driver

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it_driver.model.notificationlist.Result


class NotificationListAdapter(val context: Context /*, val positionListener : RecyclerViewItemClickListenerForCardList*/) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mList: ArrayList<com.muve.muve_it_driver.model.notificationlist.Result>? = ArrayList()

    var clickedPos: Int? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification_recycler_adapter, parent, false), context
        )


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemViewHolder: ItemViewHolder = holder as ItemViewHolder
        val currentUser: Result = mList!!.get(position)

        itemViewHolder.tv_msg!!.setText(currentUser.message)

        itemViewHolder.itemView.rootView.setOnClickListener {


            //   positionListener.onItemClick(position, currentUser)

        }


    }

    override fun getItemCount(): Int {

        return mList!!.size

    }

    fun setItems(arrOrderList: List<Result>) {

        mList!!.clear()

        for (i in arrOrderList) {
            mList!!.add(
                Result(
                    i.customerId,
                    i.datetime,
                    i.id,
                    i.isDeleted,
                    i.message
                )
            )
        }

        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: View, context: Context?) :
        RecyclerView.ViewHolder(itemView) {
        var tv_msg: TextView? = null
        // var tv_time: TextView
        //  var iv_click: ImageView

        init {
            tv_msg = itemView.findViewById<TextView>(R.id.tv_msgnoti) as TextView
            //  tv_time = itemView.findViewById<TextView>(R.id.tv_timenoti) as TextView
            //  iv_click = itemView.findViewById<View>(R.id.iv_click) as ImageView
        }
    }
}