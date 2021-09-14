package com.ciapa.androgon;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;


public class Diode
{
    int DiodeWidth;
    int DiodeHeight;

    Bitmap myBitmapDiodeOn;
    Bitmap myBitmapDiodeOff;

    boolean BlinkDiode = false;
    boolean diodes_blink_timer = false;

    float DiodeX;
    float DiodeY;

    Rect DiodeRect;
    View view;

    Timer timer;

    public Diode(View v,float x,
                    float y,
                    int w,
                    int h,
                    int resID_on,
                    int resID_off)
    {
        view = v;

        DiodeHeight = h;
        DiodeWidth = w;

        DiodeX = x;
        DiodeY = y;

        myBitmapDiodeOn = BitmapFactory.decodeResource(view.getResources(), resID_on);
        myBitmapDiodeOff = BitmapFactory.decodeResource(view.getResources(), resID_off);

        DiodeRect = new Rect((int)DiodeX,
                (int)DiodeY,
                (int)DiodeX + DiodeWidth,
                (int)DiodeY + DiodeHeight);


        // 1 s timer
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

    public void Blink(boolean blink)
    {
        BlinkDiode = blink;
    }

    // 1 sek DIODES BLINK
    private void TimerMethod()
    {
        if(diodes_blink_timer == false)
        {
            diodes_blink_timer = true;
        }else
        {
            diodes_blink_timer = false;
        }
    }

    public void Draw(Canvas canvas,boolean on_off)
    {
        if(on_off == false)
        {
            canvas.drawBitmap(myBitmapDiodeOff,DiodeX,DiodeY, null);
        }else
        {
            if(BlinkDiode == true)
            {
                if(diodes_blink_timer == true)
                {
                    canvas.drawBitmap(myBitmapDiodeOn, DiodeX, DiodeY, null);
                }else
                {
                    canvas.drawBitmap(myBitmapDiodeOff, DiodeX, DiodeY, null);
                }
            }else
            {
                canvas.drawBitmap(myBitmapDiodeOn, DiodeX, DiodeY, null);
            }
        }
    }

}




