package com.muve.muve_it_driver;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.muve.muve_it_driver.UserDetailPojo.FavouritePlacesItem;
import com.muve.muve_it_driver.addfavplac.AddFavPlacScreen;
import com.muve.muve_it_driver.util.RecyclerViewItemClickListenerFavPlace;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FavPlaceAdapterAnotherforDestination extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;
    RecyclerViewItemClickListenerFavPlace positionListener;

    List<FavouritePlacesItem> mCountriesModelList;
    WeakReference<Context> mContextWeakReference;
    Context context;
    Intent myIntent;

    public FavPlaceAdapterAnotherforDestination(/*List<FavouritePlacesItem> mCountriesModelList*/ Context context, RecyclerViewItemClickListenerFavPlace positionListener) {

        this.mCountriesModelList= new ArrayList<>();
        this.context = context;
        this.positionListener = positionListener;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fvrtplc_row_destination, parent, false), context);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        // Context context = mContextWeakReference.get();
        if (context == null) {
            return;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        FavouritePlacesItem currentUser = mCountriesModelList.get(position);
        itemViewHolder.nameTextview.setText(Character.toUpperCase(currentUser.getName().charAt(0))  + currentUser.getName().substring(1));
        itemViewHolder.tv_subAd.setText(Character.toUpperCase(currentUser.getAddress().charAt(0))  + currentUser.getAddress().substring(1));

        if(currentUser.getName().equals("Home") || currentUser.getName().equals("home") ) {

            itemViewHolder.imageView2.setImageResource(R.drawable.homefav);
        }else if (currentUser.getName().equals("Office") || currentUser.getName().equals("office")){

            itemViewHolder.imageView2.setImageResource(R.drawable.officefav);

        }else {
            itemViewHolder.imageView2.setImageResource(R.drawable.ic_baseline_location_on);


        }

        itemViewHolder.rowclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                positionListener.onItemClick(position , mCountriesModelList);
            }
        });

        itemViewHolder.et_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myIntent = new Intent(context, AddFavPlacScreen.class);
                //  myIntent.putExtra("locationscreen","Camera")
                context.startActivity(myIntent);

                /*positionListener.onItemClick(position , mCountriesModelList);*/
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

        public TextView nameTextview,tv_subAd;
        ImageView imageView2,et_edit;
        ConstraintLayout rowclick;

        public ItemViewHolder(View itemView, final Context context) {
            super(itemView);
            nameTextview = (TextView) itemView.findViewById(R.id.tv_Addrss);
            tv_subAd = (TextView) itemView.findViewById(R.id.tv_subAd);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            rowclick = (ConstraintLayout) itemView.findViewById(R.id.rowclick);
            et_edit = (ImageView) itemView.findViewById(R.id.et_edit);
        }
    }
}
