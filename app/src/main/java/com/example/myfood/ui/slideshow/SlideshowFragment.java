package com.example.myfood.ui.slideshow;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myfood.MainActivity3;
import com.example.myfood.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    ListView sweets_list;
    ArrayList<String> list3;
    ArrayList<String> list4;
    ArrayAdapter adapter3;
    DatabaseReference databaseReference;
    String load="sweets";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        sweets_list=root.findViewById(R.id.sweets_list);
        list3=new ArrayList<>();
        adapter3=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,list3);
        sweets_list.setAdapter(adapter3);
        SharedPreferences preferences=getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=preferences.getString("My Lang","en");
        if(language.equals("en")){
            load="sweets";
        }
        else if(language.equals("bn")){
            load="sweets_bn";
        }
        else if(language.equals("hi")){
            load="sweets_hi";
        }

        databaseReference= FirebaseDatabase.getInstance().getReference().child(load);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String st=dataSnapshot.getValue(String.class);
                list3.add(st);
                adapter3.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter3.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter3.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        list4=new ArrayList<>();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("sweet_url");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String s=snapshot.getValue(String.class);
                list4.add(s);
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
        sweets_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value=list4.get(position);
                Intent intent=new Intent(getContext(), MainActivity3.class);
                intent.putExtra("url",value);
                startActivity(intent);
            }
        });
    }
}