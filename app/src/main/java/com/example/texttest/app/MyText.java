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
public class MyText extends View {

    public static final int MAX_WIDTH = 200;
    public static final int FONT_SIZE = 20;
    public static final int EN_FONT_SIZE = 11;
    public static final int LARGEST_FONT_SIZE = FONT_SIZE;
    private final Paint p;

    public MyText(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(FONT_SIZE);

        int w = (int) p.measureText("A");
        System.out.println("en_w = " + w);
        w = (int) p.measureText("想");
        System.out.println("cn_w = " + w);
    }
    

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, 30);

        String sampleText = getResources().getString(R.string.cn_book_artical);
        String leftText = sampleText;

        while ((leftText = drawOneLine(canvas, leftText)).length() != 0) {
//            System.out.println("leftText = " + leftText);
        }
    }

    private String drawOneLine(Canvas canvas, String text) {
        p.setTextScaleX(1);

        int minimumCount = MAX_WIDTH/LARGEST_FONT_SIZE;

        String leftString;
        if (text.length() < minimumCount) {
            drawString(canvas, text, false);
            leftString = "";
        } else {
            int count = minimumCount;
            while (p.measureText(text.substring(0, count)) < MAX_WIDTH && count < text.length()) {
                count += 1;
            }

            count -= 1;
            int width = (int) p.measureText(text.substring(0, count));
            System.out.println("width = " + width);

            drawString(canvas, text.substring(0, count), true);
            leftString = text.substring(count, text.length());
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

    private void evalScale(String text, float d) {
        int enCount = getEnCount(text);
        int cnCount = text.length() - enCount;
        float scale = d/(enCount*EN_FONT_SIZE + cnCount*FONT_SIZE);
        System.out.println("scale = " + scale);
        p.setTextScaleX(1 + scale);
    }

    private int getEnCount(String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (isLetter(String.valueOf(c))) {
                count += 1;
            }
        }
        return count;
    }

    private boolean isLetter(String s) {
        Pattern p = Pattern.compile("[a-zA-Z0-9]");
        return p.matcher(s).find();
    }
}
