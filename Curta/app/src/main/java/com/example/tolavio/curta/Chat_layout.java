package com.example.tolavio.curta;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Chat_layout extends AppCompatActivity {

    TabLayout tabLayout_chat;
    ViewPager viewPager;
    ViewPagerAdapterChat viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);

        tabLayout_chat = (TabLayout)findViewById(R.id.tabLayout_chat);
        viewPager = (ViewPager)findViewById(R.id.viewPager_chat);
        viewPagerAdapter = new ViewPagerAdapterChat(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new ChamadasFragment());
        viewPagerAdapter.addFragments(new ConversasFragment());
        viewPagerAdapter.addFragments(new ContactosFragment());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout_chat.setupWithViewPager(viewPager);
        tabLayout_chat.getTabAt(0).setIcon(R.drawable.ic_call_black_24dp);
        tabLayout_chat.getTabAt(1).setIcon(R.drawable.ic_chat_black_24dp);
        tabLayout_chat.getTabAt(2).setIcon(R.drawable.ic_contacts_black_24dp);
    }

}
