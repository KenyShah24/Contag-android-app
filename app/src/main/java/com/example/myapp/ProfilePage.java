package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapp.Model.Photo;
import com.example.myapp.Model.Post;
import com.example.myapp.Model.SearchUser;
import com.example.myapp.ViewHolder.PhotoHolder;
import com.example.myapp.ViewHolder.PostAdapter;
import com.example.myapp.ViewHolder.dataViewHolder;
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
import java.util.Collections;
import java.util.List;

public class ProfilePage extends AppCompatActivity {

    Parcelable state;
    RecyclerView recyclerView, recyclerView1;
    ImageView imgpro, setting,options;
    TextView username, follower, following, posts;
    Button my_posts, saved_posts;
    Button edit_pro,block;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Post, postViewHolder> adapter;
    FirebaseUser fuser;
    String userid,profileid;
    DatabaseReference uref, uref1, uref2;
    Uri uri;
    View vi;
    List<String> followingList;
    List<String> postList;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile_page );
        final LinearLayout linearLayout = (LinearLayout) findViewById( R.id.private_linear );
        if (state != null) {
            layoutManager.onRestoreInstanceState( state );
            //Toast.makeText(this, "restore", Toast.LENGTH_SHORT).show();
        }
        block = findViewById( R.id.block );
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        setting = findViewById( R.id.Setting );
        userid = fuser.getUid();
        imgpro = findViewById( R.id.propic );
        username = findViewById( R.id.username );
        following = findViewById( R.id.following_num );
        follower = findViewById( R.id.follower_num );
        posts = findViewById( R.id.post_num );
        edit_pro = findViewById( R.id.edit_follow );
        setting.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProfilePage.this, Setting_page_design.class );
                startActivity( intent );
            }
        } );

        uref = FirebaseDatabase.getInstance().getReference().child( "User" );


        recyclerView = findViewById( R.id.recycler_view1 );
        recyclerView.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( this );
        ((LinearLayoutManager) layoutManager).setReverseLayout( true );
        ((LinearLayoutManager) layoutManager).setStackFromEnd( true );
        recyclerView.setLayoutManager( layoutManager );

        CheckBlock();
        checkFollowing();
        userInfo();
        getFollowers();
        getNrPost();
        getmyposts();
        //Unblock();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child( "Posts" );
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery( dbref, Post.class )
                .build();

        adapter = new FirebaseRecyclerAdapter<Post, postViewHolder>( options ) {
            @Override
            protected void onBindViewHolder(@NonNull final postViewHolder Holder, int i, @NonNull Post post) {


                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference( "Posts" );
                String uid1 = getRef( i ).getKey();
                ref1.child( uid1 ).addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            return;
                        }
                        final String userid = dataSnapshot.child( "userid" ).getValue().toString();
                        final String postid = dataSnapshot.child( "postid" ).getValue().toString();

                        if (search( postid ) != 1) {
                            Holder.layout.setVisibility( View.GONE );
                        }

                        if (search( postid ) == 1) {
                            Holder.layout.setVisibility( View.VISIBLE );
                            // Holder.comment.setVisibility(View.VISIBLE);

                            Holder.like.setVisibility( View.VISIBLE );
                            Holder.comment.setImageResource( R.drawable.ic_com );
                            Holder.comment.setVisibility( View.VISIBLE );
                            Holder.save.setVisibility( View.VISIBLE );
                            Holder.com_line.setVisibility( View.VISIBLE );


                            final String postimg = dataSnapshot.child( "postimage" ).getValue().toString();
                            final String caption = dataSnapshot.child( "caption" ).getValue().toString();

                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference( "User" );
                            final String uid = getIntent().getStringExtra( "id" );

                            ref2.child( uid ).addValueEventListener( new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    String usr = dataSnapshot.child( "usr" ).getValue().toString();
                                    String proimg = dataSnapshot.child( "imageUrl" ).getValue().toString();
                                    Glide.with( getApplicationContext() ).load( proimg ).into( Holder.proimg );
                                    Holder.username.setText( usr );
                                    Holder.publisher.setText( usr );
                                    Holder.caption.setText( caption );

                                    Glide.with( getApplicationContext() ).load( postimg ).into( Holder.postimg );

                                    isLikes( postid, Holder.like );
                                    nrLikes( Holder.likes, postid );
                                    getComments( postid, Holder.com_line );
                                    isSaved( postid, Holder.save );

                                    Holder.comment.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent( ProfilePage.this, CommentActivity.class );
                                            i.putExtra( "postid", postid );
                                            i.putExtra( "publisherid", userid );
                                            // finish();
                                            startActivity( i );
                                        }
                                    } );

                                    Holder.like.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (Holder.like.getTag().equals( "like" )) {
                                                FirebaseDatabase.getInstance().getReference().child( "Likes" )
                                                        .child( postid ).child( FirebaseAuth.getInstance().getCurrentUser().getUid() ).setValue( true );
                                            } else {
                                                FirebaseDatabase.getInstance().getReference().child( "Likes" )
                                                        .child( postid ).child( FirebaseAuth.getInstance().getCurrentUser().getUid() ).removeValue();
                                            }
                                        }
                                    } );
                                    Holder.save.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (Holder.save.getTag().equals( "save" )) {
                                                FirebaseDatabase.getInstance().getReference().child( "Saves" ).child( FirebaseAuth.getInstance().getCurrentUser().getUid() )
                                                        .child( postid ).setValue( true );
                                            } else {
                                                FirebaseDatabase.getInstance().getReference().child( "Saves" ).child( FirebaseAuth.getInstance().getCurrentUser().getUid() )
                                                        .child( postid ).removeValue();
                                            }
                                        }
                                    } );
                                    Holder.com_line.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent( ProfilePage.this, CommentActivity.class );
                                            i.putExtra( "postid", postid );
                                            i.putExtra( "publisherid", userid );
                                            // finish();
                                            startActivity( i );
                                        }
                                    } );
                                    Holder.dots.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Toast.makeText(ProfilePage.this, postid.toString(), Toast.LENGTH_SHORT).show();
                                            ShowMoreOpt( Holder.dots, userid, FirebaseAuth.getInstance().getCurrentUser().getUid(), postid, postimg );
                                        }
                                    } );
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            } );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                } );
            }

            @NonNull
            @Override
            public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from( getApplicationContext() ).inflate( R.layout.post_layout, parent, false );
                return new postViewHolder( v );
            }

        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter( adapter );
        follower.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent( ProfilePage.this, ShowFollower.class );
                startActivity( in );
            }
        } );

        following.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent( ProfilePage.this, ShowFollowing.class );
                startActivity( in );

            }
        } );

        if (userid.equals( getIntent().getStringExtra( "id" ) )) {
            edit_pro.setText( "Edit Profile" );


        } else {
            Intent intent = getIntent();
            String uid = intent.getStringExtra( "id" );
            block.setVisibility( View.VISIBLE );
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child( "User" ).child( uid );
            db.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String pri = dataSnapshot.child( "private" ).getValue().toString();
                    if (pri.equals( "true" )) {
                        recyclerView.setVisibility( View.GONE );
                        linearLayout.setVisibility( View.VISIBLE );
                        Intent intent = getIntent();
                        String uid = intent.getStringExtra( "id" );


                        DatabaseReference dbf = FirebaseDatabase.getInstance().getReference().child( "Follow" ).child( uid );
                        dbf.addValueEventListener( new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String follow = dataSnapshot.child( "followers" ).getValue().toString();
                                String following = dataSnapshot.child( "following" ).getValue().toString();
                                if (follow.equals( "true" )) {
                                    recyclerView.setVisibility( View.GONE );
                                    linearLayout.setVisibility( View.VISIBLE );
                                } else if (following.equals( "true" )) {
                                    linearLayout.setVisibility( View.GONE );
                                    recyclerView.setVisibility( View.VISIBLE );
                                } else {
                                    linearLayout.setVisibility( View.GONE );
                                    recyclerView.setVisibility( View.VISIBLE );
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        } );

                    } else
                        recyclerView.setVisibility( View.VISIBLE );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );
            checkFollow();

        }


        block.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn = block.getText().toString();

                if (btn.equals( "Block" )) {
                    FirebaseDatabase.getInstance().getReference().child( "Block_Account" ).child( fuser.getUid() )
                            .child( "Block" ).child( getIntent().getStringExtra( "id" ) ).setValue( true );
                  //  FirebaseDatabase.getInstance().getReference().child( "Block_Account" ).child( getIntent().getStringExtra( "id" ) ).child( "Unblock" )
                      //      .child( fuser.getUid() ).setValue( true );
                } else if (btn.equals( "Unblock" )) {
                    FirebaseDatabase.getInstance().getReference().child( "Block_Account" ).child( fuser.getUid() )
                            .child( "Block" ).child( getIntent().getStringExtra( "id" ) ).removeValue();
                   // FirebaseDatabase.getInstance().getReference().child( "Block_Account" ).child( getIntent().getStringExtra( "id" ) ).child( "Unblock" )
                     //       .child( fuser.getUid() ).removeValue();
                }

            }
        } );





        edit_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn=edit_pro.getText().toString();
                if(btn.equals("Edit Profile"))
                {
                    Intent i = new Intent(ProfilePage.this, EditProfile.class);
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
                                                 }
                        Toast.makeText(ProfilePage.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                        Intent in1 = new Intent(ProfilePage.this, ProfilePage.class);
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
                Toast.makeText(ProfilePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void isSaved(final String pid, final ImageView iv) {
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

    private int search(String id) {
        for (int j = 0; j < postList.size(); j++) {
            if (id.equals(postList.get(j))) {

                return 1;
            }
        }

        return -1;
    }

    private void getNrPost() {
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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.child("userid").getValue().toString();

                    if (uid.equals(getIntent().getStringExtra("id"))) {
                        postList.add(snapshot.child("postid").getValue().toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

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


    private void CheckBlock()
    {
        DatabaseReference reference12=FirebaseDatabase.getInstance().getReference().child("Block_Account").child(fuser.getUid()).child("Block");

        reference12.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(getIntent().getStringExtra("id")).exists())
                {
                    block.setText("Unblock");
                }
                else {
                    block.setText("Block");
                }
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


    /* private void Unblock() {
       DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference().child("Block_Account").child(getIntent().getStringExtra("id")).child("Block");

        ref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                follower.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference ref71 = FirebaseDatabase.getInstance().getReference().child("Block_Account").child(getIntent().getStringExtra("id")).child("Unblock");

        ref71.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/










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

    private void getComments(String postid, final TextView comments) {
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

    private void isLikes(String postid, final ImageView imgview) {
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




    private void isFollowing(final String usid, final Button button) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(fuser.getUid()).child("following");

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

        state = layoutManager.onSaveInstanceState();
          }


    @Override
    public void onBackPressed() {

        finish();

    }



}
