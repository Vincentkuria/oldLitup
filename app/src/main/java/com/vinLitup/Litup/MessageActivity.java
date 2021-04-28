package com.vinLitup.Litup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class MessageActivity extends AppCompatActivity {
    ImageView memoji_btn;
    EmojiconEditText msuper_editTxt;
    EmojIconActions emojIconActions;
    View rootView;
    ImageButton mattach_mess;
    FloatingActionButton msend_voice;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        memoji_btn=findViewById(R.id.emoji_btn);
        msuper_editTxt=findViewById(R.id.super_editTxt);
        rootView=findViewById(R.id.root_mess);
        mattach_mess=findViewById(R.id.attach_mess);
        msend_voice=findViewById(R.id.send_voice);
        emojIconActions=new EmojIconActions(MessageActivity.this,rootView,msuper_editTxt,memoji_btn,"#F8F80D","#000000","#3E3E3E");
        emojIconActions.ShowEmojIcon();
        msuper_editTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s , int start , int count , int after) {

            }

            @Override
            public void onTextChanged(CharSequence s , int start , int before , int count) {
                String txt=msuper_editTxt.getText().toString();
                if (!TextUtils.isEmpty(txt)){
                    msend_voice.setImageResource(R.drawable.ic_send);
                }else {
                    msend_voice.setImageResource(R.drawable.ic_voice_note);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog=new Dialog(MessageActivity.this);
        dialog.setContentView(R.layout.storagedialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_animation;

        CircleImageView document,photo,audio,video,contact;
        document=dialog.findViewById(R.id.document_dialog);
        photo=dialog.findViewById(R.id.photo_dialog);
        contact=dialog.findViewById(R.id.contact_dialog);
        audio=dialog.findViewById(R.id.audio_dialog);
        video=dialog.findViewById(R.id.video_dialog);

        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                goTostorageDisplay("DOCUMENT");
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                goTostorageDisplay("PHOTO");
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                goTostorageDisplay("CONTACT");
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                goTostorageDisplay("AUDIO");
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                goTostorageDisplay("VIDEO");
            }
        });

        Window window=dialog.getWindow();
        WindowManager.LayoutParams wlp=window.getAttributes();
        wlp.gravity=Gravity.BOTTOM;
        window.setAttributes(wlp);

        mattach_mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        Log.i(getIntent().getStringExtra("name"),getIntent().getStringExtra("phoneNo"));

    }

    private void goTostorageDisplay(String fileType) {
        Intent GotoStorageDisplay=new Intent(getApplicationContext(),Storagedisplay.class);
        GotoStorageDisplay.putExtra("fileType",fileType);
        startActivity(GotoStorageDisplay);
    }


}