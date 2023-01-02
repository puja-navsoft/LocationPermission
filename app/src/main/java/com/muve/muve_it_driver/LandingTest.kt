package com.muve.muve_it_driver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it.CancelResponse
import com.muve.muve_it_driver.UserDetailPojo.FavouritePlacesItem
import com.muve.muve_it_driver.UserDetailPojo.UserDetailPojo
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.home.landingpage.LandingViewModel
import com.muve.muve_it_driver.model.chat.ChatListResponse
import com.muve.muve_it_driver.model.chat.IndividualChatResponse
import com.muve.muve_it_driver.model.resendotp.ResendOtpPojo
import com.muve.muve_it_driver.model.servicecallAcceptByDriver.ServiceCallAcceptByDriverResponse
import com.muve.muve_it_driver.model.servicecallaccept.ServicecallResponse
import com.muve.muve_it_driver.model.servicecallcomplete.ServiceCallCompleteResponse
import com.muve.muve_it_driver.model.servicecallstatuswisedetails.ServiceCallStatuswiseetailsResponse
import com.muve.muve_it_driver.model.verifyotp.VerifyOtpPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.util.RecyclerViewItemClickListenerFavPlace
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.muve.muve_it_driver.util.NetworkUtility
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PrivateChannel
import com.pusher.client.util.HttpAuthorizer
import retrofit2.Call


class LandingTest : Fragment() , OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener, RecyclerViewItemClickListenerFavPlace, View.OnClickListener {


    private var service_id: String = ""
    lateinit var bottomSheetBehaviorChat: BottomSheetBehavior<RelativeLayout>
    private var customerId: String = ""
    private var driverId: String = ""
    private var pickPlace: String = ""
    private var dropPlace: String = ""
    private var vehicleType: String = ""
    var service_type: String = ""
    private var status: String = ""
    var channelname: String = "private-my-channel"
    var encodedImage: String = ""
    var ImageUrl: Uri? = null
    private lateinit var chatAdapter:ChatAdapter
    var REQUEST_IMAGE = 100
    var REQUEST_CHECK_SETTING = 101
    //    lateinit var r: Runnable
    lateinit var handler: Handler
    lateinit var channel: PrivateChannel
    var fnameSharedPrefAfterReg: String = ""
    var lnameSharedPrefAfterReg: String = ""
    var rcl_fvrtPlc: RecyclerView? = null
    var defaultIdLog: Int = 0
    var isAccepted: Boolean? = false
    var isNearBy: Boolean? = false
    var isFromHomeScreen: Boolean? = false
    var isStartDriving: Boolean? = false
    var isArrivingAtDestination: Boolean? = false
    var response1: Call<UserDetailPojo>? = null
    var favoritePlaces: List<FavouritePlacesItem?>? = null
    var sharedPreferences: SharedPreferences? = null
    var myEdit: SharedPreferences.Editor? = null
    var city: String = ""
    private lateinit var viewModel: LandingViewModel
    var mapView: MapView? = null
    var googleMap: GoogleMap? = null
    var currentLocation: Location? = null
    private val REQUEST_CODE: Int = 101
    private var mLastKnownLocation: Location? = null
    private var locationCallback: LocationCallback? = null
    private var mLatLngMain: LatLng? = null
    private var edt_destination: TextView? = null
    var lat = 0.0
    var longitude = 0.0
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var defaultIdReg: Int = 0
    var fnameSharedPrefAfterLog: String = ""
    var lnameSharedPrefAfterLog: String = ""
    var token: String = ""
    var tokenReg: String = ""
    var fnameL: String = ""
    var lnameL: String = ""
    var availability: String = ""
    var storeservice_id: String = ""
    var status_codeFromPush: Int = 0
    var iv_toggle: ImageView? = null
    var iv_menusymbol: ImageView? = null
    var navClickAgain: ImageView? = null
    var iv_downarrow: ImageView? = null
    var iv_uparrowhideview: ImageView? = null
    var iv_menusymbolmain: ImageView? = null
    var iv_timerview: ImageView? = null
    var iv_navigateArrived: ImageView? = null
    var iv_arrivedtoDest_navigate: ImageView? = null
    var iv_timerviewtooltip: ImageView? = null
    var view_takepicture: ImageView? = null
    var iv_back: ImageView? = null
    var iv_cancelServicecall: ImageView? = null
    var iv_cancelservicecall: ImageView? = null
    var iv_cancelServicecallblack: ImageView? = null
    var cross_white: ImageView? = null
    var cross_black: ImageView? = null
    var iv_navigatebutton: ImageView? = null
    var iv_navigatebuttonAfterAccepet: ImageView? = null
    var bt_arraivalAtPickup: Button? = null
    var btn_verify: Button? = null
    var btn_startDriving: Button? = null
    var btn_arrivedToDest: Button? = null
    var btn_uploadProve: Button? = null
    var btn_continueunProgress: Button? = null
    var btn_uploadSuccessfully: Button? = null
    var btn_arrivedToDestReach: Button? = null
    var btn_submitRatingFromDriversite: Button? = null
    var go_back: TextView? = null
    var cancel_call: TextView? = null
    var navClick: ImageView? = null
    var distanceServiceCall: TextView? = null
    var headerPrimaryCall: TextView? = null
    var tv_timer: TextView? = null
    // var tv_timer: Chronometer? = null
    var tv_resumeCallService: TextView? = null
    var txt_additional_comments: TextView? = null
    var tv_timertext: TextView? = null
    var transview: View? = null
    var timerview: View? = null
    var view_pouseservice: View? = null
    var view_pouseserviceMain: View? = null
    var distanceServiceCallKm: TextView? = null
    var tv_navigateArrived: TextView? = null
    var const_servicecallaccept: ConstraintLayout? = null
    var v_servicaCallPause: ConstraintLayout? = null
    var storeImage: String = ""
    var afterClickOncancelserviceView: ConstraintLayout? = null
    var afterClickOncancelserviceViewReason: ConstraintLayout? = null
    var constraintLayoutUnloadingInprogress: ConstraintLayout? = null
    var constraintLayoutloadingInprogress: ConstraintLayout? = null
    var pleasewaitsecondarycall: ConstraintLayout? = null
    var mainBottomsheet: ConstraintLayout? = null
    var constraintLayoutForArraived: ConstraintLayout? = null
    var constraintLayoutProofofDropoff: ConstraintLayout? = null
    var driverRating: ConstraintLayout? = null
    var view_customercontact: ConstraintLayout? = null
    var constraintLayoutUploadedSuccessfully: ConstraintLayout? = null
    var view_cancelservicecall: ConstraintLayout? = null
    //  var includebottomsheet: RelativeLayout? = null
    var relativeChatBottomsheet: RelativeLayout? = null
    var verifyviewInclude: RelativeLayout? = null
    // var cancelservicecall: RelativeLayout? = null
    var constraintLayout: ConstraintLayout? = null
    var constraintLayoutForDropOff: ConstraintLayout? = null
    var acceptservicecalltopview: ConstraintLayout? = null
    var showhideview: ConstraintLayout? = null
    var bottomshowhidepart: ConstraintLayout? = null
    var customercontact: ConstraintLayout? = null
    var view_contactseconddaryassist: ConstraintLayout? = null
    var secondPartContact: ConstraintLayout? = null
    var headerpart: ConstraintLayout? = null
    var pauseservice: ConstraintLayout? = null
    var ratingService: TextView? = null
    var tv_totalprice: TextView? = null
    var tv_serviceid: TextView? = null
    var tv_source: TextView? = null
    var tv_username: TextView? = null
    var go_back_: TextView? = null
    var cancel_call_: TextView? = null
    var tv_customerlocationForCancelService: TextView? = null
    var tv_customerlocation_arrivedlocation: TextView? = null
    var tv_vehicleType: TextView? = null
    var tv_servicename: TextView? = null
    var tv_verifypickupcode: TextView? = null
    var tv_cartype: TextView? = null
    var tv_servicerequestid_: TextView? = null
    var tv_customerlocation: TextView? = null
    var tv_timerUnloadingInprogress: TextView? = null
    var tv_enterCustomerCode: TextView? = null
    var tv_accept: TextView? = null
    var tv_decln: TextView? = null
    var tv_dest: TextView? = null
    var tv_pouseservice: TextView? = null
    var btn_continueunProgressloading: Button? = null
    var tv_timerloadingInprogress: TextView? = null
    var tv_dropofflocation_: TextView? = null
    var tv_dropofflocation: TextView? = null
    var tv_pauseTimerCount: TextView? = null
    var includeheaderpart: View? = null
    var includebottomsheet_arraivepickup_location: View? = null
    var includebottomsheet_cancelservicecall: View? = null
    var editTextMsgSend: EditText? = null
    var ivMsgSend: ImageView? = null
    var mNavigationDrawerFragment: HomeActivity? = null
    var mDrawerLayout: DrawerLayout? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var PERMISSION_ID = 44
    var c_lat: Double? = 0.0
    var ratingbar: RatingBar? = null
    var p_lat: Double? = 0.0
    var c_long: Double? = 0.0
    var p_long: Double? = 0.0
    var pick_latFromList: Double? = 0.0
    var pick_longFromList: Double? = 0.0
    var dest_latFromList: Double? = 0.0
    var dest_longFromList: Double? = 0.0
    var onoffcall: Boolean? = false
    var driver_status: String? = ""
    var driver_updatestatus: String? = "Accepted"
    var response2: Call<ServicecallResponse>? = null
    var responsedecline: Call<ResendOtpPojo>? = null
    var responseServicecallPause: Call<VerifyOtpPojo>? = null
    var responseDriverStatusSend: Call<Driverstatusstoreresponse>? = null
    var responseCahtList: Call<ChatListResponse>? = null
    var responseServicecallcancel: Call<CancelResponse>? = null
    var responseImageUpload: Call<ImageUploadResponse>? = null
    var responseVerifyCode: Call<VerifyOtpPojo>? = null
    var responsestartdriving: Call<VerifyOtpPojo>? = null
    var responseArraivedToDest: Call<VerifyOtpPojo>? = null
    var responseGiveRatingFromDriverSiteToCustomer: Call<VerifyOtpPojo>? = null
    var responsedoingservicecallcomplete: Call<ServiceCallCompleteResponse>? = null
    var response3: Call<ServiceCallAcceptByDriverResponse>? = null
    var response4: Call<ServiceCallStatuswiseetailsResponse>? = null
    var responseArrive: Call<VerifyOtpPojo>? = null
    var visible_resentBtn: Boolean = false
    var pd_loader: ProgressBar? = null
    var chatRecycle: RecyclerView? = null
    var locationManager: LocationManager? = null
    var lattitudeC: String? = null
    var longitudeC:String? = null
    val REQUEST_LOCATION = 1

    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var mCurrLocationMarkerForCustomer: Marker? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null

    var mHandler: Handler? = null
    var mHandlerNew: Handler? = null
    var mHandlerinProgress: Handler? = null
    var mHandlerinProgressNew: Handler? = null
    var mHandlerunloading: Handler? = null
    var mHandlerunloadingNew: Handler? = null
    var mHandlerForPause: Handler? = null
    var mHandlerForPauseNew: Handler? = null
    var timeInSeconds = 0L
    var timeInSecondsNew = 0L
    var timeInSeconds_inLoading = 0L
    var timeInSeconds_inLoadingNew = 0L
    var timeInSeconds_unloadingin = 0L
    var timeInSeconds_unloadinginNew = 0L
    var timeInSeconds_pause = 0L
    var timeInSeconds_pauseNew = 0L
    var startButtonClicked = false
    var time_difference: Long = 0L
    var edt_password1 : EditText? = null
    var edt_password2 : EditText? = null
    var edt_password3 : EditText? = null
    var edt_password4 : EditText? = null
    var receiver : BroadcastReceiver? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null

    var mMarkerOptions: MarkerOptions? = null
    var chatListArr: List<IndividualChatResponse>? = null
    // lateinit var chatAdapter: ChatAdapter
    var pusher : Pusher? =null

    var authorizer = HttpAuthorizer(NetworkUtility.authUrl)

    var options = PusherOptions().setAuthorizer(authorizer)
    private var provider: String? = null

    companion object{

        var mMap: GoogleMap? = null
        var mPolyline: Polyline? = null
        var dist = 0.0
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.landing_fragment, container, false)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        getLocation()



        // Inflate the layout for this fragment
        return view
    }

   /* override fun onMapReady(p0: GoogleMap?) {


    }*/

    override fun onClick(p0: View?) {


    }

    override fun onLocationChanged(p0: Location) {


    }

    override fun onConnected(p0: Bundle?) {


    }

    override fun onConnectionSuspended(p0: Int) {


    }

    override fun onConnectionFailed(p0: ConnectionResult) {


    }

    override fun onItemClick(position: Int, countryList: List<FavouritePlacesItem>) {


    }


    fun getLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        } else {

            locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager


            val location: Location =
                locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
            /*  val location1: Location =
                  locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!*/
            val location2: Location =
                locationManager!!.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)!!


            if (location != null) {
                val latti = location.latitude
                val longi = location.longitude
                lattitudeC = latti.toString()
                longitudeC = longi.toString()

                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latti , longi), 16F))


            } /*else if (location1 != null) {
                 val latti = location1.latitude
                 val longi = location1.longitude
                 lattitudeC = latti.toString()
                 longitudeC = longi.toString()

                 mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latti , longi), 16F))


             } */else if (location2 != null) {
                val latti = location2.latitude
                val longi = location2.longitude
                lattitudeC = latti.toString()
                longitudeC = longi.toString()

                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latti , longi), 16F))


            } else {
                Toast.makeText(requireActivity(), "Unble to Trace your location", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onMapReady(p0: GoogleMap) {

    }


}