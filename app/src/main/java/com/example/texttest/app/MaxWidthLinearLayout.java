/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.texttest.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * A simple {@link android.widget.LinearLayout} subclass, useful only in the vertical orientation, that allows each
 * child to define a maximum width using a {@link com.example.texttest.app.MaxWidthLinearLayout.LayoutParams#maxWidth layout_maxWidth} attribute
 * (only useful if the child's {@link android.view.ViewGroup.LayoutParams#width layout_width} is {@link
 * android.view.ViewGroup.LayoutParams#MATCH_PARENT}).
 */
public class MaxWidthLinearLayout extends LinearLayout {
    public MaxWidthLinearLayout(Context context) {
        super(context);
    }

    public MaxWidthLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxWidthLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Temporarily assign a new {@link android.view.ViewGroup.LayoutParams#width} to the maximum width if the
     * child would normally exceed the maximum width.
     */
    private void assignTemporaryChildWidthDuringMeasure(View child, int parentWidthMeasureSpec) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        assert lp != null;
        int availableWidth = MeasureSpec.getSize(parentWidthMeasureSpec);
        if (lp.width == LayoutParams.MATCH_PARENT && availableWidth > lp.maxWidth) {
            lp.oldLayoutWidth = LayoutParams.MATCH_PARENT;
            lp.width = Math.min(lp.maxWidth, availableWidth);
        }
    }

    /**
     * Revert any changes caused by {@link #assignTemporaryChildWidthDuringMeasure(android.view.View,
     * int)}.
     */
    private void revertChildWidthDuringMeasure(View child) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        assert lp != null;
        if (lp.oldLayoutWidth != Integer.MIN_VALUE) {
            lp.width = lp.oldLayoutWidth;
        }
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec,
            int parentHeightMeasureSpec) {
        assignTemporaryChildWidthDuringMeasure(child, parentWidthMeasureSpec);
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
        revertChildWidthDuringMeasure(child);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec,
            int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        assignTemporaryChildWidthDuringMeasure(child, parentWidthMeasureSpec);
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed,
                parentHeightMeasureSpec, heightUsed);
        revertChildWidthDuringMeasure(child);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new LayoutParams(lp);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams lp) {
        return lp instanceof LayoutParams;
    }

    /**
     * Provides additional layout params (specifically {@link #maxWidth layout_maxWidth}) for
     * children of {@link com.example.texttest.app.MaxWidthLinearLayout}.
     */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        private int maxWidth;
        private int oldLayoutWidth = Integer.MIN_VALUE; // used to store old lp.width during measure

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MaxWidthLinearLayout_Layout);
            assert a != null;
            maxWidth = a.getLayoutDimension(
                    R.styleable.MaxWidthLinearLayout_Layout_layout_maxWidth, Integer.MAX_VALUE);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }
}
