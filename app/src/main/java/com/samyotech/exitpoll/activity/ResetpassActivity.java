package com.samyotech.exitpoll.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.https.NetworkTask;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;
import com.samyotech.exitpoll.utils.Consts;
import com.samyotech.exitpoll.utils.CustomEditText;
import com.samyotech.exitpoll.utils.DialogUtility;
import com.samyotech.exitpoll.utils.ProjectUtils;

public class ResetpassActivity extends AppCompatActivity implements View.OnClickListener {
    Button resetBTN;
    CustomEditText etCurrentPass, etNewPass, etConfNewPass;
    LinearLayout backUP;
    SharedPrefrence preference;
    NetworkTask networkTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass);

        preference = SharedPrefrence.getInstance(this);
        init();
    }

    public void init() {
        resetBTN = (Button) findViewById(R.id.resetBTN);
        etCurrentPass = (CustomEditText) findViewById(R.id.etCurrentPass);
        etNewPass = (CustomEditText) findViewById(R.id.etNewPass);
        etConfNewPass = (CustomEditText) findViewById(R.id.etConfNewPass);
        backUP = (LinearLayout) findViewById(R.id.backUP);
        resetBTN.setOnClickListener(this);
        backUP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetBTN:
                checkpass();
                break;
            case R.id.backUP:
                startActivity(new Intent(ResetpassActivity.this, Settings.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
                break;
        }

    }

    public void checkpass() {

        if (!etCurrentPass.getText().toString().trim().equals(preference.getValue(SharedPrefrence.PASSWORD))) {
            Log.e("pass", preference.getValue(SharedPrefrence.PASSWORD));
            Toast.makeText(ResetpassActivity.this, "Please Enter Current Password", Toast.LENGTH_SHORT).show();
        } else if (etNewPass.getText().toString().trim().equals("")) {
            Toast.makeText(ResetpassActivity.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
        } else if (etConfNewPass.getText().toString().trim().equals("")) {
            Toast.makeText(ResetpassActivity.this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (!etNewPass.getText().toString().trim().equals(etConfNewPass.getText().toString().trim())) {
            Toast.makeText(ResetpassActivity.this, "Do not match New Password and Confirm Password", Toast.LENGTH_SHORT).show();
        } else {
            DialogUtility.showProgressDialog(this, false, "Please wait...");
            resetPass();
        }

    }


    public void resetPass() {
        networkTask = new NetworkTask(Consts.RESET_METHOD, getParam(), ResetpassActivity.this);
        networkTask.execute(Consts.POST_METHOD);
        networkTask.setOnTaskFinishedEvent(new NetworkTask.AsyncResponse() {
            @Override
            public void processFinish(boolean output, String message) {
                Log.e("output", String.valueOf(output));
                if (preference.getBooleanValue("resetStatus")) {
                    preference.setBooleanValue(SharedPrefrence.IS_LOGIN, true);
                    DialogUtility.showToast(getApplicationContext(), message);
                    DialogUtility.pauseProgressDialog();
                    preference.setValue(SharedPrefrence.PASSWORD, etNewPass.getText().toString().trim());
                    startActivity(new Intent(ResetpassActivity.this, LoginActivity.class));
                    finish();

                } else {
                    DialogUtility.showToast(getApplicationContext(), message);
                    DialogUtility.pauseProgressDialog();
                }

            }

        });
    }

    public ContentValues getParam() {
        ContentValues values = new ContentValues();
        values.put(Consts.EMAIL, preference.getValue(SharedPrefrence.EMAIL));
        values.put(Consts.PASSWORD, etNewPass.getText().toString().trim());

        Log.e("email", preference.getValue(SharedPrefrence.EMAIL));
        Log.e("passs", etNewPass.getText().toString().trim());
        return values;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ResetpassActivity.this, Settings.class));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

}
