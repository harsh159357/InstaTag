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

package com.harsh.instatagsample.utilities;

import android.util.SparseArray;

import com.harsh.instatagsample.models.SomeOne;

import java.util.ArrayList;

public class SomeOneData {
    private static final ArrayList<SomeOne> sSomeOneArrayList = new ArrayList<>();
    private static final SparseArray<SomeOne> sSparseArrayOfSomeOne = new SparseArray<>();

    static {
        sSomeOneArrayList.add(new SomeOne("Alex", "Alexander Graham Bell", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Alexander_Graham_Bell_in_colors-1-.jpg/220px-Alexander_Graham_Bell_in_colors-1-.jpg"));
        sSomeOneArrayList.add(new SomeOne("Beeth", "Beethoven", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Beethoven.jpg/220px-Beethoven.jpg"));
        sSomeOneArrayList.add(new SomeOne("Charles", "Charles Dickens", "http://i2.mirror.co.uk/incoming/article1506690.ece/ALTERNATES/s615b/Illustration%20of%20Bob%20Cratchit"));
        sSomeOneArrayList.add(new SomeOne("Backy", "David Beckham", "http://static.independent.co.uk/s3fs-public/styles/story_large/public/thumbnails/image/2015/12/20/17/7-Beckham-PA.jpg"));
        sSomeOneArrayList.add(new SomeOne("Edie", "Edward Smith", "http://static1.squarespace.com/static/5006453fe4b09ef2252ba068/t/51bba617e4b0b3d30b84e989/1371252249838/10418597-captain-edward-smith.jpg?format=500w"));
        sSomeOneArrayList.add(new SomeOne("Franklin", "Franklin De RooseVelt", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Gerald_Ford_-_NARA_-_530680.tif/lossy-page1-220px-Gerald_Ford_-_NARA_-_530680.tif.jpg"));
        sSomeOneArrayList.add(new SomeOne("Geraford", "Gerald Ford", "http://www.facts-about.org.uk/images/henry-ford.jpg"));
        sSomeOneArrayList.add(new SomeOne("Henrhoo", "Henrey Hoover", "http://www.reviewsbymary.com/wp-content/uploads/2015/06/henry-the-hoover-2.jpg"));
        sSomeOneArrayList.add(new SomeOne("Ian", "Ian Thorpe", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Ian_Thorpe_with_a_smile.jpg/250px-Ian_Thorpe_with_a_smile.jpg"));
        sSomeOneArrayList.add(new SomeOne("Jchan", "Jackie Chan", "http://static.tvgcdn.net/rovi/showcards/feed/87/thumbs/31298087_c357x476+182+0_140x187.jpg"));
        sSomeOneArrayList.add(new SomeOne("Kk", "Kishore Kumar", "http://1.bp.blogspot.com/-PY6__w87Tnw/UgfPVOTjazI/AAAAAAAAA6E/kqWSGELGRQw/s400/kishore-kumar.jpg"));
        sSomeOneArrayList.add(new SomeOne("MJ", "Michael JackSon", "https://s2.vagalume.com/michael-jackson/images/michael-jackson.jpg"));
        sSomeOneArrayList.add(new SomeOne("NeilA", "Neil Armstrong", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/Neil_Armstrong_pose.jpg/220px-Neil_Armstrong_pose.jpg"));
        sSomeOneArrayList.add(new SomeOne("Oscar", "Oscar Wilde", "http://media.gettyimages.com/photos/irish-playwright-novelist-and-wit-oscar-wilde-picture-id2641729"));
        sSomeOneArrayList.add(new SomeOne("Pope", "Pope Francis", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4d/Franciscus_in_2015.jpg/220px-Franciscus_in_2015.jpg"));
        sSomeOneArrayList.add(new SomeOne("Ronaldo", "Ronaldo", "http://www.telegraph.co.uk/content/dam/football/2016/05/28/cronaldo-large_trans++n2n2hk5qKEJ--A9z8HbLAj7qDM1k-FXUpc9UWB6g6Ho.jpg"));
        sSomeOneArrayList.add(new SomeOne("Schumak", "Schumakar", "http://i.dailymail.co.uk/i/pix/2016/02/04/16/0E450B9800000578-3431978-image-a-1_1454603246726.jpg"));
        sSomeOneArrayList.add(new SomeOne("Tiger", "Tiger Woods", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Tiger_Woods_with_a_fan_2014_cr.jpg/220px-Tiger_Woods_with_a_fan_2014_cr.jpg"));
        sSomeOneArrayList.add(new SomeOne("Ubolt", "Ussain Bolt", "http://cdn.images.express.co.uk/img/dynamic/4/590x/Bolt-601256.jpg"));
        sSomeOneArrayList.add(new SomeOne("Virat", "Virat Kohli", "http://ste.india.com/sites/default/files/styles/zm_700x400/public/2016/02/10/459174-virat-kohli-odis-sadnw-700.jpg?itok=qK3WNL0k"));
        sSomeOneArrayList.add(new SomeOne("WiilShake", "William Shakespeare", "http://shakespeare.mit.edu/shake.gif"));
        sSomeOneArrayList.add(new SomeOne("XaWoo", "Xavier Woods", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/Xavier_Woods_In_March_2015.jpg/1200px-Xavier_Woods_In_March_2015.jpg"));
        sSomeOneArrayList.add(new SomeOne("ZayMa", "Zayn Malik", "http://i1.mirror.co.uk/incoming/article7134812.ece/ALTERNATES/s615b/Zayn-Malik.jpg"));
        for (int i = 0; i < sSomeOneArrayList.size(); i++) {
            sSparseArrayOfSomeOne.put(i, sSomeOneArrayList.get(i));
        }
    }

    public static ArrayList<SomeOne> getDummySomeOneList() {
        return sSomeOneArrayList;
    }

    public static SomeOne giveSomeOne(int index) {
        SomeOne someOne = null;
        for (int i = 0; i < sSparseArrayOfSomeOne.size(); i++) {
            int key = sSparseArrayOfSomeOne.keyAt(i);
            if (key == index) {
                Object obj = sSparseArrayOfSomeOne.get(key);
                if (obj != null) {
                    someOne = (SomeOne) obj;
                }
                break;
            }
        }
        return someOne;
    }

    public static ArrayList<SomeOne> getFilteredUser(String searchString) {
        ArrayList<SomeOne> filteredUser = new ArrayList<>();
        for (SomeOne someOne : sSomeOneArrayList) {
            if (someOne.getFullName().contains(searchString) ||
                    someOne.getUserName().contains(searchString)) {
                filteredUser.add(someOne);
            }
        }
        if (filteredUser.isEmpty()) {
            filteredUser.add(new SomeOne("No Result Found",
                    "No Result Found", "No Result Found"));
        }
        return filteredUser;
    }

    public static SomeOne giveSomeOne(String someOneUserName) {
        SomeOne someOne = null;
        for (int i = 0; i < sSomeOneArrayList.size(); i++) {
            if (someOneUserName.equals(sSomeOneArrayList.get(i).getUserName())) {
                someOne = sSomeOneArrayList.get(i);
                break;
            }
        }
        return someOne;
    }

}
