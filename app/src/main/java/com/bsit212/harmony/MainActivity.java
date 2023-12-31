package com.bsit212.harmony;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bsit212.harmony.cmd.FirebaseCmd;
import com.bsit212.harmony.models.*;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.giphy.sdk.core.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static boolean isLoggedIn = false;
    public FirebaseAuth mAuth;
//    public String currentUsername;
//    public static String otherUsername;
    public static UserModel otherUserModel;
    private Fragment currentFragment;
    public UserModel currentUserModel;
    public ChatroomModel chatroomModel;
    public String chatroomId;

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

        if (savedInstanceState != null){
            isLoggedIn = savedInstanceState.getBoolean("isLoggedsIn");
            currentUserModel = savedInstanceState.getParcelable("currentUserModel");
            chatroomId = savedInstanceState.getString("chatroomId");
            if (currentUserModel.getUid() != null){
                Log.i("yowell","currentUserModel: "+currentUserModel.getUid().toString());
            }
            otherUserModel = savedInstanceState.getParcelable("otherUserModel");
            if (otherUserModel != null){
                Log.i("yowell","otherUserModel: "+otherUserModel.getUid().toString());
            }
        } else {
            FirebaseUser currentUser = mAuth.getCurrentUser();

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
                launchFragment(launchFragment.login);
                super.onStart();
            }

        }


    }

    public void launchFragment(launchFragment launchFragment){
        switch(launchFragment){
            case login:
                LoginFragment lg = new LoginFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_main,lg).commit();
                setBackground(1);
                currentFragment = lg;
                break;
            case contacts:
                ContactsFragment ct = new ContactsFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left)
                        .replace(R.id.fl_main,ct).commit();
                setBackground(4);
                currentFragment = ct;
                break;
            case message:
                MessageFragment ms = new MessageFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom)
                        .replace(R.id.fl_main,ms).commit();
                setBackground(3);
                currentFragment = ms;
                break;
            case register:
                RegisterFragment fr = new RegisterFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.fl_main,fr).commit();
                setBackground(1);
                currentFragment = fr;
                break;
            case call:
                CallFragment cl = new CallFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fl_main,cl).commit();
                setBackground(1);
                currentFragment = cl;
                break;
        }

    }

    private void setBackground(int bg){
        int i = 0;
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            i = R.drawable.bg_cloud_ls;
        } else {
            i = R.drawable.bg_cloud;
        }
        switch(bg){
            case 1:
                getWindow().setBackgroundDrawableResource(i);
                getWindow().setStatusBarColor(ContextCompat.getColor(this,android.R.color.transparent));
                break;
            case 2:
                getWindow().setBackgroundDrawableResource(i);
                getWindow().setStatusBarColor(ContextCompat.getColor(this,android.R.color.transparent));
                break;
            case 3:
                getWindow().setBackgroundDrawableResource(i);
                getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.BlueFadingNight));
                break;
            case 4:
                getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.BlueDarkPastel)));
                break;
            default:
                getWindow().setBackgroundDrawableResource(i);
                Toast.makeText(MainActivity.this, "defaultBG", Toast.LENGTH_SHORT).show();
                getWindow().setStatusBarColor(ContextCompat.getColor(this,android.R.color.transparent));
                break;
        }
    }

    public void login(String email, String password){
        LoginFragment.login_changeUI(LoginFragment.LoginState.in_ongoing, this);
        Log.e("yowell","sadlife1");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("yowell","sadlife2 task is successful "+FirebaseCmd.currentUserId());
                            fetchUserModel(FirebaseCmd.currentUserId(), null, null, new FetchUserCallback() {
                                @Override
                                public void onUserFetched(UserModel user) {
                                    Log.e("yowell","sadlife3");
                                    currentUserModel = user;
                                    isLoggedIn=true;
                                    launchFragment(launchFragment.contacts);
                                }
                                @Override
                                public void onUserFetchFailed() {
                                    LoginFragment.login_changeUI(LoginFragment.LoginState.in_incorrect, MainActivity.this);
                                }
                            });
                        } else {
                            LoginFragment.login_changeUI(LoginFragment.LoginState.in_incorrect, MainActivity.this);
                            Log.e("yowell","sadlife4");
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
                            Map<String, Object> user = new HashMap<>();
                            user.put("username", username);
                            user.put("email", email);
                            FirebaseCmd.allUserCollectionReference()
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
                            RegisterFragment.register_changeUI(RegisterFragment.RegisterState.in_incorrect,MainActivity.this);
                        }
                    }
                });
    }
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        launchFragment(launchFragment.login);
        isLoggedIn=false;
    }
    public interface ContactsFetchListener {
        void onContactsFetched(List<UserModel> allContacts);
    }
    public interface AddContactLister {
        void onContactAdd();
        void onContactAddFail();
        void onContactExists();
    }
    public void createContact(String email, AddContactLister callback) {
        // Query the user's contacts to check if the contact already exists
        Query contactQuery = FirebaseCmd.currentUserContactsCollection().whereEqualTo("email", email).limit(1);

        contactQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                if (!querySnapshot.isEmpty()) {
                    Log.d("yowell", "Contact already exists");
                    callback.onContactExists();
                } else {
                    fetchUserModel(null, null, email, new FetchUserCallback() {
                        @Override
                        public void onUserFetched(UserModel user) {
                            Map<String, Object> contactData = new HashMap<>();
                            contactData.put("username", user.getUsername());
                            contactData.put("email", user.getEmail());

                            FirebaseCmd.currentUserContactsCollection().document(user.getUid()).set(contactData)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d("yowell", "fetchOtherUserModel(): Contact created successfully ");
                                        callback.onContactAdd();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("yowell", "fetchOtherUserModel(): Error creating contact: " + e);
                                        callback.onContactAddFail();
                                    });
                        }
                        @Override
                        public void onUserFetchFailed() {
                            callback.onContactAddFail();
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
                    if (search == null || username.toLowerCase().contains(search.toLowerCase()) || email.toLowerCase().contains(search.toLowerCase())) {
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
    public interface ChatroomsFetchListener {
        void onChatroomsFetched(List<ContactsModel> chatrooms);
        void onChatroomsFetchFailed();
    }
    public void fetchChatroomsAndLastMessages(String search, final ChatroomsFetchListener listener) {
        Query chatroomsQuery = FirebaseCmd.allChatroomCollectionReference()
                .whereArrayContains("users", FirebaseCmd.currentUserId());
        chatroomsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot chatroomsSnapshot = task.getResult();
                List<ContactsModel> chatrooms = new ArrayList<>();

                int i = 0;

                Log.d("yowell","fetchChatroomsAndLastMessages().chatroomsQuery is successful");

                if(chatroomsSnapshot.isEmpty()){
                    Log.d("yowell","chatroomsSnapshot.isEmpty()");
                    listener.onChatroomsFetchFailed();
                } else {
                    for (QueryDocumentSnapshot chatroomDoc : chatroomsSnapshot) {
                        List<String> userStrings = new ArrayList<>();
                        String chatroomId = chatroomDoc.getId();
                        List<Object> users = (List<Object>) chatroomDoc.get("users");
                        if (users != null) {
                            int d;
                            for (Object user : users) {
                                if (user instanceof String) {
                                    userStrings.add((String) user);
                                }
                            }
                        }
                        i++;
                        String otherUserId = findOtherUser(userStrings, FirebaseCmd.currentUserId());
                        Log.d("yowell","fetchChatroomsAndLastMessages().otheruserid: "+ otherUserId + " "+i);
                        Log.d("yowell","fetchChatroomsAndLastMessages().user"+ userStrings);
                        fetchUserModel(otherUserId, null, null, new FetchUserCallback() {
                            @Override
                            public void onUserFetched(UserModel user) {
                                Log.d("yowell","onUserFetched: "+user.getUsername());
                                // Step 4: Get the last message in the chatroom
                                Query messagesQuery = chatroomDoc.getReference().collection("messages")
                                        .orderBy("timestamp", Query.Direction.DESCENDING)
                                        .limit(1);
                                messagesQuery.get().addOnCompleteListener(messageTask -> {
                                    if (messageTask.isSuccessful()) {
                                        QuerySnapshot messageSnapshot = messageTask.getResult();
                                        MessageModel lastMessage = null;
                                        if (!messageSnapshot.isEmpty()) {
                                            lastMessage = messageSnapshot.getDocuments().get(0).toObject(MessageModel.class);
                                        }
                                        ContactsModel chatroom = new ContactsModel(chatroomId, user, lastMessage);
                                        if (search == null || chatroom.getUser().getUsername().toLowerCase().contains(search.toLowerCase()) || chatroom.getUser().getEmail().toLowerCase().contains(search.toLowerCase())) {
                                            chatrooms.add(chatroom);
                                        }

                                        if (chatroom.getLastMessage()!=null ){
                                            if (chatroom.getLastMessage().getTimestamp()!=null){
                                                Log.d("yowell",chatroom.getLastMessage().getText());
                                                if (chatrooms != null){
                                                    chatrooms.sort(new Comparator<ContactsModel>() {
                                                        @Override public int compare(ContactsModel c1, ContactsModel c2) {
                                                            MessageModel lastMessage1 = c1.getLastMessage();
                                                            MessageModel lastMessage2 = c2.getLastMessage();
                                                            // Check if either of the ContactsModel has no lastMessage
                                                            if (lastMessage1 == null && lastMessage2 == null) {
                                                                return 0; // Both have no last message, they are considered equal.
                                                            } else if (lastMessage1 == null) {
                                                                return -1; // contactsModel1 has no last message, it goes to the bottom.
                                                            } else if (lastMessage2 == null) {
                                                                return 1; // contactsModel2 has no last message, it goes to the bottom.
                                                            } else {
                                                                // Compare based on the timestamp of the last message.
                                                                return Long.compare(lastMessage2.getTimestamp().getSeconds(), lastMessage1.getTimestamp().getSeconds());
                                                            }
                                                        }
                                                    });
                                                }
                                            }

                                        }
                                        listener.onChatroomsFetched(chatrooms);
//                                    if (chatrooms.size() == chatroomsSnapshot.size()) {
//                                        listener.onChatroomsFetched(chatrooms);
//                                    }
                                    } else {
                                        listener.onChatroomsFetchFailed();
                                    }
                                });
                            }
                            @Override
                            public void onUserFetchFailed() {
                                Log.d("yowell","onUserFetchFail: ");
                                listener.onChatroomsFetchFailed();
                            }
                        });
                    }
                }

            } else {
                listener.onChatroomsFetchFailed();
                Log.d("yowell","fetchChatroomsAndLastMessages().chatroomsQuery is not successful");
            }
        });
    }
    private String findOtherUser(List<String> users, String currentUser) {
        for (String user : users) {
            if (!user.equals(currentUser)) {
                return user;
            }
        }
        return null;
    }
    public interface FetchUserCallback {
        void onUserFetched(UserModel user);
        void onUserFetchFailed();
    }
    public void fetchUserModel(String uid, String username, String email, FetchUserCallback callback) {

        Query query = null;

        if (username!=null){
            query = FirebaseCmd.allUserCollectionReference().whereEqualTo("username", username);
        } else if (email != null) {
            query = FirebaseCmd.allUserCollectionReference().whereEqualTo("email", email);
        } else if (uid != null) {
            FirebaseCmd.allUserCollectionReference().document(uid).get().addOnCompleteListener(task -> {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()){
                    UserModel user = new UserModel(uid, documentSnapshot.getString("username"), documentSnapshot.getString("email"));
                    callback.onUserFetched(user);
                }
            });
        }


        if (query!=null){
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot matchingDocument = querySnapshot.getDocuments().get(0);
                        UserModel user = new UserModel(matchingDocument.getId(),matchingDocument.getString("username"),matchingDocument.getString("email"));
                        callback.onUserFetched(user);

                    } else {
                        Log.e("yowell", "fetchOtherUserModel(): Failed to get otherUserModel");
                        callback.onUserFetchFailed();
                    }
                } else {
                    Log.e("yowell", "fetchOtherUserModel(): Error while searching for the User: " + task.getException());
                    callback.onUserFetchFailed();
                }
            }).addOnFailureListener(e -> {
                Log.e("yowell", "fetchOtherUserModel(): Error while fetching user: " + e.getMessage());
                callback.onUserFetchFailed();
            });
        }
    }
    public interface MessageCallback {
        void onMessagesReceived(List<MessageModel> messages, Query messagesCollectionSorted, CollectionReference messagesCollection);
        void onMessageFetchFailed();
    }
    public void getOrCreateChatroom(String uid2, MessageCallback callback) {
        String chatroomId = FirebaseCmd.getChatroomId(FirebaseCmd.currentUserId(), uid2);
        DocumentReference chatroomDocRef = FirebaseCmd.getChatroomReference(chatroomId);

        chatroomDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //if chatroom exists
                        getMessages(chatroomDocRef, callback);

                    } else {
                        createChatroom(chatroomDocRef, callback);
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
                        MessageModel message = new MessageModel();
                        message.setSender(messageDoc.getString("sender"));
                        message.setText(messageDoc.getString("text"));
                        message.setTimestamp(messageDoc.getTimestamp("timestamp"));
                        items.add(message);
                    }
                    callback.onMessagesReceived(items, messagesCollectionSorted, messagesCollection);
                })
                .addOnFailureListener(e -> {
                    // Handle the case where fetching chatroom messages failed
                    callback.onMessageFetchFailed();
                });

    }
    private void createChatroom(DocumentReference chatroomDocRef, MessageCallback callback) {
        List<String> users = new LinkedList<>();
        users.add(FirebaseCmd.currentUserId());
        users.add(otherUserModel.getUid());
        chatroomDocRef.set(new ChatroomModel(users))
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

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        Log.d("yowell","onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isLoggedIn", isLoggedIn);
        outState.putParcelable("currentUserModel", currentUserModel);
        outState.putParcelable("otherUserModel", otherUserModel);
        outState.putString("chatRoomId",chatroomId);

//        getSupportFragmentManager().putFragment(outState, "currentFragment",currentFragment);
    }
}

