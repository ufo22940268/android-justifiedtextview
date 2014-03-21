package me.biubiubiu.justifytext.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.regex.Pattern;

/**
 * Created by ccheng on 3/18/14.
 */
public class JustifyTextView extends View {

    public static int mViewWidth;

    /*
    Unit is px.
     */
    //TODO Text size will displays strange when scroll back to previous page.
    public static int mTextSize = 35;
    private final Paint p;

    private String mArticalText = "";
    private float mSpacingAdd;
    private float mSpacingMulti = 1;
    private int mTextColor = android.R.color.primary_text_light;

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        p.setColor(getContext().getResources().getColor(mTextColor));
        p.setTextSize(mTextSize);
        p.setAntiAlias(true);
    }

    public String getText() {
        return mArticalText;
    }

    public void setText(String articalText) {
        mArticalText = articalText;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mViewWidth = getMeasuredWidth();
        mViewWidth -= mTextSize/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, 30);

        String leftText = mArticalText;

        while ((leftText = drawOneLine(canvas, leftText)).length() != 0) {
//            System.out.println("leftText = " + leftText);
        }
    }

    private String drawOneLine(Canvas canvas, String text) {
        int minimumCount = mViewWidth/mTextSize;

        String leftString;
        if (text.length() < minimumCount) {
            String s = text;
            if (text.contains("\n")) {
                int p = text.indexOf("\n");
                s = text.substring(0, p);
                leftString = text.substring(p + 1, text.length());
            } else {
                leftString = "";
            }

            drawString(canvas, text, false);
        } else {
            int count = minimumCount;
            while (p.measureText(text.substring(0, count)) < mViewWidth && count < text.length()) {
                count += 1;
            }

            count -= 1;
            int width = (int) p.measureText(text.substring(0, count));

            //Detect if string contains new line sign, if it contains, then
            //break string return the rest to caller.
            String s = text.substring(0, count);
            boolean needSpacing = false;
            char leftPunc = 0;
            if (s.contains("\n")) {
                int p = s.indexOf("\n");
                s = s.substring(0, p);
                leftString = text.substring(p + 1, text.length());
                needSpacing = false;
            } else {
                leftString = text.substring(count, text.length());
                needSpacing = true;

                //If the first word of next line is punctuation, then move the punctuation to upper line.
                if (leftString.length() > 0) {
                    char punc = leftString.charAt(0);
                    if (isChinesePunctuation(punc)) {
                        leftString = leftString.substring(1);
                        leftPunc = punc;
                    }
                }
            }

            drawString(canvas, s, needSpacing, leftPunc);
        }

        //Move to next line.
        canvas.translate(0, evalLineSpacing());
        return leftString;
    }

    private float evalLineSpacing() {
        return mSpacingAdd + mSpacingMulti*mTextSize;
    }

    private void drawString(Canvas canvas, String text, boolean needScale) {
        drawString(canvas, text, needScale, (char) 0);
    }

    private void drawString(Canvas canvas, String text, boolean needScale, char punc) {
        int w = (int)p.measureText(text);
        float spacing = 0;
        if (needScale && w < mViewWidth) {
            float d = (float)(mViewWidth - w);
            spacing = d/text.length();
        }
        System.out.println("draw text = " + text + "$");
        int x = 0;

        canvas.save();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            canvas.drawText(String.valueOf(c), 0, 0, p);
            if (i != text.length() - 1) {
                float dx = getCharWidth(c) + spacing;
                canvas.translate(dx, 0);
            } else {
                canvas.translate(mTextSize/2 + 3, 0);
            }
        }

        if (punc != 0) {
            canvas.drawText(String.valueOf(punc), 0, 0, p);
        }

        canvas.restore();
    }


    private float getCharWidth(char c) {
        return p.measureText(String.valueOf(c));
    }


    private boolean isLetter(String s) {
        Pattern p = Pattern.compile("[a-zA-Z0-9]");
        return p.matcher(s).find();
    }

    // 根据UnicodeBlock方法判断中文标点符号
    public boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS) {
            return true;
        } else {
            return false;
        }
    }

    public void setHeight(int height) {
        setTextSizeInPx(height);
    }

    private void setTextSizeInPx(int size) {
        mTextSize = size;
    }

    public void setTextSize(int flag, int size) {
        setTextSizeInPx(size);
        invalidate();
    }

    public void setLineSpacing(float add, float multi) {
        mSpacingAdd = add;
        mSpacingMulti = multi;
    }

    public void setTextColor(int color) {
        mTextColor = color;
        p.setColor(color);
    }
}
