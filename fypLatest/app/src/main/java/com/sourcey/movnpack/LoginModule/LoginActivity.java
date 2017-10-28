package com.sourcey.movnpack.LoginModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.DrawerModule.DrawerActivity;
import com.sourcey.movnpack.Helpers.JsonParser;
import com.sourcey.movnpack.Model.LoginModel;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.Model.User;
import com.sourcey.movnpack.Network.ConnectionDetector;
//import com.sourcey.materiallogindemo.Network.ConnnectionDetector;
import com.sourcey.movnpack.Network.HttpHandler;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.SP.SPSignUpActivity;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.MemorizerUtil;
//import com.sourcey.materiallogindemo.Utility.MemorizerUtils;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    ProgressDialog progressDialog;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    @Bind(R.id.link_signup_sp) TextView _signupLinkSP;
    @Bind(R.id.loginLayout) LinearLayout loginLayout;
    @Bind(R.id.radioCheck) RadioGroup radioCheck;
    @Bind(R.id.userRadioBtn) RadioButton userRadioBtn;
    @Bind(R.id.serviceProviderRadioBtn) RadioButton spRadioBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener((View.OnClickListener)this);
        _signupLink.setOnClickListener((View.OnClickListener)this);
        _signupLinkSP.setOnClickListener((View.OnClickListener)this);



        loginLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                MemorizerUtil.hideSoftInput(v,getApplicationContext());

                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {

        if (_loginButton.getId() == view.getId()){

            login();

        }
        else if (_signupLink.getId() == view.getId()){
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);

            //finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        else if (_signupLinkSP.getId() == view.getId()){
            Intent intent = new Intent(getApplicationContext(), SPSignUpActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);

            //finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        ConnectionDetector connnectionDetector = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = connnectionDetector.isConnectingToInternet();


        if(userRadioBtn.isChecked()){
            // Hit API for User Login
            if (isInternetPresent) {
                new LoginRequest(email, password).execute();
            }
            else {
                MemorizerUtil.displayToast(getApplicationContext(),"No Internet Connection");
            }

        }

        else {
            // Hit API for Service Provider Login
            MemorizerUtil.displayToast(getApplicationContext(),"HIt SP API");

        }

        // TODO: Implement your own authentication logic here.


        onLoginSuccess();


    }



    @Override
    protected void onResume() {
        super.onResume();

        _emailText.setText("");
        _passwordText.setText("");

    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
        //onBackPressed();
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
       // finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid e mail address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }



    public class LoginRequest extends AsyncTask<String, Void, String> {

        private String email, password;

        public LoginRequest(String email, String password) {
            // this.id = id;
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Verifying details...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                response = login_api(email,password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            LoginModel model = JsonParser.getInstance().parseLoginResponse(s);

            if (model != null) {
                switch (model.getErrorCode()) {
                    case 200:
                        //Yahan Khulwa de Activity
                        Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
                        User user = model.getUser();

                        ////////
                        Session session = Session.getInstance();

                        session.setUser(user);

                        User userDB = DatabaseManager.getInstance(getApplicationContext()).getUser(user.getEmail());
                        if (userDB == null) {// Entry in DB

                            DatabaseManager.getInstance(getApplicationContext()).addUser(user);

                        }
                        else { // Need Nothing to do

                        }
                        /////
                        String email = user.getEmail();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email",email);
                        editor.commit();
                        MemorizerUtil.displayToast(getApplicationContext(),model.getMessage());
                        startActivity(intent);
                        finish();
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

            if(progressDialog != null){
                progressDialog.dismiss();
            }

        }

    }

    public String login_api(String email, String password) {
        String response = "";
        try {

            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);
             response = httpHandler.performPostCall(AppConstants.API_LOGIN, params);
            // response = httpHandler.performPostCall("https://androidfyp.000webhostapp.com/signup.php", params);

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        return response;

    }





}
