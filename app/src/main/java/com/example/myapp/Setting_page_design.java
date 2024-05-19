package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Setting_page_design extends AppCompatActivity {
    ImageView back;
    BottomNavigationView bnav;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private SectionStatePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page_design);
       // Toast.makeText(this, "Start Account Setting Page", Toast.LENGTH_SHORT).show();
       // mViewPager=(ViewPager)findViewById(R.id.container);
        mRelativeLayout=(RelativeLayout)findViewById(R.id.relLayout);
        mViewPager=(ViewPager)findViewById(R.id.centerpage);
        setupSettingList();
        setupFragments();

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Setting_page_design.this,ProfilePage.class);
                startActivity(intent);
            }
        });
    }

    private void setupFragments()
    {
        pagerAdapter=new SectionStatePagerAdapter(getSupportFragmentManager());

         pagerAdapter.addFragment(new fragment_setting_login_information(),getString(R.string.Saved_Login_Info));
        pagerAdapter.addFragment(new fragment_setting_block_account(),getString(R.string.Block_Account));
          pagerAdapter.addFragment(new fragment_setting_history(),getString(R.string.Search_History));
          pagerAdapter.addFragment(new fragment_setting_notification(),getString(R.string.Follow_following_notification));
         pagerAdapter.addFragment(new fragment_setting_account_privacy(),getString(R.string.Account_Privacy));
         pagerAdapter.addFragment(new fragment_setting_close_friends(),getString(R.string.Close_Friends));
           pagerAdapter.addFragment(new fragment_setting_contact_sync(),getString(R.string.Contact_Syncing));
          pagerAdapter.addFragment(new fragment_setting_post_like(),getString(R.string.Posts_liked));
         pagerAdapter.addFragment(new fragment_setting_saved(),getString(R.string.saved));
         pagerAdapter.addFragment(new fragment_setting_logout(),getString(R.string.Logout));
          pagerAdapter.addFragment(new fragment_setting_about_us(),getString(R.string.About));
         pagerAdapter.addFragment(new fragment_setting_comment(),getString(R.string.comment));
         pagerAdapter.addFragment(new fragment_setting_help(),getString(R.string.Help));
          pagerAdapter.addFragment(new fragment_setting_tagged_post(),getString(R.string.Tagged_Posts));
    }

    private void setViewPager(int fragmentNumber)
    {
        mRelativeLayout.setVisibility(View.GONE);
       // Toast.makeText(this, "Navigation Fragment", Toast.LENGTH_SHORT).show();
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(fragmentNumber);
    }

    private void setupSettingList() {
      //  Toast.makeText(this, "Account Setting List", Toast.LENGTH_SHORT).show();
        ListView listView = findViewById(R.id.lvAccountSetting);
        ArrayList<String> options = new ArrayList<>();
       /* List<Integer> icon=new ArrayList<>();
        icon.add(R.drawable.ic_addimage);
        icon.add(R.drawable.ic_file);*/
        options.add(getString(R.string.Saved_Login_Info));
        options.add(getString(R.string.Block_Account));
        options.add(getString(R.string.Search_History));
        options.add(getString(R.string.Follow_following_notification));
        options.add(getString(R.string.Account_Privacy));

        options.add(getString(R.string.Close_Friends));
        options.add(getString(R.string.Contact_Syncing));
        options.add(getString(R.string.Posts_liked));
        options.add(getString(R.string.saved));
        options.add(getString(R.string.Logout));
        options.add(getString(R.string.About));
        options.add(getString(R.string.comment));
        options.add(getString(R.string.Help));
        options.add(getString(R.string.Tagged_Posts));

        ArrayAdapter adapter = new ArrayAdapter(Setting_page_design.this, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);


       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(Setting_page_design.this, "Item Click", Toast.LENGTH_SHORT).show();


                setViewPager(position);
            }
        });
    }

/*


    private BottomNavigationView.OnNavigationItemSelectedListener navlist=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.navigation_home:
                            Intent i2=new Intent(Setting_page_design.this,HomePage.class);
                            finish();
                            startActivity(i2);
                            break;
                        case R.id.navigation_addimage:
                            Intent i=new Intent(Setting_page_design.this,PostPage.class);
                            finish();
                            startActivity(i);
                            break;

                        case R.id.navigation_setting:
                            Intent i1=new Intent(Setting_page_design.this,ProfilePage.class);
                            finish();
                            startActivity(i1);
                            break;

                    }
                    return false;
                }
            };*/



}
