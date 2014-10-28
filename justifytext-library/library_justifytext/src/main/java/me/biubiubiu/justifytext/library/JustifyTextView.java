package me.biubiubiu.justifytext.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.Character;
import java.lang.String;

/**
 * @author ccheng
 * @Date 3/18/14
 */
public class JustifyTextView extends TextView {

    private int mLineY;
    private int mViewWidth;
    public static final String TWO_CHINESE_BLANK = "  ";

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mViewWidth = getMeasuredWidth();
        String text = getText().toString();

        mLineY = 0;
        mLineY += getTextSize();
        Layout layout = getLayout();

        // layout.getLayout()在4.4.3出现NullPointerException
        if (layout == null) {
            return;
        }

        Paint.FontMetrics fm = paint.getFontMetrics();

        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout.getSpacingAdd());

        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
            String line = text.substring(lineStart, lineEnd);
            if (needScale(line) && i < layout.getLineCount() - 1) {
                drawScaledText(canvas, lineStart, line, width);
            } else {
                canvas.drawText(line, 0, mLineY, paint);
            }
            mLineY += textHeight;
        }
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth) {

        float x = 0;

        // draw blank without justify
        int offset = 0;
        for (; offset < line.length(); offset++) {
            char c = line.charAt(offset);
            if( c==32 || c==12288) {
                String blank = String.valueOf(c);
                canvas.drawText(blank, x, mLineY, getPaint());
                float bw = StaticLayout.getDesiredWidth(blank, getPaint());
                x += bw;
                offset++;
            } else {
                break;
            }
        }

        if(offset == line.length()){
            return;
        }
        line = line.substring(offset);

        // count of surrogate pair
        int longCharCount = 0;
        for(int i= 0; i < line.length(); i++) {
            if(i + 1 < line.length() && Character.isSurrogatePair(line.charAt(i), line.charAt(i + 1))) {
                longCharCount++;
            }
        }

        int gapCount = line.length() - 1 - longCharCount;
        float d = (mViewWidth - lineWidth) / gapCount;
        for (int j=0; j < line.length(); j++){
            if(j + 1 < line.length() && Character.isSurrogatePair(line.charAt(j), line.charAt(j + 1))) {
                String c = new String(new char[] { line.charAt(j), line.charAt(j + 1) });
                float cw = StaticLayout.getDesiredWidth(c, getPaint());
                canvas.drawText(c, x, mLineY, getPaint());
                x += cw + d;
                j++;
            } else {
                String c = String.valueOf(line.charAt(j));
                float cw = StaticLayout.getDesiredWidth(c, getPaint());
                canvas.drawText(c, x, mLineY, getPaint());
                x += cw + d;
            }
        }
    }

    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    private boolean needScale(String line) {
        if (line == null || line.length() == 0) {
            return false;
        } else {
            return line.charAt(line.length() - 1) != '\n';
        }
    }