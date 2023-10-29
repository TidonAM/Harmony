package com.bsit212.harmony;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.telecom.Call;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static boolean isLoggedIn = false;

    private FirebaseAuth mAuth;
    public String currentUsername;
    FirebaseFirestore db;

    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
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
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fl_main,ms).commit();
                setBackground(1);
                break;
            case register:
                RegisterFragment fr = new RegisterFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fl_main,fr).commit();
                setBackground(1);
                break;
            case call:
                CallFragment cl = new CallFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fl_main,cl).commit();
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
                            db.collection("users").document(FirebaseAuth.getInstance().getUid()).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                       if (documentSnapshot.exists()) {
                                           String username = documentSnapshot.getString("username");
                                           if (username != null) {
                                               currentUsername = username;
                                           } else {
                                               currentUsername = "Username N/A";
                                           }
                                           launchFragment(launchFragment.contacts);
                                           isLoggedIn = true;
                                       } else {
                                           Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                       }
                                    });
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

    public void register(String email, String username, String password){
        RegisterFragment.register_changeUI(RegisterFragment.RegisterState.in_ongoing,this);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user1 = mAuth.getCurrentUser();
                            Map<String, Object> user = new HashMap<>();
                            user.put("username", username);
                            user.put("email", email);
                            db.collection("users")
                                    .document(FirebaseAuth.getInstance().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            launchFragment(launchFragment.login);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("yowell",e.toString());
                                            LoginFragment.login_changeUI(LoginFragment.LoginState.in_incorrect,MainActivity.this);
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            RegisterFragment.register_changeUI(RegisterFragment.RegisterState.in_incorrect,MainActivity.this);
                        }
                    }
                });
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        launchFragment(launchFragment.login);
        LoginFragment.login_changeUI(LoginFragment.LoginState.out_success, this);
    }

    public void ContactstoMessage(){
        launchFragment(launchFragment.message);
    }

    public void MessagetoCall(){
        launchFragment(launchFragment.call);
    }

    public CollectionReference getAllUsers(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public void setup() {
        TextView one = findViewById(ContactsFragment.btnpeople1.getLabelFor());
        Query query = getAllUsers()
                .whereGreaterThanOrEqualTo("username",one.getText().toString());

    }

    void Chatroom(String user2Uid) {
        String chatroomId = "";
        FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
            }
        });
        getChatroomId(mAuth.getCurrentUser().getUid().toString(),user2Uid);

    }

    public static String getChatroomId(String userId1, String userId2){
        if(userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        } else {
            return userId2+"_"+userId1;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            Log.i("yowell",currentUser.getUid());
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");
                            if (username != null) {
                                currentUsername = username;
                            } else {
                                currentUsername = "Username N/A";
                            }
                            launchFragment(launchFragment.contacts);
                            isLoggedIn = true;
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to success on signin", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        Log.i("yowell","null");
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fl_main);

        if (f instanceof MessageFragment) {
            launchFragment(launchFragment.contacts); // Replace with the correct logic to navigate to ContactsFragment
        } else if (f instanceof ContactsFragment) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastBackPressTime < 2500) {
                super.onBackPressed(); // Exit the app
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                lastBackPressTime = currentTime;
            }
        } else if(f instanceof CallFragment) {
            launchFragment(launchFragment.message);
        }
        else {
            super.onBackPressed();
        }
    }
}

