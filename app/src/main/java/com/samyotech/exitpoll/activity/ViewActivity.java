package com.samyotech.exitpoll.activity;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.adapter.Recycler_View_Adapter;
import com.samyotech.exitpoll.dto.Album;
import com.samyotech.exitpoll.https.NetworkTask;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;
import com.samyotech.exitpoll.utils.Consts;
import com.samyotech.exitpoll.utils.DialogUtility;
import com.samyotech.exitpoll.utils.ProjectUtils;

import java.util.ArrayList;


public class ViewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    public ArrayList<Album> albumList;
    SharedPrefrence preference;
    NetworkTask networkTask;
    private Recycler_View_Adapter adapter;
    LinearLayout backUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewsfav);
        ProjectUtils.showAnalytics((SysApplication) getApplication(), "ViewActivity");
        preference = SharedPrefrence.getInstance(this);
        viewFav();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        backUP = (LinearLayout) findViewById(R.id.backUP);
        backUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                finish();
            }
        });

    }

    public void viewFav() {
        DialogUtility.showProgressDialog(this, false, "Please wait...");
        networkTask = new NetworkTask(Consts.VIEWFAV_METHOD, getParam(), ViewActivity.this);
        networkTask.execute(Consts.POST_METHOD);
        networkTask.setOnTaskFinishedEvent(new NetworkTask.AsyncResponse() {
            @Override
            public void processFinish(boolean output, String message) {

                Log.e("output", String.valueOf(output));
                if (output) {
                    check();
                    //Log.e("POLITATION_RECORD", "<-- get POLITATION detail..." + preference.getList(SharedPrefrence.POLITATION_RECORD));

                    DialogUtility.pauseProgressDialog();

                } else {

                    DialogUtility.pauseProgressDialog();
                }

            }

        });
    }

    public ContentValues getParam() {
        ContentValues values = new ContentValues();
        values.put(Consts.USEREMAIL, preference.getValue(SharedPrefrence.EMAIL));
        return values;
    }

    public void check() {
        albumList = new ArrayList<>();
        albumList = preference.getList(SharedPrefrence.VIEW_POLITATION_RECORD);
        adapter = new Recycler_View_Adapter(albumList, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

}
