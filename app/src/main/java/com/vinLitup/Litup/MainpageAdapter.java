package com.vinLitup.Litup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainpageAdapter extends FragmentStateAdapter {
    FloatingActionButton FAb;

    public MainpageAdapter(@NonNull FragmentActivity fragmentActivity, FloatingActionButton Fab) {
        super(fragmentActivity);
        FAb=Fab;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MessageFragment(FAb);
            case 1:
                return new StatusFragment(FAb);
            case 2:
                return new VideoFragment(FAb);
            default:
                return new CallsFragment(FAb);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
