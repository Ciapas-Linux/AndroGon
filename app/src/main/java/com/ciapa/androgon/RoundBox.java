package com.ciapa.androgon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Looper;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;


public class RoundBox
{
    int BoxWidth;
    int BoxHeight;
    Paint BoxPaint;
    float BoxX;
    float BoxY;
    Rect BoxRect;
    boolean BoxClicked = false;
    MediaPlayer mp;
    int Button_res_ID_sound;
    View view;
    float radius;
    String BoxText;
    static TextPaint mTextPaint;
    Rect TextRect;
    int TextWidth;
    int TextHeight;
    int TextColor = Color.rgb(0,255,0);
    float TextX;
    float TextY;
    boolean with_text = false;

    public RoundBox(View v,
                   float x,
                   float y,
                   float width,
                   float height,
                   float rad,
                   int res_ID_sound)
    {
        view = v;

        BoxPaint = new Paint();
        BoxPaint.setAlpha(255);
        BoxX = x;
        BoxY = y;
        radius = rad;
        BoxWidth = (int)width;
        BoxHeight = (int)height;

        BoxRect = new Rect((int)BoxX,
                (int)BoxY,
                (int)BoxX + BoxWidth,
                (int)BoxY + BoxHeight);

        Button_res_ID_sound = res_ID_sound;

        mp = MediaPlayer.create(view.getContext(), Button_res_ID_sound);
    }

    public void SetText(String text, int color, int size)
    {
        BoxText = text;
        TextColor = color;
        TextRect = new Rect();
        mTextPaint = new TextPaint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(TextColor);
        mTextPaint.setTextSize(pxFromDp(view.getContext(),size));
        mTextPaint.setShadowLayer(20, 10.0f, 10.0f, Color.BLACK);
        mTextPaint.getTextBounds(text,0,text.length(),TextRect);
        TextHeight = TextRect.height();
        TextWidth = TextRect.width();
        with_text = true;
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

    public static float pxFromDp(final Context context, final float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public boolean Update(float x,float y)
    {
        if(BoxRect.contains((int) x, (int) y))
        {
            BoxClicked = true;
            play();
            return true;
        }
        return false;
    }

    private void play()
    {
        try
        {
            // if (mp.isPlaying())
            // {
            mp.stop();
            mp.release();
            mp = MediaPlayer.create(view.getContext(), Button_res_ID_sound);
            // }
            mp.start();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void SetBoxTimeOut(int timeout)
    {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable()
                {
                    public void run()
                    {
                        BoxClicked = false;
                    }
                },
                timeout);
    }

    public void Draw(Canvas canvas)
    {
        BoxPaint.setColor(Color.argb(150,0,175,10));
        BoxPaint.setStrokeWidth(5);
        canvas.drawRoundRect(BoxRect.left,
                BoxRect.top,
                BoxRect.right,
                BoxRect.bottom,
                radius,
                radius,
                BoxPaint);
        BoxPaint.setColor(Color.argb(180,0,65,10));
        BoxPaint.setStrokeWidth(5);
        canvas.drawRoundRect(BoxRect.left + 5,
                BoxRect.top + 5,
                BoxRect.right - 5,
                BoxRect.bottom - 10,
                radius,
                radius,
                BoxPaint);

        if(with_text == true) {
            drawMultiLineEllipsizedText(canvas,
                    BoxRect.left + 35,
                    BoxRect.top + 35,
                    BoxRect.right - 35,
                    BoxRect.bottom - 10,
                    BoxText);
        }

    }

}


