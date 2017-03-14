package com.samyotech.exitpoll.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.https.NetworkTask;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;
import com.samyotech.exitpoll.utils.Consts;
import com.samyotech.exitpoll.utils.DialogUtility;
import com.samyotech.exitpoll.utils.ProjectUtils;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView signUpTV, tvForgotPassword;
    private TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    private EditText emailET, passwordET;
    private Button submitBTN;
    NetworkTask networkTask;
    SharedPrefrence preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preference = SharedPrefrence.getInstance(this);


        initView();

    }

    public void initView() {
        signUpTV = (TextView) findViewById(R.id.signUpTV);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        submitBTN = (Button) findViewById(R.id.submitBTN);

        signUpTV.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        submitBTN.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signUpTV:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
                break;
            case R.id.tvForgotPassword:
                startActivity(new Intent(LoginActivity.this, ForgetpasswordActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                finish();
                break;
            case R.id.submitBTN:
                submitForm();
                break;
        }


    }


    private void submitForm() {

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        } else {
            DialogUtility.showProgressDialog(this, false, "Please wait...");
            getLogin();
        }

    }

    public void getLogin() {
        networkTask = new NetworkTask(Consts.LOGIN_METHOD, getParam(), LoginActivity.this);
        networkTask.execute(Consts.POST_METHOD);
        networkTask.setOnTaskFinishedEvent(new NetworkTask.AsyncResponse() {
            @Override
            public void processFinish(boolean output, String message) {
                Log.e("output", String.valueOf(output));
                if (preference.getBooleanValue("loginStatus")) {
                    preference.setBooleanValue(SharedPrefrence.IS_LOGIN, true);
                    Log.e("isloginlogin", SharedPrefrence.IS_LOGIN);
                    DialogUtility.showToast(getApplicationContext(), message);
                    DialogUtility.pauseProgressDialog();
                    preference.setValue(SharedPrefrence.EMAIL, emailET.getText().toString());
                    preference.setValue(SharedPrefrence.PASSWORD, passwordET.getText().toString());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
        values.put(Consts.EMAIL, emailET.getText().toString().trim());
        values.put(Consts.PASSWORD, passwordET.getText().toString().trim());
        values.put(Consts.FCMKEY, preference.getValue(SharedPrefrence.TOKAN));
        Log.e("tokensss", preference.getValue(SharedPrefrence.TOKAN));
        return values;
    }

    private boolean validatePassword() {
        if (!ProjectUtils.isEditTextFilled(passwordET)) {
            passwordTextInputLayout.setError(getString(R.string.err_msg_password));
            passwordET.requestFocus();
            return false;
        } else {
            passwordTextInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public boolean validateEmail() {
        if (!ProjectUtils.isEmailValid(emailET.getText().toString().trim())) {
            emailTextInputLayout.setError(getString(R.string.err_msg_email));
            emailET.requestFocus();
            return false;
        } else {
            emailTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.exitpoll)
                .setTitle("ExitPoll")
                .setMessage("Are you sure want to close ExitPoll?")
                .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}
