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

public class CreateTicketFragment extends Fragment {

    private Context mContext;
    private Spinner mSpinnerState;
    private Spinner mSpinnerCity;
    private Spinner mSpinnerZone;
    private Spinner mSpinnerOffice;
    private Spinner mSpinnerFault;
    private Spinner mSpinnerClient;
    private EditText mEditTextDescription;

    private APIService mApiService;

    private StateResponse stateResponse;
    private CityResponse cityResponse;
    private ZoneResponse zoneResponse;
    private ClientResponse clientResponse;
    private OfficeResponse officeResponse;
    private FaultResponse faultResponse;

    private int zonePosition;
    private int faultPosition;
    private int officePosition;

    private List<String> stateList = new ArrayList<>();
    private List<String> cityList = new ArrayList<>();
    private List<String> zoneList = new ArrayList<>();
    private List<String> clientList = new ArrayList<>();
    private List<String> officeList = new ArrayList<>();
    private String userId = "0";

    public CreateTicketFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_ticket, container, false);
        mApiService= ApiUtils.getAPIService();
        SharedPreferences pref = mContext.getSharedPreferences("IffcoPref", 0);
        userId = pref.getString(Constant.PREF_KEY_USER_ID,"0");
        Log.e("User id", userId);

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

        mEditTextDescription = view.findViewById(R.id.etDescription);

        Button mButtonCreateTicket = view.findViewById(R.id.btnCreateTicket);

        mButtonCreateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTicket(faultResponse.getTblFault().get(faultPosition).getIntFaultId(),
                        officeResponse.getTblOffice().get(officePosition).getIntOfficeId());
            }
        });

        mSpinnerState = view.findViewById(R.id.spinnerState);
        mSpinnerCity = view.findViewById(R.id.spinnerCity);
        mSpinnerZone = view.findViewById(R.id.spinnerZone);
        mSpinnerOffice = view.findViewById(R.id.spinnerOffice);
        mSpinnerFault = view.findViewById(R.id.spinnerFault);
        mSpinnerClient = view.findViewById(R.id.spinnerClient);
        mSpinnerCity.setEnabled(false);
        mSpinnerZone.setEnabled(false);
        mSpinnerOffice.setEnabled(false);
        mSpinnerClient.setEnabled(false);
        stateList.add("Select State");
        cityList.add("Select City");
        zoneList.add("Select Zone");
        clientList.add("Select Client");
        officeList.add("Select Office");
        setCityAdapter();
        setZoneAdapter();
        setClientAdapter();
        setOfficeAdapter();
        getState();
        getFault();
    }

    private void setCityAdapter(){
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(mContext, R.layout.custom_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCity.setAdapter(cityAdapter);
    }

    private void setZoneAdapter(){
        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<>(mContext, R.layout.custom_spinner_item, zoneList);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerZone.setAdapter(zoneAdapter);
    }

    private void setClientAdapter(){
        ArrayAdapter<String> clientAdapter = new ArrayAdapter<>(mContext, R.layout.custom_spinner_item, clientList);
        clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerClient.setAdapter(clientAdapter);
    }
    private void setOfficeAdapter(){
        ArrayAdapter<String> officeAdapter = new ArrayAdapter<>(mContext, R.layout.custom_spinner_item, officeList);
        officeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOffice.setAdapter(officeAdapter);
    }

    private void getState(){

        Map<String,String> stateFields=new HashMap<>();
        stateFields.put("UserId",userId);

        mApiService.getState(stateFields).enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                if(response.isSuccessful() && response.code()==200) {
                    stateResponse = response.body();
                    if(stateResponse!=null && stateResponse.getTblState().size()> 0){
                        stateList.clear();
                        stateList.add("Select State");
                        for(State state: stateResponse.getTblState()){
                            stateList.add(state.getStrStateName());
                        }
                        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(mContext, R.layout.custom_spinner_item, stateList);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerState.setAdapter(stateAdapter);
                        mSpinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(position != 0){
                                    mSpinnerCity.setEnabled(true);
                                    mSpinnerZone.setEnabled(false);
                                    mSpinnerOffice.setEnabled(false);
                                    mSpinnerClient.setEnabled(false);
                                    zoneList.clear();
                                    clientList.clear();
                                    officeList.clear();
                                    zoneList.add("Select Zone");
                                    clientList.add("Select Client");
                                    officeList.add("Select Office");
                                    setZoneAdapter();
                                    setClientAdapter();
                                    setOfficeAdapter();
                                    getCity(stateResponse.getTblState().get(position-1).getIntStateId());
                                }
                                else{
                                    mSpinnerCity.setEnabled(false);
                                    mSpinnerZone.setEnabled(false);
                                    mSpinnerOffice.setEnabled(false);
                                    mSpinnerClient.setEnabled(false);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }else{
                        Toast.makeText(mContext,"Success false",Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<StateResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext,"Unable to login due to server error");
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
                if(response.isSuccessful() && response.code()==200) {
                    cityResponse = response.body();
                    if(cityResponse!=null && cityResponse.getTblCity().size()> 0){
                        cityList.clear();
                        cityList.add("Select City");
                        for(City city: cityResponse.getTblCity()){
                            cityList.add(city.getStrCityName());
                        }
                        setCityAdapter();
                        mSpinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(position != 0){
                                    mSpinnerZone.setEnabled(true);
                                    mSpinnerOffice.setEnabled(false);
                                    mSpinnerClient.setEnabled(false);
                                    clientList.clear();
                                    officeList.clear();
                                    clientList.add("Select Client");
                                    officeList.add("Select Office");
                                    setClientAdapter();
                                    setOfficeAdapter();
                                    getZone(cityResponse.getTblCity().get(position-1).getIntCityId());
                                }
                                else{
                                    mSpinnerZone.setEnabled(false);
                                    mSpinnerOffice.setEnabled(false);
                                    mSpinnerClient.setEnabled(false);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }else{
                        Toast.makeText(mContext,"Success false",Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext,"Unable to login due to server error");
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
                if(response.isSuccessful() && response.code()==200) {
                    zoneResponse = response.body();
                    if(zoneResponse!=null && zoneResponse.getTblZone().size()> 0){
                        zoneList.clear();
                        zoneList.add("Select Zone");
                        for(Zone zone: zoneResponse.getTblZone()){
                            zoneList.add(zone.getStrZoneName());
                        }
                        setZoneAdapter();
                        mSpinnerZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(position != 0){
                                    mSpinnerOffice.setEnabled(false);
                                    mSpinnerClient.setEnabled(true);
                                    officeList.clear();
                                    officeList.add("Select Office");
                                    setOfficeAdapter();
                                    getClient(zoneResponse.getTblZone().get(position-1).getIntZoneId());
                                    zonePosition = position-1;
                                }else{
                                    mSpinnerOffice.setEnabled(false);
                                    mSpinnerClient.setEnabled(false);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{
                        Toast.makeText(mContext,"Success false",Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<ZoneResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext,"Unable to login due to server error");
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
                if(response.isSuccessful() && response.code()==200) {
                    clientResponse = response.body();
                    if(clientResponse!=null && clientResponse.getTblClient().size()> 0){
                        clientList.clear();
                        clientList.add("Select Client");
                        for(Client client: clientResponse.getTblClient()){
                            clientList.add(client.getStrFullName());
                        }
                        setClientAdapter();
                        mSpinnerClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(position != 0){
                                    getOffice(zoneResponse.getTblZone().get(zonePosition).getIntZoneId(),clientResponse.getTblClient().get(position-1).getIntClientId());
                                    mSpinnerOffice.setEnabled(true);
                                }
                                else{
                                    mSpinnerOffice.setEnabled(false);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{
                        Toast.makeText(mContext,"Success false",Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<ClientResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext,"Unable to login due to server error");
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
                if(response.isSuccessful() && response.code()==200) {
                    officeResponse = response.body();
                    if(officeResponse!=null && officeResponse.getTblOffice().size()> 0){
                        officeList.clear();
                        officeList.add("Select Office");
                        for(Office office: officeResponse.getTblOffice()){
                            officeList.add(office.getStrOfficeName());
                        }
                        setOfficeAdapter();
                        mSpinnerOffice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                officePosition = position-1;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{
                        Toast.makeText(mContext,"Success false",Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<OfficeResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext,"Unable to login due to server error");
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
                faultList.add("Select Fault");
                if(response.isSuccessful() && response.code()==200) {
                    faultResponse = response.body();
                    if(faultResponse!=null && faultResponse.getTblFault().size()> 0){
                        for(Fault fault: faultResponse.getTblFault()){
                            faultList.add(fault.getStrFault());
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, R.layout.custom_spinner_item, faultList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerFault.setAdapter(dataAdapter);
                        mSpinnerFault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                faultPosition = position-1;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{
                        Toast.makeText(mContext,"Success false",Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<FaultResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext,"Unable to login due to server error");
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
                    Toast.makeText(mContext,"Ticket Created",Toast.LENGTH_LONG).show();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    HomeFragment mHomeFragment = new HomeFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame, mHomeFragment).commit();
                }else if(response.code()==401){
                    Toast.makeText(mContext,"Unauthorized",Toast.LENGTH_LONG).show();
                }else if(response.code()==500){
                    Toast.makeText(mContext,"Internal server error",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext,"Internal server error",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext,"Unable to login due to server error");
            }
        });
    }
}
