package com.example.myfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login_fragment extends Fragment {

    AutoCompleteTextView et1,et2;
    FirebaseAuth auth;
    Button log_btn;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_login_fragment, container, false);

        et1=v.findViewById(R.id.email_log);
        et2=v.findViewById(R.id.password_log);
       log_btn=v.findViewById(R.id.log_btn);
        auth=FirebaseAuth.getInstance();
        preferences=getActivity().getSharedPreferences("Email", Context.MODE_PRIVATE);
        editor=preferences.edit();
        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             final String mail=et1.getText().toString().trim();
                String pass=et2.getText().toString().trim();
                if(mail.isEmpty()||pass.isEmpty()){
                    Toast.makeText(getContext(), "Please enter all the field", Toast.LENGTH_SHORT).show();
                }
                else {
                    final ProgressDialog dialog=new ProgressDialog(getContext());
                    dialog.setMessage("Processing");
                    dialog.show();
                    auth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                dialog.dismiss();
                                editor.putString("mail",mail);
                                editor.commit();
                                Intent intent =new Intent(getContext(),MainActivity2.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Please input a valid data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }


        });

        return v;
    }
}