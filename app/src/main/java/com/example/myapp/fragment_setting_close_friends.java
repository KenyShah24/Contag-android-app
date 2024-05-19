package com.example.myapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_setting_close_friends extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.close_friend, container, false );
        ImageView close = view.findViewById( R.id.close );
        //ImageView list = view.findViewById( R.id.list );
        Button list=view.findViewById( R.id.list );
        close.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent( getActivity(),Setting_page_design.class );
                //startActivity( intent );
                getActivity().finish();
            }
        } );


        list.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( getActivity(),close_friends_page.class );
                startActivity( intent );
            }
        } );
        return view;
    }
}


