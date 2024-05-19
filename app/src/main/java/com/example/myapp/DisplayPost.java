package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapp.Model.Post;
import com.example.myapp.Model.SearchUser;
import com.example.myapp.ViewHolder.dataViewHolder;
import com.example.myapp.ViewHolder.postViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DisplayPost extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Post, postViewHolder> adapter;
    View vi;
    FirebaseDatabase dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_post);

/*        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<SearchUser>()
                .setQuery(,SearchUser.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<Post, postViewHolder>(options) {


            @Override
            public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
                return new postViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull postViewHolder Holder, int i, @NonNull Post post) {

                Glide.with(getApplicationContext()).load(post.getPostimage()).into(Holder.postimg);
                if(post.getDescription().equals("")){
                    Holder.caption.setVisibility(View.GONE);
                }
                else
                {
                    Holder.caption.setVisibility(View.VISIBLE);
                    Holder.caption.setText(post.getDescription());
                }
                publisherInfo(Holder.postimg,Holder.username,Holder.publisher,post.getPublisher());

            }


        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);*/


    }
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
}
