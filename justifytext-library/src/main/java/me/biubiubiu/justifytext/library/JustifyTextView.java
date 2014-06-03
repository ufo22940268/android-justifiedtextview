package me.biubiubiu.justifytext.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ccheng on 3/18/14.
 */
public class JustifyTextView extends TextView {

    private Layout mLayout;
    private int mLineY;
    private int mViewWidth;

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDraw(Canvas canvas) {
        Canvas fakeCanvas = new Canvas();
        super.onDraw(fakeCanvas);
        mViewWidth = getMeasuredWidth();
        String text = (String) getText();
        mLineY = 0;
        mLineY += getTextSize();
        mLayout =  getLayout();
        for (int i = 0; i < mLayout.getLineCount(); i++) {
            int lineStart = mLayout.getLineStart(i);
            int lineEnd = mLayout.getLineEnd(i);
            String line = text.substring(lineStart, lineEnd);

            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
            if (needScale(line)) {
                drawScaledText(canvas, line, width);
            } else {
                canvas.drawText(line, 0, mLineY, getPaint());
            }

            mLineY += getLineHeight();
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

    private boolean needScale(String width) {
        if (width.length() == 0) {
            return false;
        } else {
            return width.charAt(width.length() - 1) != '\n';
        }
    }
}
