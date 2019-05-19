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

package com.harsh.instatagsample.interfaces;

public interface AppConstants {

    int OFFSCREEN_PAGE_LIMIT = 3;
    int ADD_TAG_DELAY_MILLIS = 2000;
    int CONFIGURATION_DELAY_MILLIS = 2000;
    int CHOOSE_A_PHOTO_TO_BE_TAGGED = 5000;

    String NO_USER_FOUND = "No User Found";

    interface ProgressText {
        String PROGRESS_MSG = "Saving Tag...";
        String RELOADING = "Reloading...";
    }

    interface Animations {
        interface Show {
            String BOUNCE_DOWN = "Bounce Down";
            String FADE_IN = "Fade In";
            String SLIDE_DOWN = "Slide Down";
            String ZOOM_IN = "Zoom In";
        }

        interface Hide {
            String BOUNCE_UP = "Bounce Up";
            String FADE_OUT = "Fade Out";
            String SLIDE_UP = "Slide Up";
            String ZOOM_OUT = "Zoom Out";
        }
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
        String NEW_CONFIGURATION_SAVED = "NEW_CONFIGURATION_SAVED";
        String NEW_PHOTO_IS_TAGGED = "NEW_PHOTO_IS_TAGGED";
    }
}
