package com.bsit212.harmony;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bsit212.harmony.cmd.FirebaseCmd;
import com.bsit212.harmony.models.ContactsModel;
import com.bsit212.harmony.models.UserModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
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
    static ProgressBar progressBar;

    public static Button btnpeople1;
    public static Button btnpeople2;
    public static Button btnpeople3;
    static RecyclerView recyclerView;
    String etSearchStr;

    MainActivity mainActivity;

    public List<ContactsModel> items;

    static ContactsRecyclerView contactsRecyclerView;

    private androidx.appcompat.app.AlertDialog customEmailDialog;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public boolean isValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showCustomEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_addcontact, null);
        builder.setView(dialogView);
        TextInputLayout tilEmail = dialogView.findViewById(R.id.til_email);
        EditText emailEditText = dialogView.findViewById(R.id.et_email);
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilEmail.setError(null);
            }
        });

        customEmailDialog = new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Add Email")
                .setView(dialogView)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .show();

        customEmailDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                if (isValid(email)) {
//                    Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();
                    mainActivity = (MainActivity) getActivity();
                    mainActivity.createContact(email, new MainActivity.AddContactLister() {
                        @Override public void onContactAdd() {
                            refreshC();
                            customEmailDialog.dismiss(); // Dismiss the dialog here
                        }
                        @Override public void onContactExists() {
                            tilEmail.setError("Contact already exists");
                        }
                        @Override public void onContactAddFail() {
                            tilEmail.setError("User does not exist");
                        }
                    });
                } else {
                    tilEmail.setError("Invalid Email");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts,container,false);
        mainActivity = (MainActivity) getActivity();
        items = new ArrayList<>();
        tvUsername = view.findViewById(R.id.home_tv_username);
        progressBar = view.findViewById(R.id.contacts_progressbar);
        imLogout = view.findViewById(R.id.home_ib_logout);
        imAdd = view.findViewById(R.id.home_ib_add);
        etSearch = view.findViewById(R.id.contacts_searchuser);
        recyclerView = view.findViewById(R.id.contacts_recyclerview);

        if (mainActivity.currentUserModel != null) {
            tvUsername.setText(mainActivity.currentUserModel.getUsername());
        } else {
            Log.i("yowell","ContactsFragment: currentUserModel is null, signing out");
            mainActivity.signOut();
            mainActivity.isLoggedIn = false;
        }
        if (mainActivity.isLoggedIn == true) {imLogout.setEnabled(true);} else {imLogout.setEnabled(false);}

        etSearchStr = etSearch.getText().toString();
        if (TextUtils.isEmpty(etSearchStr)){ etSearchStr = null; }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshC();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etSearchStr = etSearch.getText().toString();
                if (TextUtils.isEmpty(etSearchStr)){
                    etSearchStr = null;
                } else {
                    Log.i("yowell",etSearchStr);
                }
                refreshC();
            }
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
        });
        imLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.signOut();
                    mainActivity.isLoggedIn = false;
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
//    public void refreshContacts(){
//        mainActivity.fetchContacts(etSearchStr,new MainActivity.ContactsFetchListener() {
//            @Override
//            public void onContactsFetched(List<UserModel> allContacts) {
//                items.clear();
//                items.addAll(allContacts);
//                contactsRecyclerView = new ContactsRecyclerView(items);
//                contactsRecyclerView.setOnItemClickListener(new ContactsRecyclerView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(int position, String toptext, String bottomtext) {
//                        Log.d("yowell","refreshContacts().onItemClick(): "+toptext+" "+bottomtext);
//                        mainActivity.fetchUserModel(true,null, toptext, null, new MainActivity.FetchUserCallback() {
//                            @Override
//                            public void onUserFetched(UserModel user) {
//                                mainActivity.otherUserModel = user;
//                                Log.d("yowell","refreshContacts().onUserFetched(): " + mainActivity.otherUserModel.getUsername() + " " + mainActivity.otherUserModel.getEmail());
//                                mainActivity.launchFragment(MainActivity.launchFragment.message);
//                            }
//
//                            @Override
//                            public void onUserFetchFailed() {
//                                Log.e("yowell","Can't get User");
//                            }
//                        });
//
//                    }
//                });
//                recyclerView.setAdapter(contactsRecyclerView);
//            }
//        });
//
//    }

    public void isLoading(boolean bool){
        if (bool==true){
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    public void refreshC(){
        isLoading(true);
        Log.d("yowell","refreshC()");
        mainActivity.fetchChatroomsAndLastMessages(etSearchStr, new MainActivity.ChatroomsFetchListener() {
            @Override
            public void onChatroomsFetched(List<ContactsModel> chatrooms) {
                Log.d("yowell","onChatroomsFetched()");
                items.clear();
                items.addAll(chatrooms);
                contactsRecyclerView = new ContactsRecyclerView(items);
                contactsRecyclerView.setOnItemClickListener(new ContactsRecyclerView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, String toptext, String bottomtext) {
                        Log.d("yowell","refreshC().onItemClick(): "+toptext+" "+bottomtext);
                        mainActivity.fetchUserModel(null, toptext, null, new MainActivity.FetchUserCallback() {
                            @Override
                            public void onUserFetched(UserModel user) {
                                mainActivity.otherUserModel = user;
                                Log.d("yowell","refreshC().onUserFetched(): " + mainActivity.otherUserModel.getUsername() + " " + mainActivity.otherUserModel.getEmail());
                                mainActivity.launchFragment(MainActivity.launchFragment.message);
                            }
                            @Override
                            public void onUserFetchFailed() {
                                Log.e("yowell","Can't get User");
                            }
                        });
                    }
                });
                recyclerView.setAdapter(contactsRecyclerView);
                isLoading(false);
            }

            @Override
            public void onChatroomsFetchFailed() {
                Log.e("yowell","onChatroomsFetchFailed()");
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
            mParam1 = getArguments().getString("param1");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

}