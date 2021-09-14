package com.ciapa.androgon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class ViewTools extends View
{
    // Main class & Utilities instance
    public final MainActivity main;
    public final Utilities util;
    MyToastBox toastbox;

    float eventX;
    float eventY;

    int ButtonDistance = 50;
    int ButtonWidth;
    int ButtonHeight;

    MediaPlayer mp;

    Timer Timer1;
    Timer Timer2;

    boolean DrawCircle = false;

    // >BUTTONY<
    CButton ButtonExit;
    CButton ButtonUpdate;
    CButton ButtonWykres;

    // >Layout coordinates<
    Rect top_box_rect;
    Rect big_box_rect;

    Paint paint;

    public ViewTools(Context context,MainActivity activity)
    {
        super(context);

        main = activity;
        util = main.util;

        paint = new Paint();

        ButtonWidth = BitmapFactory.decodeResource(getResources(), R.drawable.btstarton).getWidth();
        ButtonHeight = BitmapFactory.decodeResource(getResources(), R.drawable.btstarton).getHeight();

        top_box_rect = new Rect((int)(main.screenWidth - util.Percent(99,main.screenWidth)),
                (int)(main.screenHeight - util.Percent(99,main.screenHeight)),
                (int)(main.screenWidth - util.Percent(1,main.screenWidth)),
                (int)(main.screenHeight - util.Percent(80,main.screenHeight)));

        big_box_rect = new Rect((int)(main.screenWidth - util.Percent(99,main.screenWidth)),
                top_box_rect.bottom + 50,
                (int)(main.screenWidth - util.Percent(1,main.screenWidth)),
                (int)(main.screenHeight - util.Percent(1,main.screenHeight)));
                //width: 2560


        // FIRST CENTERED BATON
        ButtonUpdate = new CButton(this,
                (main.screenWidth  - ButtonWidth) * 0.5f,
                big_box_rect.top + 25,
                R.drawable.btupdateon,
                R.drawable.btupdateoff,
                R.raw.keyboard,600);

        ButtonWykres = new CButton(this,
                ButtonUpdate.ButtonX - ButtonWidth - ButtonDistance,
                big_box_rect.top + 25,
                R.drawable.btcharton,
                R.drawable.btchartoff,
                R.raw.keyboard,600);

        // RIGHT BOTTOM of screen
        ButtonExit = new CButton(this,
                main.screenWidth - ButtonWidth - (ButtonWidth/6),
                main.screenHeight  - ButtonHeight - (ButtonHeight/4),
                R.drawable.btcloseon,
                R.drawable.btcloseoff,
                R.raw.keyboard,600);

        toastbox = new MyToastBox(main);
        toastbox.SetText("Hej witamy w aplikacji <ANDROGON>  v 0.1.7 właśnie próbuję połączyć się ze sterownikiem !");
        toastbox.SetIcon(R.drawable.happyface);

        mp = MediaPlayer.create(context, R.raw.beep);

        Timer1 = new Timer();
        Timer1.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Timer1Method();
            }

        }, 0, 100);

        Timer2 = new Timer();
        Timer2.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Timer2Method();
            }

        }, 12000, 25000);


        // toastbox.StartTimeOut(7000);
    }


    // 100 ms
    private void Timer1Method()
    {

    }

    // long timer
    private void Timer2Method()
    {

    }


    void play(int resid)
    {
        try
        {
            // if (mp.isPlaying())
            // {
            mp.stop();
            mp.release();
            mp = MediaPlayer.create(getContext(), resid);
            // }
            mp.start();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    void SetVoiceTimeOut(int timeout, final int VoiceNumber)
    {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run()
            {
                switch (VoiceNumber)
                {
                    // BT.kolumna
                    case 5:
                        play(R.raw.widokkolumny);
                        break;

                    // wykresiki
                    case 7:
                        play(R.raw.wykresiki);
                        break;

                    // exitapp
                    case 8:
                        play(R.raw.exitapp);
                        break;


                }
            }
        }, timeout);
    }

    void SetDrawCircleTimeOut(int timeout)
    {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run()
            {
                DrawCircle = false;
                //invalidate();
            }
        }, timeout);

    }

    // CHECK IF BUTTON IS CLICKED
    void CheckButtons()
    {
        // EXIT X
        if(ButtonExit.Update(eventX,eventY))
        {
             new android.os.Handler().postDelayed(
                     new Runnable()
                     {
                         public void run()
                        {
                            main.viewTools.setVisibility(View.GONE);
                            main.viewTools.setAlpha(0f);

                            main.mainView.setVisibility(View.VISIBLE);
                            main.mainView.bringToFront();
                            main.mainView.animate()
                                    .alpha(1f)
                                    .setDuration(2500)
                                    .setListener(null);
                        }
                    },
                    500);
             return;
        }


        // IF CLICKED KOLUMNA
        if(ButtonUpdate.Update(eventX,eventY))
        {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run()
                        {
                            main.viewTools.setAlpha(0f);
                            main.viewTools.setVisibility(View.GONE);

                            main.viewUpdate.setVisibility(View.VISIBLE);
                            main.viewUpdate.bringToFront();

                            main.viewUpdate.animate()
                                    .alpha(1f)
                                    .setDuration(2500)
                                    .setListener(null);

                            main.viewUpdate.postInvalidate();
                        }
                    },
                    1000);

            //SetVoiceTimeOut(1500,5);
            return;
        }

        // IF CLICKED CHARTS
        if(ButtonWykres.Update(eventX,eventY))
        {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run()
                        {
                            main.viewTools.setAlpha(0f);
                            main.viewTools.setVisibility(View.GONE);

                            // main.viewChart.setVisibility(View.VISIBLE);
                            // main.viewChart.bringToFront();

                            //main.viewChart.animate()
                            //      .alpha(1f)
                            //    .setDuration(2500)
                            //  .setListener(null);

                            //main.viewChart.postInvalidate();
                            //main.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            main.plot.setVisibility(View.VISIBLE);
                            //main.btnX.setVisibility(View.VISIBLE);
                        }
                    },
                    1000);

            SetVoiceTimeOut(1500,7);
            return;
        }
    }


    void DrawBackground(Canvas canvas)
    {


        //TOP BOX
        paint.setColor(Color.rgb(0,175,10));
        paint.setStrokeWidth(5);
        canvas.drawRoundRect(top_box_rect.left,
                top_box_rect.top,
                top_box_rect.right,
                top_box_rect.bottom,
                50,
                50,paint);
        paint.setColor(Color.rgb(0,75,10));
        paint.setStrokeWidth(5);
        canvas.drawRoundRect(top_box_rect.left + 5,
                top_box_rect.top + 5,
                top_box_rect.right - 5,
                top_box_rect.bottom - 10,
                50,
                50,paint);

        //BIG BOX
        paint.setColor(Color.argb(150,0,175,10));
        paint.setStrokeWidth(5);
        canvas.drawRoundRect(big_box_rect.left,
                big_box_rect.top,
                big_box_rect.right,
                big_box_rect.bottom,
                50,
                50,
                paint);
        paint.setColor(Color.argb(180,0,65,10));
        paint.setStrokeWidth(5);
        canvas.drawRoundRect(big_box_rect.left + 5,
                big_box_rect.top + 5,
                big_box_rect.right - 5,
                big_box_rect.bottom - 10,
                50,
                50,
                paint);
        // end big box


        canvas.drawText("ESP: " + main.SERVER_IP + "  ---  " + "AndroGon v 0.2.0 \uD83D\uDC95", 50, 80, main.mTextIPPaint);

    }


    void DrawButtons(Canvas canvas)
    {
        ButtonExit.Draw(canvas);
        ButtonUpdate.Draw(canvas);
        ButtonWykres.Draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(this.getVisibility() != VISIBLE) return;

        DrawBackground(canvas);
        DrawButtons(canvas);
        toastbox.DrawTimeOut(canvas);
        if(DrawCircle == true)
        {
            canvas.drawCircle(eventX, eventY, 30, main.green);
            SetDrawCircleTimeOut(2000);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        eventX = event.getX();
        eventY = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                DrawCircle = true;
                CheckButtons();
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
    }
}

