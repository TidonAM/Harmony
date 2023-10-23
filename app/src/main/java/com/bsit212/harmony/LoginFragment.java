package com.bsit212.harmony;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.bsit212.harmony.MainActivity.goLogin;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bsit212.harmony.MainActivity;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static LinearLayout linearLayout;
    static EditText etUsername;
    static EditText etPassword;
    static TextInputLayout tilShowPassword;
    static TextView tvForgot;
    static TextView tvRegister;
    static Button btnLogin;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        linearLayout = view.findViewById(R.id.login_layout_main);
        etUsername = view.findViewById(R.id.login_et_username);
        etPassword = view.findViewById(R.id.login_et_password);
        tilShowPassword = view.findViewById(R.id.login_til_password);
        tvForgot = view.findViewById(R.id.login_tv_forget);
        tvRegister = view.findViewById(R.id.login_tv_register);
        btnLogin = view.findViewById(R.id.login_btn_login);
        etUsername.addTextChangedListener(login_textWatcher);
        etPassword.addTextChangedListener(login_textWatcher);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (etUsername.getText().toString() != null && etPassword.getText().toString() != null) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        if (mainActivity != null) {
                            mainActivity.login(etUsername.getText().toString(), etPassword.getText().toString());
                        }
                    }
                } catch(Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("yowell",e.toString());
                }
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://subscribe.linphone.org/login/email"));
                startActivity(browserIntent);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://subscribe.linphone.org/register/email"));
                startActivity(browserIntent);
//                bg.delete();
            }
        });

        return view;
    }

    public final TextWatcher login_textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String stringEtPassword = etPassword.getText().toString();
            String stringEtUsername = etUsername.getText().toString();
            if (stringEtPassword.length() < 2 || stringEtUsername.length() < 2) {
                btnLoginDisabled(getContext());
            } else {
                btnLoginEnable(getContext());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    public static void login_ongoing(android.content.Context context) {
        btnLogin.setText("Logging in");
        btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.Green));
        btnLogin.setEnabled(false);
        etUsername.setEnabled(false);
        etPassword.setEnabled(false);
        tilShowPassword.setEnabled(false);
    }

    public static void login_incorrect(android.content.Context context) {
        btnLogin.setText("Incorrect Credentials! Try Again");
        btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.Red));
        btnLogin.setEnabled(false);
        etUsername.setEnabled(true);
        etPassword.setEnabled(true);
        tilShowPassword.setEnabled(true);
    }

    public static void logout_success(android.content.Context context) {
        btnLogin.setText("Logged Out Successfully");
        btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.BlueCornflower));
        btnLogin.setEnabled(true);
        etPassword.setEnabled(true);
        etUsername.setEnabled(true);
        tilShowPassword.setEnabled(true);
    }

    public static void login_success(android.content.Context context) {
        btnLogin.setText("Log in");
        btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.BlueCornflower));
        btnLogin.setEnabled(true);
        etPassword.setEnabled(true);
        etUsername.setEnabled(true);
        tilShowPassword.setEnabled(true);
    }

    public static void logging_out(android.content.Context context){
        btnLogin.setEnabled(false);
        btnLogin.setText("Logging Out");
        btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.Gray));
    }

    public static void btnLoginEnable(android.content.Context context){
        btnLogin.setText("Log in");
        btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.BlueCornflower));
        btnLogin.setEnabled(true);
    }

    public static void btnLoginDisabled(android.content.Context context){
        btnLogin.setText("Log in");
        btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.Gray));
        btnLogin.setEnabled(false);
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
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

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_login, container, false);
//    }

}