package com.digidot.ishansupportsystem.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.activity.HomeActivity;
import com.digidot.ishansupportsystem.util.Constant;

public class HomeFragment extends Fragment {

    private Context mContext;
    private CardView mCardViewTodaysOpenTicket;
    private CardView mCardViewTotalOpenTicket;
    private CardView mCardViewTotalDependencyTicket;
    private CardView mCardViewTotalCloseTicket;

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
        Constant.CURRENT_LOADED_FRAGMENT=Constant.FRAGMNET_HOME;
        ((HomeActivity) getActivity()).setToolbarTitle(Constant.FRAGMNET_HOME.toString());
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

    private void initView(View view){
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
    }

    private void loadFragment(String status){
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
}
