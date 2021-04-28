package com.vinLitup.Litup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class SettingsActivity extends AppCompatActivity {


    private de.hdodenhof.circleimageview.CircleImageView mProfile_image;
    private Button mFinishsetup;
    private ProgressBar mProfile_imageLoading;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference profileImageFolder;
    private TextView usernamDisplay,bioDisplay;
    private Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        profileImageFolder=storage.getReference().child("PROFILE IMAGES").child(mAuth.getCurrentUser().getPhoneNumber());
        usernamDisplay=findViewById(R.id.usernamedisplay);
        bioDisplay=findViewById(R.id.biodisplay);
        mProfile_image=findViewById(R.id.profile_image);
        mProfile_imageLoading=findViewById(R.id.profile_imageLoading);
        mProfile_imageLoading.setVisibility(View.INVISIBLE);
        mFinishsetup=findViewById(R.id.Finishsetup);
        Retreavedata();

        dialog=new Dialog(SettingsActivity.this);
        dialog.setContentView(R.layout.editdialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.login_btn));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_animation;

        final TextView minputnote=dialog.findViewById(R.id.inputnote);
        final TextView mtextcount=dialog.findViewById(R.id.textcount);
        final TextView mtotaltext=dialog.findViewById(R.id.totaltext);
        final EditText mtextinput=dialog.findViewById(R.id.textinput);
        final Button mcancelbtn=dialog.findViewById(R.id.cancelbtn);
        final Button mokbtn=dialog.findViewById(R.id.okbtn);


        mFinishsetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.child("USERS").child(mAuth.getCurrentUser().getPhoneNumber()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.hasChild("UserName")){
                            Intent GotoMainActivity=new Intent(getApplicationContext(),MainActivity.class);
                            GotoMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(GotoMainActivity);
                        }else{
                            Toast.makeText(SettingsActivity.this , "Please enter UserName is required" , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        mProfile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });

        usernamDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minputnote.setText(R.string.enter_username);
                mtotaltext.setText("/25");
                mtextinput.setMaxLines(1);
                mtextinput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
                mtextinput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s , int start , int count , int after) {
                        String characters=mtextinput.getText().toString();
                            mtextcount.setText(""+characters.length());
                    }

                    @Override
                    public void onTextChanged(CharSequence s , int start , int before , int count) {
                        String characters=mtextinput.getText().toString();
                        mtextcount.setText(""+characters.length());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                mokbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String textvalue=mtextinput.getText().toString();
                        if (TextUtils.isEmpty(textvalue)){
                            Toast.makeText(SettingsActivity.this , "please enter Value the empty field is required" , Toast.LENGTH_SHORT).show();
                        }else {
                            mDatabaseReference.child("USERS").child(mAuth.getCurrentUser().getPhoneNumber()).child("UserName").setValue(textvalue);
                            usernamDisplay.setText(textvalue);
                            mtextinput.setText("");
                            dialog.dismiss();
                        }
                    }
                });
                mcancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mtextinput.setText("");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        bioDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minputnote.setText("Please Enter your bio");
                mtotaltext.setText("/100");
                mtextinput.setMaxLines(1);
                mtextinput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
                mtextinput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s , int start , int count , int after) {
                        String characters=mtextinput.getText().toString();
                        mtextcount.setText(""+characters.length());
                    }

                    @Override
                    public void onTextChanged(CharSequence s , int start , int before , int count) {
                        String characters=mtextinput.getText().toString();
                        mtextcount.setText(""+characters.length());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                mokbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String textvalue=mtextinput.getText().toString();
                        if (TextUtils.isEmpty(textvalue)){
                            Toast.makeText(SettingsActivity.this , "please enter Value the empty field is required" , Toast.LENGTH_SHORT).show();
                        }else {
                            mDatabaseReference.child("USERS").child(mAuth.getCurrentUser().getPhoneNumber()).child("Bio").setValue(textvalue);
                            bioDisplay.setText(textvalue);
                            mtextinput.setText("");
                            dialog.dismiss();
                        }
                    }
                });
                mcancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mtextinput.setText("");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

   private void Retreavedata() {


        mDatabaseReference.child("USERS").child(mAuth.getCurrentUser().getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() & dataSnapshot.hasChild("UserName") & dataSnapshot.hasChild("Bio") & dataSnapshot.hasChild("Image")){
                    String retrieveUsername=dataSnapshot.child("UserName").getValue().toString();
                    String retrieveBio=dataSnapshot.child("Bio").getValue().toString();
                    String retrieveImage=dataSnapshot.child("Image").getValue().toString();
                    usernamDisplay.setText(retrieveUsername);
                    bioDisplay.setText(retrieveBio);
                    Glide.with(SettingsActivity.this).load(retrieveImage).placeholder(R.drawable.ic_baseline_account_circle_24).into(mProfile_image);
                }else if (dataSnapshot.exists() & dataSnapshot.hasChild("UserName") & dataSnapshot.hasChild("Bio")){
                    String retrieveUsername=dataSnapshot.child("UserName").getValue().toString();
                    String retrieveBio=dataSnapshot.child("Bio").getValue().toString();
                    usernamDisplay.setText(retrieveUsername);
                    bioDisplay.setText(retrieveBio);
                }else if (dataSnapshot.exists() & dataSnapshot.hasChild("UserName") & dataSnapshot.hasChild("Image")){
                    String retrieveUsername=dataSnapshot.child("UserName").getValue().toString();
                    String retrieveImage=dataSnapshot.child("Image").getValue().toString();
                    usernamDisplay.setText(retrieveUsername);
                    Glide.with(SettingsActivity.this).load(retrieveImage).placeholder(R.drawable.ic_baseline_account_circle_24).into(mProfile_image);
                }else if (dataSnapshot.exists() & dataSnapshot.hasChild("UserName")){
                    String retrieveUsername=dataSnapshot.child("UserName").getValue().toString();
                    usernamDisplay.setText(retrieveUsername);
                }else if (dataSnapshot.exists() & dataSnapshot.hasChild("Image")){
                    String retrieveImage=dataSnapshot.child("Image").getValue().toString();
                    Glide.with(SettingsActivity.this).load(retrieveImage).placeholder(R.drawable.ic_baseline_account_circle_24).into(mProfile_image);
                }else if (dataSnapshot.exists()&dataSnapshot.hasChild("Bio")){
                    String retrieveBio=dataSnapshot.child("Bio").getValue().toString();
                    bioDisplay.setText(retrieveBio);
                }else{

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mProfile_imageLoading.setVisibility(View.INVISIBLE);

    }


    @Override
    protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mProfile_imageLoading.setVisibility(View.VISIBLE);
                profileImageFolder.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            mProfile_imageLoading.setVisibility(View.INVISIBLE);
                            profileImageFolder.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String downloadUrl=task.getResult().toString();
                                    mDatabaseReference.child("USERS").child(mAuth.getCurrentUser().getPhoneNumber()).child("Image").setValue(downloadUrl);
                                }
                            });

                        }else{
                            mProfile_imageLoading.setVisibility(View.INVISIBLE);
                            Toast.makeText(SettingsActivity.this , task.getException().toString() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mProfile_image.setImageURI(resultUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this , error.toString() , Toast.LENGTH_SHORT).show();
            }
        }
    }


}