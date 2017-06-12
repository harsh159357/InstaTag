package com.harsh.instatagsample;

import android.util.SparseArray;

import java.util.ArrayList;

public class SomeOnesData {
    private static ArrayList<SomeOne> someOneArrayList = new ArrayList<>();
    private static SparseArray<SomeOne> sparseArrayOfSomeOne = new SparseArray<>();

    static {
        someOneArrayList.add(new SomeOne("Alex", "Alexander Graham Bell", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Alexander_Graham_Bell_in_colors-1-.jpg/220px-Alexander_Graham_Bell_in_colors-1-.jpg"));
        someOneArrayList.add(new SomeOne("Beeth", "Beethoven", "http://a4.files.biography.com/image/upload/c_fit,cs_srgb,dpr_1.0,q_80,w_620/MTI2NTgyMzkyODM5MjE5ODQz.jpg"));
        someOneArrayList.add(new SomeOne("Charles", "Charles Dickens", "http://i2.mirror.co.uk/incoming/article1506690.ece/ALTERNATES/s615b/Illustration%20of%20Bob%20Cratchit"));
        someOneArrayList.add(new SomeOne("Backy", "David Beckham", "http://static.independent.co.uk/s3fs-public/styles/story_large/public/thumbnails/image/2015/12/20/17/7-Beckham-PA.jpg"));
        someOneArrayList.add(new SomeOne("Edie", "Edward Smith", "http://static1.squarespace.com/static/5006453fe4b09ef2252ba068/t/51bba617e4b0b3d30b84e989/1371252249838/10418597-captain-edward-smith.jpg?format=500w"));
        someOneArrayList.add(new SomeOne("Franklin", "Franklin De RooseVelt", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Gerald_Ford_-_NARA_-_530680.tif/lossy-page1-220px-Gerald_Ford_-_NARA_-_530680.tif.jpg"));
        someOneArrayList.add(new SomeOne("Geraford", "Gerald Ford", "http://www.facts-about.org.uk/images/henry-ford.jpg"));
        someOneArrayList.add(new SomeOne("Henrhoo", "Henrey Hoover", "http://www.reviewsbymary.com/wp-content/uploads/2015/06/henry-the-hoover-2.jpg"));
        someOneArrayList.add(new SomeOne("Ian", "Ian Thorpe", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Ian_Thorpe_with_a_smile.jpg/250px-Ian_Thorpe_with_a_smile.jpg"));
        someOneArrayList.add(new SomeOne("Jchan", "Jackie Chan", "http://static.tvgcdn.net/rovi/showcards/feed/87/thumbs/31298087_c357x476+182+0_140x187.jpg"));
        someOneArrayList.add(new SomeOne("Kk", "Kishore Kumar", "http://1.bp.blogspot.com/-PY6__w87Tnw/UgfPVOTjazI/AAAAAAAAA6E/kqWSGELGRQw/s400/kishore-kumar.jpg"));
        someOneArrayList.add(new SomeOne("MJ", "Michael JackSon", "https://s2.vagalume.com/michael-jackson/images/michael-jackson.jpg"));
        someOneArrayList.add(new SomeOne("NeilA", "Neil Armstrong", "https://en.wikipedia.org/wiki/File:Neil_Armstrong_pose.jpg"));
        someOneArrayList.add(new SomeOne("Oscar", "Oscar Wilde", "http://media.gettyimages.com/photos/irish-playwright-novelist-and-wit-oscar-wilde-picture-id2641729"));
        someOneArrayList.add(new SomeOne("Pope", "Pope Francis", "http://a2.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTE1ODA0OTcyMDMzNTQxNjQ1.jpg"));
        someOneArrayList.add(new SomeOne("Ronaldo", "Ronaldo", "http://www.telegraph.co.uk/content/dam/football/2016/05/28/cronaldo-large_trans++n2n2hk5qKEJ--A9z8HbLAj7qDM1k-FXUpc9UWB6g6Ho.jpg"));
        someOneArrayList.add(new SomeOne("Schumak", "Schumakar", "http://i.dailymail.co.uk/i/pix/2016/02/04/16/0E450B9800000578-3431978-image-a-1_1454603246726.jpg"));
        someOneArrayList.add(new SomeOne("Tiger", "Tiger Woods", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Tiger_Woods_with_a_fan_2014_cr.jpg/220px-Tiger_Woods_with_a_fan_2014_cr.jpg"));
        someOneArrayList.add(new SomeOne("Ubolt", "Ussain Bolt", "http://cdn.images.express.co.uk/img/dynamic/4/590x/Bolt-601256.jpg"));
        someOneArrayList.add(new SomeOne("Virat", "Virat Kohli", "http://ste.india.com/sites/default/files/styles/zm_700x400/public/2016/02/10/459174-virat-kohli-odis-sadnw-700.jpg?itok=qK3WNL0k"));
        someOneArrayList.add(new SomeOne("WiilShake", "William Shakespeare", "http://shakespeare.mit.edu/shake.gif"));
        someOneArrayList.add(new SomeOne("XaWoo", "Xavier Woods", "https://en.wikipedia.org/wiki/File:Xavier_Woods_In_March_2015.jpg"));
        someOneArrayList.add(new SomeOne("ZayMa", "Zayn Malik", "http://i1.mirror.co.uk/incoming/article7134812.ece/ALTERNATES/s615b/Zayn-Malik.jpg"));
        for (int i = 0; i < someOneArrayList.size(); i++) {
            sparseArrayOfSomeOne.put(i, someOneArrayList.get(i));
        }
    }

    public static ArrayList<SomeOne> getDummySomeOneList() {
        return someOneArrayList;
    }

    public static SomeOne giveSomeOne(int index) {
        SomeOne someOne = null;
        for (int i = 0; i < sparseArrayOfSomeOne.size(); i++) {
            int key = sparseArrayOfSomeOne.keyAt(i);
            if (key == index) {
                Object obj = sparseArrayOfSomeOne.get(key);
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
        for (SomeOne someOne : someOneArrayList) {
            if (someOne.getFullName().contains(searchString) ||
                    someOne.getUserName().contains(searchString)) {
                filteredUser.add(someOne);
            }
        }
        if (filteredUser.isEmpty()) {
            filteredUser.add(new SomeOne("No Result Found", "No Result Found", "No Result Found"));
        }
        return filteredUser;
    }

    public static SomeOne giveSomeOne(String someOneUserName) {
        SomeOne someOne = null;
        for (int i = 0; i < someOneArrayList.size(); i++) {
            if (someOneUserName.equals(someOneArrayList.get(i).getUserName())) {
                someOne = someOneArrayList.get(i);
                break;
            }
        }
        return someOne;
    }

}
