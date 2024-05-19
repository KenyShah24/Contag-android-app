package com.example.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.example.myapp.Model.Post;
import com.example.myapp.ViewHolder.PostAdapter;
import com.example.myapp.ViewHolder.postViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
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
import com.onesignal.OneSignal;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Parcelable state;
    int cnt=0;
    CircleImageView dp;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home);




        auth1=FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.result_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        if(state!=null) {
            layoutManager.onRestoreInstanceState(state);
            //Toast.makeText(this, "restore", Toast.LENGTH_SHORT).show();
        }

        pList=new ArrayList<>();

        postcount();

        // Toast.makeText(HomePage.this,"now  "+String.valueOf(post_cnt), Toast.LENGTH_SHORT).show();

        ViewPost();


        bnav=findViewById(R.id.nav_view1);
        bnav.setOnNavigationItemSelectedListener(navlist);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        dbref= FirebaseDatabase.getInstance().getReference("User").child(auth1.getUid());
        hview= (View) navigationView.getHeaderView(0);
        name=hview.findViewById(R.id.name);
        dp=hview.findViewById(R.id.dp);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url=dataSnapshot.child("imageUrl").getValue().toString();
                Glide.with(getApplicationContext()).load(url).into(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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

    private BottomNavigationView.OnNavigationItemSelectedListener navlist=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.navigation_home:
                            Intent i2=new Intent(MyHome.this,MyHome.class);
                           // finish();
                            startActivity(i2);
                            break;
                        case R.id.navigation_addimage:
                            Intent i=new Intent(MyHome.this,PostPage.class);
                           // finish();
                            startActivity(i);
                            break;

                        case R.id.navigation_setting:
                            Intent i1=new Intent(MyHome.this,ProfilePage.class);
                            i1.putExtra("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            //finish();
                            startActivity(i1);
                            break;

                    }
                    return false;
                }
    };

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.title_Search) {
            Intent in=new Intent(MyHome.this,SearchPage.class);
            startActivity(in);
           // finish();
            // Handle the camera action
        } else if (id == R.id.title_Memories) {

            Intent in2=new Intent(MyHome.this,SavedPost.class);
            startActivity(in2);
            //finish();
        } else if (id == R.id.title_Notification) {
            Intent in2=new Intent(MyHome.this,BellActivity.class);
            startActivity(in2);

        } else if (id == R.id.title_logout) {

            OneSignal.setSubscription(false);
            auth1.signOut();
            finish();
            Intent in1=new Intent(MyHome.this,Login.class);
            startActivity(in1);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    private void isLikes(final String postid, final ImageView imgview, final String userid)
    {
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imgview.setImageResource(R.drawable.ic_liked);
                    imgview.setTag("liked");
                   // addNotification(userid,postid);
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
       // Toast.makeText(this, "next", Toast.LENGTH_SHORT).show();

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
          //  popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
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
                        Toast.makeText(MyHome.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                      /*  Intent in1 = new Intent(MyHome.this, MyHome.class);
                       // adapter.notifyDataSetChanged();
                        finish();
                        startActivity(in1);*/
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
                Toast.makeText(MyHome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNotification(final String userid, String postid)
    {
        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        final String notify_id=ref.push().getKey();
        HashMap<String,Object> h=new HashMap<>();
        h.put("userid",fuser.getUid());
        h.put("text","liked your post");
        h.put("postid",postid);
        h.put("ispost",true);
        ref.child(notify_id).setValue(h);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String notify_key=dataSnapshot.child("notification_key").getValue().toString();
                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name=dataSnapshot.child("usr").getValue().toString();
                       // Toast.makeText(MyHome.this, "hii", Toast.LENGTH_SHORT).show();
                        if(!userid.equals(fuser.getUid()))
                        new SendNotification(name+" liked your post","Contag",notify_key);
                        //Toast.makeText(MyHome.this, "hello "+notify_key, Toast.LENGTH_SHORT).show();
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

                        if(!dataSnapshot.exists())
                        {
                            return;
                        }
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

                                    isLikes(postid,Holder.like,uid);
                                    nrLikes(Holder.likes,postid);
                                    getComments(postid,Holder.com_line);
                                    isSaved(postid,Holder.save);

                                    Holder.comment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i=new Intent(MyHome.this,CommentActivity.class);
                                            i.putExtra("postid",postid);
                                            i.putExtra("publisherid",userid);
                                            //finish();
                                            startActivity(i);
                                        }
                                    });

                                    Holder.like.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(Holder.like.getTag().equals("like")){
                                                FirebaseDatabase.getInstance().getReference().child("Likes")
                                                        .child(postid).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                                                //Toast.makeText(MyHome.this, userid+"\n"+postid, Toast.LENGTH_SHORT).show();
                                                addNotification(userid,postid);

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
                                            Intent i=new Intent(MyHome.this,CommentActivity.class);
                                            i.putExtra("postid",postid);
                                            i.putExtra("publisherid",userid);
                                            //finish();
                                            startActivity(i);
                                        }
                                    });
                                    Holder.dots.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ShowMoreOpt(Holder.dots,userid,auth1.getCurrentUser().getUid(),postid,postimg);
                                            //finish();
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


                        //on
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

}
