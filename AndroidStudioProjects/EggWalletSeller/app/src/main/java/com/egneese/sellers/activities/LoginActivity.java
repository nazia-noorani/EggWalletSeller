package com.egneese.sellers.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.egneese.sellers.R;
import com.egneese.sellers.asynctasks.LoginAsyncTask;
import com.egneese.sellers.constants.EggWallet;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.RegistrationDTO;
import com.egneese.sellers.ui.CustomEditText;
import com.egneese.sellers.ui.CustomTouchListener;
import com.egneese.sellers.ui.SnackBar;
import com.egneese.sellers.util.NetworkCheck;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 1/8/2016.
 */
public class LoginActivity extends ActionBarActivity implements EggWallet {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.btnLogin)
    Button btnLogin;
    @InjectView(R.id.etPhoneNumber)
    CustomEditText etPhoneNumber;
    @InjectView(R.id.etPassword)
    CustomEditText etPassword;
    @InjectView(R.id.txtForgotPassword)
    com.neopixl.pixlui.components.textview.TextView txtForgotPassword;
    @InjectView(R.id.txtShowPassword)
    com.neopixl.pixlui.components.textview.TextView txtShowPassword;
    private int transition = 0;
    @InjectView(R.id.llLogin)
    LinearLayout llLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        populate();
    }


    private void populate() {
        ButterKnife.inject(this);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
     //   etPhoneNumber.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
      //  etPassword.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        txtForgotPassword.setOnTouchListener(new CustomTouchListener());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setSupportActionBar(toolbar);
        ((TextView)toolbar.findViewById(R.id.toolbarTitle)).setText("Login");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            btnLogin.setBackgroundResource(R.drawable.ripple);
        }

        getSupportActionBar().setTitle("");

       /* TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = telephonyManager.getLine1Number();
        if(null != mPhoneNumber){
            etPhoneNumber.setText(mPhoneNumber);
        }*/
        txtShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtShowPassword.getText().toString().equals("show")){
                    etPassword.setTransformationMethod(null);
                    txtShowPassword.setText("hide");
                    etPassword.setSelection(etPassword.getText().toString().length());

                }else{
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    txtShowPassword.setText("show");
                    etPassword.setSelection(etPassword.getText().toString().length());
                }
            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationDTO registrationDTO = new RegistrationDTO();
                registrationDTO.setMobile(etPhoneNumber.getText().toString().trim());
                registrationDTO.setPassword(etPassword.getText().toString().trim());
                if (NetworkCheck.isNetworkAvailable(LoginActivity.this)) {
                    if (registrationDTO.getMobile().length() == 10) {
                        if (registrationDTO.getPassword().length() != 0) {
                            LoginAsyncTask loginAsyncTask = new LoginAsyncTask(LoginActivity.this, registrationDTO);
                            loginAsyncTask.execute();
                        } else {
                            MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                            messageCustomDialogDTO.setTitle(getResources().getString(R.string.login_activity_invalid_password_title));
                            messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                            messageCustomDialogDTO.setMessage(getResources().getString(R.string.login_activity_invalid_password));
                            messageCustomDialogDTO.setContext(LoginActivity.this);
                            SnackBar.show(LoginActivity.this, messageCustomDialogDTO);
                        }
                    } else {
                        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                        messageCustomDialogDTO.setTitle(getResources().getString(R.string.login_activity_invalid_phone_title));
                        messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                        messageCustomDialogDTO.setMessage(getResources().getString(R.string.login_activity_invalid_phone));
                        messageCustomDialogDTO.setContext(LoginActivity.this);
                        SnackBar.show(LoginActivity.this, messageCustomDialogDTO);
                    }
                } else {
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(getResources().getString(R.string.login_activity_no_internet_title));
                    messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(getResources().getString(R.string.login_activity_no_internet));
                    messageCustomDialogDTO.setContext(LoginActivity.this);
                    SnackBar.show(LoginActivity.this, messageCustomDialogDTO);
                }

            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transition = 1;
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(new MyGestureDetector());
        llLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) return false;
                return false;
            }
        });
    }
    protected void onResume()
    {
        super.onResume();
        transition = 0;
        if(!NetworkCheck.isNetworkAvailable(this))
            SnackBar.noInternet(this);
    }

    protected void onPause()
    {
        super.onPause();
        if(transition != 1)
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
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

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float slope = (e1.getY() - e2.getY()) / (e1.getX() - e2.getX());
                float angle = (float) Math.atan(slope);
                float angleInDegree = (float) Math.toDegrees(angle);
                if (e1.getX() - e2.getX() > 20 && Math.abs(velocityX) > 20) {
                    if ((angleInDegree < 45 && angleInDegree > -45)) {
                        finish();
                    }
                } else if (e2.getX() - e1.getX() > 20 && Math.abs(velocityX) > 20) {
                    if ((angleInDegree < 45 && angleInDegree > -45)) {
                        finish();
                    }
                }
                return true;
            } catch (Exception e) {
            }
            return false;
        }
    }

}
