package com.digidot.ishansupportsystem.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Spinner mSpinnerTicketStatus;
    private boolean initial  =true;
    private LinearLayout mLinearLayoutDateFilter;
    private EditText etToDate;
    private EditText etFromDate;
    private ImageView imgViewToDate;
    private ImageView imgViewFromDate;
    private Button mBtnApply;
    int mYear,mMonth,mDay;
    Calendar c;

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
        Constant.CURRENT_LOADED_FRAGMENT=Constant.FRAGMNET_TICKET_LIST;
        ((HomeActivity) getActivity()).setToolbarTitle(Constant.FRAGMNET_TICKET_LIST.toString());
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
        mLinearLayoutDateFilter=view.findViewById(R.id.linearLayoutDateFilter);
        mFloatingActionButton = view.findViewById(R.id.fab_chart);
        etToDate = view.findViewById(R.id.editTextToDate);
        etFromDate = view.findViewById(R.id.editTextFromDate);
        imgViewToDate=view.findViewById(R.id.imgViewToDate);
        imgViewFromDate=view.findViewById(R.id.imgViewFromDate);
        mBtnApply=view.findViewById(R.id.btnApply);
        etFromDate.setText(Utils.getInstance().getCurrentDate());
        etToDate.setText(Utils.getInstance().getCurrentDate());
        String isCreateRight=pref.getString(Constant.PREF_KEY_IS_CREATE_TICKET_RIGHT,"False");
        c = Calendar.getInstance();
        if(isCreateRight.equals("True")){
            mFloatingActionButton.setVisibility(View.VISIBLE);
        }else {
            mFloatingActionButton.setVisibility(View.GONE);
        }
        mBtnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTickets();
            }
        });
        imgViewToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                getToDate();
            }
        });

        imgViewFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                getFromDate();
            }
        });
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                CreateTicketFragment mCreateTicketFragment = new CreateTicketFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame, mCreateTicketFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        mSpinnerTicketStatus = view.findViewById(R.id.spinnerTicketStatus);
        mSpinnerTicketStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!initial){
                    ticketStatus = mSpinnerTicketStatus.getSelectedItem().toString();
                    if(ticketStatus.equals(Constant.TICKET_STATUS_CLOSE)){
                        mLinearLayoutDateFilter.setVisibility(View.VISIBLE);
                    }else {
                        mLinearLayoutDateFilter.setVisibility(View.GONE);
                    }
                    getTickets();
                }
                initial = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getTicketStatus();

        mRecyclerViewTicketList = view.findViewById(R.id.rvTicketList);
        mRecyclerViewTicketList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext,1);
        mRecyclerViewTicketList.setLayoutManager(layoutManager);
    }

    private void getToDate(){
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        c.set(year,monthOfYear,dayOfMonth);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        etToDate.setText(df.format(c.getTime()));

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getFromDate(){
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        c.set(year,monthOfYear,dayOfMonth);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        etFromDate.setText(df.format(c.getTime()));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getTicketStatus(){
        List<String> ticketStatusList=new ArrayList<>();
        ticketStatusList.add(0,Constant.TICKET_STATUS_OPEN);
        ticketStatusList.add(1,Constant.TICKET_STATUS_CLOSE);
        ticketStatusList.add(2,Constant.TICKET_STATUS_DEPENDENCY);

        ArrayAdapter dataAdapter = new ArrayAdapter(mContext, R.layout.custom_spinner_item, ticketStatusList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTicketStatus.setAdapter(dataAdapter);
    }

    private void getTickets(){

        Map<String,String> ticketFields=new HashMap<>();
        ticketFields.put("UserId",userId);
        ticketFields.put("TicketStatus",ticketStatus);
        if(ticketStatus.equals(Constant.TICKET_STATUS_CLOSE)){
            ticketFields.put("StartDate",etFromDate.getText().toString());
            ticketFields.put("EndDate",etToDate.getText().toString());
        }else {
            ticketFields.put("StartDate","");
            ticketFields.put("EndDate","");
        }
        ((HomeActivity)mContext).viewProgress();
        mApiService.getTickets(ticketFields).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                ((HomeActivity)mContext).hideProgress();
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
