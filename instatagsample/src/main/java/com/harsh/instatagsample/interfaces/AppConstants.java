package com.harsh.instatagsample.interfaces;

public interface AppConstants {

    int OFFSCREEN_PAGE_LIMIT = 3;
    int ADD_TAG_DELAY_MILLIS = 2000;
    int CHOOSE_A_PHOTO_TO_BE_TAGGED = 5000;

    String NO_USER_FOUND = "No Result Found";

    interface ProgressText {
        String PROGRESS_MSG = "Saving Tag";
    }

    interface ToastText {
        String CHOOSE_A_PHOTO = "Please choose a photo";
        String TAG_ONE_USER_AT_LEAST = "Please tag one user at least";
        String PHOTO_TAGGED_SUCCESSFULLY = "Photo tagged successfully";
    }

    interface PreferenceKeys {
        String TAGGED_PHOTOS = "TAGGED_PHOTOS";
    }

    interface Events {
        String NEW_PHOTO_IS_TAGGED = "NEW_PHOTO_IS_TAGGED";
    }
}
