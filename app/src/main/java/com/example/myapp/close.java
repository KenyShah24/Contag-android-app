package com.example.myapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Model.Post;
import com.example.myapp.Model.SearchUser;
import com.example.myapp.ViewHolder.PhotoHolder;
import com.example.myapp.ViewHolder.dataViewHolder;
import com.example.myapp.ViewHolder.postViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class close extends AppCompatActivity {
    List<String> close_friends;
    RecyclerView recyclerView;
    String name,url;
    FirebaseRecyclerAdapter<Post, PhotoHolder> adapter;
    FirebaseRecyclerAdapter<Post, postViewHolder> adapter1;
    DatabaseReference ref;
    List<String> followingList;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_close_friends_page );
        close_friends=new ArrayList<>();
        close_friends();
    }
    private String getid(int i)
    {
        return close_friends.get(i);
    }

    private int search(String id)
    {
        for(int j=0;j<close_friends.size();j++)
        {
            if(id.equals(close_friends.get(j))) {
                //Toast.makeText(this, id+" returns", Toast.LENGTH_SHORT).show();
                return 1;
            }
        }
        //Toast.makeText(this, id+"-1", Toast.LENGTH_SHORT).show();
        return -1;
    }
    private void close_friends()
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Friend")
                .child( FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    close_friends.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
