package com.bsit212.harmony;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class MainActivity extends AppCompatActivity {

    public static boolean isLoggedIn = false;
    public static boolean goLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        launchFragment(launchFragment.login);

    }

    public enum launchFragment{
        login,
        contacts,
        message,
        register,
        call
    }

    public void launchFragment(launchFragment launchFragment){
        int bg = 0;
        switch(launchFragment){
            case login:
                LoginFragment lg = new LoginFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fl_main,lg).commit();
                setBackground(1);
                break;
            case contacts:
                ContactsFragment ct = new ContactsFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fl_main,ct).commit();
                setBackground(1);
                break;
            case message:
                MessageFragment ms = new MessageFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,ms).commit();
                setBackground(1);
                break;
            case register:
                RegisterFragment fr = new RegisterFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,fr).commit();
                setBackground(1);
                break;
            case call:
                CallFragment cl = new CallFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,cl).commit();
                setBackground(1);
                break;
        }

    }

    private void setBackground(int bg){
        switch(bg){
            case 1:
                getWindow().setBackgroundDrawableResource(R.drawable.bg_cloud);
                break;
            case 2:
                getWindow().setBackgroundDrawableResource(R.drawable.bg_call);
                break;
            default:
                getWindow().setBackgroundDrawableResource(R.drawable.bg_cloud);
                Toast.makeText(MainActivity.this, "defaultBG", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void login(String email, String password){
        LoginFragment.login_changeUI(LoginFragment.LoginState.in_ongoing, this);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            LoginFragment.login_changeUI(LoginFragment.LoginState.in_success,MainActivity.this);
                            launchFragment(launchFragment.contacts);
                            isLoggedIn = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            LoginFragment.login_changeUI(LoginFragment.LoginState.in_incorrect, MainActivity.this);
                        }
                    }
                });
    }

    public void register(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            launchFragment(launchFragment.login);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        launchFragment(launchFragment.login);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            launchFragment(launchFragment.contacts);
            isLoggedIn = true;
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//    }
}

