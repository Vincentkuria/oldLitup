package com.vinLitup.Litup;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MessageFragment extends Fragment {
    FloatingActionButton Fab;
    public MessageFragment(FloatingActionButton FAB) {
        Fab=FAB;
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_message , container , false);
    }


    @Override
    public void onResume() {
        super.onResume();

        Fab.setImageResource(R.drawable.ic_comfab);
        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GotoAddmessage=new Intent(getContext(),AddmessageActivity.class);
                startActivity(GotoAddmessage);

            }
        });

    }

}
