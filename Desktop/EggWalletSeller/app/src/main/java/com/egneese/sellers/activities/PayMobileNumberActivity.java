package com.egneese.sellers.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.egneese.sellers.R;
import com.egneese.sellers.asynctasks.PayMobileNumberAsyncTask;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.RequestDTO;
import com.egneese.sellers.dto.SellerDTO;
import com.egneese.sellers.ui.SnackBar;
import com.egneese.sellers.ui.TypefaceSpan;
import com.egneese.sellers.util.NetworkCheck;
import com.egneese.sellers.util.RequiredDTOFactory;
import com.egneese.sellers.util.SessionDTODFactory;
import com.rey.material.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by adityaagrawal on 13/01/16.
 */
public class PayMobileNumberActivity extends ActionBarActivity implements View.OnClickListener{
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.etAmount)
    EditText etAmount;
    @InjectView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @InjectView(R.id.btnPayMoney)
    Button btnPayMoney;
    @InjectView(R.id.btnPickContact)
    ImageView btnPickContact;
    private int RQS_PICK_CONTACT = 1;
    private SellerDTO sellerDTO = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_phone_number);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnPayMoney.setBackgroundResource(R.drawable.ripple_green);
        }

        sellerDTO = SessionDTODFactory.getSellerDTO(this);

        SpannableString s = new SpannableString("Pay Money");
        s.setSpan(new TypefaceSpan(this, "roboto_light.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        btnPickContact.setOnClickListener(this);
        btnPayMoney.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPayMoney:
                if (NetworkCheck.isNetworkAvailable(this)) {
                    if (!(etAmount.getText().toString().trim().equals(""))) {
                        try {
                            RequestDTO requestDTO = new RequestDTO();
                            requestDTO.setMobile(etPhoneNumber.getText().toString());
                            requestDTO.setAmount(Double.parseDouble(etAmount.getText().toString()));

                            if (requestDTO.getMobile().length() == 10) {
                                PayMobileNumberAsyncTask requestMoneyAsyncTask = new PayMobileNumberAsyncTask(this, RequiredDTOFactory.getObject(this),requestDTO );
                                requestMoneyAsyncTask.execute();
                            } else {
                                MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                                messageCustomDialogDTO.setTitle(getResources().getString(R.string.login_activity_invalid_phone_title));
                                messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                                messageCustomDialogDTO.setMessage(getResources().getString(R.string.login_activity_invalid_phone));
                                messageCustomDialogDTO.setContext(this);
                                SnackBar.show(this, messageCustomDialogDTO);
                            }
                        } catch (Exception e) {
                            MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                            messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                            messageCustomDialogDTO.setMessage(getResources().getString(R.string.invalid_amount));
                            messageCustomDialogDTO.setContext(this);
                            SnackBar.show(this, messageCustomDialogDTO);
                        }
                    } else {
                        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                        messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                        messageCustomDialogDTO.setMessage(getResources().getString(R.string.empty_amount));
                        messageCustomDialogDTO.setContext(this);
                        SnackBar.show(this, messageCustomDialogDTO);
                    }
                } else {
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(getResources().getString(R.string.login_activity_no_internet_title));
                    messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(getResources().getString(R.string.login_activity_no_internet));
                    messageCustomDialogDTO.setContext(this);
                    SnackBar.show(this, messageCustomDialogDTO);
                }
                break;
            case R.id.btnPickContact:
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContactIntent, RQS_PICK_CONTACT);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RQS_PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri contactUri = data.getData();
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                    Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                    cursor.moveToFirst();
                    int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(column);
                    number = number.replaceAll(" ", "");
                    if (number.length() == 10)
                        etPhoneNumber.setText(number);
                    else if (number.length() == 11) {
                        number = number.substring(1);
                        if (number.length() == 10)
                            etPhoneNumber.setText(number);
                        else {
                            showError();
                        }
                    } else {
                        number = number.substring(3);
                        if (number.length() == 10)
                            etPhoneNumber.setText(number);
                        else {
                            showError();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showError();
                }
            }
        }
    }

    private void showError() {
        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
        messageCustomDialogDTO.setTitle(getResources().getString(R.string.login_activity_invalid_phone_title));
        messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
        messageCustomDialogDTO.setMessage(getResources().getString(R.string.login_activity_invalid_phone));
        messageCustomDialogDTO.setContext(this);
        SnackBar.show(this, messageCustomDialogDTO);
    }
}
