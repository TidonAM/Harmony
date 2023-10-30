package com.bsit212.harmony;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static TextView tvUsername;
    static ImageButton imLogout;

    public static Button btnpeople1;
    public static Button btnpeople2;
    public static Button btnpeople3;
    static RecyclerView recyclerView;

    static ContactsRecyclerView contactsRecyclerView;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts,container,false);

        List<ItemData> items = new ArrayList<>();

        tvUsername = view.findViewById(R.id.home_tv_username);
        imLogout = view.findViewById(R.id.home_ib_logout);
//        btnpeople1 = view.findViewById(R.id.btn_people1);
//        btnpeople2 = view.findViewById(R.id.btn_people2);
//        btnpeople3 = view.findViewById(R.id.btn_people3);
        MainActivity mainActivity = (MainActivity) getActivity();

        recyclerView = view.findViewById(R.id.contacts_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainActivity.fetchContacts();
        recyclerView.setAdapter(contactsRecyclerView);


        if (mainActivity.currentUsername != null) {
            tvUsername.setText(mainActivity.currentUsername);
        } else {
            Log.i("yowell","signingout");
            mainActivity.signOut();
        }

        if (MainActivity.isLoggedIn == true) {
            imLogout.setEnabled(true);
        } else {
            imLogout.setEnabled(false);
        }

        imLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.signOut();
                }
                LoginFragment.login_changeUI(LoginFragment.LoginState.out_ongoing,getContext());
            }
        });
//        btnpeople1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity mainActivity = (MainActivity) getActivity();
//                if (mainActivity != null) {
//                    mainActivity.ContactstoMessage();
//                }
//            }
//        });
//        btnpeople2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity mainActivity = (MainActivity) getActivity();
//                if (mainActivity != null) {
//                    mainActivity.ContactstoMessage();
//                }
//            }
//        });
//        btnpeople3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity mainActivity = (MainActivity) getActivity();
//                if (mainActivity != null) {
//                    mainActivity.ContactstoMessage();
//                }
//            }
//        });

        return view;
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