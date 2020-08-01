package com.example.myfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Button btn_sign,btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager=getSupportFragmentManager();
        btn_login=findViewById(R.id.login1);
       btn_sign=findViewById(R.id.Signup1);

        fragmentManager.beginTransaction().replace(R.id.container1,new Login_fragment()).commit();
        btn_sign.setBackground(getDrawable(R.drawable.basic_button));
        btn_login.setBackground(getDrawable(R.drawable.login_btn));
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.container1,new Login_fragment()).commit();
                btn_login.setBackground(getDrawable(R.drawable.login_btn));
                btn_sign.setBackground(getDrawable(R.drawable.basic_button));
            }
        });

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.container1,new Signup_fragment()).commit();
                btn_sign.setBackground(getDrawable(R.drawable.signup_btn));
                btn_login.setBackground(getDrawable(R.drawable.basic_button));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
            finish();
        }
    }
}