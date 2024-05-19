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
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
public class fragment_setting_about_us extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_about_us, container, false);
        ImageView back=view.findViewById( R.id.back );
        Button b1=view.findViewById( R.id.button1 );
        ToggleButton t1=view.findViewById( R.id.togglebutton );
        Switch night_mode_always = (Switch)view.findViewById(R.id.onoff);
        night_mode_always.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getActivity(), "TEST", Toast.LENGTH_LONG).show();
            }
        });
        t1.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText( getActivity(), "on", Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText( getActivity(), "off", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
        b1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getActivity(), "click", Toast.LENGTH_SHORT ).show();
            }
        } );
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( getActivity(),Setting_page_design.class );
                startActivity( intent );
            }
        } );

        return view;
    }
}
