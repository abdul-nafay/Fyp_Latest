package com.sourcey.movnpack.SP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.IntentCompat;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.DrawerModule.DrawerActivity;
import com.sourcey.movnpack.DrawerModule.SPDrawerActivity;
import com.sourcey.movnpack.Helpers.JsonParser;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.LoginModule.LoginActivity;
import com.sourcey.movnpack.LoginModule.SignupActivity;
import com.sourcey.movnpack.Model.ServiceProvider;
import com.sourcey.movnpack.Model.SignupModel;
import com.sourcey.movnpack.Network.ConnectionDetector;
import com.sourcey.movnpack.Network.HttpHandler;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.MemorizerUtil;
import com.sourcey.movnpack.Utility.SPCategory;
import com.sourcey.movnpack.Utility.Utility;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SPSignUpActivity extends Activity implements View.OnClickListener {

    String[] spinnerList = {"Labour","Cargo","Mandi","Picnic","Packing","Electrician","Plumber"};
    ProgressDialog progressDialog;


    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_cnic) EditText _cnicText;
    @Bind(R.id.input_license_number) EditText _licenseNumberText;

    @Bind(R.id.input_license_number_parent)
    TextInputLayout _licenseNumberTextParent;
    @Bind(R.id.input_categoryList) Spinner _categoryListText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    @Bind(R.id.signUpLayout) LinearLayout signUpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sp_activity_sign_up);
        ButterKnife.bind(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,spinnerList);
        _categoryListText = (Spinner) findViewById(R.id.input_categoryList);

        _categoryListText.setAdapter(arrayAdapter);


        _categoryListText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position == 0 || position == 5 || position == 6){
                _licenseNumberTextParent.setVisibility(View.GONE);
            }
            else {
                _licenseNumberTextParent.setVisibility(View.VISIBLE);
             }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.i("","");
        }
        });


        _loginLink.setOnClickListener((View.OnClickListener) this);
        _signupButton.setOnClickListener((View.OnClickListener) this);


    }


    public void signUp(){

        if(!validate()){

            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobileNumber = _mobileText.getText().toString();
        String address = _addressText.getText().toString();
        String cnicNumber = _cnicText.getText().toString();
        String licenseNumber = _licenseNumberText.getText().toString();
        String category = _categoryListText.getSelectedItem().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        ConnectionDetector connnectionDetector = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = connnectionDetector.isConnectingToInternet();
        if (isInternetPresent) {
            new SignUpRequestSP(name, email, mobileNumber, password,address,cnicNumber,licenseNumber,1).execute();
        }
        else {
            MemorizerUtil.displayToast(getApplicationContext(),"No Internet Connection");
        }

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(name);
        serviceProvider.setEmail(email);
        serviceProvider.setPhoneNumber(mobileNumber);
        serviceProvider.setAddress(address);
        serviceProvider.setCNIC(cnicNumber);
        serviceProvider.setLicenseNumber(licenseNumber);
        serviceProvider.setCategory(Utility.getCategoryForServiceProviderUsingString(category));

        serviceProvider.setPassword(reEnterPassword);

        DatabaseManager.getInstance(getApplicationContext()).addServiceProvider(serviceProvider);

/*
        Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email",email);
        editor.commit();
        //MemorizerUtil.displayToast(getApplicationContext(),model.getMessage());
        startActivity(mainIntent);
*/


    }

    public boolean validate(){

        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobileNumber = _mobileText.getText().toString();
        String address = _addressText.getText().toString();
        String cnicNumber = _cnicText.getText().toString();
        String licenseNumber = _licenseNumberText.getText().toString();
        String category = _categoryListText.getSelectedItem().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3){
            _nameText.setError("Atleast 3 Characters");
            return false;
        }
        else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            _emailText.setError("Enter a Valid Email Address");
            valid = false;
        }
        else{
            _emailText.setError(null);
        }

        if (mobileNumber.isEmpty() || mobileNumber.length() != 10){
            _mobileText.setError("Enter a Valid Mobile Number");
            valid = false;
        }
        else {
            _mobileText.setError(null);
        }

        if (address.isEmpty()){
            _addressText.setError("Enter Valid Address");
            valid = false;
        }
        else {
            _addressText.setError(null);
        }

        if (cnicNumber.isEmpty() || cnicNumber.length() != 13){
            _cnicText.setError("Enter Valid CNIC Number");
            valid = false;
        }
        else {
            _cnicText.setError(null);
        }

        /*if (licenseNumber.isEmpty() || licenseNumber.length() != 13){
            _licenseNumberText.setError("Enter Valid License Number");
            valid = false;
        }
        else {
            _licenseNumberText.setError(null);
        }*/

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }


    @Override
    public void onClick(View v) {

        if (_loginLink.getId() == v.getId()){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        else if (_signupButton.getId() == v.getId()){

            signUp();
        }
    }

    public class SignUpRequestSP extends AsyncTask<String, Void, String> {

        private String name, email, mobile, password;
        private String Address;
        private String CNIC;
        private String LicenseNumber;
        private int Category;


        public SignUpRequestSP(String name, String email, String mobile, String password,String address,String cnic,String licenseNumber,int category) {
            // this.id = id;
            this.name = name;
            this.email = email;
            this.mobile = mobile;
            this.password = password;
            this.Address = address;
            this.CNIC = cnic;
            this.LicenseNumber = licenseNumber;
            this.Category = category;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(SPSignUpActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                response = signUpSP_Api(name, email, mobile, password,Address,CNIC,LicenseNumber,Category);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            SignupModel model = JsonParser.getInstance().ParseSignupResponse(s);

            if (model != null) {
                switch (model.getErrorCode()) {
                    case 200:

                        ServiceProvider serviceProvider = new ServiceProvider();
                        serviceProvider.setName(name);
                        serviceProvider.setEmail(email);
                        serviceProvider.setPhoneNumber(mobile);
                        serviceProvider.setAddress(Address);
                        serviceProvider.setCNIC(CNIC);
                        serviceProvider.setLicenseNumber(LicenseNumber);
                        serviceProvider.setCategory(1);
                        serviceProvider.setPassword(password);

                        DatabaseManager.getInstance(getApplicationContext()).addServiceProvider(serviceProvider);

                        Session.getInstance().setServiceProvider(serviceProvider);

                        Intent intent = new Intent(getApplicationContext(), SPDrawerActivity.class);
                        ComponentName cn = intent.getComponent();
                        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);

                        HashMap<String,String> map = new HashMap<>();
                        map.put("Email",email);
                        map.put("Type","1");

                        Gson gson = new Gson();
                        String mapString = gson.toJson(map);

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("HashString",mapString).apply();
                        editor.commit();
                        MemorizerUtil.displayToast(getApplicationContext(),model.getMessage());
                        startActivity(mainIntent);
                        /*
                        ///
                        //Yahan Khulwa de Activity
                        Intent intent = new Intent(SignupActivity.this, HomeMapActivity.class);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email",email);
                        editor.commit();
                        finish();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                        */
                        break;

                    case 500:

                        MemorizerUtil.displayToast(getApplicationContext(),model.getMessage());
                        break;
                    default:
                        //Error Message
                        MemorizerUtil.displayToast(getApplicationContext(),"Something went wrong");
                        //Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            else {

            }

            if(progressDialog != null){
                progressDialog.dismiss();
            }

        }

    }

    public String signUpSP_Api(String name, String email, String mobile, String password,String address,String cnic,String licenseNumber,int category) {
        String response = "";
        try {

            HttpHandler httpHandler = new HttpHandler();
            HashMap params = new HashMap<>();
            params.put("name", name);
            params.put("email", email);
            params.put("phone_number", mobile);
            params.put("password", password);
            params.put("address",address);
            params.put("cnic",cnic);
            params.put("license_no",licenseNumber);
            SPCategory spCat = Utility.getCategoryForServiceProviderUsingString(_categoryListText.getText().toString().toUpperCase());
            params.put("category",spCat.value);
            response = httpHandler.performPostCall(AppConstants.API_SIGNUP_SP, params);
            //response = httpHandler.performPostCall(AppConstants.API_SIGNUP, params);

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        return response;

    }
}