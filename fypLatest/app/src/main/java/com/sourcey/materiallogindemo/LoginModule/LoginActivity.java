package com.sourcey.materiallogindemo.LoginModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.materiallogindemo.HomeModule.HomeMapActivity;
import com.sourcey.materiallogindemo.Helpers.JsonParser;
import com.sourcey.materiallogindemo.Model.LoginModel;
import com.sourcey.materiallogindemo.Model.User;
import com.sourcey.materiallogindemo.Network.ConnectionDetector;
//import com.sourcey.materiallogindemo.Network.ConnnectionDetector;
import com.sourcey.materiallogindemo.Network.HttpHandler;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Utility.AppConstants;
import com.sourcey.materiallogindemo.Utility.MemorizerUtil;
//import com.sourcey.materiallogindemo.Utility.MemorizerUtils;

import java.util.HashMap;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    ProgressDialog progressDialog;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    @Bind(R.id.loginLayout) LinearLayout loginLayout;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                MemorizerUtil.hideSoftInput(v,getApplicationContext());

                return false;
            }
        });
        
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);

                //finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        /*final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
*/
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.


        ConnectionDetector connnectionDetector = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = connnectionDetector.isConnectingToInternet();
        if (isInternetPresent) {
            new LoginRequest(email, password).execute();
        }
        else {
            MemorizerUtil.displayToast(getApplicationContext(),"No Internet Connection");
        }
        onLoginSuccess();

        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }*/

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
                        Intent intent = new Intent(getApplicationContext(), HomeMapActivity.class);
                        User user = model.getUser();
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
