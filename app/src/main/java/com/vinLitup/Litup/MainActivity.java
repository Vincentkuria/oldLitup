package com.vinLitup.Litup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseReference= FirebaseDatabase.getInstance().getReference();
    private Toolbar toolbar;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private FloatingActionButton Fab;
    private HashMap<String,String> recieveContacts=new HashMap<>();
    private boolean areContTaken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        viewPager2=findViewById(R.id.view_pager);
        tabLayout=findViewById(R.id.tabs);
        setSupportActionBar(toolbar);
        Fab=findViewById(R.id.fAB);
        Intent GoToMain=getIntent();
        areContTaken=GoToMain.getBooleanExtra("areconttaken",false);

        viewPager2.setAdapter(new MainpageAdapter(this,Fab));

        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tabLayout , viewPager2 , new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab , int position) {
                switch (position){
                    case 0:
                        tab.setIcon(R.drawable.ic_private_messaging);
                        break;
                    case 1:
                        tab.setText("STATUS");
                        break;
                    case 2:
                        tab.setText("VIDEO");
                        break;
                    case 3:
                        tab.setIcon(R.drawable.ic_call);

                }

            }
        });
        tabLayoutMediator.attach();
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser==null){
            Intent GotoLogin=new Intent(getApplicationContext(),LoginActivity.class);
            GotoLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(GotoLogin);
        }else{
            //To determine if the user have set username
            String phoneNo=currentUser.getPhoneNumber();
            mDatabaseReference.child("USERS").child(phoneNo).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("UserName")){

                    }else{
                        Intent GotoSettings=new Intent(MainActivity.this,SettingsActivity.class);
                        GotoSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(GotoSettings);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (!areContTaken){

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},3);

            }else {
                try {
                    getcontacts();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

        }


    }




    private void getcontacts() throws InterruptedException {

        final Cursor[] cursor = new Cursor[1];
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                cursor[0] =getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null, null);
                KeepValues.contacts.clear();
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();


        TelephonyManager telephonyManager=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso=telephonyManager.getSimCountryIso().toUpperCase();
        String internationalNo;

        thread.join();
        while (cursor[0].moveToNext()){

            String name= cursor[0].getString(cursor[0].getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone= cursor[0].getString(cursor[0].getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            try {
                String nomalNo=PhoneNumberUtils.normalizeNumber(phone);
                if (!PhoneNumberUtils.isVoiceMailNumber(nomalNo)){
                    internationalNo=PhoneNumberUtils.formatNumberToE164(nomalNo , countryIso);
                    if (!(internationalNo.equals(mAuth.getCurrentUser().getPhoneNumber()))){
                        recieveContacts.put(internationalNo,name);
                    }

                }

            }catch (Exception e){

            }

        }

        cursor[0].close();
        for (Map.Entry<String,String> entry:recieveContacts.entrySet()){

            final StoreContacts values=new StoreContacts(entry.getValue(),entry.getKey());
            KeepValues.contacts.add(values);

        }
        //sort to alphabetical order
        Collections.sort(KeepValues.contacts);
        //contacts.add(0,new StoreContacts("CREATE","GROUP"));
        recieveContacts.clear();


    }



    @Override
    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode , permissions , grantResults);

        if (requestCode==3){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                try {
                    getcontacts();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
