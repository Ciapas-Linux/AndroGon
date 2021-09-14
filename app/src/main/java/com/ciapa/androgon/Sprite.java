package com.ciapa.androgon;

import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class Sprite
{

    int FRAME_W;
    int FRAME_H;
    private int NB_FRAMES = 0;
    private int COUNT_X = 0;
    private int COUNT_Y = 0;

    private float SpriteX;
    private float SpriteY;

    int framenumber;

    Bitmap bmpSpriteSheet;
    private Bitmap[] bmps;

    long TimeOutTime  = 1000;  // 1 sek
    long start_time = 0;

    Rect frame_rect;
    Paint paint;
    boolean frameon = false;
    boolean backgroundon = false;
    int BackGrounColor;

    private MainActivity mainActivity;

    public Sprite(MainActivity activity,int resID,int numframes,int cntx,int cnty,int duration,int scale)
    {
        mainActivity = activity;
        NB_FRAMES = numframes;
        COUNT_X = cntx;
        COUNT_Y = cnty;

        bmpSpriteSheet = BitmapFactory.decodeResource(activity.getResources(), resID);

        if (bmpSpriteSheet != null)
        {
            bmps = new Bitmap[NB_FRAMES];
            int currentFrame = 0;

            FRAME_W = bmpSpriteSheet.getWidth() / COUNT_X;   // IF sprite sheet is box:  32x32 64x64
            FRAME_H = bmpSpriteSheet.getHeight() / COUNT_Y;

            for (int i = 0; i < COUNT_Y; i++)
            {
                for (int j = 0; j < COUNT_X; j++)
                {
                    bmps[currentFrame] = Bitmap.createBitmap(bmpSpriteSheet,
                            FRAME_W * j,
                            FRAME_H * i,
                            FRAME_W,
                            FRAME_H);

                    // apply scale factor
                   // bmps[currentFrame] = Bitmap.createScaledBitmap(
                        //    bmps[currentFrame], FRAME_W * SCALE_FACTOR, FRAME_H
                          //          * SCALE_FACTOR, true);

                    currentFrame++;

                    if (currentFrame >= NB_FRAMES)
                    {
                        break;
                    }
                }
            }

            //mTextPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            //mTextPaint.setColor(Color.GREEN);
            //mTextPaint.setTextSize(58.0f);

            paint = new Paint();
            paint.setStrokeWidth(8);
            paint.setColor(Color.argb(255,0,255,0));
            paint.setStyle(Paint.Style.STROKE);

            frame_rect = new Rect();

        }

    }// end constructor


    public void SetSpritePosition(float x, float y)
    {
        SpriteX = x;
        SpriteY = y;
        frame_rect.left = (int)x;
        frame_rect.top =  (int)y;
        frame_rect.right =  (int)x + FRAME_W;
        frame_rect.bottom = (int)y + FRAME_H;
    }

    public void SetFrameOn(boolean frm)
    {
        frameon = frm;
    }

    public void SetBackgroundOn(boolean bkgnd, int color)
    {
        backgroundon = bkgnd;
        BackGrounColor = color;
    }

    public void DrawFrame(Canvas canvas)
    {
        paint.setColor(Color.argb(255,0,255,0));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(frame_rect.left - 5,
                frame_rect.top - 5,
                frame_rect.right + 5,
                frame_rect.bottom + 5,
                50,
                50,
                paint);
    }

    public void DrawBackground(Canvas canvas)
    {
        paint.setColor(Color.argb(255,0,185,0));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(frame_rect.left,
                frame_rect.top,
                frame_rect.right,
                frame_rect.bottom,
                50,
                50,
                paint);
    }

    public void Play(Canvas canvas)
    {
        if(backgroundon)
        {
            DrawBackground(canvas);
        }
        if(framenumber >= NB_FRAMES) framenumber = 0;
        canvas.drawBitmap(bmps[framenumber], SpriteX, SpriteY, null);
        framenumber++;
        if(frameon)
        {
            DrawFrame(canvas);
        }
    }

    public void Draw(Canvas canvas,float x,float y, int frame)
    {
        if(backgroundon)
        {
            DrawBackground(canvas);
        }
        canvas.drawBitmap(bmps[frame], SpriteX, SpriteY, null);
        if(frameon)
        {
            DrawFrame(canvas);
        }
    }

    public void StartTimeOut(int time)
    {
        TimeOutTime  = System.currentTimeMillis() + time;
        start_time = System.currentTimeMillis();
    }

    public void DrawTimeOut(Canvas canvas,int frame)
    {
        if(System.currentTimeMillis() < TimeOutTime)
        {
            if(backgroundon)
            {
                DrawBackground(canvas);
            }
            canvas.drawBitmap(bmps[frame], SpriteX, SpriteY, null);
            if(frameon)
            {
                DrawFrame(canvas);
            }
        }
    }

    public void DrawTimeOutAnimate(Canvas canvas)
    {
        if(System.currentTimeMillis() < TimeOutTime)
        {
            if(backgroundon)
            {
                DrawBackground(canvas);
            }
            if(framenumber >= NB_FRAMES) framenumber = 0;
            canvas.drawBitmap(bmps[framenumber], SpriteX, SpriteY, null);
            framenumber++;
            if(frameon)
            {
                DrawFrame(canvas);
            }

        }
    }

}
