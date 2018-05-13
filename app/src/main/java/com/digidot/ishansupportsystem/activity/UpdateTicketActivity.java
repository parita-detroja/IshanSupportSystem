package com.digidot.ishansupportsystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.model.Dependency;
import com.digidot.ishansupportsystem.model.DependencyResponse;
import com.digidot.ishansupportsystem.model.Resolution;
import com.digidot.ishansupportsystem.model.ResolutionResponse;
import com.digidot.ishansupportsystem.model.UpdateTicket;
import com.digidot.ishansupportsystem.model.UpdateTicketResponce;
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

public class UpdateTicketActivity extends AppCompatActivity {

    private Spinner mSpinnerDependency;
    private Spinner mSpinnerResolution;
    private EditText mEditTextRemarks;
    private Button mButtonUpdateTicket;

    private APIService mApiService;

    private String userId = "0";
    private String ticketId = "0";

    private DependencyResponse dependencyResponse;
    private ResolutionResponse resolutionResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ticket);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("IffcoPref", 0);
        userId = pref.getString(Constant.PREF_KEY_USER_ID,"0");
        Log.e("User id", userId);
        mApiService= ApiUtils.getAPIService();
        getDependency();
        getResolution("");
        initView();
    }

    private void initView(){
        mSpinnerDependency = findViewById(R.id.spinnerDependency);
        mSpinnerResolution = findViewById(R.id.spinnerResolution);
        mEditTextRemarks = findViewById(R.id.etRemarks);
        mButtonUpdateTicket = findViewById(R.id.btnUpdateTicket);

        mButtonUpdateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTicket();
            }
        });
    }

    private void updateTicket(){
        Map<String,String> updateFields=new HashMap<>();
        updateFields.put("UserId",userId);
        updateFields.put("TicketId",ticketId);
        updateFields.put("DependencyId",dependencyResponse.getTblDependency().
                get((int) mSpinnerDependency.getSelectedItemId()).getIntDependencyId());
        updateFields.put("ResolutionId",resolutionResponse.getTblResolution().
                get((int) mSpinnerResolution.getSelectedItemId()).getIntResolutionId());
        updateFields.put("Remarks",mEditTextRemarks.getText().toString());


        mApiService.getUpdateTicket(updateFields).enqueue(new Callback<UpdateTicketResponce>() {
            @Override
            public void onResponse(Call<UpdateTicketResponce> call, Response<UpdateTicketResponce> response) {
                if(response.isSuccessful() && response.code()==200) {
                    UpdateTicketResponce updateTicketResponce = response.body();
                    List<String> updateTicketList = new ArrayList<>();
                    if(updateTicketResponce !=null && updateTicketResponce.getTblUpdateTicket().size()> 0){
                        for(UpdateTicket updateTicket: updateTicketResponce.getTblUpdateTicket()){
                            updateTicketList.add(updateTicket.getIntStatusId());
                        }
                        Intent mIntent = new Intent(UpdateTicketActivity.this,MainActivity.class);
                        startActivity(mIntent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Success false",Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<UpdateTicketResponce> call, Throwable t) {
                Utils.getInstance().showDialog(UpdateTicketActivity.this,"Unable to login due to server error");
            }
        });
    }

    private void getDependency(){
        Map<String,String> dependencyFields=new HashMap<>();
        dependencyFields.put("UserId",userId);

        mApiService.getDependency(dependencyFields).enqueue(new Callback<DependencyResponse>() {
            @Override
            public void onResponse(Call<DependencyResponse> call, Response<DependencyResponse> response) {
                if(response.isSuccessful() && response.code()==200) {
                    dependencyResponse = response.body();
                    List<String> dependencyList = new ArrayList<>();
                    if(dependencyResponse !=null && dependencyResponse.getTblDependency().size()> 0){
                        for(Dependency dependency: dependencyResponse.getTblDependency()){
                            dependencyList.add(dependency.getStrDependency());
                        }
                        ArrayAdapter<String> dependencyAdapter = new ArrayAdapter<>(UpdateTicketActivity.this, R.layout.custom_spinner_item, dependencyList);
                        dependencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerDependency.setAdapter(dependencyAdapter);
                    }else{
                        Toast.makeText(getApplicationContext(),"Success false",Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<DependencyResponse> call, Throwable t) {
                Utils.getInstance().showDialog(UpdateTicketActivity.this,"Unable to login due to server error");
            }
        });
    }

    private void getResolution(String broadCategoryId){
        Map<String,String> resolutionFields=new HashMap<>();
        resolutionFields.put("UserId",userId);
        resolutionFields.put("BroadCategoryId ",broadCategoryId);

        mApiService.getResolution(resolutionFields).enqueue(new Callback<ResolutionResponse>() {
            @Override
            public void onResponse(Call<ResolutionResponse> call, Response<ResolutionResponse> response) {
                if(response.isSuccessful() && response.code()==200) {
                    resolutionResponse = response.body();
                    List<String> resolutionList = new ArrayList<>();
                    if(resolutionResponse !=null && resolutionResponse.getTblResolution().size()> 0){
                        for(Resolution resolution: resolutionResponse.getTblResolution()){
                            resolutionList.add(resolution.getStrResolutionCode());
                        }
                        ArrayAdapter<String> resolutionAdapter = new ArrayAdapter<>(UpdateTicketActivity.this, R.layout.custom_spinner_item, resolutionList);
                        resolutionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerResolution.setAdapter(resolutionAdapter);
                    }else{
                        Toast.makeText(getApplicationContext(),"Success false",Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<ResolutionResponse> call, Throwable t) {
                Utils.getInstance().showDialog(UpdateTicketActivity.this,"Unable to login due to server error");
            }
        });
    }
}
