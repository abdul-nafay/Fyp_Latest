package com.sourcey.movnpack.SP;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.sourcey.movnpack.Helpers.JsonParser;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.Model.ServiceProvider;
import com.sourcey.movnpack.Model.SpProfileViewModel;
import com.sourcey.movnpack.Network.HttpHandler;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.MemorizerUtil;
import com.sourcey.movnpack.Utility.Utility;

import java.util.HashMap;

import butterknife.Bind;


public class spProfileInfo extends AppCompatActivity {


    @Bind(R.id.sp_name)
    TextView _spName;
    @Bind(R.id.sp_mobile)
    TextView _spMobileNumber;
    @Bind(R.id.sp_category)
    TextView _spCategory;
    @Bind(R.id.sp_registration_number)
    TextView _spRegistrationNumber;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_profile_info);
        _spName = (TextView) findViewById(R.id.sp_name);
        _spCategory = (TextView) findViewById(R.id.sp_category);
        _spMobileNumber = (TextView) findViewById(R.id.sp_mobile);
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Verifying details...");
        progressDialog.show();

        String number = (String) getIntent().getExtras().get("number");
        new SP_ProfileInfo(number).execute();

    }


    public class SP_ProfileInfo extends AsyncTask<String, Void, String> {

        private String phoneNumber;

        public SP_ProfileInfo(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//
//            progressDialog = new ProgressDialog(LoginActivity.this,
//                    R.style.AppTheme_Dark_Dialog);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("Verifying details...");
//            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                response = spProfile_api(phoneNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            SpProfileViewModel model = JsonParser.getInstance().parseModelViewResponse(s);
            progressDialog.hide();
            if (model != null) {
                switch (model.getErrorCode()) {
                    case 200:
                        //Yahan Khulwa de Activity
                        ServiceProvider serviceProvider = (ServiceProvider) model.getServiceProvider();

                        ////////
                        Session session = Session.getInstance();


                        session.setServiceProvider(serviceProvider);


                        _spName.setText(serviceProvider.getName());
                        _spCategory.setText(Utility.getCategoryNameFromServiceCategory(serviceProvider.getCategory()));
                        _spMobileNumber.setText(serviceProvider.getPhoneNumber());

                       /* User userDB = DatabaseManager.getInstance(getApplicationContext()).getServiceProvider(serviceProvider.getEmail());
                        if (userDB == null) {// Entry in DB

                            DatabaseManager.getInstance(getApplicationContext()).addServiceProvider(serviceProvider);

                        }
                        else { // Need Nothing to do

                        }
                        /////
                        String email = serviceProvider.getEmail();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email",email);
                        editor.commit();
*/

                        break;

                    case 500:

                        MemorizerUtil.displayToast(getApplicationContext(),model.getMessage());

                        break;

                    default:
                        //Error Message
                        MemorizerUtil.displayToast(getApplicationContext(),"Something went wrong");
                        break;
                }
            }
            else {

            }

           /* if(progressDialog != null){
                progressDialog.dismiss();
            }*/

        }

    }


    public String spProfile_api(String phoneNumber) {
        String response = "";
        try {

            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("phone_number" , phoneNumber);

            response = httpHandler.performPostCall(AppConstants.API_SP_Details, params);

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        return response;

    }


}
