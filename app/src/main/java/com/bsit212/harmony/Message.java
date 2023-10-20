package com.bsit212.harmony;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class Message extends AppCompatActivity {

    String []data = {"Hello","Hi","Yo"};
    int counter = 0;

    @Override
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
}