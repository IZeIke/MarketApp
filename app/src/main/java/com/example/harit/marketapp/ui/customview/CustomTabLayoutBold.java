package com.example.harit.marketapp.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;


public class CustomTabLayoutBold extends TabLayout {

    public CustomTabLayoutBold(Context context) {
        super(context);
    }

    public CustomTabLayoutBold(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabLayoutBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addTab(@NonNull Tab tab) {
        super.addTab(tab);
        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());

        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                //((TextView) tabViewChild).setTypeface(Fonts.paethaiBold());
            }
        }

    }
}
