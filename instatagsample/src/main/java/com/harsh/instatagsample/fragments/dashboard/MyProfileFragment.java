/*
 * Copyright 2017 Harsh Sharma
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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.harsh.instatagsample.R;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

public class MyProfileFragment extends Fragment {
    private FrameLayout frameLayout;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_profile,
                container, false);
        frameLayout = rootView.findViewById(R.id.my_profile);
        myProfile();
        return rootView;
    }

    public void myProfile() {
        AboutBuilder builder = AboutBuilder.with(getActivity())
/*
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
*/
                .setPhoto(R.drawable.profile_picture_instatag)
                .setCover(R.drawable.profile_cover_instatag)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setName(getString(R.string.name))
                .setSubTitle(getString(R.string.sub_title))
                .setLinksColumnsCount(4)
                .setBrief(getString(R.string.brief))
                .addGitHubLink(getString(R.string.github_link))
                .addFacebookLink(getString(R.string.facebook_link))
                .addTwitterLink(getString(R.string.twitter_link))
                .addInstagramLink(getString(R.string.instagram_link))
                .addGooglePlusLink(getString(R.string.google_plus_link))
                .addLinkedInLink(getString(R.string.linkedin_link))
                .addEmailLink(getString(R.string.email_link))
                .addWhatsappLink(getString(R.string.name), getString(R.string.whatsapp))
                .addFeedbackAction(getString(R.string.email_link))
                .setWrapScrollView(true)
                .setShowAsCard(true);

        AboutView view = builder.build();
        frameLayout.addView(view);
    }


}
