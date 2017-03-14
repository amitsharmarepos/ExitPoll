package com.samyotech.exitpoll.activity;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.dto.BaseDTO;
import com.samyotech.exitpoll.dto.UserDTO;
import com.samyotech.exitpoll.fragment.MainFragment;
import com.samyotech.exitpoll.https.NetworkTask;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;
import com.samyotech.exitpoll.utils.Consts;
import com.samyotech.exitpoll.utils.DialogUtility;
import com.samyotech.exitpoll.utils.ImageCompression;
import com.samyotech.exitpoll.utils.ProjectUtils;
import com.samyotech.exitpoll.https.UploadFileToServer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UpdateprofileActivity extends FragmentActivity implements View.OnClickListener {
    private TextInputLayout nameTextInputLayout, dobETInputLayout, mobTextInputLayout, postCodeTextInputLayout, emailTextInputLayout, doorTextInputLayout;
    private EditText nameET, dobET, mobileNoET, emailET, doorET, postCodeET;
    private Button submitsBTN;
    private ImageView ivUser;
    LinearLayout backUP;
    DatePickerDialog datePickerDOB;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    SharedPrefrence preference;
    NetworkTask networkTask;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    BottomSheet.Builder builder;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    DisplayImageOptions options;
    byte[] resultByteArray;
    UploadFileToServer uploadFileToServer;
    UserDTO userDTO;
    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);
        ProjectUtils.showAnalytics((SysApplication) getApplication(), "UpdateprofileActivity");
        preference = SharedPrefrence.getInstance(this);
        init();

        userDTO = preference.getUserDetails(SharedPrefrence.USER_DETAIL);
        if (userDTO != null) {
            updateUser();
        }

        builder = new BottomSheet.Builder(this).sheet(R.menu.menu_cards);
        builder.title("ExitPoll : Take Image From");
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {


                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(UpdateprofileActivity.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(UpdateprofileActivity.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                File file = getOutputMediaFile(1);
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                picUri = Uri.fromFile(file);

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY);

                            }
                        }
                        break;
                    case R.id.cancel_cards:
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });
        ivUser.setOnClickListener(this);
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Exitpoll");

        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "Exitpoll_" + timeStamp + ".png");

            imageName = "Exitpoll_" + timeStamp + ".png";
        } else {
            return null;
        }

        return mediaFile;
    }

    public void init() {

        nameTextInputLayout = (TextInputLayout) findViewById(R.id.nameTextInputLayout);
        dobETInputLayout = (TextInputLayout) findViewById(R.id.dobETInputLayout);
        mobTextInputLayout = (TextInputLayout) findViewById(R.id.mobTextInputLayout);


        nameET = (EditText) findViewById(R.id.nameET);
        dobET = (EditText) findViewById(R.id.dobET);
        mobileNoET = (EditText) findViewById(R.id.mobileNoET);
        emailET = (EditText) findViewById(R.id.emailET);
        emailET.setText(preference.getValue(SharedPrefrence.EMAIL));
        emailET.setEnabled(false);
        doorET = (EditText) findViewById(R.id.doorET);
        backUP = (LinearLayout) findViewById(R.id.backUP);
        doorET.addTextChangedListener(new MyTextWatcher(doorET));


        postCodeET = (EditText) findViewById(R.id.postCodeET);
        ivUser = (ImageView) findViewById(R.id.ivUser);
        submitsBTN = (Button) findViewById(R.id.submitsBTN);
        dobET.setOnClickListener(this);
        submitsBTN.setOnClickListener(this);
        backUP.setOnClickListener(this);
        doorET.setOnClickListener(this);


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
                    Toast.makeText(UpdateprofileActivity.this, "Valid Date", Toast.LENGTH_SHORT).show();
                }
            }

        }, calendar.get(Calendar.YEAR) - 18, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dobET:
                datePickerDOB.setTitle("Select Date Of Birth");
                datePickerDOB.show();
                break;
            case R.id.ivUser:
                builder.show();
                break;
            case R.id.submitsBTN:
                submitForm();
                break;
            case R.id.backUP:
                startActivity(new Intent(UpdateprofileActivity.this, Settings.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
                break;
            case R.id.doorET:
                findPlace(v);
                break;
        }
    }

    private void submitForm() {
        if (!validateNameET(nameET, nameTextInputLayout)) {
            return;
        }
        if (!validateMobile()) {
            return;
        } else {

            upDateProfile();
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

    private boolean validatePin() {
        if (!ProjectUtils.isEditTextFilled1(postCodeET)) {
            postCodeTextInputLayout.setError(getString(R.string.pincode_required));
            postCodeET.requestFocus();
            return false;
        } else {
            postCodeTextInputLayout.setErrorEnabled(false);
        }

        return true;
    }


    public ContentValues getByteParam() {
        ContentValues values = new ContentValues();
        values.put(Consts.USER_PROFILE_PIC, resultByteArray);
        Log.e("hhhhhh", "" + resultByteArray);
        return values;
    }

    public void upDateProfile() {
        DialogUtility.showProgressDialog(this, false, "Please wait...");
        uploadFileToServer = new UploadFileToServer(Consts.UPDATE_USER_PROFILE, getParamForUpdateProfile(), getByteParam(), UpdateprofileActivity.this);
        uploadFileToServer.execute(Consts.POST_METHOD);
        uploadFileToServer.setOnTaskFinishedEvent(new UploadFileToServer.AsyncResponse() {

            @Override
            public void processFinish(boolean output, String message) {
                DialogUtility.pauseProgressDialog();
                DialogUtility.showToast(UpdateprofileActivity.this, message);
                startActivity(new Intent(UpdateprofileActivity.this, Settings.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
            }

            @Override
            public void processFinish(boolean output, String message, BaseDTO galleryDTO) {

            }

        });
    }

    public ContentValues getParamForUpdateProfile() {
        ContentValues values = new ContentValues();
        values.put(Consts.NAME, nameET.getText().toString().trim());
        values.put(Consts.DOB, dobET.getText().toString().trim());
        values.put(Consts.EMAIL, preference.getValue(SharedPrefrence.EMAIL));
        values.put(Consts.MOBILE, mobileNoET.getText().toString().trim());
        values.put(Consts.ADDRESS, doorET.getText().toString().trim());
        values.put(Consts.POSTELCODE, postCodeET.getText().toString().trim());
        return values;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                place = PlaceAutocomplete.getPlace(this, data);

                doorET.setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == CROP_CAMERA_IMAGE) {

            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));

                try {
                    //bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();

                    imageCompression = new ImageCompression(this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            updateUserImage(ivUser, "file://" + imagePath);

                            try {
                                // bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);

                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                bm = BitmapFactory.decodeFile(imagePath, bmOptions);
                                ByteArrayOutputStream buffer = new ByteArrayOutputStream(bm.getWidth() * bm.getHeight());
                                bm.compress(Bitmap.CompressFormat.PNG, 100, buffer);
                                resultByteArray = buffer.toByteArray();
                                bm.recycle();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        if (requestCode == CROP_GALLERY_IMAGE) {

            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));

                try {
                    bm = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();

                    imageCompression = new ImageCompression(this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            updateUserImage(ivUser, "file://" + imagePath);
                            Log.e("image", imagePath);

                            try {
                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                bm = BitmapFactory.decodeFile(imagePath, bmOptions);
                                ByteArrayOutputStream buffer = new ByteArrayOutputStream(bm.getWidth() * bm.getHeight());
                                bm.compress(Bitmap.CompressFormat.PNG, 100, buffer);
                                resultByteArray = buffer.toByteArray();
                                bm.recycle();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        if (requestCode == PICK_FROM_CAMERA) {
            if (picUri != null) {
                startCropping(picUri, CROP_CAMERA_IMAGE);
//                startCropActivity(picUri, CROP_CAMERA_IMAGE);
            } else {

            }
        }


        if (requestCode == PICK_FROM_GALLERY) {
            try {
                Uri tempUri = data.getData();

                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {
                    startCropping(tempUri, CROP_GALLERY_IMAGE);
//                    startCropActivity(tempUri, CROP_GALLERY_IMAGE);

                } else {

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    public void startCropping(Uri uri, int requestCode) {

        Intent intent = new Intent(this, MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }

    public void updateUserImage(final ImageView imageView, String uri) {
        ImageLoader.getInstance().displayImage(uri, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                ivUser.setImageResource(R.drawable.ic_add_photo_large);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                ivUser.setImageResource(R.drawable.ic_add_photo_large);
            }
        });
    }

    public void updateUser() {

        nameET.setText(userDTO.getName());
        dobET.setText(userDTO.getDob());
        mobileNoET.setText(userDTO.getMobileno());
        doorET.setText(userDTO.getDoor());
        postCodeET.setText(userDTO.getPincode());

        if (userDTO.getImage().equals(""))
            ivUser.setImageResource(R.drawable.ic_add_photo_large);
        else
            updateUserImage(ivUser, userDTO.getImage());

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public void findPlace(View view) {
        try {
            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UpdateprofileActivity.this, Settings.class));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

}