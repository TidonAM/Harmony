package com.bsit212.harmony;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_message);

            Intent intent = getIntent();
            String stringUsername = intent.getStringExtra("chatname");

            EditText etType = findViewById(R.id.et_type);
            List<String> items = new LinkedList<>();

            RecyclerView recyclerView = findViewById(R.id.RecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ChatRecyclerView chatRecyclerView = new ChatRecyclerView(items);
            recyclerView.setAdapter(chatRecyclerView);

            findViewById(R.id.img_send).setOnClickListener(view -> {
                String typed = etType.getText().toString();
                typed.replaceAll(System.getProperty("line.separator"), "");
                if (typed.matches("")) {
                    Toast toast = Toast.makeText(Message.this, "Type first", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    items.add(typed);
                    counter++;
                    chatRecyclerView.notifyItemInserted(items.size()-1);
                    recyclerView.scrollToPosition(items.size()-1);
                    etType.setText("");
                }
            });

            findViewById(R.id.img_call).setOnClickListener(view -> {
                Intent intent2 = new Intent(Message.this, call.class);
                intent2.putExtra("chatname", stringUsername);
                Message.this.startActivity(intent2);
            });
        }
        return inflater.inflate(R.layout.fragment_message, container, false);
    }
}