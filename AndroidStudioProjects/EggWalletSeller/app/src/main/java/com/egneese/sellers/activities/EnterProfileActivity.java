package com.egneese.sellers.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.egneese.sellers.R;
import com.egneese.sellers.asynctasks.EnterProfileAsyncTask;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.RequestDTO;
import com.egneese.sellers.dto.RequiredDTO;
import com.egneese.sellers.dto.SellerDTO;
import com.egneese.sellers.dto.SessionDTO;
import com.egneese.sellers.ui.CustomEditText;
import com.egneese.sellers.ui.SnackBar;
import com.egneese.sellers.ui.TypefaceSpan;
import com.egneese.sellers.util.NetworkCheck;
import com.egneese.sellers.util.RequiredDTOFactory;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 1/8/2016.
 */
public class EnterProfileActivity extends ActionBarActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.txtUpdateProfileTitle)
    TextView txtUpdatProfileTitle;
    @InjectView(R.id.etName)
    CustomEditText etName;
    @InjectView(R.id.etEmail)
    CustomEditText etEmail;
    @InjectView(R.id.etMobile)
    CustomEditText etMobile;

    @InjectView(R.id.btnContinue)
    Button btnContinue;
    @InjectView(R.id.txtSelectCity)
    com.neopixl.pixlui.components.textview.TextView txtSelectCity;
    @InjectView(R.id.txtGender)
    com.neopixl.pixlui.components.textview.TextView txtGender;
    @InjectView(R.id.txtDOB)
    com.neopixl.pixlui.components.textview.TextView txtDOB;


    private SessionDTO sessionDTO;
    private SellerDTO sellerDTO;
    private String dob = "";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_profile);
        populate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!NetworkCheck.isNetworkAvailable(this))
            SnackBar.noInternet(this);
    }

    private void populate() {
        ButterKnife.inject(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
        sellerDTO = sessionDTO.getSellerDTO();

        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        setSupportActionBar(toolbar);
        SpannableString s = new SpannableString("Complete Profile");
        s.setSpan(new TypefaceSpan(this, "LatoLatin-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnContinue.setBackgroundResource(R.drawable.ripple);
        }


        s = new SpannableString("PERSONAL INFORMATION");
        s.setSpan(new TypefaceSpan(this, "ProximaNova-Semibold.otf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtUpdatProfileTitle.setText(s);

        etMobile.setFocusable(false);
        etMobile.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        etMobile.setClickable(false); // user navigates with wheel and selects widget

        if (sellerDTO.getEmail() != null) {
            etEmail.setText(sellerDTO.getEmail());
        }
        if (sellerDTO.getMobile() != null) {
            etMobile.setText(sellerDTO.getMobile());
        }

        txtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        StringBuffer dobBuffer = new StringBuffer();
                        dobBuffer.append(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat outFmt = new SimpleDateFormat("dd-MMMM-yyyy");
                        try {
                            dob = dobBuffer.toString();
                            txtDOB.setText(outFmt.format(inFmt.parse(dobBuffer.toString())));
                            txtDOB.setAlpha(1.0f);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(EnterProfileActivity.this, datePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        txtGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] genders = {"Male", "Female"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(EnterProfileActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Select Gender");
                builder.setSingleChoiceItems(genders, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtGender.setText(genders[which]);
                        txtGender.setAlpha(1.0f);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        txtSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(EnterProfileActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {

                } catch (GooglePlayServicesNotAvailableException e) {

                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequiredDTO requiredDTO = RequiredDTOFactory.getObject(EnterProfileActivity.this);
                RequestDTO requestDTO = new RequestDTO();

                requestDTO.setName(etName.getText().toString().trim());
                requestDTO.setEmail(etEmail.getText().toString().trim());
                if(!(txtSelectCity.getText().toString().equals("Address")))
                    requestDTO.setAddress(txtSelectCity.getText().toString().trim());
                if(!(txtGender.getText().toString().equals("Gender")))
                    requestDTO.setGender(txtGender.getText().toString().trim());
                requestDTO.setDob(dob);

                if (NetworkCheck.isNetworkAvailable(EnterProfileActivity.this)) {
                        EnterProfileAsyncTask enterProfileAsyncTask = new EnterProfileAsyncTask(EnterProfileActivity.this, requiredDTO, requestDTO);
                        enterProfileAsyncTask.execute();
                } else {
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(getResources().getString(R.string.gcm_activity_no_internet_title));
                    messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(getResources().getString(R.string.gcm_activity_no_internet));
                    messageCustomDialogDTO.setContext(EnterProfileActivity.this);
                    SnackBar.show(EnterProfileActivity.this, messageCustomDialogDTO);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                SpannableString s = new SpannableString(place.getAddress());
                s.setSpan(new TypefaceSpan(this, "roboto_light.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtSelectCity.setAlpha(1.0f);
                txtSelectCity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                txtSelectCity.setText(s);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

            } else if (resultCode == RESULT_CANCELED) {

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_enter_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_skip){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isProfileUpdated", true);
            editor.commit();

            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}





