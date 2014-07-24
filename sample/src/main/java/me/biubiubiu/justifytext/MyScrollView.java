package me.biubiubiu.justifytext;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by ccheng on 7/24/14.
 */
public class MyScrollView extends ScrollView {

    private ScrollView mAlternativeScrollView;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setAlternativeScrollView(ScrollView alternativeScrollView) {
        mAlternativeScrollView = alternativeScrollView;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mAlternativeScrollView != null) {
            mAlternativeScrollView.scrollTo(l, t);
        }
    }
}
