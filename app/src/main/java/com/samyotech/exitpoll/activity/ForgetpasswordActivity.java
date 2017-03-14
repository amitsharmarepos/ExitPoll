package com.samyotech.exitpoll.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.https.NetworkTask;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;
import com.samyotech.exitpoll.utils.Consts;
import com.samyotech.exitpoll.utils.DialogUtility;
import com.samyotech.exitpoll.utils.ProjectUtils;

public class ForgetpasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout emailTextInputLayout;
    private EditText emailET;
    private Button submitBTN;
    NetworkTask networkTask;
    SharedPrefrence preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        initView();
        preference = SharedPrefrence.getInstance(this);

    }

    public void initView() {
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        emailET = (EditText) findViewById(R.id.emailET);
        submitBTN = (Button) findViewById(R.id.submitBTN);
        submitBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submitBTN:
                submitForm();
                break;
        }

    }

    private void submitForm() {

        if (!validateEmail()) {
            return;
        } else {
            DialogUtility.showProgressDialog(this, false, "Please wait...");
            getForgotPassword();
        }


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

    public void getForgotPassword() {
        networkTask = new NetworkTask(Consts.FORGOT_PASSWORd, getParamForForgotPassword(), ForgetpasswordActivity.this);
        networkTask.execute(Consts.POST_METHOD);
        networkTask.setOnTaskFinishedEvent(new NetworkTask.AsyncResponse() {
            @Override
            public void processFinish(boolean output, String message) {
                DialogUtility.pauseProgressDialog();

                if (preference.getBooleanValue("forgetstatus")) {
                    Toast.makeText(ForgetpasswordActivity.this, preference.getValue("message"), Toast.LENGTH_SHORT).show();
                    preference.setValue(SharedPrefrence.FORGOT_PASS_EMAIL, emailET.getText().toString().trim());
                    startActivity(new Intent(ForgetpasswordActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(ForgetpasswordActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public ContentValues getParamForForgotPassword() {
        ContentValues values = new ContentValues();
        values.put(Consts.EMAIL, emailET.getText().toString().trim());
        return values;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgetpasswordActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }
}
