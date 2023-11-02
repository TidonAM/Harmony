package com.bsit212.harmony;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

    static ImageButton imAdd;

    public EditText etSearch;

    public static Button btnpeople1;
    public static Button btnpeople2;
    public static Button btnpeople3;
    static RecyclerView recyclerView;
    String etSearchStr;

    MainActivity mainActivity;

    public List<UserModel> items;

    static ContactsRecyclerView contactsRecyclerView;

    public ContactsFragment() {
        // Required empty public constructor
    }

    private void showCustomEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_addcontact, null);
        builder.setView(dialogView);

        EditText emailEditText = dialogView.findViewById(R.id.et_email);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEditText.getText().toString();
                Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();
                mainActivity = (MainActivity) getActivity();
                mainActivity.createContact(email);
                refreshContacts();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts,container,false);
        mainActivity = (MainActivity) getActivity();
        items = new ArrayList<>();
        tvUsername = view.findViewById(R.id.home_tv_username);
        imLogout = view.findViewById(R.id.home_ib_logout);
        imAdd = view.findViewById(R.id.home_ib_add);
        etSearch = view.findViewById(R.id.contacts_searchuser);
        recyclerView = view.findViewById(R.id.contacts_recyclerview);

        if (mainActivity.currentUserModel != null) {
            tvUsername.setText(mainActivity.currentUserModel.getUsername());
        } else {
            Log.i("yowell","ContactsFragment: currentUserModel is null, signing out");
            mainActivity.signOut();
        }
        if (MainActivity.isLoggedIn == true) {
            imLogout.setEnabled(true);
        } else {
            imLogout.setEnabled(false);
        }

        if (TextUtils.isEmpty(etSearchStr)){
            etSearchStr = null;
        }
        etSearchStr = etSearch.getText().toString();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshContacts();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etSearchStr = etSearch.getText().toString();
                if (TextUtils.isEmpty(etSearchStr)){
                    etSearchStr = null;
                } else {
                    Log.i("yowell",etSearchStr);
                }
                refreshContacts();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
        });

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

        imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomEmailDialog();
            }
        });
        return view;
    }
    public void refreshContacts(){
        mainActivity.fetchContacts(etSearchStr,new MainActivity.ContactsFetchListener() {
            @Override
            public void onContactsFetched(List<UserModel> allContacts) {
                items.clear();
                items.addAll(allContacts);
                contactsRecyclerView = new ContactsRecyclerView(items);
                contactsRecyclerView.setOnItemClickListener(new ContactsRecyclerView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, String toptext, String bottomtext) {
                        Log.d("yowell","refreshContacts().onItemClick(): "+toptext+" "+bottomtext);
                        mainActivity.fetchOtherUserModel(null, toptext, null, new MainActivity.FetchUserCallback() {
                            @Override
                            public void onUserFetched(UserModel user) {
                                mainActivity.launchFragment(MainActivity.launchFragment.message);
                                mainActivity.otherUserModel = user;
                            }

                            @Override
                            public void onUserFetchFailed() {
                                Log.e("yowell","Can't get User");
                            }
                        });

                    }
                });
                recyclerView.setAdapter(contactsRecyclerView);
            }
        });
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