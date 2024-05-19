package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.List;

public class SearchPage extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<SearchUser, dataViewHolder> adapter;
    View vi;
    FirebaseDatabase dbref;
    DatabaseReference ref,uref,uref1,uref2;
    ImageButton mSearchBtn;
    private EditText mSearchField;
   // Button follow;
    FirebaseUser fuser;
    String sender_uid;
    Context mcontext;
    private List<Data> mUsers;
    private DatabaseReference chatref,notifyref;
    int it=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        chatref=FirebaseDatabase.getInstance().getReference().child("Request");
        notifyref=FirebaseDatabase.getInstance().getReference().child("Notifications");
        sender_uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbref= FirebaseDatabase.getInstance();
        ref=dbref.getReference().child("User");
        uref=FirebaseDatabase.getInstance().getReference().child("User");
        mSearchBtn = (ImageButton) findViewById(R.id.searchbtn);
        mSearchField = findViewById(R.id.search_field);
        recyclerView=findViewById(R.id.result_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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

        finish();
       }
    private void showlist(String searchtxt)
    {

        Query fsearchquery=ref.orderByChild("usr").startAt(searchtxt).endAt(searchtxt+"\uf8ff");
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
                uref.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                           String proimg = dataSnapshot.child("imageUrl").getValue().toString();
                            String usrname = dataSnapshot.child("usr").getValue().toString();

                            Holder.rlay.setVisibility(View.VISIBLE);
                        Holder.usrtxt.setVisibility(View.VISIBLE);
                        Holder.follow.setVisibility(View.VISIBLE);
                        Holder.imgtxt.setVisibility(View.VISIBLE);
                            Holder.usrtxt.setText(usrname);
                            Glide.with(getApplicationContext()).load(proimg).into(Holder.imgtxt);
                            if(uid.equals(fuser.getUid()))
                            {
                                //Holder.follow.setVisibility(View.VISIBLE);
                                 Holder.follow.setVisibility(View.GONE);
                            }
                            else
                            {
                                 Holder.follow.setVisibility(View.VISIBLE);
                            }

                       isFollowing(uid,Holder.follow);
                        Holder.follow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(Holder.follow.getText().toString().equals("FOLLOW"))
                                {
                                 //   Toast.makeText(SearchPage.this, uid, Toast.LENGTH_SHORT).show();
                                    uref1=FirebaseDatabase.getInstance().getReference().child("Follow").child(fuser.getUid())
                                            .child("following").child(uid);
                                    uref1.setValue(true);
                                    uref2=FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("followers").child(fuser.getUid());
                                    uref2.setValue(true);

                                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User").child(uid);
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final String notify=dataSnapshot.child("notification_key").getValue().toString();
                                           // String name=dataSnapshot.child("usr").getValue().toString();
                                            DatabaseReference refe=FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());
                                            refe.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String name=dataSnapshot.child("usr").getValue().toString();
                                                    new SendNotification(name+" started following you","Contag",notify);
                                                    addNotification(uid);
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
                                else
                                {
                                    FirebaseDatabase.getInstance().getReference().child("Follow").child(fuser.getUid()).child("following").child(uid).removeValue();

                                    FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("followers").child(fuser.getUid()).removeValue();

                                }

                            }
                        });

                        Holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(SearchPage.this,ProfilePage.class);
                                intent.putExtra("id",uid);
                                startActivity(intent);
                            }
                        });

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
    }
    private void addNotification(String userid)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        String notify_id=ref.push().getKey();
        HashMap<String,Object> h=new HashMap<>();
        h.put("userid",fuser.getUid());
        h.put("text","started following you");
        h.put("postid","");
        h.put("ispost",false);
        ref.child(notify_id).setValue(h);
    }
    private void isFollowing(final String usid, final Button button)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(fuser.getUid()).child("following");
       // Toast.makeText(SearchPage.this, usid, Toast.LENGTH_SHORT).show();
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

    private void sendRequest(final String receiver_uid, final Button button)
    {
        chatref.child(sender_uid).child(receiver_uid).child("req_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    chatref.child(receiver_uid).child(sender_uid).child("req_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                HashMap<String,String> chatnotification=new HashMap<>();
                                chatnotification.put("from",sender_uid);
                                chatnotification.put("type","request");

                                notifyref.child(receiver_uid).push().setValue(chatnotification).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            button.setEnabled(true);
                                            button.setText("Requested");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

}
