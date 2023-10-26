package com.bsit212.harmony;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.linphone.core.ChatRoom;
import org.linphone.core.*;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    public background bg = new background();

    public static boolean isLoggedIn = false;
    public boolean isHidden;
    public static boolean goLogin;
    public boolean isIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launchFragment_login();

    }

    public void launchFragment_login() {
        LoginFragment fr = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,fr).commit();
        getWindow().setBackgroundDrawableResource(R.drawable.bg_cloud);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
