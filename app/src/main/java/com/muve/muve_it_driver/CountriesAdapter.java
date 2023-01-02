package com.muve.muve_it_driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muve.muve_it_driver.model.countrymodel.DataItem;
import com.muve.muve_it_driver.ui.auth.choose_country.SelectCountryCodeFragment;
import com.muve.muve_it_driver.util.RecyclerViewItemClickListener;

import java.util.ArrayList;

public class CountriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;
    RecyclerViewItemClickListener positionListener;

    ArrayList<DataItem> mCountriesModelList;
   // WeakReference<Context> mContextWeakReference;
   SelectCountryCodeFragment context;

    public CountriesAdapter(ArrayList<DataItem> mCountriesModelList,   SelectCountryCodeFragment context , RecyclerViewItemClickListener positionListener) {

        this.mCountriesModelList = mCountriesModelList;
        this.context = context;
        this.positionListener = positionListener;


       // this.mContextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      //  Context context = mContextWeakReference.get();
        if (viewType == SECTION_VIEW) {
            return new SectionHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_recycler_adapter, parent, false));
        }
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_recycler_adapter2, parent, false), context.requireContext());
    }

    @Override
    public int getItemViewType(int position) {
        if (mCountriesModelList.get(position).isSection()) {
            return SECTION_VIEW;
        } else {
            return CONTENT_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

       // Context context = mContextWeakReference.get();
        if (context == null) {
            return;
        }
        if (SECTION_VIEW == getItemViewType(position)) {

            SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) holder;
            DataItem sectionItem = mCountriesModelList.get(position);
            sectionHeaderViewHolder.headerTitleTextview.setText(sectionItem.getCountry());
            return;
        }

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        DataItem currentUser = ((DataItem) mCountriesModelList.get(position));
        itemViewHolder.nameTextview.setText(currentUser.getCountry() + "( + "+currentUser.getCode()+")");



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

    public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitleTextview;

        public SectionHeaderViewHolder(View itemView) {
            super(itemView);
            headerTitleTextview = (TextView) itemView.findViewById(R.id.headerTitleTextview);
        }
    }
}