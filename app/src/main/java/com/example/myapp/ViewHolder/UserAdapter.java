package com.example.myapp.ViewHolder;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapp.Data;
import com.example.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mcontext;
    private List<Data> mUsers;
    private FirebaseUser fbuser;
    private DatabaseReference dbref;

    public UserAdapter(Context mcontext, List<Data> mUsers) {
        this.mcontext = mcontext;
        this.mUsers = mUsers;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.user_layout,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

        dbref=FirebaseDatabase.getInstance().getReference("User");
        fbuser= FirebaseAuth.getInstance().getCurrentUser();
        final Data user=mUsers.get(i);

        holder.follow.setVisibility(View.VISIBLE);
        holder.username.setText(user.getUsr());
        Glide.with(mcontext).load(user.getImageUrl()).into(holder.imgview);

        isFollowing(user.getId(),holder.follow);
        if(user.getId().equals(fbuser.getUid()))
        {
            holder.follow.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=mcontext.getSharedPreferences("PREPS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",user.getId());


            }
        });

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.follow.getText().toString().equals("follow"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(fbuser.getUid()).child("following").child(user.getId()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).child("followers").child(fbuser.getUid()).setValue(true);

                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(fbuser.getUid()).child("following").child(user.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).child("followers").child(fbuser.getUid()).removeValue();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView imgview;
        public Button follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.name_txt);
            imgview = itemView.findViewById(R.id.pro_img);
            follow = itemView.findViewById(R.id.follow);
        }


    }
    private void isFollowing(final String usid, final Button button) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(fbuser.getUid()).child("following");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(usid).exists()) {
                    button.setText("following");
                } else {
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
