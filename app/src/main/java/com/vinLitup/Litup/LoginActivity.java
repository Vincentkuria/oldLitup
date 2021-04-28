package com.vinLitup.Litup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {
    static CountryCodePicker mCPP;
    static EditText meditTextPhone;
    private Button mloginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialize widgets
        mCPP=findViewById(R.id.CPP);
        meditTextPhone=findViewById(R.id.editTextPhone);
        mloginbtn=findViewById(R.id.loginbtn);

        mCPP.registerCarrierNumberEditText(meditTextPhone);
        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onlyphonenNo=meditTextPhone.getText().toString().trim();
                if (TextUtils.isEmpty(onlyphonenNo)){
                    Toast.makeText(LoginActivity.this, "PLEASE ENTER PHONE NUMBER", Toast.LENGTH_SHORT).show();
                }else {
                    String fullphoneNo=mCPP.getFullNumberWithPlus().replace(" ","");
                    Intent GotoVerification=new Intent(getApplicationContext(),VerificationActivity.class);
                    GotoVerification.putExtra("number",fullphoneNo);
                    GotoVerification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(GotoVerification);
                }
            }
        });
    }
}