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
import com.egneese.sellers.asynctasks.RequestMoneyAsyncTask;
import com.egneese.sellers.asynctasks.RequestMoneyQRCodeAsyncTask;
import com.egneese.sellers.constants.EggWallet;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.RequestMoneyDTO;
import com.egneese.sellers.dto.RequestMoneyJSONDTO;
import com.egneese.sellers.dto.SellerDTO;
import com.egneese.sellers.ui.SnackBar;
import com.egneese.sellers.ui.TypefaceSpan;
import com.egneese.sellers.util.NetworkCheck;
import com.egneese.sellers.util.SessionDTODFactory;
import com.rey.material.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by adityaagrawal on 10/01/16.
 */
public class RequestMoneyActivity extends ActionBarActivity implements View.OnClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.etAmount)
    EditText etAmount;
    @InjectView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @InjectView(R.id.etDisc)
    EditText etDisc;
    @InjectView(R.id.btnRequestMoney)
    Button btnRequestMoney;
    @InjectView(R.id.btnPickContact)
    ImageView btnPickContact;
    @InjectView(R.id.btnGenerateCode)
    Button btnGenerateCode;
    private int RQS_PICK_CONTACT = 1;
    private SellerDTO sellerDTO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_money);
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
            btnRequestMoney.setBackgroundResource(R.drawable.ripple_green);
        }

        sellerDTO = SessionDTODFactory.getSellerDTO(this);

        SpannableString s = new SpannableString("Request Money");
        s.setSpan(new TypefaceSpan(this, "roboto_light.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        btnPickContact.setOnClickListener(this);
        btnRequestMoney.setOnClickListener(this);
        btnGenerateCode.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkCheck.isNetworkAvailable(this))
            SnackBar.noInternet(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPickContact:
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent, RQS_PICK_CONTACT);
                break;
            case R.id.btnRequestMoney:
                if (NetworkCheck.isNetworkAvailable(this)) {
                    if (!(etAmount.getText().toString().trim().equals(""))) {
                        try {
                            RequestMoneyDTO requestMoneyDTO = new RequestMoneyDTO();
                            requestMoneyDTO.setMobile(etPhoneNumber.getText().toString());
                            requestMoneyDTO.setDesc(etDisc.getText().toString());
                            requestMoneyDTO.setAmount(Double.parseDouble(etAmount.getText().toString()));
                            requestMoneyDTO.setRequestType(EggWallet.BUYER_REQ_WR_TYPE);

                            if (requestMoneyDTO.getMobile().length() == 10) {
                                RequestMoneyAsyncTask requestMoneyAsyncTask = new RequestMoneyAsyncTask(this, requestMoneyDTO);
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

            case R.id.btnGenerateCode:
                if (!(etAmount.getText().toString().trim().equals(""))) {
                    double amount = Double.parseDouble(etAmount.getText().toString());

                    RequestMoneyJSONDTO requestMoneyJSONDTO = new RequestMoneyJSONDTO();
                    requestMoneyJSONDTO.setAmount(amount);
                    requestMoneyJSONDTO.setRequestType(EggWallet.BUYER_REQ_WR_TYPE);
                    requestMoneyJSONDTO.setId(sellerDTO.getId());
                    requestMoneyJSONDTO.setDesc(etDisc.getText().toString());


                    if (requestMoneyJSONDTO.getAmount() > 1000) {
                        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                        messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                        messageCustomDialogDTO.setMessage(getResources().getString(R.string.amount_must_be_less));
                        messageCustomDialogDTO.setContext(this);
                        SnackBar.show(this, messageCustomDialogDTO);
                    } else {

                        RequestMoneyQRCodeAsyncTask requestMoneyQRCodeAsyncTask = new RequestMoneyQRCodeAsyncTask(this, requestMoneyJSONDTO);
                        requestMoneyQRCodeAsyncTask.execute();

                    }
                } else {
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(getResources().getString(R.string.empty_amount));
                    messageCustomDialogDTO.setContext(this);
                    SnackBar.show(this, messageCustomDialogDTO);
                }
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
