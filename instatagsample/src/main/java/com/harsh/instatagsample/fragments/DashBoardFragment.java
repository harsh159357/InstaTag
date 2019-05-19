/*
 * Copyright 2019 Harsh Sharma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.harsh.instatagsample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.harsh.instatagsample.R;
import com.harsh.instatagsample.activities.DashBoardActivity;
import com.harsh.instatagsample.fragments.dashboard.ViewPagerAdapterForDashBoard;
import com.harsh.instatagsample.interfaces.AppConstants;
import com.harsh.instatagsample.utilities.CustomViewPager;

public class DashBoardFragment extends Fragment implements AppConstants {
    BottomNavigationView bottomNavigationView;
    CustomViewPager customViewPager;

    private View rootView;
    private DashBoardActivity dashBoardActivityContext;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_home:
                    customViewPager.setCurrentItem(0, true);
                    break;
                case R.id.tab_tag_photo:
                    customViewPager.setCurrentItem(1, true);
                    break;
                case R.id.tab_search:
                    customViewPager.setCurrentItem(2, true);
                    break;
            }
            return true;
        }
    };
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            bottomNavigationView.getMenu().getItem(position).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dash_board, container, false);
        bottomNavigationView = rootView.findViewById(R.id.bottom_navigation_view);
        customViewPager = rootView.findViewById(R.id.dashboard_pager);
        initView();
        return rootView;
    }

    private void initView() {
        customViewPager.setAdapter(new ViewPagerAdapterForDashBoard(dashBoardActivityContext
                .getSupportFragmentManager(), this));
        customViewPager.setPagingEnabled(true);
        customViewPager.addOnPageChangeListener(onPageChangeListener);
        customViewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dashBoardActivityContext = (DashBoardActivity) context;
    }

    public void setHomeAsSelectedTab() {
        bottomNavigationView.setSelectedItemId(R.id.tab_home);
    }

}
