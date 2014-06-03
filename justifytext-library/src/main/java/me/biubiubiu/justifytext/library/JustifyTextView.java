package me.biubiubiu.justifytext.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ccheng on 3/18/14.
 */
public class JustifyTextView extends TextView {

    private StaticLayout mLayout;
    private int mLineY;
    private int mViewWidth;

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mViewWidth = getMeasuredWidth();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDraw(Canvas canvas) {
        String text = (String) getText();
        System.out.println("mViewWidth = " + mViewWidth);
        mLineY += getTextSize();
        mLayout = (StaticLayout) getLayout();
        for (int i = 0; i < mLayout.getLineCount(); i++) {
            int lineStart = mLayout.getLineStart(i);
            int lineEnd = mLayout.getLineEnd(i);
            String line = text.substring(lineStart, lineEnd);

            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
            System.out.println("line = " + line);
            System.out.println("width = " + width);
            if (needScale(width)) {
                drawScaledText(canvas, line, width);
            } else {
                canvas.drawText(line, 0, mLineY, getPaint());
            }

            mLineY += getTextSize() * getLineSpacingMultiplier() + getLineSpacingExtra();
        }
    }

    private void drawScaledText(Canvas canvas, String line, float lineWidth) {
        float d = (mViewWidth - lineWidth)/line.length();
        float x = 0;
        for (int i = 0; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }

    private boolean needScale(float width) {
        return (mViewWidth - width) < mViewWidth*0.3;
    }
}
