package com.vinLitup.Litup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AddcallsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcalls);
        Toast.makeText(this , "Addcalls" , Toast.LENGTH_SHORT).show();
    }
}