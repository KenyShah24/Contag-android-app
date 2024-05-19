package com.example.myapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapp.Model.Post;
import com.example.myapp.ViewHolder.PostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PostDetailActivity extends Fragment {

    String postid;
    private RecyclerView rv;
    private PostAdapter postAdapter;
    private List<Post> postList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_post_detail,container,false);

        SharedPreferences preferences=getContext().getSharedPreferences("PREPS",Context.MODE_PRIVATE);
        postid=preferences.getString("postid","none");

        rv=view.findViewById(R.id.recycle_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);

        postList=new ArrayList<>();
        postAdapter=new PostAdapter(getContext(),postList);
        rv.setAdapter(postAdapter);

        readPost();

        return view;
    }


    private void readPost()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts").child(postid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                Post post=dataSnapshot.getValue(Post.class);
                postList.add(post);

                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
