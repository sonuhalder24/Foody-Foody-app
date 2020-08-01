package com.example.myfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Signup_fragment extends Fragment {

  AutoCompleteTextView et1,et2,et3,et4;
    Button btn_sign;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_signup_fragment, container, false);

        et1=v.findViewById(R.id.person);
        et2=v.findViewById(R.id.email);
        et3=v.findViewById(R.id.password);
        et4=v.findViewById(R.id.con_password);
        btn_sign=v.findViewById(R.id.button);
        auth=FirebaseAuth.getInstance();
        btn_sign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 try{
                final String mail = et2.getText().toString().trim();
                String pass = et3.getText().toString().trim();
                if (et1.getText().toString().trim().isEmpty() || et2.getText().toString().trim().isEmpty() || et3.getText().toString().trim().isEmpty() || et4.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter all the field", Toast.LENGTH_SHORT).show();

                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(et2.getText().toString().trim()).matches()) {
                        Toast.makeText(getContext(), "Please Enter a valid email", Toast.LENGTH_SHORT).show();
                    } else {
                        if (pass.length() < 6) {
                            Toast.makeText(getContext(), "Password should Contain At least 6 charaacter", Toast.LENGTH_SHORT).show();
                            et3.setText(null);
                            et4.setText(null);
                        } else {
                            if (pass.equals(et4.getText().toString().trim()) == false) {
                                Toast.makeText(getContext(), "Please Confirm the Password Correctly", Toast.LENGTH_SHORT).show();
                                et3.setText(null);
                                et4.setText(null);
                            } else {
                                final ProgressDialog dialog = new ProgressDialog(getContext());
                                dialog.setMessage("Processing");
                                dialog.show();
                                auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
                                            String s1 = et1.getText().toString().trim();
                                            int val=0;
                                            for(int i=0;i<mail.length();i++){
                                                int check=mail.charAt(i);
                                                if((check>=32&&check<=47)||(check>=58&&check<=64)){
                                                    break;
                                                }
                                                else{
                                                    val++;
                                                }
                                            }
                                            SharedPreferences preferences = getActivity().getSharedPreferences("Email", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("mail", mail);
                                            editor.commit();

                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(mail.substring(0, val));
                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("name", s1);
                                            map.put("Email", mail);
                                            map.put("imageUrl", "");
                                            ref.setValue(map);

                                            Intent intent = new Intent(getContext(), MainActivity2.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Process Failure", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }

                    }
                }
            }
                catch(Exception e){
                    Toast.makeText(getContext(), "Error occur", Toast.LENGTH_SHORT).show();
                }

            }
        });




        return v;

    }
}