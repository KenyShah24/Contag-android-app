package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapp.Model.Post;
import com.example.myapp.ViewHolder.postViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class profile_private extends AppCompatActivity {
    Parcelable state;
    RecyclerView recyclerView, recyclerView1;
    ImageView imgpro, setting,options;
    TextView username, follower, following, posts;
    Button my_posts, saved_posts;
    Button edit_pro;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Post, postViewHolder> adapter;
    FirebaseUser fuser;
    String userid,profileid;
    DatabaseReference uref, uref1, uref2;
    Uri uri;
    View vi;
    List<String> followingList;
    List<String> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile_private );

        if(state!=null) {
            layoutManager.onRestoreInstanceState(state);
            //Toast.makeText(this, "restore", Toast.LENGTH_SHORT).show();
        }

        fuser = FirebaseAuth.getInstance().getCurrentUser();


        // SharedPreferences pref=getApplicationContext().getSharedPreferences( "PERF", getApplicationContext().MODE_PRIVATE );
        setting=findViewById(R.id.Setting);
        // SharedPreferences prefs = getApplicationContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        //profileid=prefs.getString("profileid","none");

        // Toast.makeText(this, profileid, Toast.LENGTH_SHORT).show();

        userid = fuser.getUid();

        imgpro = findViewById(R.id.propic);
        username = findViewById(R.id.username);
        following = findViewById(R.id.following_num);
        follower = findViewById(R.id.follower_num);
        posts = findViewById(R.id.post_num);
        //    my_posts = findViewById(R.id.my_posts);
        //saved_posts=findViewById(R.id.saved_posts);
        edit_pro = findViewById(R.id.edit_follow);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(profile_private.this, Setting_page_design.class);
                startActivity(intent);
            }
        });

        uref = FirebaseDatabase.getInstance().getReference().child("User");


        recyclerView = findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);



       /* recyclerView1=findViewById(R.id.recycler_view_save);
        recyclerView1.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager);*/


        checkFollowing();
        userInfo();
        getFollowers();
        // getNrPost();
        // getmyposts();

   /* my_posts.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        Intent i=new Intent(ProfilePage.this,EditProfile.class);
        startActivity(i);
    }
});*/
        //myPhotos();

      /*  DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Posts");
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(dbref, Post.class)
                .build();

        //Toast.makeText(this, "Hii", Toast.LENGTH_SHORT).show();
        adapter = new FirebaseRecyclerAdapter<Post, postViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final postViewHolder Holder, int i, @NonNull Post post) {


                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Posts");
                String uid1 = getRef(i).getKey();
                ref1.child(uid1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists())
                        {
                            return;
                        }

                        final String userid = dataSnapshot.child("userid").getValue().toString();
                        final String postid = dataSnapshot.child("postid").getValue().toString();

                        if (search(postid) != 1) {
                            Holder.layout.setVisibility(View.GONE);
                        }

                        if (search(postid) == 1) {
                            Holder.layout.setVisibility(View.VISIBLE);
                            // Holder.comment.setVisibility(View.VISIBLE);

                            Holder.like.setVisibility(View.VISIBLE);
                            Holder.comment.setImageResource(R.drawable.ic_com);
                            Holder.comment.setVisibility(View.VISIBLE);
                            Holder.save.setVisibility(View.VISIBLE);
                            Holder.com_line.setVisibility(View.VISIBLE);


                            final String postimg = dataSnapshot.child("postimage").getValue().toString();
                            final String caption = dataSnapshot.child("caption").getValue().toString();

                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("User");
                            final String uid = getIntent().getStringExtra("id");

                            ref2.child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    String usr = dataSnapshot.child("usr").getValue().toString();
                                    String proimg = dataSnapshot.child("imageUrl").getValue().toString();
                                    Glide.with(getApplicationContext()).load(proimg).into(Holder.proimg);
                                    Holder.username.setText(usr);
                                    Holder.publisher.setText(usr);
                                    Holder.caption.setText(caption);

                                    Glide.with(getApplicationContext()).load(postimg).into(Holder.postimg);

                                    isLikes(postid, Holder.like);
                                    nrLikes(Holder.likes, postid);
                                    getComments(postid, Holder.com_line);
                                    isSaved(postid, Holder.save);

                                    Holder.comment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(ProfilePage.this, CommentActivity.class);
                                            i.putExtra("postid", postid);
                                            i.putExtra("publisherid", userid);
                                            // finish();
                                            startActivity(i);
                                        }
                                    });

                                    Holder.like.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (Holder.like.getTag().equals("like")) {
                                                FirebaseDatabase.getInstance().getReference().child("Likes")
                                                        .child(postid).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                                            } else {
                                                FirebaseDatabase.getInstance().getReference().child("Likes")
                                                        .child(postid).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                            }
                                        }
                                    });
                                    Holder.save.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (Holder.save.getTag().equals("save")) {
                                                FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .child(postid).setValue(true);
                                            } else {
                                                FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .child(postid).removeValue();
                                            }
                                        }
                                    });
                                    Holder.com_line.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(ProfilePage.this, CommentActivity.class);
                                            i.putExtra("postid", postid);
                                            i.putExtra("publisherid", userid);
                                            // finish();
                                            startActivity(i);
                                        }
                                    });
                                    Holder.dots.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Toast.makeText(ProfilePage.this, postid.toString(), Toast.LENGTH_SHORT).show();
                                            ShowMoreOpt(Holder.dots,userid,FirebaseAuth.getInstance().getCurrentUser().getUid(),postid,postimg);
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        //Holder.layout.setVisibility(View.VISIBLE);
                        //} if

                    }
                    // }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @NonNull
            @Override
            public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.post_layout, parent, false);
                return new postViewHolder(v);
            }

        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

*/
        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(profile_private.this, ShowFollower.class);
                startActivity(in);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(profile_private.this, ShowFollowing.class);
                startActivity(in);
            }
        });

        if (userid.equals(getIntent().getStringExtra("id"))) {
            edit_pro.setText("Edit Profile");
        } else {
            checkFollow();
            //saved_posts.setVisibility(View.GONE);
        }

        edit_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn=edit_pro.getText().toString();
                if(btn.equals("Edit Profile"))
                {
                    Intent i = new Intent(profile_private.this, EditProfile.class);
                    startActivity(i);
                }
                else if(btn.equals("FOLLOW"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(fuser.getUid())
                            .child("following").child(getIntent().getStringExtra("id")).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(getIntent().getStringExtra("id")).child("followers")
                            .child(fuser.getUid()).setValue(true);
                }
                else if(btn.equals("FOLLOWING"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(fuser.getUid())
                            .child("following").child(getIntent().getStringExtra("id")).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(getIntent().getStringExtra("id")).child("followers")
                            .child(fuser.getUid()).removeValue();
                }

            }
        });


    }

    private void ShowMoreOpt(ImageView iv, String id, String myid, final String pid, final String pimg)
    {
        PopupMenu popupMenu=new PopupMenu(getApplicationContext(),iv, Gravity.END);
        if(id.equals(myid)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
        }
        else
        {
            ;
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id=menuItem.getItemId();
                if(id==0)
                {
                    beginDelete(pid,pimg);
                }
                return false;
            }
        });


    }

    private void beginDelete(final String pid, String pimg)
    {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Deleting..");
        StorageReference sr= FirebaseStorage.getInstance().getReferenceFromUrl(pimg);
        //Toast.makeText(this, sr.toString(), Toast.LENGTH_SHORT).show();
        sr.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Query fq=FirebaseDatabase.getInstance().getReference("Posts").orderByChild("postid").equalTo(pid);
                //      Toast.makeText(MyHome.this, fq.toString(), Toast.LENGTH_LONG).show();
                fq.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            ds.getRef().removeValue();
                            //Toast.makeText(MyHome.this, ds.getRef().toString(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MyHome.this, "done", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(profile_private.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                        Intent in1 = new Intent(profile_private.this, ProfilePage.class);
                        in1.putExtra("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        finish();
                        startActivity(in1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(profile_private.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*  private void isSaved(final String pid, final ImageView iv) {
          FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
          DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Saves")
                  .child(fuser.getUid());
          db.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if (dataSnapshot.child(pid).exists()) {
                      iv.setImageResource(R.drawable.ic_saved);
                      iv.setTag("saved");
                  } else {
                      iv.setImageResource(R.drawable.ic_save);
                      iv.setTag("save");
                  }

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });

      }
  */
    private int search(String id) {
        for (int j = 0; j < postList.size(); j++) {
            if (id.equals(postList.get(j))) {
                //Toast.makeText(this, id+" returns", Toast.LENGTH_SHORT).show();
                return 1;
            }
        }
        //Toast.makeText(this, id+"-1", Toast.LENGTH_SHORT).show();
        return -1;
    }

    /*  private void getNrPost() {
          DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Posts");
          ref2.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  int i = 0;
                  for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                      String uid = snapshot.child("userid").getValue().toString();
                      //  Toast.makeText(ProfilePage.this, userid, Toast.LENGTH_SHORT).show();
                      if (uid.equals(getIntent().getStringExtra("id"))) {
                          i++;
                      }
                  }
                  posts.setText("" + i);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
      }

      private void getmyposts() {
          postList = new ArrayList<>();
          // Toast.makeText(this, "getmyposts", Toast.LENGTH_SHORT).show();
          DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Posts");
          ref.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                      String uid = snapshot.child("userid").getValue().toString();
                      // Toast.makeText(ProfilePage.this, uid, Toast.LENGTH_SHORT).show();
                      if (uid.equals(getIntent().getStringExtra("id"))) {
                          postList.add(snapshot.child("postid").getValue().toString());
                          //Toast.makeText(ProfilePage.this,snapshot.child("postid").getValue().toString() , Toast.LENGTH_SHORT).show();
                      }
                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });

      }
  */
    private void userInfo() {

        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("User").child(getIntent().getStringExtra("id"));
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String usname = dataSnapshot.child("usr").getValue().toString();
                String url = dataSnapshot.child("imageUrl").getValue().toString();

                Glide.with(getApplicationContext()).load(url).into(imgpro);
                username.setText(usname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkFollow()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Follow").child(fuser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(getIntent().getStringExtra("id")).exists())
                {
                    edit_pro.setText("FOLLOWING");
                }
                else {
                    edit_pro.setText("FOLLOW");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getFollowers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Follow").child(getIntent().getStringExtra("id")).child("followers");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                follower.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Follow").child(getIntent().getStringExtra("id")).child("following");

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*  private void getComments(String postid, final TextView comments) {
          DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);
          ref2.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  comments.setText("View All " + dataSnapshot.getChildrenCount() + " Comments");
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
      }
  */
    private void checkFollowing() {
        followingList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followingList.add(snapshot.getKey());
                    //Toast.makeText(HomePage.this, snapshot.getKey(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

 /*   private void isLikes(String postid, final ImageView imgview) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imgview.setImageResource(R.drawable.ic_liked);
                    imgview.setTag("liked");
                } else {
                    imgview.setImageResource(R.drawable.ic_lik);
                    imgview.setTag("like");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nrLikes(final TextView likes, String postid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount() + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
*/

    /*private void myPhotos()
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Posts");

        FirebaseRecyclerOptions options= new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(ref,Post.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Post, PhotoHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PhotoHolder photoHolder, int i, @NonNull Post post) {

                DatabaseReference dbref=FirebaseDatabase.getInstance().getReference().child("Posts");
                String u=getRef(i).getKey();
                Toast.makeText(ProfilePage.this, u, Toast.LENGTH_SHORT).show();
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //String user=dataSnapshot.child("usr").getValue().toString();
                        //Toast.makeText(ProfilePage.this, user, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_layout,parent,false);
                //Toast.makeText(ProfilePage.this, "hello", Toast.LENGTH_SHORT).show();
                return new PhotoHolder(v);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }*/


    private void isFollowing(final String usid, final Button button) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(fuser.getUid()).child("following");
        //Toast.makeText(ProfilePage.this, usid, Toast.LENGTH_SHORT).show();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(usid).exists()) {
                    button.setText("FOLLOWING");
                } else {
                    button.setText("FOLLOW");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save ListView state @ onPause
        state = layoutManager.onSaveInstanceState();
        //Toast.makeText(this, "next", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {
        // Intent i = new Intent(ProfilePage.this, HomePage.class);
        finish();
        //   startActivity(i);
    }



}
    /*private void myPhotos()
    {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Posts");
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(dbref,Post.class)
                .build();
        //Toast.makeText(this, options.toString(), Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, options.toString()+" next", Toast.LENGTH_SHORT).show();

        adapter = new FirebaseRecyclerAdapter<Post, PhotoHolder>(options) {


            @Override
            public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_layout,parent,false);
                //Toast.makeText(ProfilePage.this, "hello", Toast.LENGTH_SHORT).show();
                return new PhotoHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final PhotoHolder Holder, final int i, @NonNull Post post) {

                DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Posts");
                String uid1=getRef(i).getKey();
                Toast.makeText(ProfilePage.this, uid1, Toast.LENGTH_SHORT).show();
                ref1.child(uid1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String userid = dataSnapshot.child("userid").getValue().toString();
                        final String postid=dataSnapshot.child("postid").getValue().toString();
                        Toast.makeText(ProfilePage.this, postid, Toast.LENGTH_SHORT).show();
                        for (String id : followingList) {
                            //Holder.layout.setVisibility(View.VISIBLE);
                            //String id="2rh1MZJzV0dmpad4QtDq1tKJw1o1";
                            //Toast.makeText(HomePage.this, id, Toast.LENGTH_SHORT).show();
                            //
                            if (!userid.equals(id)) {

                            }

                            if(userid.equals(id) || userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                final String postimg = dataSnapshot.child("postimage").getValue().toString();
                                final String caption = dataSnapshot.child("caption").getValue().toString();
                                DatabaseReference ref2=FirebaseDatabase.getInstance().getReference("User");
                                final String uid=userid;
                                ref2.child(uid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        String usr=dataSnapshot.child("usr").getValue().toString();
                                        String proimg=dataSnapshot.child("imageUrl").getValue().toString();
                                        //      Toast.makeText(HomePage.this, proimg, Toast.LENGTH_SHORT).show();
                                        Glide.with(getApplicationContext()).load(proimg).into(Holder.post_image);






                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(ProfilePage.this, "cancel", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            //Holder.layout.setVisibility(View.VISIBLE);
                        }

                    }
                    // }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ProfilePage.this, "cancel1", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }*/


       /* adapter = new FirebaseRecyclerAdapter<Photo, PhotoHolder>(options) {

            @Override
            public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_layout,parent,false);
              //  Toast.makeText(ProfilePage.this, v.toString(), Toast.LENGTH_SHORT).show();
                return new PhotoHolder(v);
                //return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull final PhotoHolder Holder, int i, @NonNull Photo photo) {

                DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Posts");
                String uid1=getRef(i).getKey();
                Toast.makeText(ProfilePage.this, uid1, Toast.LENGTH_SHORT).show();
                ref1.child(uid1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String usid = dataSnapshot.child("userid").getValue().toString();
                        final String url=dataSnapshot.child("postimage").getValue().toString();
                        if (usid.equals(userid)) {
                            Glide.with(getApplicationContext()).load(url).into(Holder.post_image);
                        }

                     }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
       // Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();

    }*/





