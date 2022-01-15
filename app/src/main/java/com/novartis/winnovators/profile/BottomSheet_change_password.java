package com.novartis.winnovators.profile;

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

public class BottomSheet_change_password extends BottomSheetDialogFragment {

    Context mContext;

    public BottomSheet_change_password(Context mContext) {
        this.mContext = mContext;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_change_password, container, false);
    }

    EditText current_pass,new_pass;
    ImageView closeButton;
    Button submitButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        current_pass = view.findViewById(R.id.curren_pass);
        new_pass = view.findViewById(R.id.new_pass);
        submitButton = view.findViewById(R.id.btn_submit);
        closeButton = view.findViewById(R.id.img_close);



        closeButton.setOnClickListener(v -> dismiss());
        submitButton.setOnClickListener(v -> {
            if (current_pass.length()==0||new_pass.length()==0){
                Toast.makeText(mContext, "please, fill all fields", Toast.LENGTH_SHORT).show();
            }else {
                sendBackResult(current_pass.getText().toString(),new_pass.getText().toString());
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

    public interface PasswordSubmitListener {
        void passwordSubmitListener(String currentPass, String newPass);
    }

    public void sendBackResult(String currentPass,String newPass) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        PasswordSubmitListener listener = (PasswordSubmitListener) getActivity();
        listener.passwordSubmitListener(currentPass,newPass);
        dismiss();
    }
}

