package com.digidot.ishansupportsystem.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.adapter.TicketHistoryAdapter;
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

public class ViewTicketFragment extends Fragment {

    private Context mContext;

    private String userId = "0";
    private String ticketId, ticketNo = "", fault = "",
            date = "", description = "",ticketStatus;
    private APIService mApiService;
    private SharedPreferences pref;
    private TextView mTextviewTicketNoValue, mTextviewDateValue,
            mTextViewFaultValue, mTextviewDescriptionValue;
    private ImageView mImageView;
    private Ticket mTicket;
    private ArrayList<String> headerList;
    private HashMap<String, ArrayList<Ticket>> childList;
    private ExpandableListView mExpandableListView;
    public ViewTicketFragment() {
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
        View view = inflater.inflate(R.layout.fragment_view_ticket, container, false);
        init(view);
        Bundle bundle = this.getArguments();
        ticketId = bundle.getString(Constant.INTENT_PARAM_TICKET_ID);
        ticketNo = bundle.getString(Constant.INTENT_PARAM_TICKET_NO);
        fault = bundle.getString(Constant.INTENT_PARAM_TICKET_FAULT);
        date = bundle.getString(Constant.INTENT_PARAM_TICKET_DATE);
        description = bundle.getString(Constant.INTENT_PARAM_TICKET_DESCRIPTION);
        ticketStatus = bundle.getString(Constant.INTENT_PARAM_TICKET_STATUS);
        mTextviewTicketNoValue.setText(ticketNo);
        mTextviewDateValue.setText(date);
        mTextViewFaultValue.setText(fault);
        mTextviewDescriptionValue.setText(description);
        if (ticketStatus.equals(Constant.TICKET_STATUS_CLOSE)) {
            mImageView.setVisibility(View.GONE);
        }
        mApiService = ApiUtils.getAPIService();
        pref = mContext.getSharedPreferences("IffcoPref", 0);
        userId = pref.getString(Constant.PREF_KEY_USER_ID, "0");
        Log.e("User id", userId);
        getTicketHistory();
        setListener();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setListener() {
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                UpdateTicketFragment mUpdateTicketFragment = new UpdateTicketFragment();
                String dependencyCode = "";
                String broadCategory = "";
                String resolutionCode = "";
                if(mTicket != null){
                    dependencyCode = mTicket.getStrDependencyCode();
                    broadCategory = mTicket.getStrBroadCategory();
                    resolutionCode = mTicket.getStrResolutionCode();
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.INTENT_PARAM_TICKET_ID, ticketId);
                bundle.putString(Constant.INTENT_PARAM_TICKET_NO, ticketNo);
                bundle.putString(Constant.INTENT_PARAM_TICKET_DEPENDENCY_CODE, dependencyCode);
                bundle.putString(Constant.INTENT_PARAM_TICKET_BROAD_CATEGORY, broadCategory);
                bundle.putString(Constant.INTENT_PARAM_TICKET_RESOLUTION_CODE, resolutionCode);
                mUpdateTicketFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.frame, mUpdateTicketFragment).commit();
            }
        });
    }

    private void init(View view) {
        mImageView = view.findViewById(R.id.imageUpdateTicket);
        mTextviewTicketNoValue = view.findViewById(R.id.textviewTicketNoValue);
        mTextviewDateValue = view.findViewById(R.id.textviewDateValue);
        mTextViewFaultValue = view.findViewById(R.id.textViewFaultValue);
        mTextviewDescriptionValue = view.findViewById(R.id.textviewDescriptionValue);
        mExpandableListView = view.findViewById(R.id.expandable_history_data);
    }

    private void getTicketHistory() {

        Map<String, String> ticketHistoryFields = new HashMap<>();
        ticketHistoryFields.put("UserId", userId);
        ticketHistoryFields.put("TicketId", ticketId);

        mApiService.getTicketHistory(ticketHistoryFields).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    TicketResponse ticketResponse = response.body();
                    if (ticketResponse != null && ticketResponse.getTblTicket().size() > 0) {
                        mTicket = ticketResponse.getTblTicket().get(0);
                        headerList = new ArrayList<>();
                        childList = new HashMap<>();
                        for (Ticket ticket : ticketResponse.getTblTicket()){
                            String header = ticket.getStrTicketStatus() + " (" +
                                    ticket.getDtStatusDate() +")";
                            headerList.add(header);
                            ArrayList<Ticket> ticketArrayList = new ArrayList<>();
                            ticketArrayList.add(ticket);
                            childList.put(header,ticketArrayList);
                        }

                        TicketHistoryAdapter ticketHistoryAdapter = new TicketHistoryAdapter
                                (mContext, headerList, childList);
                        mExpandableListView.setAdapter(ticketHistoryAdapter);
                        ticketHistoryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, "Empty data", Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(mContext, "Unauthorized", Toast.LENGTH_LONG).show();
                } else if (response.code() == 500) {
                    Toast.makeText(mContext, "Internal server error", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Internal server error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext, "Unable to login due to server error");
            }
        });
    }
}
