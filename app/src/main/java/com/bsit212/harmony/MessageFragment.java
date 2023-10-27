package com.bsit212.harmony;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class MessageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int counter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message,container,false);

//        Intent intent = view.getIntent();
//        String stringUsername = intent.getStringExtra("chatname");

        EditText etType = view.findViewById(R.id.et_type);
        List<String> items = new LinkedList<>();

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ChatRecyclerView chatRecyclerView = new ChatRecyclerView(items);
        recyclerView.setAdapter(chatRecyclerView);

        view.findViewById(R.id.img_send).setOnClickListener(view2 -> {
            String typed = etType.getText().toString();
            typed.replaceAll(System.getProperty("line.separator"), "");
            if (typed.matches("")) {
                Toast toast = Toast.makeText(getContext(), "Type first", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                items.add(typed);
                counter++;
                chatRecyclerView.notifyItemInserted(items.size()-1);
                recyclerView.scrollToPosition(items.size()-1);
                etType.setText("");
            }
        });

        view.findViewById(R.id.img_call).setOnClickListener(view2 -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.MessagetoCall();
            }
        });

        return view;
    }

    public MessageFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
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