package com.sourcey.movnpack.Network;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.sourcey.movnpack.Helpers.API_TYPE;
import com.sourcey.movnpack.Utility.AppConstants;

import org.json.JSONObject;

import java.util.HashMap;

import static com.sourcey.movnpack.R.id.amount;

/**
 * Created by Abdul Nafay Waseem on 12/24/2017.
 */

public class MessageAsyncTask extends AsyncTask<String, Void, String> {

    private HashMap<String,String> params;
    private String url;
    private MessageAsyncInterface delegate;
    private API_TYPE apiType;
    public MessageAsyncTask(HashMap<String,String> params , String url , MessageAsyncInterface delegate , API_TYPE apiType)
    {
        this.params = params;
        this.url = url;
        this.delegate = delegate;
        this.apiType = apiType;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";
        try {
            response = callAPI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.delegate.didCompleteWithTask(this.params,this.apiType);
    }

    public String callAPI(){

        String response = "";
        try {

            HttpHandler httpHandler = new HttpHandler();
            Log.i("ALi",params.toString());
            response = httpHandler.performPostCallWithHeader(url, params);
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        return response;

    }

}



