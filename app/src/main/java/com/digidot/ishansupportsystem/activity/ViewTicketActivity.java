package com.digidot.ishansupportsystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTicketActivity extends AppCompatActivity {
    private String userId = "0";
    private String ticketId,ticketNo="",fault="",date="",description="",dependency="",broadcategory="",
            resolutionCode="",ticketStatus="",ticketDate="",dependencyCode="";
    private APIService mApiService;
    private SharedPreferences pref;
    private TextView mTextviewTicketNoValue,mTextviewDateValue,
                        mTextViewFaultValue,mTextviewDescriptionValue,mTextviewStatusDateValue,
                        mTextviewTicketStatusValue,mTextViewDependencyCodeValue,
                        mTextviewResolutionCodeValue,mTextviewBroadCategoryValue;
    private LinearLayout mLinearLayoutDependencyCode,mLinearLayoutResolutionCode,mLinearLayoutBroadCategory;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);

        init();
        ticketId=getIntent().getStringExtra(Constant.INTENT_PARAM_TICKET_ID);
        ticketNo=getIntent().getStringExtra(Constant.INTENT_PARAM_TICKET_NO);
        fault=getIntent().getStringExtra(Constant.INTENT_PARAM_TICKET_FAULT);
        date=getIntent().getStringExtra(Constant.INTENT_PARAM_TICKET_DATE);
        description=getIntent().getStringExtra(Constant.INTENT_PARAM_TICKET_DESCRIPTION);
        ticketStatus=getIntent().getStringExtra(Constant.INTENT_PARAM_TICKET_STATUS);
        mTextviewTicketNoValue.setText(ticketNo);
        mTextviewDateValue.setText(date);
        mTextViewFaultValue.setText(fault);
        mTextviewTicketStatusValue.setText(ticketStatus);
        mTextviewDescriptionValue.setText(description);
        if(ticketStatus.equals(Constant.TICKET_STATUS_CLOSE)){
            mImageView.setVisibility(View.GONE);
        }
        mApiService= ApiUtils.getAPIService();
        pref=getApplicationContext().getSharedPreferences("IffcoPref",0);
        userId = pref.getString(Constant.PREF_KEY_USER_ID,"0");
        Log.e("User id", userId);
        getTicketHistory();
        setListener();
    }

    private void setListener(){
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewTicketActivity.this, UpdateTicketActivity.class);
                intent.putExtra(Constant.INTENT_PARAM_TICKET_ID,ticketId);
                intent.putExtra(Constant.INTENT_PARAM_TICKET_NO,ticketNo);
                intent.putExtra(Constant.INTENT_PARAM_TICKET_DEPENDENCY_CODE,mTextViewDependencyCodeValue.getText().toString());
                intent.putExtra(Constant.INTENT_PARAM_TICKET_BROAD_CATEGORY,mTextviewBroadCategoryValue.getText().toString());
                intent.putExtra(Constant.INTENT_PARAM_TICKET_RESOLUTION_CODE,mTextviewResolutionCodeValue.getText().toString());
                finish();
                startActivity(intent);
            }
        });
    }

    private void init(){
        mImageView=findViewById(R.id.imageUpdateTicket);
        mTextviewTicketNoValue=findViewById(R.id.textviewTicketNoValue);
        mTextviewDateValue=findViewById(R.id.textviewDateValue);
        mTextViewFaultValue=findViewById(R.id.textViewFaultValue);
        mTextviewStatusDateValue=findViewById(R.id.textviewStatusDateValue);
        mTextviewDescriptionValue=findViewById(R.id.textviewDescriptionValue);
        mTextviewTicketStatusValue=findViewById(R.id.textviewTicketStatusValue);
        mTextViewDependencyCodeValue=findViewById(R.id.textViewDependencyCodeValue);
        mTextviewResolutionCodeValue=findViewById(R.id.textviewResolutionCodeValue);
        mTextviewBroadCategoryValue=findViewById(R.id.textviewBroadCategoryValue);
        mLinearLayoutDependencyCode=findViewById(R.id.linearLayoutDependencyCode);
        mLinearLayoutResolutionCode=findViewById(R.id.linearLayoutResolutionCode);
        mLinearLayoutBroadCategory=findViewById(R.id.linearLayoutBroadCategory);
    }

    private void getTicketHistory(){

        Map<String,String> ticketHistoryFields=new HashMap<>();
        ticketHistoryFields.put("UserId",userId);
        ticketHistoryFields.put("TicketId",ticketId);

        mApiService.getTicketHistory(ticketHistoryFields).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                List<Ticket> ticketList = new ArrayList<>();
                if(response.isSuccessful() && response.code()==200) {
                    TicketResponse ticketResponse = response.body();
                    if(ticketResponse!=null && ticketResponse.getTblTicket().size()> 0){
                        Ticket ticket=ticketResponse.getTblTicket().get(0);
                        setControlValue(ticket);
                    }else{
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
                Utils.getInstance().showDialog(ViewTicketActivity.this,"Unable to login due to server error");
            }
        });
    }

    private void setControlValue(Ticket ticket){
        mTextviewStatusDateValue.setText(ticket.getDtStatusDate());
        mTextviewDescriptionValue.setText(ticket.getStrDescription());
        if(ticket.getStrDependency().equals("")){
            mLinearLayoutBroadCategory.setVisibility(View.VISIBLE);
            mLinearLayoutResolutionCode.setVisibility(View.VISIBLE);
            mTextviewResolutionCodeValue.setText(ticket.getStrResolutionCode());
            mTextviewBroadCategoryValue.setText(ticket.getStrBroadCategory());
        }else{
            mLinearLayoutDependencyCode.setVisibility(View.VISIBLE);
            mTextViewDependencyCodeValue.setText(ticket.getStrDependency());
        }

    }

}
