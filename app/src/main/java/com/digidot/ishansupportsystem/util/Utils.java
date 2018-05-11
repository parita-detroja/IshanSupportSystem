package com.digidot.ishansupportsystem.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.digidot.ishansupportsystem.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ABC on 26-01-2018.
 */

public class Utils {
    private static Utils utils;
    private static String TAG="Utils";
    ProgressDialog progressDialog;

    public static Utils getInstance(){
        if(utils==null){
            utils=new Utils();
        }
        return utils;
    }

    public String getMobileName(){
        String strMobileName= Build.MODEL;
        return strMobileName;
    }

    public String getOSVersion(){
        String strOSVersion=Build.VERSION.RELEASE;
        return strOSVersion;
    }

    public String getStrDateFromEpocTime(long epoctime){
        Log.i(TAG,"Epoctime = "+epoctime);
        Date date = new Date(epoctime);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String strDate = format.format(date);
        Log.i(TAG,"Date = "+strDate);
        return strDate;
    }

    public Long getEpocTImeFromStrDate(String strDate){
        long epoctime=0;
        try {
            Log.i(TAG,"Date = "+strDate);
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = formatter1.parse(strDate);
            epoctime=date.getTime();
            Log.i(TAG,"Epoctime = "+epoctime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return epoctime;
    }

    public void showDialog(Activity context,String msg){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert, null);
        dialogBuilder.setView(dialogView);

        TextView tvMsg = (TextView) dialogView.findViewById(R.id.textViewMsg);
        tvMsg.setText(msg);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void showProgressDialog(Context context){
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    public void closeProgressDialog(){
        if (progressDialog!=null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
