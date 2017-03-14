package com.samyotech.exitpoll.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.dto.Favdto;
import com.samyotech.exitpoll.https.NetworkTask;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;
import com.samyotech.exitpoll.utils.ProjectUtils;
import com.samyotech.exitpoll.utils.TouchImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class ViewDetailsActivity extends AppCompatActivity {
    ImageView ic_action_back, ic_action_Share;
    TextView nameTv, posTv, partyTv, wichpostTv, educationTv, ageTv;
    String name, post, party, wichpost, education, age, img;
    TouchImageView imageshow;
    NetworkTask networkTask;
    SharedPrefrence preference;
    DisplayImageOptions options;
    RelativeLayout rl_top_icons, rl_bottomView;
    boolean isHide = false;
    private List<Favdto> favlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdetails);
        ProjectUtils.showAnalytics((SysApplication) getApplication(), "DetailsActivity");
        preference = SharedPrefrence.getInstance(ViewDetailsActivity.this);

        favlist = preference.getFavFList(SharedPrefrence.FAV_RECORD);


        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.defaultimg)
                .cacheInMemory()
                .cacheOnDisc()
                .build();
        initView();
    }

    public void initView() {

        imageshow = (TouchImageView) findViewById(R.id.imageshow);
        ic_action_Share = (ImageView) findViewById(R.id.ic_action_Share);
        ic_action_back = (ImageView) findViewById(R.id.ic_action_back);
        nameTv = (TextView) findViewById(R.id.nameTv);
        posTv = (TextView) findViewById(R.id.posTv);
        partyTv = (TextView) findViewById(R.id.partyTv);
        wichpostTv = (TextView) findViewById(R.id.wichpostTv);
        educationTv = (TextView) findViewById(R.id.educationTv);
        ageTv = (TextView) findViewById(R.id.ageTv);
        rl_top_icons = (RelativeLayout) findViewById(R.id.rl_top_icons);
        rl_bottomView = (RelativeLayout) findViewById(R.id.rl_bottomView);


        Intent in = getIntent();
        name = in.getStringExtra("name");
        post = in.getStringExtra("post");
        party = in.getStringExtra("party");
        wichpost = in.getStringExtra("whichpost");
        education = in.getStringExtra("education");
        age = in.getStringExtra("age");
        img = in.getStringExtra("img");
        nameTv.setText(name);
        posTv.setText(post);
        partyTv.setText(party);
        wichpostTv.setText(wichpost);
        educationTv.setText(education);
        ageTv.setText(age);
        String politicsid = in.getStringExtra("politicsid");


        ImageLoader.getInstance().displayImage("http://samyotechlabs.com/exitpoll/" + img, imageshow, options);


        imageshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isHide) {
                    fadeInAndShowView(rl_top_icons);
                    fadeInAndShowView(rl_bottomView);
                    isHide = false;
                } else {
                    fadeOutAndHideView(rl_top_icons);
                    fadeOutAndHideView(rl_bottomView);
                    isHide = true;
                }


            }
        });
        ic_action_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewDetailsActivity.this, ViewActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
            }
        });
 /*       ic_action_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap mBitmap;
                mBitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.defaultimg);
                Bitmap icon = mBitmap;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });*/


        ic_action_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int THUMBSIZE = 64;

              /*  Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath),
                        THUMBSIZE, THUMBSIZE);*/
                Bitmap bitmap;
                OutputStream output;

                // Retrieve the image from the res folder
                bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_launcher), THUMBSIZE, THUMBSIZE);
               /* bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile("http://samyotech.com/exitpoll/" + img),
                        THUMBSIZE, THUMBSIZE);*/


                // Find the SD Card path
                File filepath = Environment.getExternalStorageDirectory();

                // Create a new folder AndroidBegin in SD Card
                File exitpolls = new File(filepath.getAbsolutePath() + "/ExitPoll/");
                exitpolls.mkdirs();
                // Create a name for the saved image
                File file = new File(exitpolls, "appicon.png");

                try {

                    // Share Intent
                    Intent share = new Intent(Intent.ACTION_SEND);

                    // Type of file to share
                    share.setType("image/jpeg");

                    output = new FileOutputStream(file);

                    // Compress into png format image from 0% - 100%
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                    output.flush();
                    output.close();

                    String text = "ExitPoll Information";
                    // Locate the image to Share
                    Uri uri = Uri.fromFile(file);

                    // Pass the image into an Intnet
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.putExtra(Intent.EXTRA_TEXT, text + "\n" + "Name : " + name + "\n" + "Post : " + post + "\n" + "Party : " + party +
                            "\n" + "WhichPost : " + wichpost + "\n" + "Education : " + education + "\n" + "Age : " + age);

                    // Show the social share chooser list
                    startActivity(Intent.createChooser(share, "Share Image Tutorial"));

                } catch (Exception e) {

                }

            }
        });

    }

    private void fadeInAndShowView(final RelativeLayout img) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(500);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeIn);
    }

    private void fadeOutAndHideView(final RelativeLayout img) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(500);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeOut);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewDetailsActivity.this, ViewActivity.class));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }
}
