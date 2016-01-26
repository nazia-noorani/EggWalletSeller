package com.egneese.sellers.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.egneese.sellers.R;
import com.egneese.sellers.asynctasks.ForgotPasswordAsyncTask;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.RegistrationDTO;
import com.egneese.sellers.ui.CustomEditText;
import com.egneese.sellers.ui.SnackBar;
import com.egneese.sellers.ui.TypefaceSpan;
import com.egneese.sellers.util.NetworkCheck;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 1/8/2016.
 */
public class ForgotPasswordActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.btnForgotPassword)
    Button btnForgotPassword;
    @InjectView(R.id.etPhoneNumber)
    CustomEditText etPhoneNumber;
    private int transition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        populate();
    }


    private void populate(){
        ButterKnife.inject(this);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SpannableString s = new SpannableString("Reset Password");
        s.setSpan(new TypefaceSpan(this, "LatoLatin-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle("");
        ((TextView)toolbar.findViewById(R.id.toolbarTitle)).setText(s);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = telephonyManager.getLine1Number();

        if(null != mPhoneNumber){
            etPhoneNumber.setText(mPhoneNumber);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            btnForgotPassword.setBackgroundResource(R.drawable.ripple);
        }

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkCheck.isNetworkAvailable(ForgotPasswordActivity.this)) {
                    RegistrationDTO userxDTO = new RegistrationDTO();
                    userxDTO.setMobile(etPhoneNumber.getText().toString());
                    if(userxDTO.getMobile().length() == 10) {

                        ForgotPasswordAsyncTask forgotPasswordAsyncTask = new ForgotPasswordAsyncTask(ForgotPasswordActivity.this, userxDTO);
                        forgotPasswordAsyncTask.execute();
                    }else{
                        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                        messageCustomDialogDTO.setTitle(getResources().getString(R.string.register_activity_invalid_phone_title));
                        messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                        messageCustomDialogDTO.setMessage(getResources().getString(R.string.register_activity_invalid_phone));
                        messageCustomDialogDTO.setContext(ForgotPasswordActivity.this);
                        SnackBar.show(ForgotPasswordActivity.this, messageCustomDialogDTO);
                    }
                }else{
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(getResources().getString(R.string.register_activity_no_internet_title));
                    messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(getResources().getString(R.string.register_activity_no_internet));
                    messageCustomDialogDTO.setContext(ForgotPasswordActivity.this);
                    SnackBar.show(ForgotPasswordActivity.this, messageCustomDialogDTO);
                }
            }
        });
    }

    protected void onResume()
    {
        super.onResume();
        transition = 0;
        super.onResume();
        if(!NetworkCheck.isNetworkAvailable(this))
            SnackBar.noInternet(this);
    }

    protected void onPause()
    {
        super.onPause();
        if(transition != 1)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
