package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapp.Model.SearchUser;
import com.example.myapp.ViewHolder.dataViewHolder;
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

public class ShowFollowing extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<SearchUser, dataViewHolder> adapter,adapter1;
    View vi;
    FirebaseDatabase dbref;
    DatabaseReference ref,ref1,uref,uref1,uref2;
    ImageButton mSearchBtn;
    private EditText mSearchField;
    FirebaseUser fuser;
    String myid;
    private List<String> following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_following);

        dbref= FirebaseDatabase.getInstance();
        ref=dbref.getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        uref=FirebaseDatabase.getInstance().getReference().child("User");
        mSearchBtn = (ImageButton) findViewById(R.id.searchbtn);
        mSearchField = findViewById(R.id.search_here1);
        myid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref1=FirebaseDatabase.getInstance().getReference().child("User");

        recyclerView=findViewById(R.id.following_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        getFollowers();

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchtxt = mSearchField.getText().toString();
                showlist(searchtxt);

            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ShowFollowing.this, ProfilePage.class);
        intent.putExtra("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
        finish();
        startActivity(intent);
    }
    private void showlist(String searchtxt)
    {

        Query fsearchquery=ref1.orderByChild("usr").startAt(searchtxt).endAt(searchtxt+"\uf8ff");

        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<SearchUser>()
                .setQuery(fsearchquery,SearchUser.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<SearchUser, dataViewHolder>(options) {


            @Override
            public dataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout,parent,false);
                return new dataViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull final dataViewHolder Holder, int i, @NonNull SearchUser searchUser) {

                fuser= FirebaseAuth.getInstance().getCurrentUser();
                final String uid=getRef(i).getKey();
                if(searchlist(uid)!=1)
                {
                    Holder.rlay.setVisibility(View.GONE);
                }
               // Toast.makeText(ShowFollower.this, uid, Toast.LENGTH_SHORT).show();
                if(searchlist(uid)==1) {


                    Holder.rlay.setVisibility(View.VISIBLE);
                    Holder.imgtxt.setVisibility(View.VISIBLE);
                    Holder.usrtxt.setVisibility(View.VISIBLE);
                    Holder.follow.setVisibility(View.VISIBLE);
                    uref.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String proimg = dataSnapshot.child("imageUrl").getValue().toString();
                            String usrname = dataSnapshot.child("usr").getValue().toString();
                            Holder.usrtxt.setText(usrname);
                            Glide.with(getApplicationContext()).load(proimg).into(Holder.imgtxt);
                            if (uid.equals(fuser.getUid())) {
                                Holder.follow.setVisibility(View.GONE);
                            } else {
                                Holder.follow.setVisibility(View.VISIBLE);
                            }
                            isFollowing(uid, Holder.follow);
                            Holder.follow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (Holder.follow.getText().toString().equals("FOLLOW")) {
                                        uref1 = FirebaseDatabase.getInstance().getReference().child("Follow").child(fuser.getUid())
                                                .child("following").child(uid);
                                        uref1.setValue(true);

                                        uref2 = FirebaseDatabase.getInstance().getReference().child("Follow").
                                                child(uid).child("followers").child(fuser.getUid());
                                        uref2.setValue(true);
                                    } else {
                                        FirebaseDatabase.getInstance().getReference().child("Follow").
                                                child(fuser.getUid()).child("following").child(uid).removeValue();

                                        FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).
                                                child("followers").child(fuser.getUid()).removeValue();

                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


                Holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(ShowFollowing.this,ProfilePage.class);
                        intent.putExtra("id",uid);
                        startActivity(intent);
                    }
                });
            }

        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
    private void isFollowing(final String usid, final Button button)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(fuser.getUid()).child("following");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(usid).exists())
                {
                    button.setText("FOLLOWING");
                }
                else
                {
                    button.setText("FOLLOW");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private int searchlist(String id)
    {
        for(int j=0;j<following.size();j++)
        {
            if(id.equals(following.get(j))) {
                return 1;
            }
        }
        return -1;
    }

    private void getFollowers()
    {

        following=new ArrayList<>();
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Follow").child(myid).child("following");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    if(searchlist(snapshot.getKey())!=1) {
                        following.add(snapshot.getKey());
                        // Toast.makeText(ShowFollower.this, snapshot.getKey(), Toast.LENGTH_SHORT).show();
                    }
                    else
                        ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
