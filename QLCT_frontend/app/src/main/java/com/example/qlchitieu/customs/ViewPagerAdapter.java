package com.example.qlchitieu.customs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.qlchitieu.fragments.BorrowListFragment;
import com.example.qlchitieu.fragments.LoanListFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int numOfTab;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numOfTab = behavior;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new BorrowListFragment();
            case 1:
                return new LoanListFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTab;
    }

}
