package com.egneese.sellers.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;

import com.egneese.sellers.R;
import com.egneese.sellers.constants.NetworkConstants;
import com.egneese.sellers.dto.ErrorDTO;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.RequestDTO;
import com.egneese.sellers.dto.RequiredDTO;
import com.egneese.sellers.dto.SellerDTO;
import com.egneese.sellers.dto.SessionDTO;
import com.egneese.sellers.dto.WalletDTO;
import com.egneese.sellers.ui.SnackBar;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by adityaagrawal on 14/01/16.
 */
public class PayMobileNumberAsyncTask extends AsyncTask<Void, Void, Void> implements NetworkConstants {

    private Context context;
    private SweetAlertDialog pDialog;
    private RequiredDTO requiredDTO;
    private RequestDTO requestDTO;
    private InputStream is;
    private Exception exceptionToBeThrown;
    private HttpEntity entity;
    private SessionDTO sessionDTO;
    private String result = "";
    private int statusCode = 0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Double amount = 0.0;

    public PayMobileNumberAsyncTask(Context context, RequiredDTO requiredDTO, RequestDTO requestDTO) {
        this.context = context;
        this.requiredDTO = requiredDTO;
        this.requestDTO = requestDTO;
        amount = requestDTO.getAmount();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        editor = sharedPreferences.edit();
        sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
    }

    @Override
    protected void onPreExecute() {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.primary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        Gson gson = new Gson();

        list.add(new BasicNameValuePair("required", gson.toJson(requiredDTO)));
        list.add(new BasicNameValuePair("data", gson.toJson(requestDTO)));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GET_NETWORK_IP + CHECK_MOBILE_PAYMENT_DATA);

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            entity = httpResponse.getEntity();
            is = entity.getContent();
            statusCode = httpResponse.getStatusLine().getStatusCode();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line);
            is.close();
            result = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionToBeThrown = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pDialog.dismissWithAnimation();

        try {

            if (exceptionToBeThrown != null) {
                exceptionToBeThrown.printStackTrace();
                if (exceptionToBeThrown instanceof HttpHostConnectException) {
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
                    messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.login_activity_no_internet));
                    messageCustomDialogDTO.setContext(context);
                    SnackBar.show(context, messageCustomDialogDTO);
                } else if(exceptionToBeThrown instanceof UnknownHostException){
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
                    messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.login_activity_no_internet));
                    messageCustomDialogDTO.setContext(context);
                    SnackBar.show(context, messageCustomDialogDTO);
                }else {
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
                    messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setContext(context);
                    messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.error_message));
                    SnackBar.show(context, messageCustomDialogDTO);
                }
            } else {

                JSONObject jsonObject = new JSONObject(result);


                if (statusCode >= 200 && statusCode <= 299) {
                    Gson gson = new Gson();
                    jsonObject = new JSONObject(jsonObject.getString("response"));

                    final String id = jsonObject.getString("id");
                    WalletDTO walletDTO = gson.fromJson(jsonObject.getString("wallet"), WalletDTO.class);
                    SellerDTO sellerDTO = sessionDTO.getSellerDTO();
                    sellerDTO.setWallet(walletDTO);
                    sessionDTO.setSellerDTO(sellerDTO);
                    editor.putString("session", gson.toJson(sessionDTO));
                    editor.commit();

                    final MaterialDialog mMaterialDialog = new MaterialDialog(context).setTitle("Pay Confirmation").setMessage(jsonObject.getString("message"));
                    mMaterialDialog.setPositiveButton("Pay", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                            RequestDTO requestDTO = new RequestDTO();
                            requestDTO.setId(id);
                            requestDTO.setAmount(amount);
                            ConfirmPayMobileAsyncTask confirmPayMobileAsyncTask = new ConfirmPayMobileAsyncTask(context, requiredDTO, requestDTO);
                            confirmPayMobileAsyncTask.execute();
                        }
                    });

                    mMaterialDialog.setNegativeButton("CANCEL", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                        }
                    });

                    mMaterialDialog.show();
                    /*
                    Intent intent = new Intent(context, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                } else {
                    ErrorDTO errorDTO = new Gson().fromJson(jsonObject.getString("error"), ErrorDTO.class);

                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(errorDTO.getName());
                    messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(errorDTO.getMessage());
                    messageCustomDialogDTO.setContext(context);
                    SnackBar.show(context, messageCustomDialogDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
            messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
            messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
            messageCustomDialogDTO.setContext(context);
            messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.error_message) + result );
            SnackBar.show(context, messageCustomDialogDTO);
        }
    }
}
