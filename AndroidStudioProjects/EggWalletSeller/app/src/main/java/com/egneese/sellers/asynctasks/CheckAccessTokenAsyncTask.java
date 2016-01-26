package com.egneese.sellers.asynctasks;

/**
 * Created by nazianoorani on 21/01/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

import com.egneese.sellers.activities.AppInfoActivity;
import com.egneese.sellers.activities.DashboardActivity;
import com.egneese.sellers.activities.EnterProfileActivity;
import com.egneese.sellers.constants.NetworkConstants;
import com.egneese.sellers.dto.RequiredDTO;
import com.egneese.sellers.dto.SellerDTO;
import com.egneese.sellers.dto.SessionDTO;
import com.egneese.sellers.util.RequiredDTOFactory;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class CheckAccessTokenAsyncTask extends AsyncTask<Void, Void, Void> implements NetworkConstants {

    private Context context;
    private InputStream is;
    private Exception exceptionToBeThrown;
    private HttpEntity entity;
    private String result = "";
    private int statusCode = 0;

    public CheckAccessTokenAsyncTask(Context context) {
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... params) {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        RequiredDTO requiredDTO = RequiredDTOFactory.getObject(context);

        list.add(new BasicNameValuePair("required", new Gson().toJson(requiredDTO)));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(GET_NETWORK_IP + CHECK_ACCESS_TOKEN);

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
        if (exceptionToBeThrown != null) {
            exception();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(result);
                jsonObject = new JSONObject(jsonObject.getString("response"));

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                SessionDTO sessionDTO = gson.fromJson(sharedPreferences.getString("session", null), SessionDTO.class);

                if (jsonObject.getBoolean("isSuccess")) {
                    SellerDTO sellerDTO = gson.fromJson(jsonObject.toString(), SellerDTO.class);

                    sessionDTO.setSellerDTO(sellerDTO);

                    editor.putString("session", gson.toJson(sessionDTO));
                    editor.commit();

                    if (sharedPreferences.getBoolean("isProfileUpdated", false)) {
                        Intent intent = new Intent(context, DashboardActivity.class);
                        context.startActivity(intent);
                        ((ActionBarActivity) context).finish();
                    } else {
                        Intent intent = new Intent(context, EnterProfileActivity.class);
                        context.startActivity(intent);
                        ((ActionBarActivity) context).finish();
                    }
                } else {
                    sessionDTO.setSellerDTO(null);

                    editor.putString("session", gson.toJson(sessionDTO));
                    editor.putBoolean("isLogin", false);
                    editor.putBoolean("isProfileUpdated", false);
                    editor.commit();

                    Intent intent = new Intent(context, AppInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            } catch (Exception e) {
                exception();
            }
        }

    }


    private void exception() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (sharedPreferences.getBoolean("isProfileUpdated", false)) {
//            Intent intent = new Intent(context, DashboardActivity.class);
//            context.startActivity(intent);
            ((ActionBarActivity) context).finish();
        } else {
//            Intent intent = new Intent(context, EnterProfileActivity.class);
//            context.startActivity(intent);
            ((ActionBarActivity) context).finish();
        }
           /* final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_action);
            TextView text = (TextView) dialog.findViewById(R.id.txtDialogMessageTitle);
            text.setText("Error");
            text = (TextView) dialog.findViewById(R.id.txtDialogMessageMessage);
            text.setText(context.getResources().getString(R.string.server_error) + result);

            Button yes = (Button)dialog.findViewById(R.id.btnDismissDialogMessage);
            yes.setText("Cancel");
            yes.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    CheckAccessTokenAsyncTask.this.cancel(true);
                    ((ActionBarActivity) context).finish();
                }
            });
            Button no = (Button)dialog.findViewById(R.id.btnDialogAction);
            no.setText("Retry");
            no.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    CheckAccessTokenAsyncTask.this.cancel(true);
                    CheckAccessTokenAsyncTask checkAccessTokenAsyncTask = new CheckAccessTokenAsyncTask(context);
                    checkAccessTokenAsyncTask.execute();

                }
            });
            dialog.show();*/



           /* final MaterialDialog mMaterialDialog = new MaterialDialog(context);
            mMaterialDialog.setTitle("Network Error");
            mMaterialDialog.setMessage("Could not communicate with servers.")
                    .setPositiveButton("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CheckAccessTokenAsyncTask checkAccessTokenAsyncTask = new CheckAccessTokenAsyncTask(context, userxDTO);
                            checkAccessTokenAsyncTask.execute();
                        }
                    })
                    .setNegativeButton("CANCEL", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                            ((ActionBarActivity) context).finish();
                        }
                    });
            mMaterialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ((ActionBarActivity) context).finish();
                }
            });
            mMaterialDialog.show();
        */
    }
}
