package com.vinLitup.Litup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;



public class AddmessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    RecyclerView recyclerView;
    AddmessRecyAdapter addmessRecyAdapter;
    Context mcontext=AddmessageActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmessage);

        recyclerView=findViewById(R.id.recycler_addmess);
        toolbar=findViewById(R.id.toolbar_addmess);
        toolbar.setTitle("Select contact");
        toolbar.setSubtitle(KeepValues.contacts.size()-1+" contacts");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addmessRecyAdapter=new AddmessRecyAdapter(mcontext);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(addmessRecyAdapter);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addmesssearch,menu);
        MenuItem item=menu.findItem(R.id.search_addmess);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    addmessRecyAdapter.searchFilter(newText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}