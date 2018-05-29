package com.digidot.ishansupportsystem.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.activity.HomeActivity;
import com.digidot.ishansupportsystem.util.Constant;

public class SettingFragment extends Fragment {

    private SharedPreferences pref;
    private Switch notificationSwitch;
    public SettingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        Constant.CURRENT_LOADED_FRAGMENT=Constant.FRAGMNET_SETTINGS;
        ((HomeActivity) getActivity()).setToolbarTitle(Constant.FRAGMNET_SETTINGS.toString());
        pref=getActivity().getSharedPreferences("IffcoPref",0);
        initView(view);
        return view;
    }

    public void initView(View view){
        notificationSwitch=view.findViewById(R.id.switch_notification_option);
        notificationSwitch.setChecked(pref.getBoolean(Constant.PREF_KEY_NOTIFICATION_ACTIVATION_KEY,true));
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pref.edit().putBoolean(Constant.PREF_KEY_NOTIFICATION_ACTIVATION_KEY,true).commit();
                }else{
                    pref.edit().putBoolean(Constant.PREF_KEY_NOTIFICATION_ACTIVATION_KEY,false).commit();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
