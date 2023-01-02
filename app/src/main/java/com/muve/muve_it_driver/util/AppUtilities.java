package com.muve.muve_it_driver.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;


public class AppUtilities {
    private static AppUtilities comminInstance = null;
    private Context mContext;
    private static Context context = null;
    private static DecimalFormatSymbols symbolsEN_US;
    private static DecimalFormat df;

    /**
     * Recently set context will be returned.
     * If not set it from current class it will
     * be null.
     *
     * @return Context
     */
    public static final Context getContext() {
        return AppUtilities.context;
    }

    /**
     * First set context from every activity
     * before use any static method of AppUtils class.
     *
     * @param ctx
     */
    public static final void setContext(Context ctx) {
        AppUtilities.context = ctx;
    }

    private AppUtilities() {
    }

    public static AppUtilities getInstance(Context mContext) {
        if (comminInstance == null) {
            comminInstance = new AppUtilities();
        }
        comminInstance.mContext = mContext;
        return comminInstance;
    }

    /**
     * Validate Email
     *
     * @param target
     * @return
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void showSnackBar(View parentLayout, String messege, int length) {

        Snackbar snackbar = Snackbar.make(parentLayout, messege, length);
/*                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));*/

        snackbar.show();
    }

    public static String getColorHexValueAsString(Context context, int color) {
        return String.format("#%06X", (0xFFFFFF & context.getResources().getColor(color)));
    }

    /**
     * Format Date
     *
     * @param date
     * @return
     */
    public static String getFormattedDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        if (!((day > 10) && (day < 19)))
            switch (day % 10) {
                case 1:
                    return new SimpleDateFormat("d'st' MMM yyyy").format(date);
                case 2:
                    return new SimpleDateFormat("d'nd' MMM yyyy").format(date);
                case 3:
                    return new SimpleDateFormat("d'rd' MMM yyyy").format(date);
                default:
                    return new SimpleDateFormat("d'th' MMM yyyy").format(date);
            }
        return new SimpleDateFormat("d'th' MMM yyyy").format(date);
    }

    public static String getFormattedDateGraph(String date) {
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        if (!((day > 10) && (day < 19)))
            switch (day % 10) {
                case 1:
                    return new SimpleDateFormat("d'st' MMM ").format(d);
                case 2:
                    return new SimpleDateFormat("d'nd' MMM ").format(d);
                case 3:
                    return new SimpleDateFormat("d'rd' MMM ").format(d);
                default:
                    return new SimpleDateFormat("d'th' MMM ").format(d);
            }
        return new SimpleDateFormat("d'th' MMM ").format(d);
    }

    public static String getFormattedDateConversation(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthFormatDate = new SimpleDateFormat("MMMM yyyy").format(cal.getTime());
        return monthFormatDate;
    }

    public static String getMonthFormatDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthFormatDate = new SimpleDateFormat("MMM dd,yyyy").format(cal.getTime());
        return monthFormatDate;
    }

    public static String getMonthFormatDateTime(String date) throws ParseException {
        Date d = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthFormatDate = new SimpleDateFormat("EEEE MMM dd, yyyy").format(cal.getTime());
        return monthFormatDate;
    }

    public static String withNumberSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }

    /**
     * Check Online Status
     *
     * @return
     */
    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        /*if(netInfo != null && netInfo.isConnectedOrConnecting()){
            try {
                return isConnected();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isConnected() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    public static String CapsFirst(String str) {
        String[] words = str.split(" ");
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            ret.append(Character.toUpperCase(words[i].charAt(0)));
            ret.append(words[i].substring(1));
            if (i < words.length - 1) {
                ret.append(' ');
            }
        }
        return ret.toString();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboardFragment(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getAddress(Context mContext, double latitude, double longitude) {
        String full_add = null;
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                String postalCode = addresses.get(0).getPostalCode();

                full_add = cityName + "," + stateName + "," + postalCode;
            }
            return full_add;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean checkAllCon(Activity activity) {

        ConnectivityManager mConnectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        // Skip if no connection, or background data disabled
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            return false;
        }

        // Only update if WiFi or 3G is connected and not roaming
        int netType = info.getType();
        int netSubtype = info.getSubtype();

        if (netType == ConnectivityManager.TYPE_WIFI || netType == ConnectivityManager.TYPE_MOBILE) {
            return info.isConnected();
        } else if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming()) {
            return info.isConnected();
        } else {
            return false;
        }
    }

    public static String encodeBase64(String str) {
        byte[] data = new byte[0];
        try {
            data = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }

    public static Bitmap getBitmapFromBase64(String str) {
        byte[] imageBytes = Base64.decode(str, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        decodedImage = Bitmap.createScaledBitmap(decodedImage, 250, 250, false);
        decodedImage.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
        return decodedImage;
    }

    public static Bitmap getBitmapFromBase64Ar(String str) {
        byte[] imageBytes = Base64.decode(str, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        decodedImage = Bitmap.createScaledBitmap(decodedImage, 400, 400, false);
        decodedImage.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
        return decodedImage;
    }

    public static String[] extractLinks(String text) {
        List<String> links = new ArrayList<String>();
        Matcher m = Patterns.WEB_URL.matcher(text);
        while (m.find()) {
            String url = m.group();
            Log.d("TAG", "URL extracted: " + url);
            links.add(url);
        }

        return links.toArray(new String[links.size()]);
    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.getCount();
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static void showEditTextsAsMandatory(EditText... ets) {
        for (EditText et : ets) {
            String hint = et.getHint().toString();

            et.setHint(Html.fromHtml(hint + "<font color=\"#ff0000\">" + " *" + "</font>"));
        }
    }

    public static void showTextViewsAsMandatory(TextView... tvs) {
        for (TextView tv : tvs) {
            String text = tv.getText().toString();

            tv.setText(Html.fromHtml(text + "<font color=\"#ff0000\">" + " *" + "</font>"));
        }
    }

    public static void showSpinnerAsMandatory(Spinner... sps) {
        for (Spinner spinner : sps) {
            ((TextView) spinner.getSelectedView()).setText(Html.fromHtml(((TextView) spinner.getSelectedView()).getText() + "<font color=\"#ff0000\">" + " *" + "</font>"));
        }
    }

    public static DecimalFormat formatDecimal() {
        symbolsEN_US = DecimalFormatSymbols.getInstance(Locale.US);
        df = new DecimalFormat("0.00", symbolsEN_US);
        return df;
    }

    public static void changeLang(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        config.setLayoutDirection(myLocale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        Intent i = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

/*
    public static void setApplicationLanguage(Context mContext, String language) {

        Resources res = RetailApplication.applicationContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(language)); // API 17+ only.
        } else {
            conf.locale = new Locale(language);
        }
        res.updateConfiguration(conf, dm);
    }
*/


    public static void changeLangWithoutRestart(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        config.setLayoutDirection(myLocale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public static String getCurrentDatePrint() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date());
    }

    public static String getCurrentDateWithDay() {
        return new SimpleDateFormat("EEEE MMM,dd yyyy", Locale.getDefault()).format(new Date());
    }

    public static Date convertStringToDate(String s) {
        DateFormat parser = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date date = null;
        try {
            date = (Date) parser.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrentTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return (sdf.format(d));
    }

    public static String getCurrentTimePrint() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.US);
        return (sdf.format(d));
    }

    public static String getCustomFormatDate(String date, String format) {
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthFormatDate = new SimpleDateFormat(format).format(cal.getTime());
        return monthFormatDate;
    }

/*
    public static String[] checkCategoryActiveStatus(List<CategoryItem> apiStatus, String itemID) {
        String[] status = new String[2];
        boolean isExist = false;
        for (int i = 0; i < apiStatus.size(); i++) {
            if (apiStatus.get(i).getId() == Integer.parseInt(itemID)) {
                status[0] = apiStatus.get(i).getIsblocked();
                status[1] = apiStatus.get(i).getIsdeleted();
                isExist = true;
            }
        }
        if (!isExist) {
            status[0] = "y";
            status[1] = "y";
        }
        return status;
    }
*/

/*
    public static String[] checkItemActiveStatus(List<ApiStatusItemList> apiStatus, String itemID) {



        String[] status = new String[2];
        boolean isExist = false;
        for (int i = 0; i < apiStatus.size(); i++) {
            if (apiStatus.get(i).getId() == Integer.parseInt(itemID)) {
                status[0] = apiStatus.get(i).getIsblocked();
                status[1] = apiStatus.get(i).getIsdeleted();
                isExist = true;
            }
        }
        if (!isExist) {
            status[0] = "y";
            status[1] = "y";
        }
        return status;
    }
*/

    public static String convertUTCToNewFormat(String ourDate) throws ParseException {
        try {
            SimpleDateFormat formatter;
            if (ourDate.contains("T") || ourDate.contains("Z")) {
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            //ourDate = "00-00-0000 00:08";
            ourDate = getCurrentDate() + " " + getCurrentTime();
        }
        return ourDate;
    }

    public static String convertUTCToFormat(String ourDate) throws ParseException {
        try {
            SimpleDateFormat formatter;
            if (ourDate.contains("T") || ourDate.contains("Z")) {
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            //ourDate = "00-00-0000 00:08";
            ourDate = getCurrentDate() + " " + getCurrentTime();
        }
        return ourDate;
    }

    public static String convertUTCToDateFormat(String ourDate) throws ParseException {
        try {
            SimpleDateFormat formatter;
            if (ourDate.contains("T") || ourDate.contains("Z")) {
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:08";
        }
        return ourDate;
    }

    public static String convertUTCToNewDateFormat(String ourDate) throws ParseException {
        try {
            SimpleDateFormat formatter;
            if (ourDate.contains("T") || ourDate.contains("Z")) {
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.US);
            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
            }
            //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy",Locale.US); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:08";
        }
        return ourDate;
    }

    public static String convertUTCToNewTimeFormat(String ourDate) throws ParseException {
        try {
            SimpleDateFormat formatter;
            if (ourDate.contains("T") || ourDate.contains("Z")) {
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.US);
            } else {
                formatter = new SimpleDateFormat("HH:mm:ss",Locale.US);
            }
            //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm",Locale.US); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:08";
        }
        return ourDate;
    }

    public static String convertUTCToTimeFormat(String ourDate) throws ParseException {
        try {
            SimpleDateFormat formatter;
            if (ourDate.contains("T") || ourDate.contains("Z")) {
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:08";
        }
        return ourDate;
    }

    public static String convertUTCToDatePrintFormat(String ourDate) throws ParseException {
        try {
            SimpleDateFormat formatter;
            if (ourDate.contains("T") || ourDate.contains("Z")) {
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:08";
        }
        return ourDate;
    }

    public static String convertUTCToTimePrintFormat(String ourDate) throws ParseException {
        try {
            SimpleDateFormat formatter;
            if (ourDate.contains("T") || ourDate.contains("Z")) {
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);

//            SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss"); //this format changeable
            SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:08";
        }
        return ourDate;
    }

    public static String getTimeAfterMinutes(String currentDate, int min) {

        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        try {
            // Get calendar set to current date and time with Singapore time zone
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Calcutta"));
            calendar.setTime(format.parse(currentDate));

            //Set calendar before 10 minutes
            calendar.add(Calendar.MINUTE, min);
            //Formatter
            DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
            return formatter.format(calendar.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static boolean compareTime(String time, String endtime) {

        String pattern = "MM-dd-yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if (date1.before(date2)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    public static String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    public static char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }

    //create bitmap from view and returns it
    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public static HashMap<String, String> convertToHashMap(String jsonString) {
        HashMap<String, String> myHashMap = new HashMap<String, String>();
        if (jsonString != null) {
            try {
                //JSONArray jArray = new JSONArray(jsonString);
                JSONObject jObject = new JSONObject(jsonString);
                for (Iterator<String> it = jObject.keys(); it.hasNext(); ) {
                    String keys = it.next();
                    myHashMap.put(keys, jObject.getString(keys));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return myHashMap;
    }

    public static boolean isMyServiceRunning(Context ctx, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static double roundDecimal(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /*public static ITransAPI transApi;
    static DecimalFormat formatter = new DecimalFormat("#.00");

    public static boolean dosale(Context ctx, String transAmount, String tipAmount) {
        Log.e("transAmount", "" + transAmount);
        Log.e("tipAmount", "" + tipAmount);
        transApi = TransAPIFactory.createTransAPI(); //for init only
        String amtVal = formatter.format(Double.parseDouble(transAmount)).replace(".", "");
        if (AppPreferences.getInstance().getDecimalPoint().equalsIgnoreCase("3")) {
            amtVal = amtVal + "0";
        }else if (AppPreferences.getInstance().getDecimalPoint().equalsIgnoreCase("2")) {
            amtVal = amtVal;
        }
        Log.e("amtVal", "" + Long.parseLong(amtVal));
        doCommConnect(ctx, true);
        SaleMsg.Request request = new SaleMsg.Request();
        request.setCategory(SdkConstants.CATEGORY_SALE);
        request.setAppId("1");
        request.setAmount(Long.parseLong(amtVal));
        request.setTipAmount(Long.parseLong(tipAmount));
        return transApi.startTrans(ctx, request);
    }

    public static boolean doCommConnect(Context ctx, boolean isDefault) {
        CommConnectMsg.Request request = new CommConnectMsg.Request();
        request.setCategory(SdkConstants.CATEGORY_COMM_CONNECT);
        request.setAppId("1");
        request.setDefault(isDefault);
        request.setCommType(CommConnectMsg.Request.USB);
        return transApi.startTrans(ctx, request);
    }*/
}
