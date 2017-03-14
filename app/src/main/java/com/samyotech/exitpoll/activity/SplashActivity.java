package com.samyotech.exitpoll.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.dto.Album;
import com.samyotech.exitpoll.https.NetworkTask;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;
import com.samyotech.exitpoll.utils.Consts;
import com.samyotech.exitpoll.utils.DialogUtility;
import com.samyotech.exitpoll.utils.ProjectUtils;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    SharedPrefrence preference;
    NetworkTask networkTask;
    private static int SPLASH_TIME_OUT = 3000;
    private Context mContext;
    public ArrayList<Album> albumList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preference = SharedPrefrence.getInstance(this);
        getDATA();
        mContext = SplashActivity.this;
        ProjectUtils.showAnalytics((SysApplication) getApplication(), "SplashActivity");
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                if (preference.getBooleanValue(SharedPrefrence.IS_LOGIN)) {
                    Log.e("islogin1", SharedPrefrence.IS_LOGIN);
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    public void getDATA() {
        DialogUtility.showProgressDialog(this, false, "Please wait...");
        networkTask = new NetworkTask(Consts.DATA_METHOD, getParam(), SplashActivity.this);
        networkTask.execute(Consts.POST_METHOD);
        networkTask.setOnTaskFinishedEvent(new NetworkTask.AsyncResponse() {
            @Override
            public void processFinish(boolean output, String message) {

                Log.e("output", String.valueOf(output));
                if (output) {
                    albumList = preference.getList(SharedPrefrence.POLITATION_RECORD);

                    DialogUtility.pauseProgressDialog();

                } else {
                    DialogUtility.pauseProgressDialog();
                }

            }

        });
    }

    public ContentValues getParam() {
        ContentValues values = new ContentValues();
        return values;
    }



}
