package com.bsit212.harmony.cmd;

import com.bsit212.harmony.MainActivity;
import com.bsit212.harmony.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FirebaseCmd {
    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static UserModel otherUserModel() {
        return MainActivity.otherUserModel;
    }

    public static CollectionReference currentUserContactsCollection() {
        return currentUserInfo().collection("contacts");
    }

    public static boolean isLoggedIn() {
        if(currentUserId()!=null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserInfo() {
        return allUserCollectionReference().document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatroomReference(String chatroomId) {
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getMessageReference(String chatroomId) {
        return getChatroomReference(chatroomId).collection("messages");
    }

    public static String getChatroomId(String uid1, String uid2){
        if(uid1.hashCode()<uid2.hashCode()){
            return uid1+"_"+uid2;
        } else {
            return uid2+"_"+uid1;
        }
    }

    public static CollectionReference allChatroomCollectionReference() {
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}
