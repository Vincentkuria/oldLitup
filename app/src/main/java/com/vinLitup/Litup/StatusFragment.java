package com.vinLitup.Litup;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class StatusFragment extends Fragment {
    FloatingActionButton Fab;
Button button;
FirebaseAuth mAuth=FirebaseAuth.getInstance();
TextView mtexty;
    public StatusFragment(FloatingActionButton FAB) {
        // Required empty public constructor
        Fab=FAB;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {


        View v=inflater.inflate(R.layout.fragment_status , container , false);
        button=v.findViewById(R.id.button);
        mtexty=v.findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=mAuth.getCurrentUser().getPhoneNumber();
                mtexty.setText(phone);
                mAuth.signOut();


            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Fab.setImageResource(R.drawable.ic_phofab);
        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AddstatusActivity.class));
            }
        });
    }
}