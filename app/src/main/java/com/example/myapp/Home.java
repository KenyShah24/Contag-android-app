package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapp.Model.Post;
import com.example.myapp.ViewHolder.PhotoHolder;
import com.example.myapp.ViewHolder.postViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    List<String> SavedList;
    RecyclerView recyclerView;
    String name,url;
    FirebaseRecyclerAdapter<Post, PhotoHolder> adapter;
    FirebaseRecyclerAdapter<Post, postViewHolder> adapter1;
    DatabaseReference ref;
    List<String> followingList;
    RecyclerView.LayoutManager layoutManager;
    //RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_post);

        recyclerView=findViewById(R.id.saved_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        SavedList=new ArrayList<>();

        saveList();

        ViewPost();

        /*ref= FirebaseDatabase.getInstance().getReference().child("Posts");

        FirebaseRecyclerOptions<Post> options=new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(ref,Post.class)
                .build();


        adapter=new FirebaseRecyclerAdapter<Post, PhotoHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PhotoHolder Holder, int i, @NonNull Post post) {

                DatabaseReference ref1=FirebaseDatabase.getInstance().getReference().child("Posts");
                String uid=getRef(i).getKey();
                //Toast.makeText(SavedPost.this, uid, Toast.LENGTH_SHORT).show();
                ref1.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String postid=dataSnapshot.child("postid").getValue().toString();
                        //Toast.makeText(SavedPost.this, postid, Toast.LENGTH_SHORT).show();
                        for(String id:SavedList)
                        {
                            // Toast.makeText(SavedPost.this, id, Toast.LENGTH_SHORT).show();
                            if(postid.equals(id))
                            {
                                //   Toast.makeText(SavedPost.this, id, Toast.LENGTH_SHORT).show();
                                String url=dataSnapshot.child("postimage").getValue().toString();
                                Glide.with(getApplicationContext()).load(url).into(Holder.post_image);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_layout,parent,false);
                return new PhotoHolder(v);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);*/



    }

    @Override
    public void onBackPressed() {

        Intent i=new Intent(Home.this,HomePage.class);
        finish();
        startActivity(i);
        //super.onBackPressed();
    }

    /*private void getname(String uid)
        {
            //final String[] name = new String[1];
            final int[] cnt = {0};
            int i;
            DatabaseReference ref2=FirebaseDatabase.getInstance().getReference("User").child(uid);
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name=dataSnapshot.child("usr").getValue().toString();
                    cnt[0]++;
                    //return id;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Toast.makeText(this, String.valueOf(cnt[0]), Toast.LENGTH_SHORT).show();
            i= cnt[0];
            //return name;
        }

        private void geturl(String uid)
        {
           // final String[] url = new String[1];
            DatabaseReference ref2=FirebaseDatabase.getInstance().getReference("User").child(uid);
            //Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    url =dataSnapshot.child("imageUrl").getValue().toString();
                    //Toast.makeText(SavedPost.this, url, Toast.LENGTH_SHORT).show();
                    //return id;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
           // return url;
        }*/
    private String getid(int i)
    {
        return SavedList.get(i);
    }

    private int search(String id)
    {
        for(int j=0;j<SavedList.size();j++)
        {
            if(id.equals(SavedList.get(j))) {
                //Toast.makeText(this, id+" returns", Toast.LENGTH_SHORT).show();
                return 1;
            }
        }
        //Toast.makeText(this, id+"-1", Toast.LENGTH_SHORT).show();
        return -1;
    }
    private void saveList()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Saves")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    // String save=snapshot.getKey();
                    //Toast.makeText(SavedPost.this, save, Toast.LENGTH_SHORT).show();
                    SavedList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void ViewPost()
    {
        //checkFollowing();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");

        //Query sort=ref.orderByChild("counter");
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(ref,Post.class)
                .build();

        // Toast.makeText(this, options.toString(), Toast.LENGTH_SHORT).show();

        adapter1 = new FirebaseRecyclerAdapter<Post, postViewHolder>(options) {


            @Override
            public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
                //Toast.makeText(HomePage.this, "hii", Toast.LENGTH_SHORT).show();
                return new postViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final postViewHolder Holder, final int i, @NonNull Post post) {

                DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Posts");
                // int k=0;
                String uid1=getRef(i).getKey();

                // Toast.makeText(HomePage.this, uid1, Toast.LENGTH_SHORT).show();
                ref1.child(uid1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String userid = dataSnapshot.child("userid").getValue().toString();
                        final String postid=dataSnapshot.child("postid").getValue().toString();
                        //Toast.makeText(HomePage.this, postid, Toast.LENGTH_SHORT).show();
                       // for (int k=0;k<SavedList.size();k++) {

                        if(search(postid)!=1){
                            Holder.layout.setVisibility(View.GONE);
                        }
                            //Toast.makeText(SavedPost.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(SavedPost.this,"..", Toast.LENGTH_SHORT).show();
                            //k++;
                            //Holder.layout.setVisibility(View.VISIBLE);
                            //String id="2rh1MZJzV0dmpad4QtDq1tKJw1o1";
                            //Toast.makeText(SavedPost.this, id, Toast.LENGTH_SHORT).show();

                            if(search(postid)==1) {

                                //SavedList.remove(k);
                                Holder.layout.setVisibility(View.VISIBLE);
                                Holder.comment.setVisibility(View.VISIBLE);
                                Holder.like.setVisibility(View.VISIBLE);
                                Holder.save.setVisibility(View.VISIBLE);
                                Holder.com_line.setVisibility(View.VISIBLE);
                                //recyclerView.setEnabled(true);
                                // Holder.layout.setVisibility(View.VISIBLE);
                                final String postimg = dataSnapshot.child("postimage").getValue().toString();
                                final String caption = dataSnapshot.child("caption").getValue().toString();
                                //Toast.makeText(SavedPost.this, userid+" : "+caption.toString(), Toast.LENGTH_SHORT).show();

                                //getname(userid);
                                //geturl(userid);
                                //Toast.makeText(SavedPost.this, "this  : "+name, Toast.LENGTH_SHORT).show();


                                DatabaseReference ref2=FirebaseDatabase.getInstance().getReference("User").child(userid);
                                ref2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        // Toast.makeText(SavedPost.this, "usr: "+String.valueOf(i), Toast.LENGTH_SHORT).show();

                                        String usr=dataSnapshot.child("usr").getValue().toString();
                                        String proimg=dataSnapshot.child("imageUrl").getValue().toString();
                                        //      Toast.makeText(HomePage.this, proimg, Toast.LENGTH_SHORT).show();
                                        Glide.with(getApplicationContext()).load(proimg).into(Holder.proimg);
                                        Holder.username.setText(usr);
                                        Holder.publisher.setText(usr);
                                        Holder.caption.setText(caption);
                                        Glide.with(getApplicationContext()).load(postimg).into(Holder.postimg);

                                        isLikes(postid,Holder.like);
                                        nrLikes(Holder.likes,postid);
                                        getComments(postid,Holder.com_line);
                                        isSaved(postid,Holder.save);

                                        Holder.like.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(Holder.like.getTag().equals("like")){
                                                    FirebaseDatabase.getInstance().getReference().child("Likes")
                                                            .child(postid).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                                                }
                                                else
                                                {
                                                    FirebaseDatabase.getInstance().getReference().child("Likes")
                                                            .child(postid).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                                }
                                            }
                                        });
                                        Holder.save.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(Holder.save.getTag().equals("save"))
                                                {
                                                    FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child(postid).setValue(true);
                                                }
                                                else {
                                                    FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child(postid).removeValue();
                                                }
                                            }
                                        });
                                        Holder.com_line.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent i=new Intent(Home.this,CommentActivity.class);
                                                i.putExtra("postid",postid);
                                                i.putExtra("publisherid",userid);
                                                startActivity(i);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            //Holder.layout.setVisibility(View.VISIBLE);
                        //} for loop

                    }
                    // }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        };

        adapter1.startListening();
        adapter1.notifyDataSetChanged();
        recyclerView.setAdapter(adapter1);

    }

    private void getComments(String postid, final TextView comments)
    {
        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.setText("View All "+dataSnapshot.getChildrenCount()+" Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void isLikes(String postid,final ImageView imgview)
    {
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imgview.setImageResource(R.drawable.ic_liked);
                    imgview.setTag("liked");
                }
                else
                {
                    imgview.setImageResource(R.drawable.ic_lik);
                    imgview.setTag("like");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nrLikes(final TextView likes, String postid)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+" likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isSaved(final String pid, final ImageView iv)
    {
        FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Saves")
                .child(fuser.getUid());
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(pid).exists())
                {
                    iv.setImageResource(R.drawable.ic_saved);
                    iv.setTag("saved");
                }
                else
                {
                    iv.setImageResource(R.drawable.ic_save);
                    iv.setTag("save");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkFollowing()
    {
        followingList=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    followingList.add(snapshot.getKey());
                    //Toast.makeText(HomePage.this, snapshot.getKey(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
