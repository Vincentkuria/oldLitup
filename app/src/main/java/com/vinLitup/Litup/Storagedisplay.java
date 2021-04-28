package com.vinLitup.Litup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;


public class Storagedisplay extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<File> files=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storagedisplay);
        String fileType=getIntent().getStringExtra("fileType");
        recyclerView=findViewById(R.id.files_recyclerView);
        Toolbar toolbar=findViewById(R.id.toolbar_stordisp);
        toolbar.setTitle("Choose "+fileType.toLowerCase());
/******this contains storage path in the drive or file manager ****/
        String[] allPath=StorageUtil.getStorageDirectories(this);


        for (String path:allPath){
            File storage=new File(path);
            files.addAll(Filefinder.loadFile(storage,fileType));
        }


        StordispRecyAdapter stordispRecyAdapter=new StordispRecyAdapter(fileType,files,getApplicationContext());

        if (fileType.equals("VIDEO")||fileType.equals("PHOTO")){

            recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        }else {

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

        }

        recyclerView.setAdapter(stordispRecyAdapter);
    }
}