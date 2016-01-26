package com.egneese.sellers.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.egneese.sellers.constants.EggWallet;
import com.egneese.sellers.dto.RequiredDTO;
import com.egneese.sellers.dto.SellerDTO;
import com.egneese.sellers.dto.SessionDTO;
import com.google.gson.Gson;


/**
 * Created by Dell on 1/8/2016.
 */
public class RequiredDTOFactory {

    public static RequiredDTO getObject(Context context){
        RequiredDTO requiredDTO = new RequiredDTO();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SessionDTO sessionDTO = new Gson().fromJson(sharedPreferences.getString("session", null), SessionDTO.class);
        SellerDTO buyerDTO = sessionDTO.getSellerDTO();

        requiredDTO.setId(buyerDTO.getId());
        requiredDTO.setAccessToken(buyerDTO.getAccessToken());
        requiredDTO.setScope(EggWallet.SCOPE);
        requiredDTO.setMobile(buyerDTO.getMobile());

        return requiredDTO;
    }
}