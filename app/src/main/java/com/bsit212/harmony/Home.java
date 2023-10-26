package com.bsit212.harmony;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String stringUsername = intent.getStringExtra("username");
        TextView tvUsername = findViewById(R.id.home_tv_username);
        if (stringUsername == null) {
            tvUsername.setText("");
        } else {
//            tvUsername.setText(stringUsername);
            tvUsername.setText(core.getAccountList().toString());
        }

        Button btnpeople1 = findViewById(R.id.btn_people1);
        Button btnpeople2 = findViewById(R.id.btn_people2);
        Button btnpeople3 = findViewById(R.id.btn_people3);
        home_core.getAccountList().toString();
        btnpeople1.setOnClickListener(this);
        btnpeople2.setOnClickListener(this);
        btnpeople3.setOnClickListener(this);

    }

    public void onClick(View v) {
        Intent intent = new Intent(Home.this, Message.class);
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
        Home.this.startActivity(intent);
    }


    public void onPause() {
        unregister();
        super.onPause();
    }

    public void unregister() {
        // Here we will disable the registration of our Account
        Account account = home_core.getDefaultAccount();
        if (account == null) {
            return;
        }
        AccountParams params = account.getParams();
        // Returned params object is const, so to make changes we first need to clone it
        AccountParams clonedParams = params.clone();
        // Now let's make our changes
        clonedParams.setRegisterEnabled(false);
        // And apply them
        account.setParams(clonedParams);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        bg.unregister();
//        this.finish();
//    }

}