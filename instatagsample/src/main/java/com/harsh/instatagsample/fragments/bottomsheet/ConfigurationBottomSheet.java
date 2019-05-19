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

package com.harsh.instatagsample.fragments.bottomsheet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.harsh.instatagsample.InstaTagApplication;
import com.harsh.instatagsample.R;
import com.harsh.instatagsample.adapters.AnimationSpinnerAdapter;
import com.harsh.instatagsample.interfaces.AppConstants;
import com.harsh.instatagsample.models.Asset;

public class ConfigurationBottomSheet extends BottomSheetDialogFragment implements AppConstants, View.OnClickListener {

    private View rootView;

    Spinner spinnerTagShowAnimation, spinnerTagHideAnimation;

    Asset[] tagShowAssets, tagHideAssets;

    AnimationSpinnerAdapter tagShowAnimationSpinnerAdapter,
            tagHideAnimationSpinnerAdapter;

    AlertDialog carrotTopColorDialogPicker,
            tagBackgroundColorDialogPicker,
            tagTextColorDialogPicker,
            likeColorDialogPicker;

    TextView textViewCarrotTopColor,
            textViewTagBackgroundColor,
            textViewTagTextColor,
            textViewLikeColor;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog bottomSheetDialog1 = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet
                        = bottomSheetDialog1.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
                BottomSheetBehavior.from(bottomSheet).setHideable(true);
            }
        });

        return bottomSheetDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_sheet_configuration, container);

        spinnerTagShowAnimation = rootView.findViewById(R.id.spinner_tag_show_animation);
        spinnerTagHideAnimation = rootView.findViewById(R.id.spinner_tag_hide_animation);

        textViewCarrotTopColor = rootView.findViewById(R.id.tv_carrot_top_color);
        textViewTagBackgroundColor = rootView.findViewById(R.id.tv_tag_background_color);
        textViewTagTextColor = rootView.findViewById(R.id.tv_tag_text_color);
        textViewLikeColor = rootView.findViewById(R.id.tv_like_color);

        textViewCarrotTopColor.setOnClickListener(this);
        textViewTagBackgroundColor.setOnClickListener(this);
        textViewTagTextColor.setOnClickListener(this);
        textViewLikeColor.setOnClickListener(this);

        rootView.findViewById(R.id.btn_apply).setOnClickListener(this);

        textViewCarrotTopColor
                .setBackgroundColor(InstaTagApplication.getInstance().getCarrotTopColor());
        textViewTagBackgroundColor
                .setBackgroundColor(InstaTagApplication.getInstance().getTagBackgroundColor());
        textViewTagTextColor
                .setBackgroundColor(InstaTagApplication.getInstance().getTagTextColor());
        textViewLikeColor
                .setBackgroundColor(InstaTagApplication.getInstance().getLikeColor());

        initSpinners();
        initColorPickers();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initSpinners() {
        tagShowAssets = new Asset[]{
                new Asset(Animations.Show.BOUNCE_DOWN, R.anim.bounce_down),
                new Asset(Animations.Show.FADE_IN, R.anim.fade_in),
                new Asset(Animations.Show.SLIDE_DOWN, R.anim.slide_down),
                new Asset(Animations.Show.ZOOM_IN, R.anim.zoom_in),
        };

        tagHideAssets = new Asset[]{
                new Asset(Animations.Hide.BOUNCE_UP, R.anim.bounce_up),
                new Asset(Animations.Hide.FADE_OUT, R.anim.fade_out),
                new Asset(Animations.Hide.SLIDE_UP, R.anim.slide_up),
                new Asset(Animations.Hide.ZOOM_OUT, R.anim.zoom_out),
        };

        tagShowAnimationSpinnerAdapter = new AnimationSpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                tagShowAssets);

        tagHideAnimationSpinnerAdapter = new AnimationSpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                tagHideAssets);

        spinnerTagShowAnimation.setAdapter(tagShowAnimationSpinnerAdapter);

        spinnerTagHideAnimation.setAdapter(tagHideAnimationSpinnerAdapter);

        spinnerTagShowAnimation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Asset asset = tagShowAnimationSpinnerAdapter.getItem(position);

                InstaTagApplication.getInstance().setTagShowAnimation(asset.getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        spinnerTagHideAnimation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Asset asset = tagHideAnimationSpinnerAdapter.getItem(position);

                InstaTagApplication.getInstance().setTagHideAnimation(asset.getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

    }

    private void initColorPickers() {

        carrotTopColorDialogPicker = ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle(getString(R.string.carrot_top_color))
                .initialColor(getResources().getColor(R.color.colorPrimaryDark))
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        Log.d("Selected Color", "onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton(R.string.ok, new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        textViewCarrotTopColor.setBackgroundColor(selectedColor);
                        InstaTagApplication.getInstance().setCarrotTopColor(selectedColor);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build();

        tagBackgroundColorDialogPicker = ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle(getString(R.string.tag_background_color))
                .initialColor(getResources().getColor(R.color.colorPrimaryDark))
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        Log.d("Selected Color", "onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton(R.string.ok, new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        textViewTagBackgroundColor.setBackgroundColor(selectedColor);
                        InstaTagApplication.getInstance().setTagBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build();


        tagTextColorDialogPicker = ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle(getString(R.string.tag_text_color))
                .initialColor(getResources().getColor(android.R.color.white))
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        Log.d("Selected Color", "onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton(R.string.ok, new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        textViewTagTextColor.setBackgroundColor(selectedColor);
                        InstaTagApplication.getInstance().setTagTextColor(selectedColor);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build();

        likeColorDialogPicker = ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle(getString(R.string.like_color))
                .initialColor(getResources().getColor(android.R.color.white))
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        Log.d("Selected Color", "onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton(R.string.ok, new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        textViewLikeColor.setBackgroundColor(selectedColor);
                        InstaTagApplication.getInstance().setLikeColor(selectedColor);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_apply:
                dismiss();
                Intent configurationSaved = new Intent(Events.NEW_CONFIGURATION_SAVED);
                LocalBroadcastManager.getInstance(getActivity())
                        .sendBroadcast(configurationSaved);
                break;
            case R.id.tv_carrot_top_color:
                carrotTopColorDialogPicker.show();
                break;
            case R.id.tv_tag_background_color:
                tagBackgroundColorDialogPicker.show();
                break;
            case R.id.tv_tag_text_color:
                tagTextColorDialogPicker.show();
                break;
            case R.id.tv_like_color:
                likeColorDialogPicker.show();
                break;
        }
    }
}