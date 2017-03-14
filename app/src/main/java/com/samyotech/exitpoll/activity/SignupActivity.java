package com.samyotech.exitpoll.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.https.NetworkTask;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;
import com.samyotech.exitpoll.utils.Consts;
import com.samyotech.exitpoll.utils.DialogUtility;
import com.samyotech.exitpoll.utils.ProjectUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView signInTV;
    private TextInputLayout nameTextInputLayout, dobETInputLayout, emailTextInputLayout, mobTextInputLayout, passwordTextInputLayout;
    private EditText nameET, dobET, emailET, mobileNoET, passwordET, conformET;
    private Button submitsBTN;
    DatePickerDialog datePickerDOB;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    SharedPrefrence preference;
    NetworkTask networkTask;
   // boolean clickeye = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        preference = SharedPrefrence.getInstance(this);


        init();
    }

    public void init() {

        signInTV = (TextView) findViewById(R.id.signInTV);

        nameTextInputLayout = (TextInputLayout) findViewById(R.id.nameTextInputLayout);
        dobETInputLayout = (TextInputLayout) findViewById(R.id.dobETInputLayout);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        mobTextInputLayout = (TextInputLayout) findViewById(R.id.mobTextInputLayout);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);

        nameET = (EditText) findViewById(R.id.nameET);
        dobET = (EditText) findViewById(R.id.dobET);
        emailET = (EditText) findViewById(R.id.emailET);
        mobileNoET = (EditText) findViewById(R.id.mobileNoET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        conformET = (EditText) findViewById(R.id.conformET);

        submitsBTN = (Button) findViewById(R.id.submitsBTN);
        passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        conformET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        signInTV.setOnClickListener(this);
        dobET.setOnClickListener(this);
        submitsBTN.setOnClickListener(this);
//        passwordET.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (clickeye) {
//                    conformET.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    clickeye = false;
//
//                } else {
//                    clickeye = true;
//                    conformET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                }
//            }
//        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        calendar = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        datePickerDOB = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                Calendar today = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if (today.get(Calendar.YEAR) - 17 > newDate.get(Calendar.YEAR)) {
                    dobET.setText(dateFormatter.format(newDate.getTime()));
                } else {
                    Toast.makeText(SignupActivity.this, "Valid Date", Toast.LENGTH_SHORT).show();
                }
            }

        }, calendar.get(Calendar.YEAR) - 18, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


    }



    /* Calendar minAdultAge = new GregorianCalendar();
    minAdultAge.add(Calendar.YEAR, -18);
    if (minAdultAge.before(userAge)) {
        SHOW_ERROR_MESSAGE;
    }
}*/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signInTV:
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
                break;
            case R.id.dobET:
                datePickerDOB.setTitle("Select Date Of Birth");
                datePickerDOB.show();
                break;
            case R.id.submitsBTN:
                submitForm();
                break;

        }

    }


    private void submitForm() {
        if (!validateNameET(nameET, nameTextInputLayout)) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if (!validateMobile()) {
            return;
        } else {
            checkpass();
        }


    }


    private boolean validatePassword() {
        if (!ProjectUtils.isEditTextFilled1(passwordET)) {
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

    public boolean validateNameET(EditText editText, TextInputLayout layout) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            layout.setError(getString(R.string.name_required));
            editText.requestFocus();
            return false;
        } else {
            layout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateMobile() {
        if (!ProjectUtils.isPhoneNumberValid(mobileNoET.getText().toString().trim())) {
            mobTextInputLayout.setError(getString(R.string.mobile_required));
            mobileNoET.requestFocus();
            return false;
        } else {
            mobTextInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public void checkpass() {

        if (passwordET.getText().toString().trim().equals("")) {
            Toast.makeText(SignupActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (conformET.getText().toString().trim().equals("")) {
            Toast.makeText(SignupActivity.this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (!passwordET.getText().toString().trim().equals(conformET.getText().toString().trim())) {
            Toast.makeText(SignupActivity.this, "Do not match Password and Confirm Password", Toast.LENGTH_SHORT).show();
        } else {
            DialogUtility.showProgressDialog(this, false, "Please wait...");
            getRegister();
        }

    }

    public void getRegister() {
        networkTask = new NetworkTask(Consts.REGISTER_METHOD, getParam(), SignupActivity.this);
        networkTask.execute(Consts.POST_METHOD);
        networkTask.setOnTaskFinishedEvent(new NetworkTask.AsyncResponse() {
            @Override
            public void processFinish(boolean output, String message) {
                Log.e("output", String.valueOf(output));
                if (preference.getBooleanValue("signupCheck")) {
                    preference.setBooleanValue(SharedPrefrence.IS_LOGIN, true);
                    DialogUtility.showToast(getApplicationContext(), message);
                    DialogUtility.pauseProgressDialog();
                    preference.setValue(SharedPrefrence.NAME, nameET.getText().toString());
                    preference.setValue(SharedPrefrence.DOB, dobET.getText().toString());
                    preference.setValue(SharedPrefrence.EMAIL, emailET.getText().toString());
                    preference.setValue(SharedPrefrence.MOBILE, mobileNoET.getText().toString());
                    preference.setValue(SharedPrefrence.PASSWORD, passwordET.getText().toString());
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
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
        values.put(Consts.NAME, nameET.getText().toString().trim());
        values.put(Consts.DOB, dobET.getText().toString().trim());
        values.put(Consts.EMAIL, emailET.getText().toString().trim());
        values.put(Consts.MOBILE, mobileNoET.getText().toString().trim());
        values.put(Consts.PASSWORD, passwordET.getText().toString().trim());
        values.put(Consts.FCMKEY, preference.getValue(SharedPrefrence.TOKAN));
        Log.e("tokenjj", preference.getValue(SharedPrefrence.TOKAN));

        return values;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }


}
