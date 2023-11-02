package com.bsit212.harmony;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    public static boolean isLoggedIn = false;
    private FirebaseAuth mAuth;
//    public String currentUsername;
//    public static String otherUsername;
    public static UserModel otherUserModel;
    public UserModel currentUserModel;

    FirebaseFirestore db;
    private long lastBackPressTime = 0;

//    public String otheruseruid;
    public enum launchFragment{
        login,
        contacts,
        message,
        register,
        call
    }

    public static String getCurrentUIDStr() {
        return FirebaseAuth.getInstance().getUid().toString();
    };

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
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_main,lg).commit();
                setBackground(1);
                break;
            case contacts:
                ContactsFragment ct = new ContactsFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left)
                        .replace(R.id.fl_main,ct).commit();
                setBackground(1);
                break;
            case message:
                MessageFragment ms = new MessageFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom)
                        .replace(R.id.fl_main,ms).commit();
                setBackground(1);
                break;
            case register:
                RegisterFragment fr = new RegisterFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, R.anim.fade_out)
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
                            FirebaseCmd.currentUserInfo().get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                       if (documentSnapshot.exists()) {
                                           currentUserModel.setUid(documentSnapshot.getId());
                                           currentUserModel.setUsername(documentSnapshot.getString("username"));
                                           currentUserModel.setEmail(documentSnapshot.getString("email"));
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
        isLoggedIn=false;
        LoginFragment.login_changeUI(LoginFragment.LoginState.out_success, this);
    }

    public interface ContactsFetchListener {
        void onContactsFetched(List<UserModel> allContacts);
    }

    public void createContact(String email) {
        // Query the user's contacts to check if the contact already exists
        Query contactQuery = FirebaseCmd.currentUserContactsCollection().whereEqualTo("email", email).limit(1);

        contactQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                if (!querySnapshot.isEmpty()) {
                    Log.d("yowell", "Contact already exists");
                } else {
                    fetchOtherUserModel(null, null, email, new FetchUserCallback() {
                        @Override
                        public void onUserFetched(UserModel user) {
                            Map<String, Object> contactData = new HashMap<>();
                            contactData.put("username", user.getUsername());
                            contactData.put("email", user.getEmail());

                            FirebaseCmd.currentUserContactsCollection().document(user.getUid()).set(contactData)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d("yowell", "fetchOtherUserModel(): Contact created successfully ");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("yowell", "fetchOtherUserModel(): Error creating contact: " + e);
                                    });
                        }
                        @Override
                        public void onUserFetchFailed() {

                        }
                    });

                }
            } else {
                // Handle the case where checking for the contact failed
                Log.e("yowell", "Error checking contact: " + task.getException());
            }
        });
    }

    public void fetchContacts(String search, ContactsFetchListener listener){
        Log.i("yowell","fetchContacts()");
        Query allContactsQuery = FirebaseCmd.currentUserContactsCollection();

        // Add a listener to retrieve all contacts
        allContactsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("yowell","fetchContacts(): task Successful");
                QuerySnapshot querySnapshot = task.getResult();
                List<UserModel> allContacts = new ArrayList<>();

                for (QueryDocumentSnapshot document : querySnapshot) {
                    String uid = document.getId();
                    String username = document.getString("username");
                    String email = document.getString("email");
                    if (search == null || username.toLowerCase().contains(search.toLowerCase())) {
                        UserModel contactUserModel = new UserModel(uid,username, email);
                        allContacts.add(contactUserModel);
                        Log.i("yowell", "     " + username + " " + email);
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

    public interface FetchUserCallback {
        void onUserFetched(UserModel user);
        void onUserFetchFailed();
    }

    public void fetchOtherUserModel(String uid, String username, String email, FetchUserCallback callback) {

        Query query = null;

        if (username!=null){
            query = FirebaseCmd.currentUserContactsCollection().whereEqualTo("username", username);
        } else if (email!=null){
            query = FirebaseCmd.currentUserContactsCollection().whereEqualTo("email", email);
        } else if (uid!=null){
            query = FirebaseCmd.currentUserContactsCollection().whereEqualTo(FieldPath.documentId(), uid);
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot matchingDocument = querySnapshot.getDocuments().get(0);
                    otherUserModel.setUid(matchingDocument.getId());
                    otherUserModel.setEmail(matchingDocument.getString("email"));
                    otherUserModel.setUsername(matchingDocument.getString("username"));
                    callback.onUserFetched(otherUserModel);
                } else {
                    Log.e("yowell", "fetchOtherUserModel(): Failed to get otherUserModel");
                    callback.onUserFetchFailed();
                }
            } else {
                Log.e("yowell", "fetchOtherUserModel(): Error while searching for the username: " + task.getException());
                callback.onUserFetchFailed();
            }
        }).addOnFailureListener(e -> {
            Log.e("yowell", "fetchOtherUserModel(): Error while fetching user: " + e.getMessage());
            callback.onUserFetchFailed();
        });
    }


    public interface MessageCallback {
        void onMessagesReceived(List<MessageModel> messages,Query messagesCollectionSorted, CollectionReference messagesCollection);
        void onMessageFetchFailed();
    }

    public void getOrCreateChatroom(String uid2, MessageCallback callback) {
        String chatroomId = FirebaseCmd.getChatroomId(getCurrentUIDStr(), uid2);
        DocumentReference chatroomDocRef = FirebaseCmd.getChatroomReference(chatroomId);

        chatroomDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //if chatroom exists
                        CollectionReference messagesCollection = chatroomDocRef.collection("messages");

                        Query messagesCollectionSorted = chatroomDocRef.collection("messages").orderBy("timestamp", Query.Direction.ASCENDING);
                        messagesCollection.get()
                                        .addOnCompleteListener(task -> {
                                            if(task.isSuccessful()) {
                                                QuerySnapshot querySnapshot = task.getResult();
                                                if (!querySnapshot.isEmpty()) {
                                                    messagesCollectionSorted
                                                            .get()
                                                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                                                List<MessageModel> items = new LinkedList<>();
                                                                for (QueryDocumentSnapshot messageDoc : queryDocumentSnapshots) {
                                                                    String sender = messageDoc.getString("sender");
                                                                    String text = messageDoc.getString("text");
                                                                    Timestamp timestamp = messageDoc.getTimestamp("timestamp");
                                                                    MessageModel message = new MessageModel();
                                                                    message.setSender(sender);
                                                                    Log.i("yowell","getChatroom Message: "+text);
                                                                    message.setText(text);
                                                                    message.setTimestamp(timestamp);
                                                                    items.add(message);
                                                                    callback.onMessagesReceived(items,messagesCollectionSorted,messagesCollection);
                                                                }
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.i("yowell","Messages exists, but failed to fetch");
                                                                callback.onMessageFetchFailed();
                                                            });
                                                } else {
                                                    List<MessageModel> items = new LinkedList<>();
                                                    callback.onMessagesReceived(items,messagesCollectionSorted, messagesCollection);
                                                    Log.i("yowell","Messages do not exist. Creating..");
                                                }
                                            } else {

                                            }

                                        });

                    } else {
                        //if chatroom does not exist
                        chatroomDocRef.set(new ChatroomModel(getCurrentUIDStr(), uid2))
                                .addOnSuccessListener(aVoid -> {
                                    CollectionReference messagesCollection = chatroomDocRef.collection("messages");
                                    List<MessageModel> items = new LinkedList<>();
                                    callback.onMessagesReceived(items,messagesCollection, messagesCollection);
                                })
                                .addOnFailureListener(e -> {

                                });
                    }
                });
    }

    private void getMessages(DocumentReference chatroomReference, MessageCallback callback) {
        CollectionReference messagesCollection = chatroomReference.collection("messages");
        Query messagesCollectionSorted = messagesCollection.orderBy("timestamp", Query.Direction.ASCENDING);

        messagesCollectionSorted.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<MessageModel> items = new LinkedList<>();
                    for (QueryDocumentSnapshot messageDoc : queryDocumentSnapshots) {
                        String sender = messageDoc.getString("sender");
                        String text = messageDoc.getString("text");
                        Timestamp timestamp = messageDoc.getTimestamp("timestamp");
                        MessageModel message = new MessageModel();
                        message.setSender(sender);
                        message.setText(text);
                        message.setTimestamp(timestamp);
                        items.add(message);
                    }
                    callback.onMessagesReceived(items, messagesCollectionSorted, messagesCollection);
                })
                .addOnFailureListener(e -> {
                    // Handle the case where fetching chatroom messages failed
                    callback.onMessageFetchFailed();
                });
    }

    private void createChatroom(DocumentReference chatroomDocRef, String uid2, MessageCallback callback) {
        chatroomDocRef.set(new ChatroomModel(getCurrentUIDStr(), uid2))
                .addOnSuccessListener(aVoid -> {
                    CollectionReference messagesCollection = chatroomDocRef.collection("messages");
                    List<MessageModel> items = new LinkedList<>();
                    callback.onMessagesReceived(items, messagesCollection, messagesCollection);
                })
                .addOnFailureListener(e -> {
                    // Handle the case where creating the chatroom failed
                });
    }

    public void getCurrentUserModel(getCurrentUserCallback callback) {
        FirebaseCmd.currentUserInfo().get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserModel currentUserModel = new UserModel(
                                documentSnapshot.getId(),
                                documentSnapshot.getString("username"),
                                documentSnapshot.getString("email")
                        );
                        callback.onCurrentUserFetched(currentUserModel);
                    } else {
                        callback.onCurrentUserFetchedFailed();
                    }
                }).addOnFailureListener(e -> {
                    callback.onCurrentUserFetchedFailed();
                });
    }

    public CompletableFuture<UserModel> getCurrentUserModel() {
        CompletableFuture<UserModel> future = new CompletableFuture<>();

        FirebaseCmd.currentUserInfo().get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserModel currentUserModel = new UserModel(
                                documentSnapshot.getId(),
                                documentSnapshot.getString("username"),
                                documentSnapshot.getString("email")
                        );
                        future.complete(currentUserModel);
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(e -> {
                    future.completeExceptionally(e);
                });

        return future;
    }

    public interface getCurrentUserCallback {
        void onCurrentUserFetched(UserModel userModel);
        void onCurrentUserFetchedFailed();
    }

    @Override
    public void onStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        otherUserModel = new UserModel(null,null,null);
        if(currentUser != null && isLoggedIn == false){
            getCurrentUserModel(new getCurrentUserCallback() {
                @Override
                public void onCurrentUserFetched(UserModel userModel) {
                    currentUserModel = userModel;
                    launchFragment(launchFragment.contacts);
                    isLoggedIn = true;
                }
                @Override
                public void onCurrentUserFetchedFailed() {
                    finish();
                }
            });

            super.onStart();
        } else {
            super.onStart();
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

