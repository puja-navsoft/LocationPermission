package com.muve.muve_it_driver;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.muve.muve_it_driver.model.servicecalllisting.DetailItem;
import com.muve.muve_it_driver.util.NetworkUtility;
import com.muve.muve_it_driver.util.RecyclerViewItemClickListenerServiceCallList;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ServiceCallAllListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    RecyclerViewItemClickListenerServiceCallList positionListener;

    // List<DetailItem> mCountriesModelList;
    ArrayList<DetailItem> mCountriesModelList = new ArrayList<>();
    WeakReference<Context> mContextWeakReference;
    Context context;
    Intent myIntent;
    // ArrayList<DetailItem?>= ArrayList()


    public ServiceCallAllListAdapter(/*List<DetailItem> mCountriesModelList,*/ Context context, RecyclerViewItemClickListenerServiceCallList positionListener) {

        // this.mCountriesModelList=mCountriesModelList ;
        this.context = context;
        this.positionListener = positionListener;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicecall_history_recycler_adapter, parent, false), context);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        // Context context = mContextWeakReference.get();
        if (context == null) {
            return;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        DetailItem currentUser = mCountriesModelList.get(position);
        itemViewHolder.tv_servicetime.setText("Service Call no. : " + currentUser.getServiceId().toString());
        itemViewHolder.timing.setText(mCountriesModelList.get(position).getDate().toString() + " " + mCountriesModelList.get(position).getTime().toString());
        //  itemViewHolder.tv_servicePrice.setText(mCountriesModelList.get(position).getDriver_charge().toString());
        itemViewHolder.tv_servicePrice.setText(new DecimalFormat("$0.00").format(Double.valueOf(mCountriesModelList.get(position).getDriver_charge().toString())));
        itemViewHolder.tv_source.setText(mCountriesModelList.get(position).getPickPlace().toString());
        itemViewHolder.tv_destination.setText(mCountriesModelList.get(position).getDropPlace().toString());


        Glide.with(context).load(NetworkUtility.imgbaseUrl/*+"/media/"*/ + mCountriesModelList.get(position).getCustomer_images()).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(itemViewHolder.user_imageView);

        if (mCountriesModelList.get(position).getVehicleType().equals("Cargo Van")) {

            itemViewHolder.carTypeImage.setImageResource(R.drawable.pickupcargo);

        } else {
            itemViewHolder.carTypeImage.setImageResource(R.drawable.van_img);

        }

        if (mCountriesModelList.get(position).getStatus().equals("Cancelled")) {

            itemViewHolder.iv_cancelservice.setVisibility(View.VISIBLE);
            itemViewHolder.iv_cancelservice.setImageResource(R.drawable.canceltag);

        } else if (mCountriesModelList.get(position).getStatus().equals("Completed")) {

            itemViewHolder.iv_cancelservice.setVisibility(View.VISIBLE);
            itemViewHolder.iv_cancelservice.setImageResource(R.drawable.completedtag);
        }

        else if (mCountriesModelList.get(position).getStatus().equals("Incompleted")) {

            itemViewHolder.iv_cancelservice.setVisibility(View.VISIBLE);
            itemViewHolder.iv_cancelservice.setImageResource(R.drawable.incompletetag);
        }

        if (mCountriesModelList.get(position).getStatus().equals("Completed") || mCountriesModelList.get(position).getStatus().equals("Cancelled") || mCountriesModelList.get(position).getStatus().equals("Incompleted")) {

            itemViewHolder.iv_blink.setVisibility(View.GONE);


        } else {
            itemViewHolder.iv_blink.setVisibility(View.VISIBLE);

            final Animation animation = new AlphaAnimation(1, 0);
            animation.setDuration(1000);
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.REVERSE);
            itemViewHolder.iv_blink.startAnimation(animation);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                positionListener.onItemClick(position, mCountriesModelList);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mCountriesModelList.size();
    }

    public void setItems(ArrayList<DetailItem> arrOrderList) {
        this.mCountriesModelList = arrOrderList;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView timing, tv_servicetime, tv_servicePrice, tv_source, tv_destination;
        ImageView iv_blink, carTypeImage, iv_cancelservice;
        ConstraintLayout rowclick;
        CircleImageView user_imageView;

        public ItemViewHolder(View itemView, final Context context) {
            super(itemView);
            timing = (TextView) itemView.findViewById(R.id.textView);
            tv_servicetime = (TextView) itemView.findViewById(R.id.tv_servicetime);
            tv_servicePrice = (TextView) itemView.findViewById(R.id.tv_servicePrice);
            tv_source = (TextView) itemView.findViewById(R.id.tv_source);
            tv_destination = (TextView) itemView.findViewById(R.id.tv_destination);
            rowclick = (ConstraintLayout) itemView.findViewById(R.id.rowclick);
            user_imageView = (CircleImageView) itemView.findViewById(R.id.imageView);
            carTypeImage = (ImageView) itemView.findViewById(R.id.carTypeImage);
            iv_cancelservice = (ImageView) itemView.findViewById(R.id.iv_cancelservice);
            iv_blink = (ImageView) itemView.findViewById(R.id.iv_blink);
        }
    }
}
