package flavor.tech.com.iptfwebrtc.util;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Utility {

    public static void sendEmail(Activity pActivity, String pTo, String pSubject, String pMailTxt, Boolean isAttachment, String fileloc) {

        Log.i("Send email", "");
        String[] TO = {pTo};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, pSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, pMailTxt);
        if (isAttachment)
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fileloc));

        try {
            pActivity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//            finish();
            Log.i(Constant.TAG, "Finished sending email...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(pActivity, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendSMS(Activity pActivity, String pSMSNumber, String pSMSText) {
        Uri uri = Uri.parse("smsto:" + pSMSNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", pSMSText);
        pActivity.startActivity(intent);
    }

    public static void callSupport(Activity pActivity, String pNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + pNumber));
            pActivity.startActivity(intent);
        }catch (ActivityNotFoundException AE){
            Toast.makeText(pActivity, "Calling not supported or Check call permission in setting for app.", Toast.LENGTH_LONG).show();
        }
    }

    public static void setSharedPreferences(String pPrefName, HashMap pData, Context pCtx, Boolean pIsNew ) {
        Log.i(Constant.TAG, "Going to save data into preference :" + pPrefName);
        SharedPreferences preferences = pCtx.getSharedPreferences(pPrefName, pCtx.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(pPrefName, true);
        Set set = pData.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            editor.putString(me.getKey().toString(), me.getValue().toString());
            Log.i(Constant.TAG, "Key: "+ me.getKey() +" Value: "+me.getValue().toString());
        }
        if (pIsNew ) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static HashMap getSharedPreferences(String pPrefName, Context pCtx){
        Log.i(Constant.TAG, "Going to get data from preference :" + pPrefName);
        HashMap hmData = new HashMap();
        SharedPreferences prefs = pCtx.getSharedPreferences(
                pPrefName, Context.MODE_PRIVATE);
        if ( prefs.contains(pPrefName) ) {
            for (Map.Entry entry : prefs.getAll().entrySet())
                hmData.put(entry.getKey(), entry.getValue().toString());
        }else{
            hmData = null;
        }
        return hmData;
    }

    public static void deleteSharedPreferences(String pPrefName, Context pCtx){
        SharedPreferences preferences = pCtx.getSharedPreferences(pPrefName, 0);
        preferences.edit().clear().commit();
    }

    public static String getSharedPreferencesByValue(String pPrefName, Context pCtx, String pKey ){
        Log.i(Constant.TAG, "Going to get data from preference :" + pPrefName +" Key: "+ pKey);
        String mData = null;
        SharedPreferences prefs = pCtx.getSharedPreferences(
                pPrefName, Context.MODE_PRIVATE);
        if ( prefs.contains(pPrefName) ) {
            mData = prefs.getString(pKey, "None").toString();
        }
        return mData;
    }
    public static String getCurrentTime() {
        Calendar now = Calendar.getInstance();
        System.out.println("Time here " + now.getTime());
        return now.getTime().toString();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }
}
