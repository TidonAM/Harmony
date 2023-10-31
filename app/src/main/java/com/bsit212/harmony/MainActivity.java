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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static boolean isLoggedIn = false;
    private FirebaseAuth mAuth;
    public String currentUsername;
    public static String otherUsername;
    FirebaseFirestore db;
    private long lastBackPressTime = 0;
    public enum launchFragment{
        login,
        contacts,
        message,
        register,
        call
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        launchFragment(launchFragment.login);

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

    public static CollectionReference getAllUsers(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    void Chatroom(String user2Uid) {
        String chatroomId = "";
        FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
            }
        });
        getChatroomId(mAuth.getCurrentUser().getUid().toString(),user2Uid);

    }

    public interface ContactsFetchListener {
        void onContactsFetched(List<ItemData> allContacts);
    }

    public void fetchContacts(String search, ContactsFetchListener listener){
        Log.i("yowell","fetchContacts()");
        DocumentReference userDocumentRef = getAllUsers().document(FirebaseAuth.getInstance().getUid());
        CollectionReference contactsCollection = userDocumentRef.collection("contacts");
        Query allContactsQuery;

        if (search != null) {
            allContactsQuery = contactsCollection.whereEqualTo("username", search);
            Log.i("yowell","fetchContacts(): search is not null");
        } else {
            allContactsQuery = contactsCollection;
            Log.i("yowell","fetchContacts(): search is null");
        }

        // Add a listener to retrieve all contacts
        allContactsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("yowell","fetchContacts(): task Successful");
                QuerySnapshot querySnapshot = task.getResult();
                List<ItemData> allContacts = new ArrayList<>();

                for (QueryDocumentSnapshot document : querySnapshot) {

                    Log.i("yowell","fetchContacts(): for loop ongoing");

                    Map<String, Object> contactData = document.getData();
                    Object username = contactData.get("username");
                    Object email = contactData.get("email");

                    if (username != null) {
                        String usernameStr = username.toString();
                        String emailStr = email.toString();

                        // Create an ItemData object or update your existing data structure
                        ItemData item = new ItemData(usernameStr,emailStr);
                        allContacts.add(item);
                        Log.i("yowell","     "+usernameStr+" "+emailStr);
                    } else {
                        Log.i("yowell","fetchContacts(): username is null");
                    }
                }
                Log.i("yowell","allContacts.toString(): "+allContacts.toString());
                listener.onContactsFetched(allContacts);

            } else {
                Log.i("yowell","fetchContacts(): task Failed");
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });


    }

    public void fetchOtherUID(String username){
        DocumentReference currentUserDocumentRef = getAllUsers().document(FirebaseAuth.getInstance().getUid());
        CollectionReference contactsCollection = currentUserDocumentRef.collection("contacts");

        Query query = contactsCollection.whereEqualTo("username",username);

        query.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot matchingDocument = querySnapshot.getDocuments().get(0);
                    otherUsername = username;
                    String otheruseruid = matchingDocument.getId();
                    Log.i("yowell", "otheruseruid: "+otheruseruid);
                } else {
                    Log.e("yowell", "user does not exist");
                }
            } else {
                Log.e("yowell", "Error while searching for the username: " + task.getException());
            }
        });
    }

    public static String getChatroomId(String userId1, String userId2){
        if(userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        } else {
            return userId2+"_"+userId1;
        }
    }

    public void createChatroom(String uid1, String uid2) {
        DocumentReference chatroomDocRef = db.collection("chatrooms").document(getChatroomId(uid1, uid2));

        Chatroom chatroom = new Chatroom(uid1, uid2);

        // Set the data for the chatroom document
        chatroomDocRef.set(chatroom)
                .addOnSuccessListener(aVoid -> {
                    // Successfully created chatroom
                    // You can also create subcollections for messages and metadata here if needed
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    class Chatroom {
        private String user1;
        private String user2;

        public Chatroom() {
            // Required for Firestore
        }

        public Chatroom(String user1, String user2) {
            this.user1 = user1;
            this.user2 = user2;
        }

        public String getUser1() {
            return user1;
        }

        public String getUser2() {
            return user2;
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

