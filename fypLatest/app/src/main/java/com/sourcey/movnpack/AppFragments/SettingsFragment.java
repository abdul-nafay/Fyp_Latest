package com.sourcey.movnpack.AppFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Helpers.JsonParser;
import com.sourcey.movnpack.Model.User;
import com.sourcey.movnpack.Model.UpdateUserModel;
import com.sourcey.movnpack.Network.ConnectionDetector;
import com.sourcey.movnpack.Network.HttpHandler;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.MemorizerUtil;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.sourcey.movnpack.Helpers.Session.sharedInstance;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final String TAG = "UserPUpdate Fragment";

    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String reEnterPassword;

    private String newName;
    private String newPassword;

    ProgressDialog progressDialog;
    boolean update;


    @Bind(R.id.init)
    TextView _init;

    @Bind(R.id.input_name)
    EditText _nameText;
    //@Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.emailTextView)
    TextView _emailText;
    @Bind(R.id.mobileTextView)
    TextView _mobileText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @Bind(R.id.btn_update)
    Button _updateButton;
    @Bind(R.id.updateUserProfileLayout)
    LinearLayout updateUserProfileLayout;


    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileUpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        User user = sharedInstance.getUser();
        phoneNumber = user.getPhoneNumber();
        email = user.getEmail();
        password = user.getPassword();
        name = user.getName();




    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _nameText = (EditText) getView().findViewById(R.id.input_name);
        _nameText.setText(name);

        _emailText = (TextView) getView().findViewById(R.id.emailTextView);
        _emailText.setText(email);

        _mobileText = (TextView) getView().findViewById(R.id.mobileTextView);
        _mobileText.setText(phoneNumber);

        _passwordText = (EditText) getView().findViewById(R.id.input_password);
        _reEnterPasswordText=  (EditText) getView().findViewById(R.id.input_reEnterPassword);




        _updateButton = (Button) getView().findViewById(R.id.btn_update);

        _updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void update() {
        Log.d(TAG, "update");

        if (!validate()) {
            onUpdateFailed();
            return;
        }

        _updateButton.setEnabled(false);


        //_nameText = (EditText) getView().findViewById(R.id.input_name);
        newName = _nameText.getText().toString();

        newPassword = _passwordText.getText().toString();
        //reEnterPassword = _reEnterPasswordText.getText().toString();


        // TODO: Implement your own signup logic here.

        ConnectionDetector connnectionDetector = new ConnectionDetector(getActivity().getApplicationContext());
        Boolean isInternetPresent = connnectionDetector.isConnectingToInternet();
        if (isInternetPresent) {
            new updateRequest(newName, email, phoneNumber, newPassword).execute();
        } else {
            MemorizerUtil.displayToast(getActivity().getApplicationContext(),"No Internet Connection");
        }

        User userModel = new User();
        userModel.setName(newName);
        userModel.setEmail(email);
        userModel.setPhoneNumber(phoneNumber);
        userModel.setPassword(newPassword);



        DatabaseManager db = DatabaseManager.getInstance(getActivity());
        update = db.edituserprofile(userModel);
        if(update){
            sharedInstance.setUser(userModel);
            //   fragmentClass = HomeMapFragment.class;
        }
        onUpdateSuccess();


    }



    public boolean validate() {
        boolean valid = true;

        name = _nameText.getText().toString();
        password = _passwordText.getText().toString();
        reEnterPassword = _reEnterPasswordText.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
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

    public void onUpdateFailed() {
        MemorizerUtil.displayToast(getActivity().getApplicationContext(), "update failed");
        _updateButton.setEnabled(true);
    }

    public void onUpdateSuccess() {
        _updateButton.setEnabled(true);
        MemorizerUtil.displayToast(getActivity().getApplicationContext(),"Update Successful!");

        //setResult(RESULT_OK, null);
        //finish();
    }


    public String update_api(String name, String email, String mobile, String password) {
        String response = "";
        try {

            HttpHandler httpHandler = new HttpHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("name", name);
            params.put("email", email);
            params.put("phone_number", mobile);
            params.put("password", password);
            response = httpHandler.performPostCall(AppConstants.API_UPDATE_USER, params);
            //response = httpHandler.performPostCall(AppConstants.API_SIGNUP, params);

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        return response;

    }


    public class updateRequest extends AsyncTask<String, Void, String> {

        private String name, email, mobile, password;

        public updateRequest(String name, String email, String mobile, String password) {
            // this.id = id;
            this.name = name;
            this.email = email;
            this.mobile = mobile;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();*/

        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                response = update_api(name, email, mobile, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            UpdateUserModel model = JsonParser.getInstance().ParseUserupdateResponse(s);

            if (model != null) {
                switch (model.getErrorCode()) {
                    case 200:

                        ///
/*

                        Intent intent = new Intent(getActivity().getApplicationContext(), DrawerActivity.class);
                        ComponentName cn = intent.getComponent();
                        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email", email);
                        editor.commit();
                        MemorizerUtil.displayToast(getActivity().getApplicationContext(), model.getMessage());
                        startActivity(mainIntent);
*/

                      /*  ///
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

                        MemorizerUtil.displayToast(getActivity().getApplicationContext(), model.getMessage());
                        break;
                    default:
                        //Error Message
                        MemorizerUtil.displayToast(getActivity().getApplicationContext(), "Something went wrong");
                        //Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        break;
                }
            } else {

            }

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }

    }




















}
