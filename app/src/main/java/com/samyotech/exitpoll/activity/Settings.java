package com.samyotech.exitpoll.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;
import com.samyotech.exitpoll.utils.ProjectUtils;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    LinearLayout back;
    RelativeLayout rlLogout, rlResetPass, updatepro;
    SharedPrefrence preference;
    SwitchCompat not_switch;
    boolean notifyAlert = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preference = SharedPrefrence.getInstance(this);
        ProjectUtils.showAnalytics((SysApplication) getApplication(), "Settings");
        notifyAlert = preference.getBooleanValue(SharedPrefrence.NOTIFIOCATION_ENABLE);
        init();

    }

    public void init() {
        back = (LinearLayout) findViewById(R.id.back);
        rlLogout = (RelativeLayout) findViewById(R.id.rlLogout);
        rlResetPass = (RelativeLayout) findViewById(R.id.rlResetPass);

        updatepro = (RelativeLayout) findViewById(R.id.updatepro);
        not_switch = (SwitchCompat) findViewById(R.id.not_switch);
        not_switch.setChecked(notifyAlert);

        back.setOnClickListener(this);
        rlLogout.setOnClickListener(this);
        rlResetPass.setOnClickListener(this);
        updatepro.setOnClickListener(this);
        not_switch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(Settings.this, MainActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
                break;
            case R.id.rlLogout:
                confirmLogout();
                Log.e("isloginlogout", SharedPrefrence.IS_LOGIN);
                break;
            case R.id.rlResetPass:
                startActivity(new Intent(Settings.this, ResetpassActivity.class));
                finish();
                break;
            case R.id.updatepro:
                startActivity(new Intent(Settings.this, UpdateprofileActivity.class));
                finish();
                break;
            case R.id.not_switch:
                clickSwitch();
                break;
        }
    }

    private void clickSwitch() {
        if (not_switch.isChecked()) {
            preference.setBooleanValue(SharedPrefrence.NOTIFIOCATION_ENABLE, true);
        } else {
            preference.setBooleanValue(SharedPrefrence.NOTIFIOCATION_ENABLE, false);
        }
    }

    public void confirmLogout() {
        try {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.exitpoll)
                    .setTitle("ExitPoll")
                    .setMessage("Are you sure want to logout?")
                    .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            logout();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        preference.clearPreferences(SharedPrefrence.IS_LOGIN);
        preference.clearPreferences(SharedPrefrence.FAV_RECORD);
        preference.clearPreferences(SharedPrefrence.POLITATION_RECORD);
        preference.clearPreferences(SharedPrefrence.USER_DETAIL);
        preference.clearPreferences(SharedPrefrence.VIEW_POLITATION_RECORD);
        startActivity(new Intent(Settings.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Settings.this, MainActivity.class));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }


}
