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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.harsh.instatagsample.R;
import com.harsh.instatagsample.adapters.UserAdapter;
import com.harsh.instatagsample.interfaces.UserClickListener;
import com.harsh.instatagsample.models.User;
import com.harsh.instatagsample.utilities.UsersData;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements UserClickListener {

    private RecyclerView recyclerViewUsers;
    private EditText searchForUser;
    private UserAdapter userAdapter;
    private final ArrayList<User> users = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerViewUsers = rootView.findViewById(R.id.rv_users);
        searchForUser = rootView.findViewById(R.id.search_for_a_person);
        searchForUser.addTextChangedListener(textWatcher);

        users.addAll(UsersData.getUsers());
        userAdapter = new UserAdapter(users, getActivity(), this);
        recyclerViewUsers.setAdapter(userAdapter);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (searchForUser.getText().toString().trim().equals("")) {
                users.clear();
                users.addAll(UsersData.getUsers());
                userAdapter.notifyDataSetChanged();
            } else {
                users.clear();
                users.addAll(UsersData.
                        getFilteredUsers(searchForUser.getText().toString().trim()));
                userAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onUserClick(final User user, int position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), user.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
