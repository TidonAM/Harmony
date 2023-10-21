package com.bsit212.harmony;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.linphone.core.ChatRoom;
import org.linphone.core.*;




public class MainActivity extends AppCompatActivity {

    public Core core;
    public Factory factory;
    private ChatRoom chatRoom;
    public background bg = new background();

    public static boolean isLoggedIn = false;
    public boolean isHidden;
    public static boolean goLogin;
    public boolean isIn;
    LoginFragment login;
    ContactsFragment contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goLogin = false;

        login = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,login).addToBackStack(null).commit();
        getWindow().setBackgroundDrawableResource(R.drawable.bg_cloud);

        //liblinphone Core initialization//
        factory = Factory.instance();
        core = factory.createCore(null, null, this);
        isHidden = true;

    }

    private CoreListener coreListener = new CoreListenerStub() {
        @Override
        public void onAccountRegistrationStateChanged(Core core, Account account, RegistrationState state, String message) {
            LoginFragment.login_ongoing(MainActivity.this);
            if (state == RegistrationState.Failed) {
                //if failed or incorrect
                isLoggedIn = false;
                LoginFragment.login_incorrect(MainActivity.this);
            } else if (state == RegistrationState.Cleared) {
                //if Logged out successfully
                isLoggedIn = false;
                LoginFragment.logout_success(MainActivity.this);
            } else if (state == RegistrationState.Ok && isLoggedIn == false) {
                //if Logged in successfully
                isLoggedIn = true;
                LoginFragment.login_success(MainActivity.this);

                contacts = new ContactsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,contacts).commit();


//                if (etUsername.getText().toString() == null) {
//                    tvUsername.setText("");
//                } else {
//                    tvUsername.setText(etUsername.getText().toString());
//                    Log.i("yowell",core.getAccountList().toString());
//                }

            }
        }

//        @Override
//        public void onMessageReceived(Core core, ChatRoom chatRoom1, ChatMessage message) {
//            if (chatRoom == null) {
//                if (chatRoom.hasCapability(ChatRoomCapabilities.Basic.toInt())) {
//                    chatRoom = chatRoom1;
//                    ((EditText) findViewById(R.id.remote_address)).setText(chatRoom1.getPeerAddress().asStringUriOnly());
//                    ((EditText) findViewById(R.id.remote_address)).setEnabled(false);
//                }
//            }
//            chatRoom.markAsRead();
//            addMessageToHistory(message);
//        }
//
//    };
//
//    private ChatMessageListener chatMessageListener = new ChatMessageListenerStub() {
//        @Override
//        public void onMsgStateChanged(ChatMessage message, ChatMessage.State state) {
//            View messageView = (View) message.getUserData();
//            switch (state) {
//                case InProgress:
//                    messageView.setBackgroundColor(getResources().getColor(R.color.yellow));
//                    break;
//                case Delivered:
//                    // The proxy server has acknowledged the message with a 200 OK
//                    messageView.setBackgroundColor(getResources().getColor(R.color.orange));
//                    break;
//                case DeliveredToUser:
//                    // User as received it
//                    messageView.setBackgroundColor(getResources().getColor(R.color.blue));
//                    break;
//                case Displayed:
//                    // User as read it (client called chatRoom.markAsRead()
//                    messageView.setBackgroundColor(getResources().getColor(R.color.green));
//                    break;
//                case NotDelivered:
//                    // User might be invalid or not registered
//                    messageView.setBackgroundColor(getResources().getColor(R.color.red));
//                    break;
//                case ChatMessage.State.FileTransferDone:
//                    // We finished uploading/downloading the file
//                    if (!message.isOutgoing) {
//                        ((LinearLayout) findViewById(R.id.messages)).removeView(messageView);
//                        addMessageToHistory(message);
//                    }
//                    break;
//            }
//        }
    };

    public void unregister() {
        // Here we will disable the registration of our Account
        Account account = core.getDefaultAccount();
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

    public void delete() {
        Account account = core.getDefaultAccount();
        if (account == null) {
            return;
        }
        core.removeAccount(account);
        core.clearAccounts();
        core.clearAllAuthInfo();
    }
//
//    private void createBasicChatRoom() {
//        // In this tutorial we will create a Basic chat room
//        // It doesn't include advanced features such as end-to-end encryption or groups
//        // But it is interoperable with any SIP service as it's relying on SIP SIMPLE messages
//        // If you try to enable a feature not supported by the basic backend, isValid() will return false
//        ChatRoomParams params = core.createDefaultChatRoomParams();
//        params.setBackend(ChatRoomBackend.Basic);
////        params.isEncryptionEnabled();
////        params.isGroupEnabled();
//        if (params.isValid()) {
//            // We also need the SIP address of the person we will chat with
//            String remoteSipUri = findViewById(R.id.remote_address).getText().toString();
//            Address remoteAddress = Factory.instance().createAddress(remoteSipUri);
//            if (remoteAddress != null) {
//                // And finally we will need our local SIP address
//                Address localAddress = core.defaultAccount().params().identityAddress();
//                ChatRoom room = core.createChatRoom(params, localAddress, new Address[]{remoteAddress});
//                if (room != null) {
//                    chatRoom = room;
//                    findViewById(R.id.remote_address).setEnabled(false);
//                }
//            }
//        }
//    }
//
//    private void sendMessage() {
//        if (chatRoom == null) {
//            // We need a ChatRoom object to send chat messages in it, so let's create it if it hasn't been done yet
//            createBasicChatRoom();
//        }
//        String message = ((EditText) findViewById(R.id.message)).getText().toString();
//        // We need to create a ChatMessage object using the ChatRoom
//        ChatMessage chatMessage = chatRoom.createMessageFromUtf8(message);
//        // Then we can send it, progress will be notified using the onMsgStateChanged callback
//        chatMessage.addListener(chatMessageListener);
//        addMessageToHistory(chatMessage);
//        // Send the message
//        chatMessage.send();
//        // Clear the message input field
//        ((EditText) findViewById(R.id.message)).getText().clear();
//    }
//
//    private void addMessageToHistory(ChatMessage chatMessage) {
//        // To display a chat message, iterate over its contents list
//        for (Content content : chatMessage.getContents()) {
//            if (content.isText()) {
//                // Content is of type plain/text
//                addTextMessageToHistory(chatMessage, content);
//            } else if (content.isFile()) {
//                // Content represents a file we received and downloaded or a file we sent
//                // Here we assume it's an image
//                if (content.getName() != null) {
//                    String name = content.getName();
//                    if (name.endsWith(".jpeg") || name.endsWith(".jpg") || name.endsWith(".png")) {
//                        addImageMessageToHistory(chatMessage, content);
//                    }
//                }
//            } else if (content.isFileTransfer()) {
//                // Content represents a received file we didn't download yet
//                addDownloadButtonToHistory(chatMessage, content);
//            }
//        }
//    }
//
//    private void addTextMessageToHistory(ChatMessage chatMessage, Content content) {
//        TextView messageView = new TextView(this);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        if (chatMessage.isOutgoing()) {
//            layoutParams.gravity = Gravity.RIGHT;
//            messageView.setBackgroundColor(getColor(R.color.white));
//        } else {
//            layoutParams.gravity = Gravity.LEFT;
//            messageView.setBackgroundColor(getColor(R.color.purple_200));
//        }
//
//        messageView.setLayoutParams(layoutParams);
//
//        // Content is of type plain/text, we can get the text in the content
//        messageView.setText(content.getUtf8Text());
//
//        chatMessage.setUserData(messageView);
//
//        LinearLayout messagesLayout = findViewById(R.id.messages);
//        messagesLayout.addView(messageView);
//
//        ScrollView scrollView = findViewById(R.id.scroll);
//        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//    }


    public void login(String username, String password) {
        String domain = "sip.linphone.org";
        TransportType transportType = TransportType.Tls;

        AuthInfo authInfo = Factory.instance().createAuthInfo(username, null, password, null, null, domain, null);
        AccountParams accountParams = core.createAccountParams();
        Address identity = Factory.instance().createAddress("sip:" + username + "@" + domain);
        accountParams.setIdentityAddress(identity);
        Address address = Factory.instance().createAddress("sip:" + domain);
        if (address != null) {
            address.setTransport(transportType);
        }
        accountParams.setServerAddress(address);
        accountParams.setRegisterEnabled(true);
        Account account = core.createAccount(accountParams);
        core.addAuthInfo(authInfo);
        core.addAccount(account);
        core.setDefaultAccount(account);
        core.addListener(coreListener);
        account.addListener(new AccountListener() {
            @Override
            public void onRegistrationStateChanged(Account account, RegistrationState state, String message) {
                Log.i("yowell","[Account] Registration state changed: " + state + ", " + message);
//                Toast.makeText(MainActivity.this, "[Account] Registration state changed: " + state + ", " + message, Toast.LENGTH_SHORT).show();
            }
        });
        core.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
        delete();

    }
}
