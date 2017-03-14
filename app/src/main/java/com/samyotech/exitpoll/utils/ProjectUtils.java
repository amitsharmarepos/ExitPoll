package com.samyotech.exitpoll.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.activity.SysApplication;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by samyotech on 30/11/16.
 */

public class ProjectUtils {

    /*validation code here*/
    public static boolean isPasswordValid(String number) {

        //String regexStr = "^([0-9\\(\\)\\/\\+ \\-]*)$";
        String regexStr = " (?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8,20})$";

        if (number.length() < 8 || number.length() > 10 || number.matches(regexStr) == false) {
            //	Log.d("tag", "Number is not valid");
            return false;
        }

        return true;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else if (email.equals("")) {
            return false;
        }
        return false;
    }

    public boolean validateDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        return false;
    }

    public static boolean isPhoneNumberValid(String number) {

        //String regexStr = "^([0-9\\(\\)\\/\\+ \\-]*)$";
        String regexStr = "^((0)|(91)|(00)|[7-9]){1}[0-9]{3,14}$";

        if (number.length() < 10 || number.length() > 10 || number.matches(regexStr) == false) {
            //	Log.d("tag", "Number is not valid");
            return false;
        }

        return true;
    }

    public static boolean isPinCodeValid(String number) {

        //String regexStr = "^([0-9\\(\\)\\/\\+ \\-]*)$";
        String regexStr = "^[1-9][0-9]{6}$";

        if (number.length() < 6 || number.length() > 6 || number.matches(regexStr) == false) {
            //	Log.d("tag", "Number is not valid");
            return false;
        }

        return true;
    }


    public static boolean isEditTextFilled(EditText text) {
        if (text.getText() != null && text.getText().toString().trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEditTextFilled1(EditText text) {
        if (text.getText() != null && text.getText().toString().trim().length() > 5) {
            return true;
        } else {
            return false;
        }
    }



                      /*validation code here*/


    public static boolean hasPermissionInManifest(Activity activity, int requestCode, String permissionName) {
        if (ContextCompat.checkSelfPermission(activity,
                permissionName)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{permissionName},
                    requestCode);
        } else {
            return true;
        }
        return false;
    }

    public static void showAnalytics(SysApplication sysApplication, String nameOfScreen) {
        Tracker tracker = sysApplication.getDefaultTracker();
        tracker.setScreenName(nameOfScreen);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        tracker.enableExceptionReporting(true);
    }

}
