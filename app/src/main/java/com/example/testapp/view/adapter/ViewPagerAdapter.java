package com.example.testapp.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * Created by User on 13/09/2020
 */
public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> fragmentList;

    public ViewPagerAdapter(@NonNull Fragment fragment,  List<Fragment> fragmentList) {
        super(fragment);
        this.fragmentList=fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }


}
