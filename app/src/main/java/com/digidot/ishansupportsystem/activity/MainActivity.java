package com.digidot.ishansupportsystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.digidot.ishansupportsystem.R;
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
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public static Logger logger=Logger.getLogger("MainActivity");

    private RecyclerView mRecyclerViewTicketList;
    private List<Ticket> mListTicket;
    private FloatingActionButton mFloatingActionButton;
    private String userId = "0";
    private Spinner mSpinnerTicketStatus;
    private APIService mApiService;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApiService= ApiUtils.getAPIService();
        pref=getApplicationContext().getSharedPreferences("IffcoPref",0);
        userId = pref.getString(Constant.PREF_KEY_USER_ID,"0");
        Log.e("User id", userId);
        initView();
    }

    private void initView(){

        mFloatingActionButton = findViewById(R.id.fab_chart);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this,CreateTicketActivity.class);
                startActivity(mIntent);
            }
        });

        mSpinnerTicketStatus = findViewById(R.id.spinnerTicketStatus);
        getTicketStatus();
        mSpinnerTicketStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getTickets();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRecyclerViewTicketList = findViewById(R.id.rvTicketList);
        mRecyclerViewTicketList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        mRecyclerViewTicketList.setLayoutManager(layoutManager);
    }

    private void getTicketStatus(){
        List<String> ticketStatusList=new ArrayList<>();
        ticketStatusList.add(0,Constant.TICKET_STATUS_OPEN);
        ticketStatusList.add(1,Constant.TICKET_STATUS_CLOSE);
        ticketStatusList.add(2,Constant.TICKET_STATUS_DEPENDENCY);

        ArrayAdapter dataAdapter = new ArrayAdapter(MainActivity.this, R.layout.custom_spinner_item, ticketStatusList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTicketStatus.setAdapter(dataAdapter);
    }

    private void getTickets(){

        Map<String,String> ticketFields=new HashMap<>();
        ticketFields.put("UserId",userId);
        ticketFields.put("TicketStatus",mSpinnerTicketStatus.getSelectedItem().toString());

        mApiService.getTickets(ticketFields).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                List<Ticket> ticketList = new ArrayList<>();
                if(response.isSuccessful() && response.code()==200) {
                    TicketResponse ticketResponse = response.body();
                    if(ticketResponse!=null && ticketResponse.getTblTicket().size()> 0){
                        TicketListAdapter adapter = new TicketListAdapter(MainActivity.this, ticketResponse.getTblTicket());
                        mRecyclerViewTicketList.setAdapter(adapter);
                    }else{
                        TicketListAdapter adapter = new TicketListAdapter(MainActivity.this
                                ,new ArrayList<Ticket>());
                        mRecyclerViewTicketList.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(),"Empty data",Toast.LENGTH_LONG).show();
                    }
                }else if(response.code()==401){
                    Toast.makeText(getApplicationContext(),"Unauthorized",Toast.LENGTH_LONG).show();
                }else if(response.code()==500){
                    Toast.makeText(getApplicationContext(),"Internal server error",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Internal server error",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Utils.getInstance().showDialog(MainActivity.this,"Unable to login due to server error");
            }
        });
    }

}
