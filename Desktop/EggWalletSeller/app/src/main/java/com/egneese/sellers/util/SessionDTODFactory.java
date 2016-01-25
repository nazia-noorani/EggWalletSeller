package com.egneese.sellers.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.egneese.sellers.dto.SellerDTO;
import com.egneese.sellers.dto.SessionDTO;
import com.google.gson.Gson;

/**
 * Created by adityaagrawal on 05/01/16.
 */
public class SessionDTODFactory {
    public static SessionDTO getSessionDTO(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SessionDTO sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
        return sessionDTO;
    }

    public static SellerDTO getSellerDTO(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SessionDTO sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
        SellerDTO sellerDTO = sessionDTO.getSellerDTO();
        return sellerDTO;
    }

}
