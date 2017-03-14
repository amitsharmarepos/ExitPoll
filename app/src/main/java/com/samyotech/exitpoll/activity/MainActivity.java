package com.samyotech.exitpoll.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.adapter.AlbumsAdapter;
import com.samyotech.exitpoll.dto.Album;
import com.samyotech.exitpoll.dto.Favdto;
import com.samyotech.exitpoll.https.NetworkTask;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;
import com.samyotech.exitpoll.utils.Consts;
import com.samyotech.exitpoll.utils.DialogUtility;
import com.samyotech.exitpoll.utils.ProjectUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public Context mContext;
    public RecyclerView recyclerView;
    public AlbumsAdapter adapter;
    public ArrayList<Album> albumList;
    public ArrayList<Favdto> favlist;
    SharedPrefrence preference;
    NetworkTask networkTask;
    ImageView imgg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProjectUtils.showAnalytics((SysApplication) getApplication(), "MainActivity");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preference = SharedPrefrence.getInstance(this);

        initCollapsingToolbar();
        mContext = new MainActivity();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        try {

            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        albumList = new ArrayList<>();
        albumList = preference.getList(SharedPrefrence.POLITATION_RECORD);

        if (albumList.size() == 0) {
            getDATA();
        } else {


            Log.e("albumList", albumList.toString());
            adapter = new AlbumsAdapter(this, albumList);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }

    public void getDATA() {
        DialogUtility.showProgressDialog(this, false, "Please wait...");
        networkTask = new NetworkTask(Consts.DATA_METHOD, getParam(), MainActivity.this);
        networkTask.execute(Consts.POST_METHOD);
        networkTask.setOnTaskFinishedEvent(new NetworkTask.AsyncResponse() {
            @Override
            public void processFinish(boolean output, String message) {

                Log.e("output", String.valueOf(output));
                if (output) {
                    check();
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

    public void check() {
        albumList = new ArrayList<>();
        albumList = preference.getList(SharedPrefrence.POLITATION_RECORD);
        Log.e("albumList", albumList.toString());
        adapter = new AlbumsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_viewfev:
                startActivity(new Intent(MainActivity.this, ViewActivity.class));
                break;

            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, Settings.class));
                break;
           /* case R.id.action_updatepro:
                startActivity(new Intent(MainActivity.this, UpdateprofileActivity.class));
                break;
            case R.id.action_resetpass:
                startActivity(new Intent(MainActivity.this, ResetpassActivity.class));
                break;
            case R.id.action_logout:
                preference.clearPreferences(SharedPrefrence.IS_LOGIN);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;*/

        }
        return true;
    }

    public void getallFav() {

        networkTask = new NetworkTask(Consts.ALLFAV_METHOD, getParamss(), MainActivity.this);
        networkTask.execute(Consts.POST_METHOD);
        networkTask.setOnTaskFinishedEvent(new NetworkTask.AsyncResponse() {
            @Override
            public void processFinish(boolean output, String message) {

                Log.e("output", String.valueOf(output));
                if (output) {
//                    favlist = new ArrayList<>();
//                    favlist = preference.getFavFList(SharedPrefrence.FAV_RECORD);
//                    Log.e("albumList", favlist.toString());
                    Log.e("allfavresponse", "<-- get allfavresponse detail..." + preference.getFavFList(SharedPrefrence.FAV_RECORD));


                } else {

                    DialogUtility.pauseProgressDialog();
                }

            }

        });
    }

    public ContentValues getParamss() {
        ContentValues values = new ContentValues();
        values.put(Consts.USEREMAIL, preference.getValue(SharedPrefrence.EMAIL));
        return values;
    }

    @Override
    protected void onResume() {
        getallFav();
        super.onResume();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
