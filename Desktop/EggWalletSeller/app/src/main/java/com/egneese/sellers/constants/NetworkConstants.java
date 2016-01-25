package com.egneese.sellers.constants;

/**
 * Created by Dell on 1/4/2016.
 */
public interface NetworkConstants {
    String GET_NETWORK_IP = "http://www.eggwallet.com:80/api/";

    String REGISTER_URL = "Sellers/register";
    String CONFIRM_OTP_URL = "Sellers/verifyMobile";
    String RESEND_OTP_URL = "Sellers/resendMobileOtp";
    String CHECK_ACCESS_TOKEN = "Sellers/verifyMobileCredentials";
    String LOGIN_URL = "Sellers/login";
    String LOGOUT_URL = "Sellers/logout";
    String PROFILE_UPDATE_URL = "Sellers/updateProfile";
    String FORGOT_PASSWORD_URL = "Sellers/forgotPassword";
    String RESET_PASSWORD_URL = "Sellers/resetPassword";



    String REQUEST_MONEY_URL = "Sellers/requestMoney";
    String REQUEST_QR_MONEY_URL = "Sellers/requestMoney";
    String GET_QR_INFO_URL = "Sellers/getRequestedData";
    String PAY_URL = "Sellers/payRequestedMoney";
    String CHECK_MOBILE_PAYMENT_DATA = "Sellers/checkMobilePaymentData";
    String MOBILE_PAYMENT_DATA = "Sellers/payViaMobile";
    String LOAD_TRANSACTION_URL ="Sellers/getTransactions?query=QUERY";


    String CHANGE_PASSWORD_URL = "/Sellers/changePassword";

}
