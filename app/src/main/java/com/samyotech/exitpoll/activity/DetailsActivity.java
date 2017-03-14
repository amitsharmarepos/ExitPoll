package com.samyotech.exitpoll.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.dto.Album;
import com.samyotech.exitpoll.dto.Favdto;
import com.samyotech.exitpoll.https.NetworkTask;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;
import com.samyotech.exitpoll.utils.Consts;
import com.samyotech.exitpoll.utils.DialogUtility;
import com.samyotech.exitpoll.utils.ProjectUtils;
import com.samyotech.exitpoll.utils.TouchImageView;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    ImageView ic_action_back, ic_action_addfav;
    TouchImageView imageshow;
    TextView nameTv, posTv, partyTv, wichpostTv, educationTv, ageTv;
    NetworkTask networkTask;
    SharedPrefrence preference;
    DisplayImageOptions options;
    RelativeLayout rl_top_icons, rl_bottomView;
    boolean isHide = false;
    private List<Favdto> favlist;

    Boolean isFav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ProjectUtils.showAnalytics((SysApplication) getApplication(), "DetailsActivity");
        preference = SharedPrefrence.getInstance(DetailsActivity.this);

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
        ic_action_back = (ImageView) findViewById(R.id.ic_action_back);
        ic_action_addfav = (ImageView) findViewById(R.id.ic_action_addfav);
        nameTv = (TextView) findViewById(R.id.nameTv);
        posTv = (TextView) findViewById(R.id.posTv);
        partyTv = (TextView) findViewById(R.id.partyTv);
        wichpostTv = (TextView) findViewById(R.id.wichpostTv);
        educationTv = (TextView) findViewById(R.id.educationTv);
        ageTv = (TextView) findViewById(R.id.ageTv);
        rl_top_icons = (RelativeLayout) findViewById(R.id.rl_top_icons);
        rl_bottomView = (RelativeLayout) findViewById(R.id.rl_bottomView);


        Intent in = getIntent();

        nameTv.setText(in.getStringExtra("name"));
        posTv.setText(in.getStringExtra("post"));
        partyTv.setText(in.getStringExtra("party"));
        wichpostTv.setText(in.getStringExtra("whichpost"));
        educationTv.setText(in.getStringExtra("education"));
        ageTv.setText(in.getStringExtra("age"));
        String politicsid = in.getStringExtra("politicsid");


        if (favlist != null && favlist.size() != 0) {


            for (int i = 0; i < favlist.size(); i++) {

                if (favlist.get(i).getPoliticsid().equals(politicsid)) {

                    ic_action_addfav.setImageResource(R.drawable.dislike);
                    isFav = true;

                }

            }

        }


        ImageLoader.getInstance().displayImage("http://samyotechlabs.com/exitpoll/" + in.getStringExtra("img"), imageshow, options);

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
                startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
            }
        });

        ic_action_addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                preference.getBooleanValue(SharedPrefrence.IS_FAV);

                if (isFav) {

                    isFav = false;
                    deleteFav();

                    ic_action_addfav.setImageResource(R.drawable.like);


                } else {


                    isFav = true;
                    addFav();

                    ic_action_addfav.setImageResource(R.drawable.dislike);


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

    public void addFav() {
        networkTask = new NetworkTask(Consts.FAVOURITE_METHOD, getParam(), DetailsActivity.this);
        networkTask.execute(Consts.POST_METHOD);
        networkTask.setOnTaskFinishedEvent(new NetworkTask.AsyncResponse() {
            @Override
            public void processFinish(boolean output, String message) {
                Log.e("output", String.valueOf(output));
                if (output) {
                    Intent in = getIntent();
                    DialogUtility.pauseProgressDialog();
                    preference.setValue(SharedPrefrence.ID, in.getStringExtra("politicsid"));
                } else {
                    DialogUtility.pauseProgressDialog();
                }

            }

        });
    }

    public ContentValues getParam() {
        Intent in = getIntent();
        ContentValues values = new ContentValues();
        values.put(Consts.USEREMAIL, preference.getValue(SharedPrefrence.EMAIL));
        values.put(Consts.ID, in.getStringExtra("politicsid"));
        Log.e("politicsid", preference.getValue(SharedPrefrence.EMAIL));
        return values;
    }


    public void deleteFav() {
        networkTask = new NetworkTask(Consts.DELETEFAV_METHOD, getParamdelete(), DetailsActivity.this);
        networkTask.execute(Consts.POST_METHOD);
        networkTask.setOnTaskFinishedEvent(new NetworkTask.AsyncResponse() {
            @Override
            public void processFinish(boolean output, String message) {
                Log.e("output", String.valueOf(output));
                if (output) {
                    Intent in = getIntent();
                    DialogUtility.pauseProgressDialog();
                    preference.setValue(SharedPrefrence.ID, in.getStringExtra("politicsid"));
                } else {
                    DialogUtility.pauseProgressDialog();
                }

            }

        });
    }

    public ContentValues getParamdelete() {
        Intent in = getIntent();
        ContentValues values = new ContentValues();
        values.put(Consts.ID, in.getStringExtra("politicsid"));
        return values;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DetailsActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }
}
