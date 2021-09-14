package com.ciapa.androgon;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;

import android.os.Looper;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.media.MediaPlayer;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.graphics.Paint.Style;


public class CButton
{
    int ButtonWidth;
    int ButtonHeight;

    Bitmap myBitmapBtOn;
    Bitmap myBitmapBtOff;

    Paint paint;

    float ButtonX;
    float ButtonY;

    Rect ButtonRect;

    boolean ButtonClicked = false;
    boolean fadein  = false;
    boolean fadeout = false;
    int alpha;

    MediaPlayer mp;
    int Button_res_ID_sound;
    View view;

    int timeout;

    public CButton(View v,
                    float x,
                    float y,
                    int resID_on,
                    int resID_off,
                    int res_ID_sound,
                    int time)
    {
        view = v;

        paint = new Paint();
        paint.setAlpha(255);

        timeout = time;

        ButtonX = x;
        ButtonY = y;

        myBitmapBtOn = BitmapFactory.decodeResource(view.getResources(), resID_on);
        myBitmapBtOff = BitmapFactory.decodeResource(view.getResources(), resID_off);

        ButtonWidth = myBitmapBtOn.getWidth();
        ButtonHeight = myBitmapBtOn.getHeight();

        Button_res_ID_sound = res_ID_sound;


        ButtonRect = new Rect((int)ButtonX,
                (int)ButtonY,
                (int)ButtonX + ButtonWidth,
                (int)ButtonY + ButtonHeight);

        mp = MediaPlayer.create(view.getContext(), Button_res_ID_sound);
    }

    public boolean Update(float x,float y)
    {
        if(ButtonRect.contains((int) x, (int) y))
        {
            ButtonClicked = true;
            play();
            SetButtonTimeOut(timeout);
            return true;
        }
    return false;
    }

    public void hide()
    {
        paint.setAlpha(0);
    }

    public void show(int timeout)
    {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run()
                    {
                        paint.setAlpha(255);
                    }
                },
                timeout);
    }

    public void fade_out()
    {
        fadeout = true;
    }

    public void fade_in()
    {
        fadein = true;
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

    private void SetButtonTimeOut(int timeout)
    {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable()
                {
                    public void run()
                    {
                      ButtonClicked = false;
                    }
                },
                timeout);
    }

    public void Draw(Canvas canvas)
    {
        if(ButtonClicked == false)
        {
           // if(fadeout)
           // {
            //    if(alpha >= 0)
             //   {
              //      alpha -= 15;
              //      paint.setAlpha(alpha);
             //   }
           // }
            canvas.drawBitmap(myBitmapBtOff,ButtonX,ButtonY, paint);
        }else
        {
            canvas.drawBitmap(myBitmapBtOn,ButtonX,ButtonY, paint);
        }
    }

}


