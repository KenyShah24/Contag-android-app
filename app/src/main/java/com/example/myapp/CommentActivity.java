package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapp.Model.Comment;
import com.example.myapp.Model.Post;
import com.example.myapp.Model.SearchUser;
import com.example.myapp.ViewHolder.CommentViewHolder;
import com.example.myapp.ViewHolder.dataViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {


    EditText addcom;
    ImageView img_pro,back;
    TextView post;
    String postid;
    String publisherid;
    FirebaseUser fuser;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Comment, CommentViewHolder> adapter;
    private List<String> mComment;
    String username2,imgpro2,comment2;
    int j=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        try {
            this.getSupportActionBar().hide();
        } catch (Exception e) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

        fuser = FirebaseAuth.getInstance().getCurrentUser();
              addcom = findViewById(R.id.add_com);
        img_pro = findViewById(R.id.imgpro);
        post = findViewById(R.id.post);

        Intent i = getIntent();
        postid = i.getStringExtra("postid");
        publisherid = i.getStringExtra("publisherid");


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addcom.getText().toString().equals("")) {
                    Toast.makeText(CommentActivity.this, "Cannot send empty comment!", Toast.LENGTH_SHORT).show();
                } else {
                    AddComment();
                }

            }
        });

        getImage();


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getComment();
    }



    private void readComment()
    {
        mComment=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //mComment.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    //Comment comment=snapshot.getValue(Comment.class);
                    mComment.add(snapshot.getKey());
                    //Toast.makeText(CommentActivity.this, snapshot.getKey(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private String fetchid(int i)
    {
        return mComment.get(i);
    }


    private void getComment()
    {
        readComment();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        //Toast.makeText(this, ref.toString(), Toast.LENGTH_SHORT).show();
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(ref,Comment.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(options) {


            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
                return new CommentViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final CommentViewHolder Holder, int i, @NonNull Comment comment1) {

               /* DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

                final String uid = ref1.getKey();
                Toast.makeText(CommentActivity.this, uid, Toast.LENGTH_SHORT).show();
                ref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {*/
                       // j=0;
                      //  for (String id : mComment) {

                          //  if (id.equals("-Lqp_S-RMw-7yKkUprUU")){
                                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Comments").child(postid).child(fetchid(i));
                           // Toast.makeText(CommentActivity.this, ref2.child("comment").toString(), Toast.LENGTH_SHORT).show();
                            ref2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // Toast.makeText(CommentActivity.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                                    comment2 = dataSnapshot.child("comment").getValue().toString();
                                    String userid = dataSnapshot.child("publisher").getValue().toString();
                                    Holder.comment1.setText(comment2);
                                   // Toast.makeText(CommentActivity.this, String.valueOf(j) + " : " + comment2, Toast.LENGTH_SHORT).show();
                                    j++;
                                    DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("User");
                                    ref3.child(userid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            username2 = dataSnapshot.child("usr").getValue().toString();
                                            imgpro2 = dataSnapshot.child("imageUrl").getValue().toString();

                                            //  Toast.makeText(CommentActivity.this, id + " : " + username2, Toast.LENGTH_SHORT).show();
                                            Holder.username.setText(username2);
                                            Glide.with(getApplicationContext()).load(imgpro2).into(Holder.imgpro1);


                                            //Holder.comment1.setText(comment2);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        //}


                        //}

                    //}

                        /*@Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

                private void AddComment()
                {
                    final String comment=addcom.getText().toString();
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Comments").child(postid);
                    String cmtid=ref.push().getKey();
                    HashMap<String,Object> h=new HashMap<>();
                    h.put("comment",addcom.getText().toString());
                    h.put("publisher",fuser.getUid());
                    ref.child(cmtid).setValue(h);

                    addcom.setText("");
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts").child(postid);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String postowner=dataSnapshot.child("userid").getValue().toString();
                            //Toast.makeText(CommentActivity.this, "", Toast.LENGTH_SHORT).show();
                            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("User").child(postowner);
                            reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final String notify_key=dataSnapshot.child("notification_key").getValue().toString();
                                    DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());
                                    reference2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String user=dataSnapshot.child("usr").getValue().toString();
                                            SpannableString ss=new SpannableString(user);
                                            StyleSpan bold=new StyleSpan(Typeface.BOLD);
                                            ss.setSpan(bold,0,user.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            //Toast.makeText(CommentActivity.this, ss.toString(), Toast.LENGTH_SHORT).show();
                                            addNotification(comment,postowner);
                                            if(!postowner.equals(fuser.getUid()))
                                            new SendNotification(ss.toString()+" commented : "+comment,"Contag",notify_key);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    readComment();
                }
    private void getImage()
    {
        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url=dataSnapshot.child("imageUrl").getValue().toString();
                Glide.with(getApplicationContext()).load(url).into(img_pro);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void addNotification(String comment,String userid)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        String notify_id=ref.push().getKey();
        HashMap<String,Object> h=new HashMap<>();
        h.put("userid",fuser.getUid());
        h.put("text","commented : "+comment);
        h.put("postid",postid);
        h.put("ispost",true);
        ref.child(notify_id).setValue(h);
    }
}
