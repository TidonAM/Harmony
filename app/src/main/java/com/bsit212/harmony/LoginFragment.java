package com.bsit212.harmony;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment{

//    TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static LinearLayout linearLayout;
    int currentAccountIndex;
    static EditText etUsername;
    static EditText etPassword;
    static TextInputLayout tilShowPassword;
    static TextView tvForgot;
    static TextView tvRegister;
    static Button btnLogin;
    static ImageView autoSet;

    public LoginFragment() {
        // Required empty public constructor
    }

    public void onClick(View v){
        String[] usernames = {
                "angeltidon18@yahoo.com",
                "tatsudoni2600@gmail.com",
                // Add more usernames as needed
        };

        String[] passwords = {
                "angelt",
                "tatsudoni",
                // Add more passwords as needed
        };
        if (v.getId() == R.id.imageView4){
            if (currentAccountIndex < usernames.length) {
                etUsername.setText(usernames[currentAccountIndex]);
                etPassword.setText(passwords[currentAccountIndex]);
                currentAccountIndex = (currentAccountIndex + 1) % usernames.length;
            }
        } else if (v.getId() == R.id.login_btn_login) {
            if (etUsername.getText().toString() != null && etPassword.getText().toString() != null) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.login(etUsername.getText().toString(),etPassword.getText().toString());
                }
            }
        } else if (v.getId() == R.id.login_tv_forget) {

        } else if (v.getId() == R.id.login_tv_register) {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.launchFragment(MainActivity.launchFragment.register);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentAccountIndex = 0;
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        linearLayout = view.findViewById(R.id.login_layout_main);
        etUsername = view.findViewById(R.id.login_et_username);
        etPassword = view.findViewById(R.id.login_et_password);
        tilShowPassword = view.findViewById(R.id.login_til_password);
        tvForgot = view.findViewById(R.id.login_tv_forget);
        tvRegister = view.findViewById(R.id.login_tv_register);
        btnLogin = view.findViewById(R.id.login_btn_login);
        autoSet = view.findViewById(R.id.imageView4);
        etUsername.addTextChangedListener(login_textWatcher);
        etPassword.addTextChangedListener(login_textWatcher);
        autoSet.setOnClickListener(this::onClick);
        btnLogin.setOnClickListener(this::onClick);
        tvForgot.setOnClickListener(this::onClick);
        tvRegister.setOnClickListener(this::onClick);

        return view;
    }
    public final TextWatcher login_textWatcher = new TextWatcher() {
        public boolean isValid(String email) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String stringEtPassword = etPassword.getText().toString();
            String stringEtUsername = etUsername.getText().toString();
            if (stringEtPassword.length() < 6 || !isValid(stringEtUsername)) {
                login_changeUI(LoginState.btnDisable,getContext());
            } else {
                login_changeUI(LoginState.btnEnable,getContext());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    public enum LoginState {
        in_ongoing,
        in_incorrect,
        in_success,
        out_success,
        out_ongoing,
        btnEnable,
        btnDisable
    }

    public static void login_changeUI(LoginState state,android.content.Context context){
        switch (state) {
            case in_ongoing:
                btnLogin.setText("Logging in");
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.Green));
                btnLogin.setEnabled(false);
                etUsername.setEnabled(false);
                etPassword.setEnabled(false);
                tilShowPassword.setEnabled(false);
                break;
            case in_incorrect:
                btnLogin.setText("Incorrect Credentials! Try Again");
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.Red));
                btnLogin.setEnabled(false);
                etUsername.setEnabled(true);
                etPassword.setEnabled(true);
                tilShowPassword.setEnabled(true);
                break;
            case in_success:
                btnLogin.setText("Log in");
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.BlueCornflower));
                btnLogin.setEnabled(true);
                etPassword.setEnabled(true);
                etUsername.setEnabled(true);
                tilShowPassword.setEnabled(true);
                break;
            case out_success:
                btnLogin.setText("Logged Out Successfully");
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.BlueCornflower));
                btnLogin.setEnabled(false);
                etPassword.setEnabled(true);
                etUsername.setEnabled(true);
                tilShowPassword.setEnabled(true);
                break;
            case out_ongoing:
                btnLogin.setEnabled(false);
                btnLogin.setText("Logging Out");
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.Gray));
                break;
            case btnEnable:
                btnLogin.setText("Log in");
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.BlueCornflower));
                btnLogin.setEnabled(true);
                break;
            case btnDisable:
                btnLogin.setText("Log in");
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.Gray));
                btnLogin.setEnabled(false);
                break;
        }
    }

//    TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    }

}