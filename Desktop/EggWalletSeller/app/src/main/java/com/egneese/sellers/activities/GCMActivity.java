package com.egneese.sellers.activities;

/**
 * Created by nazianoorani on 21/01/16.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andraskindler.parallaxviewpager.ParallaxViewPager;
import com.egneese.sellers.GCMIntentService;
import com.egneese.sellers.R;
import com.egneese.sellers.adapters.CustomPagerAdapter;
import com.egneese.sellers.asynctasks.CheckAccessTokenAsyncTask;
import com.egneese.sellers.dto.SessionDTO;
import com.egneese.sellers.util.NetworkCheck;
import com.google.android.gcm.GCMRegistrar;

import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class GCMActivity extends ActionBarActivity {
    private SessionDTO sessionDTO;
    private Snackbar snackbar;
    private CountDownTimer countDownTimer, countDownTimer1;
    @InjectView(R.id.gcmparentlayout)
    RelativeLayout gcmparentlayout;
    CheckAccessTokenAsyncTask checkAccessTokenAsyncTask;
    private CustomPagerAdapter mCustomPagerAdapter;
    @InjectView(R.id.pager)
    ParallaxViewPager mViewPager;
    @InjectView(R.id.circleIndicatorAppInfo)
    ImageView circleIndicatorAppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        /*sessionDTO = new SessionDTO();
        sessionDTO.setGcmID("GCM");
        editor.putString("session", gson.toJson(sessionDTO));
        editor.commit();*/

        if (sharedPreferences.getString("session", null) == null) {
            sessionDTO = new SessionDTO();
            editor.putString("session", gson.toJson(sessionDTO));
            editor.commit();
            setContentView(R.layout.activity_gcm_activity);
            ButterKnife.inject(this);
            snackbar = Snackbar.make(gcmparentlayout, "No Internet Connection found !!!", Snackbar.LENGTH_INDEFINITE);
            showSilder();
            registerGCM();
        } else {
            sessionDTO = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
            if (sessionDTO.getGcmID() == null) {
                setContentView(R.layout.activity_gcm_activity);
                ButterKnife.inject(this);
                snackbar = Snackbar.make(gcmparentlayout, "No Internet Connection found !!!", Snackbar.LENGTH_INDEFINITE);
                showSilder();
                registerGCM();

            } else if (!sharedPreferences.getBoolean("isLogin", false)) {
                Intent intent = new Intent(this, AppInfoActivity.class);
                startActivity(intent);
                finish();
            } else {
                setContentView(R.layout.activity_splash);
//                snackbar = Snackbar.make(gcmparentlayout, "No Internet Connection found !!!", Snackbar.LENGTH_INDEFINITE);
                sessionDTO = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);

                checkAccessTokenAsyncTask = new CheckAccessTokenAsyncTask(this);
                checkAccessTokenAsyncTask.execute();


            }
        }

    }

    private void registerGCM() {

        if(!NetworkCheck.isNetworkAvailable(this)){
            if(!snackbar.isShown()) {
                snackbar.show();
            }

            countDownTimer1 = new CountDownTimer(300000, 1000) {
                public void onTick(long millisUntilFinished) {
                    if(NetworkCheck.isNetworkAvailable(GCMActivity.this)) {
                        snackbar.dismiss();
                        countDownTimer1.cancel();
                        registerGCM();
                    }
                }

                public void onFinish() {

                }
            }.start();

        }

        else{
            countDownTimer = new CountDownTimer(5000, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (sessionDTO.getGcmID() != null) {
                        countDownTimer.cancel();
                    }
                }

                public void onFinish() {
                    if (sessionDTO.getGcmID() == null) {
                        if(!snackbar.isShown()) {
                            snackbar.show();
                        }
                    }
                }
            }.start();

            try {
                GCMRegistrar.checkDevice(this);
                GCMRegistrar.checkManifest(this);
                GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed()
    {
        if (checkAccessTokenAsyncTask != null)
            checkAccessTokenAsyncTask.cancel(true);

        super.onBackPressed();
    }

    private void showSilder(){
        mCustomPagerAdapter = new CustomPagerAdapter(this);
        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_1);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setBackgroundResource(R.drawable.ic_launcher);
        mViewPager.setScaleType(ParallaxViewPager.FIT_WIDTH);
        mViewPager.setOverlapPercentage(0.147f);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_1);
                        break;
                    case 1:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_2);
                        break;
                    case 2:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_3);
                        break;
                    case 3:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_4);
                        break;
                    case 4:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_5);
                        break;
                    case 5:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_6);
                        break;
                    case 6:
                        circleIndicatorAppInfo.setImageResource(R.mipmap.loader_7);
                        break;
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {


            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
