package com.digidot.ishansupportsystem.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.activity.HomeActivity;
import com.digidot.ishansupportsystem.model.Broad;
import com.digidot.ishansupportsystem.model.BroadResponse;
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

public class UpdateTicketFragment extends Fragment {

    private Context mContext;
    private Spinner mSpinnerDependency;
    private Spinner mSpinnerResolution;
    private EditText mEditTextRemarks;
    private Spinner mSpinnerBroadCategory;
    private RadioGroup mRadioGroupDependency;
    private LinearLayout mLinearLayoutDependency;
    private LinearLayout mLinearLayoutResolution;
    private TextView mTextViewTicketNumber;

    private RadioButton mRadioButtonYes;
    private RadioButton mRadioButtonNo;

    private APIService mApiService;

    private String userId = "";
    private String ticketId = "";
    private String ticketNumber = "";
    private String dependencyId = "";
    private String dependencyCode = "";
    private String resolutionCode = "";
    private String broadCategoryId = "";
    private String broadCategoryCode = "";

    private DependencyResponse dependencyResponse;
    private ResolutionResponse resolutionResponse;
    private BroadResponse broadResponse;

    public UpdateTicketFragment() {
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
        View view = inflater.inflate(R.layout.fragment_update_ticket, container, false);
        SharedPreferences pref = mContext.getSharedPreferences("IffcoPref", 0);
        userId = pref.getString(Constant.PREF_KEY_USER_ID, "0");
        Log.e("User id", userId);
        mApiService = ApiUtils.getAPIService();
        initView(view);
        fatchData();

        getDependency();
        getBroadCategory();
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

    private void fatchData() {
        Bundle bundle = this.getArguments();
        ticketId = bundle.getString(Constant.INTENT_PARAM_TICKET_ID);
        ticketNumber = bundle.getString(Constant.INTENT_PARAM_TICKET_NO);
        if (bundle.getString(Constant.INTENT_PARAM_TICKET_DEPENDENCY_CODE) != null) {
            dependencyCode = bundle.getString(Constant.INTENT_PARAM_TICKET_DEPENDENCY_CODE);
        } else {
            dependencyCode = "";
        }
        resolutionCode = bundle.getString(Constant.INTENT_PARAM_TICKET_RESOLUTION_CODE);
        broadCategoryCode = bundle.getString(Constant.INTENT_PARAM_TICKET_BROAD_CATEGORY);
        mTextViewTicketNumber.setText(ticketNumber);
        if (dependencyCode.isEmpty()) {
            mRadioButtonNo.setChecked(true);
            mLinearLayoutResolution.setVisibility(View.VISIBLE);
            mLinearLayoutDependency.setVisibility(View.GONE);
        } else {
            mRadioButtonYes.setChecked(true);
            mLinearLayoutDependency.setVisibility(View.VISIBLE);
            mLinearLayoutResolution.setVisibility(View.GONE);
        }
    }

    private void initView(View view) {
        mTextViewTicketNumber = view.findViewById(R.id.textTicketNumber);
        mSpinnerDependency = view.findViewById(R.id.spinnerDependency);
        mSpinnerResolution = view.findViewById(R.id.spinnerResolution);
        mSpinnerBroadCategory = view.findViewById(R.id.spinnerBroadCategory);
        mLinearLayoutDependency = view.findViewById(R.id.linearLayoutDependencyCode);
        mLinearLayoutResolution = view.findViewById(R.id.linearLayoutResolution);
        mEditTextRemarks = view.findViewById(R.id.etRemarks);
        mRadioGroupDependency = view.findViewById(R.id.radioDependency);
        mRadioButtonYes = view.findViewById(R.id.radioButtonYes);
        mRadioButtonNo = view.findViewById(R.id.radioButtonNo);
        Button mButtonUpdateTicket = view.findViewById(R.id.btnUpdateTicket);
        mRadioGroupDependency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radioButtonYes) {
                    mLinearLayoutDependency.setVisibility(View.VISIBLE);
                    mLinearLayoutResolution.setVisibility(View.GONE);
                } else {
                    mLinearLayoutResolution.setVisibility(View.VISIBLE);
                    mLinearLayoutDependency.setVisibility(View.GONE);
                }
            }
        });
        mButtonUpdateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTicket();
            }
        });
        mSpinnerBroadCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                broadCategoryId = broadResponse.getTblBroadCategory().get(i).getIntBroadCategoryId();
                getResolution();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateTicket() {
        Location mLocation = ((HomeActivity)mContext).getLocation();
        Map<String, String> updateFields = new HashMap<>();
        updateFields.put("UserId", userId);
        updateFields.put("TicketId", ticketId);
        updateFields.put("Image", "");
        if (mRadioGroupDependency.getCheckedRadioButtonId() == R.id.radioButtonYes) {
            updateFields.put("DependencyId", dependencyResponse.getTblDependency().
                    get((int) mSpinnerDependency.getSelectedItemId()).getIntDependencyId());
            updateFields.put("ResolutionId", "");
        } else {
            updateFields.put("DependencyId", "");
            updateFields.put("ResolutionId", resolutionResponse.getTblResolution().
                    get((int) mSpinnerResolution.getSelectedItemId()).getIntResolutionId());
        }
        if(mLocation != null){
            updateFields.put("Latitude",mLocation.getLatitude()+"");
            updateFields.put("Longitude",mLocation.getLongitude()+"");
        }else {
            updateFields.put("Latitude","0.00");
            updateFields.put("Longitude","0.00");
        }
        updateFields.put("Remarks", mEditTextRemarks.getText().toString());


        mApiService.getUpdateTicket(updateFields).enqueue(new Callback<UpdateTicketResponce>() {
            @Override
            public void onResponse(Call<UpdateTicketResponce> call, Response<UpdateTicketResponce> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    UpdateTicketResponce updateTicketResponce = response.body();
                    List<String> updateTicketList = new ArrayList<>();
                    if (updateTicketResponce != null && updateTicketResponce.getTblTicket().size() > 0) {
                        for (UpdateTicket updateTicket : updateTicketResponce.getTblTicket()) {
                            updateTicketList.add(updateTicket.getIntStatusId());
                        }
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        TicketListFragment mTicketListFragment = new TicketListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.TICKET_STATUS, Constant.TICKET_STATUS_DEPENDENCY);
                        mTicketListFragment.setArguments(bundle);
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame, mTicketListFragment)
                                .commit();
                    } else {
                        Toast.makeText(mContext, "Success false", Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<UpdateTicketResponce> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext, "Unable to login due to server error");
            }
        });
    }

    private void getDependency() {
        Map<String, String> dependencyFields = new HashMap<>();
        dependencyFields.put("UserId", userId);

        mApiService.getDependency(dependencyFields).enqueue(new Callback<DependencyResponse>() {
            @Override
            public void onResponse(Call<DependencyResponse> call, Response<DependencyResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    int position = 0;
                    dependencyResponse = response.body();
                    List<String> dependencyList = new ArrayList<>();
                    if (dependencyResponse != null && dependencyResponse.getTblDependency().size() > 0) {
                        int index = 0;
                        for (Dependency dependency : dependencyResponse.getTblDependency()) {
                            dependencyList.add(dependency.getStrDependency());
                            if (dependency.getStrDependency().equals(dependencyCode)) {
                                position = index;
                            }
                            index++;
                        }
                        ArrayAdapter<String> dependencyAdapter = new ArrayAdapter<>(mContext, R.layout.custom_spinner_item, dependencyList);
                        dependencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerDependency.setAdapter(dependencyAdapter);
                        mSpinnerDependency.setSelection(position);
                    } else {
                        Toast.makeText(mContext, "Success false", Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<DependencyResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext, "Unable to login due to server error");
            }
        });
    }

    private void getResolution() {
        Map<String, String> resolutionFields = new HashMap<>();
        resolutionFields.put("UserId", userId);
        resolutionFields.put("BroadCategoryId", broadCategoryId);

        mApiService.getResolution(resolutionFields).enqueue(new Callback<ResolutionResponse>() {
            @Override
            public void onResponse(Call<ResolutionResponse> call, Response<ResolutionResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    resolutionResponse = response.body();
                    int position = 0;
                    List<String> resolutionList = new ArrayList<>();
                    if (resolutionResponse != null && resolutionResponse.getTblResolution().size() > 0) {
                        int index = 0;
                        for (Resolution resolution : resolutionResponse.getTblResolution()) {
                            resolutionList.add(resolution.getStrResolutionCode());
                            if (resolution.getStrResolutionCode().equals(resolutionCode)) {
                                position = index;
                            }
                            index++;
                        }
                        ArrayAdapter<String> resolutionAdapter = new ArrayAdapter<>(mContext, R.layout.custom_spinner_item, resolutionList);
                        resolutionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerResolution.setAdapter(resolutionAdapter);
                        mSpinnerResolution.setSelection(position);
                    } else {
                        Toast.makeText(mContext, "Success false", Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<ResolutionResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext, "Unable to resolution due to server error");
            }
        });
    }

    private void getBroadCategory() {
        Map<String, String> broadCategoryFields = new HashMap<>();
        broadCategoryFields.put("UserId", userId);

        mApiService.getBroad(broadCategoryFields).enqueue(new Callback<BroadResponse>() {
            @Override
            public void onResponse(Call<BroadResponse> call, Response<BroadResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    broadResponse = response.body();
                    int position = 0;
                    List<String> broadList = new ArrayList<>();
                    if (broadResponse != null && broadResponse.getTblBroadCategory().size() > 0) {
                        int index = 0;
                        for (Broad broad : broadResponse.getTblBroadCategory()) {
                            broadList.add(broad.getStrBroadCategory());
                            if (broad.getStrBroadCategory().equals(broadCategoryCode)) {
                                position = index;
                                broadCategoryId = broad.getIntBroadCategoryId();
                                getResolution();
                            }
                            index++;
                        }
                        ArrayAdapter<String> broadAdapter = new ArrayAdapter<>(mContext, R.layout.custom_spinner_item, broadList);
                        broadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerBroadCategory.setAdapter(broadAdapter);
                        mSpinnerBroadCategory.setSelection(position);
                    } else {
                        Toast.makeText(mContext, "Success false", Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<BroadResponse> call, Throwable t) {
                Utils.getInstance().showDialog((Activity) mContext, "Unable to login due to server error");
            }
        });
    }
}
