package com.egneese.sellers.asynctasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.egneese.sellers.R;
import com.egneese.sellers.activities.QRCodeDisplayActivity;
import com.egneese.sellers.constants.NetworkConstants;
import com.egneese.sellers.dto.ErrorDTO;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.QRDataDTO;
import com.egneese.sellers.dto.RequestMoneyJSONDTO;
import com.egneese.sellers.dto.SellerDTO;
import com.egneese.sellers.dto.SessionDTO;
import com.egneese.sellers.security.Encryption;
import com.egneese.sellers.ui.SnackBar;
import com.egneese.sellers.util.QRCodeGenerator;
import com.egneese.sellers.util.RequiredDTOFactory;
import com.google.gson.Gson;
import com.google.zxing.WriterException;

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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by adityaagrawal on 12/01/16.
 */
public class RequestMoneyQRCodeAsyncTask extends AsyncTask<Void, Void, Void> implements NetworkConstants {

    private Context context;
    private SweetAlertDialog pDialog;
    private RequestMoneyJSONDTO requestMoneyJSONDTO;
    private InputStream is;
    private Exception exceptionToBeThrown;
    private HttpEntity entity;
    private String result = "";
    private int statusCode = 0;
    private SessionDTO sessionDTO;
    private SharedPreferences.Editor sharedEditor;
    private SharedPreferences sharedPreferences;

    public RequestMoneyQRCodeAsyncTask(Context context, RequestMoneyJSONDTO requestMoneyJSONDTO) {
        this.context = context;
        this.requestMoneyJSONDTO = requestMoneyJSONDTO;
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
        list.add(new BasicNameValuePair("required", gson.toJson(RequiredDTOFactory.getObject(context))));
        list.add(new BasicNameValuePair("data", gson.toJson(requestMoneyJSONDTO)));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GET_NETWORK_IP + REQUEST_QR_MONEY_URL);

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
                    Bitmap bitmap;
                    SellerDTO sellerDTO = sessionDTO.getSellerDTO();
                    sellerDTO.setId(jsonObject.getString("id"));
                    sellerDTO.setAccessToken(jsonObject.getString("accessToken"));
                    sellerDTO.setMobile(jsonObject.getString("mobile"));

                    sessionDTO.setSellerDTO(sellerDTO);
                    sharedEditor.putString("session", gson.toJson(sessionDTO));
                    sharedEditor.commit();

                    try {


                        QRDataDTO qrDataDTO = new QRDataDTO();
                        qrDataDTO.setRequestType(requestMoneyJSONDTO.getRequestType());
                        qrDataDTO.setRequestId(jsonObject.getString("requestId"));

                        String hash = Encryption.encrypt(gson.toJson(qrDataDTO));

                        Toast.makeText(context.getApplicationContext(), hash + qrDataDTO.toString(), Toast.LENGTH_SHORT).show();
                        bitmap = QRCodeGenerator.generate(hash);
                        String filename = "bitmap.png";
                        FileOutputStream stream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        stream.close();
                        bitmap.recycle();

                        Intent intent = new Intent(context, QRCodeDisplayActivity.class);
                        intent.putExtra("image", filename);
                        context.startActivity(intent);

                    } catch (WriterException e) {
                        e.printStackTrace();
                        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                        messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                        messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.error_message));
                        messageCustomDialogDTO.setContext(context);
                        SnackBar.show(context, messageCustomDialogDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                        MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                        messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                        messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.error_message));
                        messageCustomDialogDTO.setContext(context);
                        SnackBar.show(context, messageCustomDialogDTO);
                    }

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
