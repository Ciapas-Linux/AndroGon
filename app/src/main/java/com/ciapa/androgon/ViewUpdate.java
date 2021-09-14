package com.ciapa.androgon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.codekidlabs.storagechooser.StorageChooser;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ViewUpdate extends View
{
    // Main class & Utilities instance
    public final MainActivity main;
    public final Utilities util;
    MyToastBox toastbox;

    float eventX;
    float eventY;

    int ButtonWidth;
    int ButtonHeight;

    MediaPlayer mp;

    Timer Timer1;
    Timer Timer2;

    boolean DrawCircle = false;

    // >BUTTONY<
    CButton ButtonExit;
    CButton ButtonStartUpdate;
    CButton ButtonFile;

    // >Layout coordinates<
    Rect top_box_rect;
    Rect big_box_rect;

    Paint paint;

    List<String> mSelected_files;


    public static final int FILEPICKER_PERMISSIONS = 1;

    public ViewUpdate(Context context,MainActivity activity)
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


        // RIGHT BOTTOM of screen
        ButtonExit = new CButton(this,
                main.screenWidth - ButtonWidth - (ButtonWidth/6),
                main.screenHeight  - ButtonHeight - (ButtonHeight/4),
                R.drawable.btcloseon,
                R.drawable.btcloseoff,
                R.raw.keyboard,600);

        ButtonFile = new CButton(this,
                ButtonExit.ButtonX - ButtonWidth - ButtonHeight/3,
                ButtonExit.ButtonY,
                R.drawable.btfileson,
                R.drawable.btfilesoff,
                R.raw.keyboard,600);



        // ButtonStartUpdate


        toastbox = new MyToastBox(main);
        toastbox.SetText("UWAGA !!! tutaj można dokonać aktualizacji oprogramowania sterownika.");
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


         //toastbox.StartTimeOut(3000);
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
                            main.viewUpdate.setVisibility(View.GONE);
                            main.viewUpdate.setAlpha(0f);

                            main.viewTools.setVisibility(View.VISIBLE);
                            main.viewTools.bringToFront();
                            main.viewTools.animate()
                                    .alpha(1f)
                                    .setDuration(2500)
                                    .setListener(null);
                        }
                    },
                    500);
             return;
        }


        if(ButtonFile.Update(eventX,eventY))
        {

            String[] PERMISSIONS = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            if(hasPermissions(main, PERMISSIONS))
            {
                ShowFilepicker();
            }else
            {
                ActivityCompat.requestPermissions(main, PERMISSIONS, FILEPICKER_PERMISSIONS);
            }

            return;
        }

    }

    /**
     * Helper method that verifies whether the permissions of a given array are granted or not.
     *
     * @param context
     * @param permissions
     * @return {Boolean}
     */
    public static boolean hasPermissions(Context context, String... permissions)
    {
        if (context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method that displays the filepicker of the StorageChooser.
     */
    public void ShowFilepicker()
    {
        // 1. Initialize dialog
        final StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(main)
                .withFragmentManager(main.getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setType(StorageChooser.FILE_PICKER)
                .build();

        // 2. Retrieve the selected path by the user and show in a toast !
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path)
            {
                Toast.makeText(main, "Wybrłeś plik: " + path, Toast.LENGTH_LONG).show();
            }
        });

        // 3. Display File Picker !
        chooser.show();
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
        ButtonFile.Draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
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

