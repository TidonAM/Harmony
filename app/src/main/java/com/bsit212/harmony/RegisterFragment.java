package com.bsit212.harmony;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    static LinearLayout linearLayout;

    static EditText etEmail;
    static EditText etUsername;
    static EditText etPassword;
    static TextInputLayout tilShowPassword;
    static TextView tvLogin;
    static Button btnRegister;

    static ImageView autoSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        linearLayout = view.findViewById(R.id.register_layout_main);
        etEmail = view.findViewById(R.id.register_et_email);
        etUsername = view.findViewById(R.id.register_et_username);
        etPassword = view.findViewById(R.id.register_et_password);
        tilShowPassword = view.findViewById(R.id.register_til_password);
        tvLogin = view.findViewById(R.id.register_tv_login);
        btnRegister = view.findViewById(R.id.register_btn_register);
        autoSet = view.findViewById(R.id.imageView4);
        etUsername.addTextChangedListener(register_textWatcher);
        etPassword.addTextChangedListener(register_textWatcher);

        autoSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etEmail.setText("angeltidon18@yahoo.com");
                etUsername.setText("angelt");
                etPassword.setText("angelt");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (etUsername.getText().toString() != null || etPassword.getText().toString() != null || etEmail.getText().toString() != null) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        if (mainActivity != null) {
                            mainActivity.register(etUsername.getText().toString(), etPassword.getText().toString(),etEmail.getText().toString());
                            Log.i("yowell","Register() Done");
                        }
                    }
                } catch(Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("yowell",e.toString());
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://subscribe.linphone.org/register/email"));
//                startActivity(browserIntent);
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.launchFragment_login();
                }
//                bg.delete();
            }
        });

        return view;
    }

    public final TextWatcher register_textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String stringEtPassword = etPassword.getText().toString();
            String stringEtUsername = etUsername.getText().toString();
            String stringEtEmail = etEmail.getText().toString();
            if (stringEtPassword.length() < 2 || stringEtUsername.length() < 2 || stringEtEmail.length() < 2 ) {
                btnRegisterDisabled(getContext());
            } else {
                btnRegisterEnable(getContext());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    public static void register_ongoing(android.content.Context context) {
        btnRegister.setText("Registering");
        btnRegister.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.Green));
        btnRegister.setEnabled(false);
        etUsername.setEnabled(false);
        etPassword.setEnabled(false);
        tilShowPassword.setEnabled(false);
    }

    public static void register_fail(android.content.Context context) {
        btnRegister.setText("Incorrect Credentials! Try Again");
        btnRegister.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.Red));
        btnRegister.setEnabled(false);
        etUsername.setEnabled(true);
        etPassword.setEnabled(true);
        tilShowPassword.setEnabled(true);
    }

    public static void register_success(android.content.Context context) {
        btnRegister.setText("Register");
        btnRegister.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.BlueCornflower));
        btnRegister.setEnabled(true);
        etPassword.setEnabled(true);
        etUsername.setEnabled(true);
        tilShowPassword.setEnabled(true);
    }

    public static void btnRegisterEnable(android.content.Context context){
        btnRegister.setText("Register");
        btnRegister.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.BlueCornflower));
        btnRegister.setEnabled(true);
    }

    public static void btnRegisterDisabled(android.content.Context context){
        btnRegister.setText("Register");
        btnRegister.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.Gray));
        btnRegister.setEnabled(false);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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