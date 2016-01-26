
package com.egneese.sellers.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.egneese.sellers.R;
import com.egneese.sellers.asynctasks.RegisterAsyncTask;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.RegistrationDTO;
import com.egneese.sellers.dto.SessionDTO;
import com.egneese.sellers.ui.CustomEditText;
import com.egneese.sellers.ui.SnackBar;
import com.egneese.sellers.ui.TypefaceSpan;
import com.egneese.sellers.util.NetworkCheck;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Dell on 1/4/2016.
 */

public class RegisterActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.btnRegister)
    Button btnRegister;
    @InjectView(R.id.etPhoneNumber)
    CustomEditText etPhoneNumber;
    @InjectView(R.id.etShopName)
    CustomEditText etShopName;
    SessionDTO sessionDTO;
    @InjectView(R.id.txtAgreement)
    TextView txtAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        populate();
    }


    private void populate() {
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        SpannableString s = new SpannableString("Sign Up");
        s.setSpan(new TypefaceSpan(this, "LatoLatin-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) toolbar.findViewById(R.id.toolbarTitle)).setText(s);

        getSupportActionBar().setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnRegister.setBackgroundResource(R.drawable.ripple);
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = telephonyManager.getLine1Number();
        if (null != mPhoneNumber) {
            etPhoneNumber.setText(mPhoneNumber);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);


        s = new SpannableString(getResources().getString(R.string.agreement_sentence));

        ClickableSpan termsSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(RegisterActivity.this, LoadURLActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("action", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ClickableSpan privacySpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(RegisterActivity.this, LoadURLActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("action", 2);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan contentSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(RegisterActivity.this, LoadURLActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("action", 3);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        s.setSpan(termsSpan, 40, 57, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black_steel)), 40, 57, 0);
        s.setSpan(new RelativeSizeSpan(1.1f), 40, 57, 0);

        s.setSpan(privacySpan, 58, 73, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black_steel)), 58, 73, 0);
        s.setSpan(new RelativeSizeSpan(1.1f), 58, 73, 0);

        s.setSpan(contentSpan, 77, 94, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black_steel)), 77, 94, 0);
        s.setSpan(new RelativeSizeSpan(1.1f), 77, 94, 0);


        txtAgreement.setText(s);
        txtAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        txtAgreement.setHighlightColor(Color.TRANSPARENT);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkCheck.isNetworkAvailable(RegisterActivity.this)) {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    RegistrationDTO registrationDTO = new RegistrationDTO();

                    registrationDTO.setShopName(etShopName.getText().toString().trim());
                    registrationDTO.setMobile(etPhoneNumber.getText().toString().trim());
                    registrationDTO.setDeviceId(telephonyManager.getDeviceId());
                    registrationDTO.setLocale(telephonyManager.getSimCountryIso().toUpperCase());

                    if (!(registrationDTO.getMobile().length() == 10)) {
                        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                        messageCustomDialogDTO.setTitle(getResources().getString(R.string.register_activity_invalid_phone_title));
                        messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                        messageCustomDialogDTO.setMessage(getResources().getString(R.string.register_activity_invalid_phone));
                        messageCustomDialogDTO.setContext(RegisterActivity.this);
                        SnackBar.show(RegisterActivity.this, messageCustomDialogDTO);
                    } else {
                        RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask(RegisterActivity.this, registrationDTO);
                        registerAsyncTask.execute();
                    }
                } else {
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(getResources().getString(R.string.gcm_activity_no_internet_title));
                    messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(getResources().getString(R.string.gcm_activity_no_internet));
                    messageCustomDialogDTO.setContext(RegisterActivity.this);
                    SnackBar.show(RegisterActivity.this, messageCustomDialogDTO);
                }
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start_application, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_cancel) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        if (!NetworkCheck.isNetworkAvailable(this))
            SnackBar.noInternet(this);
    }

    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

    }

}

