package com.egneese.sellers.security;

import com.egneese.sellers.constants.EggWallet;
import com.egneese.sellers.dto.QRDataDTO;
import com.google.gson.Gson;

import org.hashids.Hashids;

/**
 * Created by adityaagrawal on 12/01/16.
 */
public class Encryption {
    public static String encrypt(String data) throws Exception {
        Hashids hashids = new Hashids(EggWallet.SALT);
        Gson gson = new Gson();
        QRDataDTO qrDataDTO = gson.fromJson(data, QRDataDTO.class);
        String hashPart = hashids.encodeHex(qrDataDTO.getRequestId());
        data = qrDataDTO.getRequestType() + hashPart;
        return data;

    }

    public static String decrypt(String encypt) throws Exception {
        Hashids hashids = new Hashids(EggWallet.SALT);
        QRDataDTO qrDataDTO = new QRDataDTO();

        qrDataDTO.setRequestType(encypt.substring(0, 4));
        String data = encypt.substring(4, encypt.length());

        qrDataDTO.setRequestId(hashids.decodeHex(data));

        Gson gson = new Gson();
        data = gson.toJson(qrDataDTO);
        return data;
    }

}
