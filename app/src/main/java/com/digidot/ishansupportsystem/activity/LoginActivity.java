package com.digidot.ishansupportsystem.activity;

import android.support.v7.app.AppCompatActivity;

import com.digidot.ishansupportsystem.activity.HomeActivity;
import com.digidot.ishansupportsystem.retrofit.APIService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.model.EndPoint;
import com.digidot.ishansupportsystem.model.LoginRequest;
import com.digidot.ishansupportsystem.model.LoginResponse;
import com.digidot.ishansupportsystem.retrofit.ApiUtils;
import com.digidot.ishansupportsystem.retrofit.RetrofitClient;
import com.digidot.ishansupportsystem.util.Constant;
import com.digidot.ishansupportsystem.util.Utils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private final String TAG="LoginActivity";
    APIService mApiService;
    private  Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService= ApiUtils.getAPIService();
        getProjectEndPoint();
        setContentView(R.layout.activity_login);
        pref=getApplicationContext().getSharedPreferences("IffcoPref",0);
        editor=pref.edit();
        init();
    }

    private void getProjectEndPoint(){
        mApiService= ApiUtils.getAPIService();
        mApiService.getEndPoint(Constant.PROJECT_NAME).enqueue(new Callback<EndPoint>() {
            @Override
            public void onResponse(Call<EndPoint> call, Response<EndPoint> response) {
                if(response.isSuccessful()) {
                    EndPoint endPoint=response.body();
                    if(endPoint!=null && endPoint.getTblWebApi()!=null){
                        Constant.BASE_URL=endPoint.getTblWebApi().get(0).getAPILink()+"/";
                        RetrofitClient.setRetrofit(null);
                        mApiService= ApiUtils.getAPIService();
                        pref.edit().putString(Constant.PREF_KEY_WEBSITE_LINK,endPoint.getTblWebApi().get(0).getWebsiteLink()).commit();
                        pref.edit().putString(Constant.PREF_KEY_API_LINK,Constant.BASE_URL).commit();
                        //getNotiFicationCategoryData();
                        Log.i(TAG, "Base URL = " + Constant.BASE_URL);
                    }else{
                        Log.i(TAG, "null body");
                    }
                }else{
                    Log.i(TAG, "Got error");
                }
            }

            @Override
            public void onFailure(Call<EndPoint> call, Throwable t) {
                Log.e(TAG, "Unable to get end point");
            }
        });
    }

    private void init(){
        btnLogin= findViewById(R.id.btnLogin);
        etUsername = findViewById(R.id.edittextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        setListener();
    }

    private void setListener(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateLoginInput()){
                    LoginRequest loginRequest=new LoginRequest();
                    loginRequest.setUserName(etUsername.getText().toString());
                    loginRequest.setPassword(etPassword.getText().toString());
                    login(loginRequest);
                }
            }
        });
    }

    private void login(final LoginRequest loginRequest){
        Map<String,String> loginFields=new HashMap<>();
        loginFields.put("UserName",loginRequest.getUserName());
        loginFields.put("Password",loginRequest.getPassword());

        mApiService.login(loginFields).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful() && response.code()==200) {
                    LoginResponse loginResponse=response.body();
                    if(loginResponse!=null && loginResponse.getTblLogin().get(0)!=null){
                        if(loginResponse.getTblLogin().get(0).getError()!=null
                                && !loginResponse.getTblLogin().get(0).getError().isEmpty()){
                            Utils.getInstance().showDialog(LoginActivity.this,loginResponse.getTblLogin().get(0).getError());
                        }else {
                            LoginRequest result=loginResponse.getTblLogin().get(0);
                            editor.putString(Constant.PREF_KEY_USER_ID,result.getIntUserId());
                            editor.putString(Constant.PREF_KEY_USERNAME,result.getUserName());
                            editor.putString(Constant.PREF_KEY_IS_CREATE_TICKET_RIGHT,result.getIsCreate());
                            editor.putString(Constant.PREF_KEY_IS_UPDATE_TICKET_RIGHT,result.getIsUpdateStatus());
                            editor.commit();
                            finish();
                            editor.putBoolean(Constant.PREF_KEY_LOGIN,true).commit();
                            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Error from server",Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Utils.getInstance().showDialog(LoginActivity.this,"Unable to login due to server error");
            }
        });

    }

    private boolean validateLoginInput(){
        String result="";
        if(etUsername.getText()==null || etUsername.getText().toString().isEmpty()){
            result+=this.getString(R.string.error_username_required);
        }else if(etPassword.getText()==null || etPassword.getText().toString().isEmpty()){
            result+=this.getString(R.string.error_password_required);
        }

        if(result.isEmpty()){
            return true;
        }else {
            Utils.getInstance().showDialog(this,result);
            return false;
        }
    }
}

