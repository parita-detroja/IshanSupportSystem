package com.digidot.ishansupportsystem.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.activity.HomeActivity;
import com.digidot.ishansupportsystem.adapter.TicketListAdapter;
import com.digidot.ishansupportsystem.model.Ticket;
import com.digidot.ishansupportsystem.model.TicketResponse;
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

public class TicketListFragment extends Fragment {

    private Context mContext;
    private RecyclerView mRecyclerViewTicketList;
    private List<Ticket> mListTicket;
    private FloatingActionButton mFloatingActionButton;
    private String userId = "0";
    private APIService mApiService;
    private SharedPreferences pref;
    private  String ticketStatus;

    public TicketListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ticket_list, container, false);
        mApiService = ApiUtils.getAPIService();
        pref = mContext.getSharedPreferences("IffcoPref", 0);
        userId = pref.getString(Constant.PREF_KEY_USER_ID, "0");
        Log.e("User id", userId);
        initView(view);
        Bundle bundle = this.getArguments();
        ticketStatus = bundle.getString(Constant.TICKET_STATUS);
        getTickets();
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

    private void initView(View view){

        mFloatingActionButton = view.findViewById(R.id.fab_chart);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                CreateTicketFragment mCreateTicketFragment = new CreateTicketFragment();
                fragmentManager.beginTransaction().replace(R.id.frame, mCreateTicketFragment).commit();
            }
        });

        mRecyclerViewTicketList = view.findViewById(R.id.rvTicketList);
        mRecyclerViewTicketList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext,1);
        mRecyclerViewTicketList.setLayoutManager(layoutManager);
    }

    private void getTickets(){

        Map<String,String> ticketFields=new HashMap<>();
        ticketFields.put("UserId",userId);
        ticketFields.put("TicketStatus",ticketStatus);

        mApiService.getTickets(ticketFields).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                List<Ticket> ticketList = new ArrayList<>();
                if(response.isSuccessful() && response.code()==200) {
                    TicketResponse ticketResponse = response.body();
                    if(ticketResponse!=null && ticketResponse.getTblTicket().size()> 0){
                        TicketListAdapter adapter = new TicketListAdapter((HomeActivity) mContext, ticketResponse.getTblTicket());
                        mRecyclerViewTicketList.setAdapter(adapter);
                    }else{
                        TicketListAdapter adapter = new TicketListAdapter((HomeActivity)mContext
                                ,new ArrayList<Ticket>());
                        mRecyclerViewTicketList.setAdapter(adapter);
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
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext,"Unable to login due to server error");
            }
        });
    }
}
