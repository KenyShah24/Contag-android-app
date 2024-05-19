package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapp.Model.Post;
import com.example.myapp.Model.SearchUser;
import com.example.myapp.ViewHolder.PostAdapter;
import com.example.myapp.ViewHolder.postViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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

public class HomePage extends AppCompatActivity {

    Parcelable state;
    int cnt=0;
    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    ActionBar actbar;
    String n;
    NavigationView nv;
    FirebaseAuth auth1;
    DatabaseReference dbref,dbref1;
    TextView name,name1;
    String username;
    String uid;
    View hview;
    int avoid[],post_cnt=0;
    BottomNavigationView bnav;


    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<String> followingList;
    private List<String> pList;

    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Post, postViewHolder> adapter;
    View vi;
   // FirebaseDatabase dbref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        auth1=FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.result_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        if(state!=null) {
            layoutManager.onRestoreInstanceState(state);
            Toast.makeText(this, "restore", Toast.LENGTH_SHORT).show();
        }

        pList=new ArrayList<>();

        postcount();

       // Toast.makeText(HomePage.this,"now  "+String.valueOf(post_cnt), Toast.LENGTH_SHORT).show();

        ViewPost();



        myDrawer = (DrawerLayout)findViewById(R.id.Drawer);
        nv=findViewById(R.id.nv);

        bnav=findViewById(R.id.nav_view);
        bnav.setOnNavigationItemSelectedListener(navlist);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.title_Search:
                       Intent in=new Intent(HomePage.this,SearchPage.class);
                       startActivity(in);
                       finish();
                        myDrawer.closeDrawers();
                        break;

                    case R.id.title_Memories:
                        Intent in2=new Intent(HomePage.this,SavedPost.class);
                        startActivity(in2);
                        finish();
                        myDrawer.closeDrawers();
                        break;

                    case R.id.title_logout:
                        auth1.signOut();
                        finish();
                        Intent in1=new Intent(HomePage.this,Login.class);
                        startActivity(in1);
                        break;

                }

                return false;
            }

        });
        myToggle = new ActionBarDrawerToggle(this,myDrawer,R.string.open, R.string.close);
        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        dbref= FirebaseDatabase.getInstance().getReference("User").child(auth1.getUid());
        hview= (View) nv.getHeaderView(0);
        name=hview.findViewById(R.id.name);



        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            username=dataSnapshot.child("usr").getValue().toString();
            name.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(myToggle.onOptionsItemSelected(item))
        {
            cnt++;
            if(cnt%2!=0)
                myDrawer.bringToFront();
               // ViewCompat.setTranslationZ(view, translationZ);
                //recyclerView.setZ(-1);
            else
                recyclerView.bringToFront();
            return true;
        }
       //recyclerView.setZ(0);
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlist=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.navigation_home:
                            Intent i2=new Intent(HomePage.this,HomePage.class);
                            finish();
                            startActivity(i2);
                            break;
                        case R.id.navigation_addimage:
                            Intent i=new Intent(HomePage.this,PostPage.class);
                            finish();
                            startActivity(i);
                            break;

                        case R.id.navigation_setting:
                            Intent i1=new Intent(HomePage.this,ProfilePage.class);
                            finish();
                            startActivity(i1);
                            break;

                    }
                    return false;
                }
            };

    private void publisherInfo(final ImageView img_pro, final TextView username, final TextView publisher, String userid)
    {
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("User").child(userid);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Data usr=dataSnapshot.getValue(Data.class);
                Glide.with(getApplicationContext()).load(usr.getImageUrl()).into(img_pro);
                username.setText(usr.getUsr());
                publisher.setText(usr.getUsr());
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
       // avoid=new int[followingList.size()];
    }
    private void readPosts()
    {
        //pList=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    String userid=snapshot.child("userid").getValue().toString();

                    for(String id: followingList)
                    {
                        if(userid.equals(id))
                        {
                            String postimg = snapshot.child("postimage").getValue().toString();

                        }
                    }

                }
                //postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    private void postcount()
    {
        DatabaseReference df=FirebaseDatabase.getInstance().getReference().child("Posts");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                post_cnt++;
                }
                avoid=new int[post_cnt];
               // Toast.makeText(HomePage.this,"In fun : "+String.valueOf(post_cnt), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Toast.makeText(this, String.valueOf(post_cnt), Toast.LENGTH_SHORT).show();
    }
    private int search(String id)
    {
        for(int j=0;j<followingList.size();j++)
        {
            if(id.equals(followingList.get(j))) {
                //Toast.makeText(this, id+" returns", Toast.LENGTH_SHORT).show();
                return 1;
            }
        }
        //Toast.makeText(this, id+"-1", Toast.LENGTH_SHORT).show();
        return -1;
    }

    private int searchpost(String id)
    {
        for(int j=0;j<pList.size();j++)
        {
            if(id.equals(pList.get(j))) {
                Toast.makeText(this, id+" returns", Toast.LENGTH_SHORT).show();
                return 1;
            }
        }
        //Toast.makeText(this, id+"-1", Toast.LENGTH_SHORT).show();
        return -1;
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

    @Override
    public void onPause() {
        super.onPause();
        // Save ListView state @ onPause
        state = layoutManager.onSaveInstanceState();
        Toast.makeText(this, "next", Toast.LENGTH_SHORT).show();

    }

    private void ViewPost()
    {
        checkFollowing();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");

        //Query sort=ref.orderByChild("counter");
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(ref,Post.class)
                .build();

       // Toast.makeText(this, options.toString(), Toast.LENGTH_SHORT).show();

        adapter = new FirebaseRecyclerAdapter<Post, postViewHolder>(options) {


            @Override
            public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
                //Toast.makeText(HomePage.this, "hii", Toast.LENGTH_SHORT).show();
                return new postViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final postViewHolder Holder, final int i, @NonNull Post post) {

                DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Posts");

                String uid1=getRef(i).getKey();
               // Toast.makeText(HomePage.this, uid1, Toast.LENGTH_SHORT).show();
                ref1.child(uid1).addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                       final String userid = dataSnapshot.child("userid").getValue().toString();
                                                       final String postid=dataSnapshot.child("postid").getValue().toString();
                                                  // Toast.makeText(HomePage.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                                                       //Toast.makeText(HomePage.this, postid, Toast.LENGTH_SHORT).show();
                                                       if (search(userid)!=1 && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                                           //avoid[i]=1;
                                                           Holder.layout.setVisibility(View.GONE);
                                                           //Toast.makeText(HomePage.this, userid, Toast.LENGTH_SHORT).show();
                                                       }

                                                           //Holder.layout.setVisibility(View.VISIBLE);
                                                       //String id="2rh1MZJzV0dmpad4QtDq1tKJw1o1";
                                                       //Toast.makeText(HomePage.this, id, Toast.LENGTH_SHORT).show();
                                                           //
                                                      /* if (!userid.equals(id)) {

                                                           }*/

                                                      // if(userid.equals(id) || userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                                   if((search(userid)==1 ||userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))){

                                                       //pList.add(postid);
                                                        avoid[i]=2;
                                                           Holder.layout.setVisibility(View.VISIBLE);
                                                           Holder.comment.setVisibility(View.VISIBLE);
                                                           Holder.like.setVisibility(View.VISIBLE);
                                                           Holder.save.setVisibility(View.VISIBLE);
                                                            Holder.com_line.setVisibility(View.VISIBLE);
                                                           //recyclerView.setEnabled(true);
                                                          // Holder.layout.setVisibility(View.VISIBLE);
                                                           final String postimg = dataSnapshot.child("postimage").getValue().toString();
                                                           final String caption = dataSnapshot.child("caption").getValue().toString();
                                                          // Toast.makeText(HomePage.this, userid+" : "+caption.toString(), Toast.LENGTH_SHORT).show();

                                                           DatabaseReference ref2=FirebaseDatabase.getInstance().getReference("User");
                                                           final String uid=userid;
                                                           //Toast.makeText(HomePage.this, ref2.toString(), Toast.LENGTH_SHORT).show();
                                                           ref2.child(uid).addValueEventListener(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


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

                                                                   Holder.comment.setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View view) {
                                                                           Intent i=new Intent(HomePage.this,CommentActivity.class);
                                                                           i.putExtra("postid",postid);
                                                                           i.putExtra("publisherid",userid);
                                                                           finish();
                                                                           startActivity(i);
                                                                       }
                                                                   });

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
                                                                           Intent i=new Intent(HomePage.this,CommentActivity.class);
                                                                           i.putExtra("postid",postid);
                                                                           i.putExtra("publisherid",userid);
                                                                           finish();
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
                                                       //} if

                                                   }
                                              // }

                                               @Override
                                               public void onCancelled(@NonNull DatabaseError databaseError) {

                                               }
                });

            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onBackPressed() {
        finish();
        //super.onBackPressed();
    }
}

