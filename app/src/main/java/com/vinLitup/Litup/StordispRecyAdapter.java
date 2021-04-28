package com.vinLitup.Litup;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StordispRecyAdapter extends RecyclerView.Adapter {

    String fileType;
    ArrayList<File> files;
    Context context;
    int prevPosition;
    Audiodisplay audiodisplay;
    MediaPlayer mediaPlayer = null;
    boolean isplaying=false;
    int i=0;

    public StordispRecyAdapter(String fileType , ArrayList<File> files, Context context) {
        this.fileType = fileType;
        this.files = files;
        this.context=context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (fileType) {
            case "PHOTO":
                return 0;
            case "DOCUMENT":
                return 1;
            case "AUDIO":
                return 2;
            case "VIDEO":
                return 3;
            default:
                return 4;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view;
        if (viewType==0 || viewType==3){
            view=layoutInflater.inflate(R.layout.singleimage,parent,false);
            return new ImageVideodisplay(view);
        }else if (viewType==1){
            view=layoutInflater.inflate(R.layout.singledocument,parent,false);
            return new Documentdisplay(view);
        }else if (viewType==2){
            view=layoutInflater.inflate(R.layout.singleaudio,parent,false);
            return new Audiodisplay(view);
        }else {
            view=layoutInflater.inflate(R.layout.singlecontact,parent,false);
            return new Contactdisplay(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder , int position) {
        i++;
        Log.d("ERfind"," "+ i);
        switch (fileType) {

            case "VIDEO": {
                ImageVideodisplay imageVideodisplay = (ImageVideodisplay) holder;
                Uri uri = Uri.fromFile(files.get(position));
                Glide.with(context)
                        .load(uri).thumbnail(0.1f).into(imageVideodisplay.imageView);

                break;
            }
            case "PHOTO": {
                ImageVideodisplay imageVideodisplay = (ImageVideodisplay) holder;
                Glide.with(context).load(files.get(position)).into(imageVideodisplay.imageView);

                break;
            }
            case "DOCUMENT":

                Documentdisplay documentdisplay = (Documentdisplay) holder;
                documentdisplay.textView.setText(files.get(position).getName());

                break;
            case "AUDIO":
                audiodisplay = (Audiodisplay) holder;
                if (position==prevPosition){
                    audiodisplay.imageButton.setImageResource(R.drawable.ic_pause);
                }else {
                    audiodisplay.imageButton.setImageResource(R.drawable.ic_play);
                }
                audiodisplay.textView.setText(files.get(position).getName());


                break;
            default:
                final Contactdisplay contactdisplay = (Contactdisplay) holder;
                contactdisplay.contactName.setText(KeepValues.contacts.get(position).getName());
                contactdisplay.contactNumber.setText(KeepValues.contacts.get(position).getPhoneNo());

                final DatabaseReference mDatabaseref= FirebaseDatabase.getInstance().getReference().child("USERS");
                mDatabaseref.child(KeepValues.contacts.get(position).getPhoneNo()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.hasChild("Image")){
                            String retrieveImage=dataSnapshot.child("Image").getValue().toString();

                            Glide.with(context).load(retrieveImage).placeholder(R.drawable.ic_yellowaccount).into(contactdisplay.contactImg);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
        }

    }

    @Override
    public int getItemCount() {
        if (fileType.equals("CONTACT"))
            return KeepValues.contacts.size();
        else
        return files.size();
    }

    class ImageVideodisplay extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ImageVideodisplay(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView1);
        }
    }

    class Documentdisplay extends RecyclerView.ViewHolder{

        TextView textView;

        public Documentdisplay(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView2);
        }
    }

    class Audiodisplay extends RecyclerView.ViewHolder{
        TextView textView;
        ImageButton imageButton;

        public Audiodisplay(@NonNull final View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.audioname);
            imageButton=itemView.findViewById(R.id.playaudio);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prevPosition=getAdapterPosition();
                    notifyDataSetChanged();

                    /*if(!isplaying){
                        imageButton.setImageResource(R.drawable.ic_pause);
                        isplaying=true;
                        MediaSwitcher(getAdapterPosition());
                        prevPosition=getAdapterPosition();
                    }else {
                        mediaPlayer.release();
                        mediaPlayer=null;
                        isplaying=false;
                        imageButton.setImageResource(R.drawable.ic_play);

                    }*/


                }
            });
        }

        private void MediaSwitcher(int adapterPosition) {

            if (mediaPlayer!=null){
                mediaPlayer.release();
                mediaPlayer=null;
                //bindViewHolder(audiodisplay,prevPosition);
            }
            mediaPlayer=MediaPlayer.create(context, Uri.fromFile(files.get(adapterPosition)));
            mediaPlayer.start();

            //prevPosition=adapterPosition;

        }
    }

    class Contactdisplay extends RecyclerView.ViewHolder{

        TextView contactName,contactNumber;
        CircleImageView contactImg;

        public Contactdisplay(@NonNull View itemView) {
            super(itemView);
            contactName=itemView.findViewById(R.id.contact_name);
            contactNumber=itemView.findViewById(R.id.contact_number);
            contactImg=itemView.findViewById(R.id.contact_dialog);

        }
    }
}
