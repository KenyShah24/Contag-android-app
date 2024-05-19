package com.example.myapp.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapp.Data;
import com.example.myapp.Model.Post;
import com.example.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public Context mcontext;
    public List<Post> mpost;
    private FirebaseUser f_user;

    public PostAdapter(Context mcontext, List<Post> mpost) {
        this.mcontext = mcontext;
        this.mpost = mpost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.activity_display_post,parent,false);
        return new PostAdapter.ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        f_user= FirebaseAuth.getInstance().getCurrentUser();
        Post post=mpost.get(i);

        Glide.with(mcontext).load(post.getPostimage()).into(holder.post_img);
        if(post.getDescription().equals("")){
            holder.caption.setVisibility(View.GONE);
        }
        else
        {
            holder.caption.setVisibility(View.VISIBLE);
            holder.caption.setText(post.getDescription());
        }
        publisherInfo(holder.img_pro,holder.username,holder.user,post.getPublisher());
    }

    @Override
    public int getItemCount() {
        return mpost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView img_pro,post_img,like,comment,save;
        public TextView username,likes,user,caption,comments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_pro=itemView.findViewById(R.id.image_profile);
            post_img=itemView.findViewById(R.id.post_image);
            like=itemView.findViewById(R.id.like);
            comment=itemView.findViewById(R.id.comment);
            save=itemView.findViewById(R.id.save);
            username=itemView.findViewById(R.id.username);
            likes=itemView.findViewById(R.id.likes);
            user=itemView.findViewById(R.id.publisher);
            caption=itemView.findViewById(R.id.description);
            comments=itemView.findViewById(R.id.comments);



        }


    }

    private void publisherInfo(final ImageView img_pro, final TextView username, final TextView publisher, String userid)
    {
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("User").child(userid);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Data usr=dataSnapshot.getValue(Data.class);
                Glide.with(mcontext).load(usr.getImageUrl()).into(img_pro);
                username.setText(usr.getUsr());
                publisher.setText(usr.getUsr());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
