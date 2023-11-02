package com.bsit212.harmony;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class addContactsDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_addcontact, container, false);

        EditText emailEditText = rootView.findViewById(R.id.et_email);
//        Button addButton = rootView.findViewById(R.id.btn_add);
//        Button cancelButton = rootView.findViewById(R.id.btn_cancel);
//
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Retrieve the email input
//                String email = emailEditText.getText().toString();
//
//                // Do something with the email (e.g., add it to a list)
//
//                // Dismiss the dialog
//                dismiss();
//            }
//        });
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Cancel button click action
//                dismiss();
//            }
//        });

        return rootView;
    }
}
