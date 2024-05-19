package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapp.Model.Comment;
import com.example.myapp.Model.notifyuser;
import com.example.myapp.ViewHolder.CommentViewHolder;
import com.example.myapp.ViewHolder.notifyHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BellActivity extends AppCompatActivity {

    String ispost;
    FirebaseUser fuser;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<notifyuser, notifyHolder> adapter;
    private List<String> mNotify;

    String profileimg,comment,postimg,usrname,usrid,post_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bell);

        try {
            this.getSupportActionBar().hide();
        } catch (Exception e) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

        fuser = FirebaseAuth.getInstance().getCurrentUser();


//        Toast.makeText(this, "layout", Toast.LENGTH_SHORT).show();




        /*OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();*/

        OneSignal.startInit(this).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notification_key").setValue(userId);

            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);

        recyclerView = findViewById(R.id.recycle_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        getNotification();



    }





    private void readNotification()
    {
        mNotify=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notifications").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //mComment.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    //Comment comment=snapshot.getValue(Comment.class);
                    mNotify.add(snapshot.getKey());
                    //Toast.makeText(BellActivity.this, snapshot.getKey(), Toast.LENGTH_SHORT).show();
                }
                Collections.reverse(mNotify);
               // Toast.makeText(BellActivity.this,"reversed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private String fetchid(int i)
    {
        return mNotify.get(i);
    }


    private void getNotification()
    {
        readNotification();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notifications").child(fuser.getUid());
        //Toast.makeText(this, ref.toString(), Toast.LENGTH_SHORT).show();
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<notifyuser>()
                .setQuery(ref,notifyuser.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<notifyuser, notifyHolder>(options) {


            @Override
            public notifyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_item, parent, false);
                //Toast.makeText(BellActivity.this,"reversed11", Toast.LENGTH_SHORT).show();
                return new notifyHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final notifyHolder Holder, int i, @NonNull notifyuser nuser) {

                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Notifications").child(fuser.getUid()).child(fetchid(i));

                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Toast.makeText(CommentActivity.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                         comment = dataSnapshot.child("text").getValue().toString();
                         ispost=dataSnapshot.child("ispost").getValue().toString();
                        usrid = dataSnapshot.child("userid").getValue().toString();
                        post_id=dataSnapshot.child("postid").getValue().toString();

                        Holder.txt.setText(comment);

                        //Toast.makeText(BellActivity.this, ispost+"++", Toast.LENGTH_SHORT).show();
                        if(ispost.equals("true"))
                        {
                            Holder.post_image1.setVisibility(View.VISIBLE);
                           // Toast.makeText(BellActivity.this, "visible", Toast.LENGTH_SHORT).show();
                            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Posts").child(post_id);
                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    postimg=dataSnapshot.child("postimage").getValue().toString();
                                    Glide.with(getApplicationContext()).load(postimg).into(Holder.post_image1);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            Holder.post_image1.setVisibility(View.GONE);
                        }

                        //fetching username and id of the commented user
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User").child(usrid);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                usrname=dataSnapshot.child("usr").getValue().toString();
                                profileimg=dataSnapshot.child("imageUrl").getValue().toString();

                                Holder.user1.setText(usrname);
                                Glide.with(getApplicationContext()).load(profileimg).into(Holder.image_profile);

                        //        Toast.makeText(BellActivity.this, ispost, Toast.LENGTH_SHORT).show();
                          //      Toast.makeText(BellActivity.this, "...", Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        // Toast.makeText(CommentActivity.this, String.valueOf(j) + " : " + comment2, Toast.LENGTH_SHORT).show();



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public int getItemCount() {
                //Toast.makeText(BellActivity.this, "hii "+String.valueOf(mNotify.size()), Toast.LENGTH_SHORT).show();
                return mNotify.size();
            }
        };

        adapter.startListening();
        //mNotify.add("-LxpO2rUdXhWSO5qmHUM");
        //Toast.makeText(this, "after list", Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
        //Toast.makeText(this, "notify", Toast.LENGTH_SHORT).show();

        recyclerView.setAdapter(adapter);

        // Toast.makeText(this, "set adapter", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, String.valueOf(adapter.getItemCount()), Toast.LENGTH_SHORT).show();



    }





}
   /* private void getNotification {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notification").child(fuser.getUid());
        //Toast.makeText(this, ref.toString(), Toast.LENGTH_SHORT).show();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(ref, Comment.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(options) {
        }
    }*/





/*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        notify=dataSnapshot.child("notification_key").getValue().toString();
                        new SendNotification("Hello user","Contag",notify);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(BellActivity.this, notify, Toast.LENGTH_SHORT).show();

            }
        });*/
