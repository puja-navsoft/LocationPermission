package com.muve.muve_it_driver.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.muve.muve_it_driver.model.UserResponse;
import com.muve.muve_it_driver.model.aboutusmodel.DataItemAbout;
import com.muve.muve_it_driver.model.aboutusmodel.cityDataItemAbout;
import com.muve.muve_it_driver.model.countrymodel.DataItem;
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPreferenceManager {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "muve_it";


    private static final String IS_LOGIN = "IsLoggedIn";

    public String key_name = "name";
    public String key_id = "id";
    public String key_firstName = "first_name";
    public String key_LastName = "last_name";
    public String key_email = "email";
    public String key_mobile = "mobile";
    public String key_mobile_code = "mobile_code";
    public String key_profile_pic = "profile_pic";
    public String key_token = "token";
    public String REMEMBER_DATA = "user";
    public String COUNTRY_LIST = "country_list";
    public String ABOUT_LIST = "about_list";
    public String ABOUT_CITY= "about_city";
    public SharedPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void storeUserProfileDetail(String id ,String argFirstName,String argLastName, String argEmail,
                                       String argCode, String phone, String argToken) {

        editor.putString(key_id, id);
        editor.putString(key_firstName, argFirstName);
        editor.putString(key_LastName, argLastName);
        editor.putString(key_email, argEmail);
        editor.putString(key_mobile_code, argCode);
        editor.putString(key_mobile, phone);
        editor.putString(key_token, argToken);
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public String getUserProfileDetail(String argWhat) {

        String returnResult = "";
        switch (argWhat) {
            case "name":
                returnResult = pref.getString(key_name, "");
                break;
            case "email":
                returnResult = pref.getString(key_email, "");
                break;
            case "mobile":
                returnResult = pref.getString(key_mobile, "");
                break;
            case "profile_pic":
                returnResult = pref.getString(key_profile_pic, "");
                break;
            case "token":
                returnResult = pref.getString(key_token, "");
                break;


        }
        return returnResult;
    }


    public void saveLoginUserData(UserResponse userResponse) {
        Gson gson = new Gson();
        String json = gson.toJson(userResponse);
        editor.putString(REMEMBER_DATA, json);
        editor.apply();
    }

    private void clearLoginDetail() {
        editor.putBoolean(IS_LOGIN, false);
    //    editor.putString(Constants.SHARED_PREFERENCE_KEYS.LOGIN_DETAIL, "");

        editor.putString(REMEMBER_DATA, "");
        editor.commit();
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
       /* editor.clear();
        editor.commit();*/

        clearLoginDetail();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, WelcomeActivity.class);
        // Closing all the Activities
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }
    public UserResponse getLoginUserData() {
        Gson gson = new Gson();
        String json = pref.getString(REMEMBER_DATA, null);
        Type type = new TypeToken<UserResponse>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public List<DataItem> getCountryList() {
        Gson gson = new Gson();
        String json = pref.getString(COUNTRY_LIST, null);
        Type type = new TypeToken<List<DataItem>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public List<DataItemAbout> getAboutList() {
        Gson gson = new Gson();
        String json = pref.getString(ABOUT_LIST, null);
        Type type = new TypeToken<List<DataItemAbout>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public List<cityDataItemAbout> getPreferedCity() {
        Gson gson = new Gson();
        String json = pref.getString(ABOUT_CITY, null);
        Type type = new TypeToken<List<cityDataItemAbout>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void saveAboutList(List<DataItemAbout> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(ABOUT_LIST, json);
        editor.apply();
    }


    public void saveCityList(List<cityDataItemAbout> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(ABOUT_CITY, json);
        editor.apply();
    }


    public void saveCountryList(List<DataItem> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(COUNTRY_LIST, json);
        editor.apply();
    }
   /* public void saveCustPhno(String cust_phno) {
        editor.putString("cust_phno", cust_phno);
        editor.apply();
    }

    public String getCustPhno() {
        return pref.getString("cust_phno", "");
    }
*/
    public void saveCustFirstname(String cust_fname) {
        editor.putString("cust_phno", cust_fname);
        editor.apply();
    }

    public String getCustFirstname() {
        return pref.getString("cust_fname", "");
    }

    public void saveCustLastname(String cust_lname) {
        editor.putString("cust_lname", cust_lname);
        editor.apply();
    }

    public String getCustLastname() {
        return pref.getString("cust_lname", "");
    }

    public void saveCustemail(String cust_email) {
        editor.putString("cust_email", cust_email);
        editor.apply();
    }
    public String getCustCustemail() {
        return pref.getString("cust_email", "");
    }

    public void saveCustPhone(String cust_phone) {
        editor.putString("cust_phone", cust_phone);
        editor.apply();
    }

    public String getCustPhone() {
        return pref.getString("cust_phone", "");
    }

 public void saveCustcode(String cust_code) {
        editor.putString("cust_code", cust_code);
        editor.apply();
    }

    public String getCustcode() {
        return pref.getString("Custcode", "");
    }

    public void saveCustPromo(String cust_promo) {
        editor.putString("cust_promo", cust_promo);
        editor.apply();
    }

    public String getCustPromo() {
        return pref.getString("cust_promo", "");
    }

    public void saveCustiphoneUpdateSharedPref(String iphoneUpdateSharedPref) {
        editor.putString("iphoneUpdateSharedPref", iphoneUpdateSharedPref);
        editor.apply();
    }

    public String getCustiphoneUpdateSharedPref() {
        return pref.getString("iphoneUpdateSharedPref", "");
    }

    public void saveemailUpdateSharedPref(String emailUpdateSharedPref) {
        editor.putString("emailUpdateSharedPref", emailUpdateSharedPref);
        editor.apply();
    }

    public String getemailUpdateSharedPref() {
        return pref.getString("emailUpdateSharedPref", "");
    }


    public void saveCustiphoneCodeUpdateSharedPref(String iphoneCodeUpdateSharedPref) {
        editor.putString("iphoneCodeUpdateSharedPref", iphoneCodeUpdateSharedPref);
        editor.apply();
    }

    public String getCustiphoneCodeUpdateSharedPref() {
        return pref.getString("iphoneCodeUpdateSharedPref", "");
    }

    public void storeRegId(int regID) {
        editor.putInt("regID", regID);
        editor.apply();
    }
    public int getRegId() {
        return pref.getInt("deviceID",0);
    }

    public void clearall() {
        editor.clear().apply();
    }

}
