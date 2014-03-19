package com.example.texttest.app;

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

    public static final int MAX_WIDTH = 400;
    public static final int FONT_SIZE = 20;
    public static final int LARGEST_FONT_SIZE = FONT_SIZE;
    private final Paint p;

    private String mArticalText = "";

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(FONT_SIZE);
    }

    public String getText() {
        return mArticalText;
    }

    public void setText(String articalText) {
        mArticalText = articalText;
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
        int minimumCount = MAX_WIDTH/LARGEST_FONT_SIZE;

        String leftString;
        if (text.length() < minimumCount) {
            System.out.println("JustifyTextView.drawOneLine");
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
            while (p.measureText(text.substring(0, count)) < MAX_WIDTH && count < text.length()) {
                count += 1;
            }

            count -= 1;
            int width = (int) p.measureText(text.substring(0, count));
            System.out.println("width = " + width);

            //Detect if string contains new line sign, if it contains, then
            //break string return the rest to caller.
            String s = text.substring(0, count);
            boolean needSpacing = false;
            if (s.contains("\n")) {
                int p = s.indexOf("\n");
                s = s.substring(0, p);
                leftString = text.substring(p + 1, text.length());
                needSpacing = false;
            } else {
                leftString = text.substring(count, text.length());
                needSpacing = true;
            }

            drawString(canvas, s, needSpacing);
        }

        canvas.translate(0, FONT_SIZE);
        return leftString;
    }

    private void drawString(Canvas canvas, String text, boolean needScale) {
        int w = (int)p.measureText(text);
        float spacing = 0;
        if (needScale && w < MAX_WIDTH) {
            float d = (float)(MAX_WIDTH - w);
            spacing = d/text.length();
        }
        System.out.println("draw text = " + text + "$");
        int x = 0;

        canvas.save();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            canvas.drawText(String.valueOf(c), 0, 0, p);
            canvas.translate(getCharWidth(c) + spacing, 0);
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
}
