package com.muve.muve_it_driver.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtility {

    companion object {
        //Client ...

        //Local
        // const val baseUrl = "http://192.168.3.167:8000/driver/" //local url
        // const val baseUrlForPusher = "http://192.168.3.60:8555/driver/" //local pusher base url
        // const val authUrl = "http://192.168.3.60:8555/pusher/auth" // pusher auth url local

        //DEVELOPEMENT

         const val baseUrl = "http://3.99.2.158:3308/driver/" //dev url
         const val baseUrlForPusher = "http://3.99.2.158:8555/driver/" //dev pusher base url
         const val authUrl = "http://3.99.2.158:8555/pusher/auth" // pusher auth url dev

        //*******LIVE*******//
         //const val baseUrl = "https://muve-it-api.com/driver/" //LIVE url
       //  const val baseUrlForPusher = "https://muve-it-api2.com/driver/" //live pusher base url
        // const val authUrl = "https://muve-it-api2.com/pusher/auth" // pusher auth url live


        const val imgbaseUrl = "https://muveit.s3.amazonaws.com"
        const val getLocationIdUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?"
        const val getLocationLatLongUrl = "https://maps.googleapis.com/maps/api/place/details/json?"

        const val TERMS_CONDITIONS_URL = "https://www.muve-it.com/terms-and-conditions-customers"
        const val TERMS_CONDITIONS_HAULERS_URL =
            "https://www.muve-it.com/terms-and-conditions-haulers"
        const val HAULERS_URL = "https://www.muve-it.com/haulers"
        const val MAP_DIRECTIONS_BASE_URL = "https://maps.googleapis.com/maps/api/directions/"

        const val PRIVACY_URL = "https://www.muve-it.com/privacy"
        const val HOW_IT_WORKS = "https://info1899332.wixsite.com/muve-it/how-it-works"
        const val DRIVER_TEST_URL = "https://5h1k6pjt0mo.typeform.com/to/TWy7jsGR"
        const val ONLINE_POLICE_URL = "https://tritoncanada.ca/online-police-checks/"

        /* *** -- live  pusher key  ---- ****/
       /*  const val app_id = "1441291"
         const val key = "f2cbe73f0ec64c065529"
         const val secret = "be1c322f400a25bccca9"
         const val cluster = "us2"*/

       /*  *** -- dev  pusher key  ---- ****/
        const val app_id = "1477722"
        const val key = "efaffd251ad7104c2ddf"
        const val secret = "4c06784a747444c7052d"
        const val cluster = "us2"

        //API const variable
        const val getCountryCallingCode = "getCountryCallingCode"
        const val getfirst_service_call = "first_service_call"
        const val registration = "registration"
        const val verifyotp = "verifyOTP"
        const val resentOtp = "resendAPI"
        const val editDetails = "editDetails"
        const val login = "login"
        const val getforceupdate = "forced_device_update"
        const val forgotpasswordsendotp = "forgot_password_send_otp"
        const val changepassword = "forgot_password_change"
        const val getuserdetails = "getuserdetails"
        const val editprofile = "editprofile"
        const val driver_information = "driver_information"
        const val driver_vehicle_information = "driver_vehicle_information"
        const val addaddress = "addfavouritelocation"
        const val deletefavouritelocation = "deletefavouritelocation"
        const val imageupload = "upload_image"
        const val servicecall_accept = "get_servicecall"
        const val driver_service_call_accept = "driver_service_call_accept"
        const val servicecall_details = "servicecall_details"
        const val servicecall_arrived = "arrived"
        const val verifycode = "verifycode"
        const val arriveddestination = "arriveddestination"
        const val servicecallcomplete = "servicecallcomplete"
        const val rating = "rating"
        const val servicecall_history_list = "servicecall_history_list"
        const val servicecallpause = "servicecallpause"
        const val cancelled = "cancelled"
        const val startdriving = "startdriving"
        const val deleteaccount = "checkdelete"
        const val driver_status_send = "driver_status_send"
        const val help = "help"
        const val chat_messaging = "chat_messaging"
        const val notification = "notification"
        const val earning = "earning"
        const val add_bank_account_details = "add_bank_account_details"
        const val servicecall_decline = "servicecall_decline"
        const val secondservicecallcomplete = "secondservicecallcomplete"
        const val emergency_cancelled = "emergency_cancelled"
        const val attempt_status = "attempt_status"


        fun isNetworkAvailable(context: Context): Boolean {
            val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netWInfo = conMgr.activeNetworkInfo
            return netWInfo != null && netWInfo.isAvailable && netWInfo.isConnected
        }


    }



}