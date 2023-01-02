package com.muve.muve_it_driver.restapi;

import com.muve.muve_it.CancelResponse;
import com.muve.muve_it.CompleteSecondaryService;
import com.muve.muve_it.Emargencycancel;
import com.muve.muve_it_driver.Driverstatusstoreresponse;
import com.muve.muve_it_driver.ImageUploadResponse;
import com.muve.muve_it_driver.UserDetailPojo.UserDetailPojo;
import com.muve.muve_it_driver.model.aboutusmodel.AboutUsPojo;
import com.muve.muve_it_driver.model.addaddress.AddaddressResponse;
import com.muve.muve_it_driver.model.chat.ChatListResponse;
import com.muve.muve_it_driver.model.countrymodel.CountryPojo;
import com.muve.muve_it_driver.model.criminalrecord.CriminalRecordResponse;
import com.muve.muve_it_driver.model.driverinformation.DriverInfoPojo;
import com.muve.muve_it_driver.model.earninglist.EarningListResponse;
import com.muve.muve_it_driver.model.editprofile.EditProfilePojo;
import com.muve.muve_it_driver.model.forceupdate.ForceupdatePojo;
import com.muve.muve_it_driver.model.loginmodel.LoginPojo;
import com.muve.muve_it_driver.model.notificationlist.NotificationListResponse;
import com.muve.muve_it_driver.model.registrationmodel.RegistrationPojo;
import com.muve.muve_it_driver.model.resendotp.ResendOtpPojo;
import com.muve.muve_it_driver.model.servicecallAcceptByDriver.ServiceCallAcceptByDriverResponse;
import com.muve.muve_it_driver.model.servicecallaccept.ServicecallResponse;
import com.muve.muve_it_driver.model.servicecallcomplete.ServiceCallCompleteResponse;
import com.muve.muve_it_driver.model.servicecalllisting.ServiceCallListingResponse;
import com.muve.muve_it_driver.model.servicecallstatuswisedetails.ServiceCallStatuswiseetailsResponse;
import com.muve.muve_it_driver.model.verifyotp.VerifyOtpPojo;
import com.muve.muve_it_driver.util.NetworkUtility;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface ApiInterface {

    @GET(NetworkUtility.getCountryCallingCode)
        // to get all categories list like Piza,Burger,etc
    Call<CountryPojo> getCountryList();


    @POST(NetworkUtility.getuserdetails)
    @FormUrlEncoded
    Call<UserDetailPojo> getuserdetails(@Header("Authorization") String token, @FieldMap Map<String, Object> req);

    @POST(NetworkUtility.getforceupdate)
    @FormUrlEncoded
    Call<ForceupdatePojo> getforceupdate(@FieldMap Map<String, Object> req);


    @GET(NetworkUtility.getfirst_service_call)
    Call<AboutUsPojo> getfirst_service_call();


    @POST(NetworkUtility.registration)
    Call<RegistrationPojo> doingRegistration(@Body Map<String, Object> req);

    @POST(NetworkUtility.verifyotp)
    Call<VerifyOtpPojo> doingVerifyOTP(@Body Map<String, Object> req);

    @POST(NetworkUtility.help)
    Call<VerifyOtpPojo> doingHelpFunctionality(@Header("Authorization") String token,@Body Map<String, Object> req);

    @POST(NetworkUtility.notification)
    Call<NotificationListResponse> getnotification(@Header("Authorization") String token, @Body Map<String, Object> req);

    @POST(NetworkUtility.earning)
    Call<EarningListResponse> getearning(@Header("Authorization") String token, @Body Map<String, Object> req);

    @POST(NetworkUtility.add_bank_account_details)
    Call<CriminalRecordResponse> getadd_bank_account_details(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.editprofile)
    Call<EditProfilePojo> doingEditProfile(@Header("Authorization") String token,@Body Map<String, Object> req);

    @POST(NetworkUtility.driver_information)
    Call<DriverInfoPojo> doingdriverinformation(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.servicecall_details)
    Call<ServiceCallStatuswiseetailsResponse> doingservicecall_detailsinformation(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.driver_status_send)
    Call<Driverstatusstoreresponse> doingdriver_status_send(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.chat_messaging)
    Call<ChatListResponse> doingchat_messaging(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.servicecall_arrived)
    Call<VerifyOtpPojo> doingservicecall_arrived(@Header("Authorization") String token, @Body Map<String, Object> req);

    /* @POST(NetworkUtility.checkdelete)
     Call<VerifyOtpPojo> doingcheckdelete(@Header("Authorization") String token, @Body Map<String, Object> req);
   */
    @POST(NetworkUtility.deleteaccount)
    Call<VerifyOtpPojo> doingdeleteaccount(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.servicecall_decline)
    Call<ResendOtpPojo> doingservicecall_decline(@Header("Authorization") String token, @Body Map<String, Object> req);

    @POST(NetworkUtility.secondservicecallcomplete)
    Call<CompleteSecondaryService> doingsecondservicecallcomplete(@Header("Authorization") String token, @Body Map<String, Object> req);



    @POST(NetworkUtility.emergency_cancelled)
    Call<Emargencycancel> doingemergency_cancelled(@Header("Authorization") String token, @Body Map<String, Object> req);

    @POST(NetworkUtility.attempt_status)
    Call<ResendOtpPojo> doingattempt_status(@Header("Authorization") String token, @Body Map<String, Object> req);

    @POST(NetworkUtility.verifycode)
    Call<VerifyOtpPojo> doingverifycode(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.arriveddestination)
    Call<VerifyOtpPojo> doingarriveddestination(@Header("Authorization") String token, @Body Map<String, Object> req);

    @POST(NetworkUtility.servicecallcomplete)
    Call<ServiceCallCompleteResponse> doingservicecallcomplete(@Header("Authorization") String token, @Body Map<String, Object> req);

    @POST(NetworkUtility.rating)
    Call<VerifyOtpPojo> doingrating(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.servicecall_history_list)
    Call<ServiceCallListingResponse> doingservicecall_history_list(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.servicecallpause)
    Call<VerifyOtpPojo> doingservicecallpause(@Header("Authorization") String token, @Body Map<String, Object> req);

    @POST(NetworkUtility.cancelled)
    Call<CancelResponse> doingservicecallcancelled(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.startdriving)
    Call<VerifyOtpPojo> doingservicecallstartdriving(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.driver_vehicle_information)
    Call<DriverInfoPojo> doingdriver_vehicle_information(@Header("Authorization") String token, @Body Map<String, Object> req);


    @POST(NetworkUtility.servicecall_accept)
    Call<ServicecallResponse> doingservicecall_accept(@Header("Authorization") String token, @Body Map<String, Object> req);

    //socket API
    @POST(NetworkUtility.driver_service_call_accept)
    Call<ServiceCallAcceptByDriverResponse> doingdriver_service_call_accept(@Body Map<String, Object> req);


    @POST(NetworkUtility.imageupload)
    Call<ImageUploadResponse> doingImageUpload(@Header("Authorization") String token, @Body Map<String, Object> req);

    @POST(NetworkUtility.resentOtp)
    Call<ResendOtpPojo> doingResendAPIOTP(@Body Map<String, Object> req);

    @POST(NetworkUtility.login)
    @FormUrlEncoded
    Call<LoginPojo> doingLogin(@FieldMap Map<String, Object> req);


    @PATCH(NetworkUtility.editDetails)
    @Headers({"Content-Type: application/json"})
    Call<ResendOtpPojo> doingEditDetails(@Body Map<String, Object> log);


    @POST(NetworkUtility.forgotpasswordsendotp)
    Call<VerifyOtpPojo> doingforgot_password_send_otp
            (@Body Map<String, Object> req);

    @POST(NetworkUtility.changepassword)
    Call<VerifyOtpPojo> doingPasswordChange
            (@Body Map<String, Object> req);


    @POST(NetworkUtility.addaddress)
    @FormUrlEncoded
    Call<AddaddressResponse> doingaddaddress(@FieldMap Map<String, Object> req);

    @PUT(NetworkUtility.addaddress)
    @FormUrlEncoded
    Call<AddaddressResponse> doingeditaddress(@FieldMap Map<String, Object> req);


    @POST(NetworkUtility.deletefavouritelocation)
    @FormUrlEncoded
    Call<AddaddressResponse> doingdeleteaddress(@FieldMap Map<String, Object> req);

}

// testing
