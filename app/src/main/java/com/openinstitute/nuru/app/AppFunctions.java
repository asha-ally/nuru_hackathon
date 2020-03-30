package com.openinstitute.nuru.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.openinstitute.nuru.R;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oi-dev-01 on 24/09/17.
 */

public class AppFunctions extends Application {

    Context context;
    private static String result;
    private static JSONObject formData = new JSONObject();

    static boolean isNotEmpty = true;




    public static void func_showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void func_showAlerts(Context context, CharSequence msg, String alert_type) {

        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);

        View toastView = toast.getView();
        if(alert_type.equals("")) {
            toastView.setBackgroundResource(R.drawable.toast_drawable);
        }
        else if(alert_type.equals("warning")) {
            toastView.setBackgroundResource(R.drawable.toast_drawable_warning);
        }
        else if(alert_type.equals("success")) {
            toastView.setBackgroundResource(R.drawable.toast_drawable_success);
        }
        toastView.setTop(500);
        toast.show();
    }

    public static boolean isInternetConnected(Context context) {
        boolean flag = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            flag = true;
        }
        return flag;
    }


    public static String func_stringpart(String str, int len) {

        String out = (str.trim().length() > len) ? str.trim().substring(0, len) + "..." : str;

        return out;
    }


    public static String func_formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    public static String func_formatDateFromString(String value) {

        if(value.length() == 13){
            Long raw_time_long = Long.parseLong(value);
            java.util.Date new_time = new java.util.Date(raw_time_long);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm"); //, HH:mm
            return sdf.format(new_time);
        } else {
            return value;
        }

        //Date date = null;
        /*if(value.length() > 6){
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-mm-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

            try {
                String reformat = sdf.format(fromUser.parse(value));
                return reformat;
            }
            catch (Exception e) {
                Log.d("DateFromString", e.getLocalizedMessage());
            }
        }*/

    }


    public static String func_formatUnixDate(String raw_time) {

        Long raw_time_long = Long.parseLong(raw_time);
        if(raw_time.length() < 13){
            raw_time_long = raw_time_long * 1000;
        }
        java.util.Date new_time = new java.util.Date(raw_time_long);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");

        return sdf.format(new_time);
    }


    public static String func_formatDecimal(double val) {

        DecimalFormat form = new DecimalFormat("0.00");
        String value_out = String.valueOf(form.format(val));

        return value_out;
    }


    public static String base64Encode(String rawData) {
        result = null;

        byte[] data = rawData.getBytes(StandardCharsets.UTF_8);
        result = Base64.encodeToString(data, Base64.DEFAULT);
        return result;
    }


    public static String base64Decode(String rawData) {
        result = null;

        byte[] data = Base64.decode(rawData, Base64.DEFAULT);
        result = new String(data, StandardCharsets.UTF_8);
        return result;
    }


    public static String func_sanitizePhoneNumber(String phone) {

        if(phone.equals("")){
            return "";
        }

        else if (phone.length() < 11 & phone.startsWith("0")) {
            String p = phone.replaceFirst("^0", "254");
            return p;
        }

        else if(phone.length() == 13 && phone.startsWith("+")){
            //String p = phone.replaceFirst("^+", "");
            String p = phone.substring(1);
            return p;
        } else {
            return phone;
        }
    }

}
