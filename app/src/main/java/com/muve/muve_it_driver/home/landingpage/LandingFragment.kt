package com.muve.muve_it_driver.home.landingpage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.Context.SENSOR_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.*
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.muve.muve_it.CancelResponse
import com.muve.muve_it.CompleteSecondaryService
import com.muve.muve_it.Emargencycancel
import com.muve.muve_it_driver.*
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.UserDetailPojo.FavouritePlacesItem
import com.muve.muve_it_driver.UserDetailPojo.UserDetailPojo
import com.muve.muve_it_driver.cancelservicecallscreen.CancelServiceCallScreen
import com.muve.muve_it_driver.cancelservicetoreturnhome.AfterCancelServiceScreen
import com.muve.muve_it_driver.completeservicecallsecondary.AfterCompleteSecondaryServiceScreen
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.model.aboutusmodel.AboutUsPojo
import com.muve.muve_it_driver.model.chat.ChatListResponse
import com.muve.muve_it_driver.model.chat.IndividualChatResponse
import com.muve.muve_it_driver.model.resendotp.ResendOtpPojo
import com.muve.muve_it_driver.model.servicecallAcceptByDriver.ServiceCallAcceptByDriverResponse
import com.muve.muve_it_driver.model.servicecallaccept.ServicecallResponse
import com.muve.muve_it_driver.model.servicecallcomplete.ServiceCallCompleteResponse
import com.muve.muve_it_driver.model.servicecallstatuswisedetails.ServiceCallStatuswiseetailsResponse
import com.muve.muve_it_driver.model.verifyotp.VerifyOtpPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiClientForPusher
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.service.LocationService
import com.muve.muve_it_driver.serviceCallDetailsScreen.DirectionsJSONParser
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.*
import com.ncorti.slidetoact.SlideToActView
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PrivateChannel
import com.pusher.client.channel.PrivateChannelEventListener
import com.pusher.client.channel.PusherEvent
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.net.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class LandingFragment : Fragment(), OnMapReadyCallback,
    LocationListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, RecyclerViewItemClickListenerFavPlace,
    SensorEventListener/*, View.OnClickListener*//*, SlideToActView.OnSlideCompleteListener ,SlideToActView.OnSlideToActAnimationEventListener*/ {

    var service_id: String = ""
    var service_idstore: String = ""
    lateinit var bottomSheetBehaviorChat: BottomSheetBehavior<RelativeLayout>
    var customerId: String = ""
    var driverId: String = ""
    var second_driver_id: String = ""
    var pickPlace: String = ""
    var dropPlace: String = ""
    var vehicleType: String = ""
    var loadunloadafterAccptInServiceCall: Boolean? = null
    var vehicleImage: String = ""
    var customer_name: String = ""
    var customer_image: String = ""
    var unattendDrop: Boolean? = null
    var loadUnload: Boolean? = null
    var is_reassign: Boolean? = false
    var reassign_driver: String = ""
    var service_type: String = ""
    private var status: String = ""
    var encodedImage: String = ""
    var driverstatus: String = ""
    var ImageUrl: Uri? = null
    private lateinit var chatAdapter: ChatAdapter


    var REQUEST_IMAGE = 0
    var REQUEST_CHECK_SETTING = 101


    lateinit var handler: Handler
    lateinit var channel: PrivateChannel
    var fnameSharedPrefAfterReg: String = ""
    var lnameSharedPrefAfterReg: String = ""
    var defaultIdLog: String = ""
    var isAccepted: Boolean? = false
    var isNearBy: Boolean? = false
    var isStartDriving: Boolean? = false
    var isArrivingAtDestination: Boolean? = false
    var response1: Call<UserDetailPojo>? = null
    var sharedPreferences: SharedPreferences? = null
    var myEdit: SharedPreferences.Editor? = null
    var city: String = ""
    var tv_reassignmentLocation: TextView? = null
    private lateinit var viewModel: LandingViewModel
    var mapView: MapView? = null
    var currentLocation: Location? = null
    private var edt_destination: TextView? = null
    var lat = 0.0
    var longitude = 0.0
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var defaultIdReg: String = ""
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
    var dummylayout: ConstraintLayout? = null
    var iv_navigateArrived: ImageView? = null
    var iv_arrivedtoDest_navigate: ImageView? = null
    var iv_timerviewtooltip: ImageView? = null
    var view_takepicture: ImageView? = null
    var iv_back: ImageView? = null
    var iv_cancelServicecall: ImageView? = null
    var iv_cancelservicecall: ImageView? = null
    var imageView36: ImageView? = null
    var textView54: TextView? = null
    var iv_cancelServicecallblack: ImageView? = null
    var tv_emergencycancel1: TextView? = null
    var tv_yes_emergency: TextView? = null
    var tv_no_emergency: TextView? = null
    var cross_white: ImageView? = null
    var cross_black: ImageView? = null
    var iv_navigatebutton: ImageView? = null
    var iv_navigatebuttonAfterAccepet: ImageView? = null
    var bt_arraivalAtPickup: Button? = null
    var bt_arraivalAtPickupF: Button? = null
    var btn_verify: Button? = null

    //var btn_startDriving: Button? = null
    var btn_startDriving: SlideToActView? = null

    //    var btn_arrivedToDest: Button? = null
    var btn_arrivedToDest: SlideToActView? = null
    var btn_uploadProve: Button? = null
    var btn_continueunProgress: Button? = null
    var btn_uploadSuccessfully: Button? = null

    // var btn_arrivedToDestReach: Button? = null
    var btn_submitRatingFromDriversite: Button? = null
    var go_back: TextView? = null
    var back: TextView? = null
    var toCall: TextView? = null
    var tv_customercontact: TextView? = null
    var cancel_call: TextView? = null
    var textView53: TextView? = null
    var textView46: TextView? = null
    var navClick: ImageView? = null
    var distanceServiceCall: TextView? = null
    var headerPrimaryCall: TextView? = null
    var tv_timer: TextView? = null

    // var tv_timer: Chronometer? = null
    var tv_resumeCallService: TextView? = null
    var txt_additional_comments: TextView? = null
    var tv_timertext: TextView? = null
    var transview: View? = null

    //  var timerview: View? = null
    var view_pouseservice: ConstraintLayout? = null

    //   var view_pouseserviceMain: ConstraintLayout? = null
    var distanceServiceCallKm: TextView? = null
    var tv_navigateArrived: TextView? = null
    var const_servicecallaccept: ConstraintLayout? = null
    var v_servicaCallPause: ConstraintLayout? = null
    var reassign: ConstraintLayout? = null
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
    var img_profile: ImageView? = null
    var view_customercontact: ConstraintLayout? = null
    var constraintLayoutUploadedSuccessfully: ConstraintLayout? = null
    var view_cancelservicecall: ConstraintLayout? = null

    //  var includebottomsheet: RelativeLayout? = null
    var relativeChatBottomsheet: RelativeLayout? = null
    var imageView22: ImageView? = null
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
    var name: TextView? = null
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

    // var tv_accept: TextView? = null
    var tv_accept: SlideToActView? = null
    var btn_startDrivingreassignment: SlideToActView? = null
    var tv_decln: TextView? = null
    var tv_dest: TextView? = null
    var tv_pouseservice: TextView? = null
    var btn_continueunProgressloading: Button? = null
    var tv_timerloadingInprogress: TextView? = null
    var tv_dropofflocation_: TextView? = null
    var tv_dropofflocation: TextView? = null
    var tv_pauseTimerCount: TextView? = null
    var tv_emergencycancel: TextView? = null
    var includeheaderpart: View? = null
    var includedummylayout: View? = null
    var includebottomsheet_arraivepickup_location: View? = null
    var includebottomsheet_cancelservicecall: View? = null
    var editTextMsgSend: EditText? = null
    var ivMsgSend: ImageView? = null
    var loadunloadiv: ImageView? = null
    var unatndiv: ImageView? = null
    var mNavigationDrawerFragment: HomeActivity? = null
    var mDrawerLayout: DrawerLayout? = null

    var PERMISSION_ID = 44
    var c_lat: Double? = 0.0
    var booleanservice_type: Boolean? = null
    var ratingbar: RatingBar? = null
    var p_lat: Double? = 0.0
    var c_long: Double? = 0.0
    var p_long: Double? = 0.0
    var pick_latFromList: Double? = 0.0
    var pick_longFromList: Double? = 0.0
    var dest_latFromList: Double? = 0.0
    var dest_longFromList: Double? = 0.0
    var previous_driver_lat: Double? = 0.0
    var previous_driver_long: Double? = 0.0
    var onoffcall: Boolean? = false
    var driver_status: String? = ""
    var afteremargencynewservice_id: String? = ""
    var driver_updatestatus: String? = "Accepted"
    var response2: Call<ServicecallResponse>? = null
    var responsedecline: Call<ResendOtpPojo>? = null
    var responseSecondaryService: Call<CompleteSecondaryService>? = null
    var responseServicecallPause: Call<VerifyOtpPojo>? = null
    var responseDriverStatusSend: Call<Driverstatusstoreresponse>? = null
    var responseCahtList: Call<ChatListResponse>? = null
    var responseServicecallcancel: Call<CancelResponse>? = null
    var responseImageUpload: Call<ImageUploadResponse>? = null
    var responseVerifyCode: Call<VerifyOtpPojo>? = null
    var responsestartdriving: Call<VerifyOtpPojo>? = null
    var responseArraivedToDest: Call<VerifyOtpPojo>? = null
    var responseEmaregencycancel: Call<Emargencycancel>? = null
    var responseGiveRatingFromDriverSiteToCustomer: Call<VerifyOtpPojo>? = null
    var responsedoingservicecallcomplete: Call<ServiceCallCompleteResponse>? = null
    var response3: Call<ServiceCallAcceptByDriverResponse>? = null
    var response4: Call<ServiceCallStatuswiseetailsResponse>? = null
    var responseArrive: Call<VerifyOtpPojo>? = null
    var visible_resentBtn: Boolean = false
    var pd_loader: ProgressBar? = null
    var pd_loader1: ProgressBar? = null
    var chatRecycle: RecyclerView? = null
    var locationManager: LocationManager? = null
    var lattitudeC: String? = null
    var longitudeC: String? = null
    var price: String? = null
    var driver_idCancel: String? = null
    var service_idCancel: String? = null
    val REQUEST_LOCATION = 1
    val requestCall = 1
    var jsonObjectDriver: JSONObject? = null
    var jsonObjectDriver1: JSONObject? = null
    var jsonObjectDriverEmergencyCancel: JSONObject? = null
    var service_idbind: String? = null
    var driver_updatestatusbind: String? = null


    var mCurrLocationMarkerForCustomer: Marker? = null
    var mGoogleApiClient: GoogleApiClient? = null

    // var mLocationRequest: LocationRequest? = null
    lateinit var mLocationRequest: LocationRequest
    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
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
    var edt_password1: EditText? = null
    var edt_password2: EditText? = null
    var edt_password3: EditText? = null
    var edt_password4: EditText? = null
    var vehicle_type: TextView? = null
    var tv_transferCall: TextView? = null
    var searchingForSecondaryDriverLayout: ConstraintLayout? = null
    var callcustomercare: ConstraintLayout? = null
    var vehicle_image: ImageView? = null
    var iv_userimg: ImageView? = null
    var iv_loadunloadtooltip: ImageView? = null
    var imagev_servicaCallPause: ImageView? = null
    var iv_unatten: ImageView? = null
    var iv_unattentooltip: ImageView? = null
    var iv_navigatebuttonForReassignment: ImageView? = null
   // var tv_loadunloadtooltip: TextView? = null
    var tv_loadunloadtooltip: ConstraintLayout? = null
  //  var tv_unattentooltip: TextView? = null
    var tv_unattentooltip: ConstraintLayout? = null
    var receiver: BroadcastReceiver? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null
    var pleasewaitsecondaysearching: ProgressBar? = null
    var emergencyview: ConstraintLayout? = null
    var distance=10.0
    var popupnodriver: ConstraintLayout? = null
    var popupok: TextView? = null
    var mMarkerOptions: MarkerOptions? = null
    var chatListArr: List<IndividualChatResponse>? = null
    var mLocationService: LocationService = LocationService()
    lateinit var mServiceIntent: Intent
    var pusher: Pusher? = null
    var customer_waiting_time: String? = ""
    var authorizer = HttpAuthorizer(NetworkUtility.authUrl)
    var options = PusherOptions().setAuthorizer(authorizer)
    var jsonObjectAfterReassignDriver: JSONObject? = null
    var screen_width = 0f

    var chatClickOn=""

    var primaryDriverName=""
    var secondaryDriverName=""

    companion object {

        var mMap: GoogleMap? = null
        var mPolyline: Polyline? = null
        var dist = 0.0
        val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view: View = inflater.inflate(R.layout.landing_fragment, container, false)

        tv_pauseTimerCount = view.findViewById<TextView>(R.id.tv_pauseTimerCount)
        popupnodriver = view.findViewById<ConstraintLayout>(R.id.popupnoservicecall) as ConstraintLayout
        reassign = view.findViewById<ConstraintLayout>(R.id.reassign) as ConstraintLayout
        popupok = view.findViewById<TextView>(R.id.popupok) as TextView
        tv_reassignmentLocation = view.findViewById<TextView>(R.id.tv_reassignmentLocation) as TextView

        tv_emergencycancel = view.findViewById<TextView>(R.id.tv_emergencycancel)
        includeheaderpart = view.findViewById<ConstraintLayout>(R.id.includeheaderpart)
        callcustomercare = view.findViewById<ConstraintLayout>(R.id.callcustomercare)
        //  includedummylayout = view.findViewById<ConstraintLayout>(R.id.includedummylayout)
        includebottomsheet_arraivepickup_location =
            view.findViewById<FrameLayout>(R.id.includebottomsheet_arraivepickup_location)
        includebottomsheet_cancelservicecall =
            view.findViewById<FrameLayout>(R.id.includebottomsheet_cancelservicecall)
        headerpart = view.findViewById<ConstraintLayout>(R.id.headerpart)
        dummylayout = view.findViewById<ConstraintLayout>(R.id.dummylayout)
        iv_downarrow =
            (includeheaderpart as ConstraintLayout?)?.findViewById<ImageView>(R.id.iv_downarrow)
        // dummylayout = (includedummylayout as ConstraintLayout?)?.findViewById<ConstraintLayout>(R.id.dummylayout)
        iv_cancelServicecall =
            (includebottomsheet_cancelservicecall as FrameLayout?)?.findViewById<ImageView>(R.id.iv_cancelServicecall)
        cross_black =
            (includebottomsheet_cancelservicecall as FrameLayout?)?.findViewById<ImageView>(R.id.cross_black)
        iv_cancelServicecallblack =
            (includebottomsheet_cancelservicecall as FrameLayout?)?.findViewById<ImageView>(R.id.iv_cancelServicecallblack)
        //   tv_emergencycancel1 = (includebottomsheet_cancelservicecall as FrameLayout?)?.findViewById<TextView>(R.id.tv_emergencycancel1)
        tv_yes_emergency = view.findViewById<TextView>(R.id.tv_yes_emergency)
        tv_no_emergency = view.findViewById<TextView>(R.id.tv_no_emergency)
        iv_cancelservicecall = view.findViewById<ImageView>(R.id.iv_cancelservicecall)
        cross_white =
            (includebottomsheet_cancelservicecall as FrameLayout?)?.findViewById<ImageView>(R.id.cross_white)
        textView53 =
            (includebottomsheet_cancelservicecall as FrameLayout?)?.findViewById<TextView>(R.id.textView53)
        tv_vehicleType = view.findViewById<TextView>(R.id.tv_vehicleType)
        tv_loadunloadtooltip = view.findViewById<ConstraintLayout>(R.id.tv_loadunloadtooltip)
        tv_servicename = view.findViewById<TextView>(R.id.tv_servicename)
        tv_verifypickupcode = view.findViewById<TextView>(R.id.tv_verifypickupcode)
        editTextMsgSend = view.findViewById<EditText>(R.id.editTextMsgSend)
        ivMsgSend = view.findViewById<ImageView>(R.id.ivMsgSend)
        loadunloadiv = view.findViewById<ImageView>(R.id.loadunloadiv)
        unatndiv = view.findViewById<ImageView>(R.id.unatndiv)
        pleasewaitsecondaysearching =
            view.findViewById<ProgressBar>(R.id.pleasewaitsecondaysearching)
        searchingForSecondaryDriverLayout =
            view.findViewById<ConstraintLayout>(R.id.searchingForSecondaryDriverLayout)
        tv_dropofflocation_ = view.findViewById<TextView>(R.id.tv_dropofflocation_)
        tv_customerlocation_arrivedlocation =
            view.findViewById<TextView>(R.id.tv_customerlocation_arrivedlocation)
        tv_servicerequestid_ = view.findViewById<TextView>(R.id.tv_servicerequestid_)
        tv_customerlocation = view.findViewById<TextView>(R.id.tv_customerlocation)
        go_back_ = view.findViewById<TextView>(R.id.go_back_)
        cancel_call_ = view.findViewById<TextView>(R.id.cancel_call_)
        vehicle_type = view.findViewById<TextView>(R.id.vehicle_type)
        vehicle_image = view.findViewById<ImageView>(R.id.vehicle_image)
        tv_dropofflocation = view.findViewById<TextView>(R.id.tv_dropofflocation)
        tv_totalprice = view.findViewById<TextView>(R.id.tv_totalprice)
        tv_serviceid = view.findViewById<TextView>(R.id.tv_serviceid)
        tv_transferCall = view.findViewById<TextView>(R.id.tv_transferCall)
        tv_customerlocationForCancelService =
            view.findViewById<TextView>(R.id.tv_customerlocationForCancelService)
        iv_toggle = view.findViewById<ImageView>(R.id.iv_toggle)
        imageView22 = view.findViewById<ImageView>(R.id.imageView22)
        navClick = view.findViewById<ImageView>(R.id.navClick)
        iv_userimg = view.findViewById<ImageView>(R.id.iv_userimg)
        //  iv_cancelServicecallblack = view.findViewById<ImageView>(R.id.iv_cancelServicecallblack)
        // cross_white = view.findViewById<ImageView>(R.id.cross_white)
        iv_uparrowhideview = view.findViewById<ImageView>(R.id.iv_uparrowhideview)
        iv_menusymbol = view.findViewById<ImageView>(R.id.iv_menusymbol)
        iv_menusymbolmain = view.findViewById<ImageView>(R.id.iv_menusymbolmain)
        navClickAgain = view.findViewById<ImageView>(R.id.navClickAgain)
        //   iv_downarrow = view.findViewById<ImageView>(R.id.iv_downarrow)
        bt_arraivalAtPickup = view.findViewById<Button>(R.id.bt_arraivalAtPickup)
        bt_arraivalAtPickupF = view.findViewById<Button>(R.id.bt_arraivalAtPickupF)
        tv_emergencycancel1 = view.findViewById<Button>(R.id.tv_emergencycancel1)
        iv_navigatebutton = view.findViewById<ImageView>(R.id.iv_navigatebutton)
        iv_navigatebuttonAfterAccepet =
            view.findViewById<ImageView>(R.id.iv_navigatebuttonAfterAccepet)
        constraintLayout = view.findViewById<ConstraintLayout>(R.id.constraintLayout)
        view_cancelservicecall = view.findViewById<ConstraintLayout>(R.id.view_cancelservicecall)
        acceptservicecalltopview =
            view.findViewById<ConstraintLayout>(R.id.acceptservicecalltopview)
        showhideview = view.findViewById<ConstraintLayout>(R.id.showhideview)
        customercontact = view.findViewById<ConstraintLayout>(R.id.customercontact)
        iv_loadunloadtooltip = view.findViewById<ImageView>(R.id.iv_loadunloadtooltip)
        imagev_servicaCallPause = view.findViewById<ImageView>(R.id.imagev_servicaCallPause)
        iv_unatten = view.findViewById<ImageView>(R.id.iv_unatten)
        iv_unattentooltip = view.findViewById<ImageView>(R.id.iv_unattentooltip)
        iv_navigatebuttonForReassignment = view.findViewById<ImageView>(R.id.iv_navigatebuttonForReassignment)
        tv_unattentooltip = view.findViewById<ConstraintLayout>(R.id.tv_unattentooltip)
        pleasewaitsecondarycall = view.findViewById<ConstraintLayout>(R.id.pleasewaitsecondarycall)
        view_contactseconddaryassist =
            view.findViewById<ConstraintLayout>(R.id.view_contactseconddaryassist)
        secondPartContact = view.findViewById<ConstraintLayout>(R.id.secondPartContact)
        emergencyview = view.findViewById<ConstraintLayout>(R.id.emergencyview)
        bottomshowhidepart = view.findViewById<ConstraintLayout>(R.id.bottomshowhidepart)
        pauseservice = view.findViewById<ConstraintLayout>(R.id.pauseservice)
        distanceServiceCall = view.findViewById<TextView>(R.id.distanceServiceCall)
        headerPrimaryCall = view.findViewById<TextView>(R.id.headerPrimaryCall)
        distanceServiceCallKm = view.findViewById<TextView>(R.id.distanceServiceCallKm)
        tv_timerloadingInprogress = view.findViewById<TextView>(R.id.tv_timerloadingInprogress)
        tv_resumeCallService = view.findViewById<TextView>(R.id.tv_resumeCallService)
        txt_additional_comments = view.findViewById<TextView>(R.id.txt_additional_comments)
        ratingService = view.findViewById<TextView>(R.id.ratingService)
        tv_source = view.findViewById<TextView>(R.id.tv_source)
        tv_username = view.findViewById<TextView>(R.id.tv_username)
        name = view.findViewById<TextView>(R.id.name)
        transview = view.findViewById<View>(R.id.transview)
        tv_dest = view.findViewById<TextView>(R.id.tv_dest)
        tv_decln = view.findViewById<TextView>(R.id.tv_decln)
        // val slide = view.findViewById<SlideToActView>(R.id.tv_accept)
        //  tv_accept = view.findViewById<TextView>(R.id.tv_accept)
        tv_accept = view.findViewById<SlideToActView>(R.id.tv_accept)
        btn_startDrivingreassignment = view.findViewById<SlideToActView>(R.id.btn_startDrivingreassignment)
        tv_cartype = view.findViewById<TextView>(R.id.tv_cartype)
        ratingbar = view.findViewById<RatingBar>(R.id.ratingbar)
        const_servicecallaccept = view.findViewById<ConstraintLayout>(R.id.const_servicecallaccept)
        constraintLayoutUnloadingInprogress =
            view.findViewById<ConstraintLayout>(R.id.constraintLayoutUnloadingInprogress)
        constraintLayoutloadingInprogress =
            view.findViewById<ConstraintLayout>(R.id.constraintLayoutloadingInprogress)
        constraintLayoutForDropOff =
            view.findViewById<ConstraintLayout>(R.id.constraintLayoutForDropOff)
        //  includebottomsheet = view.findViewById<RelativeLayout>(R.id.includebottomsheet)
        relativeChatBottomsheet = view.findViewById<RelativeLayout>(R.id.relativeChatBottomsheet)
        //  cancelservicecall = view.findViewById<RelativeLayout>(R.id.cancelservicecall)
        tv_timer = view.findViewById<TextView>(R.id.tv_timer)
        // tv_timer = view.findViewById<Chronometer>(R.id.tv_timer)
        tv_pouseservice = view.findViewById<TextView>(R.id.tv_pouseservice)
        btn_continueunProgressloading =
            view.findViewById<Button>(R.id.btn_continueunProgressloading)
        tv_timerUnloadingInprogress = view.findViewById<TextView>(R.id.tv_timerUnloadingInprogress)
        tv_navigateArrived = view.findViewById<TextView>(R.id.tv_navigateArrived)
        iv_timerview = view.findViewById<ImageView>(R.id.iv_timerview)
        iv_navigateArrived = view.findViewById<ImageView>(R.id.iv_navigateArrived)
        view_takepicture = view.findViewById<ImageView>(R.id.view_takepicture)
        iv_arrivedtoDest_navigate = view.findViewById<ImageView>(R.id.iv_arrivedtoDest_navigate)
        iv_timerviewtooltip = view.findViewById<ImageView>(R.id.iv_timerviewtooltip)
        //  timerview = view.findViewById<View>(R.id.timerview)
        //  view_pouseserviceMain = view.findViewById<ConstraintLayout>(R.id.view_pouseserviceMain)
        tv_timertext = view.findViewById<TextView>(R.id.tv_timertext)
        mainBottomsheet = view.findViewById<ConstraintLayout>(R.id.mainBottomsheet)
        constraintLayoutUploadedSuccessfully =
            view.findViewById<ConstraintLayout>(R.id.constraintLayoutUploadedSuccessfully)
        constraintLayoutForArraived =
            view.findViewById<ConstraintLayout>(R.id.constraintLayoutForArraived)
        constraintLayoutProofofDropoff =
            view.findViewById<ConstraintLayout>(R.id.constraintLayoutProofofDropoff)
        v_servicaCallPause = view.findViewById<ConstraintLayout>(R.id.v_servicaCallPause)
        tv_enterCustomerCode = view.findViewById<Button>(R.id.tv_enterCustomerCode)
        btn_submitRatingFromDriversite =
            view.findViewById<Button>(R.id.btn_submitRatingFromDriversite)
        verifyviewInclude = view.findViewById<RelativeLayout>(R.id.verifyviewInclude)
        btn_verify = view.findViewById<Button>(R.id.btn_verify)
        // btn_startDriving = view.findViewById<Button>(R.id.btn_startDriving)
        btn_startDriving = view.findViewById<SlideToActView>(R.id.btn_startDriving)
        //  btn_arrivedToDest = view.findViewById<Button>(R.id.btn_arrivedToDest)
        btn_arrivedToDest = view.findViewById<SlideToActView>(R.id.btn_arrivedToDest)
        btn_uploadProve = view.findViewById<Button>(R.id.btn_uploadProve)
        go_back = view.findViewById<TextView>(R.id.go_back)
        back = view.findViewById<TextView>(R.id.back)
        toCall = view.findViewById<TextView>(R.id.toCall)
        tv_customercontact = view.findViewById<TextView>(R.id.tv_customercontact)
        cancel_call = view.findViewById<TextView>(R.id.cancel_call)
        textView46 = view.findViewById<TextView>(R.id.textView46)
        btn_continueunProgress = view.findViewById<Button>(R.id.btn_continueunProgress)
        //   btn_arrivedToDestReach = view.findViewById<Button>(R.id.btn_arrivedToDestReach)
        btn_uploadSuccessfully = view.findViewById<Button>(R.id.btn_uploadSuccessfully)
        iv_back = view.findViewById<ImageView>(R.id.iv_back)

        imageView36 = view.findViewById<ImageView>(R.id.imageView36)
        textView54 = view.findViewById<TextView>(R.id.textView54)
        //  cross_black = view.findViewById<ImageView>(R.id.cross_black)
        view_pouseservice = view.findViewById<ConstraintLayout>(R.id.view_pouseservice)
        driverRating = view.findViewById<ConstraintLayout>(R.id.driverRating)
        img_profile = view.findViewById<ImageView>(R.id.img_profile)
        view_customercontact = view.findViewById<ConstraintLayout>(R.id.view_customercontact)
        afterClickOncancelserviceView =
            view.findViewById<ConstraintLayout>(R.id.afterClickOncancelserviceView)
        afterClickOncancelserviceViewReason =
            view.findViewById<ConstraintLayout>(R.id.afterClickOncancelserviceViewReason)
        pd_loader = view.findViewById<ProgressBar>(R.id.pd_loaderBack)
        pd_loader1 = view.findViewById<ProgressBar>(R.id.pd_loader1)
        chatRecycle = view.findViewById<RecyclerView>(R.id.chatRecycle)
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        //  bottomSheetBehavior = BottomSheetBehavior.from<RelativeLayout>(includebottomsheet!!)
        bottomSheetBehaviorChat = BottomSheetBehavior.from<RelativeLayout>(relativeChatBottomsheet!!)
        //  bottomSheetBehaviorCancelService = BottomSheetBehavior.from<RelativeLayout>(cancelservicecall!!)

        edt_password1 = view.findViewById(R.id.edt_password1)
        edt_password2 = view.findViewById(R.id.edt_password2)
        edt_password3 = view.findViewById(R.id.edt_password3)
        edt_password4 = view.findViewById(R.id.edt_password4)

        val criteria = Criteria()
        val bestProvider = locationManager!!.getBestProvider(criteria, true)

        buildGoogleApiClient()
        mapView = view.findViewById<MapView>(R.id.map) as MapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)

        val mSensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager?
        val mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)


        sharedPreferenceManager = SharedPreferenceManager(requireActivity())


        if (requireActivity().intent.getBooleanExtra("loadUnload", false) == true) {

            loadUnload = true

        } else {
            loadUnload = false

        }

        if (requireActivity().intent.getBooleanExtra("unattendDrop", false) == true) {

            unattendDrop = true

        } else {

            unattendDrop = false

        }

        if (!requireActivity().intent.hasExtra("vehicleImage")!!.equals("")) {

            vehicleImage = requireActivity().intent.getStringExtra("vehicleImage").toString()!!

        }
        if (!requireActivity().intent.hasExtra("customer_name")!!.equals("")) {

            customer_name = requireActivity().intent.getStringExtra("customer_name").toString()!!

        }

        handler = Handler(Looper.myLooper()!!)
        mHandlerunloading = Handler(Looper.myLooper()!!)
        mHandlerunloadingNew = Handler(Looper.getMainLooper())
        mHandlerinProgressNew = Handler(Looper.getMainLooper())
        mHandler = Handler(Looper.getMainLooper())
        mHandlerinProgress = Handler(Looper.getMainLooper())
        mHandlerForPause = Handler(Looper.getMainLooper())
        mHandlerNew = Handler(Looper.getMainLooper())
        mHandlerForPauseNew = Handler(Looper.getMainLooper())

        options.setCluster(NetworkUtility.cluster)

        val authorizer = HttpAuthorizer(NetworkUtility.authUrl)
        val options = PusherOptions().setAuthorizer(authorizer)
        options.setCluster(NetworkUtility.cluster)

        pusher = Pusher(NetworkUtility.key, options)

        pusher!!.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.i(
                    "Pusher",
                    "State changed from ${change.previousState} to ${change.currentState}"
                )

                if (change.currentState.toString().equals("CONNECTED", ignoreCase = true)) {


                    val socketId = pusher!!.connection.socketId
                    if (socketId != null){
                        val channelname = "private-my-channel"
                        Log.v("socketId", "socketId" + socketId)
                        val parameters = HashMap<String, String>()
                        parameters["socket_id"] = socketId
                        parameters["channel_name"] = "private-my-channel"
                        authorizer.setHeaders(parameters)
                        pusher!!.unsubscribe(channelname)

                        channel = pusher!!.subscribePrivate("private-my-channel")

                    }

                }


            }

            override fun onError(message: String?, code: String?, e: java.lang.Exception?) {

                Log.i(
                    "Pusher",
                    "There was a problem connecting! code ($code), message ($message), exception($e)"
                )
            }

        }, ConnectionState.ALL)

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                try {


                    Log.v("reassign_driverpuja","reassign_driver"+reassign_driver)
                    if (intent.getIntExtra("status_codeFromPush", 0) == 412) {

                        callServiceCallAPI()
                    }

                    if ((intent.getIntExtra("status_codeFromPushMSG", 0) == 201) && (relativeChatBottomsheet!!.visibility == View.VISIBLE) ) {



                        Log.v("executeshow","")
                        callChatAPI(chatAdapter,false)
                    }

                    else if (intent.getIntExtra("status_codeFromPushServiceCancell", 0) == 350) {
                        if (activity != null && isAdded()) {
                            /* Toast.makeText(
                                requireActivity(),
                                "Service Call cancelled by customer",
                                Toast.LENGTH_SHORT
                            ).show()*/
                            mMap!!.clear()

                            isAccepted = false
                            isNearBy = false
                            isStartDriving = false
                            isArrivingAtDestination = false
                            pick_latFromList = 0.0

                            if (mPolyline != null)
                                mPolyline!!.remove()
                            mCurrLocationMarker!!.remove()

                            val myIntent = Intent(requireActivity(), HomeActivity::class.java)
                            requireActivity().startActivity(myIntent)
                            // requireActivity().finish()
                        }

                    } else if (intent.getIntExtra(
                            "status_codeFromPushMultipleDeviceLogin",
                            0
                        ) == 204
                    ) {
                        if (activity != null && isAdded()) {
                            Toast.makeText(requireActivity(), "Your Account is Logged In from another Device", Toast.LENGTH_SHORT).show()

                            getLoggedOut()
                        }

                    } else if (intent.getIntExtra(
                            "status_codeFromPushNo_Secondary_Driver_Available",
                            0
                        ) == 510
                    ) {
                        if (activity != null && isAdded()) {
                            Toast.makeText(requireActivity(), "No secondary driver available now!", Toast.LENGTH_SHORT).show()

                            val myIntent = Intent(requireActivity(), HomeActivity::class.java)
                            requireActivity().startActivity(myIntent)
                        }

                    } else if (intent.getIntExtra(
                            "status_codeFromPushPrimaryReAssignLogOut",
                            0
                        ) == 855
                    ) {

                        /* if (activity != null && isAdded()) {
                             val myIntent = Intent(requireActivity(), HomeActivity::class.java)
                             requireActivity().startActivity(myIntent)
                         }*/

                        if (activity != null && isAdded()) {

                            getLoggedOut()
                            stopServiceFunc()
                        }

                    }
                    else if (intent.getIntExtra("status_codeFromPushPrimaryReAssign", 0) == 866) {

                        if (NetworkUtility.isNetworkAvailable(requireActivity())) {
                            getUserDetails("")
                        }
                        else{

                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.msg_no_internet),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }

                    else if (intent.getIntExtra("status_codeFromPushNoDriverAvailable", 0) == 857) {

                        if (activity != null && isAdded()) {
                            Toast.makeText(requireActivity(), "No secondary driver available now!", Toast.LENGTH_SHORT).show()

                            val myIntent = Intent(requireActivity(), HomeActivity::class.java)
                            requireActivity().startActivity(myIntent)
                        }

                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        sharedPreferences =
            requireContext().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)

        try {

            defaultIdReg = sharedPreferences!!.getString("idSharedPref", "")!!
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog", "")!!
            availability = sharedPreferences!!.getString("availability", "")!!

            if (requireActivity().intent.hasExtra("pick_lat")!!) {
                pick_latFromList = requireActivity().intent.getStringExtra("pick_lat")?.toDouble()!!
                pick_longFromList =
                    requireActivity().intent.getStringExtra("pick_long")?.toDouble()!!
                dest_latFromList = requireActivity().intent.getStringExtra("drop_lat")?.toDouble()!!
                dest_longFromList =
                    requireActivity().intent.getStringExtra("drop_long")?.toDouble()!!
                service_id = requireActivity().intent.getStringExtra("service_id")!!
                customerId = requireActivity().intent.getStringExtra("customerid")!!
                driverId = requireActivity().intent.getStringExtra("driverid")!!
                if (requireActivity().intent.hasExtra("second_driver_id")) {
                    second_driver_id = requireActivity().intent.getStringExtra("second_driver_id")!!
                }
                pickPlace = requireActivity().intent.getStringExtra("pickPlace")!!
                dropPlace = requireActivity().intent.getStringExtra("dropPlace")!!
                vehicleType = requireActivity().intent.getStringExtra("vehicleType")!!
                vehicleImage = requireActivity().intent.getStringExtra("vehicleImage").toString()!!
                status = requireActivity().intent.getStringExtra("status")!!
                driver_updatestatus = status

                // vehicle_type!!.setText(vehicleType+" "+"Image")

                Glide.with(requireActivity()).load(NetworkUtility.imgbaseUrl + vehicleImage)/*.placeholder(R.drawable.defaultperson)*/.error(R.drawable.defaultperson)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(vehicle_image!!)

                if (!status.equals(
                        "Requested",
                        ignoreCase = true
                    ) || !status.equals("PushedOutToDriver", ignoreCase = true)
                ) {

                    isAccepted = true

                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (defaultIdLog.equals("")) {

            defaultIdReg = sharedPreferences!!.getString("idSharedPref", "")!!
            fnameSharedPrefAfterReg =
                sharedPreferences!!.getString("fnameSharedPref", "").toString()
            lnameSharedPrefAfterReg =
                sharedPreferences!!.getString("lnameSharedPref", "").toString()
            tokenReg = sharedPreferences!!.getString("AuthSharedPref", "").toString()
        } else {

            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog", "")!!
            fnameSharedPrefAfterLog =
                sharedPreferences!!.getString("fnameSharedPrefAfterLog", "").toString()
            lnameSharedPrefAfterLog =
                sharedPreferences!!.getString("lnameSharedPrefAfterLog", "").toString()
            token = sharedPreferences!!.getString("Authorization", "").toString()
        }

        MapsInitializer.initialize(requireActivity())


        if (checkPermissions()) {

            if (isLocationEnabled(requireContext())) {


            } else {


                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, 100)

            }

        } else {

            requestPermissions()
        }


        navClick!!.setOnClickListener {

            (activity as HomeActivity).openCloseDrawer()

        }

        navClickAgain!!.setOnClickListener {

            (activity as HomeActivity).openCloseDrawer()
        }



        try {

            if (sharedPreferences!!.getString("availability", "").equals("On Call")) {

                onoffcall = true
                driver_status = "On Call"

                val req = HashMap<String, Any>()
                val gson = Gson()
                req["id"] = sharedPreferences!!.getString("idSharedPrefAfterLog", "")!!
                req["driver_long"] = c_long!!
                req["driver_lat"] = c_lat!!
                req["vehicle_type"] = sharedPreferences!!.getString("vehicle_type", "")!!

                val final = gson.toJson(req)
                try {
                    channel.trigger("client-send-driver-loc", final)

                } catch (e: Exception) {


                }

                iv_toggle!!.setImageResource(R.drawable.onlineimg)

                driverStatusUpdateAPICal(defaultIdLog, defaultIdReg, driver_status!!)



            } else if (availability.equals("Logged out", ignoreCase = true)) {

                availability = "Offline"
                onoffcall = false
                driver_status = "Offline"
                iv_toggle!!.setImageResource(R.drawable.offlineimg)
                driverStatusUpdateAPICal(defaultIdLog, defaultIdReg, driver_status!!)


            } else {

                const_servicecallaccept!!.visibility = View.GONE
                Log.v("onoffcall+", "onoffcall+" + onoffcall)
                Log.v("c_long+", "onoffcall+" + c_long)
                Log.v("c_lat+", "c_lat+" + c_lat)

                onoffcall = false
                driver_status = "Offline"
                iv_toggle!!.setImageResource(R.drawable.offlineimg)
                handler.removeCallbacksAndMessages(null)

                driverStatusUpdateAPICal(defaultIdLog, defaultIdReg, driver_status!!)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        iv_toggle!!.setOnClickListener {

            if (onoffcall == false) {

                Log.v("onoffcall+", "onoffcall+" + onoffcall)
                Log.v("c_long+", "onoffcall+" + c_long)
                Log.v("c_lat+", "c_lat+" + c_lat)
                onoffcall = true
                driver_status = "On Call"

                iv_toggle!!.setImageResource(R.drawable.onlineimg)

                val sharedPreferences = requireActivity().getSharedPreferences(
                    "MySharedPref",
                    AppCompatActivity.MODE_PRIVATE
                )
                val myEdit = sharedPreferences.edit()

                myEdit.putString("availability", "On Call")
                myEdit.apply()

                driverStatusUpdateAPICal(defaultIdLog, defaultIdReg, driver_status!!)

                //  location sent in pusher

                val req = HashMap<String, Any>()
                val gson = Gson()
                req["id"] = sharedPreferences!!.getString("idSharedPrefAfterLog", "")!!
                req["driver_long"] = c_long!!
                req["driver_lat"] = c_lat!!
                req["vehicle_type"] = sharedPreferences!!.getString("vehicle_type", "")!!

                val final = gson.toJson(req)
                try {
                    channel.trigger("client-send-driver-loc", final)

                } catch (e: Exception) {


                }
                Log.v("client-send-driver-loc", "client-send-driver-loc" + final)

                Log.v(
                    "triggerdone",
                    "" + distanceCalculate(
                        p_lat!!,
                        p_long!!,
                        c_lat!!,
                        c_long!!
                    )
                )

            } else {

                Log.v("onoffcall+", "onoffcall+" + onoffcall)
                Log.v("c_long+", "onoffcall+" + c_long)
                Log.v("c_lat+", "c_lat+" + c_lat)

                const_servicecallaccept!!.visibility = View.GONE

                onoffcall = false
                driver_status = "Offline"
                iv_toggle!!.setImageResource(R.drawable.offlineimg)
                val sharedPreferences = requireActivity().getSharedPreferences(
                    "MySharedPref",
                    AppCompatActivity.MODE_PRIVATE
                )
                val myEdit = sharedPreferences.edit()

                myEdit.putString("availability", "Offline")
                myEdit.apply()
                handler.removeCallbacksAndMessages(null)

                driverStatusUpdateAPICal(defaultIdLog, defaultIdReg, driver_status!!)
            }

            Log.v("color+", "color+" + onoffcall)

        }

        try {

            if (activity != null && isAdded()) {
                if (requireActivity().intent.hasExtra("status")) {

                    if (requireActivity().intent.getStringExtra("status")!! != null && requireActivity().intent.getStringExtra(
                            "status"
                        )!!.equals("Accepted", ignoreCase = true)
                    ) {

                        handler.removeCallbacksAndMessages(null)

                        constraintLayout!!.visibility = View.VISIBLE
                        includeheaderpart!!.visibility = View.VISIBLE

                        navClick!!.visibility = View.GONE
                        iv_menusymbol!!.visibility = View.VISIBLE
                        iv_toggle!!.visibility = View.GONE



                        showvalueafteraccept(
                            requireActivity().intent.getStringExtra("service_id")!!,
                            requireActivity().intent.getStringExtra("customerid")!!,
                            requireActivity().intent.getStringExtra("driverid")!!,
                            requireActivity().intent.getStringExtra("pickPlace")!!,
                            requireActivity().intent.getStringExtra("dropPlace")!!,
                            requireActivity().intent.getStringExtra("vehicleType")!!,
                            requireActivity().intent.getStringExtra("service_type")!!,
                            requireActivity().intent.getStringExtra("customer_image")!!,
                        )


                    }
                    else if (requireActivity().intent.getStringExtra("status")!!
                            .equals("ArrivedAtPickUp", ignoreCase = true)
                    ) {

                        includeheaderpart!!.visibility = View.VISIBLE
                        navClick!!.visibility = View.GONE
                        iv_toggle!!.visibility = View.GONE
                        includebottomsheet_cancelservicecall!!.visibility = View.VISIBLE

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            val dtf: DateTimeFormatter =
                                // DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS")
                                DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss.SSSS")
                            val now: LocalDateTime = LocalDateTime.now()
                            System.out.println("pujaa" + dtf.format(now));

                            val CurrentDate = dtf.format(now)
                            val FinalDate = requireActivity().intent.getStringExtra("arrived_time")!!
                            System.out.println("FinalDate" + FinalDate);
                            val date1: Date
                            val date2: Date
                            // val dates = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS")
                            val dates = SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSSS")
                            date1 = dates.parse(CurrentDate)
                            date2 = dates.parse(FinalDate)
                            Log.v("date1","date1"+date1)
                            Log.v("date2","date2"+date2)
                            // Calucalte time difference in second

                            timeInSecondsNew = (date1.time - date2.time) / 1000

                            Log.v("timeInSecondsNew" ,"timeInSecondsNew"+timeInSecondsNew)

                            if (timeInSecondsNew >= 30L) {

                                iv_cancelServicecall!!.visibility = View.INVISIBLE
                                cross_black!!.visibility = View.INVISIBLE
                                textView53!!.visibility = View.VISIBLE
                                iv_cancelServicecallblack!!.visibility = View.VISIBLE
                                cross_white!!.visibility = View.VISIBLE

                            } else {

                                iv_cancelServicecall!!.visibility = View.VISIBLE
                                cross_black!!.visibility = View.VISIBLE
                                textView53!!.visibility = View.VISIBLE
                                iv_cancelServicecallblack!!.visibility = View.INVISIBLE
                                cross_white!!.visibility = View.INVISIBLE


                            }

                        }
                        customerGreetStartTimerNew()


                        showvalueafteraccept(
                            requireActivity().intent.getStringExtra("service_id")!!,
                            requireActivity().intent.getStringExtra("customerid")!!,
                            requireActivity().intent.getStringExtra("driverid")!!,
                            requireActivity().intent.getStringExtra("pickPlace")!!,
                            requireActivity().intent.getStringExtra("dropPlace")!!,
                            requireActivity().intent.getStringExtra("vehicleType")!!,
                            requireActivity().intent.getStringExtra("service_type")!!,
                            requireActivity().intent.getStringExtra("customer_image")!!,
                        )

                        tv_customerlocation!!.setText(requireActivity().intent.getStringExtra("pickPlace")!!)


                        if (service_type.equals("")) {

                            tv_enterCustomerCode!!.setText("Enter Customer Code")

                        } else {

                            iv_cancelServicecall!!.visibility = View.GONE
                            cross_black!!.visibility = View.GONE
                            textView53!!.visibility = View.GONE
                            iv_cancelServicecallblack!!.visibility = View.GONE
                            cross_white!!.visibility = View.GONE
                            textView46!!.visibility = View.GONE
                            iv_timerview!!.visibility = View.GONE
                            tv_timer!!.visibility = View.GONE
                            tv_enterCustomerCode!!.setText("Enter Primary Driver Code")

                        }


                        tv_customerlocationForCancelService!!.setText(
                            requireActivity().intent.getStringExtra(
                                "pickPlace"
                            )
                        )

                    } else if (requireActivity().intent.getStringExtra("status")!!
                            .equals("CustomerCodeEntered", ignoreCase = true)
                    ) {


                        includeheaderpart!!.visibility = View.VISIBLE
                        includebottomsheet_arraivepickup_location!!.visibility = View.GONE
                        constraintLayout!!.visibility = View.GONE
                        constraintLayoutloadingInprogress!!.visibility = View.VISIBLE
                        navClick!!.visibility = View.GONE
                        iv_toggle!!.visibility = View.GONE

                        showvalueafteraccept(
                            requireActivity().intent.getStringExtra("service_id")!!,
                            requireActivity().intent.getStringExtra("customerid")!!,
                            requireActivity().intent.getStringExtra("driverid")!!,
                            requireActivity().intent.getStringExtra("pickPlace")!!,
                            requireActivity().intent.getStringExtra("dropPlace")!!,
                            requireActivity().intent.getStringExtra("vehicleType")!!,
                            requireActivity().intent.getStringExtra("service_type")!!,
                            requireActivity().intent.getStringExtra("customer_image")!!,
                        )

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            val dtf: DateTimeFormatter =
                                //  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS")
                                DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss.SSSS")
                            val now: LocalDateTime = LocalDateTime.now()
                            System.out.println("pujaa" + dtf.format(now));

                            val CurrentDate = dtf.format(now)
                            val FinalDate =
                                requireActivity().intent.getStringExtra("verifyCodeTime")!!
                            val date1: Date
                            val date2: Date
                            //val dates = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS")
                            val dates = SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSSS")
                            date1 = dates.parse(CurrentDate)
                            date2 = dates.parse(FinalDate)

                            // Calucalte time difference in second

                            timeInSeconds_inLoadingNew = (date1.time - date2.time) / 1000
                        }

                        loadingInProgressStartTimerNew()

                    } else if (requireActivity().intent.getStringExtra("status")!!
                            .equals("DrivingToDestination", ignoreCase = true)
                    ) {

                        handler.removeCallbacksAndMessages(null)
                        acceptservicecalltopview!!.visibility = View.VISIBLE
                        includeheaderpart!!.visibility = View.VISIBLE
                        view_pouseservice!!.visibility = View.VISIBLE
                        iv_menusymbol!!.visibility = View.VISIBLE
                        includebottomsheet_arraivepickup_location!!.visibility = View.GONE
                        constraintLayout!!.visibility = View.GONE
                        isStartDriving = false
                        isArrivingAtDestination = true

                        showvalueafteraccept(
                            requireActivity().intent.getStringExtra("service_id")!!,
                            requireActivity().intent.getStringExtra("customerid")!!,
                            requireActivity().intent.getStringExtra("driverid")!!,
                            requireActivity().intent.getStringExtra("pickPlace")!!,
                            requireActivity().intent.getStringExtra("dropPlace")!!,
                            requireActivity().intent.getStringExtra("vehicleType")!!,
                            requireActivity().intent.getStringExtra("service_type")!!,
                            requireActivity().intent.getStringExtra("customer_image")!!
                        )

                        constraintLayoutForDropOff!!.visibility = View.GONE
                        constraintLayoutForArraived!!.visibility = View.VISIBLE
                        view_pouseservice!!.visibility = View.VISIBLE
                        //     tv_pouseservice!!.visibility = View.VISIBLE
                        iv_menusymbol!!.visibility = View.VISIBLE
                        iv_cancelservicecall!!.visibility = View.GONE
                        imageView36!!.visibility = View.GONE
                        textView54!!.visibility = View.GONE
                        tv_dropofflocation_!!.setText(dropPlace)


                        /*  acceptservicecalltopview!!.visibility = View.VISIBLE
                          includeheaderpart!!.visibility = View.VISIBLE

                          navClick!!.visibility = View.GONE
                          iv_toggle!!.visibility = View.GONE

                          showvalueafteraccept(
                              requireActivity().intent.getStringExtra("service_id")!!,
                              requireActivity().intent.getStringExtra("customerid")!!,
                              requireActivity().intent.getStringExtra("driverid")!!,
                              requireActivity().intent.getStringExtra("pickPlace")!!,
                              requireActivity().intent.getStringExtra("dropPlace")!!,
                              requireActivity().intent.getStringExtra("vehicleType")!!,
                              requireActivity().intent.getStringExtra("service_type")!!,
                              requireActivity().intent.getStringExtra("customer_image")!!
                          )

                          constraintLayoutForDropOff!!.visibility = View.GONE
                          constraintLayoutForArraived!!.visibility = View.VISIBLE
                          view_pouseservice!!.visibility = View.VISIBLE
                        //  tv_pouseservice!!.visibility = View.VISIBLE
                          iv_menusymbol!!.visibility = View.VISIBLE

                          isArrivingAtDestination = true
                          tv_dropofflocation_!!.setText(requireActivity().intent.getStringExtra("dropPlace")!!)
  */

                    }
                    else if (requireActivity().intent.getStringExtra("status")!!.equals("ArrivedAtDestination", ignoreCase = true))
                    {

                        handler.removeCallbacksAndMessages(null)
                        includeheaderpart!!.visibility = View.VISIBLE
                        constraintLayoutForArraived!!.visibility = View.GONE
                        constraintLayoutUnloadingInprogress!!.visibility = View.VISIBLE
                        navClick!!.visibility = View.GONE
                        iv_toggle!!.visibility = View.GONE
                        includebottomsheet_arraivepickup_location!!.visibility = View.GONE
                        constraintLayout!!.visibility = View.GONE

                        showvalueafteraccept(
                            requireActivity().intent.getStringExtra("service_id")!!,
                            requireActivity().intent.getStringExtra("customerid")!!,
                            requireActivity().intent.getStringExtra("driverid")!!,
                            requireActivity().intent.getStringExtra("pickPlace")!!,
                            requireActivity().intent.getStringExtra("dropPlace")!!,
                            requireActivity().intent.getStringExtra("vehicleType")!!,
                            requireActivity().intent.getStringExtra("service_type")!!,
                            requireActivity().intent.getStringExtra("customer_image")!!

                        )


                        constraintLayoutUnloadingInprogress!!.visibility = View.VISIBLE
                        iv_menusymbol!!.visibility = View.VISIBLE


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            val dtf: DateTimeFormatter =
                                //DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS")
                                DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss.SSSS")
                            val now: LocalDateTime = LocalDateTime.now()
                            System.out.println("pujaa" + dtf.format(now))

                            val CurrentDate = dtf.format(now)
                            val FinalDate =
                                requireActivity().intent.getStringExtra("arrivedDestinationTime")!!
                            val date1: Date
                            val date2: Date
                            // val dates = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS")
                            val dates = SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSSS")
                            date1 = dates.parse(CurrentDate)
                            date2 = dates.parse(FinalDate)

                            // Calucalte time difference in second

                            timeInSeconds_unloadinginNew = (date1.time - date2.time) / 1000

                        }

                        unloadingInProgressStartTimerNew()


                    }

                    else if (requireActivity().intent.getStringExtra("status")!!
                            .equals("Paused", ignoreCase = true)
                    ) {

                        handler.removeCallbacksAndMessages(null)
                        includeheaderpart!!.visibility = View.VISIBLE
                        navClick!!.visibility = View.GONE
                        iv_toggle!!.visibility = View.GONE
                        reassign!!.visibility = View.GONE
                        includebottomsheet_arraivepickup_location!!.visibility = View.GONE

                        showvalueafteraccept(
                            requireActivity().intent.getStringExtra("service_id")!!,
                            requireActivity().intent.getStringExtra("customerid")!!,
                            requireActivity().intent.getStringExtra("driverid")!!,
                            requireActivity().intent.getStringExtra("pickPlace")!!,
                            requireActivity().intent.getStringExtra("dropPlace")!!,
                            requireActivity().intent.getStringExtra("vehicleType")!!,
                            requireActivity().intent.getStringExtra("service_type")!!,
                            requireActivity().intent.getStringExtra("customer_image")!!
                        )

                        constraintLayoutForDropOff!!.visibility = View.GONE
                        constraintLayoutForArraived!!.visibility = View.VISIBLE
                        // view_pouseservice!!.visibility = View.VISIBLE
                        //  tv_pouseservice!!.visibility = View.VISIBLE
                        iv_menusymbol!!.visibility = View.VISIBLE

                        tv_dropofflocation_!!.setText(requireActivity().intent.getStringExtra("dropPlace")!!)



                        view_pouseservice!!.visibility = View.GONE
                        iv_downarrow!!.visibility = View.GONE

                        //   tv_pouseservice!!.visibility = View.GONE
                        pauseservice!!.visibility = View.VISIBLE

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

/*
                            val dtf: DateTimeFormatter =
                                //  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS")
                                DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss.SSSS")
                            val now: LocalDateTime = LocalDateTime.now()
                            System.out.println("pujaa" + dtf.format(now));

                            val CurrentDate = dtf.format(now)
                            val FinalDate = requireActivity().intent.getStringExtra("pause_time")!!
                            val date1: Date
                            val date2: Date
                            // val dates = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS")
                            val dates = SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSSS")
                            date1 = dates.parse(CurrentDate)
                            date2 = dates.parse(FinalDate)

                            // Calucalte time difference in second

                            //  timeInSeconds_pauseNew = (date1.time - date2.time) / 1000

*/


                            val dtf: DateTimeFormatter =

                                DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss.SSSS")
                            val now: LocalDateTime = LocalDateTime.now()
                            System.out.println("pujaa" + dtf.format(now));

                            val CurrentDate = dtf.format(now)
                            val FinalDate = requireActivity().intent.getStringExtra("pause_time")!!
                            System.out.println("FinalDate" + FinalDate);
                            val date1: Date
                            val date2: Date
                            // val dates = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS")
                            val dates = SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSSS")
                            date1 = dates.parse(CurrentDate)
                            date2 = dates.parse(FinalDate)
                            Log.v("date1","date1"+date1)
                            Log.v("date2","date2"+date2)
                            // Calucalte time difference in second

                            timeInSeconds_pauseNew = (date1.time - date2.time) / 1000

                            Log.v("timeInSecondsNew" ,"timeInSecondsNew"+timeInSeconds_pauseNew)


                            startTimerOnPauseClickNew()

                        }


                    }

                }
            }
        } catch (e: Exception) {

            e.printStackTrace()
        }


        iv_cancelServicecallblack!!.setOnClickListener {

            view_cancelservicecall!!.visibility = View.VISIBLE

        }

        iv_navigatebuttonAfterAccepet!!.setOnClickListener {

            var gmmIntentUri =
                Uri.parse("google.navigation:q=" + pick_latFromList + "," + pick_longFromList + "&mode=d");
            var mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent)

            includeheaderpart!!.visibility = View.GONE

            isNearBy = true


        }

        iv_navigatebutton!!.setOnClickListener {

            var gmmIntentUri =
                Uri.parse("google.navigation:q=" + pick_latFromList + "," + pick_longFromList + "&mode=d");
            var mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent)

            includeheaderpart!!.visibility = View.GONE

            isNearBy = true


        }

        iv_arrivedtoDest_navigate!!.setOnClickListener {

            var gmmIntentUri =
                Uri.parse("google.navigation:q=" + dest_latFromList + "," + dest_longFromList + "&mode=d");
            var mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent)

            includeheaderpart!!.visibility = View.VISIBLE

            // isNearBy = true

            constraintLayoutForDropOff!!.visibility = View.GONE
            constraintLayoutForArraived!!.visibility = View.VISIBLE
            view_pouseservice!!.visibility = View.VISIBLE
            //     tv_pouseservice!!.visibility = View.VISIBLE
            iv_menusymbol!!.visibility = View.VISIBLE
            iv_cancelservicecall!!.visibility = View.GONE
            imageView36!!.visibility = View.GONE
            textView54!!.visibility = View.GONE
            tv_dropofflocation_!!.setText(dropPlace)

        }

        iv_navigatebuttonForReassignment!!.setOnClickListener {

            var gmmIntentUri =
                Uri.parse("google.navigation:q=" + previous_driver_lat + "," + previous_driver_long + "&mode=d")
            var mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)

           // reassign!!.visibility = View.VISIBLE

        }

        back!!.setOnClickListener {

            callcustomercare!!.visibility = View.GONE
        }

        btn_continueunProgressloading!!.setOnClickListener {
            if (activity != null && isAdded()) {

                customercontact!!.visibility = View.GONE
                transview!!.visibility = View.GONE
                mHandler?.removeCallbacks(mStatusChecker)
                customercontact!!.visibility = View.GONE

                verifyviewInclude!!.visibility = View.GONE
                //  cancelservicecall!!.visibility = View.GONE
                constraintLayoutloadingInprogress!!.visibility = View.GONE
                includebottomsheet_cancelservicecall!!.visibility = View.GONE

                //  bottomSheetBehaviorCancelService!!.state = BottomSheetBehavior.STATE_COLLAPSED
                constraintLayout!!.visibility = View.GONE
                constraintLayoutForDropOff!!.visibility = View.VISIBLE
                iv_cancelservicecall!!.visibility = View.VISIBLE
                imageView36!!.visibility = View.VISIBLE
                textView54!!.visibility = View.VISIBLE
                view_pouseservice!!.visibility = View.VISIBLE
                //   tv_pouseservice!!.visibility = View.GONE
                imageView36!!.visibility = View.GONE
                textView54!!.visibility = View.GONE
                verifyviewInclude!!.visibility = View.GONE
                tv_dropofflocation!!.setText(requireActivity().intent.getStringExtra("dropPlace")!!)

                isStartDriving = true
            }


        }

        iv_cancelservicecall!!.setOnClickListener {

            afterClickOncancelserviceViewReason!!.visibility = View.VISIBLE
        }

        go_back_!!.setOnClickListener {

            afterClickOncancelserviceViewReason!!.visibility = View.GONE
        }

       /* cancel_call_!!.setOnClickListener {
            if (activity != null && isAdded()) {
                val myIntent = Intent(requireActivity(), CancelServiceCallScreen::class.java)
                myIntent.putExtra(
                    "service_id",
                    requireActivity().intent.getStringExtra("service_id")!!
                )
                startActivity(myIntent)
            }
        }*/


        go_back!!.setOnClickListener {

            view_cancelservicecall!!.visibility = View.GONE

        }

        go_back_!!.setOnClickListener {

            afterClickOncancelserviceViewReason!!.visibility = View.GONE
        }

        cancel_call!!.setOnClickListener {

            view_cancelservicecall!!.visibility = View.GONE

            normalwiseServiceCallCancel(service_id)

        }

        view_takepicture!!.setOnClickListener {

            customercontact!!.visibility = View.GONE
            reassign!!.visibility = View.GONE


            try {

                if (activity != null && isAdded()) {

                    AppUtilities.hideSoftKeyboard(requireActivity())

                    driver_updatestatus = "ArrivedAtDestination"

                    val req = HashMap<String, Any>()
                    val gson = Gson()
                    req["id"] = service_idstore
                    req["driver_long"] = c_long!!
                    req["driver_lat"] = c_lat!!
                    req["status"] = driver_updatestatus!!

                    val final = gson.toJson(req)
                    try {
                        channel.trigger("client-order-progress-status", final)

                    } catch (e: Exception) {

                    }
                    onPayInDocUpload()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btn_uploadSuccessfully!!.setOnClickListener {
            customercontact!!.visibility = View.GONE

            try {
                if (activity != null && isAdded()) {

                    servicecallcompleteAPIcalling(
                        requireActivity().intent.getStringExtra("service_id")!!,
                        requireActivity().intent.getStringExtra("customerid")!!,
                        requireActivity().intent.getStringExtra("driverid")!!,
                        requireActivity().intent.getStringExtra("pickPlace")!!,
                        requireActivity().intent.getStringExtra("dropPlace")!!,
                        requireActivity().intent.getStringExtra("vehicleType")!!,
                        requireActivity().intent.getStringExtra("customer_image")!!

                    )
                }
            } catch (e: java.lang.Exception) {

                e.printStackTrace()
            }


        }


        btn_verify!!.setOnClickListener {
            customercontact!!.visibility = View.GONE
            if (activity != null && isAdded()) {
                callVerifyOTP(
                    requireActivity().intent.getStringExtra("service_id")!!,
                    requireActivity().intent.getStringExtra("customerid")!!,
                    requireActivity().intent.getStringExtra("driverid")!!,
                    requireActivity().intent.getStringExtra("pickPlace")!!,
                    requireActivity().intent.getStringExtra("dropPlace")!!,
                    requireActivity().intent.getStringExtra("vehicleType")!!
                )

            }
        }


        btn_continueunProgress!!.setOnClickListener {

            mHandlerunloading!!.removeCallbacksAndMessages(null)
            customercontact!!.visibility = View.GONE

            showvalueafteraccept(
                service_id!!,
                customerId!!,
                driverId!!,
                pickPlace!!,
                dropPlace!!,
                vehicleType,
                service_type,
                customer_image
            )

            constraintLayoutUnloadingInprogress!!.visibility = View.GONE
            constraintLayoutProofofDropoff!!.visibility = View.VISIBLE

        }

        btn_uploadSuccessfully!!.setOnClickListener {

            customercontact!!.visibility = View.GONE

            servicecallcompleteAPIcalling(
                service_id,
                customerId,
                driverId,
                pickPlace,
                dropPlace,
                vehicleType,
                customer_image
            )

        }


        tv_resumeCallService!!.setOnClickListener {

            view_pouseservice!!.visibility = View.VISIBLE
            iv_downarrow!!.visibility = View.VISIBLE
            pauseservice!!.visibility = View.GONE

            try {

                if (activity != null && isAdded()) {

                    AppProgressBar.openLoader(
                        requireActivity(),
                        requireActivity().getResources().getString(R.string.pleasewait)
                    )

                    val apiservice: ApiInterface =
                        ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                    val req = HashMap<String, Any>()


                    if (defaultIdLog.equals("")) {

                        req["driver_id"] = defaultIdReg
                        req["service_id"] = /*storeservice_id*/ service_id

                        if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                            service_type = ""
                        }
                        else{
                            service_type = "secondary"
                        }

                        req["service_type"] = service_type!!

                        responseServicecallPause =
                            apiservice.doingservicecallpause(tokenReg, req)
                    } else {

                        req["driver_id"] = defaultIdLog
                        req["service_id"] = /*storeservice_id*/ service_id

                        if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                            service_type = ""
                        }
                        else{
                            service_type = "secondary"
                        }

                        req["service_type"] = service_type!!

                        responseServicecallPause = apiservice.doingservicecallpause(token, req)

                    }

                    responseServicecallPause!!.enqueue(object : Callback<VerifyOtpPojo?> {
                        @SuppressLint("LongLogTag")
                        override fun onResponse(
                            call: Call<VerifyOtpPojo?>,
                            response1: Response<VerifyOtpPojo?>
                        ) {
                            if (response1.isSuccessful()) {

                                AppProgressBar.closeLoader()
                                timeInSeconds_pause = 0L
                                timeInSeconds_pauseNew = 0L
                                driver_updatestatus = ""
                                driver_updatestatus = /*"Resume"*/ "DrivingToDestination"

                                val req = HashMap<String, Any>()
                                val gson = Gson()
                                req["id"] = service_id
                                req["driver_long"] = c_long!!
                                req["driver_lat"] = c_lat!!
                                req["status"] = driver_updatestatus!!

                                val final = gson.toJson(req)
                                try {
                                    channel.trigger("client-order-progress-status", final)

                                } catch (e: Exception) {

                                    e.printStackTrace()
                                }
                                Log.v(
                                    "clientorderprogressstatus",
                                    "clientorderprogressstatus" + final
                                )

                                view_pouseservice!!.visibility = View.VISIBLE
                                iv_downarrow!!.visibility = View.VISIBLE
                                pauseservice!!.visibility = View.GONE
                                mHandlerForPause?.removeCallbacksAndMessages(null)

                            } else {

                                AppProgressBar.closeLoader()

                                if (response1.body() != null) {

                                    if (response1.body()!!.message.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                            ignoreCase = true
                                        )
                                    ) {

                                        if (activity != null && isAdded()) {

                                            val sharedPreferences =
                                                requireActivity().getSharedPreferences(
                                                    "MySharedPref",
                                                    Context.MODE_PRIVATE
                                                )
                                            val editor = sharedPreferences.edit()
                                            sharedPreferenceManager!!.logoutUser()
                                            editor.clear()
                                            editor.commit()


                                            val myIntent =
                                                Intent(
                                                    requireActivity(),
                                                    WelcomeActivity::class.java
                                                )
                                            startActivity(myIntent)

                                            requireActivity().finish()

                                        }
                                    } else {

                                        if (activity != null && isAdded()) {
                                            Toast.makeText(requireActivity(), response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()

                                        }
                                    }

                                }
                            }
                        }

                        override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                            AppProgressBar.closeLoader()
                            if (t is NoConnectivityException) {

                                if (activity != null && isAdded()) {

                                    Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()

                                } else {

                                    Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }




        cancel_call_!!.setOnClickListener {

            if (activity != null && isAdded()) {

                val myIntent = Intent(requireActivity(), CancelServiceCallScreen::class.java)
                myIntent.putExtra("service_id", service_id)
                myIntent.putExtra("driver_long", c_long)
                myIntent.putExtra("driver_lat", c_lat)
                startActivity(myIntent)
            }
        }

        // 9th June

        tv_enterCustomerCode!!.setOnClickListener {

            verifyviewInclude!!.visibility = View.VISIBLE
            customercontact!!.visibility = View.GONE


            if (service_type.equals("")) {

                tv_verifypickupcode!!.setText("Verify Customer pickup code")

            } else {


                tv_verifypickupcode!!.setText("Verify Primary Service Provider Code")

            }

            clickOnPinView(edt_password1!!, edt_password2!!, edt_password3!!, edt_password4!!)

        }

        iv_back!!.setOnClickListener {

            verifyviewInclude!!.visibility = View.GONE

        }

        tv_pouseservice!!.setOnClickListener {

            callPauseServiceCallAPI(service_id)

        }

        tv_resumeCallService!!.setOnClickListener {

            view_pouseservice!!.visibility = View.VISIBLE
            iv_downarrow!!.visibility = View.VISIBLE
            pauseservice!!.visibility = View.GONE

            try {
                if (activity != null && isAdded()) {


                    AppProgressBar.openLoader(
                        requireActivity(),
                        requireActivity().getResources().getString(R.string.pleasewait)
                    )

                    val apiservice: ApiInterface =
                        ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                    val req = HashMap<String, Any>()


                    if (defaultIdLog!!.equals("")) {

                        req["driver_id"] = defaultIdReg
                        req["service_id"] = service_id

                        if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                            service_type = ""
                        }
                        else{
                            service_type = "secondary"
                        }

                        req["service_type"] = service_type!!

                        responseServicecallPause = apiservice.doingservicecallpause(tokenReg, req)
                    } else {

                        req["driver_id"] = defaultIdLog
                        req["service_id"] = service_id

                        if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                            service_type = ""
                        }
                        else{
                            service_type = "secondary"
                        }

                        req["service_type"] = service_type!!

                        responseServicecallPause = apiservice.doingservicecallpause(token, req)

                    }

                    responseServicecallPause!!.enqueue(object : Callback<VerifyOtpPojo?> {
                        override fun onResponse(
                            call: Call<VerifyOtpPojo?>,
                            response1: Response<VerifyOtpPojo?>
                        ) {
                            if (response1.isSuccessful()) {

                                AppProgressBar.closeLoader()
                                timeInSeconds_pause = 0L
                                timeInSeconds_pauseNew = 0L


                                driver_updatestatus = /*"Resume"*/ "DrivingToDestination"

                                mLocationService = LocationService()
                                mServiceIntent = Intent(requireActivity(), mLocationService.javaClass)

                                if (!Util.isMyServiceRunning(mLocationService.javaClass, requireActivity())) {

                                    mServiceIntent.putExtra("id", service_id)
                                    mServiceIntent.putExtra("status", driver_updatestatus)
                                    requireActivity().startService(mServiceIntent)

                                } else {

                                }

                                // 27.10.22

                               /* val req = HashMap<String, Any>()
                                val gson = Gson()
                                req["id"] = service_id
                                req["driver_long"] = c_long!!
                                req["driver_lat"] = c_lat!!
                                req["status"] = driver_updatestatus!!

                                val final = gson.toJson(req)
                                try {

                                    channel.trigger("client-order-progress-status", final)

                                } catch (e: Exception) {

                                }
                                Log.v("clientordrprgrestatus", "clientorderprogressstatus" + final)*/

                                view_pouseservice!!.visibility = View.VISIBLE
                                iv_downarrow!!.visibility = View.VISIBLE

                                //    tv_pouseservice!!.visibility = View.VISIBLE
                                pauseservice!!.visibility = View.GONE
                                mHandlerForPause?.removeCallbacksAndMessages(null)

                            } else {

                                AppProgressBar.closeLoader()

                                if (response1.body() != null) {

                                    if (response1.body()!!.message.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                            ignoreCase = true
                                        )
                                    ) {

                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()


                                        val myIntent =
                                            Intent(
                                                requireActivity(),
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        requireActivity().finish()

                                    } else {


                                        Toast.makeText(requireActivity(), response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()


                                    }

                                }
                            }
                        }

                        override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                            AppProgressBar.closeLoader()
                            if (t is NoConnectivityException) {
                                Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                            } else {

                                Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


/*
        btn_arrivedToDestReach!!.setOnClickListener {

            isArrivingAtDestination = false
            customercontact!!.visibility = View.GONE
            iv_downarrow!!.visibility = View.VISIBLE


            callArrivedToDest(
                service_id,
                customerId,
                driverId,
                pickPlace,
                dropPlace,
                vehicleType,
                service_type
            )

        }
*/

        btn_arrivedToDest!!.onSlideCompleteListener =
            object : SlideToActView.OnSlideCompleteListener {
                override fun onSlideComplete(view: SlideToActView) {
                    Log.e("complete2", "test")

                    isArrivingAtDestination = false
                    customercontact!!.visibility = View.GONE
                    view_pouseservice!!.visibility = View.GONE
                    iv_downarrow!!.visibility = View.VISIBLE

                    iv_arrivedtoDest_navigate!!.visibility = View.INVISIBLE
                    iv_navigateArrived!!.visibility = View.INVISIBLE
                    tv_navigateArrived!!.visibility = View.INVISIBLE

                    callArrivedToDest(
                        service_id,
                        customerId,
                        driverId,
                        pickPlace,
                        dropPlace,
                        vehicleType,
                        service_type
                    )


                }

            }


        txt_additional_comments!!.setOnTouchListener(OnTouchListener { view, motionEvent ->
            txt_additional_comments!!.isFocusableInTouchMode = true
            txt_additional_comments!!.setCursorVisible(true)
            false
        })

        txt_additional_comments!!.setImeOptions(EditorInfo.IME_ACTION_DONE)
        txt_additional_comments!!.setRawInputType(InputType.TYPE_CLASS_TEXT)

        txt_additional_comments!!.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (activity != null && isAdded()) {
                    val imm =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(txt_additional_comments!!.getWindowToken(), 0)
                }
            }
            false
        })

        btn_submitRatingFromDriversite!!.setOnClickListener {

            getRatingApiCalling(
                service_id,
                customerId,
                driverId,
                pickPlace,
                dropPlace,
                vehicleType
            )


        }

        btn_continueunProgressloading!!.setOnClickListener {

            showvalueafteraccept(
                service_id!!,
                customerId!!,
                driverId!!,
                pickPlace!!,
                dropPlace!!,
                vehicleType,
                service_type!!,
                customer_image
            )

            transview!!.visibility = View.GONE
            verifyviewInclude!!.visibility = View.GONE
            customercontact!!.visibility = View.GONE
            iv_downarrow!!.visibility = View.VISIBLE

            constraintLayoutloadingInprogress!!.visibility = View.GONE
            includebottomsheet_cancelservicecall!!.visibility = View.GONE

            constraintLayout!!.visibility = View.GONE
            constraintLayoutForDropOff!!.visibility = View.VISIBLE
            iv_cancelservicecall!!.visibility = View.VISIBLE
            imageView36!!.visibility = View.VISIBLE
            textView54!!.visibility = View.VISIBLE
            tv_dropofflocation!!.setText(dropPlace)
            iv_menusymbol!!.visibility = View.VISIBLE
            mHandlerinProgress!!.removeCallbacksAndMessages(null)


        }

        iv_cancelservicecall!!.setOnClickListener {

            afterClickOncancelserviceViewReason!!.visibility = View.VISIBLE

        }


/*
        btn_startDriving!!.setOnClickListener {

            callStartDriving(
                service_id,
                customerId,
                driverId,
                pickPlace,
                dropPlace,
                vehicleType
            )
            customercontact!!.visibility = View.GONE
            iv_downarrow!!.visibility = View.VISIBLE


        }
*/

        btn_startDriving!!.onSlideCompleteListener =
            object : SlideToActView.OnSlideCompleteListener {
                override fun onSlideComplete(view: SlideToActView) {
                    Log.e("complete2", "test")

                    callStartDriving(
                        service_id,
                        customerId,
                        driverId,
                        pickPlace,
                        dropPlace,
                        vehicleType
                    )
                    customercontact!!.visibility = View.GONE
                    iv_downarrow!!.visibility = View.VISIBLE
                    view_pouseservice!!.visibility = View.VISIBLE


                }

            }

        btn_startDrivingreassignment!!.onSlideCompleteListener =
            object : SlideToActView.OnSlideCompleteListener {
                override fun onSlideComplete(view: SlideToActView) {
                    Log.e("reassin", "test")


                    callresumeAPIforReassignServiceCall(
                        service_id,
                        customerId,
                        driverId,
                        pickPlace,
                        dropPlace,
                        vehicleType)

                    customercontact!!.visibility = View.GONE
                    iv_downarrow!!.visibility = View.VISIBLE
                    view_pouseservice!!.visibility = View.VISIBLE


                }

            }



        btn_uploadProve!!.setOnClickListener {

            customercontact!!.visibility = View.GONE
            iv_downarrow!!.visibility = View.VISIBLE

            if (storeImage.equals("")) {

                if (activity != null && isAdded()) {

                    Toast.makeText(requireActivity(), "Please take a picture of delivered cargo", Toast.LENGTH_SHORT).show()

                }
            } else {
                constraintLayoutProofofDropoff!!.visibility = View.GONE
                reassign!!.visibility = View.GONE
                constraintLayoutUploadedSuccessfully!!.visibility = View.VISIBLE
            }

        }


/*
        tv_accept!!.setOnClickListener {


            Log.v("service_type","a:"+service_type)


            getAfterAcceptServiceCallAPI(
                service_id,
                customerId,
                driverId,
                pickPlace,
                dropPlace,
                vehicleType,
                service_type,
                loadunloadafterAccptInServiceCall!!
            )

        }
*/

        tv_accept!!.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                Log.e("complete1", "test")

                Log.v("service_type", "a:" + service_type)



            }

        }
        tv_accept!!.onSlideResetListener = object : SlideToActView.OnSlideResetListener {
            override fun onSlideReset(view: SlideToActView) {
                Log.e("complete", "reset")
            }
        }

        tv_accept!!.onSlideUserFailedListener = object : SlideToActView.OnSlideUserFailedListener {
            override fun onSlideFailed(view: SlideToActView, isOutside: Boolean) {
                Log.e("complete", "faild")

                tv_accept!!.resetSlider()

            }
        }

        tv_accept!!.onSlideToActAnimationEventListener =
            object : SlideToActView.OnSlideToActAnimationEventListener {
                override fun onSlideCompleteAnimationStarted(
                    view: SlideToActView,
                    threshold: Float
                ) {

                }

                override fun onSlideCompleteAnimationEnded(view: SlideToActView) {
                    Log.v("dsj","")

                    const_servicecallaccept!!.visibility = View.GONE

                    getAfterAcceptServiceCallAPI(
                        service_idstore,
                        customerId,
                        driverId,
                        pickPlace,
                        dropPlace,
                        vehicleType,
                        service_type,
                        loadunloadafterAccptInServiceCall!!
                    )
                    tv_accept!!.resetSlider()
                }

                override fun onSlideResetAnimationStarted(view: SlideToActView) {

                }

                override fun onSlideResetAnimationEnded(view: SlideToActView) {



                }
            }


        tv_decln!!.setOnClickListener {

            const_servicecallaccept!!.visibility = View.GONE

            //changes for store service id
            getDecline(service_idstore)

        }

        iv_navigatebuttonAfterAccepet!!.setOnClickListener {

            var gmmIntentUri =
                Uri.parse("google.navigation:q=" + pick_latFromList + "," + pick_longFromList + "&mode=d");
            var mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

            isNearBy = true


        }


        bt_arraivalAtPickup!!.setOnClickListener {

            customercontact!!.visibility = View.GONE
            iv_downarrow!!.visibility = View.VISIBLE

            isNearBy = false

            includebottomsheet_arraivepickup_location!!.visibility = View.GONE

            arrivedPickUpLocationAPICalling(
                service_id,
                customerId,
                driverId,
                pickPlace,
                dropPlace,
                vehicleType ,
                customer_image
            )
        }

        bt_arraivalAtPickupF!!.setOnClickListener {

            customercontact!!.visibility = View.GONE
            iv_downarrow!!.visibility = View.VISIBLE

            isNearBy = false

            includebottomsheet_arraivepickup_location!!.visibility = View.GONE

            arrivedPickUpLocationAPICalling(
                service_id,
                customerId,
                driverId,
                pickPlace,
                dropPlace,
                vehicleType,
                customer_image
            )
        }

        iv_downarrow!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {

                if (loadUnload == false && service_type.equals("")) {
                    //  if (service_type.equals("")) {
                    includebottomsheet_arraivepickup_location!!.visibility = View.GONE
                    //constraintLayout!!.visibility = View.GONE
                    bottomshowhidepart!!.visibility = View.VISIBLE
                    customercontact!!.visibility = View.VISIBLE
                    secondPartContact!!.visibility = View.VISIBLE
                    iv_uparrowhideview!!.visibility = View.VISIBLE
                    view_contactseconddaryassist!!.visibility = View.GONE
                    iv_downarrow!!.visibility = View.VISIBLE
                    view_pouseservice!!.visibility = View.GONE

                    loadunloadiv!!.setImageResource(R.drawable.cancelfalseicon);
                    loadunloadiv!!.getLayoutParams().height = 35
                    loadunloadiv!!.getLayoutParams().width = 35

                    if (unattendDrop == true) {

                        unatndiv!!.setImageResource(R.drawable.checkmarkcircle);
                        unatndiv!!.getLayoutParams().height = 35
                        unatndiv!!.getLayoutParams().width = 35

                    } else {
                        unatndiv!!.setImageResource(R.drawable.cancelfalseicon);
                        unatndiv!!.getLayoutParams().height = 35
                        unatndiv!!.getLayoutParams().width = 35
                    }

                    if (activity != null && isAdded()) {
                        Glide.with(requireActivity()).load(NetworkUtility.imgbaseUrl + vehicleImage)/*.placeholder(R.drawable.defaultperson)*/.error(R.drawable.defaultperson)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(vehicle_image!!)
                    }

                } else {

                    if (loadUnload== true && /*service_type.equals("")*/
                        tv_servicename!!.text.toString().trim().equals("Primary Service")) {


                        bottomshowhidepart!!.visibility = View.VISIBLE
                        customercontact!!.visibility = View.VISIBLE
                        tv_customercontact!!.visibility = View.VISIBLE
                        iv_uparrowhideview!!.visibility = View.VISIBLE
                        secondPartContact!!.visibility = View.VISIBLE
                        iv_downarrow!!.visibility = View.GONE
                        view_contactseconddaryassist!!.visibility = View.VISIBLE
                        view_pouseservice!!.visibility = View.GONE

                        loadunloadiv!!.setImageResource(R.drawable.checkmarkcircle);
                        loadunloadiv!!.getLayoutParams().height = 35
                        loadunloadiv!!.getLayoutParams().width = 35
                        view_contactseconddaryassist!!.visibility = View.VISIBLE

                        if (unattendDrop == true) {

                            unatndiv!!.setImageResource(R.drawable.checkmarkcircle);
                            unatndiv!!.getLayoutParams().height = 35
                            unatndiv!!.getLayoutParams().width = 35

                        } else {
                            unatndiv!!.setImageResource(R.drawable.cancelfalseicon);
                            unatndiv!!.getLayoutParams().height = 25
                            unatndiv!!.getLayoutParams().width = 25
                        }

                        if (activity != null && isAdded()) {
                            Glide.with(requireActivity())
                                .load(NetworkUtility.imgbaseUrl + vehicleImage)/*.placeholder(R.drawable.defaultperson)*/
                                .error(R.drawable.defaultperson)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).into(vehicle_image!!)

                        }
                    }
                    else{


                        customercontact!!.visibility = View.VISIBLE
                        showhideview!!.visibility = View.VISIBLE
                        bottomshowhidepart!!.visibility = View.VISIBLE
                        tv_customercontact!!.setText("Contact Primary Driver")
                        secondPartContact!!.visibility = View.GONE
                        iv_uparrowhideview!!.visibility = View.VISIBLE
                        iv_downarrow!!.visibility = View.INVISIBLE
                        view_contactseconddaryassist!!.visibility = View.GONE
                        view_pouseservice!!.visibility = View.GONE


                    }

                }
            }


        })

        iv_uparrowhideview!!.setOnClickListener {

            bottomshowhidepart!!.visibility = View.GONE
            customercontact!!.visibility = View.GONE
            iv_downarrow!!.visibility = View.VISIBLE

        }

        iv_back!!.setOnClickListener {

            verifyviewInclude!!.visibility = View.GONE

        }

        btn_verify!!.setOnClickListener {

            transview!!.visibility = View.GONE
            customercontact!!.visibility = View.GONE
            iv_downarrow!!.visibility = View.VISIBLE

            callVerifyOTP(
                service_id,
                customerId,
                driverId,
                pickPlace,
                dropPlace,
                vehicleType
            )

        }

        emergencyview!!.setOnClickListener {

            emergencyview!!.visibility = View.GONE

        }

        imagev_servicaCallPause!!.setOnClickListener {

            tv_loadunloadtooltip!!.visibility = View.VISIBLE
            iv_loadunloadtooltip!!.visibility = View.VISIBLE
            tv_unattentooltip!!.visibility = View.GONE
            iv_unattentooltip!!.visibility = View.GONE
        }
        iv_unatten!!.setOnClickListener {

            iv_unattentooltip!!.visibility = View.VISIBLE
            tv_unattentooltip!!.visibility = View.VISIBLE
            tv_loadunloadtooltip!!.visibility = View.GONE
            iv_loadunloadtooltip!!.visibility = View.GONE
        }

        customercontact!!.setOnClickListener {

            tv_loadunloadtooltip!!.visibility = View.GONE
            iv_loadunloadtooltip!!.visibility = View.GONE
            tv_unattentooltip!!.visibility = View.GONE
            iv_unattentooltip!!.visibility = View.GONE
        }

        secondPartContact!!.setOnClickListener {

            tv_loadunloadtooltip!!.visibility = View.GONE
            iv_loadunloadtooltip!!.visibility = View.GONE
            tv_unattentooltip!!.visibility = View.GONE
            iv_unattentooltip!!.visibility = View.GONE
        }

        view_customercontact!!.setOnClickListener {


            if(tv_customercontact!!.text.toString().equals("Contact Primary Driver")){
                chatClickOn="contactPrimary"
              //  name!!.text = requireActivity().intent.getStringExtra("driver_id")
                name!!.text = primaryDriverName
            }
            else{
                chatClickOn="contactCustomer"
                name!!.text = customer_name
            }



            iv_downarrow!!.visibility = View.VISIBLE
            customercontact!!.visibility = View.GONE
            tv_loadunloadtooltip!!.visibility = View.GONE
            iv_loadunloadtooltip!!.visibility = View.GONE
            tv_unattentooltip!!.visibility = View.GONE
            iv_unattentooltip!!.visibility = View.GONE
            // constraintLayout!!.visibility = View.VISIBLE
            relativeChatBottomsheet!!.visibility = View.VISIBLE
            bottomSheetBehaviorChat!!.peekHeight = resources.getDimension(R.dimen._380sdp).toInt()
            bottomSheetBehaviorChat!!.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehaviorChat.isDraggable = false



            if (activity != null && isAdded())
                chatAdapter = ChatAdapter(requireActivity(), driverId)

            setupRecyleview()

            //pusher = Pusher("d5041f2ec10120e451ce", options)
            pusher = Pusher(NetworkUtility.key, options)

            pusher!!.connect(object : ConnectionEventListener {
                override fun onConnectionStateChange(change: ConnectionStateChange) {
                    Log.i(
                        "Pusher",
                        "State changed from ${change.previousState} to ${change.currentState}"
                    )

                    if (change.currentState.toString().equals("CONNECTED", ignoreCase = true)) {
                        val socketId = pusher!!.connection.socketId
                        if (socketId != null){
                            val channelname = "private-my-channel"
                            Log.v("socketId", "socketId" + socketId)
                            val parameters = HashMap<String, String>()
                            parameters["socket_id"] = socketId
                            parameters["channel_name"] = "private-my-channel"
                            authorizer.setHeaders(parameters)
                            pusher!!.unsubscribe(channelname)

                            channel = pusher!!.subscribePrivate("private-my-channel")

                            channel.bind("client-send-message", object : PrivateChannelEventListener {
                                override fun onAuthenticationFailure(
                                    message: String,
                                    e: java.lang.Exception
                                ) {
                                }

                                override fun onSubscriptionSucceeded(channelName: String) {

                                }

                                override fun onEvent(event: PusherEvent) {

                                    Log.i("Pusher", "Received event with data: $event")
                                    Log.v("coneccteddriverdata", "" + event.data)

                                    if (activity != null && isAdded()) {

                                        requireActivity().runOnUiThread {

                                            if (NetworkUtility.isNetworkAvailable(requireActivity())) {
                                                callChatAPI(chatAdapter, true)
                                            }
                                            else{

                                                Toast.makeText(
                                                    requireActivity(),
                                                    getString(R.string.msg_no_internet),
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }


                                        }
                                    }


                                }
                            })

                        }
                    }


                }

                override fun onError(message: String?, code: String?, e: java.lang.Exception?) {

                    Log.i(
                        "Pusher",
                        "There was a problem connecting! code ($code), message ($message), exception($e)"
                    )
                }

            }, ConnectionState.ALL)

        }

                //chat with Secondarydriver
        view_contactseconddaryassist!!.setOnClickListener {

            chatClickOn="contactSecondaryAssist"

            iv_downarrow!!.visibility = View.VISIBLE
            customercontact!!.visibility = View.GONE
            tv_loadunloadtooltip!!.visibility = View.GONE
            iv_loadunloadtooltip!!.visibility = View.GONE
            tv_unattentooltip!!.visibility = View.GONE
            iv_unattentooltip!!.visibility = View.GONE
            // constraintLayout!!.visibility = View.VISIBLE
            relativeChatBottomsheet!!.visibility = View.VISIBLE
            bottomSheetBehaviorChat!!.peekHeight = resources.getDimension(R.dimen._380sdp).toInt()
            bottomSheetBehaviorChat!!.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehaviorChat.isDraggable = false


           // name!!.text = requireActivity().intent.getStringExtra("second_driver_id")
               name!!.text = secondaryDriverName

            if (activity != null && isAdded())
                chatAdapter = ChatAdapter(requireActivity(), driverId)

            setupRecyleview()

            //pusher = Pusher("d5041f2ec10120e451ce", options)
            pusher = Pusher(NetworkUtility.key, options)

            pusher!!.connect(object : ConnectionEventListener {
                override fun onConnectionStateChange(change: ConnectionStateChange) {
                    Log.i(
                        "Pusher",
                        "State changed from ${change.previousState} to ${change.currentState}"
                    )

                    if (change.currentState.toString().equals("CONNECTED", ignoreCase = true)) {
                        val socketId = pusher!!.connection.socketId
                        if (socketId != null){
                            val channelname = "private-my-channel"
                            Log.v("socketId", "socketId" + socketId)
                            val parameters = HashMap<String, String>()
                            parameters["socket_id"] = socketId
                            parameters["channel_name"] = "private-my-channel"
                            authorizer.setHeaders(parameters)
                            pusher!!.unsubscribe(channelname)

                            channel = pusher!!.subscribePrivate("private-my-channel")

                            channel.bind("client-send-message", object : PrivateChannelEventListener {
                                override fun onAuthenticationFailure(
                                    message: String,
                                    e: java.lang.Exception
                                ) {
                                }

                                override fun onSubscriptionSucceeded(channelName: String) {

                                }

                                override fun onEvent(event: PusherEvent) {

                                    Log.i("Pusher", "Received event with data: $event")
                                    Log.v("coneccteddriverdata", "" + event.data)

                                    if (activity != null && isAdded()) {

                                        requireActivity().runOnUiThread {

                                            if (NetworkUtility.isNetworkAvailable(requireActivity())) {
                                                callChatAPI(chatAdapter, true)
                                            }
                                            else{

                                                Toast.makeText(
                                                    requireActivity(),
                                                    getString(R.string.msg_no_internet),
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }


                                        }
                                    }


                                }
                            })

                        }
                    }


                }

                override fun onError(message: String?, code: String?, e: java.lang.Exception?) {

                    Log.i(
                        "Pusher",
                        "There was a problem connecting! code ($code), message ($message), exception($e)"
                    )
                }

            }, ConnectionState.ALL)


        }



        iv_timerview!!.setOnClickListener {

            dummylayout!!.visibility = View.VISIBLE

        }

        dummylayout!!.setOnClickListener {

            dummylayout!!.visibility = View.GONE

        }

        imageView22!!.setOnClickListener(View.OnClickListener {

            AppUtilities.hideSoftKeyboard(requireActivity())
            includeheaderpart!!.visibility = View.VISIBLE
            relativeChatBottomsheet!!.visibility = View.GONE
            includebottomsheet_arraivepickup_location!!.visibility = View.GONE
            bottomSheetBehaviorChat.peekHeight = resources.getDimension(R.dimen._1sdp).toInt()
            bottomSheetBehaviorChat.state = BottomSheetBehavior.STATE_HIDDEN
        })



        ivMsgSend!!.setOnClickListener {

            if (editTextMsgSend!!.text.toString().equals("")) {

                if (activity != null && isAdded()) {

                    Toast.makeText(requireActivity(), "Please send a message", Toast.LENGTH_SHORT).show()
                }

            } else {

                var reqmsgsend = HashMap<String, Any>()
                val gson = Gson()


                if (chatClickOn=="contactCustomer" && loadUnload == false){

                    reqmsgsend["sender_id"] = driverId
                    reqmsgsend["receiver_id"] = customerId


                }
                else  if (chatClickOn=="contactCustomer" && loadUnload == true){

                    reqmsgsend["sender_id"] = driverId
                    reqmsgsend["receiver_id"] = customerId

                }
                else  if (chatClickOn=="contactSecondaryAssist" && loadUnload == true){

                    reqmsgsend["sender_id"] = driverId
                    reqmsgsend["receiver_id"] = second_driver_id


                }
                else if (chatClickOn=="contactPrimary" && loadUnload == true){

                    reqmsgsend["sender_id"] = second_driver_id
                    reqmsgsend["receiver_id"] = driverId



                }

              /*  reqmsgsend["sender_id"] = driverId
                reqmsgsend["receiver_id"] = customerId*/

                reqmsgsend["message"] = editTextMsgSend!!.text.trim().toString()
                reqmsgsend["service_id"] = service_id
                reqmsgsend["flag"] = "driver"

                val finalmsgsend = gson.toJson(reqmsgsend)
                Log.v("finalmsgsend", "finalmsgsend" + finalmsgsend)
                try {
                    if (channel!=null) {
                        channel.trigger("client-send-message", finalmsgsend)
                    }

                } catch (e: Exception) {


                }

                editTextMsgSend!!.setText("")

                if (NetworkUtility.isNetworkAvailable(requireActivity())) {

                    callChatAPI(chatAdapter, true)
                }
                else{

                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.msg_no_internet),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }

        tv_emergencycancel!!.setOnClickListener {

            emergencyview!!.visibility = View.VISIBLE

        }

        tv_emergencycancel1!!.setOnClickListener {

            emergencyview!!.visibility = View.VISIBLE

        }

        tv_yes_emergency!!.setOnClickListener {

            if (!service_id.equals("")) {

                apicallForEmergencyCall(service_id, service_type)
            }

        }

        tv_no_emergency!!.setOnClickListener {

            emergencyview!!.visibility = View.GONE

        }

        iv_userimg!!.setOnClickListener {

            callcustomercare!!.visibility = View.VISIBLE
        }

        toCall!!.setOnClickListener {

            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + "+1 548-883-6883")
            startActivity(dialIntent)
        }

        tv_transferCall!!.setOnClickListener {

            view_pouseservice!!.visibility = View.VISIBLE
            iv_downarrow!!.visibility = View.GONE

            //   tv_pouseservice!!.visibility = View.VISIBLE
            pauseservice!!.visibility = View.VISIBLE

            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + "+1 548-883-6883")
            startActivity(dialIntent)
        }

        vehicle_image!!.setOnClickListener {

            if (!vehicleImage.equals("")) {

                val nagDialog = Dialog(
                    requireActivity(),
                    android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
                )
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                nagDialog.setCancelable(false)
                nagDialog.setContentView(R.layout.preview_image)
                val btnClose = nagDialog.findViewById<View>(R.id.btnIvClose) as Button
                val ivPreview = nagDialog.findViewById<View>(R.id.iv_preview_image) as ImageView

                Glide.with(requireActivity())
                    .load(NetworkUtility.imgbaseUrl + vehicleImage)/*.placeholder(R.drawable.defaultperson)*/
                    .error(R.drawable.defaultper)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(ivPreview!!)

                btnClose.setOnClickListener {
                    nagDialog.dismiss()
                }

                nagDialog.show()

                         }
                    }

        return view
    }


/*    fun GetDeviceipWiFiData(): String? {

        val wm =requireActivity().getSystemService(WIFI_SERVICE) as WifiManager?
        return android.text.format.Formatter.formatIpAddress(wm!!.connectionInfo.ipAddress);

    }*/


    fun GetDeviceipMobileData(): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return null
    }

    fun callresumeAPIforReassignServiceCall(
        serviceId: String,
        customerId: String,
        driverId: String,
        pickPlace: String,
        dropPlace: String,
        vehicleType: String
    ) {

        try {
            if (activity != null && isAdded()) {

                AppProgressBar.openLoader(
                    requireActivity(),
                    requireActivity().getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                if (defaultIdLog!!.equals("")) {

                    req["driver_id"] = defaultIdReg
                    req["service_id"] = serviceId

                    if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                        service_type = ""
                    }
                    else{
                        service_type = "secondary"
                    }

                    req["service_type"] = service_type!!

                    responseServicecallPause = apiservice.doingservicecallpause(tokenReg, req)
                } else {

                    req["driver_id"] = defaultIdLog
                    req["service_id"] = serviceId

                    if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                        service_type = ""
                    }
                    else{
                        service_type = "secondary"
                    }

                    req["service_type"] = service_type!!

                    responseServicecallPause = apiservice.doingservicecallpause(token, req)

                }

                responseServicecallPause!!.enqueue(object : Callback<VerifyOtpPojo?> {
                    override fun onResponse(
                        call: Call<VerifyOtpPojo?>,
                        response1: Response<VerifyOtpPojo?>
                    ) {
                        if (response1.isSuccessful()) {
                            btn_startDrivingreassignment!!.resetSlider()
                            AppProgressBar.closeLoader()
                            reassign!!.visibility = View.GONE

/*                            val req = HashMap<String, Any>()
                            val gson = Gson()
                            req["id"] = serviceId!!
                            req["customer_id"] = customerId!!

                            val final = gson.toJson(req)

                            try {

                                if (channel == null) {

                                    channel =
                                        pusher!!.subscribePrivate("private-my-channel")
                                }

                                channel.trigger("client-reassignment", final)


                            } catch (e: Exception) {

                                e.printStackTrace()
                            }*/


                            //after start driving
                            isStartDriving = false
                            isArrivingAtDestination = true

                            showvalueafteraccept(
                                serviceId!!,
                                this@LandingFragment.customerId!!,
                                this@LandingFragment.driverId!!,
                                this@LandingFragment.pickPlace!!,
                                this@LandingFragment.dropPlace!!,
                                this@LandingFragment.vehicleType,
                                service_type,
                                customer_image
                            )


                            constraintLayoutForDropOff!!.visibility = View.GONE
                            constraintLayout!!.visibility = View.GONE
                            constraintLayoutForArraived!!.visibility = View.VISIBLE
                            includebottomsheet_arraivepickup_location!!.visibility = View.GONE
                            iv_menusymbol!!.visibility = View.VISIBLE
                            iv_cancelservicecall!!.visibility = View.GONE
                            imageView36!!.visibility = View.GONE
                            textView54!!.visibility = View.GONE
                            tv_dropofflocation_!!.setText(this@LandingFragment.dropPlace)

                            //after resume

                            view_pouseservice!!.visibility = View.VISIBLE
                            iv_downarrow!!.visibility = View.VISIBLE
                            includeheaderpart!!.visibility = View.VISIBLE
                            pauseservice!!.visibility = View.GONE
                            mHandlerForPause?.removeCallbacksAndMessages(null)

                            if (!service_type.equals("secondary")) {

                                driver_updatestatus = "DrivingToDestination"

                                //  isArrivingAtDestination = true
                                // service start after start driving

                                mLocationService = LocationService()
                                mServiceIntent = Intent(requireActivity(), mLocationService.javaClass)

                                if (!Util.isMyServiceRunning(mLocationService.javaClass, requireActivity())) {

                                    mServiceIntent.putExtra("id", serviceId)
                                    mServiceIntent.putExtra("status", driver_updatestatus)
                                    requireActivity().startService(mServiceIntent)

                                   // Toast.makeText(requireActivity(), getString(R.string.service_start_successfully), Toast.LENGTH_SHORT).show()

                                } else {
                                   // Toast.makeText(requireActivity(), getString(R.string.service_already_running), Toast.LENGTH_SHORT).show()
                                }

                            }


                        } else {

                            AppProgressBar.closeLoader()

                            if (response1.body() != null) {
                                btn_startDrivingreassignment!!.resetSlider()

                                if (response1.body()!!.message.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                        ignoreCase = true
                                    )
                                ) {

                                    val sharedPreferences =
                                        requireActivity().getSharedPreferences(
                                            "MySharedPref",
                                            Context.MODE_PRIVATE
                                        )
                                    val editor = sharedPreferences.edit()
                                    sharedPreferenceManager!!.logoutUser()
                                    editor.clear()
                                    editor.commit()


                                    val myIntent =
                                        Intent(
                                            requireActivity(),
                                            WelcomeActivity::class.java
                                        )
                                    startActivity(myIntent)

                                    requireActivity().finish()

                                } else {


                                    Toast.makeText(requireActivity(), response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()


                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                        AppProgressBar.closeLoader()
                        btn_startDrivingreassignment!!.resetSlider()

                        if (t is NoConnectivityException) {
                            Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                        } else {

                            Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun apicallForEmergencyCall(service_id: String, service_type: String) {

        try {
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    this.getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {

                    req["service_id"] = service_id!!
                    req["driver_id"] = defaultIdReg
                    responseEmaregencycancel = apiservice.doingemergency_cancelled(tokenReg, req)
                } else {

                    req["service_id"] = service_id!!
                    req["driver_id"] = defaultIdLog

                    responseEmaregencycancel = apiservice.doingemergency_cancelled(token, req)

                }

                responseEmaregencycancel!!.enqueue(object : Callback<Emargencycancel?> {
                    override fun onResponse(
                        call: Call<Emargencycancel?>,
                        response1: Response<Emargencycancel?>
                    ) {
                        if (response1.isSuccessful()) {

                            AppProgressBar.closeLoader()

                            if (response1.body()!!.status == true) {


                                emergencyview!!.visibility = View.GONE

                                try {
                                    if (response1.body()!!.detail!! != null) {
                                        afteremargencynewservice_id =
                                            response1.body()!!.detail!!.newservice_id!!
                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }

                                var authorizer = HttpAuthorizer(NetworkUtility.authUrl)
                                var options = PusherOptions().setAuthorizer(authorizer)

                                pusher = Pusher(NetworkUtility.key, options)

                                pusher!!.connect(object : ConnectionEventListener {
                                    override fun onConnectionStateChange(change: ConnectionStateChange) {
                                        Log.i(
                                            "Pusher",
                                            "State changed from ${change.previousState} to ${change.currentState}"
                                        )

                                        if (change.currentState.toString()
                                                .equals("CONNECTED", ignoreCase = true)
                                        ) {
                                            val socketId = pusher!!.connection.socketId
                                            if (socketId != null) {
                                                val channelname = "private-my-channel"
                                                Log.v("socketId", "socketId" + socketId)
                                                val parameters = HashMap<String, String>()
                                                parameters["socket_id"] = socketId
                                                parameters["channel_name"] = "private-my-channel"
                                                authorizer.setHeaders(parameters)
                                                pusher!!.unsubscribe(channelname)

                                                channel =
                                                    pusher!!.subscribePrivate("private-my-channel")
                                            }
                                        }


                                    }

                                    override fun onError(
                                        message: String?,
                                        code: String?,
                                        e: java.lang.Exception?
                                    ) {

                                        Log.i(
                                            "Pusher",
                                            "There was a problem connecting! code ($code), message ($message), exception($e)"
                                        )
                                    }

                                }, ConnectionState.ALL)

                                var service_type1:String = ""

                                if (channel!! != null) {

                                    if (service_type.equals("")) {

                                        booleanservice_type = false
                                    } else if (service_type.equals("secondary")) {
                                        booleanservice_type = true

                                    }
                                    // driver_updatestatus = "Emargency Cancel"
                                    driver_updatestatus = "Emergency_Cancelled"

                                    val req = HashMap<String, Any>()
                                    val gson = Gson()
                                    req["id"] = service_id
                                    req["driver_id"] =
                                        sharedPreferences!!.getString("idSharedPrefAfterLog", "")!!
                                    req["driver_long"] = c_long!!
                                    req["driver_lat"] = c_lat!!
                                    req["status"] = driver_updatestatus!!
                                    req["new_service_id"] = afteremargencynewservice_id!!

                                    if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                                        service_type1 = ""
                                    }
                                    else{
                                        service_type1 = "secondary"
                                    }

                                    req["service_type"] = service_type1!!

                                    val final = gson.toJson(req)
                                    channel.trigger("client-order-progress-status", final)

                                    Log.v("emergencycancelsuccess", "" + final)

                                    try {

                                        val apiservice: ApiInterface =
                                            ApiClient.getClient(requireActivity())
                                                .create(ApiInterface::class.java)
                                        val req = HashMap<String, Any>()


                                        if (defaultIdLog.equals("")) {

                                            req["id"] = defaultIdReg
                                            req["driver_status"] = "Logged out"

                                            responseDriverStatusSend =
                                                apiservice.doingdriver_status_send(tokenReg, req)
                                        } else {

                                            req["id"] = defaultIdLog
                                            req["driver_status"] = "Logged out"
                                            responseDriverStatusSend =
                                                apiservice.doingdriver_status_send(token, req)

                                        }

                                        responseDriverStatusSend!!.enqueue(object :
                                            Callback<Driverstatusstoreresponse?> {
                                            override fun onResponse(
                                                call: Call<Driverstatusstoreresponse?>,
                                                response1: Response<Driverstatusstoreresponse?>
                                            ) {
                                                if (response1.isSuccessful()) {

                                                    if (response1.body()?.status == true) {
                                                        AppProgressBar.closeLoader()

                                                        val sharedPreferences =
                                                            requireActivity().getSharedPreferences(
                                                                "MySharedPref",
                                                                Context.MODE_PRIVATE
                                                            )
                                                        val editor = sharedPreferences.edit()
                                                        sharedPreferenceManager!!.logoutUser()
                                                        editor.clear()
                                                        editor.commit()

                                                        if (activity != null && isAdded()) {
                                                            val myIntent =
                                                                Intent(
                                                                    requireActivity(),
                                                                    WelcomeActivity::class.java
                                                                )
                                                            startActivity(myIntent)

                                                            requireActivity().finish()
                                                        }

                                                    } else {

                                                        AppProgressBar.closeLoader()

                                                        if (response1.body() != null) {

                                                            if (response1.body()!!.message.equals(
                                                                    resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                                                    ignoreCase = true
                                                                )
                                                            ) {

                                                                val sharedPreferences =
                                                                    requireActivity().getSharedPreferences(
                                                                        "MySharedPref",
                                                                        Context.MODE_PRIVATE
                                                                    )
                                                                val editor =
                                                                    sharedPreferences.edit()
                                                                sharedPreferenceManager!!.logoutUser()
                                                                editor.clear()
                                                                editor.commit()

                                                                if (activity != null && isAdded()) {
                                                                    val myIntent =
                                                                        Intent(
                                                                            requireActivity(),
                                                                            WelcomeActivity::class.java
                                                                        )
                                                                    requireActivity().startActivity(
                                                                        myIntent
                                                                    )

                                                                    requireActivity().finish()

                                                                } else {


                                                                    Toast.makeText(requireActivity(), response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()


                                                                }
                                                            }

                                                        }
                                                    }
                                                } else {

                                                    AppProgressBar.closeLoader()

                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<Driverstatusstoreresponse?>,
                                                t: Throwable
                                            ) {
                                                AppProgressBar.closeLoader()

                                                if (activity != null && isAdded()) {

                                                    if (t is NoConnectivityException) {
                                                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                                                    } else {

                                                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        })
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                }

                                /*}catch (e:Exception){

                            }*/

                            } else {

                                AppProgressBar.closeLoader()

                                if (response1.body() != null) {

                                    if (response1.body()!!.message.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                            ignoreCase = true
                                        )
                                    ) {

                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()

                                        if (activity != null && isAdded()) {
                                            val myIntent =
                                                Intent(
                                                    requireActivity(),
                                                    WelcomeActivity::class.java
                                                )
                                            startActivity(myIntent)

                                            requireActivity().finish()

                                        } else {


                                            Toast.makeText(requireActivity(), response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()


                                        }
                                    }

                                }
                            }


                        } else {

                            AppProgressBar.closeLoader()
                            if (response1.body() != null) {
                                if (activity != null && isAdded()) {
                                    Toast.makeText(requireActivity(), response1.body()!!.message.toString(), Toast.LENGTH_LONG).show()

                                }
                            }

                        }
                    }

                    override fun onFailure(call: Call<Emargencycancel?>, t: Throwable) {
                        AppProgressBar.closeLoader()

                        if (activity != null && isAdded()) {

                            if (t is NoConnectivityException) {
                                Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                            } else {

                                Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun callServiceCallAPIandAccept() {

        try {

            val activity: Activity? = activity
            if (activity != null && isAdded()) {

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)


                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {

                    req["driver_id"] = defaultIdReg
                    response2 = apiservice.doingservicecall_accept(tokenReg, req)
                } else {

                    req["driver_id"] = defaultIdLog
                    response2 = apiservice.doingservicecall_accept(token, req)

                }

                response2!!.enqueue(object : Callback<ServicecallResponse?> {
                    override fun onResponse(
                        call: Call<ServicecallResponse?>,
                        response1: Response<ServicecallResponse?>
                    ) {
                        if (response1.isSuccessful()) {

                            if (response1.body()!!.status == true) {


                                // service_id = response1.body()!!.detail!!.serviceId!!.toString().trim()
                                service_idstore =
                                    response1.body()!!.detail!!.serviceId!!.toString().trim()
                                Log.v("service_idstore", ":" + service_idstore)

                                customerId =
                                    response1.body()!!.detail!!.customerId!!.toString().trim()
                                driverId = response1.body()!!.detail!!.driverId!!.toString().trim()
                                pickPlace =
                                    response1.body()!!.detail!!.pickPlace!!.toString().trim()
                                dropPlace =
                                    response1.body()!!.detail!!.dropPlace!!.toString().trim()
                                vehicleType =
                                    response1.body()!!.detail!!.vehicleType!!.toString().trim()
                                loadunloadafterAccptInServiceCall =
                                    response1.body()!!.detail!!.loadUnload!!

                                service_type = response1.body()!!.detail!!.service_type!!.toString().trim()

                                if (service_type.toString().trim()
                                        .equals("secondary", ignoreCase = true)
                                ) {


                                    Log.v("service_type", ":" + service_type)
                                    tv_servicename!!.setText("Secondary Service")
                                } else {

                                    service_type = ""
                                    tv_servicename!!.setText("Primary Service")
                                }
                                const_servicecallaccept!!.visibility = View.GONE

                                if (!service_type.equals("")) {
                                    headerPrimaryCall!!.setText("Seconddary Service Call")
                                } else {
                                    headerPrimaryCall!!.setText("Primary Service Call")
                                }


                                object : CountDownTimer(20000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {

                                        //here you can have your logic to set text to edittext
                                    }

                                    override fun onFinish() {

                                        const_servicecallaccept!!.visibility = View.GONE

                                    }
                                }.start()

                                getAfterAcceptServiceCallAPI(/*service_id*/ service_idstore,
                                    customerId,
                                    driverId,
                                    pickPlace,
                                    dropPlace,
                                    vehicleType,
                                    service_type,
                                    loadunloadafterAccptInServiceCall!!
                                )

                            } else {

                                AppProgressBar.closeLoader()
                                const_servicecallaccept!!.visibility = View.GONE

                                if (response1.body() != null) {

                                    if (response1.body()!!.detail!!.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice)
                                        )
                                    ) {

                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()


                                        val myIntent =
                                            Intent(
                                                requireActivity(),
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        requireActivity().finish()

                                    } else {


                                        Toast.makeText(requireActivity(), response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()


                                    }

                                }
                            }


                        } else {

                            AppProgressBar.closeLoader()

                            if (response1.body() != null) {

                                Toast.makeText(requireActivity(), response1.body()!!.message.toString(), Toast.LENGTH_LONG).show()


                            }
                        }
                    }

                    override fun onFailure(call: Call<ServicecallResponse?>, t: Throwable) {
                        AppProgressBar.closeLoader()

                        val activity: Activity? = activity

                        if (activity != null && isAdded()) {

                            if (t is NoConnectivityException) {

                                Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                            } else {

                                Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT ).show()
                            }
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity: Activity? = activity
        if (activity != null && isAdded()) {
            mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
        }

    }

    private fun setupRecyleview() {


        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        chatRecycle!!.layoutManager = layoutManager

        val data = ArrayList<IndividualChatResponse>()


        chatRecycle!!.adapter = chatAdapter

        if (NetworkUtility.isNetworkAvailable(requireActivity())) {
            callChatAPI(chatAdapter)
        }
        else{

            Toast.makeText(
                requireActivity(),
                getString(R.string.msg_no_internet),
                Toast.LENGTH_LONG
            ).show()

        }


    }


    fun callChatAPI(chatAdapter: ChatAdapter, ismsgSend: Boolean = false) {


        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {

                Log.e("Service",tv_servicename!!.text.toString().trim())
                AppProgressBar.openLoader(
                    requireActivity(),
                    this.getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClientForPusher.getClientforpusher().create(ApiInterface::class.java)
                // val apiservice: ApiInterface = ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()

                //Primary Service call chat with primary driver with customer
                if (chatClickOn=="contactCustomer" && loadUnload == false){

                    if (defaultIdLog.equals("")) {

                        req["sender_id"] = driverId
                        req["receiver_id"] = customerId
                        req["service_id"] = service_id
                        responseCahtList = apiservice.doingchat_messaging(tokenReg, req)
                    }
                    else {

                        req["sender_id"] = driverId
                        req["receiver_id"] = customerId
                        req["service_id"] = service_id

                        responseCahtList = apiservice.doingchat_messaging(token, req)

                    }
                    Log.e("CallFrom","PDC-without load")
                    Log.e("RequestDat",req.toString())

                }
                else  if (chatClickOn=="contactCustomer" && loadUnload == true){

                    if (defaultIdLog.equals("")) {

                        req["sender_id"] = driverId
                        req["receiver_id"] = customerId
                        req["service_id"] = service_id
                        responseCahtList = apiservice.doingchat_messaging(tokenReg, req)
                    }
                    else {

                        req["sender_id"] = driverId
                        req["receiver_id"] = customerId
                        req["service_id"] = service_id

                        responseCahtList = apiservice.doingchat_messaging(token, req)

                    }
                    Log.e("CallFrom","PDC-with load")
                    Log.e("RequestDat",req.toString())

                }
                else  if (chatClickOn=="contactSecondaryAssist" && loadUnload == true){

                    if (defaultIdLog.equals("")) {

                        req["sender_id"] = driverId
                        req["receiver_id"] = second_driver_id
                        req["service_id"] = service_id
                        responseCahtList = apiservice.doingchat_messaging(tokenReg, req)
                    }
                    else {

                        req["sender_id"] = driverId
                        req["receiver_id"] = second_driver_id
                        req["service_id"] = service_id

                        responseCahtList = apiservice.doingchat_messaging(token, req)

                    }
                    Log.e("CallFrom","P-S")
                    Log.e("RequestDat",req.toString())

                }
                else if (chatClickOn=="contactPrimary" && loadUnload == true){

                    if (defaultIdLog.equals("")) {

                        req["sender_id"] = second_driver_id
                        req["receiver_id"] = driverId
                        req["service_id"] = service_id
                        responseCahtList = apiservice.doingchat_messaging(tokenReg, req)
                    }
                    else {

                        req["sender_id"] = second_driver_id
                        req["receiver_id"] = driverId
                        req["service_id"] = service_id

                        responseCahtList = apiservice.doingchat_messaging(token, req)

                    }
                    Log.e("CallFrom","S-P")
                    Log.e("RequestDat",req.toString())

                }
                /*else{
                //Secondary service call with secondary driver & primary driver

                    if (defaultIdLog.equals("")) {

                        req["sender_id"] = *//*driverId*//* second_driver_id
                        req["receiver_id"] = driverId
                        req["service_id"] = service_id
                        responseCahtList = apiservice.doingchat_messaging(tokenReg, req)
                    }
                    else {

                        req["sender_id"] = second_driver_id
                        req["receiver_id"] = *//*customerId*//* driverId
                        req["service_id"] = service_id

                        responseCahtList = apiservice.doingchat_messaging(token, req)

                    }

                    Log.e("CallFrom","PDSD")
                    Log.e("RequestDat",req.toString())
                }*/


                responseCahtList!!.enqueue(object : Callback<ChatListResponse?> {
                    override fun onResponse(
                        call: Call<ChatListResponse?>,
                        response1: Response<ChatListResponse?>
                    ) {
                        if (response1.isSuccessful()) {

                            AppProgressBar.closeLoader()

                            if (response1.body()!!.data != null && response1.body()!!.data!!.size > 0) {

                                chatListArr = response1.body()!!.data

                                if (!chatListArr!!.isNullOrEmpty()) {
                                    chatRecycle!!.visibility = View.VISIBLE

                                    if (ismsgSend)
                                        chatAdapter.setItemsInserted(chatListArr!!)
                                    else
                                        chatAdapter.setItems(chatListArr!!)


                                } else {

                                    chatRecycle!!.visibility = View.GONE
                                }

                                if (chatListArr != null && chatListArr!!.size > 0) {

                                    chatRecycle!!.smoothScrollToPosition(
                                        chatListArr!!.size - 1
                                    )
                                }


                            } else {
                                chatRecycle!!.visibility = View.GONE


                            }


                        } else {

                            AppProgressBar.closeLoader()

                        }
                    }

                    override fun onFailure(call: Call<ChatListResponse?>, t: Throwable) {
                        AppProgressBar.closeLoader()

                        val activity: Activity? = activity

                        if (activity != null && isAdded()) {
                            if ( t is NoConnectivityException ) {
                                Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                            } else {

                                Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                            }

                        }

                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLoggedOut() {


        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    this.getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {

                    req["id"] = defaultIdReg
                    req["driver_status"] = "Logged out"

                    responseDriverStatusSend = apiservice.doingdriver_status_send(tokenReg, req)
                } else {

                    req["id"] = defaultIdLog
                    req["driver_status"] = "Logged out"
                    responseDriverStatusSend = apiservice.doingdriver_status_send(token, req)

                }

                responseDriverStatusSend!!.enqueue(object : Callback<Driverstatusstoreresponse?> {
                    override fun onResponse(
                        call: Call<Driverstatusstoreresponse?>,
                        response1: Response<Driverstatusstoreresponse?>
                    ) {
                        if (response1.isSuccessful()) {

                            AppProgressBar.closeLoader()
                            val activity: Activity? = activity
                            if (activity != null && isAdded()) {
                                val sharedPreferences = requireActivity().getSharedPreferences(
                                    "MySharedPref",
                                    Context.MODE_PRIVATE
                                )
                                val editor = sharedPreferences.edit()
                                sharedPreferenceManager!!.logoutUser()
                                editor.clear()
                                editor.commit()


                                val myIntent = Intent(requireContext(), WelcomeActivity::class.java)
                                startActivity(myIntent)
                                requireActivity().finish()
                            }

                        } else {

                            AppProgressBar.closeLoader()

                            if (response1.body() != null) {

                                if (response1.body()!!.message.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                        ignoreCase = true
                                    )
                                ) {
                                    val activity: Activity? = activity
                                    if (activity != null && isAdded()) {
                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()


                                        val myIntent =
                                            Intent(
                                                requireActivity(),
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        requireActivity().finish()
                                    }

                                } else {

                                    val activity: Activity? = activity
                                    if (activity != null && isAdded()) {
                                        Toast.makeText(requireActivity(), response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()
                                    }


                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<Driverstatusstoreresponse?>, t: Throwable) {
                        AppProgressBar.closeLoader()
                        val activity: Activity? = activity
                        if (activity != null && isAdded()) {
                            if ( t is NoConnectivityException) {
                                Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                            } else {

                                Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun driverStatusUpdateAPICal(
        defaultIdLog: String,
        defaultIdReg: String,
        driverStatus: String
    ) {

        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    this.getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {

                    req["id"] = defaultIdReg
                    req["driver_status"] = driverStatus

                    responseDriverStatusSend = apiservice.doingdriver_status_send(tokenReg, req)
                } else {

                    req["id"] = defaultIdLog
                    req["driver_status"] = driverStatus
                    responseDriverStatusSend = apiservice.doingdriver_status_send(token, req)

                }

                responseDriverStatusSend!!.enqueue(object : Callback<Driverstatusstoreresponse?> {
                    override fun onResponse(
                        call: Call<Driverstatusstoreresponse?>,
                        response1: Response<Driverstatusstoreresponse?>
                    ) {
                        if (response1.isSuccessful()) {

                            AppProgressBar.closeLoader()

                        } else {

                            AppProgressBar.closeLoader()

                            if (response1.body() != null) {

                                if (response1.body()!!.message.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                        ignoreCase = true
                                    )
                                ) {

                                    val sharedPreferences = requireActivity().getSharedPreferences(
                                        "MySharedPref",
                                        Context.MODE_PRIVATE
                                    )
                                    val editor = sharedPreferences.edit()
                                    sharedPreferenceManager!!.logoutUser()
                                    editor.clear()
                                    editor.commit()


                                    val myIntent =
                                        Intent(
                                            requireActivity(),
                                            WelcomeActivity::class.java
                                        )
                                    startActivity(myIntent)

                                    requireActivity().finish()

                                } else {


                                    Toast.makeText(requireActivity(), response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()


                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<Driverstatusstoreresponse?>, t: Throwable) {
                        AppProgressBar.closeLoader()
                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            availability = "Offline"
                            onoffcall = false
                            driver_status = "Offline"
                            iv_toggle!!.setImageResource(R.drawable.offlineimg)
                            //  driverStatusUpdateAPICal(defaultIdLog ,defaultIdReg , driver_status!!)

                        } else {

                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun callPauseServiceCallAPI(storeserviceid: String) {

        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    this.getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {

                    req["driver_id"] = defaultIdReg
                    req["service_id"] = storeserviceid

                    if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                        service_type = ""
                    }
                    else{
                        service_type = "secondary"
                    }

                    req["service_type"] = service_type!!

                    responseServicecallPause = apiservice.doingservicecallpause(tokenReg, req)
                } else {

                    req["driver_id"] = defaultIdLog
                    req["service_id"] = storeserviceid

                    if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                        service_type = ""
                    }
                    else{
                        service_type = "secondary"
                    }

                    req["service_type"] = service_type!!

                    responseServicecallPause = apiservice.doingservicecallpause(token, req)

                }

                responseServicecallPause!!.enqueue(object : Callback<VerifyOtpPojo?> {
                    @SuppressLint("LongLogTag")
                    override fun onResponse(
                        call: Call<VerifyOtpPojo?>,
                        response1: Response<VerifyOtpPojo?>
                    ) {
                        if (response1.isSuccessful()) {

                            AppProgressBar.closeLoader()

                            stopServiceFunc()

                            timeInSeconds_pause = 0L
                            timeInSeconds_pauseNew = 0L

                            view_pouseservice!!.visibility = View.GONE
                            iv_downarrow!!.visibility = View.GONE
                            //     tv_pouseservice!!.visibility = View.GONE
                            pauseservice!!.visibility = View.VISIBLE
                            driver_updatestatus =""
                            driver_updatestatus = /*"Paused"*/ "DrivingToDestination"

                            val req = HashMap<String, Any>()
                            val gson = Gson()
                            req["id"] = storeserviceid!!
                            req["driver_long"] = c_long!!
                            req["driver_lat"] = c_lat!!
                            req["status"] = driver_updatestatus!!

                            val final = gson.toJson(req)

                            try {
                                channel.trigger("client-order-progress-status", final)

                            } catch (e: Exception) {

                            }
                            Log.v("clientorderprogressstatus", "clientorderprogressstatus" + final)


                            startTimerOnPauseClick()


                        } else {

                            AppProgressBar.closeLoader()

                        }
                    }

                    override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                        AppProgressBar.closeLoader()
                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun normalwiseServiceCallCancel(id: String) {

        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    this.getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {

                    req["service_id"] = id
                    req["type"] = "normal"

                    if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                        service_type = ""
                    }
                    else{
                        service_type = "secondary"
                    }

                    req["service_type"] = service_type

                    responseServicecallcancel = apiservice.doingservicecallcancelled(tokenReg, req)

                } else {

                    req["service_id"] = id
                    req["type"] = "normal"

                    if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                        service_type = ""
                    }
                    else{
                        service_type = "secondary"
                    }

                    req["service_type"] = service_type

                    responseServicecallcancel = apiservice.doingservicecallcancelled(token, req)

                }

                responseServicecallcancel!!.enqueue(object : Callback<CancelResponse?> {
                    override fun onResponse(
                        call: Call<CancelResponse?>,
                        response1: Response<CancelResponse?>
                    ) {
                        if (response1.body() == null) {
                            AppProgressBar.closeLoader()
                            return
                        } else if (response1.body() != null) {

                            AppProgressBar.closeLoader()

                            if (response1.body()!!.status == true) {

                                driver_updatestatus = "Cancelled"

                                val req = HashMap<String, Any>()
                                val gson = Gson()
                                req["id"] = id!!
                                req["driver_long"] = c_long!!
                                req["driver_lat"] = c_lat!!
                                req["status"] = driver_updatestatus!!

                                val final = gson.toJson(req)
                                try {
                                    channel.trigger("client-order-progress-status", final)

                                } catch (e: Exception) {

                                }
                                Log.v("clientorderprogresttus", "clientorderprogressstatus" + final)


                                val myIntent =
                                    Intent(requireActivity(), AfterCancelServiceScreen::class.java)
                                myIntent.putExtra("service_id", id)
                                myIntent.putExtra(
                                    "cancelcharges",
                                    response1.body()!!.detail!!.cancelCharges!!.toDouble()
                                )
                                startActivity(myIntent)
                            } else {

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {

                            AppProgressBar.closeLoader()

                            if (response1.body() != null && response1.body()?.message != null) {

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {

                                return
                            }

                        }
                    }

                    override fun onFailure(call: Call<CancelResponse?>, t: Throwable) {
                        AppProgressBar.closeLoader()
                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callArrivedToDest(
        serviceId: String,
        customerId: String,
        driverId: String,
        pickPlace: String,
        dropPlace: String,
        vehicleType: String,
        service_type: String,
    ) {
        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    this.getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()

                var service_type1:String=""

                if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                    service_type1 = ""
                }
                else{
                    service_type1 = "secondary"
                }

                if (defaultIdLog.equals("")) {

                    req["service_id"] = serviceId!!

                    req["service_type"] = service_type1!!
                    responseArraivedToDest = apiservice.doingarriveddestination(tokenReg, req)
                } else {

                    req["service_id"] = serviceId!!

                    req["service_type"] = service_type1!!
                    responseArraivedToDest = apiservice.doingarriveddestination(token, req)

                }

                responseArraivedToDest!!.enqueue(object : Callback<VerifyOtpPojo?> {
                    override fun onResponse(
                        call: Call<VerifyOtpPojo?>,
                        response1: Response<VerifyOtpPojo?>
                    ) {
                        if (response1.isSuccessful()) {

                            btn_arrivedToDest!!.resetSlider()
                            AppProgressBar.closeLoader()

                            if (response1.body()!!.status == true) {
                                btn_arrivedToDest!!.resetSlider()

                                reassign!!.visibility =View.GONE
                                stopServiceFunc()
                                view_pouseservice!!.visibility = View.GONE

                                if (service_type1.equals("secondary")) {


                                    pleasewaitsecondarycall!!.visibility = View.VISIBLE

                                    //pusher = Pusher("d5041f2ec10120e451ce", options)
                                    pusher = Pusher(NetworkUtility.key, options)

                                    pusher!!.connect(object : ConnectionEventListener {
                                        override fun onConnectionStateChange(change: ConnectionStateChange) {
                                            Log.i(
                                                "Pusher",
                                                "State changed from ${change.previousState} to ${change.currentState}"
                                            )

                                            if (change.currentState.toString()
                                                    .equals("CONNECTED", ignoreCase = true)
                                            ) {
                                                val socketId = pusher!!.connection.socketId
                                                if (socketId != null){
                                                    val channelname = "private-my-channel"
                                                    Log.v("socketId", "socketId" + socketId)
                                                    val parameters = HashMap<String, String>()
                                                    parameters["socket_id"] = socketId
                                                    parameters["channel_name"] = "private-my-channel"
                                                    authorizer.setHeaders(parameters)
                                                    pusher!!.unsubscribe(channelname)

                                                    channel =
                                                        pusher!!.subscribePrivate("private-my-channel")

                                                    channel.bind(
                                                        "client-order-progress-status",
                                                        object : PrivateChannelEventListener {
                                                            override fun onAuthenticationFailure(
                                                                message: String,
                                                                e: java.lang.Exception
                                                            ) {


                                                            }

                                                            override fun onSubscriptionSucceeded(
                                                                channelName: String
                                                            ) {

                                                            }

                                                            override fun onEvent(event: PusherEvent) {
                                                                Log.i(
                                                                    "Pusher",
                                                                    "Received event with data: $event"
                                                                )
                                                                Log.v(
                                                                    "coneccteddriverdata",
                                                                    "" + event.data
                                                                )

                                                                if (activity != null && isAdded()) {

                                                                    requireActivity().runOnUiThread {

                                                                        jsonObjectDriver =
                                                                            JSONObject(event.data)

                                                                        driver_updatestatusbind =
                                                                            jsonObjectDriver!!.getString(
                                                                                "status"
                                                                            )

                                                                        if (driver_updatestatusbind!!.equals(
                                                                                "Completed"
                                                                            )
                                                                        ) {

                                                                            if (jsonObjectDriver!!.has("id") && jsonObjectDriver!!.getString(
                                                                                    "id"
                                                                                ) != null
                                                                            ) {

                                                                                service_idbind =
                                                                                    jsonObjectDriver!!.getString(
                                                                                        "id"
                                                                                    )

                                                                                if (serviceId.equals(
                                                                                        service_idbind
                                                                                    )
                                                                                ) {

                                                                                    if (activity != null && isAdded()) {

                                                                                        requireActivity().runOnUiThread {

                                                                                            pleasewaitsecondarycall!!.visibility =
                                                                                                View.GONE

                                                                                            callsecondaryServiceCallComplete(
                                                                                                service_id
                                                                                            )


                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }


                                                            }

                                                        })

                                                }
                                            }


                                        }

                                        override fun onError(
                                            message: String?,
                                            code: String?,
                                            e: java.lang.Exception?
                                        ) {

                                            Log.i(
                                                "Pusher",
                                                "There was a problem connecting! code ($code), message ($message), exception($e)"
                                            )
                                        }

                                    }, ConnectionState.ALL)


                                } else {

                                    driver_updatestatus = "ArrivedAtDestination"

                                    val req = HashMap<String, Any>()
                                    val gson = Gson()
                                    req["id"] = serviceId
                                    req["driver_long"] = c_long!!
                                    req["driver_lat"] = c_lat!!
                                    req["status"] = driver_updatestatus!!

                                    val final = gson.toJson(req)
                                    try {
                                        channel.trigger("client-order-progress-status", final)

                                    } catch (e: Exception) {

                                    }
                                    Log.v(
                                        "clientorderprogresstus",
                                        "clientorderprogressstatus" + final
                                    )

                                    showvalueafteraccept(
                                        serviceId!!,
                                        customerId!!,
                                        driverId!!,
                                        pickPlace!!,
                                        dropPlace!!,
                                        vehicleType,
                                        /*service_type*/ service_type1,
                                        customer_image
                                    )

                                    constraintLayoutForArraived!!.visibility = View.GONE
                                    includebottomsheet_arraivepickup_location!!.visibility = View.GONE
                                    constraintLayoutUnloadingInprogress!!.visibility = View.VISIBLE
                                    iv_menusymbol!!.visibility = View.VISIBLE

                                    if (/*service_type*/ service_type1!!.equals("")) {


                                        unloadingInProgressStartTimer()


                                    } else {

                                        pleasewaitsecondarycall!!.visibility = View.VISIBLE


                                    }
                                }

                            } else {

                                AppProgressBar.closeLoader()

                                if (response1.body() != null) {
                                    btn_arrivedToDest!!.resetSlider()

                                    if (response1.body()!!.message.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                            ignoreCase = true
                                        )
                                    ) {

                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()


                                        val myIntent =
                                            Intent(
                                                requireActivity(),
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        requireActivity().finish()

                                    } else {


                                        Toast.makeText(
                                            requireActivity(),
                                            response1.body()!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    }

                                }
                            }


                        } else {

                            AppProgressBar.closeLoader()
                            if (response1.body() != null) {
                                btn_arrivedToDest!!.resetSlider()

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                            }

                        }
                    }

                    override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                        btn_arrivedToDest!!.resetSlider()

                        AppProgressBar.closeLoader()
                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun callsecondaryServiceCallComplete(service_id: String) {

        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {
                    req["service_id"] = service_id
                    responseSecondaryService =
                        apiservice.doingsecondservicecallcomplete(tokenReg!!, req)
                } else {
                    req["service_id"] = service_id

                    responseSecondaryService = apiservice.doingsecondservicecallcomplete(token, req)

                }

                responseSecondaryService!!.enqueue(object : Callback<CompleteSecondaryService?> {
                    override fun onResponse(
                        call: Call<CompleteSecondaryService?>,
                        response1: Response<CompleteSecondaryService?>
                    ) {
                        if (response1.isSuccessful() && response1.body() != null) {

                            AppProgressBar.closeLoader()


                            if (response1.body()!!.status == true) {


                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                                price = response1.body()!!.detail!!.price.toString()

                                val myIntent = Intent(
                                    requireActivity(),
                                    AfterCompleteSecondaryServiceScreen::class.java
                                )
                                myIntent.putExtra("service_id", service_id)
                                myIntent.putExtra("price", price!!.toDouble())
                                requireActivity().startActivity(myIntent)


                            } else {

                                AppProgressBar.closeLoader()

                            }


                        } else {

                            AppProgressBar.closeLoader()

                            if (response1.body() != null) {

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()


                            }
                        }
                    }

                    override fun onFailure(call: Call<CompleteSecondaryService?>, t: Throwable) {
                        AppProgressBar.closeLoader()
                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun servicecallcompleteAPIcalling(
        serviceId: String,
        customerId: String,
        driverId: String,
        pickPlace: String,
        dropPlace: String,
        vehicleType: String,
        customer_image: String
    ) {

        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    this.getResources().getString(R.string.pleasewait)
                )


                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {

                    req["service_id"] = serviceId!!
                    req["drop_image"] = storeImage

                    if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                        service_type = ""
                    }
                    else{
                        service_type = "secondary"
                    }

                    req["service_type"] = service_type

                    responsedoingservicecallcomplete =
                        apiservice.doingservicecallcomplete(tokenReg, req)

                } else {

                    req["service_id"] = serviceId!!
                    req["drop_image"] = storeImage

                    if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                        service_type = ""
                    }
                    else{
                        service_type = "secondary"
                    }

                    req["service_type"] = service_type
                    // req["driver_id"] = defaultIdLog
                    responsedoingservicecallcomplete =
                        apiservice.doingservicecallcomplete(token, req)

                }

                responsedoingservicecallcomplete!!.enqueue(object :
                    Callback<ServiceCallCompleteResponse?> {
                    override fun onResponse(
                        call: Call<ServiceCallCompleteResponse?>,
                        response1: Response<ServiceCallCompleteResponse?>
                    ) {
                        if (response1.isSuccessful()) {

                            AppProgressBar.closeLoader()

                            if (response1.body()!!.status == true) {

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                                reassign!!.visibility =View.GONE

                                driver_updatestatus = "Completed"

                                val req = HashMap<String, Any>()
                                val gson = Gson()
                                req["id"] = serviceId
                                req["driver_long"] = c_long!!
                                req["driver_lat"] = c_lat!!
                                req["status"] = driver_updatestatus!!


                                val final = gson.toJson(req)
                                try {
                                    channel.trigger("client-order-progress-status", final)

                                } catch (e: Exception) {


                                }
                                Log.v("clientorderprogresttus", "clientorderprogressstatus" + final)

                                constraintLayoutUploadedSuccessfully!!.visibility = View.GONE
                                includebottomsheet_arraivepickup_location!!.visibility = View.GONE
                                driverRating!!.visibility = View.VISIBLE

                                //stopServiceFunc()

                                Log.v(
                                    "price",
                                    "price" + response1.body()!!.detail!!.price.toString()
                                )
                                tv_totalprice!!.setText("$ " + response1.body()!!.detail!!.price.toString())
                                tv_serviceid!!.setText("Service Request #KIT- " + serviceId + "\nCompleted and Closed. You have\nearned: " + tv_totalprice!!.text.toString()+" (inclusive of all taxes)")
                                val activity: Activity? = activity
                                if (activity != null && isAdded()) {
                                    pd_loader1!!.visibility ==View.VISIBLE
                                    Glide.with(requireActivity())
                                        .load(NetworkUtility.imgbaseUrl + customer_image)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true).into(img_profile!!)
                                    pd_loader1!!.visibility ==View.GONE

                                }

                            } else {

                                AppProgressBar.closeLoader()

                                if (response1.body() != null) {

                                    if (response1.body()!!.message.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                            ignoreCase = true
                                        )
                                    ) {

                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()


                                        val myIntent =
                                            Intent(
                                                requireActivity(),
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        requireActivity().finish()

                                    } else {


                                        Toast.makeText(
                                            requireActivity(),
                                            response1.body()!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    }

                                }
                            }


                        } else {

                            AppProgressBar.closeLoader()

                            if (response1.body() != null) {

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                            }

                        }
                    }

                    override fun onFailure(call: Call<ServiceCallCompleteResponse?>, t: Throwable) {
                        AppProgressBar.closeLoader()
                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getRatingApiCalling(
        serviceId: String,
        customerId: String,
        driverId: String,
        pickPlace: String,
        dropPlace: String,
        vehicleType: String
    ) {

        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    this.getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()

                if (defaultIdLog.equals("")) {

                    req["service_id"] = serviceId!!
                    req["rating"] = ratingbar!!.rating.toString()
                    req["message"] = txt_additional_comments!!.text.toString()

                    responseGiveRatingFromDriverSiteToCustomer =
                        apiservice.doingrating(tokenReg, req)
                } else {

                    req["service_id"] = serviceId!!
                    req["rating"] = ratingbar!!.rating.toString()
                    req["message"] = txt_additional_comments!!.text.toString()

                    responseGiveRatingFromDriverSiteToCustomer = apiservice.doingrating(token, req)

                }



                responseGiveRatingFromDriverSiteToCustomer!!.enqueue(object :
                    Callback<VerifyOtpPojo?> {
                    override fun onResponse(
                        call: Call<VerifyOtpPojo?>,
                        response1: Response<VerifyOtpPojo?>
                    ) {
                        if (response1.isSuccessful()) {

                            AppProgressBar.closeLoader()

                            if (response1.body()!!.status == true) {

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                                mMap!!.clear()

                                isAccepted = false
                                isNearBy = false
                                isStartDriving = false
                                isArrivingAtDestination = false
                                pick_latFromList = 0.0

                                if (mPolyline != null)
                                    mPolyline!!.remove()
                                mCurrLocationMarker!!.remove()


                                val myIntent = Intent(requireActivity(), HomeActivity::class.java)
                                startActivity(myIntent)

                            } else {

                                AppProgressBar.closeLoader()

                                if (response1.body() != null) {

                                    if (response1.body()!!.message.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                            ignoreCase = true
                                        )
                                    ) {

                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()


                                        val myIntent =
                                            Intent(
                                                requireActivity(),
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        requireActivity().finish()

                                    } else {


                                        Toast.makeText(
                                            requireActivity(),
                                            response1.body()!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    }

                                }
                            }


                        } else {

                            AppProgressBar.closeLoader()
                            if (response1.body() != null) {


                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }


                        }
                    }

                    override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                        AppProgressBar.closeLoader()
                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun callStartDriving(
        serviceId: String,
        customerId: String,
        driverId: String,
        pickPlace: String,
        dropPlace: String,
        vehicleType: String
    ) {

        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    this.getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {

                    req["service_id"] = serviceId!!

                    if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                        service_type = ""
                    }
                    else{
                        service_type = "secondary"
                    }

                    req["service_type"] = service_type!!

                    responsestartdriving = apiservice.doingservicecallstartdriving(tokenReg, req)
                } else {
                    req["service_id"] = serviceId!!

                    if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                        service_type = ""
                    }
                    else{
                        service_type = "secondary"
                    }

                    req["service_type"] = service_type!!

                    responsestartdriving = apiservice.doingservicecallstartdriving(token, req)

                }

                responsestartdriving!!.enqueue(object : Callback<VerifyOtpPojo?> {
                    override fun onResponse(
                        call: Call<VerifyOtpPojo?>,
                        response1: Response<VerifyOtpPojo?>
                    ) {
                        if (response1.isSuccessful()) {
                            btn_startDriving!!.resetSlider()
                            AppProgressBar.closeLoader()

                            if (response1.body()!!.status == true) {


                                isStartDriving = false
                                isArrivingAtDestination = true

                                showvalueafteraccept(
                                    serviceId!!,
                                    customerId!!,
                                    driverId!!,
                                    pickPlace!!,
                                    dropPlace!!,
                                    vehicleType,
                                    service_type,
                                    customer_image
                                )


                                constraintLayoutForDropOff!!.visibility = View.GONE
                                constraintLayout!!.visibility = View.GONE
                                constraintLayoutForArraived!!.visibility = View.VISIBLE
                                view_pouseservice!!.visibility = View.VISIBLE
                                includebottomsheet_arraivepickup_location!!.visibility = View.GONE
                                iv_menusymbol!!.visibility = View.VISIBLE
                                iv_cancelservicecall!!.visibility = View.GONE
                                imageView36!!.visibility = View.GONE
                                textView54!!.visibility = View.GONE
                                tv_dropofflocation_!!.setText(dropPlace)


                                if (!service_type.equals("secondary")) {

                                    driver_updatestatus = "DrivingToDestination"

                                    //  isArrivingAtDestination = true
                                    // service start after start driving

                                    mLocationService = LocationService()
                                    mServiceIntent = Intent(requireActivity(), mLocationService.javaClass)

                                    if (!Util.isMyServiceRunning(mLocationService.javaClass, requireActivity())) {

                                        mServiceIntent.putExtra("id", serviceId)
                                        mServiceIntent.putExtra("status", driver_updatestatus)
                                        requireActivity().startService(mServiceIntent)

                                    } else {

                                    }

                                    // trigger after start driving

/*                               val req = HashMap<String, Any>()
                                    val gson = Gson()
                                    req["id"] = serviceId
                                    req["driver_long"] = c_long!!
                                    req["driver_lat"] = c_lat!!
                                    req["status"] = driver_updatestatus!!

                                    val final = gson.toJson(req)
                                    try {

                                        if (channel == null) {

                                            channel =
                                                pusher!!.subscribePrivate("private-my-channel")
                                        }

                                        channel.trigger("client-order-progress-status", final)


                                    } catch (e: Exception) {

                                        e.printStackTrace()
                                    }
                                    Log.v(
                                        "clientorderprogresttus",
                                        "clientorderprogressstatus" + final
                                    )*/

                                }
                            } else {

                                AppProgressBar.closeLoader()

                                if (response1.body() != null) {

                                    if (response1.body()!!.message.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                            ignoreCase = true
                                        )
                                    ) {

                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()


                                        val myIntent =
                                            Intent(
                                                requireActivity(),
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        requireActivity().finish()

                                    } else {


                                        Toast.makeText(
                                            requireActivity(),
                                            response1.body()!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    }

                                }
                            }


                        } else {

                            AppProgressBar.closeLoader()
                            if (response1.body() != null) {

                                btn_startDriving!!.resetSlider()

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                            }

                        }
                    }

                    override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                        btn_startDriving!!.resetSlider()
                        AppProgressBar.closeLoader()
                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun callVerifyOTP(
        serviceId: String,
        customerId: String,
        driverId: String,
        pickPlace: String,
        dropPlace: String,
        vehicleType: String
    ) {

        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    this.getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()

                req["service_id"] = serviceId!!

                if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                    service_type = ""
                }
                else{
                    service_type = "secondary"
                }

                req["service_type"] = service_type!!

                if (defaultIdLog.equals("")) {
                    req["code"] =
                        edt_password1!!.text.toString() + edt_password2!!.text.toString() + edt_password3!!.text.toString() + edt_password4!!.text.toString()

                    responseVerifyCode = apiservice.doingverifycode(tokenReg, req)
                } else {
                    req["code"] =
                        edt_password1!!.text.toString() + edt_password2!!.text.toString() + edt_password3!!.text.toString() + edt_password4!!.text.toString()

                    responseVerifyCode = apiservice.doingverifycode(token, req)

                }

                responseVerifyCode!!.enqueue(object : Callback<VerifyOtpPojo?> {
                    override fun onResponse(
                        call: Call<VerifyOtpPojo?>,
                        response1: Response<VerifyOtpPojo?>
                    ) {
                        if (response1.isSuccessful()) {

                            AppProgressBar.closeLoader()

                            if (response1.body()!!.status == true) {

                                if (service_type.equals("secondary")) {

                                    constraintLayoutForDropOff!!.visibility = VISIBLE
                                    constraintLayoutloadingInprogress!!.visibility = View.GONE
                                    includebottomsheet_arraivepickup_location!!.visibility = View.GONE
                                    iv_cancelservicecall!!.visibility = View.GONE
                                    imageView36!!.visibility = View.GONE
                                    textView54!!.visibility = View.GONE
                                    verifyviewInclude!!.visibility = View.GONE
                                    verifyviewInclude!!.visibility = View.GONE
                                    includebottomsheet_cancelservicecall!!.visibility = View.GONE
                                    constraintLayout!!.visibility = View.GONE
                                    constraintLayoutForArraived!!.visibility = View.GONE
                                    iv_menusymbol!!.visibility = View.VISIBLE

                                    showvalueafteraccept(
                                        serviceId!!,
                                        customerId!!,
                                        driverId!!,
                                        pickPlace!!,
                                        dropPlace!!,
                                        vehicleType!!,
                                        service_type!!,
                                        customer_image
                                    )
                                    tv_dropofflocation!!.setText(dropPlace)
                                    isStartDriving = true

                                } else {

                                    mHandler?.removeCallbacksAndMessages(null)


                                    verifyviewInclude!!.visibility = View.GONE
                                    includebottomsheet_cancelservicecall!!.visibility = View.GONE
                                    constraintLayout!!.visibility = View.GONE
                                    iv_menusymbol!!.visibility = View.VISIBLE

                                    constraintLayoutloadingInprogress!!.visibility = View.VISIBLE


                                    driver_updatestatus = "CustomerCodeEntered"

                                    val req = HashMap<String, Any>()
                                    val gson = Gson()
                                    req["id"] = serviceId
                                    req["driver_long"] = c_long!!
                                    req["driver_lat"] = c_lat!!
                                    req["status"] = driver_updatestatus!!
                                    val final = gson.toJson(req)
                                    try {
                                        channel.trigger("client-order-progress-status", final)

                                    } catch (e: Exception) {

                                    }
                                    Log.v(
                                        "clientorderprogresttus",
                                        "clientorderprogressstatus" + final
                                    )


                                    showvalueafteraccept(
                                        serviceId!!,
                                        customerId!!,
                                        driverId!!,
                                        pickPlace!!,
                                        dropPlace!!,
                                        vehicleType!!,
                                        service_type!!,
                                        customer_image
                                    )


                                    loadingInProgressStartTimer()

                                    constraintLayoutForArraived!!.visibility = View.GONE
                                }

                                // Loading in progress part


                            } else {

                                AppProgressBar.closeLoader()

                                edt_password1!!.setText("")
                                edt_password2!!.setText("")
                                edt_password3!!.setText("")
                                edt_password4!!.setText("")

                                if (response1.body() != null) {

                                    if (response1.body()!!.message.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                            ignoreCase = true
                                        )
                                    ) {

                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()


                                        val myIntent =
                                            Intent(
                                                requireActivity(),
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        requireActivity().finish()

                                    } else {


                                        Toast.makeText(
                                            requireActivity(),
                                            response1.body()!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    }

                                }
                            }


                        } else {

                            AppProgressBar.closeLoader()

                            if (response1.body() != null) {

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }


                        }
                    }

                    override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                        AppProgressBar.closeLoader()
                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun onPayInDocUpload() {

        val activity: Activity? = activity
        if (activity != null && isAdded()) {

            Dexter.withActivity(requireActivity())
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {

                            driver_updatestatus = "ArrivedAtDestination"

                            val req = HashMap<String, Any>()
                            val gson = Gson()
                            req["id"] = service_idstore
                            req["driver_long"] = c_long!!
                            req["driver_lat"] = c_lat!!
                            req["status"] = driver_updatestatus!!

                            val final = gson.toJson(req)
                            try {
                                channel.trigger("client-order-progress-status", final)

                            } catch (e: Exception) {

                            }

                            showImagePickerOptions()
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {

                            driver_updatestatus = "ArrivedAtDestination"

                            val req = HashMap<String, Any>()
                            val gson = Gson()
                            req["id"] = service_idstore
                            req["driver_long"] = c_long!!
                            req["driver_lat"] = c_lat!!
                            req["status"] = driver_updatestatus!!

                            val final = gson.toJson(req)
                            try {
                                channel.trigger("client-order-progress-status", final)

                            } catch (e: Exception) {

                            }

                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }
    }

     fun showImagePickerOptions() {

        driver_updatestatus = "ArrivedAtDestination"

        val req = HashMap<String, Any>()
        val gson = Gson()
        req["id"] = service_idstore
        req["driver_long"] = c_long!!
        req["driver_lat"] = c_lat!!
        req["status"] = driver_updatestatus!!

        val final = gson.toJson(req)
        try {
            channel.trigger("client-order-progress-status", final)

        } catch (e: Exception) {

        }

        val activity: Activity? = activity
        if (activity != null && isAdded()) {

            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_proof_attachment_camera)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val tvTakePicture = dialog.findViewById<TextView>(R.id.tvTakePicture)
            // TextView tvChooseGallery = dialog.findViewById(R.id.tvChooseGallery);
            //  TextView tvChooseDocument = dialog.findViewById(R.id.tvChooseDocument);
            // TextView tvChooseGallery = dialog.findViewById(R.id.tvChooseGallery);
            //  TextView tvChooseDocument = dialog.findViewById(R.id.tvChooseDocument);
            val IvDialogClose = dialog.findViewById<ImageView>(R.id.IvDialogClose)

            IvDialogClose.setOnClickListener { dialog.dismiss() }

            tvTakePicture.setOnClickListener {

                driver_updatestatus = "ArrivedAtDestination"

                val req = HashMap<String, Any>()
                val gson = Gson()
                req["id"] = service_idstore
                req["driver_long"] = c_long!!
                req["driver_lat"] = c_lat!!
                req["status"] = driver_updatestatus!!

                val final = gson.toJson(req)
                try {
                    channel.trigger("client-order-progress-status", final)

                } catch (e: Exception) {

                }

                ImagePicker.with(this)
                    .cameraOnly()
                    .cropSquare() //Crop image(Optional), Check Customization for more option
                    .compress(1024) //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    ) //Final image resolution will be less than 1080 x 1080(Optional)
                    .start(0)

                // listener.onTakeCameraSelected()
                dialog.dismiss()
            }

            /* ImagePickerActivity.showImagePickerOptionsCamera(
                requireActivity(),
                object : ImagePickerActivity.PickerOptionListener {
                    override fun onTakeCameraSelected() {
                        launchCameraIntent()
                    }

                    override fun onChooseGallerySelected() {
                        launchGalleryIntent()
                    }

                    override fun onDocumentSelected() {
                        // launchDocumentIntent()
                    }
                })*/
        }
    }

    private fun launchCameraIntent() {
        val activity: Activity? = activity
        if (activity != null && isAdded()) {
            val intent = Intent(requireActivity(), ImagePickerActivity::class.java)
            intent.putExtra(
                ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
                ImagePickerActivity.REQUEST_IMAGE_CAPTURE
            )

            // setting aspect ratio
            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

            // setting maximum bitmap width and height
            intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
            intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
            intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
            startActivityForResult(
                intent, REQUEST_IMAGE
            )
        }
    }

    private fun launchGalleryIntent() {
        val activity: Activity? = activity
        if (activity != null && isAdded()) {
            val intent = Intent(requireActivity(), ImagePickerActivity::class.java)

            intent.putExtra(
                ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
                ImagePickerActivity.REQUEST_GALLERY_IMAGE
            )

            // setting aspect ratio

            // setting aspect ratio
            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2

            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
            startActivityForResult(
                intent,
                REQUEST_IMAGE
            )

        }
    }

    fun showSettingsDialog() {

        driver_updatestatus = "ArrivedAtDestination"

        val req = HashMap<String, Any>()
        val gson = Gson()
        req["id"] = service_idstore
        req["driver_long"] = c_long!!
        req["driver_lat"] = c_lat!!
        req["status"] = driver_updatestatus!!

        val final = gson.toJson(req)
        try {
            channel.trigger("client-order-progress-status", final)

        } catch (e: Exception) {

        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.dialog_permission_title))
        builder.setMessage(getString(R.string.dialog_permission_message))
        builder.setPositiveButton(
            getString(R.string.go_to_settings)
        ) { dialog, which ->
            dialog.cancel()
            this.openSettings()
        }
        builder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings() {

        val activity: Activity? = activity
        if (activity != null && isAdded()) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri

            driver_updatestatus = "ArrivedAtDestination"

            val req = HashMap<String, Any>()
            val gson = Gson()
            req["id"] = service_idstore
            req["driver_long"] = c_long!!
            req["driver_lat"] = c_lat!!
            req["status"] = driver_updatestatus!!

            val final = gson.toJson(req)
            try {
                channel.trigger("client-order-progress-status", final)

            } catch (e: Exception) {

            }

            startActivityForResult(intent, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                //  val uri = data!!.getParcelableExtra<Uri>("path")

                driver_updatestatus = "ArrivedAtDestination"

                val req = HashMap<String, Any>()
                val gson = Gson()
                req["id"] = service_idstore
                req["driver_long"] = c_long!!
                req["driver_lat"] = c_lat!!
                req["status"] = driver_updatestatus!!

                val final = gson.toJson(req)
                try {
                    channel.trigger("client-order-progress-status", final)

                } catch (e: Exception) {

                }

                val uri = data?.data
                try {
                    // You can update this bitmap to your server
                    /* val bitmap =
                         MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)*/

                    ImageUrl = uri


                    val activity: Activity? = activity
                    if (activity != null && isAdded()) {
                        val imageStream: InputStream? =
                            requireActivity().contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage = encodeImage(selectedImage)!!

                        Log.v("image64", "" + encodedImage)
                        pd_loader!!.visibility = View.VISIBLE



                        loadProfile(uri.toString())

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == REQUEST_CHECK_SETTING) {

            when (resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(requireContext(), "GPS is Turned on", Toast.LENGTH_SHORT)
                        .show()
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(
                        requireContext(),
                        "GPS is Required to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    fun loadProfile(url: String) {
        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )
        pd_loader!!.visibility = View.VISIBLE

        driver_updatestatus = "ArrivedAtDestination"

        val req = HashMap<String, Any>()
        val gson = Gson()
        req["id"] = service_idstore
        req["driver_long"] = c_long!!
        req["driver_lat"] = c_lat!!
        req["status"] = driver_updatestatus!!

        val final = gson.toJson(req)
        try {
            channel.trigger("client-order-progress-status", final)

        } catch (e: Exception) {

        }
        imageuploadAPICallBack(url)

    }

    private fun imageuploadAPICallBack(url: String) {

        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {

                driver_updatestatus = "ArrivedAtDestination"

                val req1 = HashMap<String, Any>()
                val gson = Gson()
                req1["id"] = service_idstore
                req1["driver_long"] = c_long!!
                req1["driver_lat"] = c_lat!!
                req1["status"] = driver_updatestatus!!

                val final = gson.toJson(req1)
                try {
                    channel.trigger("client-order-progress-status", final)

                } catch (e: Exception) {

                }


                pd_loader!!.visibility = View.VISIBLE


                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {
                    req["driver_id"] = defaultIdReg
                    req["image"] = "data:image/png;base64," + encodedImage
                    responseImageUpload = apiservice.doingImageUpload(tokenReg, req)

                } else {
                    req["driver_id"] = defaultIdLog
                    req["image"] = "data:image/png;base64," + encodedImage
                    responseImageUpload = apiservice.doingImageUpload(token, req)

                }

                responseImageUpload!!.enqueue(object : Callback<ImageUploadResponse?> {
                    override fun onResponse(
                        call: Call<ImageUploadResponse?>,
                        response: Response<ImageUploadResponse?>
                    ) {
                        if (response.isSuccessful()) {

                            driver_updatestatus = "ArrivedAtDestination"

                            val req = HashMap<String, Any>()
                            val gson = Gson()
                            req["id"] = service_idstore
                            req["driver_long"] = c_long!!
                            req["driver_lat"] = c_lat!!
                            req["status"] = driver_updatestatus!!

                            val final = gson.toJson(req)
                            try {
                                channel.trigger("client-order-progress-status", final)

                            } catch (e: Exception) {

                            }

                            pd_loader!!.visibility = View.VISIBLE
                            reassign!!.visibility = View.GONE




                            if (response.body()!!.status == true) {

                                Toast.makeText(
                                    requireActivity(),
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                                pd_loader!!.visibility = View.GONE

                                driver_updatestatus = "ArrivedAtDestination"

                                val req1 = HashMap<String, Any>()
                                val gson1 = Gson()
                                req1["id"] = service_idstore
                                req1["driver_long"] = c_long!!
                                req1["driver_lat"] = c_lat!!
                                req1["status"] = driver_updatestatus!!

                                val final1 = gson1.toJson(req1)
                                try {
                                    channel.trigger("client-order-progress-status", final1)

                                } catch (e: Exception) {

                                }

                                Log.v("clientorderprogresttus", "clientorderprogressstatus" + final)

                                pd_loader!!.isVisible = false

                                Glide.with(requireActivity()).load(url).into(view_takepicture!!)


                                driver_updatestatus = "ArrivedAtDestination"

                                val req = HashMap<String, Any>()
                                val gson = Gson()
                                req["id"] = service_id
                                req["driver_long"] = c_long!!
                                req["driver_lat"] = c_lat!!
                                req["status"] = driver_updatestatus!!


                                val final = gson.toJson(req)
                                try {
                                    channel.trigger("client-order-progress-status", final)

                                } catch (e: Exception) {

                                }

                                storeImage = response.body()!!.detail!!.images.toString()


                            } else {
                                pd_loader!!.isVisible = false
                                Toast.makeText(
                                    requireActivity(),
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }


                        } else {

                            //  AppProgressBar.closeLoader()

                            pd_loader!!.isVisible = false
                            Toast.makeText(
                                requireActivity(),
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                        }
                    }

                    override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                        //  AppProgressBar.closeLoader()
                        pd_loader!!.isVisible = false

                        val activity: Activity? = activity
                        if (activity != null && isAdded()) {

                            if (t is NoConnectivityException) {
                                Toast.makeText(
                                    requireActivity(),
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                Toast.makeText(
                                    requireActivity(),
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun customerGreetStartTimer() {
        // mHandler = Handler(Looper.getMainLooper())
        mStatusChecker.run()
    }

    private fun customerGreetStartTimerNew() {
        //  mHandlerNew = Handler(Looper.getMainLooper())
        mStatusCheckerNew.run()
    }


    private fun loadingInProgressStartTimer() {
        //   mHandlerinProgress = Handler(Looper.getMainLooper())
        mStatusCheckerInLoading.run()
    }

    private fun loadingInProgressStartTimerNew() {
        // mHandlerinProgressNew = Handler(Looper.getMainLooper())
        mStatusCheckerInLoadingNew.run()
    }


    private fun unloadingInProgressStartTimer() {
        // mHandlerunloading = Handler(Looper.getMainLooper())
        mStatusCheckerunLoading.run()
    }

    private fun unloadingInProgressStartTimerNew() {
        //  mHandlerunloadingNew = Handler(Looper.getMainLooper())
        mStatusCheckerunLoadingNew.run()
    }


    private fun startTimerOnPauseClick() {

        //  mHandlerForPause = Handler(Looper.getMainLooper())
        mStatusChecker_Pause.run()
    }

    private fun startTimerOnPauseClickNew() {

        //  mHandlerForPauseNew = Handler(Looper.getMainLooper())
        mStatusChecker_PauseNew.run()
    }


    private var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                timeInSeconds += 1
                Log.e("timeInSeconds", timeInSeconds.toString())
                updateStopWatchView(timeInSeconds)
            } finally {
                mHandler!!.postDelayed(this, 1000)
            }
        }
    }

    private var mStatusCheckerNew: Runnable = object : Runnable {
        override fun run() {
            try {
                timeInSecondsNew += 1
                Log.e("timeInSeconds", timeInSecondsNew.toString())
                updateStopWatchView(timeInSecondsNew)
            } finally {
                mHandlerNew!!.postDelayed(this, 1000)
            }
        }
    }


    private var mStatusCheckerInLoading: Runnable = object : Runnable {
        override fun run() {
            try {
                timeInSeconds_inLoading += 1
                Log.e("timeInSeconds", timeInSeconds_inLoading.toString())
                updateStopWatchViewForInLoading(timeInSeconds_inLoading)
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandlerinProgress!!.postDelayed(this, 1000)
            }
        }
    }

    private var mStatusCheckerInLoadingNew: Runnable = object : Runnable {
        override fun run() {
            try {
                timeInSeconds_inLoadingNew += 1
                Log.e("timeInSeconds", timeInSeconds_inLoadingNew.toString())
                updateStopWatchViewForInLoading(timeInSeconds_inLoadingNew)
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandlerinProgressNew!!.postDelayed(this, 1000)
            }
        }
    }


    private var mStatusCheckerunLoading: Runnable = object : Runnable {
        override fun run() {
            try {
                timeInSeconds_unloadingin += 1
                Log.e("timeInSeconds", timeInSeconds_unloadingin.toString())
                updateStopWatchViewForunLoading(timeInSeconds_unloadingin)
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandlerunloading!!.postDelayed(this, 1000)
            }
        }
    }

    private var mStatusCheckerunLoadingNew: Runnable = object : Runnable {
        override fun run() {
            try {
                timeInSeconds_unloadinginNew += 1
                Log.e("timeInSeconds", timeInSeconds_unloadinginNew.toString())
                updateStopWatchViewForunLoading(timeInSeconds_unloadinginNew)
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandlerunloadingNew!!.postDelayed(this, 1000)
            }
        }
    }


    private var mStatusChecker_Pause: Runnable = object : Runnable {
        override fun run() {
            try {
                timeInSeconds_pause += 1
                Log.e("timeInSeconds", timeInSeconds_pause.toString())
                updateStopWatchViewForPause(timeInSeconds_pause)
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandlerForPause!!.postDelayed(this, 1000)
            }
        }
    }

    private var mStatusChecker_PauseNew: Runnable = object : Runnable {
        override fun run() {
            try {
                timeInSeconds_pauseNew += 1
                //   Log.e("timeInSeconds", timeInSeconds_pauseNew.toString())
                Log.e("timeInSeconds", timeInSeconds_pauseNew.toString())
                //  updateStopWatchViewForPause(timeInSeconds_pauseNew)
                updateStopWatchViewForPause(timeInSeconds_pauseNew)
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandlerForPauseNew!!.postDelayed(this, 1000)
            }
        }
    }


    private fun updateStopWatchView(timeInSeconds: Long) {

        val formattedTime = getFormattedStopWatch((timeInSeconds * 1000))
        Log.e("formattedTime", formattedTime)
        tv_timer?.text = formattedTime

        // if (!customer_waiting_time.equals("")) {
        if (timeInSeconds >= 300L /*customer_waiting_time!!.toLong()*/) {

            iv_cancelServicecall!!.visibility = View.INVISIBLE
            cross_black!!.visibility = View.INVISIBLE

            iv_cancelServicecallblack!!.visibility = View.VISIBLE
            cross_white!!.visibility = View.VISIBLE

        } else {

            iv_cancelServicecall!!.visibility = View.VISIBLE
            cross_black!!.visibility = View.VISIBLE

            iv_cancelServicecallblack!!.visibility = View.INVISIBLE
            cross_white!!.visibility = View.INVISIBLE


        }
        //  }

    }

    private fun updateStopWatchViewForunLoading(timeInSeconds: Long) {
        val formattedTime = getFormattedStopWatch((timeInSeconds * 1000))
        Log.e("formattedTime", formattedTime)
        tv_timerUnloadingInprogress?.text = formattedTime
    }

    private fun updateStopWatchViewForInLoading(timeInSeconds: Long) {
        val formattedTime = getFormattedStopWatch((timeInSeconds * 1000))
        Log.e("formattedTime", formattedTime)
        tv_timerloadingInprogress?.text = formattedTime
        // binding?.textViewStopWatch?.text = formattedTime
    }

    private fun updateStopWatchViewForPause(timeInSeconds: Long) {
        val formattedTime = getFormattedStopWatch((timeInSeconds * 1000))
        Log.e("formattedTime", formattedTime)
        tv_pauseTimerCount?.text = formattedTime
    }

    fun getFormattedStopWatch(ms: Long): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds"
    }

    private fun callServiceCallAPI() {

        try {

            val activity: Activity? = activity
            if (activity != null && isAdded()) {

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)


                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {

                    req["driver_id"] = defaultIdReg
                    response2 = apiservice.doingservicecall_accept(tokenReg, req)
                } else {

                    req["driver_id"] = defaultIdLog
                    response2 = apiservice.doingservicecall_accept(token, req)

                }

                response2!!.enqueue(object : Callback<ServicecallResponse?> {
                    override fun onResponse(
                        call: Call<ServicecallResponse?>,
                        response1: Response<ServicecallResponse?>
                    ) {
                        if (response1.isSuccessful()) {

                            if (response1.body()!!.status == true) {


                                // service_id = response1.body()!!.detail!!.serviceId!!.toString().trim()
                                service_idstore =
                                    response1.body()!!.detail!!.serviceId!!.toString().trim()
                                customerId =
                                    response1.body()!!.detail!!.customerId!!.toString().trim()
                                driverId = response1.body()!!.detail!!.driverId!!.toString().trim()
                                pickPlace =
                                    response1.body()!!.detail!!.pickPlace!!.toString().trim()
                                dropPlace =
                                    response1.body()!!.detail!!.dropPlace!!.toString().trim()
                                vehicleType =
                                    response1.body()!!.detail!!.vehicleType!!.toString().trim()
                                loadunloadafterAccptInServiceCall =
                                    response1.body()!!.detail!!.loadUnload!!

                                service_type = response1.body()!!.detail!!.service_type!!.toString().trim()

                                getLastLocation(service_type)

                                if (service_type.equals("secondary", ignoreCase = true))
                                {
                                    Log.v("service_type", ":" + service_type)
                                    Toast.makeText(requireActivity(), ""+service_type, Toast.LENGTH_SHORT)
                                        .show()
                                    tv_servicename!!.setText("Secondary Service")
                                } else {

                                    service_type = ""
                                    tv_servicename!!.setText("Primary Service")
                                    Toast.makeText(requireActivity(), ""+service_type, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                const_servicecallaccept!!.visibility = View.VISIBLE

                                ratingService!!.setText(
                                    response1.body()!!.detail!!.rating!!.toString().trim()
                                )
                                tv_dest!!.setText(
                                    response1.body()!!.detail!!.dropPlace!!.toString().trim()
                                )
                                tv_source!!.setText(
                                    response1.body()!!.detail!!.pickPlace!!.toString().trim()
                                )
                                distanceServiceCallKm!!.setText(
                                    response1.body()!!.detail!!.totalDistance!!.toString().trim()+"KM"
                                )

                                distanceCalculateinkm(
                                    c_lat!!,
                                    c_long!!,
                                    response1.body()!!.detail!!.pickLat!!.toDouble(),
                                    response1.body()!!.detail!!.pickLong!!.toDouble()
                                )

                                val startPoint = Location("locationA")
                                startPoint.latitude = c_lat!!
                                startPoint.longitude = c_long!!

                                val endPoint = Location("locationB")
                                endPoint.latitude = response1.body()!!.detail!!.pickLat!!.toDouble()
                                endPoint.longitude =
                                    response1.body()!!.detail!!.pickLong!!.toDouble()

                                val distance = startPoint.distanceTo(endPoint).toDouble()

                                Log.v("distncep", "" + DecimalFormat("0.00").format(distance))

                                if (distance >= 1000) {
                                    distanceServiceCall!!.setText(
                                        DecimalFormat("0.00").format(
                                            distance / 1000
                                        ) + " KM away"
                                    )
                                } else {

                                    distanceServiceCall!!.setText(
                                        DecimalFormat("0.00").format(distance) + " Meters away"
                                    )

                                }

                                //  distanceServiceCall!!.setText(distance.toString()+"Km")

                                if (!service_type.equals("")) {
                                    headerPrimaryCall!!.setText("Seconddary Service Call")
                                } else {
                                    headerPrimaryCall!!.setText("Primary Service Call")
                                }


                                object : CountDownTimer(20000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {

                                        //here you can have your logic to set text to edittext
                                    }

                                    override fun onFinish() {

                                        const_servicecallaccept!!.visibility = View.GONE

                                    }
                                }.start()

                            }
                            else {

                                AppProgressBar.closeLoader()

                                if (response1.body() != null) {

                                    const_servicecallaccept!!.visibility = View.GONE

                                    if (response1.body()!!.detail!!.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                        )
                                    ) {

                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()


                                        val myIntent =
                                            Intent(
                                                requireActivity(),
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        requireActivity().finish()

                                    } else {


                                        /*Toast.makeText(
                                            requireActivity(),
                                            response1.body()!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()*/


                                    }

                                }
                            }


                        } else {

                            AppProgressBar.closeLoader()

                            if (response1.body() != null) {

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()


                            }
                        }
                    }

                    override fun onFailure(call: Call<ServicecallResponse?>, t: Throwable) {

                        AppProgressBar.closeLoader()

                        val activity: Activity? = activity
                        if (activity != null && isAdded()) {

                            if (t is NoConnectivityException) {
                                Toast.makeText(
                                    requireActivity(),
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                Toast.makeText(
                                    requireActivity(),
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun getDecline(serviceId: String) {

        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {

                AppProgressBar.openLoader(
                    requireActivity(),
                    requireActivity()!!.getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                if (defaultIdLog.equals("")) {

                    req["driver_id"] = defaultIdReg
                    req["service_id"] = serviceId
                    responsedecline = apiservice.doingservicecall_decline(tokenReg!!, req)
                } else {

                    req["driver_id"] = defaultIdLog
                    req["service_id"] = serviceId

                    responsedecline = apiservice.doingservicecall_decline(token, req)

                }

                responsedecline!!.enqueue(object : Callback<ResendOtpPojo?> {
                    override fun onResponse(
                        call: Call<ResendOtpPojo?>,
                        response1: Response<ResendOtpPojo?>
                    ) {
                        if (response1.isSuccessful() && response1.body() != null) {

                            AppProgressBar.closeLoader()


                            if (response1.body()!!.status == true) {

                                const_servicecallaccept!!.visibility = View.GONE

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                                val myIntent = Intent(requireActivity(), HomeActivity::class.java)
                                requireActivity().startActivity(myIntent)
                                requireActivity().finish()


                            } else {

                                AppProgressBar.closeLoader()

                            }


                        } else {

                            AppProgressBar.closeLoader()

                            if (response1.body() != null) {

                                Toast.makeText(
                                    requireActivity(),
                                    response1.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()


                            }
                        }
                    }

                    override fun onFailure(call: Call<ResendOtpPojo?>, t: Throwable) {
                        AppProgressBar.closeLoader()
                        val activity: Activity? = activity
                        if (activity != null && isAdded()) {
                            if (t is NoConnectivityException) {
                                Toast.makeText(
                                    requireActivity(),
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                Toast.makeText(
                                    requireActivity(),
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getAfterAcceptServiceCallAPI(
        serviceId: String,
        customerId: String,
        driverId: String,
        pickPlace: String,
        dropPlace: String,
        vehicleType: String,
        service_type: String,
        loadunloadafterAccptInServiceCall: Boolean,

        ) {

        try {

            val activity: Activity? = activity
            if (activity != null && isAdded()) {

                AppProgressBar.openLoader(
                    requireActivity(),
                    requireActivity()!!.getString(R.string.pleasewait)
                )

                val apiservice1: ApiInterface =
                    ApiClientForPusher.getClientforpusher().create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                req["service_id"] = serviceId

                if (defaultIdLog.equals("")) {

                    req["driver_id"] = defaultIdReg
                } else {

                    req["driver_id"] = defaultIdLog

                }

                response3 = apiservice1.doingdriver_service_call_accept(req)

                response3!!.enqueue(object : Callback<ServiceCallAcceptByDriverResponse?> {
                    override fun onResponse(
                        call: Call<ServiceCallAcceptByDriverResponse?>,
                        response: Response<ServiceCallAcceptByDriverResponse?>
                    ) {
                        if (response.isSuccessful()) {
                            AppProgressBar.closeLoader()

                            const_servicecallaccept!!.visibility = View.GONE
                            tv_accept!!.resetSlider()

                            if (response.body()!!.status == true) {

                                service_id = service_idstore
                                
                                Log.e("hi","afterAccept")

                                getDriverDetails(serviceId)

                                Log.v("service_typeL", "service_typeL" + service_type)

                                if (loadunloadafterAccptInServiceCall == false) {

                                    if (service_type.equals("")) {

                                        searchingForSecondaryDriverLayout!!.visibility = View.GONE

                                        val req = HashMap<String, Any>()
                                        val gson = Gson()
                                        req["id"] = serviceId
                                        req["driver_long"] = c_long!!
                                        req["driver_lat"] = c_lat!!
                                        req["status"] = "On the way"!!

                                        isAccepted = true
                                        val final = gson.toJson(req)


                                        try {
                                            channel.trigger("client-order-progress-status", final)
                                        } catch (e: Exception) {

                                            e.printStackTrace()
                                        }
                                        Log.v(
                                            "clientorderprogresttus",
                                            "clientorderprogressstatus" + final
                                        )

                                        constraintLayout!!.visibility = View.VISIBLE
                                        includeheaderpart!!.visibility = View.VISIBLE
                                        navClick!!.visibility = View.GONE
                                        iv_menusymbol!!.visibility = View.VISIBLE
                                        iv_toggle!!.visibility = View.GONE

                                        showvalueafteraccept(
                                            serviceId!!,
                                            customerId!!,
                                            driverId!!,
                                            pickPlace!!,
                                            dropPlace!!,
                                            vehicleType,
                                            service_type,
                                            customer_image
                                        )

                                    }

                                } else {

                                    if (service_type.equals("secondary")) {

                                        isAccepted = true

                                        constraintLayout!!.visibility = View.VISIBLE
                                        includeheaderpart!!.visibility = View.VISIBLE
                                        navClick!!.visibility = View.GONE
                                        iv_menusymbol!!.visibility = View.VISIBLE
                                        iv_toggle!!.visibility = View.GONE

                                        showvalueafteraccept(
                                            serviceId!!,
                                            customerId!!,
                                            driverId!!,
                                            pickPlace!!,
                                            dropPlace!!,
                                            vehicleType,
                                            service_type,
                                            customer_image
                                        )

                                    }
                                    else if (service_type.equals("")) {

                                        // 1st time calling
                                        Log.v("searchview2","searchview")

                                        /*gb changes for chat chat headername*/



                                        searchingForSecondaryDriverLayout!!.visibility = View.VISIBLE

                                        //  pusher = Pusher("d5041f2ec10120e451ce", options)

                                        pusher!!.connect(object : ConnectionEventListener {
                                            override fun onConnectionStateChange(change: ConnectionStateChange) {
                                                Log.i(
                                                    "Pusher",
                                                    "State changed from ${change.previousState} to ${change.currentState}"
                                                )

                                                if (change.currentState.toString()
                                                        .equals("CONNECTED", ignoreCase = true)
                                                ) {
                                                    val socketId = pusher!!.connection.socketId
                                                    if (socketId != null){
                                                        val channelname = "private-my-channel"
                                                        Log.v("socketId", "socketId" + socketId)
                                                        val parameters = HashMap<String, String>()
                                                        parameters["socket_id"] = socketId
                                                        parameters["channel_name"] =
                                                            "private-my-channel"
                                                        authorizer.setHeaders(parameters)
                                                        pusher!!.unsubscribe(channelname)

                                                        channel =
                                                            pusher!!.subscribePrivate("private-my-channel")

                                                        channel.bind(
                                                            "client-order-progress-status",
                                                            object : PrivateChannelEventListener {
                                                                override fun onAuthenticationFailure(
                                                                    message: String,
                                                                    e: java.lang.Exception
                                                                ) {
                                                                    println("channamemessage" + "::" + message)


                                                                }

                                                                override fun onSubscriptionSucceeded(
                                                                    channelName: String
                                                                ) {

                                                                    println("channame" + "::" + channelName)

                                                                }

                                                                override fun onEvent(event: PusherEvent) {

                                                                    if (activity != null && isAdded()) {

                                                                        requireActivity().runOnUiThread {

                                                                            /*  Toast.makeText(
                                                                                requireActivity(),
                                                                                "true",
                                                                                Toast.LENGTH_LONG
                                                                            ).show()
    */

                                                                            Log.i(
                                                                                "Pusher",
                                                                                "Received event with data: $event"
                                                                            )

                                                                            jsonObjectDriverEmergencyCancel =
                                                                                JSONObject(event.data)
                                                                            Log.v(
                                                                                "Ecnclscndrystatus",
                                                                                "status" + jsonObjectDriverEmergencyCancel!!.getString(
                                                                                    "status"
                                                                                )
                                                                            )

                                                                            if (jsonObjectDriverEmergencyCancel!!.getString(
                                                                                    "status"
                                                                                )
                                                                                    .equals("Emargency Cancel") || jsonObjectDriverEmergencyCancel!!.getString(
                                                                                    "status"
                                                                                )
                                                                                    .equals("Emergency_Cancelled")
                                                                            ) {

                                                                                if (jsonObjectDriverEmergencyCancel!!.getString(
                                                                                        "driver_id"
                                                                                    ) != null
                                                                                ) {

                                                                                    driver_idCancel =
                                                                                        jsonObjectDriverEmergencyCancel!!.getString(
                                                                                            "driver_id"
                                                                                        )

                                                                                }
                                                                            }

                                                                            service_idCancel =
                                                                                jsonObjectDriverEmergencyCancel!!.getString(
                                                                                    "id"
                                                                                )
                                                                            if (activity != null && isAdded()) {

                                                                                requireActivity().runOnUiThread {


                                                                                    if (service_id.equals(
                                                                                            service_idCancel
                                                                                        )
                                                                                    ) {


                                                                                        if (jsonObjectDriverEmergencyCancel!!.getString(
                                                                                                "status"
                                                                                            )
                                                                                                .equals(
                                                                                                    "Emargency Cancel"
                                                                                                ) || jsonObjectDriverEmergencyCancel!!.getString(
                                                                                                "status"
                                                                                            )
                                                                                                .equals(
                                                                                                    "Emergency_Cancelled"
                                                                                                )
                                                                                        ) {


                                                                                            if (driver_idCancel != defaultIdLog) {

                                                                                                if (tv_servicename!!.text.toString()
                                                                                                        .equals(
                                                                                                            "Primary Service"
                                                                                                        )
                                                                                                ) {


                                                                                                    val myIntent =
                                                                                                        Intent(
                                                                                                            requireActivity(),
                                                                                                            HomeActivity::class.java
                                                                                                        )
                                                                                                    requireActivity().startActivity(
                                                                                                        myIntent
                                                                                                    )


                                                                                                    callServiceCallAPIandAccept()


                                                                                                } else if (tv_servicename!!.text.toString()
                                                                                                        .equals(
                                                                                                            "Secondary Service"
                                                                                                        )
                                                                                                ) {

                                                                                                    searchingForSecondaryDriverLayout!!.visibility = View.VISIBLE


                                                                                                }

                                                                                            }


                                                                                        }


                                                                                    }

                                                                                }
                                                                            }
                                                                        }
                                                                    }


                                                                }
                                                            })

                                                        channel.bind(
                                                            "order-confirm-status",
                                                            object : PrivateChannelEventListener {
                                                                override fun onAuthenticationFailure(
                                                                    message: String,
                                                                    e: java.lang.Exception
                                                                ) {


                                                                }

                                                                override fun onSubscriptionSucceeded(
                                                                    channelName: String
                                                                ) {

                                                                }

                                                                override fun onEvent(event: PusherEvent) {
                                                                    Log.i(
                                                                        "Pusher",
                                                                        "Received event with data: $event"
                                                                    )
                                                                    Log.v(
                                                                        "coneccteddriverdata",
                                                                        "" + event.data
                                                                    )

                                                                    if (event.data == null) {

                                                                        return
                                                                    }

                                                                    jsonObjectDriver1 =
                                                                        JSONObject(event.data)

                                                                    if (activity != null && isAdded()) {
                                                                        requireActivity().runOnUiThread {

                                                                            searchingForSecondaryDriverLayout!!.visibility = View.GONE

                                                                            val req =
                                                                                HashMap<String, Any>()
                                                                            val gson = Gson()
                                                                            req["id"] = serviceId
                                                                            req["driver_long"] =
                                                                                c_long!!
                                                                            req["driver_lat"] = c_lat!!
                                                                            req["status"] = "On the way"!!



                                                                            isAccepted = true
                                                                            val final = gson.toJson(req)



                                                                            try {

                                                                                /*  Toast.makeText(
                                                                                    requireActivity(),
                                                                                    "autotrigger1",
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()*/


                                                                            } catch (e: Exception) {

                                                                                e.printStackTrace()
                                                                            }
                                                                            Log.v(
                                                                                "clientorderprogresttus",
                                                                                "clientorderprogressstatus" + final
                                                                            )

                                                                            constraintLayout!!.visibility =
                                                                                View.VISIBLE
                                                                            includeheaderpart!!.visibility =
                                                                                View.VISIBLE
                                                                            navClick!!.visibility =
                                                                                View.GONE
                                                                            iv_menusymbol!!.visibility =
                                                                                View.VISIBLE
                                                                            iv_toggle!!.visibility =
                                                                                View.GONE

                                                                            showvalueafteraccept(
                                                                                serviceId!!,
                                                                                customerId!!,
                                                                                driverId!!,
                                                                                pickPlace!!,
                                                                                dropPlace!!,
                                                                                vehicleType,
                                                                                service_type,
                                                                                customer_image
                                                                            )

                                                                        }
                                                                    }
                                                                }

                                                            })

                                                        if (jsonObjectDriver1 != null) {

                                                            if (activity != null && isAdded()) {

                                                                requireActivity().runOnUiThread {

                                                                    searchingForSecondaryDriverLayout!!.visibility = View.GONE

                                                                    val req = HashMap<String, Any>()
                                                                    val gson = Gson()
                                                                    req["id"] = serviceId
                                                                    req["driver_long"] = c_long!!
                                                                    req["driver_lat"] = c_lat!!
                                                                    req["status"] = "On the way"!!

                                                                    isAccepted = true
                                                                    val final = gson.toJson(req)



                                                                    try {

                                                                        /*  Toast.makeText(
                                                                            requireActivity(),
                                                                            "autotrigger",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()*/
                                                                    } catch (e: Exception) {

                                                                        e.printStackTrace()
                                                                    }
                                                                    Log.v(
                                                                        "clientorderprogresttus",
                                                                        "clientorderprogressstatus" + final
                                                                    )

                                                                    constraintLayout!!.visibility =
                                                                        View.VISIBLE
                                                                    includeheaderpart!!.visibility =
                                                                        View.VISIBLE
                                                                    navClick!!.visibility = View.GONE
                                                                    iv_menusymbol!!.visibility =
                                                                        View.VISIBLE
                                                                    iv_toggle!!.visibility = View.GONE

                                                                    showvalueafteraccept(
                                                                        serviceId!!,
                                                                        customerId!!,
                                                                        driverId!!,
                                                                        pickPlace!!,
                                                                        dropPlace!!,
                                                                        vehicleType,
                                                                        service_type,
                                                                        customer_image
                                                                    )

                                                                }
                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                            override fun onError(
                                                message: String?,
                                                code: String?,
                                                e: java.lang.Exception?
                                            ) {

                                                Log.i(
                                                    "Pusher",
                                                    "There was a problem connecting! code ($code), message ($message), exception($e)"
                                                )
                                            }

                                        }, ConnectionState.ALL)

                                    }

                                }

                            }

                            else {

                                AppProgressBar.closeLoader()
                                const_servicecallaccept!!.visibility = View.GONE
                                tv_accept!!.resetSlider()

                                if (response.body() != null) {

                                    if (response.body()!!.message!!.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice),ignoreCase = true
                                        )
                                    ) {

                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()


                                        val myIntent =
                                            Intent(
                                                requireActivity(),
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        requireActivity().finish()

                                    } else {

                                        tv_accept!!.resetSlider()
                                        Toast.makeText(
                                            requireActivity(),
                                            response.body()!!.message.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    }

                                }
                            }


                        }

                        else {

                            AppProgressBar.closeLoader()
                            tv_accept!!.resetSlider()
                            if (response.body() != null) {

                                Toast.makeText(
                                    requireActivity(),
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()


                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<ServiceCallAcceptByDriverResponse?>,
                        t: Throwable
                    ) {
                        AppProgressBar.closeLoader()
                        tv_accept!!.resetSlider()
                        val activity: Activity? = activity

                        if (activity != null && isAdded()) {

                            const_servicecallaccept!!.visibility = View.GONE


                            if (t is NoConnectivityException) {
                                Toast.makeText(
                                    requireActivity(),
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                Toast.makeText(
                                    requireActivity(),
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun getDriverDetails(serviceId: String) {

        try {

            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    requireActivity()!!.getString(R.string.pleasewait)
                )

                val apiservice1: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                req["service_id"] = serviceId

                if (defaultIdLog.equals("")) {
                    req["driver_id"] = defaultIdReg!!
                    response4 = apiservice1.doingservicecall_detailsinformation(tokenReg, req)

                } else {

                    req["driver_id"] = defaultIdLog!!
                    response4 = apiservice1.doingservicecall_detailsinformation(token, req)
                }

                response4!!.enqueue(object : Callback<ServiceCallStatuswiseetailsResponse?> {
                    override fun onResponse(
                        call: Call<ServiceCallStatuswiseetailsResponse?>,
                        response: Response<ServiceCallStatuswiseetailsResponse?>
                    ) {
                        if (response.isSuccessful()) {
                            AppProgressBar.closeLoader()

                            if (response.body()!!.status == true) {
                                
                                Log.e("dataDriver",response.body()!!.toString())


                                primaryDriverName=response.body()!!.detail!!.primary_driver_name.toString()
                                secondaryDriverName=response.body()!!.detail!!.secondary_driver_name.toString()

                                pick_latFromList = response.body()!!.detail!!.pickLat!!.toDouble()
                                pick_longFromList = response.body()!!.detail!!.pickLong!!.toDouble()
                                dest_latFromList = response.body()!!.detail!!.dropLat!!.toDouble()
                                dest_longFromList = response.body()!!.detail!!.dropLong!!.toDouble()
                                pickPlace = response.body()!!.detail!!.pickPlace!!.toString()
                                dropPlace = response.body()!!.detail!!.dropPlace!!.toString()
                                customerId = response.body()!!.detail!!.customerId!!.toString()
                                service_id = response.body()!!.detail!!.serviceId!!.toString()
                                driverId = response.body()!!.detail!!.driverId!!.toString()
                                second_driver_id = response.body()!!.detail!!.second_driver_id!!.toString()
                                vehicleType = response.body()!!.detail!!.vehicleType!!.toString()
                                vehicleImage = response.body()!!.detail!!.uploadPic!!.toString()
                                loadUnload = response.body()!!.detail!!.loadUnload!!
                                unattendDrop = response.body()!!.detail!!.unattendDrop!!
                                customer_name = response.body()!!.detail!!.customerName!!
                                customer_image = response.body()!!.detail!!.customer_image!!
                                is_reassign = response.body()!!.detail!!.is_reassign!!
                                reassign_driver = response.body()!!.detail!!.reassign_driver!!
                                Log.v("reassign_driver","reassign_driver"+reassign_driver)
                                Log.v("is_reassign","is_reassign"+is_reassign)
                                previous_driver_lat = response.body()!!.detail!!.previous_driver_lat!!.toDouble()
                                previous_driver_long = response.body()!!.detail!!.previous_driver_long!!.toDouble()
                                driverstatus = response.body()!!.detail!!.status!!
                                /*Setservice_type*/ service_type = response.body()!!.detail!!.service_type.toString()!!



                                loadunloadafterAccptInServiceCall = response.body()!!.detail!!.loadUnload!!

                                 if (response.body()!!.detail!!.status!!.toString().equals("ArrivedAtDestination", ignoreCase = true))
                                {
                                   /* var Setservice_type : String?=null

                                     if (loadunloadafterAccptInServiceCall ==true){
                                          Setservice_type = service_type
                                     }
                                    else{

                                         Setservice_type = service_type
                                     }*/

                                    handler.removeCallbacksAndMessages(null)
                                    includeheaderpart!!.visibility = View.VISIBLE
                                    constraintLayoutForArraived!!.visibility = View.GONE
                                    constraintLayoutUnloadingInprogress!!.visibility = View.VISIBLE
                                    navClick!!.visibility = View.GONE
                                    iv_toggle!!.visibility = View.GONE
                                    includebottomsheet_arraivepickup_location!!.visibility = View.GONE
                                    constraintLayout!!.visibility = View.GONE

                                    showvalueafteraccept(
                                        response.body()!!.detail!!.serviceId!!.toString(),
                                        response.body()!!.detail!!.customerId!!.toString(),
                                        response.body()!!.detail!!.driverId!!.toString(),
                                        response.body()!!.detail!!.pickPlace!!.toString(),
                                        response.body()!!.detail!!.dropPlace!!.toString(),
                                        response.body()!!.detail!!.vehicleType!!.toString(),
                                        /*Setservice_type*/ service_type ,
                                        response.body()!!.detail!!.customer_image!!

                                    )


                                    constraintLayoutUnloadingInprogress!!.visibility = View.VISIBLE
                                    iv_menusymbol!!.visibility = View.VISIBLE


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                        val dtf: DateTimeFormatter =
                                            //DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS")
                                            DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss.SSSS")
                                        val now: LocalDateTime = LocalDateTime.now()
                                        System.out.println("pujaa" + dtf.format(now))

                                        val CurrentDate = dtf.format(now)
                                        val FinalDate = response.body()!!.detail!!.arrivedDestinationTime!!.toString()
                                           /* requireActivity().intent.getStringExtra("arrivedDestinationTime")!!*/
                                        val date1: Date
                                        val date2: Date
                                        // val dates = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS")
                                        val dates = SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSSS")
                                        date1 = dates.parse(CurrentDate)
                                        date2 = dates.parse(FinalDate)

                                        // Calucalte time difference in second

                                        timeInSeconds_unloadinginNew = (date1.time - date2.time) / 1000

                                    }

                                    unloadingInProgressStartTimerNew()


                                }

                                if (!response.body()!!.detail!!.second_driver_id!!.equals("")){

                                    searchingForSecondaryDriverLayout!!.visibility = View.GONE

                                   // name!!.setText()
                                   // name!!.text = response.body()!!.detail!!.driverName.toString()
                                }
                                else{
                                   // name!!.text = customer_name
                                }

                              //  if()  dfgdgd



                                if (response.body()!!.detail!!.loadUnload == true) {


                                    loadunloadiv!!.setImageResource(R.drawable.checkmarkcircle);
                                    loadunloadiv!!.getLayoutParams().height = 35
                                    loadunloadiv!!.getLayoutParams().width = 35

                                }
                                else {
                                    loadunloadiv!!.setImageResource(R.drawable.cancelfalseicon)
                                    loadunloadiv!!.getLayoutParams().height = 35
                                    loadunloadiv!!.getLayoutParams().width = 35
                                }
                                if (response.body()!!.detail!!.unattendDrop == true) {

                                    unatndiv!!.setImageResource(R.drawable.checkmarkcircle);
                                    unatndiv!!.getLayoutParams().height = 35
                                    unatndiv!!.getLayoutParams().width = 35

                                }
                                else {
                                    unatndiv!!.setImageResource(R.drawable.cancelfalseicon);
                                    unatndiv!!.getLayoutParams().height = 35
                                    unatndiv!!.getLayoutParams().width = 35
                                }

                                Glide.with(requireActivity())
                                    .load(NetworkUtility.imgbaseUrl + vehicleImage)/*.placeholder(R.drawable.defaultperson)*/.error(R.drawable.defaultperson)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true).into(vehicle_image!!)


                                isAccepted = true
                                Log.v("pu","loadunloadafterAccptInServiceCall"+loadunloadafterAccptInServiceCall)
                                if (loadunloadafterAccptInServiceCall == false) {
                                    //Normal servic call
                                    if (service_type.equals("")) {
                                    Log.v("primary","primary")
                                        if (is_reassign!! == true && driverstatus.equals("Paused") ){

                                            reassign!!.visibility = View.VISIBLE

                                            val req = HashMap<String, Any>()
                                            val gson = Gson()
                                            req["id"] = serviceId!!
                                            req["customer_id"] = customerId!!

                                            val final = gson.toJson(req)

                                            try {

                                                if (channel == null) {

                                                    channel =
                                                        pusher!!.subscribePrivate("private-my-channel")
                                                }

                                                channel.trigger("client-reassignment", final)


                                            } catch (e: Exception) {

                                                e.printStackTrace()
                                            }

                                            val geocoder: Geocoder
                                            val addresses: List<Address>

                                            val activity: Activity? = activity
                                            if (activity != null && isAdded()) {

                                                geocoder = Geocoder(requireActivity(), Locale.getDefault())

                                                addresses = geocoder.getFromLocation(
                                                    previous_driver_lat!! /*22.541321292082095*/,
                                                    previous_driver_long!! /*88.39307326810003*/,
                                                    1
                                                )

                                                city = addresses.get(0).getAddressLine(0);

                                                tv_reassignmentLocation!!.setText(city)

                                            }
                                        }
                                        else {

                                            val req = HashMap<String, Any>()
                                            val gson = Gson()
                                            req["id"] = serviceId
                                            req["driver_long"] = c_long!!
                                            req["driver_lat"] = c_lat!!
                                            req["status"] = "On the way"!!

                                            isAccepted = true
                                            val final = gson.toJson(req)

                                            searchingForSecondaryDriverLayout!!.visibility = View.GONE


                                          /*  try {
                                                channel.trigger(
                                                    "client-order-progress-status",
                                                    final
                                                )
                                            } catch (e: Exception) {

                                                e.printStackTrace()
                                            }*/
                                            Log.v(
                                                "clientorderprogresttus",
                                                "clientorderprogressstatus" + final
                                            )

                                            //   constraintLayout!!.visibility = View.VISIBLE
                                            includeheaderpart!!.visibility = View.VISIBLE
                                            navClick!!.visibility = View.GONE
                                            iv_menusymbol!!.visibility = View.VISIBLE
                                            iv_toggle!!.visibility = View.GONE
                                            reassign!!.visibility = View.GONE

                                            showvalueafteraccept(
                                                serviceId!!,
                                                customerId!!,
                                                driverId!!,
                                                pickPlace!!,
                                                dropPlace!!,
                                                vehicleType,
                                                service_type,
                                                customer_image
                                            )
                                        }

                                    }


                                }

                                else {

                                    if (service_type.equals("secondary")) {

                                         isAccepted = true

                                            constraintLayout!!.visibility = View.VISIBLE
                                            includeheaderpart!!.visibility = View.VISIBLE
                                            navClick!!.visibility = View.GONE
                                            iv_menusymbol!!.visibility = View.VISIBLE
                                            iv_toggle!!.visibility = View.GONE

                                            showvalueafteraccept(
                                                serviceId!!,
                                                customerId!!,
                                                driverId!!,
                                                pickPlace!!,
                                                dropPlace!!,
                                                vehicleType,
                                                service_type,
                                                customer_image
                                            )

                                        if (!response.body()!!.detail!!.second_driver_id!!.equals("")){

                                            searchingForSecondaryDriverLayout!!.visibility = View.GONE
                                        }


                                    }

                                    else if (service_type.equals("")) {

                                            // 1st time calling
                                            Log.v("searchview","searchview")

                                        if (!response.body()!!.detail!!.second_driver_id!!.equals("")){

                                            searchingForSecondaryDriverLayout!!.visibility = View.GONE
                                        }
                                        else{
                                            searchingForSecondaryDriverLayout!!.visibility = View.VISIBLE

                                        }


                                        // pusher = Pusher("d5041f2ec10120e451ce", options)
                                            pusher = Pusher(NetworkUtility.key, options)

                                            pusher!!.connect(object : ConnectionEventListener {
                                                override fun onConnectionStateChange(change: ConnectionStateChange) {
                                                    Log.i(
                                                        "Pusher",
                                                        "State changed from ${change.previousState} to ${change.currentState}"
                                                    )

                                                    if (change.currentState.toString()
                                                            .equals("CONNECTED", ignoreCase = true)
                                                    ) {
                                                        val socketId = pusher!!.connection.socketId
                                                        if (socketId != null) {
                                                            val channelname = "private-my-channel"
                                                            Log.v("socketId", "socketId" + socketId)
                                                            val parameters =
                                                                HashMap<String, String>()
                                                            parameters["socket_id"] = socketId
                                                            parameters["channel_name"] =
                                                                "private-my-channel"
                                                            authorizer.setHeaders(parameters)
                                                            pusher!!.unsubscribe(channelname)

                                                            channel =
                                                                pusher!!.subscribePrivate("private-my-channel")

                                                            channel.bind(
                                                                "client-order-progress-status",
                                                                object :
                                                                    PrivateChannelEventListener {
                                                                    override fun onAuthenticationFailure(
                                                                        message: String,
                                                                        e: java.lang.Exception
                                                                    ) {
                                                                        println("channamemessage" + "::" + message)


                                                                    }

                                                                    override fun onSubscriptionSucceeded(
                                                                        channelName: String
                                                                    ) {

                                                                        println("channame" + "::" + channelName)

                                                                    }

                                                                    override fun onEvent(event: PusherEvent) {

                                                                        if (activity != null && isAdded()) {

                                                                            requireActivity().runOnUiThread {

                                                                                /* Toast.makeText(
                                                                                requireActivity(),
                                                                                "true",
                                                                                Toast.LENGTH_LONG
                                                                            ).show()*/


                                                                                Log.i(
                                                                                    "Pusher",
                                                                                    "Received event with data: $event"
                                                                                )

                                                                                jsonObjectDriverEmergencyCancel =
                                                                                    JSONObject(event.data)
                                                                                Log.v(
                                                                                    "Ecnclscndrystatus",
                                                                                    "status" + jsonObjectDriverEmergencyCancel!!.getString(
                                                                                        "status"
                                                                                    )
                                                                                )

                                                                                if (jsonObjectDriverEmergencyCancel!!.getString(
                                                                                        "status"
                                                                                    )
                                                                                        .equals("Emargency Cancel") || jsonObjectDriverEmergencyCancel!!.getString(
                                                                                        "status"
                                                                                    )
                                                                                        .equals("Emergency_Cancelled")
                                                                                ) {

                                                                                    if (jsonObjectDriverEmergencyCancel!!.getString(
                                                                                            "driver_id"
                                                                                        ) != null
                                                                                    ) {

                                                                                        driver_idCancel =
                                                                                            jsonObjectDriverEmergencyCancel!!.getString(
                                                                                                "driver_id"
                                                                                            )

                                                                                    }
                                                                                }

                                                                                service_idCancel =
                                                                                    jsonObjectDriverEmergencyCancel!!.getString(
                                                                                        "id"
                                                                                    )

                                                                                if (activity != null && isAdded()) {
                                                                                    requireActivity().runOnUiThread {


                                                                                        if (service_id.equals(
                                                                                                service_idCancel
                                                                                            )
                                                                                        ) {


                                                                                            if (jsonObjectDriverEmergencyCancel!!.getString(
                                                                                                    "status"
                                                                                                )
                                                                                                    .equals(
                                                                                                        "Emargency Cancel"
                                                                                                    ) || jsonObjectDriverEmergencyCancel!!.getString(
                                                                                                    "status"
                                                                                                )
                                                                                                    .equals(
                                                                                                        "Emergency_Cancelled"
                                                                                                    )
                                                                                            ) {


                                                                                                if (driver_idCancel != defaultIdLog) {

                                                                                                    if (tv_servicename!!.text.toString()
                                                                                                            .equals(
                                                                                                                "Primary Service"
                                                                                                            )
                                                                                                    ) {


                                                                                                        val myIntent =
                                                                                                            Intent(
                                                                                                                requireActivity(),
                                                                                                                HomeActivity::class.java
                                                                                                            )
                                                                                                        requireActivity().startActivity(
                                                                                                            myIntent
                                                                                                        )


                                                                                                        callServiceCallAPIandAccept()


                                                                                                    } else if (tv_servicename!!.text.toString()
                                                                                                            .equals(
                                                                                                                "Secondary Service"
                                                                                                            )
                                                                                                    ) {

                                                                                                        Log.v("searchview1","searchview")

                                                                                                        searchingForSecondaryDriverLayout!!.visibility = View.VISIBLE


                                                                                                    }

                                                                                                }


                                                                                            }


                                                                                        }

                                                                                    }
                                                                                }
                                                                            }

                                                                        }

                                                                    }
                                                                })

                                                            channel.bind(
                                                                "order-confirm-status",
                                                                object :
                                                                    PrivateChannelEventListener {
                                                                    override fun onAuthenticationFailure(
                                                                        message: String,
                                                                        e: java.lang.Exception
                                                                    ) {


                                                                    }

                                                                    override fun onSubscriptionSucceeded(
                                                                        channelName: String
                                                                    ) {

                                                                    }

                                                                    override fun onEvent(event: PusherEvent) {
                                                                        Log.i(
                                                                            "Pusher",
                                                                            "Received event with data: $event"
                                                                        )
                                                                        Log.v(
                                                                            "coneccteddriverdata",
                                                                            "" + event.data
                                                                        )

                                                                        if (event.data == null) {

                                                                            return
                                                                        }

                                                                        jsonObjectDriver1 =
                                                                            JSONObject(event.data)

                                                                        if (activity != null && isAdded()) {
                                                                            requireActivity().runOnUiThread {

                                                                                searchingForSecondaryDriverLayout!!.visibility = View.GONE

                                                                                val req =
                                                                                    HashMap<String, Any>()
                                                                                val gson = Gson()
                                                                                req["id"] =
                                                                                    serviceId
                                                                                req["driver_long"] =
                                                                                    c_long!!
                                                                                req["driver_lat"] =
                                                                                    c_lat!!
                                                                                req["status"] = "On the way"!!

                                                                                isAccepted = true
                                                                                val final =
                                                                                    gson.toJson(req)

                                                                                try {

                                                                                    /*  Toast.makeText(
                                                                                    requireActivity(),
                                                                                    "autotrigger1",
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()*/


                                                                                } catch (e: Exception) {

                                                                                    e.printStackTrace()
                                                                                }
                                                                                Log.v(
                                                                                    "clientorderprogresttus",
                                                                                    "clientorderprogressstatus" + final
                                                                                )

                                                                                constraintLayout!!.visibility =
                                                                                    View.VISIBLE
                                                                                includeheaderpart!!.visibility =
                                                                                    View.VISIBLE
                                                                                navClick!!.visibility =
                                                                                    View.GONE
                                                                                iv_menusymbol!!.visibility =
                                                                                    View.VISIBLE
                                                                                iv_toggle!!.visibility =
                                                                                    View.GONE

                                                                                showvalueafteraccept(
                                                                                    serviceId!!,
                                                                                    customerId!!,
                                                                                    driverId!!,
                                                                                    pickPlace!!,
                                                                                    dropPlace!!,
                                                                                    vehicleType,
                                                                                    service_type,
                                                                                    customer_image
                                                                                )

                                                                            }
                                                                        }
                                                                    }

                                                                })

                                                            if (jsonObjectDriver1 != null) {
                                                                if (activity != null && isAdded()) {
                                                                    requireActivity().runOnUiThread {

                                                                        searchingForSecondaryDriverLayout!!.visibility = View.GONE

                                                                        val req =
                                                                            HashMap<String, Any>()
                                                                        val gson = Gson()
                                                                        req["id"] = serviceId
                                                                        req["driver_long"] =
                                                                            c_long!!
                                                                        req["driver_lat"] = c_lat!!
                                                                        req["status"] = "On the way"!!

                                                                        isAccepted = true
                                                                        val final = gson.toJson(req)



                                                                        try {

                                                                            /* Toast.makeText(
                                                                            requireActivity(),
                                                                            "autotrigger",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()*/
                                                                        } catch (e: Exception) {

                                                                            e.printStackTrace()
                                                                        }
                                                                        Log.v(
                                                                            "clientorderprogresttus",
                                                                            "clientorderprogressstatus" + final
                                                                        )

                                                                        constraintLayout!!.visibility =
                                                                            View.VISIBLE
                                                                        includeheaderpart!!.visibility =
                                                                            View.VISIBLE
                                                                        navClick!!.visibility =
                                                                            View.GONE
                                                                        iv_menusymbol!!.visibility =
                                                                            View.VISIBLE
                                                                        iv_toggle!!.visibility =
                                                                            View.GONE

                                                                        showvalueafteraccept(
                                                                            serviceId!!,
                                                                            customerId!!,
                                                                            driverId!!,
                                                                            pickPlace!!,
                                                                            dropPlace!!,
                                                                            vehicleType,
                                                                            service_type,
                                                                            customer_image
                                                                        )

                                                                    }
                                                                }

                                                            }

                                                        }
                                                    }

                                                }

                                                override fun onError(
                                                    message: String?,
                                                    code: String?,
                                                    e: java.lang.Exception?
                                                ) {

                                                    Log.i(
                                                        "Pusher",
                                                        "There was a problem connecting! code ($code), message ($message), exception($e)"
                                                    )
                                                }

                                            }, ConnectionState.ALL)

                                    }

                                }

                            }
                            else {

                                AppProgressBar.closeLoader()

                                if (response.body() != null) {

                                    if (response.body()!!.message.equals(
                                            resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                            ignoreCase = true
                                        )
                                    ) {

                                        val sharedPreferences =
                                            requireActivity().getSharedPreferences(
                                                "MySharedPref",
                                                Context.MODE_PRIVATE
                                            )
                                        val editor = sharedPreferences.edit()
                                        sharedPreferenceManager!!.logoutUser()
                                        editor.clear()
                                        editor.commit()


                                        val myIntent =
                                            Intent(
                                                requireActivity(),
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        requireActivity().finish()

                                    } else {

                                        if (response.body()!!.message.toString().trim().equals("ServiceCall is Not Found",true)){

                                            if (constraintLayout!!.isVisible == true || includebottomsheet_arraivepickup_location!!.isVisible == true) {
                                                Toast.makeText(
                                                    requireActivity(),
                                                    "ServiceCall is cancelled by customer",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                constraintLayout!!.visibility == View.GONE
                                                includebottomsheet_arraivepickup_location!!.visibility == View.GONE

                                                val myIntent = Intent(
                                                    requireActivity(),
                                                    HomeActivity::class.java
                                                )
                                                requireActivity().startActivity(myIntent)
                                                requireActivity().finish()

                                                /*popupnodriver!!.visibility == View.VISIBLE

                                                popupok!!.setOnClickListener {

                                                    val myIntent = Intent(
                                                        requireActivity(),
                                                        HomeActivity::class.java
                                                    )
                                                    requireActivity().startActivity(myIntent)
                                                    requireActivity().finish()
                                                }*/


                                            }
                                        }else{

                                            Toast.makeText(
                                                requireActivity(),
                                                response.body()!!.message.toString(),
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                    }

                                }
                            }


                        }
                        else {

                            AppProgressBar.closeLoader()

                            if (response.body() != null) {

                                Toast.makeText(
                                    requireActivity(),
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }


                        }
                    }

                    override fun onFailure(
                        call: Call<ServiceCallStatuswiseetailsResponse?>,
                        t: Throwable
                    ) {
                        AppProgressBar.closeLoader()
                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun showvalueafterclicknavigator(
        serviceId: String,
        customerId: String,
        driverId: String,
        pickPlace: String,
        dropPlace: String,
        vehicleType: String
    ) {

        tv_customerlocation_arrivedlocation!!.setText(pickPlace)

        showvalueafteraccept(
            serviceId!!,
            customerId!!,
            driverId!!,
            pickPlace!!,
            dropPlace!!,
            vehicleType,
            service_type,
            customer_image
        )


    }

    fun arrivedPickUpLocationAPICalling(
        serviceId: String,
        customerId: String,
        driverId: String,
        pickPlace: String,
        dropPlace: String,
        vehicleType: String,
        customer_image: String,
    ) {

        try {
            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                AppProgressBar.openLoader(
                    requireActivity(),
                    requireActivity().getString(R.string.pleasewait)
                )

                val apiservice1: ApiInterface =
                    ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()


                req["service_id"] = serviceId!!
                if (tv_servicename!!.text.toString().trim().equals("Primary Service",true)){

                    service_type = ""
                }
                else{
                    service_type = "secondary"
                }
                req["service_type"] = service_type

                if (defaultIdLog.equals("")) {
                    responseArrive = apiservice1.doingservicecall_arrived(tokenReg, req)

                } else {

                    responseArrive = apiservice1.doingservicecall_arrived(token, req)
                }

                responseArrive!!.enqueue(object : Callback<VerifyOtpPojo?> {
                    override fun onResponse(
                        call: Call<VerifyOtpPojo?>,
                        response: Response<VerifyOtpPojo?>
                    ) {
                        if (response.isSuccessful()) {
                            AppProgressBar.closeLoader()

                            const_servicecallaccept!!.visibility = View.GONE

                            if (response.body()!!.status == true) {

                                if (service_type.equals("")) {

                                    //   Toast.makeText(requireActivity(), "code", Toast.LENGTH_SHORT).show()

                                    includebottomsheet_cancelservicecall!!.visibility = View.VISIBLE
                                    includebottomsheet_arraivepickup_location!!.visibility = View.GONE

                                    iv_cancelServicecall!!.visibility = View.VISIBLE
                                    cross_black!!.visibility = View.VISIBLE
                                    textView53!!.visibility = View.VISIBLE
                                    iv_cancelServicecallblack!!.visibility = View.VISIBLE
                                    cross_white!!.visibility = View.VISIBLE
                                    textView46!!.visibility = View.VISIBLE
                                    iv_timerview!!.visibility = View.VISIBLE
                                    tv_timer!!.visibility = View.VISIBLE
                                    tv_enterCustomerCode!!.setText("Enter Customer Code")
                                    constraintLayout!!.visibility = View.GONE

                                    driver_updatestatus = "ArrivedAtPickUp"

                                    val req = HashMap<String, Any>()
                                    val gson = Gson()
                                    req["driver_long"] = c_long!!
                                    req["driver_lat"] = c_lat!!
                                    req["status"] = driver_updatestatus!!
                                    req["id"] = serviceId!!

                                    val final = gson.toJson(req)

                                    try {

                                        channel.trigger("client-order-progress-status", final)

                                    } catch (e: Exception) {

                                    }
                                    Log.v(
                                        "clientorderprogresstus",
                                        "clientorderprogressstatus" + final
                                    )
                                    includeheaderpart!!.visibility = View.VISIBLE
                                    showvalueafteraccept(
                                        serviceId!!,
                                        customerId!!,
                                        driverId!!,
                                        pickPlace!!,
                                        dropPlace!!,
                                        vehicleType,
                                        service_type,
                                        customer_image
                                    )

                                    iv_menusymbol!!.visibility = View.VISIBLE

                                    tv_customerlocationForCancelService!!.setText(pickPlace)

                                    customerGreetStartTimer()


                                } else if (service_type.equals("secondary")) {

                                    Toast.makeText(
                                        requireActivity(),
                                        "primarycode",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    includebottomsheet_cancelservicecall!!.visibility = View.VISIBLE
                                    iv_menusymbol!!.visibility = View.VISIBLE
                                    driver_updatestatus = "ArrivedAtPickUp"

                                    tv_customerlocationForCancelService!!.setText(pickPlace)
                                    iv_cancelServicecall!!.visibility = View.GONE
                                    cross_black!!.visibility = View.GONE
                                    textView53!!.visibility = View.GONE
                                    iv_cancelServicecallblack!!.visibility = View.GONE
                                    cross_white!!.visibility = View.GONE
                                    textView46!!.visibility = View.GONE
                                    iv_timerview!!.visibility = View.GONE
                                    tv_timer!!.visibility = View.GONE
                                    tv_enterCustomerCode!!.setText("Enter Primary Driver Code")

                                    includeheaderpart!!.visibility = View.VISIBLE

                                    showvalueafteraccept(
                                        serviceId!!,
                                        customerId!!,
                                        driverId!!,
                                        pickPlace!!,
                                        dropPlace!!,
                                        vehicleType,
                                        service_type,
                                        customer_image
                                    )


                                }

                            }

                        } else {

                            AppProgressBar.closeLoader()

                            Toast.makeText(
                                requireActivity(),
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                        }
                    }

                    override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                        AppProgressBar.closeLoader()

                        val activity: Activity? = activity
                        if (activity != null && isAdded()) {

                            if (t is NoConnectivityException) {
                                Toast.makeText(
                                    requireActivity(),
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                Toast.makeText(
                                    requireActivity(),
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun showvalueafteraccept(
        serviceId: String,
        customerId: String,
        driverId: String,
        pickPlace: String,
        dropPlace: String,
        vehicleType: String,
        service_type: String,
        customer_image: String,
    ) {

        /* if (defaultIdLog.equals("")) {

            tv_username!!.setText(fnameSharedPrefAfterReg + "" + lnameSharedPrefAfterReg)

        } else {
            tv_username!!.setText(fnameSharedPrefAfterLog + "" + lnameSharedPrefAfterLog)

        }*/

        try {
            //  iv_menusymbol!!.visibility = View.VISIBLE
            tv_cartype!!.setText(vehicleType)
            tv_servicerequestid_!!.setText("#KIT-" + serviceId)
            tv_customerlocation!!.setText(pickPlace)

            tv_username!!.setText(customer_name)

           // getDriverDetails(serviceId)

            if (service_type.equals("")) {
                tv_servicename!!.setText("Primary Service")
                tv_servicename!!.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.serviceprimary
                    )
                )
                tv_servicename!!.setBackground(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.round_backgroundlightblue
                    )
                )

            } else {
                tv_servicename!!.setText("Secondary Service")
                tv_servicename!!.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.servicesecondary
                    )
                )
                tv_servicename!!.setBackground(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.round_backgroundsecondary
                    )
                )

            }

        } catch (e: Exception) {

            e.printStackTrace()
        }

    }

    // method to check for permissions
   /* private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(

            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }*/


    fun checkPermissions():Boolean {
        return if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

/*                AlertDialog.Builder(requireActivity())
                    .setTitle("Location Permission")
                    .setMessage("Location")
                    .setPositiveButton(R.string.ok,
                        DialogInterface.OnClickListener { dialogInterface, i -> //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                MY_PERMISSIONS_REQUEST_LOCATION
                            )
                        })
                    .create()
                    .show()*/

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            false
        } else {
            true
        }
    }


    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {

        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String

        locationMode = try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
            return false
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        mFusedLocationClient?.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.myLooper()
                        )
                        mMap!!.setMyLocationEnabled(false)
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(requireActivity(), "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }


    private fun drawCircle(latLng: LatLng) {

        // Instantiating CircleOptions to draw a circle around the marker
        val circleOptions = CircleOptions()
        // Specifying the center of the circle
        circleOptions.center(latLng);
        // Radius of the circle
        circleOptions.radius(80.0);
        // Border color of the circle
        circleOptions.strokeColor(Color.parseColor("#FFFFFF"))
        // Fill color of the circle
        circleOptions.fillColor(Color.parseColor("#FFFFFF"))
        // Border width of the circle
        circleOptions.strokeWidth(2F);
        // Adding the circle to the GoogleMap
        //  googleMap!!.addCircle(circleOptions);
        mMap!!.addCircle(circleOptions);


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LandingViewModel::class.java)
        // TODO: Use the ViewModel
    }


    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    /*  override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }*/

    override fun onMapReady(p0: GoogleMap) {
        if(isAdded()){
            if (p0 != null) {
                mMap = p0
                mMap!!.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap!!.getUiSettings().setCompassEnabled(false);

            }
            mMap!!.isMyLocationEnabled = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    buildGoogleApiClient()
                    mMap!!.isMyLocationEnabled = false
                }
            } else {
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = false
            }

            mMarkerOptions = MarkerOptions()

            mLocationRequest = LocationRequest()
            mLocationRequest.interval = 120000 // two minute interval
            mLocationRequest.fastestInterval = 120000
            mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    //Location Permission already granted
                    //  mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                    mFusedLocationClient!!.lastLocation.addOnSuccessListener { location: Location? ->
                        location?.let { it: Location ->
                            // Logic to handle location object

                            Log.v(
                                "latitudeonfusedmapready",
                                "latitudeonfused" + it.latitude + ":" + it.longitude
                            )

                            mMap!!.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        it.latitude,
                                        it.longitude
                                    ), 16f
                                )
                            )
                        } ?: kotlin.run {
                            // Handle Null case or Request periodic location update https://developer.android.com/training/location/receive-location-updates
                        }
                    }
                    mMap!!.isMyLocationEnabled = false
                } else {
                    //Request Location Permission
                    checkLocationPermission()
                }
            } else {
                mFusedLocationClient?.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.myLooper()
                )
                mMap!!.isMyLocationEnabled = false
            }
        }
    }


    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(requireActivity())
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }



    @Synchronized
    public fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(requireActivity())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }


    override fun onItemClick(position: Int, countryList: List<FavouritePlacesItem>) {

        edt_destination!!.setText(countryList.get(position).address)


    }

    override fun onLocationChanged(location: Location) {

        Log.v(
            "Puja",
            "IN ON LOCATION CHANGE, lat=" + location.latitude + ", lon=" + location.longitude
        )

        //   Toast.makeText(activity, "IN ON LOCATION CHANGE"+location.latitude + ", lon=" + location.latitude, Toast.LENGTH_LONG).show()

        mLastLocation = location

        val latLng = LatLng(location.getLatitude(), location.getLongitude())
        c_lat = location.getLatitude()
        c_long = location.getLongitude()

        val activity: Activity? = activity
        if (activity != null && isAdded()) {


            mMap!!.clear()
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker!!.remove()
            }
            //Place current location marker
            //Place current location marker
            mMarkerOptions?.position(latLng)
            mMarkerOptions?.title("Current Position")
            mMarkerOptions?.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrowloc))
            mMarkerOptions?.anchor(0.5f, 0.5f)
            //  mMap!!.addMarker(markerOptions)
            mCurrLocationMarker = mMap!!.addMarker(mMarkerOptions!!)


            if (pick_latFromList!!>0.0) {


                val activity: Activity? = activity
                if (activity != null && isAdded()) {

                    if (requireActivity().intent.hasExtra("pick_lat"))
                        pick_latFromList =
                            requireActivity().intent.getStringExtra("pick_lat")!!.toDouble()!!

                    if (requireActivity().intent.hasExtra("pick_long"))
                        pick_longFromList =
                            requireActivity().intent.getStringExtra("pick_long")!!.toDouble()!!

                    if (requireActivity().intent.hasExtra("drop_lat"))
                        dest_latFromList =
                            requireActivity().intent.getStringExtra("drop_lat")!!.toDouble()!!

                    if (requireActivity().intent.hasExtra("drop_long"))
                        dest_longFromList =
                            requireActivity().intent.getStringExtra("drop_long")!!.toDouble()!!


                    if (isNearBy!!) {

                        distanceCalculate(
                            location.getLatitude()!!,
                            location.getLongitude()!!,/*22.58594*/
                            pick_latFromList!!,/*88.42125*/
                            pick_longFromList!!
                        ).let { itDist ->

                            Log.v("itDistAtPickup", "" + itDist)

                            if (itDist < 100) {

                                includebottomsheet_arraivepickup_location!!.visibility =
                                    View.VISIBLE

                                showvalueafterclicknavigator(
                                    service_id,
                                    customerId,
                                    driverId,
                                    pickPlace,
                                    dropPlace,
                                    vehicleType
                                )
                            }

                        }


                    } else if (isStartDriving!!) {

                        //driver way to destination
                        mCurrLocationMarker = mMap!!.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    dest_latFromList!!,
                                    dest_longFromList!!
                                )
                            ).icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))
                                .title("Marker")
                        )
                        drawRoute(
                            location.getLatitude()!!,
                            location.getLongitude()!!,
                            dest_latFromList!!,
                            dest_longFromList!!
                        )

                    }
                    else if (isArrivingAtDestination!!) {

                        mMap!!.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    dest_latFromList!!,
                                    dest_longFromList!!
                                )
                            ).icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))
                                .title("Marker")
                        )
                        drawRoute(
                            location.getLatitude()!!,
                            location.getLongitude()!!,
                            dest_latFromList!!,
                            dest_longFromList!!
                        )

                        distanceCalculate(
                            location.getLatitude()!!,
                            location.getLongitude()!!,
                            dest_latFromList!!,
                            dest_longFromList!!
                        ).let { itDist ->

                            Log.v("distbuttonArrivedtoDest", "" + itDist)
                            if (itDist > 0.0) {
                                // if (itDist < 200) {
                                // if (itDist < 1000) {

                                if (mPolyline != null) {
                                    mPolyline!!.remove()
                                }

                                view_pouseservice!!.visibility = View.VISIBLE
                                iv_arrivedtoDest_navigate!!.visibility = View.VISIBLE
                                iv_navigateArrived!!.visibility = View.VISIBLE
                                tv_navigateArrived!!.visibility = View.VISIBLE
                                // tv_pouseservice!!.visibility = View.GONE
                                constraintLayoutForArraived!!.visibility = View.VISIBLE
                                iv_menusymbol!!.visibility = View.VISIBLE

                                //  btn_arrivedToDestReach!!.visibility = View.VISIBLE
                                btn_arrivedToDest!!.visibility = View.VISIBLE
                                btn_arrivedToDest!!.outerColor = ContextCompat.getColor(requireContext(), R.color.red)
                                btn_arrivedToDest!!.isLocked = false

                                tv_dropofflocation_!!.setText(dropPlace)

                                //  }
                                /* else {

                                    constraintLayoutForDropOff!!.visibility = View.GONE
                                    constraintLayoutForArraived!!.visibility = View.VISIBLE
                                    view_pouseservice!!.visibility = View.VISIBLE
                                    //     tv_pouseservice!!.visibility = View.VISIBLE
                                    iv_menusymbol!!.visibility = View.VISIBLE
                                    tv_dropofflocation_!!.visibility = View.VISIBLE
                                    tv_dropofflocation_!!.setText(dropPlace)

                                     }*/
                            }

                        }

                    } else {
                        //driver reach to customrt

                        if (pick_latFromList!! > 0.0) {
                            mCurrLocationMarker = mMap!!.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        pick_latFromList!!,
                                        pick_longFromList!!
                                    )
                                ).icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin))
                                    .title("Marker")
                            )
                            drawRoute(
                                location.getLatitude()!!,
                                location.getLongitude()!!,
                                pick_latFromList!!,
                                pick_longFromList!!
                            )

                        }

                    }

                }

                driverTrigger(location)
            }

        }

        //move map camera

        //move map camera
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

        try {

            val geocoder: Geocoder
            val addresses: List<Address>
            val activity: Activity? = activity
            if (activity != null && isAdded()) {
                geocoder = Geocoder(activity, Locale.getDefault())

                addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                city = addresses[0].getLocality()

            }



            myEdit = sharedPreferences!!.edit()
            myEdit!!.putString("idSharedPrefAfterLoglat", location.latitude.toString())
            myEdit!!.putString("fnameSharedPrefAfterLoglongitude", location.longitude.toString())
            myEdit!!.putString("lnameSharedPrefAfterLogcity", city)
            myEdit!!.apply()


        }catch (e:java.lang.Exception){

            e.printStackTrace()
        }


    }

    private fun drawRoute(
        cLat: Double,
        cLong: Double,
        pickLatfromlist: Double,
        pickLongfromlist: Double
    ) {

        // Getting URL to the Google Directions API
        val url: String = getDirectionsUrl(LatLng(cLat /*22.55028*/,cLong /*88.39181*/), LatLng(pickLatfromlist,pickLongfromlist))
        url.let {itUrl->

            val downloadTask = DownloadTask()

            // Start downloading json data from Google Directions API
            downloadTask.execute(itUrl)
        }
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude

        // Key
        val key = "key=" + getString(R.string.google_maps_key)

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$key"

        // Output format
        val output = "json"

        // Building the url to the web service
        return NetworkUtility.MAP_DIRECTIONS_BASE_URL+"/"+output+parameters
    }

    private class DownloadTask :
        AsyncTask<String?, Void?, String>() {
        // Downloading data in non-ui thread

        // Executes in UI thread, after the execution of
        // doInBackground()
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parserTask = ParserTask()

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result)
        }

        override fun doInBackground(vararg url: String?): String {


            // For storing data from web service
            var data = ""
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]!!)
                var jsonObject = JSONObject()
                try {
                    jsonObject = JSONObject(data)
                    Log.d("DownloadTaskjsonObject", "jsonObject : $jsonObject")
                    val array = jsonObject.getJSONArray("routes")
                    Log.d("DownloadTaskarray", "array : $array")
                    val routes = array.getJSONObject(0)
                    val legs = routes.getJSONArray("legs")
                    val steps = legs.getJSONObject(0)
                    val distance = steps.getJSONObject("distance")
                    Log.i("Distance", distance.toString())
                    dist = distance.getString("text").replace("[^\\.0123456789]".toRegex(), "")
                        .toDouble()
                    Log.i("distconfirm", dist.toString())
                    // tv_km.setText(String.valueOf(dist));
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                Log.d("DownloadTask", "DownloadTask : $data")
            } catch (e: java.lang.Exception) {
                Log.d("Background Task", e.toString())
            }
            return data
        }

        fun downloadUrl(strUrl: String): String {
            var data = ""
            var iStream: InputStream? = null
            var urlConnection: HttpURLConnection? = null
            try {
                val url = URL(strUrl)

                // Creating an http connection to communicate with url
                urlConnection = url.openConnection() as HttpURLConnection

                // Connecting to url
                urlConnection.connect()

                // Reading data from url
                iStream = urlConnection!!.inputStream
                val br = BufferedReader(InputStreamReader(iStream))
                val sb = StringBuffer()
                var line: String? = ""
                while (br.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                data = sb.toString()
                br.close()
            } catch (e: java.lang.Exception) {
                Log.d("Exception on download", e.toString())
            } finally {
                iStream!!.close()
                urlConnection!!.disconnect()
            }
            return data
        }

    }


    class ParserTask :
        AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {
        // Parsing the data in non-ui thread

        // Executes in UI thread, after the parsing process
        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            var points: ArrayList<LatLng?>? = null
            var lineOptions: PolylineOptions? = null

            try {
                // Traversing through all the routes
                for (i in result!!.indices) {
                    points = ArrayList()
                    lineOptions = PolylineOptions()

                    // Fetching i-th route
                    val path = result[i]

                    // Fetching all the points in i-th route
                    for (j in path.indices) {
                        val point = path[j]
                        val lat = point["lat"]!!.toDouble()
                        val lng = point["lng"]!!.toDouble()

                        //  String distance = (point.get("distance"));
                        val position = LatLng(lat, lng)
                        points.add(position)
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points)
                    lineOptions.width(8f)
                    lineOptions.color(Color.RED)
                }

                // Drawing polyline in the Google Map for the i-th route
                if (lineOptions != null) {
                    /* with(mPolyline){
                         if (this)
                     }*/
                    /*  if (mPolyline != null) {
                          mPolyline?.remove()
                      }*/
                    mPolyline = mMap?.addPolyline(lineOptions)
                } else {
                    /* Toast.makeText(act, "No route is found", Toast.LENGTH_LONG)
                         .show()*/
                }
            }catch (e:java.lang.Exception){

                e.printStackTrace()
            }

        }

        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null
            try {
                jObject = JSONObject(jsonData[0])
                val parser = DirectionsJSONParser()

                // Starts parsing data
                routes = parser.parse(jObject)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return routes
        }
    }

    var speed=0.0
    fun driverTrigger(location: Location) {

        if (location.hasSpeed()) {

            speed = location.speed * 3.6
            // Toast.makeText(requireActivity(), " speed " + speed, Toast.LENGTH_SHORT).show()

        }
        when {
            speed < 2 -> {

                distance = 10.0

            }
            speed <3 && speed >= 2 -> {

                distance = 20.0

            }
            speed <4 && speed >= 3 -> {

                distance = 30.0
            }
            speed <5 && speed >= 4 -> {

                distance = 40.0
            }
            speed >= 5 -> {

                distance = 50.0
            }

        }
        if (p_lat == 0.0 && p_long == 0.0) {

            p_lat = location.latitude /*22.542701*/
            p_long = location.longitude /*88.3855421*/


        } else {

            distanceCalculate(p_lat!!, p_long!!, location.latitude, location.longitude).let {itDist->

                // Toast.makeText(requireActivity(), ""+itDist+" aprox dist "+distance, Toast.LENGTH_SHORT).show()
                if (itDist > distance) {

                    Log.v("itDist","itDist"+itDist)
                    //  Toast.makeText(requireActivity(), "Puja"+itDist+"approx"+distance, Toast.LENGTH_SHORT).show()

                    p_lat = location.latitude
                    p_long = location.longitude

                    try {

                        val req = HashMap<String, Any>()
                        val gson = Gson()

                        /* if (defaultIdLog.equals("")) {

                             req["id"] = defaultIdReg
                         } else {

                             req["id"] = defaultIdLog

                         }
                         req["driver_long"] = location.longitude
                         req["driver_lat"] = location.latitude
                         req["vehicle_type"] = sharedPreferences!!.getString("vehicle_type", "")!!*/

                        req["id"] = service_id
                        req["driver_long"] = location.longitude
                        req["driver_lat"] =location.latitude
                        req["status"] = driver_updatestatus!!


                        val final = gson.toJson(req)
                        try {

                            if (driver_status .equals("On Call")) {
                                channel.trigger("client-order-progress-status", final)
                            }

                        }catch (e:Exception){

                        }

                        if (isAccepted!!) {

                            val req1 = HashMap<String, Any>()
                            val gson1 = Gson()

                            req1["id"] = service_id
                            req1["driver_long"] = location.longitude
                            req1["driver_lat"] =location.latitude
                            req1["status"] = driver_updatestatus!!


                            val final1 = gson1.toJson(req1)
                            try {
                                channel.trigger("client-order-progress-status", final1)

                            }catch (e:Exception){

                            }
                            Log.v("triggerdone", "" + itDist)

                        }else{

                        }

                    }
                    catch (e: Exception) {

                        e.printStackTrace()

                    }
                } else {

                    //  Toast.makeText(requireActivity(), "Puja No Trigger"+itDist, Toast.LENGTH_SHORT).show()

                    Log.v("triggernotdone", "" + itDist)
                }
            }

        }
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {


        Log.v("onConnectedp","onConnectedP")

        mLocationRequest = LocationRequest.create()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY


        val activity: Activity? = activity
        if (activity != null && isAdded()) {

            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {

                if (mGoogleApiClient!=null && mGoogleApiClient!!.isConnected()) {

                    //your code

                    if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps()

                    }
                    /*  else if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                  //  getLocation()
                }
*/
                    try {
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                            mGoogleApiClient!!,
                            mLocationRequest,
                            this
                        )
                        mFusedLocationClient!!.lastLocation.addOnSuccessListener { location: Location? ->
                            location?.let { it: Location ->

                            } ?: kotlin.run {
                                // Handle Null case or Request periodic location update https://developer.android.com/training/location/receive-location-updates
                            }
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }


                }
            }
        }

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
    fun distance(
        lat1: Float,
        lng1: Float,
        lat2: Float,
        lng2: Float
    ): Float {
        val earthRadius = 6371000.0 //meters
        val dLat = Math.toRadians((lat2 - lat1).toDouble())
        val dLng = Math.toRadians((lng2 - lng1).toDouble())
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1.toDouble())) * Math.cos(
            Math.toRadians(lat2.toDouble())
        ) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c =
            2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return (earthRadius * c).toFloat()
    }

    private fun distanceCalculate(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {


        val loc1 = Location("")
        loc1.latitude = lat1
        loc1.longitude = lon1

        val loc2 = Location("")
        loc2.latitude = lat2
        loc2.longitude = lon2

        val distanceInMeters = loc1.distanceTo(loc2)

        /* if ((loc1.time - loc2.time) >0) {
             val speed = distanceInMeters / (loc1.time - loc2.time)
             Toast.makeText(requireContext(), speed.toString(), Toast.LENGTH_SHORT).show()
            // Log.v("speed1", "speed1" + speed)
         }*/
        //
        return  distanceInMeters.toDouble()

    }

    private fun distanceCalculateinkm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return (dist)
        // return dist / 0.62137
    }


    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    private fun starServiceFunc(){
        mLocationService = LocationService()
        mServiceIntent = Intent(requireActivity(), mLocationService.javaClass)
        if (!Util.isMyServiceRunning(mLocationService.javaClass, requireActivity())) {

            requireActivity().startService(mServiceIntent)

           // Toast.makeText(requireActivity(), getString(R.string.service_start_successfully), Toast.LENGTH_SHORT).show()

        } else {
          //  Toast.makeText(requireActivity(), getString(R.string.service_already_running), Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopServiceFunc(){
        mLocationService = LocationService()
        mServiceIntent = Intent(requireActivity(), mLocationService.javaClass)
        if (Util.isMyServiceRunning(mLocationService.javaClass, requireActivity())) {
            requireActivity().stopService(mServiceIntent)
           // Toast.makeText(requireActivity(), "Service stopped!!", Toast.LENGTH_SHORT).show()
        } else {
           // Toast.makeText(requireActivity(), "Service is already stopped!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleApiClient = GoogleApiClient.Builder(requireActivity())
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
        mGoogleApiClient!!.connect()




    }


    override fun onStop() {
        super.onStop()

        handler.removeCallbacksAndMessages(null)

    }



    @SuppressLint("MissingPermission")
    private fun getLastLocation(service_type: String) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener(OnCompleteListener<Location?>(){
                    val location =  it.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {

                        if (mMap!=null) {

                            mMap!!.clear()
                        }
                        if (mCurrLocationMarker != null) {
                            mCurrLocationMarker!!.remove()
                        }

                        if (location!=null && location.getLatitude()!=null && location.getLongitude()!=null && mMarkerOptions!=null) {
                            val latLng = LatLng(location.getLatitude(), location.getLongitude())
                            //Place current location marker
                            //Place current location marker
                            mMarkerOptions?.position(latLng)
                            mMarkerOptions?.title("Current Position")
                            mMarkerOptions?.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrowloc))
                            mMarkerOptions?.anchor(0.5f, 0.5f)
                            //  mMap!!.addMarker(markerOptions)
                            mCurrLocationMarker = mMap!!.addMarker(mMarkerOptions!!)

                            println("puja : splashscreen location : getLatitude : " + location.latitude)
                            println("puja : splashscreen location : getLongitude : " + location.longitude)
                            //   Toast.makeText(activity, "lastlocation"+location.latitude + ", lon=" + location.latitude, Toast.LENGTH_LONG).show()

                            onLocationChanged(location)

                            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F))

                            c_lat = location.latitude
                            c_long = location.longitude

                            val startPoint = Location("locationA")
                            startPoint.latitude = c_lat!!
                            startPoint.longitude = c_long!!

                            getUserDetails(service_type)

                        }
                        //   getUserDetails()

                    }
                }
                )

            }
            else {
                Toast.makeText(requireActivity(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    fun getUserDetails(service_type: String) {

        try {

            /*gb start*/
            //load process show 2-times when customer book a service...so i close the  progressBar here

          /*  AppProgressBar.openLoader(
                requireActivity(),
                this.getResources().getString(R.string.pleasewait)
            )*/

            /*gb end*/

            val apiservice: ApiInterface = ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()

            if (defaultIdLog.equals("")){

                req["driver_id"] = defaultIdReg
                response1 = apiservice.getuserdetails(tokenReg,req )

            }

            else {
                req["driver_id"] = defaultIdLog
                response1 = apiservice.getuserdetails(token,req )

            }
            response1!!.enqueue(object : Callback<UserDetailPojo?> {
                override fun onResponse(
                    call: Call<UserDetailPojo?>,
                    response: Response<UserDetailPojo?>
                ) {
                    if (response.body()==null){
                        AppProgressBar.closeLoader()
                        return
                    }else if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status == true) {

                            if (!response.body()!!.detail!!.service_id.equals("")|| !response.body()!!.detail!!.service_id.equals("0")){

                                Log.v("getuserdetails","")
                                getDriverDetails( response.body()!!.detail!!.service_id!!)

                            }else{


                            }


                        }

                        else {

                            AppProgressBar.closeLoader()



                        }
                    } else {
                        AppProgressBar.closeLoader()

                    }
                }

                override fun onFailure(call: Call<UserDetailPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()

                    val activity: Activity? = activity
                    if (activity != null && isAdded()) {

                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                requireActivity(),
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
        }
        catch (e: Exception) {
            e.printStackTrace()
        }


    }


    @SuppressLint("MissingPermission")
    fun requestNewLocationData() {

        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            /*      latTextView.setText(mLastLocation.getLatitude()+"");
            lonTextView.setText(mLastLocation.getLongitude()+"");*/println("Sukdev : splashscreen location : getLatitude : " + mLastLocation!!.latitude)
            println("Sukdev : splashscreen location : getLongitude : " + mLastLocation!!.longitude)

            mMap!!.clear()
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker!!.remove()
            }
            //Place current location marker
            //Place current location marker
            val latLng = LatLng(mLastLocation!!.getLatitude(), mLastLocation!!.getLongitude())
            mMarkerOptions?.position(latLng)
            mMarkerOptions?.title("Current Position")
            mMarkerOptions?.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrowloc))
            mMarkerOptions?.anchor(0.5f, 0.5f)
            //  mMap!!.addMarker(markerOptions)
            mCurrLocationMarker = mMap!!.addMarker(mMarkerOptions!!)
        }
    }



    fun buildAlertMessageNoGps() {

        /* val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
         builder.setMessage("Please Turn ON your GPS Connection")
             .setCancelable(false)
             .setPositiveButton("Yes", object : View.OnClickListener {
                 fun onClick(dialog: DialogInterface?, id: Int) {
                     startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                 }


             })
             .setNegativeButton("No", object : OnClickListener() {
                 fun onClick(dialog: DialogInterface, id: Int) {
                     dialog.cancel()
                 }
             })
         val alert: AlertDialog = builder.create()
         alert.show()*/
    }

    fun clickOnPinView(edt_password1 :EditText ,edt_password2:EditText,edt_password3:EditText,edt_password4:EditText) {

        edt_password1.disableCopyPaste()
        edt_password2.disableCopyPaste()
        edt_password3.disableCopyPaste()
        edt_password4.disableCopyPaste()


        //edit text1
        edt_password1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s?.length == 1){

                    edt_password2.requestFocus()
                }
            }
        })

        //edit text2
        edt_password2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s?.length == 1){

                    edt_password3.requestFocus()
                }else {

                    edt_password1.requestFocus()
                }
            }
        })

        //edit text1
        edt_password3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s?.length == 1){

                    edt_password4.requestFocus()
                }else {
                    edt_password2.requestFocus()
                }
            }
        })

        //edit text1
        edt_password4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s?.length == 0){

                    edt_password3.requestFocus()
                }
            }
        })




    }

    override fun onSensorChanged(p0: SensorEvent?) {



    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }



    fun checkLocationSetting() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.apply {
            this!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            fastestInterval = 2000
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        builder.setAlwaysShow(true)

        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(requireActivity().applicationContext)
                .checkLocationSettings(builder.build())

        result.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)
                Toast.makeText(requireActivity(), "GPS is On", Toast.LENGTH_SHORT).show()
                Log.d("", "checkSetting: GPS On")
            } catch (e: ApiException) {

                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        val resolvableApiException = e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(
                            requireActivity(),
                            REQUEST_CHECK_SETTING
                        )
                        Log.d("", "checkSetting: RESOLUTION_REQUIRED")
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // USER DEVICE DOES NOT HAVE LOCATION OPTION
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        mapView!!.onResume()

        Log.v("resumecall","")
        if (checkPermissions()) {

            if (NetworkUtility.isNetworkAvailable(requireActivity())) {
                callServiceCallAPI()
            }
            else{
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.msg_no_internet),
                    Toast.LENGTH_LONG
                ).show()

            }

            //getLastLocation()

            if (NetworkUtility.isNetworkAvailable(requireActivity())) {
                getAboutUsList()
            }
            else{
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.msg_no_internet),
                    Toast.LENGTH_LONG
                ).show()
            }

          /*  if (NetworkUtility.isNetworkAvailable(requireActivity())) {
                callServiceCallAPI()
            }
            else{
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.msg_no_internet),
                    Toast.LENGTH_LONG
                ).show()

            }*/

        }

        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled(requireContext())) {



            } else {

                Toast.makeText(
                    requireActivity(),
                    "Please turn on" + " your location...",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

        searchingForSecondaryDriverLayout!!.visibility
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver((receiver!!),
            IntentFilter("com.muve.muve_it_driver")
        )
    }

    fun getAboutUsList() {

        try {
            // AppProgressBar.openLoader(activity, resources.getString(R.string.pleasewait))
            val apiservice: ApiInterface = ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
            val response1: Call<AboutUsPojo> = apiservice.getfirst_service_call()
            response1.enqueue(object : Callback<AboutUsPojo> {
                override fun onResponse(
                    call: Call<AboutUsPojo>,
                    response: Response<AboutUsPojo>
                ) {
                    AppProgressBar.closeLoader()
                    if (response.isSuccessful) {

                        AppProgressBar.closeLoader();
                        if (response.body()!!.status == true) {

                            customer_waiting_time = response.body()!!.customer_waiting_time



                        } else {


                            //  AppProgressBar.closeLoader()


                            if (response.body()!!.details.equals(
                                    resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                    ignoreCase = true
                                )
                            ) {


                            } else {



                            }


                        }
                    }
                    else{

                        //  AppProgressBar.closeLoader()


                    }
                }

                override fun onFailure(call: Call<AboutUsPojo>, t: Throwable) {
                    // AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            requireActivity(),
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else{

                        Toast.makeText(
                            requireActivity(),
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

   /* fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = requireActivity().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true))
                if (ni.isConnected)
                    haveConnectedWifi = true
            if (ni.typeName.equals("MOBILE", ignoreCase = true))
                if (ni.isConnected)
                    haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }*/

    fun checkNetworkStatus(context: Context): String? {
        var networkStatus = ""

        // Get connect mangaer
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // check for wifi
        val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        // check for mobile data
        val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        networkStatus = if (wifi!!.isAvailable) {
            "wifi"
        } else if (mobile!!.isAvailable) {
            "mobileData"
        } else {
            "noNetwork"
        }
        return networkStatus
    }



}