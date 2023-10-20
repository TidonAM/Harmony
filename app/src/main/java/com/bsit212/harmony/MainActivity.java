package com.bsit212.harmony;

import static com.bsit212.harmony.background.core;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import org.linphone.core.ChatRoom;
import org.linphone.core.ChatMessage;
import org.linphone.core.ChatRoomCapabilities;
import com.bsit212.harmony.background.*;
import com.bsit212.harmony.Home;

import com.google.android.material.textfield.TextInputLayout;

import org.linphone.core.*;

//import org.linphone.core.tools.Log;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Core core;
    public Factory factory;
    private ChatRoom chatRoom;
    public background bg = new background();

    public LinearLayout linearLayout;
    public boolean registration = false;
    public EditText etUsername;
    public EditText etPassword;

    public TextInputLayout tilPassword;
    public TextView tvForgot;
    public TextView tvRegister;
    public String stringEtUsername;
    public String stringEtPassword;
    public Button btnLogin;
    public ImageButton ibShowPw;
    public boolean isHidden;
    public boolean isIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //liblinphone Core initialization//
        factory = Factory.instance();
        core = factory.createCore(null, null, this);



        isHidden = true;
        linearLayout = findViewById(R.id.login_layout_main);
        etUsername = findViewById(R.id.login_et_username);
        etPassword = findViewById(R.id.login_et_password);
        tilPassword = findViewById(R.id.login_til_password);
        tvForgot = findViewById(R.id.login_tv_forget);
        tvRegister = findViewById(R.id.login_tv_register);
        btnLogin = findViewById(R.id.login_btn_login);
        etUsername.addTextChangedListener(login_textWatcher);
        etPassword.addTextChangedListener(login_textWatcher);
        stringEtPassword = etPassword.getText().toString();
        stringEtUsername = etUsername.getText().toString();



        Button btnpeople1 = findViewById(R.id.btn_people1);
        Button btnpeople2 = findViewById(R.id.btn_people2);
        Button btnpeople3 = findViewById(R.id.btn_people3);
        core.getAccountList().toString();
        btnpeople1.setOnClickListener(this);
        btnpeople2.setOnClickListener(this);
        btnpeople3.setOnClickListener(this);


//        ibShowPw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isHidden == false) {
//                    ibShowPw.setBackgroundResource(R.drawable.dr_icon_eyeol);
////                    etPassword.setInputType(InputType.T);
//                    isHidden = true;
//                } else {
//                    ibShowPw.setBackgroundResource(R.drawable.dr_icon_eyefill);
//                    isHidden = false;
//                }
//            }
//        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    login();
                } catch(Exception e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("yowell",e.toString());
                }
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://subscribe.linphone.org/login/email"));
                startActivity(browserIntent);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://subscribe.linphone.org/register/email"));
                startActivity(browserIntent);
//                bg.delete();
            }
        });

    }

    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, Message.class);
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
        MainActivity.this.startActivity(intent);
    }

    private CoreListener coreListener = new CoreListenerStub() {
        @Override
        public void onAccountRegistrationStateChanged(Core core, Account account, RegistrationState state, String message) {
            btnLogin.setText("Logging in. This will take a while.");
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this,R.color.Green));
            etPassword.setEnabled(false);
            etUsername.setEnabled(false);
            tilPassword.setEnabled(false);
            if (state == RegistrationState.Failed) {
                registration = false;
//                Toast.makeText(MainActivity.this, "Failed or Cleared", Toast.LENGTH_SHORT).show();
                btnLogin.setText("Incorrect Credentials! Try Again");
                btnLogin.setEnabled(false);
                etPassword.setEnabled(true);
                etUsername.setEnabled(true);
                tilPassword.setEnabled(true);
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this,R.color.Red));
            } else if (state == RegistrationState.Cleared) {
                btnLogin.setEnabled(true);
                etPassword.setEnabled(true);
                etUsername.setEnabled(true);
                btnLogin.setText("Logged Out Successfully");
                registration = false;
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this,R.color.BlueCornflower));
            } else if (state == RegistrationState.Ok && registration == false) {
                registration = true;
                btnLogin.setEnabled(true);
                etPassword.setEnabled(true);
                etUsername.setEnabled(true);
                tilPassword.setEnabled(true);
//                Toast.makeText(MainActivity.this, "Connect", Toast.LENGTH_SHORT).show();
                btnLogin.setText("Log in");
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this,R.color.BlueCornflower));
//                Intent intent = new Intent(MainActivity.this, Home.class);
//                intent.putExtra("username", etUsername.getText().toString());
//                MainActivity.this.startActivity(intent);
                LinearLayout LL_home = findViewById(R.id.home_layout);
                LinearLayout LL_login = findViewById(R.id.login_layout_main);
                TextView tvUsername = findViewById(R.id.home_tv_username);
                ImageButton imLogout = findViewById(R.id.home_ib_logout);
                if (registration == true) {
                    imLogout.setEnabled(true);
                } else {
                    imLogout.setEnabled(false);
                }
                imLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (registration==true){
                            background.unregister(core);
                            LL_login.setVisibility(View.VISIBLE);
                            LL_home.setVisibility(View.GONE);
                            btnLogin.setEnabled(false);
                            btnLogin.setText("Logging Out");
                            btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this,R.color.Gray));

                        }
                    }
                });
                if (stringEtUsername == null) {
                    tvUsername.setText("");
                } else {
                    tvUsername.setText(stringEtUsername);
                    Log.i("yowell",core.getAccountList().toString());
                }

                LL_home.setVisibility(View.VISIBLE);
                LL_login.setVisibility(View.GONE);
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


    private void login() {
        EditText usernameEditText = findViewById(R.id.login_et_username);
        String username = usernameEditText.getText().toString();
        EditText passwordEditText = findViewById(R.id.login_et_password);
        String password = passwordEditText.getText().toString();
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

    private final TextWatcher login_textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            stringEtPassword = etPassword.getText().toString();
            stringEtUsername = etUsername.getText().toString();
            if (stringEtPassword.length() < 2 || stringEtUsername.length() < 2) {
                btnLogin.setText("Log in");
                btnLogin.setEnabled(false);
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this,R.color.Gray));
            } else {
                btnLogin.setText("Log in");
                btnLogin.setEnabled(true);
                btnLogin.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this,R.color.BlueCornflower));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        background.unregister(core);
        background.delete(core);
    }
}
