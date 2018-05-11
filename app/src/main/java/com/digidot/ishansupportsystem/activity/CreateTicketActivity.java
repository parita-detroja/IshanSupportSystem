package com.digidot.ishansupportsystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.model.City;
import com.digidot.ishansupportsystem.model.CityResponse;
import com.digidot.ishansupportsystem.model.Client;
import com.digidot.ishansupportsystem.model.ClientResponse;
import com.digidot.ishansupportsystem.model.Fault;
import com.digidot.ishansupportsystem.model.FaultResponse;
import com.digidot.ishansupportsystem.model.LoginRequest;
import com.digidot.ishansupportsystem.model.LoginResponse;
import com.digidot.ishansupportsystem.model.Office;
import com.digidot.ishansupportsystem.model.OfficeResponse;
import com.digidot.ishansupportsystem.model.State;
import com.digidot.ishansupportsystem.model.StateResponse;
import com.digidot.ishansupportsystem.model.Zone;
import com.digidot.ishansupportsystem.model.ZoneResponse;
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

public class CreateTicketActivity extends AppCompatActivity {

    private Spinner mSpinnerState;
    private Spinner mSpinnerCity;
    private Spinner mSpinnerZone;
    private Spinner mSpinnerOffice;
    private Spinner mSpinnerFault;
    private Spinner mSpinnerClient;
    private EditText mEditTextDescription;
    private Button mButtonCreateTicket;

    private APIService mApiService;
    private SharedPreferences pref;

    private StateResponse stateResponse;
    private CityResponse cityResponse;
    private ZoneResponse zoneResponse;
    private ClientResponse clientResponse;
    private OfficeResponse officeResponse;
    private FaultResponse faultResponse;

    private int zonePosition;
    private int faultPosition;
    private int officePosition;


    private String userId = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);
        mApiService= ApiUtils.getAPIService();
        pref=getApplicationContext().getSharedPreferences("IffcoPref",0);
        userId = pref.getString(Constant.PREF_KEY_USER_ID,"0");
        Log.e("User id", userId);

        initView();
    }

    private void initView(){

        mEditTextDescription = findViewById(R.id.etDescription);

        mButtonCreateTicket = findViewById(R.id.btnCreateTicket);

        mButtonCreateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTicket(faultResponse.getTblFault().get(faultPosition).getIntFaultId(),
                        officeResponse.getTblOffice().get(officePosition).getIntOfficeId());
            }
        });

        mSpinnerState = findViewById(R.id.spinnerState);
        mSpinnerCity = findViewById(R.id.spinnerCity);
        mSpinnerZone = findViewById(R.id.spinnerZone);
        mSpinnerOffice = findViewById(R.id.spinnerOffice);
        mSpinnerFault = findViewById(R.id.spinnerFault);
        mSpinnerClient = findViewById(R.id.spinnerClient);

        mSpinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCity(stateResponse.getTblState().get(position).getIntStateId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getZone(cityResponse.getTblCity().get(position).getIntCityId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getClient(zoneResponse.getTblZone().get(position).getIntZoneId());
                zonePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getOffice(zoneResponse.getTblZone().get(zonePosition).getIntZoneId(),clientResponse.getTblClient().get(position).getIntClientId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerOffice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                officePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerFault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                faultPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getState();
        getFault();
    }

    private void getState(){

        Map<String,String> stateFields=new HashMap<>();
        stateFields.put("UserId",userId);

        mApiService.getState(stateFields).enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                List<String> stateList = new ArrayList<>();
                if(response.isSuccessful() && response.code()==200) {
                    stateResponse = response.body();
                    if(stateResponse!=null && stateResponse.getTblState().size()> 0){
                        for(State state: stateResponse.getTblState()){
                            stateList.add(state.getStrStateName());
                        }

                        ArrayAdapter dataAdapter = new ArrayAdapter(CreateTicketActivity.this, R.layout.custom_spinner_item, stateList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerState.setAdapter(dataAdapter);

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
            public void onFailure(Call<StateResponse> call, Throwable t) {
                Utils.getInstance().showDialog(CreateTicketActivity.this,"Unable to login due to server error");
            }
        });
    }

    private void getCity(String stateId){

        Map<String,String> cityFields=new HashMap<>();
        cityFields.put("UserId",userId);
        cityFields.put("StateId", stateId);

        mApiService.getCity(cityFields).enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                List<String> cityList = new ArrayList<>();
                if(response.isSuccessful() && response.code()==200) {
                    cityResponse = response.body();
                    if(cityResponse!=null && cityResponse.getTblCity().size()> 0){
                        for(City city: cityResponse.getTblCity()){
                            cityList.add(city.getStrCityName());
                        }

                        ArrayAdapter dataAdapter = new ArrayAdapter(CreateTicketActivity.this, R.layout.custom_spinner_item, cityList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerCity.setAdapter(dataAdapter);

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
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Utils.getInstance().showDialog(CreateTicketActivity.this,"Unable to login due to server error");
            }
        });
    }


    private void getZone(String cityId){

        Map<String,String> zoneFields=new HashMap<>();
        zoneFields.put("UserId",userId);
        zoneFields.put("CityId", cityId);

        mApiService.getZone(zoneFields).enqueue(new Callback<ZoneResponse>() {
            @Override
            public void onResponse(Call<ZoneResponse> call, Response<ZoneResponse> response) {
                List<String> zoneList = new ArrayList<>();
                if(response.isSuccessful() && response.code()==200) {
                    zoneResponse = response.body();
                    if(zoneResponse!=null && zoneResponse.getTblZone().size()> 0){
                        for(Zone zone: zoneResponse.getTblZone()){
                            zoneList.add(zone.getStrZoneName());
                        }

                        ArrayAdapter dataAdapter = new ArrayAdapter(CreateTicketActivity.this, R.layout.custom_spinner_item, zoneList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerZone.setAdapter(dataAdapter);

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
            public void onFailure(Call<ZoneResponse> call, Throwable t) {
                Utils.getInstance().showDialog(CreateTicketActivity.this,"Unable to login due to server error");
            }
        });
    }

    private void getClient(String zoneId){

        Map<String,String> clientFields=new HashMap<>();
        clientFields.put("UserId",userId);
        clientFields.put("ZoneId", zoneId);

        mApiService.getClient(clientFields).enqueue(new Callback<ClientResponse>() {
            @Override
            public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                List<String> clientList = new ArrayList<>();
                if(response.isSuccessful() && response.code()==200) {
                    clientResponse = response.body();
                    if(clientResponse!=null && clientResponse.getTblClient().size()> 0){
                        for(Client client: clientResponse.getTblClient()){
                            clientList.add(client.getStrFullName());
                        }

                        ArrayAdapter dataAdapter = new ArrayAdapter(CreateTicketActivity.this, R.layout.custom_spinner_item, clientList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerClient.setAdapter(dataAdapter);

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
            public void onFailure(Call<ClientResponse> call, Throwable t) {
                Utils.getInstance().showDialog(CreateTicketActivity.this,"Unable to login due to server error");
            }
        });
    }

    private void getOffice(String zoneId,String clientId){

        Map<String,String> officeFields=new HashMap<>();
        officeFields.put("UserId",userId);
        officeFields.put("ZoneId", zoneId);
        officeFields.put("ClientId", clientId);

        mApiService.getOffice(officeFields).enqueue(new Callback<OfficeResponse>() {
            @Override
            public void onResponse(Call<OfficeResponse> call, Response<OfficeResponse> response) {
                List<String> officeList = new ArrayList<>();
                if(response.isSuccessful() && response.code()==200) {
                    officeResponse = response.body();
                    if(officeResponse!=null && officeResponse.getTblOffice().size()> 0){
                        for(Office office: officeResponse.getTblOffice()){
                            officeList.add(office.getStrOfficeName());
                        }

                        ArrayAdapter dataAdapter = new ArrayAdapter(CreateTicketActivity.this, R.layout.custom_spinner_item, officeList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerOffice.setAdapter(dataAdapter);

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
            public void onFailure(Call<OfficeResponse> call, Throwable t) {
                Utils.getInstance().showDialog(CreateTicketActivity.this,"Unable to login due to server error");
            }
        });
    }

    private void getFault(){

        Map<String,String> faultFields=new HashMap<>();
        faultFields.put("UserId",userId);

        mApiService.getFault(faultFields).enqueue(new Callback<FaultResponse>() {
            @Override
            public void onResponse(Call<FaultResponse> call, Response<FaultResponse> response) {
                List<String> faultList = new ArrayList<>();
                if(response.isSuccessful() && response.code()==200) {
                    faultResponse = response.body();
                    if(faultResponse!=null && faultResponse.getTblFault().size()> 0){
                        for(Fault fault: faultResponse.getTblFault()){
                            faultList.add(fault.getStrFault());
                        }

                        ArrayAdapter dataAdapter = new ArrayAdapter(CreateTicketActivity.this, R.layout.custom_spinner_item, faultList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerFault.setAdapter(dataAdapter);

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
            public void onFailure(Call<FaultResponse> call, Throwable t) {
                Utils.getInstance().showDialog(CreateTicketActivity.this,"Unable to login due to server error");
            }
        });
    }

    private void createTicket(String faultId,String officeId){

        Map<String,String> createTicketFields=new HashMap<>();
        createTicketFields.put("UserId",userId);
        createTicketFields.put("FaultId",faultId);
        createTicketFields.put("OfficeId",officeId);
        createTicketFields.put("Description",mEditTextDescription.getText().toString());

        mApiService.createTicket(createTicketFields).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful() && response.code()==200) {
                    Toast.makeText(getApplicationContext(),"Ticket Created",Toast.LENGTH_LONG).show();
                    Intent mIntent = new Intent(CreateTicketActivity.this,MainActivity.class);
                    startActivity(mIntent);
                }else if(response.code()==401){
                    Toast.makeText(getApplicationContext(),"Unauthorized",Toast.LENGTH_LONG).show();
                }else if(response.code()==500){
                    Toast.makeText(getApplicationContext(),"Internal server error",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Internal server error",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Utils.getInstance().showDialog(CreateTicketActivity.this,"Unable to login due to server error");
            }
        });
    }
}
