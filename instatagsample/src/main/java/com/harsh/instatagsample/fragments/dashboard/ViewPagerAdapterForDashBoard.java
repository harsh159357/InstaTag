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

package com.harsh.instatagsample.fragments.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.harsh.instatagsample.fragments.DashBoardFragment;

import java.util.ArrayList;

public class ViewPagerAdapterForDashBoard extends FragmentStatePagerAdapter {
    private ArrayList<String> dashBoardFragments = new ArrayList<>();
    private DashBoardFragment dashBoardFragment;

    public ViewPagerAdapterForDashBoard(FragmentManager fm, DashBoardFragment dashBoardFragment) {
        super(fm);
        dashBoardFragments.add(ViewPagerFragmentForDashBoard.DashBoardFragments.HOME);
        dashBoardFragments.add(ViewPagerFragmentForDashBoard.DashBoardFragments.TAG_PHOTO);
        dashBoardFragments.add(ViewPagerFragmentForDashBoard.DashBoardFragments.SEARCH);
        this.dashBoardFragment = dashBoardFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return ViewPagerFragmentForDashBoard
                .newInstance(dashBoardFragments.get(position), dashBoardFragment);
    }

    @Override
    public int getCount() {
        return dashBoardFragments.size();
    }

    public ArrayList<String> getDashBoardFragments() {
        return dashBoardFragments;
    }

    public void setDashBoardFragments(ArrayList<String> dashBoardFragments) {
        this.dashBoardFragments = dashBoardFragments;
    }
}
