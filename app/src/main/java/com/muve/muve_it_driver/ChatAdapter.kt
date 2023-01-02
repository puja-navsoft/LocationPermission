package com.muve.muve_it_driver

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it_driver.model.chat.IndividualChatResponse
import kotlin.collections.ArrayList

class ChatAdapter(val context: Context , val driverId : String ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mList: ArrayList<IndividualChatResponse>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chatlayout, parent, false), context
        )


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        // Context context = mContextWeakReference.get();
        if (context == null) {
            return
        }
        val itemViewHolder: ItemViewHolder = holder as ItemViewHolder
        val currentUser: IndividualChatResponse = mList!!.get(position)
        itemViewHolder.tv_msg.setText(currentUser.message)
        itemViewHolder.tv_time.setText(currentUser.timeStamp)
        if (currentUser.senderId.trim() == driverId.trim()){
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END
            }
            itemViewHolder.cvChatContainer.layoutParams=params
        }else{
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.START
            }
            itemViewHolder.cvChatContainer.layoutParams=params
        }

    }

    override fun getItemCount(): Int {

        return mList!!.size

    }
     fun setItems(arrOrderList: List<IndividualChatResponse>) {

         mList!!.clear()

         for (i in arrOrderList){
             mList!!.add(IndividualChatResponse(i.id,i.message,i.receiverId,i.senderId,i.serviceId,i.timeStamp))
         }

         notifyDataSetChanged()
    }

     fun setItemsInserted(arrOrderList: List<IndividualChatResponse>) {

         mList!!.clear()

         for (i in arrOrderList){
             mList!!.add(IndividualChatResponse(i.id,i.message,i.receiverId,i.senderId,i.serviceId,i.timeStamp))
         }

         notifyItemChanged(mList!!.size-1)
    }

    class ItemViewHolder(itemView: View, context: Context?) :
        RecyclerView.ViewHolder(itemView) {
        var cvChatContainer: CardView
        var tv_msg: TextView
        var tv_time: TextView
        var iv_msgstatus: ImageView

        init {
            cvChatContainer = itemView.findViewById<View>(R.id.cvChatContainer) as CardView
            tv_msg = itemView.findViewById<View>(R.id.tv_msg) as TextView
            tv_time = itemView.findViewById<View>(R.id.tv_time) as TextView
            iv_msgstatus = itemView.findViewById<View>(R.id.iv_msgstatus) as ImageView
        }
    }
}