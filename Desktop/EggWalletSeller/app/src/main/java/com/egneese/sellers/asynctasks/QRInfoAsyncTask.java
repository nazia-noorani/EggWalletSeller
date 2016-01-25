package com.egneese.sellers.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;

import com.egneese.sellers.R;
import com.egneese.sellers.constants.NetworkConstants;
import com.egneese.sellers.dto.ErrorDTO;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.RequestDTO;
import com.egneese.sellers.dto.RequiredDTO;
import com.egneese.sellers.dto.SellerDTO;
import com.egneese.sellers.dto.SessionDTO;
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
 * Created by adityaagrawal on 12/01/16.
 */
public class QRInfoAsyncTask extends AsyncTask<Void, Void, Void> implements NetworkConstants {

    private Context context;
    private SweetAlertDialog pDialog;
    private InputStream is;
    private Exception exceptionToBeThrown;
    private HttpEntity entity;
    private String result = "";
    private int statusCode = 0;
    private RequestDTO requestDTO;
    private RequiredDTO requiredDTO;
    private SessionDTO sessionDTO;
    private SharedPreferences.Editor sharedEditor;
    private SharedPreferences sharedPreferences;
    private LinearLayout headerView;

    public QRInfoAsyncTask(Context context, RequiredDTO requiredDTO, RequestDTO requestDTO, LinearLayout headerView) {
        this.context = context;
        this.requiredDTO = requiredDTO;
        this.requestDTO = requestDTO;
        this.headerView = headerView;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
        sharedEditor = sharedPreferences.edit();
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
            HttpPost httpPost = new HttpPost(GET_NETWORK_IP + GET_QR_INFO_URL);

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
                    jsonObject = jsonObject.getJSONObject("response");
                    Gson gson = new Gson();
                    SellerDTO sellerDTO = sessionDTO.getSellerDTO();
                    sellerDTO.setId(jsonObject.getString("id"));
                    sellerDTO.setAccessToken(jsonObject.getString("accessToken"));
                    sellerDTO.setMobile(jsonObject.getString("mobile"));

                    sessionDTO.setSellerDTO(sellerDTO);
                    sharedEditor.putString("session", gson.toJson(sessionDTO));
                    sharedEditor.commit();

                    final MaterialDialog mMaterialDialog = new MaterialDialog(context).setTitle("Pay Confirmation").setMessage(jsonObject.getString("confirmationMessage"));
                    mMaterialDialog.setPositiveButton("Pay", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                            PayAsyncTask payAsyncTask = new PayAsyncTask(context, requestDTO, headerView);
                            payAsyncTask.execute();
                        }
                    });

                    mMaterialDialog.setNegativeButton("CANCEL", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                        }
                    });

                    mMaterialDialog.show();
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
            messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.error_message));
            SnackBar.show(context, messageCustomDialogDTO);
        }
    }
}
