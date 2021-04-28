package com.vinLitup.Litup;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddmessRecyAdapter extends RecyclerView.Adapter {

    Context mcontext;
    ArrayList<StoreContacts> contacts=new ArrayList<>(KeepValues.contacts);
    ArrayList<StoreContacts> allContacts;
    ArrayList<String> members=new ArrayList<>();
    int a=0;
    private final DatabaseReference mDatabaseref= FirebaseDatabase.getInstance().getReference().child("USERS");

    public AddmessRecyAdapter(Context mcontext) {
        this.mcontext=mcontext;
        this.allContacts=new ArrayList<>(contacts);
    }

    @Override
    public int getItemViewType(int position) {

        if (contacts.get(position).getName().equals("CREATE") && contacts.get(position).getPhoneNo().equals("GROUP")){
            return 0;
        }else if (members.contains(contacts.get(position).getPhoneNo())){
            return 1;
        }else {
            return 2;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        if (a==0){
            //this is to restrict the method to search for available members only ones
            a++;
            mDatabaseref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (int i=0;i<=contacts.size()-1;i++){

                        if (dataSnapshot.hasChild(contacts.get(i).getPhoneNo())){
                            members.add(contacts.get(i).getPhoneNo());
                        }

                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(mcontext , databaseError.getMessage() , Toast.LENGTH_SHORT).show();
                }
            });

        }
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view;
        if (viewType==0){
            view=layoutInflater.inflate(R.layout.recycler_create_group,parent,false);
            return new CreategroupHolder(view);
        }else if (viewType==1){
            view=layoutInflater.inflate(R.layout.recycler_member,parent,false);
            return new MemberHolder(view);
        }else {
            view=layoutInflater.inflate(R.layout.recycler_notmember,parent,false);
            return new NotmemberHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder , final int position) {
        if (contacts.get(position).getName().equals("CREATE") && contacts.get(position).getPhoneNo().equals("GROUP")){


        }else if (members.contains(contacts.get(position).getPhoneNo())){
            StoreContacts user=contacts.get(position);
            final String name=user.getName();
            String phoneNo=user.getPhoneNo();
            final MemberHolder memberHolder=(MemberHolder) holder;
            memberHolder.mmember_name.setText(name);
            mDatabaseref.child(phoneNo).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.hasChild("Bio") && dataSnapshot.hasChild("Image")){
                        String retrieveBio=dataSnapshot.child("Bio").getValue().toString();
                        String retrieveImage=dataSnapshot.child("Image").getValue().toString();

                        memberHolder.mmember_bio.setText(retrieveBio);
                        Glide.with(mcontext).load(retrieveImage).placeholder(R.drawable.ic_baseline_account_circle_24).into(memberHolder.mmember_img);
                    }else if (dataSnapshot.exists() && dataSnapshot.hasChild("Bio")){
                        String retrieveBio=dataSnapshot.child("Bio").getValue().toString();
                        memberHolder.mmember_bio.setText(retrieveBio);
                        memberHolder.mmember_img.setImageResource(R.drawable.ic_baseline_account_circle_24);
                    }else if (dataSnapshot.exists() && dataSnapshot.hasChild("Image")){
                        String retrieveBio="I am on Litup lets chat";
                        String retrieveImage=dataSnapshot.child("Image").getValue().toString();
                        Glide.with(mcontext).load(retrieveImage).placeholder(R.drawable.ic_baseline_account_circle_24).into(memberHolder.mmember_img);
                        memberHolder.mmember_bio.setText(retrieveBio);
                    }else{
                        String retrieveBio="I am on Litup lets chat";
                        memberHolder.mmember_bio.setText(retrieveBio);
                        memberHolder.mmember_img.setImageResource(R.drawable.ic_baseline_account_circle_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            NotmemberHolder notmemberHolder=(NotmemberHolder) holder;
            notmemberHolder.mnotmember_name.setText(contacts.get(position).getName());
            notmemberHolder.mnotmember_bio.setText("Invite "+contacts.get(position).getName()+" on Litup");

        }

    }

    @Override
    public int getItemCount() {

        return contacts.size();
    }


    public void searchFilter(final String searchedTxt) throws InterruptedException {
        final ArrayList<StoreContacts> filteredlist=new ArrayList<>();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if (searchedTxt.isEmpty()){
                    filteredlist.addAll(allContacts);
                }else{
                    for(int i=0;i <=allContacts.size()-1;i++){
                        if (allContacts.get(i).getName().equals("CREATE") && allContacts.get(i).getPhoneNo().equals("GROUP")){

                        }
                        else if(allContacts.get(i).getName().trim().toLowerCase().contains(searchedTxt.trim().toLowerCase())){
                            filteredlist.add(allContacts.get(i));
                        }
                    }
                }
            }
        };

        Thread serching=new Thread(runnable);
        serching.start();
        serching.join();
        contacts.clear();
        contacts.addAll(filteredlist);
        notifyDataSetChanged();


    }

    class CreategroupHolder extends RecyclerView.ViewHolder{

        public CreategroupHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class NotmemberHolder extends RecyclerView.ViewHolder{

        CircleImageView mnotmember_img;
        TextView mnotmember_name,mnotmember_bio;
        Button minvite;

        public NotmemberHolder(@NonNull View itemView) {
            super(itemView);
            mnotmember_img=itemView.findViewById(R.id.notmember_img);
            mnotmember_name=itemView.findViewById(R.id.notmember_name);
            mnotmember_bio=itemView.findViewById(R.id.notmember_bio);
            minvite=itemView.findViewById(R.id.invite);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    Intent GotoMessageActivity=new Intent(mcontext,MessageActivity.class);
                    GotoMessageActivity.putExtra("name",contacts.get(position).getName());
                    GotoMessageActivity.putExtra("phoneNo",contacts.get(position).getPhoneNo());
                    mcontext.startActivity(GotoMessageActivity);
                    Toast.makeText( mcontext, "click INVITE to invite "+contacts.get(getAdapterPosition()).getName() , Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    class MemberHolder extends RecyclerView.ViewHolder{

        CircleImageView mmember_img;
        TextView mmember_name,mmember_bio;

        public MemberHolder(@NonNull View itemView) {
            super(itemView);
            mmember_img=itemView.findViewById(R.id.member_img);
            mmember_name=itemView.findViewById(R.id.member_name);
            mmember_bio=itemView.findViewById(R.id.member_bio);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    memberClicked(getAdapterPosition());
                }

                private void memberClicked(int position) {
                    Intent GotoMessageActivity=new Intent(mcontext,MessageActivity.class);
                    GotoMessageActivity.putExtra("name",contacts.get(position).getName());
                    GotoMessageActivity.putExtra("phoneNo",contacts.get(position).getPhoneNo());
                    mcontext.startActivity(GotoMessageActivity);
                }
            });
        }
    }



}
