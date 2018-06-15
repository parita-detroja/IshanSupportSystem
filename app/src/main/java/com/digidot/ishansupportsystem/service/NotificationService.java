package com.digidot.ishansupportsystem.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.activity.HomeActivity;
import com.digidot.ishansupportsystem.activity.LoginActivity;
import com.digidot.ishansupportsystem.model.Notification;
import com.digidot.ishansupportsystem.model.NotificationResponse;
import com.digidot.ishansupportsystem.retrofit.APIService;
import com.digidot.ishansupportsystem.retrofit.ApiUtils;
import com.digidot.ishansupportsystem.retrofit.RetrofitClient;
import com.digidot.ishansupportsystem.util.Constant;
import com.digidot.ishansupportsystem.util.Helper;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ABC on 26-01-2018.
 */

public class NotificationService extends Service {
    public static String TAG="NotificationService";
    Handler handler;
    int delay = 150000; //milliseconds
    APIService mApiService;
    SharedPreferences pref;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Service Method : onCreate() started");
        pref=this.getSharedPreferences("IffcoPref",0);
        Constant.BASE_URL=pref.getString(Constant.PREF_KEY_API_LINK, Constant.BASE_URL);
        RetrofitClient.setRetrofit(null);
        mApiService= ApiUtils.getAPIService();
     }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i(TAG,"Service Method : onStartCommand() started");
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
               getNotificationList();
               handler.postDelayed(this, delay);
            }
        }, delay);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Service Method : onDestroy() started");
        Intent intent=new Intent(this, NotificationService.class);
        startService(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getNotificationList(){
        Log.i(TAG,"base url = "+Constant.BASE_URL);
        Map<String,String> notificationFields=new HashMap<>();
        notificationFields.put("UserId",pref.getString(Constant.PREF_KEY_USER_ID,"0"));
        notificationFields.put("NotificationId",pref.getString(Constant.PREF_KEY_NOTIFICATION_ID,"0"));
        mApiService.getNotifications(notificationFields).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if(response.isSuccessful()) {
                    NotificationResponse notificationResponse=response.body();
                    if(notificationResponse!=null && notificationResponse.getTblNotifications()!=null){
                        Log.i(TAG, "got event data");
                        for (Notification notification :notificationResponse.getTblNotifications()) {
                            if(Constant.CURRENT_LOADED_FRAGMENT.equals(Constant.FRAGMNET_NOTIFICATIONS)){
                                Intent homeIntent=new Intent();
                                homeIntent.setAction("RefreshAction");
                                sendBroadcast(homeIntent);
                            }
                            boolean notificationFlag=pref.getBoolean(Constant.PREF_KEY_NOTIFICATION_ACTIVATION_KEY,true);
                            if(notificationFlag){
                                sendNotification(notification);
                            }
                            if (Helper.isAppRunning(getApplicationContext(), "com.digidot.ishansupportsystem")) {
                                Log.i(TAG,"App is running");
                            } else {
                                //sendNotification(notification);
                                Log.i(TAG,"App is not running");
                            }
                            pref.edit().putString(Constant.PREF_KEY_NOTIFICATION_ID_LIST,pref.getString(Constant.PREF_KEY_NOTIFICATION_ID,"0")).apply();
                            pref.edit().putString(Constant.PREF_KEY_NOTIFICATION_ID,notification.getIntNotificationId()).apply();
                        }
                    }else{
                        Log.e(TAG, "null body");
                    }
                }else if(response.code()==401){
                    Log.e(TAG,"Unauthorized");
                }else if(response.code()==500){
                    Log.e(TAG,"Internal server error");
                }else{
                    Log.e(TAG,"Error in login");
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {

            }
        });
    }

    public void sendNotification(Notification notification){
        if(notification.getStrNotificationTitle().length()>20){
            notification.setStrNotificationTitle(notification.getStrNotificationTitle().substring(0,20));
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Ishan - New notification generated")
                        .setContentText(notification.getStrDetails());
        Intent resultIntent;
        if(pref.getBoolean(Constant.PREF_KEY_LOGIN,false)){
            resultIntent = new Intent(this, HomeActivity.class);
        }else {
            resultIntent = new Intent(this, LoginActivity.class);
        }

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 001;
    // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}