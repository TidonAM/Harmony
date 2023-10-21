package com.bsit212.harmony;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static TextView tvUsername;
    static ImageButton imLogout;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts,container,false);



        tvUsername = view.findViewById(R.id.home_tv_username);
        imLogout = view.findViewById(R.id.home_ib_logout);
        Button btnpeople1 = view.findViewById(R.id.btn_people1);
        Button btnpeople2 = view.findViewById(R.id.btn_people2);
        Button btnpeople3 = view.findViewById(R.id.btn_people3);
        btnpeople1.setOnClickListener(this);
        btnpeople2.setOnClickListener(this);
        btnpeople3.setOnClickListener(this);
//        if (MainActivity.isLoggedIn == true) {
//            imLogout.setEnabled(true);
//        } else {
//            imLogout.setEnabled(false);
//        }

        return view;

    }

    public void onClick(View v) {
        Intent intent = new Intent(getContext(), Message.class);
        Button b = (Button)v;
        intent.putExtra("chatinitial", b.getText().toString());
        switch (v.getId()) {
            case R.id.btn_people1:
                intent.putExtra("chatname", "Gardiola");
                break;
            case R.id.btn_people2:
                intent.putExtra("chatname", "Garcia");
                break;
            case R.id.btn_people3:
                intent.putExtra("chatname", "Pascua");
                break;
        }
        getContext().startActivity(intent);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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