package com.digidot.ishansupportsystem.retrofit;

import android.util.Log;

import com.digidot.ishansupportsystem.util.Constant;


/**
 * Created by ABC on 25-01-2018.
 */

public class ApiUtils {
    private ApiUtils() {}

    public static APIService getAPIService() {
        Log.i("Base url = ", Constant.BASE_URL);
        return RetrofitClient.getClient(Constant.BASE_URL).create(APIService.class);
    }
}
