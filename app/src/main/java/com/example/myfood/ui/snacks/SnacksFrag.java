package com.example.myfood.ui.snacks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfood.MainActivity3;
import com.example.myfood.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SnacksFrag extends Fragment {
    ListView snacks_list;
    ArrayList<String> list4;
    ArrayList<String>list3;
    ArrayAdapter adapter4;
    DatabaseReference databaseReference;
    String load="snacks";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_snacks,container,false);
        snacks_list=root.findViewById(R.id.snacks_list);
        list4=new ArrayList<>();
        adapter4=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,list4);
        snacks_list.setAdapter(adapter4);

        SharedPreferences preferences=getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=preferences.getString("My Lang","en");
        if(language.equals("en")){
            load="snacks";
        }
        else if(language.equals("bn")){
            load="snacks_bn";
        }
        else if(language.equals("hi")){
            load="snacks_hi";
        }

        databaseReference= FirebaseDatabase.getInstance().getReference().child(load);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String st=dataSnapshot.getValue(String.class);
                list4.add(st);
                adapter4.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter4.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter4.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter4.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        list3=new ArrayList<>();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("snacks_url");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String s=snapshot.getValue(String.class);
                list3.add(s);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        snacks_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value=list3.get(position);
                Intent intent=new Intent(getContext(), MainActivity3.class);
                intent.putExtra("url",value);
                startActivity(intent);
            }
        });
    }
}
