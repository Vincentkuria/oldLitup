package com.vinLitup.Litup;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CallsFragment extends Fragment {
    FloatingActionButton Fab;


    public CallsFragment(FloatingActionButton FAB) {
        // Required empty public constructor
        Fab=FAB;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext() , "calls" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_calls , container , false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Fab.setImageResource(R.drawable.ic_callfab);
        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AddcallsActivity.class));
            }
        });
    }
}