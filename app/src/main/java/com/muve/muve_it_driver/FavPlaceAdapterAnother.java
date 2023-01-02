package com.muve.muve_it_driver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.muve.muve_it_driver.UserDetailPojo.FavouritePlacesItem;
import com.muve.muve_it_driver.UserDetailPojo.UserDetailPojo;
import com.muve.muve_it_driver.editfavaddress.EditFavAddress;
import com.muve.muve_it_driver.model.addaddress.AddaddressResponse;
import com.muve.muve_it_driver.preferences.SharedPreferenceManager;
import com.muve.muve_it_driver.restapi.ApiClient;
import com.muve.muve_it_driver.restapi.ApiInterface;
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity;
import com.muve.muve_it_driver.util.AppProgressBar;
import com.muve.muve_it_driver.util.NetworkUtility;
import com.muve.muve_it_driver.util.RecyclerViewItemClickListenerFavPlace;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavPlaceAdapterAnother extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SECTION_VIEW = 0;
    public static final int CONTENT_VIEW = 1;
    RecyclerViewItemClickListenerFavPlace positionListener;

    List<FavouritePlacesItem> mCountriesModelList;
    WeakReference<Context> mContextWeakReference;
    Activity activity;
    Intent myIntent;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor myEdit=null;
    String defaultIdLog="";
    String token="";
    SharedPreferenceManager sharedPreferenceManager = null;



    public FavPlaceAdapterAnother(/*List<FavouritePlacesItem> mCountriesModelList*/ Activity activity, RecyclerViewItemClickListenerFavPlace positionListener) {

        this.mCountriesModelList= new ArrayList<>();
        this.activity = activity;
        this.positionListener = positionListener;


    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fvrtplc_row, parent, false), activity);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        // Context context = mContextWeakReference.get();
        if (activity == null) {
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


        itemViewHolder.et_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAlertDialog(position , mCountriesModelList.get(position).getName(),mCountriesModelList.get(position).getId(), mCountriesModelList.get(position).getAddress(),mCountriesModelList.get(position).getLatitude(), mCountriesModelList.get(position).getLongitude());

            }
        });



    }

    private void setAlertDialog(int position, String name , int favId ,String address , String latitude ,  String longitude) {

        LayoutInflater li = LayoutInflater.from(activity);
        View promptsView = li.inflate(R.layout.custom_alert_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptsView);
        TextView tv_edit = promptsView.findViewById(R.id.tv_edit);
        TextView tv_delete = promptsView.findViewById(R.id.tv_delete) ;
        AlertDialog alert11 = alertDialogBuilder.create();

        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert11.dismiss();

                myIntent = new Intent(activity, EditFavAddress.class);
                myIntent.putExtra("selectposition",position);
                myIntent.putExtra("selectname",name);
                myIntent.putExtra("selectId",favId);
                myIntent.putExtra("selectaddress",address);
                myIntent.putExtra("selectlatitude",latitude);
                myIntent.putExtra("selectlongitude",longitude);
                activity.startActivity(myIntent);

            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteAddress(position , favId  , alert11);

                alert11.dismiss();

            }
        });


        alert11.show();

    }

    private void deleteAddress(int position , int favid ,  AlertDialog alert11) {

        AppProgressBar.openLoader(activity, activity.getResources().getString(R.string.pleasewait));

        sharedPreferences = activity.getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE);
        defaultIdLog = sharedPreferences.getString("idSharedPrefAfterLog","");

        HashMap<String, Object> logReq = new HashMap<>();
        logReq.put("id",favid);
        logReq.put("customer_id", defaultIdLog);
        Call<AddaddressResponse> response1 = ApiClient.getClient(activity).create(ApiInterface.class).doingdeleteaddress(logReq);
        response1.enqueue(new Callback<AddaddressResponse>() {
            @Override
            public void onResponse(Call<AddaddressResponse> call, Response<AddaddressResponse> response) {

                if (response.body().getStatus() == true) {

                        favlocation();


                    AppProgressBar.closeLoader();

                    Toast.makeText(
                            activity,
                            response.body().getDetail(),
                            Toast.LENGTH_LONG
                    ).show();
                }
                else {

                    AppProgressBar.closeLoader();

                    if (response.body() != null) {

                        if (response.body().getDetail().equals(
                                activity.getResources().getString(R.string.youhavebeenloggedinfromanotherdevice)
                        )
                                ) {
                            sharedPreferenceManager = new SharedPreferenceManager(activity);

                            SharedPreferences sharedPreferences =
                                    activity.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            sharedPreferenceManager.logoutUser();
                            editor.clear();
                            editor.commit();


                            Intent myIntent = new
                                    Intent(
                                            activity,
                            WelcomeActivity.class
                                        );
                            activity.startActivity(myIntent);

                            activity.finish();

                        }
                                else {


                            Toast.makeText(
                                    activity,
                            response.body().getDetail().toString(),
                                    Toast.LENGTH_SHORT
                                    ).show();


                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<AddaddressResponse> call, Throwable t) {
                AppProgressBar.closeLoader();
                alert11.dismiss();

                if(t instanceof NoConnectivityException) {
                    // show No Connectivity message to user or do whatever you want.
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();

                }else {

                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void favlocation() {

        AppProgressBar.openLoader(activity, activity.getResources().getString(R.string.pleasewait));

        sharedPreferences = activity.getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE);
        defaultIdLog = sharedPreferences.getString("idSharedPrefAfterLog","");
        token = sharedPreferences.getString("Authorization","");

        HashMap<String, Object> logReq = new HashMap<>();
        logReq.put("id",defaultIdLog);
      //  logReq.put("Authorization",token);
        Call<UserDetailPojo> response1 = ApiClient.getClient(activity).create(ApiInterface.class).getuserdetails(token,logReq);
        response1.enqueue(new Callback<UserDetailPojo>() {
            @Override
            public void onResponse(Call<UserDetailPojo> call, Response<UserDetailPojo> response) {
                AppProgressBar.closeLoader();
                if (response.body().getStatus() == true) {

                    setItems(response.body().getFavouritePlaces());
                    notifyDataSetChanged();
                  /*  Toast.makeText(
                            activity,
                            response.body().getDetail(),
                            Toast.LENGTH_LONG
                    ).show();*/



                }
            }

            @Override
            public void onFailure(Call<UserDetailPojo> call, Throwable t) {
                AppProgressBar.closeLoader();

                if(t instanceof NoConnectivityException) {
                    // show No Connectivity message to user or do whatever you want.
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();

                }else {

                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mCountriesModelList.size();
    }

    public void setItems(List<FavouritePlacesItem> arrOrderList) {
        mCountriesModelList.clear();
        this.mCountriesModelList.addAll(arrOrderList);
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
