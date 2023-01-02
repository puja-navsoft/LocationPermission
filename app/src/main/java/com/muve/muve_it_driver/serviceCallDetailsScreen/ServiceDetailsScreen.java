package com.muve.muve_it_driver.serviceCallDetailsScreen;

import static com.muve.muve_it_driver.util.NetworkUtility.MAP_DIRECTIONS_BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.muve.muve_it_driver.NoConnectivityException;
import com.muve.muve_it_driver.R;
import com.muve.muve_it_driver.model.servicecallstatuswisedetails.ServiceCallStatuswiseetailsResponse;
import com.muve.muve_it_driver.preferences.SharedPreferenceManager;
import com.muve.muve_it_driver.restapi.ApiClient;
import com.muve.muve_it_driver.restapi.ApiInterface;
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity;
import com.muve.muve_it_driver.util.AppProgressBar;
import com.muve.muve_it_driver.util.NetworkUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailsScreen extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    ImageView iv_back , tv_carImg ;
    CircleImageView iv_userImg;
    GoogleMap mMap;
    ArrayList<LatLng> locationArrayList;
    private Polyline mPolyline;
    Double dist = 0.0;
    LatLng mOrigin1; /*= new LatLng(22.54322, 88.38594);*/
    LatLng mDestination1 ;/*= new LatLng(22.54383, 88.38729);*/
    TextView  tv_payType,vehicleType,textView85,tv_rating,tv_driverid,tv_username,tv_cartype,tv_source,tv_price,tv_dest,tv_km,price,tv_srvcCall,tv_distance,tv_servicenumber,tv_serviceloaderText,tv_cancelPickup,tv_time;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor myEdit=null;
    String defaultIdLog="";
    String defaultId="";
    String token="";
    String fnameSharedPrefAfterReg="";
    String lnameSharedPrefAfterReg="";
    String tokenReg="";
    RatingBar ratingbar;
    CardView crdsprt;
    Call<ServiceCallStatuswiseetailsResponse> response1;
    SharedPreferenceManager sharedPreferenceManager = null ;
    String firstWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details_screen);

        iv_back = (ImageView)findViewById(R.id.iv_back) ;
        tv_source = (TextView)findViewById(R.id.tv_source) ;
        tv_dest = (TextView)findViewById(R.id.tv_destination) ;
        tv_time = (TextView) findViewById(R.id.tv_time) ;
        tv_price = (TextView) findViewById(R.id.tv_price) ;
        tv_servicenumber = (TextView) findViewById(R.id.tv_servicenumber) ;
        tv_cartype = (TextView) findViewById(R.id.tv_cartype) ;
        tv_username = (TextView) findViewById(R.id.tv_username) ;
        tv_rating = (TextView) findViewById(R.id.tv_rating) ;
        tv_driverid = (TextView) findViewById(R.id.tv_driverid) ;
        ratingbar = (RatingBar) findViewById(R.id.ratingbar) ;
        tv_distance = (TextView) findViewById(R.id.tv_distance) ;
        textView85 = (TextView) findViewById(R.id.textView85) ;
        tv_carImg = (ImageView) findViewById(R.id.tv_carImg) ;
        iv_userImg = (CircleImageView) findViewById(R.id.iv_userImg) ;
        crdsprt = (CardView) findViewById(R.id.crdsprt) ;
        mapView = (MapView)findViewById(R.id.map);
        tv_payType = (TextView) findViewById(R.id.tv_payType);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(this);

        try {
            sharedPreferenceManager = new SharedPreferenceManager(this);

            sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE);
            defaultIdLog = sharedPreferences.getString("idSharedPrefAfterLog","");
            defaultId = sharedPreferences.getString("idSharedPref","");
            token = sharedPreferences.getString("Authorization","");
            fnameSharedPrefAfterReg = sharedPreferences.getString("fnameSharedPref","");
            lnameSharedPrefAfterReg = sharedPreferences.getString("lnameSharedPref","");
            tokenReg = sharedPreferences.getString("AuthSharedPref","");

        }
        catch (Exception e){

            e.printStackTrace();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        locationArrayList =new ArrayList<>();
        mOrigin1 = new LatLng(Double.parseDouble(getIntent().getStringExtra("pick_lat")),Double.parseDouble(getIntent().getStringExtra("pick_long")));
        mDestination1 = new LatLng(Double.parseDouble(getIntent().getStringExtra("drop_lat")),Double.parseDouble(getIntent().getStringExtra("drop_long")));
        locationArrayList.add(mOrigin1);
        locationArrayList.add(mDestination1);


        getServiceCallDetailAPI();



    }

    private void getServiceCallDetailAPI() {

       try {

            AppProgressBar.openLoader(this, getResources().getString(R.string.pleasewait));



            HashMap<String, Object> logReq = new HashMap<>();

           if (defaultIdLog.equals("")) {
               logReq.put("service_id",getIntent().getStringExtra("service_id"));
               logReq.put("driver_id",defaultId);

                response1 = ApiClient.getClient(this).create(ApiInterface.class).doingservicecall_detailsinformation(tokenReg,logReq);

           }
           else{
               logReq.put("service_id",getIntent().getStringExtra("service_id"));
               logReq.put("driver_id",defaultIdLog);

                response1 = ApiClient.getClient(this).create(ApiInterface.class).doingservicecall_detailsinformation(token,logReq);


           }

           response1.enqueue(new Callback<ServiceCallStatuswiseetailsResponse>() {
                @Override
                public void onResponse(Call<ServiceCallStatuswiseetailsResponse> call, Response<ServiceCallStatuswiseetailsResponse> response) {

                    if (response.body().getStatus() == true) {

                        AppProgressBar.closeLoader();
                        Toast.makeText(ServiceDetailsScreen.this,response.body().getMessage(), Toast.LENGTH_LONG).show();


                        String date = response.body().getDetail().getDate();
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
                        Date newDate = null;
                        try {
                            newDate = spf.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        spf = new SimpleDateFormat("E, MMMM dd");
                        date = spf.format(newDate);
                        System.out.println(date);
                        tv_time.setText(date +", "+ response.body().getDetail().getTime());

                      //  tv_time.setText(response.body().getDetail().getDate() +""+ response.body().getDetail().getTime());
                        tv_servicenumber.setText("Service Call no. : "+response.body().getDetail().getServiceId() );
                      //  tv_payType.setText(response.body().getDetail().());
                      //  tv_price.setText(response.body().getDetail().getTotalFare());
                        tv_price.setText(new DecimalFormat("$0.00").format(Double.valueOf(response.body().getDetail().getUltimate_driver_charge())));
                        tv_distance.setText("Distance : "+response.body().getDetail().getTotalDistance()+"Km");
                        tv_source.setText(response.body().getDetail().getPickPlace());
                        tv_dest.setText(response.body().getDetail().getDropPlace());
                        tv_driverid.setText("Customer ID: "+response.body().getDetail().getCustomerId());
                        tv_username.setText(response.body().getDetail().getCustomerName());

                        if (getIntent().hasExtra("customer_images")) {
                            Glide.with(ServiceDetailsScreen.this).load(NetworkUtility.imgbaseUrl /*+ "/media/" */+ getIntent().getStringExtra("customer_images")).diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true).into(iv_userImg);
                        }

                        if (response.body().getDetail().getDropPlace().equals("Cargo Van")){
                            tv_carImg.setImageResource(R.drawable. van_img);

                        }else {

                            tv_carImg.setImageResource(R.drawable.pickupcargo);
                        }

                        tv_rating.setText(response.body().getDetail().getUltimate_customer_rating());

                        if(response.body().getDetail().getCustomerName().contains(" ")){
                            firstWord= response.body().getDetail().getCustomerName().substring(0, response.body().getDetail().getCustomerName().indexOf(" "));
                            System.out.println(firstWord);
                            textView85.setText("You rated "+firstWord);

                        }

                        ratingbar.setRating(Float.parseFloat(response.body().getDetail().getCustomer_rating()));

                    }
                    else {

                        AppProgressBar.closeLoader();

                        if (response.body() != null) {

                            if (response.body().getMessage().equalsIgnoreCase(
                                    getResources().getString(R.string.youhavebeenloggedinfromanotherdevice)
                            )
                                ) {

                                SharedPreferences sharedPreferences =
                                        getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                sharedPreferenceManager.logoutUser();
                                editor.clear();
                                editor.commit();


                                Intent myIntent = new
                                        Intent(
                                                ServiceDetailsScreen.this,
                                                WelcomeActivity.class
                                        );
                                startActivity(myIntent);

                                finish();

                            } else {


                                Toast.makeText(
                                        ServiceDetailsScreen.this,
                                        response.body().getMessage().toString(),
                                        Toast.LENGTH_LONG
                                    ).show();


                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<ServiceCallStatuswiseetailsResponse> call, Throwable t) {
                    AppProgressBar.closeLoader();

                    if(t instanceof NoConnectivityException) {
                        // show No Connectivity message to user or do whatever you want.
                        Toast.makeText(ServiceDetailsScreen.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(ServiceDetailsScreen.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }catch (Exception e) {

           e.printStackTrace();
       }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);

        for (int i = 0; i < locationArrayList.size(); i++) {


           // mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));

            if (mDestination1!=null) {

                mMap.addMarker(new MarkerOptions().position(/*locationArrayList.get(i)*/ mDestination1).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapsorngpin)));

            }
            if (mOrigin1!=null) {

                mMap.addMarker(new MarkerOptions().position(/*locationArrayList.get(i)*/ mOrigin1).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.marketset)));

            }

            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(locationArrayList.get(i))
                    .zoom(10).build();
            //Zoom in and animate the camera.
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

        drawRoute();


    }

    private void drawRoute(){

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin1, mDestination1);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=" + getString(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = MAP_DIRECTIONS_BASE_URL+output+"?"+parameters;

        return url;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject = new JSONObject(data);

                    Log.d("DownloadTaskjsonObject","jsonObject : " + jsonObject);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    Log.d("DownloadTaskarray","array : " + array);
                    JSONObject routes = array.getJSONObject(0);

                    JSONArray legs = routes.getJSONArray("legs");

                    JSONObject steps = legs.getJSONObject(0);

                    JSONObject distance = steps.getJSONObject("distance");

                    Log.i("Distance", distance.toString());
                    dist = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]","") );
                    Log.i("dist", String.valueOf(dist));


                }catch (Exception e){

                    e.printStackTrace();
                }
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    //  String distance = (point.get("distance"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }else {
                Toast.makeText(getApplicationContext(), "No route is found", Toast.LENGTH_LONG).show();
            }
        }
    }


}