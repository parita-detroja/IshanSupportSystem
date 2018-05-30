package com.digidot.ishansupportsystem.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.activity.HomeActivity;
import com.digidot.ishansupportsystem.adapter.NotificationCustomAdapter;
import com.digidot.ishansupportsystem.model.Notification;
import com.digidot.ishansupportsystem.model.NotificationResponse;
import com.digidot.ishansupportsystem.model.Ticket;
import com.digidot.ishansupportsystem.retrofit.APIService;
import com.digidot.ishansupportsystem.retrofit.ApiUtils;
import com.digidot.ishansupportsystem.util.Constant;
import com.digidot.ishansupportsystem.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment{

    private Context mContext;
    RecyclerView recyclerView;
    private String userId = "0";
    private SharedPreferences pref;
    private APIService mApiService;
    private ImageView btnRefresh;
    boolean refreshFlag=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        Constant.CURRENT_LOADED_FRAGMENT=Constant.FRAGMNET_NOTIFICATIONS;
        ((HomeActivity) getActivity()).setToolbarTitle(Constant.FRAGMNET_NOTIFICATIONS.toString());
        mApiService = ApiUtils.getAPIService();
        pref = mContext.getSharedPreferences("IffcoPref", 0);
        userId = pref.getString(Constant.PREF_KEY_USER_ID, "0");
        Log.e("User id", userId);
        String notificationId = pref.getString(Constant.PREF_KEY_NOTIFICATION_ID_LIST, "0");
        recyclerView = (RecyclerView)view.findViewById(R.id.rvNotificationTickets);
        btnRefresh= view.findViewById(R.id.btnRefresh);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshNotificationData();
            }
        });
        getNotifications(notificationId);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void refreshNotificationData(){
        refreshFlag=true;
        getNotifications(pref.getString(Constant.PREF_KEY_NOTIFICATION_ID_LIST,"0"));

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void getNotifications(String notificationId){
        Map<String,String> notificationFields=new HashMap<>();
        notificationFields.put("UserId",userId);
        notificationFields.put("NotificationId",notificationId);

        mApiService.getNotifications(notificationFields).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if(response.isSuccessful() && response.code()==200) {
                    NotificationResponse notificationResponse = response.body();
                    if(notificationResponse!=null){
                        if(refreshFlag){
                            NotificationCustomAdapter notificationCustomAdapter =(NotificationCustomAdapter) recyclerView.getAdapter();
                            notificationCustomAdapter.refreshListData(notificationResponse.getTblNotifications());
                            refreshFlag=false;
                            Toast.makeText(getActivity(),"Refreshed notification data",Toast.LENGTH_LONG).show();
                        }else{
                            NotificationCustomAdapter adapter = new NotificationCustomAdapter((
                                    HomeActivity) mContext, notificationResponse.getTblNotifications());
                            recyclerView.setAdapter(adapter);
                        }
                        if(notificationResponse.getTblNotifications().size()>0){
                            Notification notification=notificationResponse.getTblNotifications().get(notificationResponse.getTblNotifications().size()-1);
                            pref.edit().putString(Constant.PREF_KEY_NOTIFICATION_ID,notification.getIntNotificationId()).apply();
                        }
                     }else{
                        Toast.makeText(mContext,"Empty data",Toast.LENGTH_LONG).show();
                    }
                }else if(response.code()==401){
                    Toast.makeText(mContext,"Unauthorized",Toast.LENGTH_LONG).show();
                }else if(response.code()==500){
                    Toast.makeText(mContext,"Internal server error",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext,"Internal server error",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext,"Unable to login due to server error");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter mIntentFilter=new IntentFilter("RefreshAction");
        getActivity().registerReceiver(broadcastReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("RefreshAction")){
                refreshNotificationData();
            }
        }
    };
}
