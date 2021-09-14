package com.ciapa.androgon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class TextSuper
{
    int TextWidth;
    int TextHeight;
    boolean BlinkText = false;
    boolean text_blink_timer = false;
    float posx = 0;
    float posy = 0;
    Rect TextRect;
    Rect TextClickRect;
    View view;
    Timer timer;
    static TextPaint mTextPaint;
    String text = "";
    boolean TextClicked = false;
    int TextColor = Color.rgb(0,255,0);
    int TextSize;

    public TextSuper(String stext,int color, View v,float x,float y, int size)
    {
        view = v;
        text = stext;
        posx = x;
        posy = y;
        TextColor = color;
        TextSize = size;

        TextClickRect = new Rect();
        TextRect = new Rect();
        mTextPaint = new TextPaint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(TextColor);
        mTextPaint.setTextSize(pxFromDp(view.getContext(),TextSize));
        mTextPaint.setShadowLayer(20, 10.0f, 10.0f, Color.BLACK);
        mTextPaint.getTextBounds(text,0,text.length(),TextRect);
        TextHeight = TextRect.height();
        TextWidth = TextRect.width();
        TextClickRect.left = (int)posx;
        TextClickRect.top = (int)posy;
        TextClickRect.right = (int)posx + TextWidth;
        TextClickRect.bottom = (int)posy + TextHeight;


        // 1 s timer for text blinking
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                TimerMethod();
            }

        }, 0, 1000);
    }

    public boolean Update(float x,float y)
    {
        if(TextClickRect.contains((int) x, (int) y))
        {
         //   TextClicked = true;
        return true;
        }
        return false;
    }

    public void SetShadow(int color, int radius, int dx, int dy)
    {
        mTextPaint.setShadowLayer(radius, dx, dy, color);
    }

    public void SetColor(int color)
    {
        TextColor = color;
        mTextPaint.setColor(TextColor);
    }

    public void SetText(String stext,float x,float y)
    {
        text = stext;
        posx = x;
        posy = y;
        mTextPaint.setTextSize(pxFromDp(view.getContext(),TextSize));
        mTextPaint.getTextBounds(text,0,stext.length(),TextRect);
        TextHeight = TextRect.height();
        TextWidth = TextRect.width();
        TextClickRect.left = (int)posx - 15;
        TextClickRect.top = (int)posy - TextHeight - 15;
        TextClickRect.right = (int)posx + TextWidth + 15;
        TextClickRect.bottom = (int)posy + 15;
    }

    public void SetPosition(float x,float y)
    {
        posx = x;
        posy = y;
        TextClickRect.left = (int)posx;
        TextClickRect.bottom = TextHeight;
        TextClickRect.right = TextWidth;
        TextClickRect.top = (int)posy;
    }

    public void Blink(boolean blink)
    {
        BlinkText = blink;
    }

    public static float pxFromDp(final Context context, final float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    // 1 sek DIODES BLINK
    private void TimerMethod()
    {
        if(text_blink_timer == false)
        {
            text_blink_timer = true;
        }else
        {
            text_blink_timer = false;
        }
    }

    public void Draw(Canvas canvas)
    {
            if(BlinkText == true) // blinked text
            {
                if(text_blink_timer == true)
                {
                    canvas.drawText(text, posx, posy, mTextPaint);
                }else
                {

                }
            }else
            {  // no blinked
                canvas.drawText(text, posx, posy, mTextPaint);
            }
    }

    public static void drawMultiLineEllipsizedText(final Canvas _canvas,
                                                   final float _left,
                                                   final float _top,
                                                   final float _right,
                                                   final float _bottom,
                                                   final String _text)
    {
        final float height = _bottom - _top;
        final StaticLayout measuringTextLayout = new StaticLayout(_text, mTextPaint, (int) Math.abs(_right - _left),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        int line = 0;
        final int totalLineCount = measuringTextLayout.getLineCount();
        for (line = 0; line < totalLineCount; line++)
        {
            final int lineBottom = measuringTextLayout.getLineBottom(line);
            if (lineBottom > height)
            {
                break;
            }
        }
        line--;

        if (line < 0)
        {
            return;
        }

        int lineEnd;
        try {
            lineEnd = measuringTextLayout.getLineEnd(line);
        } catch (Throwable t) {
            lineEnd = _text.length();
        }
        String truncatedText = _text.substring(0, Math.max(0, lineEnd));

        if (truncatedText.length() < 3)
        {
            return;
        }

        if (truncatedText.length() < _text.length())
        {
            truncatedText = truncatedText.substring(0, Math.max(0, truncatedText.length() - 3));
            truncatedText += "...";
        }
        final StaticLayout drawingTextLayout = new StaticLayout(truncatedText, mTextPaint, (int) Math.abs(_right
                - _left), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        _canvas.save();
        _canvas.translate(_left, _top);
        drawingTextLayout.draw(_canvas);
        _canvas.restore();
    }

    void drawMultiLineText(String str, float x, float y, Canvas canvas)
    {
        String[] lines = str.split("\n");
        float txtSize = -mTextPaint.ascent() + mTextPaint.descent();

        if (mTextPaint.getStyle() == Paint.Style.FILL_AND_STROKE || mTextPaint.getStyle() == Paint.Style.STROKE)
        {
            txtSize += mTextPaint.getStrokeWidth(); //add stroke width to the text size
        }
        float lineSpace = txtSize * 0.2f;  //default line spacing

        for (int i = 0; i < lines.length; ++i)
        {
            canvas.drawText(lines[i], x, y + (txtSize + lineSpace) * i, mTextPaint);
        }
    }

}





