package com.vinLitup.Litup;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Splash_screen extends AppCompatActivity {
private Runnable runnable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        runnable=new Runnable() {
            @Override
            public void run() {
                boolean areContTaken=false;
                //take contacts if the permission is given and if not get contacts in main activity.
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS ) == PackageManager.PERMISSION_GRANTED){

                    try {
                        getcontacts();
                        areContTaken=true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
                    try {
                        getcontacts();
                        areContTaken=true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Intent GoToMain=new Intent(getApplicationContext(),MainActivity.class);
                GoToMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                GoToMain.putExtra("areconttaken",areContTaken);
                startActivity(GoToMain);

            }
        };

        Thread thread=new Thread(runnable);
        thread.start();

    }




    private void getcontacts() throws InterruptedException {
        HashMap<String,String> recieveContacts=new HashMap<>();

        final Cursor[] cursor = new Cursor[1];
        cursor[0] =getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null, null);
        KeepValues.contacts.clear();


        TelephonyManager telephonyManager=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso=telephonyManager.getSimCountryIso().toUpperCase();
        String internationalNo;

        while (cursor[0].moveToNext()){

            String name= cursor[0].getString(cursor[0].getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone= cursor[0].getString(cursor[0].getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            try {
                String nomalNo= PhoneNumberUtils.normalizeNumber(phone);
                if (!PhoneNumberUtils.isVoiceMailNumber(nomalNo)){
                    internationalNo=PhoneNumberUtils.formatNumberToE164(nomalNo , countryIso);
                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
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
}