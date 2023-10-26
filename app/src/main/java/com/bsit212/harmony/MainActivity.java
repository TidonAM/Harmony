package com.bsit212.harmony;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static boolean isLoggedIn = false;
    public static boolean goLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launchFragment_login();

    }



    public void launchFragment_contacts() {
        ContactsFragment fr = new ContactsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,fr).commit();
        getWindow().setBackgroundDrawableResource(R.drawable.bg_cloud);
    }

    public void launchFragment_message() {
        MessageFragment fr = new MessageFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,fr).commit();
        getWindow().setBackgroundDrawableResource(R.drawable.bg_call);
    }

    public void launchFragment_register() {
        RegisterFragment fr = new RegisterFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,fr).commit();
        getWindow().setBackgroundDrawableResource(R.drawable.bg_cloud);
    }

    public void login(){

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//    }
}
