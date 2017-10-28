package com.sourcey.movnpack.SP;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.IntentCompat;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.DrawerModule.DrawerActivity;
import com.sourcey.movnpack.LoginModule.LoginActivity;
import com.sourcey.movnpack.Model.ServiceProvider;
import com.sourcey.movnpack.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SPSignUpActivity extends Activity implements View.OnClickListener {

    String[] spinnerList = {"Labour","Cargo","Mandi","Picnic","Paccking","Electrician","Plumber"};

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_cnic) EditText _cnicText;
    @Bind(R.id.input_license_number) EditText _licenseNumberText;
    @Bind(R.id.input_categoryList) EditText _categoryListText;
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
        MaterialBetterSpinner spinner = (MaterialBetterSpinner) findViewById(R.id.input_categoryList);
        spinner.setAdapter(arrayAdapter);

        _loginLink.setOnClickListener((View.OnClickListener) this);
        _signupButton.setOnClickListener((View.OnClickListener) this);
        /*_loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), SPLoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });*/


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
        String category = _categoryListText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(name);
        serviceProvider.setEmail(email);
        serviceProvider.setPhoneNumber(mobileNumber);
        serviceProvider.setAddress(address);
        serviceProvider.setCNIC(cnicNumber);
        serviceProvider.setLicenseNumber(licenseNumber);
        serviceProvider.setCategory(1);
        serviceProvider.setPassword(reEnterPassword);

        DatabaseManager.getInstance(getApplicationContext()).addServiceProvider(serviceProvider);

        Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email",email);
        editor.commit();
        //MemorizerUtil.displayToast(getApplicationContext(),model.getMessage());
        startActivity(mainIntent);


    }

    public boolean validate(){

        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobileNumber = _mobileText.getText().toString();
        String address = _addressText.getText().toString();
        String cnicNumber = _cnicText.getText().toString();
        String licenseNumber = _licenseNumberText.getText().toString();
        String category = _categoryListText.getText().toString();
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

        if (licenseNumber.isEmpty() || licenseNumber.length() != 13){
            _licenseNumberText.setError("Enter Valid License Number");
            valid = false;
        }
        else {
            _licenseNumberText.setError(null);
        }

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
}