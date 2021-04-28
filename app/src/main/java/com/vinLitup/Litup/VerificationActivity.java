package com.vinLitup.Litup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    private TextView mtextnote;
    private EditText meditTextNumber;
    private Button mverifybtn;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        //initialize
        mtextnote = findViewById(R.id.textnote);
        meditTextNumber = findViewById(R.id.editTextNumber);
        mverifybtn = findViewById(R.id.verifybtn);
        mProgressBar = findViewById(R.id.progressBar);

        mProgressBar.setVisibility(View.INVISIBLE);
        Intent GotoVerification = getIntent();
        final String phoneNumber = GotoVerification.getStringExtra("number");

        verify(phoneNumber);
    }



    private void verify(String phoneNumber) {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mProgressBar.setVisibility(View.INVISIBLE);

                if (e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(getApplicationContext(),"Invalid Phone Number",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
                GotoLogin();

            }

            @Override
            public void onCodeSent(final String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                mverifybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code= String.valueOf(meditTextNumber.getText());
                        if (TextUtils.isEmpty(code)){
                            Toast.makeText(VerificationActivity.this , "please enter verification code" , Toast.LENGTH_SHORT).show();
                        }else{
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s, code);
                            signInWithPhoneAuthCredential(credential);
                        }

                    }
                });


            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber ,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks



    }

    private void GotoLogin() {
        Intent GobacktoLogin =new Intent(getApplicationContext(),LoginActivity.class);
        GobacktoLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(GobacktoLogin);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mProgressBar.setVisibility(View.VISIBLE);
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final DatabaseReference mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("USERS");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists() && dataSnapshot.hasChild(mAuth.getCurrentUser().getPhoneNumber())){
                                        Intent GotoMainActivity=new Intent(getApplicationContext(),MainActivity.class);
                                        GotoMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(GotoMainActivity);

                                    }else {
                                        Intent GotoSettings=new Intent(VerificationActivity.this,SettingsActivity.class);
                                        GotoSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(GotoSettings);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            //Intent GotoSettings=new Intent(VerificationActivity.this,SettingsActivity.class);
                            //GotoSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //startActivity(GotoSettings);
                        } else {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(VerificationActivity.this , "Failed to Login Please RETRY" , Toast.LENGTH_SHORT).show();
                            GotoLogin();
                        }
                    }
                });
    }



}




