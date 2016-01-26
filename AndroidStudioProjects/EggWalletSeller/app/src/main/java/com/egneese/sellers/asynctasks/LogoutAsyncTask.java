package com.egneese.sellers.asynctasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.egneese.sellers.R;
import com.egneese.sellers.activities.AppInfoActivity;
import com.egneese.sellers.constants.NetworkConstants;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.RequestDTO;
import com.egneese.sellers.dto.RequiredDTO;
import com.egneese.sellers.dto.SessionDTO;
import com.egneese.sellers.ui.SnackBar;
import com.egneese.sellers.util.RequiredDTOFactory;
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

/**
 * Created by Dell on 1/8/2016.
 */
public class LogoutAsyncTask extends AsyncTask<Void, Void, Void> implements NetworkConstants {

    RequiredDTO requiredDTO;
    private Context context;
    private SweetAlertDialog pDialog;
    private InputStream is;
    private Exception exceptionToBeThrown;
    private HttpEntity entity;
    private String result = "";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public LogoutAsyncTask(Context context) {
        this.context = context;
        requiredDTO = RequiredDTOFactory.getObject(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        editor = sharedPreferences.edit();
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

        list.add(new BasicNameValuePair("required", new Gson().toJson(requiredDTO)));
        list.add(new BasicNameValuePair("data", new Gson().toJson(new RequestDTO())));
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GET_NETWORK_IP + LOGOUT_URL);

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            entity = httpResponse.getEntity();
            is = entity.getContent();
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
        if (exceptionToBeThrown != null) {
            if (exceptionToBeThrown instanceof HttpHostConnectException) {
                MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
                messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.login_activity_no_internet));
                messageCustomDialogDTO.setContext(context);
                SnackBar.show(context, messageCustomDialogDTO);
            } else if (exceptionToBeThrown instanceof UnknownHostException) {
                MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
                messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.login_activity_no_internet));
                messageCustomDialogDTO.setContext(context);
                SnackBar.show(context, messageCustomDialogDTO);
            } else {
                MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
                messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                messageCustomDialogDTO.setContext(context);
                messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.error_message));
                SnackBar.show(context, messageCustomDialogDTO);
            }
        } else {
            try {
                JSONObject jsonObject = new JSONObject(result);

                if (true == jsonObject.getBoolean("response")) {
                    Gson gson = new Gson();
                    SessionDTO sessionDTO = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
                    sessionDTO.setSellerDTO(null);

                    editor.putString("session", gson.toJson(sessionDTO));
                    editor.putBoolean("isLogin", false);
                    editor.putBoolean("isProfileUpdated", false);
                    editor.commit();

                    Intent intent = new Intent(context, AppInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
                    messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.error_message));
                    messageCustomDialogDTO.setContext(context);
                    SnackBar.show(context, messageCustomDialogDTO);
                }
            } catch (Exception e) {
                MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                messageCustomDialogDTO.setTitle(context.getResources().getString(R.string.oops));
                messageCustomDialogDTO.setButton(context.getResources().getString(R.string.ok));
                messageCustomDialogDTO.setContext(context);
                messageCustomDialogDTO.setMessage(context.getResources().getString(R.string.error_message));
                SnackBar.show(context, messageCustomDialogDTO);
            }
        }
    }
}