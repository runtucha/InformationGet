package com.example.informationget.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.informationget.db.Information;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by glenn_cui on 17-8-11.
 */
public class Util {
    private static String TAG = "gtest";
    public static boolean handleGankApiResponse(String response){
        //Log.d(TAG, "handleGankApiResponse: response :"+response);

        if (!TextUtils.isEmpty(response)){

            try {

                JSONObject jsonObject = new JSONObject(response);
                Log.d(TAG, ": responese is " + response);

                JSONArray allInformation = jsonObject.getJSONArray("results");

                Log.d(TAG, "handleGankApiResponse:allInformation is  "+allInformation);

                for (int i = 0;i<allInformation.length();i++){
                    JSONObject androidObject = allInformation.getJSONObject(i);
                    Log.d(TAG, "handleGankApiResponse: androidObject" + androidObject);
                    Information information = new Information();
                    //Log.d(TAG, "handleGankApiResponse: 1 " + androidObject.getString("error"));

                    information.setInfoId(androidObject.getString("_id"));

                    information.setDesc(androidObject.getString("desc"));
                    information.setWho(androidObject.getString("who"));
                    information.setType(androidObject.getString("type"));
                    information.setUrl(androidObject.getString("url"));
                    information.setPublishedAt(androidObject.getString("publishedAt"));
                    information.save();

                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;

    }
}
