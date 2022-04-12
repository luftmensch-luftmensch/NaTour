/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.natour.R;
import com.google.android.material.textview.MaterialTextView;
import com.tokenautocomplete.TokenCompleteTextView;

public class CustomTokenCompleteTextView extends TokenCompleteTextView<String> {

    public CustomTokenCompleteTextView(Context context) {
        super(context);
    }

    public CustomTokenCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTokenCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(String tagRicerca) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.tag_ricerca_token,
                (ViewGroup) getParent(),
                false);
        ((MaterialTextView) layout.findViewById(R.id.TagRicercaTesto)).setText(tagRicerca);

        return layout;
    }

    @Override
    protected String defaultObject(String completionText) {
        return completionText;
    }
}