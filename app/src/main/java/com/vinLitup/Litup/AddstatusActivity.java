package com.vinLitup.Litup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AddstatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstatus);
        Toast.makeText(this , "Addstatus" , Toast.LENGTH_SHORT).show();
    }
}