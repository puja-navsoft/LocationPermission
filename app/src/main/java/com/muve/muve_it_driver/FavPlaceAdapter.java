package com.muve.muve_it_driver;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.muve.muve_it_driver.UserDetailPojo.FavouritePlacesItem;
import com.muve.muve_it_driver.util.RecyclerViewItemClickListenerFavPlace;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FavPlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;
    RecyclerViewItemClickListenerFavPlace positionListener;

    List<FavouritePlacesItem> mCountriesModelList;
    WeakReference<Context> mContextWeakReference;
    Context context;

    public FavPlaceAdapter(/*List<FavouritePlacesItem> mCountriesModelList*/ Context context, RecyclerViewItemClickListenerFavPlace positionListener) {

        this.mCountriesModelList= new ArrayList<>();
        this.context = context;
        this.positionListener = positionListener;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_recycler_adapter, parent, false), context);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        // Context context = mContextWeakReference.get();
        if (context == null) {
            return;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Random rnd = new Random();
        int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        //itemViewHolder.ryt_home.setBackground(ContextCompat.getDrawable(context, R.drawable.round_white));
        itemViewHolder.cv_main.setCardBackgroundColor(currentColor);
        FavouritePlacesItem currentUser = mCountriesModelList.get(position);
        itemViewHolder.nameTextview.setText(currentUser.getName().toUpperCase());

       /* if (currentUser.getName().equalsIgnoreCase("")){

        }*/


        itemViewHolder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                positionListener.onItemClick(position , mCountriesModelList);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                positionListener.onItemClick(position , mCountriesModelList);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mCountriesModelList.size();
    }

    public void setItems(List<FavouritePlacesItem> arrOrderList) {
        this.mCountriesModelList = arrOrderList;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextview;
        public LinearLayout ryt_home;
        public CardView cv_main;

        public ItemViewHolder(View itemView, final Context context) {
            super(itemView);
            nameTextview = (TextView) itemView.findViewById(R.id.plc);
            ryt_home = (LinearLayout) itemView.findViewById(R.id.ryt_home);
            cv_main = (CardView) itemView.findViewById(R.id.cv_main);
        }
    }
}
