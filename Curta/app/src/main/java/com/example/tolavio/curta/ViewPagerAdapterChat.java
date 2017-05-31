package com.example.tolavio.curta;

import android.content.ClipData;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Tolavio on 19-11-2016.
 */

public class ViewPagerAdapterChat extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    //ArrayList<Integer> itens = new ArrayList<>();

    public void addFragments(Fragment fragments){

        this.fragments.add(fragments);
        //this.itens.add(itens);
    }

    public ViewPagerAdapterChat(FragmentManager fm){

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
