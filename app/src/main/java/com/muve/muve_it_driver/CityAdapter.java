package com.muve.muve_it_driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muve.muve_it_driver.model.aboutusmodel.cityDataItemAbout;
import com.muve.muve_it_driver.util.RecyclerViewItemClickListenerPreferedCity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;
    RecyclerViewItemClickListenerPreferedCity positionListener;

    ArrayList<cityDataItemAbout> mCountriesModelList;
    WeakReference<Context> mContextWeakReference;
    Context context;

    public CityAdapter(ArrayList<cityDataItemAbout> mCountriesModelList, Context context, RecyclerViewItemClickListenerPreferedCity positionListener) {

        this.mCountriesModelList = mCountriesModelList;
        this.context = context;
        this.positionListener = positionListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_recycler_adapter2, parent, false), context);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        // Context context = mContextWeakReference.get();
        if (context == null) {
            return;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        cityDataItemAbout currentUser = ((cityDataItemAbout) mCountriesModelList.get(position));
        itemViewHolder.nameTextview.setText(currentUser.getCity_name()/*.toUpperCase()*/);



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

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextview;

        public ItemViewHolder(View itemView, final Context context) {
            super(itemView);
            nameTextview = (TextView) itemView.findViewById(R.id.txt_country);
        }
    }
}
