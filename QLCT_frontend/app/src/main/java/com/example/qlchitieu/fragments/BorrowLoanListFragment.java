package com.example.qlchitieu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.qlchitieu.MainActivity;
import com.example.qlchitieu.R;
import com.example.qlchitieu.customs.ViewPagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class BorrowLoanListFragment extends Fragment {
    private TabLayout tabLayout;
    private TabItem tab_borrow, tab_loan;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    MainActivity activity;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_loan_borrow, container, false);
        activity = (MainActivity) getActivity();
        tabLayout = view.findViewById(R.id.tab_layout);
        tab_borrow = view.findViewById(R.id.borrow_tab);
        tab_loan = view.findViewById(R.id.loan_tab);
        viewPager = view.findViewById(R.id.view_page);


        adapter = new ViewPagerAdapter(getFragmentManager(),tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
    //    tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    adapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 1){
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;
    }
}
