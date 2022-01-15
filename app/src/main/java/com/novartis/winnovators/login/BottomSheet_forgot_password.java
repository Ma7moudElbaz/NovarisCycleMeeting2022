package com.novartis.winnovators.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.novariscyclemeeting2022.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet_forgot_password extends BottomSheetDialogFragment {

    Context mContext;

    public BottomSheet_forgot_password(Context mContext) {
        this.mContext = mContext;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_forgot_password, container, false);
    }

    EditText email;
    ImageView closeButton;
    Button submitButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.email);
        submitButton = view.findViewById(R.id.btn_submit);
        closeButton = view.findViewById(R.id.img_close);



        closeButton.setOnClickListener(v -> dismiss());
        submitButton.setOnClickListener(v -> {
            if (email.length()==0){
                Toast.makeText(mContext, "please, fill all fields", Toast.LENGTH_SHORT).show();
            }else {
                sendBackResult(email.getText().toString());
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface EmailSubmitListener {
        void emailSubmitListener(String emailAddress);
    }

    public void sendBackResult(String emailAddress) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        EmailSubmitListener listener = (EmailSubmitListener) getActivity();
        listener.emailSubmitListener(emailAddress);
        dismiss();
    }
}

