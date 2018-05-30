package com.digidot.ishansupportsystem.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.activity.HomeActivity;
import com.digidot.ishansupportsystem.model.City;
import com.digidot.ishansupportsystem.model.CityResponse;
import com.digidot.ishansupportsystem.model.DeshboardHome;
import com.digidot.ishansupportsystem.model.DeshboardHomeResponse;
import com.digidot.ishansupportsystem.retrofit.APIService;
import com.digidot.ishansupportsystem.retrofit.ApiUtils;
import com.digidot.ishansupportsystem.util.Constant;
import com.digidot.ishansupportsystem.util.Utils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private Context mContext;
    private CardView mCardViewTodaysOpenTicket;
    private CardView mCardViewTotalOpenTicket;
    private CardView mCardViewTotalDependencyTicket;
    private CardView mCardViewTotalCloseTicket;

    private TextView mTextViewOpenTicketNumber;
    private TextView mTextViewTotalOpenTicketNumber;
    private TextView mTextViewDependencyTicketNumber;
    private TextView mTextViewCloseTicketNumber;

    private String userId = "0";
    private APIService mApiService;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Constant.CURRENT_LOADED_FRAGMENT = Constant.FRAGMNET_HOME;
        ((HomeActivity) getActivity()).setToolbarTitle(Constant.FRAGMNET_HOME.toString());
        mApiService = ApiUtils.getAPIService();
        SharedPreferences pref = mContext.getSharedPreferences("IffcoPref", 0);
        userId = pref.getString(Constant.PREF_KEY_USER_ID, "0");
        initView(view);
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

    private void initView(View view) {
        mCardViewTodaysOpenTicket = view.findViewById(R.id.card_todays_open_ticket);
        mCardViewTotalOpenTicket = view.findViewById(R.id.card_total_open_ticket);
        mCardViewTotalDependencyTicket = view.findViewById(R.id.card_total_dependency_ticket);
        mCardViewTotalCloseTicket = view.findViewById(R.id.card_total_close_ticket);

        mCardViewTodaysOpenTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(Constant.TICKET_STATUS_OPEN);
            }
        });

        mCardViewTotalOpenTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(Constant.TICKET_STATUS_OPEN);
            }
        });

        mCardViewTotalDependencyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(Constant.TICKET_STATUS_DEPENDENCY);
            }
        });

        mCardViewTotalCloseTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(Constant.TICKET_STATUS_CLOSE);
            }
        });

        mTextViewOpenTicketNumber = view.findViewById(R.id.text_todays_open_ticket_number);
        mTextViewTotalOpenTicketNumber = view.findViewById(R.id.text_total_open_ticket_number);
        mTextViewDependencyTicketNumber = view.findViewById(R.id.text_total_dependency_ticket_number);
        mTextViewCloseTicketNumber = view.findViewById(R.id.text_card_total_close_ticket_number);

        getDeshboardData();
    }

    private void loadFragment(String status) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        TicketListFragment mTicketListFragment = new TicketListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TICKET_STATUS, status);
        mTicketListFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.frame, mTicketListFragment)
                .addToBackStack(null)
                .commit();
    }

    private void getDeshboardData() {

        Map<String, String> dashboardData = new HashMap<>();
        dashboardData.put("UserId", userId);

        mApiService.getDashboardCount(dashboardData).enqueue(new Callback<DeshboardHomeResponse>() {
            @Override
            public void onResponse(Call<DeshboardHomeResponse> call, Response<DeshboardHomeResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    DeshboardHomeResponse deshboardHomeResponse = response.body();
                    if (deshboardHomeResponse != null && deshboardHomeResponse.getTblHome().size() > 0) {
                        for(DeshboardHome deshboardHome:deshboardHomeResponse.getTblHome()){
                            switch (deshboardHome.getHeader()) {
                                case "Closed":
                                    mTextViewCloseTicketNumber.setText(deshboardHome.getTotal());
                                    break;
                                case "Dependency":
                                    mTextViewDependencyTicketNumber.setText(deshboardHome.getTotal());
                                    break;
                                case "Today_Open":
                                    mTextViewOpenTicketNumber.setText(deshboardHome.getTotal());
                                    break;
                                case "Total_Open":
                                    mTextViewTotalOpenTicketNumber.setText(deshboardHome.getTotal());
                                    break;
                            }

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DeshboardHomeResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext, "Unable to login due to server error");
            }
        });
    }
}
