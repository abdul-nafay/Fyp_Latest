package com.sourcey.materiallogindemo.Helpers;

import com.sourcey.materiallogindemo.Model.SignupModel;

import org.json.JSONObject;

/**
 * Created by abdul on 9/20/17.
 */

public class JsonParser {

    static JsonParser sharedInstance;

    private JsonParser() {


    }

    public static JsonParser getInstance()
    {
        if (sharedInstance == null) {
            sharedInstance = new JsonParser();
        }
        return sharedInstance;
    }

    public SignupModel ParseSignupResponse(String s) {

        try {
            JSONObject response = new JSONObject(s);
            int errorCode = response.getInt("error");
            switch (errorCode) {
                case 200:
                  return new SignupModel(200,response.getString("message"));
                default:
                    return new SignupModel(errorCode,response.getString("message"));
            }
        }

        catch (Exception e)
        {
                return null;
        }


    }



}
