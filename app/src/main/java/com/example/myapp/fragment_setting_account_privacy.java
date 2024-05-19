package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fragment_setting_account_privacy extends Fragment {
    @Nullable

    FirebaseUser fuser;
    FirebaseDatabase dbref;
    DatabaseReference ref,uref,uref1,uref2;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_privacy, container, false);
        ImageView back=view.findViewById( R.id.back );
        final Switch s1 = (Switch)view.findViewById(R.id.onoff);
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        uref=FirebaseDatabase.getInstance().getReference().child("User");
        dbref= FirebaseDatabase.getInstance();
        ref=dbref.getReference().child("User");
        uref=FirebaseDatabase.getInstance().getReference().child("User");
        uref.child( fuser.getUid() ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String acc=dataSnapshot.child( "private" ).getValue().toString();
                if(acc.equals( "true" ))
                    s1.setChecked( true );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );




        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(s1.isChecked()) {
                    fuser= FirebaseAuth.getInstance().getCurrentUser();

                    uref1 = FirebaseDatabase.getInstance().getReference().child("User").child(fuser.getUid())
                            .child("private");
                    uref1.setValue(true);
                    Toast.makeText(getActivity(),"Account Is Private",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    fuser= FirebaseAuth.getInstance().getCurrentUser();
                    uref1 = FirebaseDatabase.getInstance().getReference().child("User").child(fuser.getUid())
                            .child("private");
                    uref1.setValue(false);
                    Toast.makeText(getActivity(),"Account Is Public",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });


        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText( getActivity(), "hello", Toast.LENGTH_SHORT ).show();

                Intent intent=new Intent( getActivity(),Setting_page_design.class );
                startActivity( intent );
            }
        } );

        return view;
    }
}
