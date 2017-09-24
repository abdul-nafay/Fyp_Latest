package com.sourcey.materiallogindemo.Helpers;

import com.sourcey.materiallogindemo.Model.LoginModel;
import com.sourcey.materiallogindemo.Model.SignupModel;
import com.sourcey.materiallogindemo.Model.User;

import org.json.JSONArray;
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

    public LoginModel parseLoginResponse(String s) {

        try {
            JSONObject response = new JSONObject(s);

            User user = new User();

            JSONObject responseArray = response.getJSONObject("user");

            int errorCode = response.getInt("error");
            String name = responseArray.getString("name");
            String email = responseArray.getString("email");
            String number = responseArray.getString("phone_number");
            String password = responseArray.getString("password");
            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(number);
            user.setPassword(password);

            switch (errorCode) {
                case 200:
                    return new LoginModel(200,response.getString("message"),user);
                default:
                    return new LoginModel(errorCode,response.getString("message"),user);
            }
        }

        catch (Exception e)
        {
            return null;
        }

    }


}
