package com.digidot.ishansupportsystem.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.activity.HomeActivity;
import com.digidot.ishansupportsystem.adapter.NotificationCustomAdapter;
import com.digidot.ishansupportsystem.adapter.TicketListAdapter;
import com.digidot.ishansupportsystem.model.Notification;
import com.digidot.ishansupportsystem.model.NotificationResponse;
import com.digidot.ishansupportsystem.model.Ticket;
import com.digidot.ishansupportsystem.model.TicketResponse;
import com.digidot.ishansupportsystem.retrofit.APIService;
import com.digidot.ishansupportsystem.retrofit.ApiUtils;
import com.digidot.ishansupportsystem.util.Constant;
import com.digidot.ishansupportsystem.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class NotificationFragment extends Fragment{

    private Context mContext;
    static final int PICK_FILTER_REQUEST = 1;
    private Button btnDateTime;
    private Button btnCategory;
    RecyclerView recyclerView;
    List<Notification> notificationList;
    private String userId = "0";
    private String notificationId = "0";
    private SharedPreferences pref;
    private APIService mApiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        mApiService = ApiUtils.getAPIService();
        pref = mContext.getSharedPreferences("IffcoPref", 0);
        userId = pref.getString(Constant.PREF_KEY_USER_ID, "0");
        Log.e("User id", userId);
        notificationId = pref.getString(Constant.PREF_KEY_NOTIFICATION_ID, "0");
        Log.e("User id", notificationId);
        recyclerView = (RecyclerView)view.findViewById(R.id.rvNotificationTickets);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        getNotifications();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void getNotifications(){

        Map<String,String> notificationFields=new HashMap<>();
        notificationFields.put("UserId",userId);
        notificationFields.put("NotificationId",notificationId);

        mApiService.getNotifications(notificationFields).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                List<Ticket> ticketList = new ArrayList<>();
                if(response.isSuccessful() && response.code()==200) {
                    NotificationResponse notificationResponse = response.body();
                    if(notificationResponse!=null && notificationResponse.getTblNotifications().size()> 0){
                        NotificationCustomAdapter adapter = new NotificationCustomAdapter((
                                HomeActivity) mContext, notificationResponse.getTblNotifications());
                        recyclerView.setAdapter(adapter);
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
}
