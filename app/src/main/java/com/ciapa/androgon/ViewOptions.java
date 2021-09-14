package com.ciapa.androgon;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.MotionEvent;
import android.media.MediaPlayer;
import java.util.Timer;
import java.util.TimerTask;

import android.view.WindowManager;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;

public class ViewOptions extends View
{
    Paint mTextPaint;
    Paint mTextPaintCzas;
    Paint mTextPaintMoc;
    Paint mTextPaintWifi;
    Paint mTextStatusPaint;
    Paint mTextIPPaint;
    Paint mTemperaturePaint;
    Paint green = new Paint();
    int screenWidth;
    int screenHeight;

    // Main class instance
    private MainActivity main;


    // BUTTONS >>>
    Bitmap myBitmapBtExitOn = BitmapFactory.decodeResource(getResources(), R.drawable.btcloseon);
    Bitmap myBitmapBtExitOff = BitmapFactory.decodeResource(getResources(), R.drawable.btcloseoff);
    Bitmap myBitmapBtSaveOn = BitmapFactory.decodeResource(getResources(), R.drawable.btzapison);
    Bitmap myBitmapBtSaveOff = BitmapFactory.decodeResource(getResources(), R.drawable.btzapisoff);
    Bitmap myBitmapBtAdminOn = BitmapFactory.decodeResource(getResources(), R.drawable.btadminon);
    Bitmap myBitmapBtAdminOff = BitmapFactory.decodeResource(getResources(), R.drawable.btadminoff);
    float eventX;
    float eventY;
    Rect ButtonExitRect;
    Rect ButtonSaveRect;
    Rect ButtonAdminRect;
    static final int  ButtonDistance = 50;
    Rect TmpAlarmZbiornikBounds;
    boolean TmpAlarmZbiornikClicked = false;
    boolean ButtonExitClicked = false;
    boolean ButtonSaveClicked = false;
    boolean ButtonAdminClicked = false;
    int ViewPadding;
    boolean DrawCircle = false;
    MediaPlayer mp;
    private Timer Timer1;
    boolean xmlReaded = false;


    public ViewOptions(Context context,MainActivity activity)
    {
        super(context);

        main = activity;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // samsung screen --->   width: 1440px x height:2560px
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        ViewPadding = (screenWidth - (myBitmapBtAdminOff.getWidth() * 3) - (ButtonDistance * 2))/2; // first button from left

        ButtonExitRect = new Rect(screenWidth - myBitmapBtExitOff.getWidth() - ViewPadding,
                screenHeight - myBitmapBtExitOff.getHeight(),
                screenWidth - ViewPadding,
                screenHeight);

        ButtonSaveRect = new Rect(screenWidth - (myBitmapBtSaveOff.getWidth() * 2) - ButtonDistance - ViewPadding,
                screenHeight - myBitmapBtSaveOff.getHeight(),
                screenWidth - myBitmapBtSaveOff.getWidth() - ButtonDistance - ViewPadding,
                screenHeight); //:-->left, top, right, boottom

        ButtonAdminRect = new Rect(screenWidth - (myBitmapBtAdminOff.getWidth() * 3) - (ButtonDistance*2) - ViewPadding,
                screenHeight - myBitmapBtAdminOff.getHeight(),
                screenWidth - (myBitmapBtAdminOff.getWidth() * 2) - (ButtonDistance*2) - ViewPadding,
                screenHeight); //:-->left, top, right, boottom>

                //left: screenWidth - (myBitmapBtExitOff.getWidth() * 2) - ButtonDistance,



        // NORMAL TXT
        mTextPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setTextSize(pxFromDp(context, 18));

        // CZAS TXT
        mTextPaintCzas = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaintCzas.setColor(Color.RED);
        mTextPaintCzas.setTextSize(pxFromDp(context, 28));
        mTextPaintCzas.setFakeBoldText(true);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.digital);
        mTextPaintCzas.setTypeface(typeface);


        // MOC TXT
        mTextPaintMoc = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaintMoc.setColor(Color.rgb(255,107,0));
        mTextPaintMoc.setTextSize(pxFromDp(context, 22));

        // MOC WIFI
        mTextPaintWifi = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaintWifi.setColor(Color.rgb(0,94,255));
        mTextPaintWifi.setTextSize(pxFromDp(context, 22));

        // ---===STATUS PAINT===---
        mTextStatusPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextStatusPaint.setColor(Color.YELLOW);
        mTextStatusPaint.setTextSize(pxFromDp(context, 15));

        // SMALL TXT IP
        mTextIPPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextIPPaint.setColor(Color.GREEN);
        mTextIPPaint.setTextSize(pxFromDp(context, 12));

        // TEMPERATURY DIGITAL FONT
        mTemperaturePaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTemperaturePaint.setColor(Color.YELLOW);
        mTemperaturePaint.setTextSize(pxFromDp(context, 25));
        mTemperaturePaint.setFakeBoldText(true);
        mTemperaturePaint.setTypeface(typeface);

        TmpAlarmZbiornikBounds = new Rect();
        String sTmpAlarmZbiornik = "Alarm zabiornik°C";
        mTextPaint.getTextBounds(sTmpAlarmZbiornik, 0, sTmpAlarmZbiornik.length(), TmpAlarmZbiornikBounds );

        TmpAlarmZbiornikBounds.left = 100 - 20;
        TmpAlarmZbiornikBounds.top = 300 - 20 - TmpAlarmZbiornikBounds.height();
        TmpAlarmZbiornikBounds.right = 100 + 70 + TmpAlarmZbiornikBounds.width();
        TmpAlarmZbiornikBounds.bottom = 300 + 20;


        green.setColor(Color.GREEN);
        //green.setAntiAlias(false);
        green.setStyle(Paint.Style.STROKE);


        mp = MediaPlayer.create(context, R.raw.beep);


        Timer1 = new Timer();
        Timer1.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Timer1Method();
            }

        }, 0, 1000);

        postInvalidate();


    }

    protected void showInputDialog()
    {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editText = promptView.findViewById(R.id.edittext);
        //editText.setTransformationMethod(new NumericKeyBoardTransformationMethod());
        //editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        KeyListener keyListener = DigitsKeyListener.getInstance("1234567890.");
        editText.setKeyListener(keyListener);
       

      /*  editText.setOnKeyListener(new OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                play(R.raw.beep);
                return false;
            }
        });*/

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("TAK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                       main.TmpAlarmuZbiornik = editText.getText().toString();
                        play(R.raw.beep);
                    }
                })
                .setNegativeButton("NIE",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                                play(R.raw.beep);
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

    // 1000 ms = 1 sekunda
    private void Timer1Method()
    {
       postInvalidate();
    }


    void play(int resid)
    {
        try
        {
            if (mp.isPlaying())
            {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(getContext(), resid);
            }
            mp.start();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void SetDrawCircleTimeOut(int timeout)
    {
       new Handler(Looper.getMainLooper()).postDelayed(() ->
       {
           DrawCircle = false;
           //invalidate();
       }, timeout);
    }

    void SetButtonTimeOut(int timeout, final int ButtonNumber)
    {
        new Handler(Looper.getMainLooper()).postDelayed(() ->
        {
            switch (ButtonNumber)
            {
                // Button.exit
                case 1:
                    ButtonExitClicked = false;

                    main.viewSettings.setVisibility(View.GONE);
                    main.viewSettings.setAlpha(0f);

                    main.mainView.setVisibility(View.VISIBLE);
                    main.mainView.bringToFront();
                    main.mainView.animate()
                            .alpha(1f)
                            .setDuration(2500)
                            .setListener(null);

                    break;

                // Txt --> AlarmZbiornik Clicked
                case 2:
                    TmpAlarmZbiornikClicked = false;

                    break;
                // Button.Save
                case 3:
                    ButtonSaveClicked = false;

                    break;

                // Button.Save
                case 4:
                    ButtonAdminClicked = false;

                    break;

            }
            invalidate();
        }, timeout);
     }


    // CHECK IF BUTTON IS CLICKED
    void CheckButtons()
    {
        // IF CLICKED EXIT
        if(ButtonExitRect.contains( (int)eventX,(int)eventY) == true)
        {
            ButtonExitClicked = true;
            xmlReaded = false;
            play(R.raw.beep);
            SetButtonTimeOut(600,1);
            return;
        }

        // IF CLICKED SAVE
        if(ButtonSaveRect.contains( (int)eventX,(int)eventY) == true)
        {
            ButtonSaveClicked = true;
            play(R.raw.beep);
            SetButtonTimeOut(600,3);
            return;
        }

        // IF CLICKED ADMIN
        if(ButtonAdminRect.contains( (int)eventX,(int)eventY) == true)
        {
            ButtonAdminClicked = true;
            play(R.raw.beep);
            SetButtonTimeOut(600,4);
            return;
        }

        // IF CLICKED TmpAlarmZbiornikBounds
        if(TmpAlarmZbiornikBounds.contains( (int)eventX,(int)eventY) == true)
        {
            TmpAlarmZbiornikClicked = true;
            play(R.raw.beep);
            showInputDialog();

            SetButtonTimeOut(600,2);
            return;
        }
    }

    // {----------------------------------------}
    // {<<<<<<<< ...(B.U.T.T.O.N.S)... >>>>>>>>>}
    // {----------------------------------------}
    void DrawButtons(Canvas canvas)
    {
        // ####### -- E.X.I.T
        if(ButtonExitClicked == false)
        {
            canvas.drawBitmap(myBitmapBtExitOff,
                     screenWidth - myBitmapBtExitOff.getWidth() - ViewPadding, //1335
                     screenHeight - myBitmapBtExitOff.getHeight(), null);  // x: 950 y: 2270
        }else
        {
            canvas.drawBitmap(myBitmapBtExitOn,
                    screenWidth - myBitmapBtExitOn.getWidth() - ViewPadding,
                    screenHeight - myBitmapBtExitOn.getHeight(), null);  // x: 950 y: 2270
        }

        // ####### -- S.A.V.E
        if(ButtonSaveClicked == false)
        {
            canvas.drawBitmap(myBitmapBtSaveOff,
                    screenWidth - (myBitmapBtSaveOff.getWidth() * 2) - ButtonDistance - ViewPadding, //1650
                    screenHeight - myBitmapBtSaveOff.getHeight(), null);  // x: 950 y: 2270
        }else
        {
            canvas.drawBitmap(myBitmapBtSaveOn,
                    screenWidth - (myBitmapBtSaveOff.getWidth() * 2) - ButtonDistance - ViewPadding,
                    screenHeight - myBitmapBtSaveOn.getHeight(), null);  // x: 950 y: 2270
        }

        // ####### -- A.D.M.I.N
        if(ButtonAdminClicked == false)
        {
            canvas.drawBitmap(myBitmapBtAdminOff,
                    screenWidth - (myBitmapBtAdminOff.getWidth() * 3) - (ButtonDistance * 2) - ViewPadding,
                    screenHeight - myBitmapBtAdminOff.getHeight(), null);  // x: 950 y: 2270
        }else
        {
            canvas.drawBitmap(myBitmapBtAdminOn,
                    screenWidth - (myBitmapBtAdminOff.getWidth() * 3) - (ButtonDistance * 2) - ViewPadding,
                    screenHeight - myBitmapBtAdminOn.getHeight(), null);  // x: 950 y: 2270
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(this.getVisibility() != VISIBLE) return;

        DrawButtons(canvas);

        /* ###########################################################
         samsung screen --->   width: 1440px x height:2560px
         <<<<<<<<<<< TEMPERATURY >>>>>>>>>............................
        */

        canvas.drawText("USTAWIENIA:", 100, 100, mTextPaint);

        canvas.drawText("Alarm zabiornik°C", 100, 300, mTextPaint);
        canvas.drawText(main.TmpAlarmuZbiornik, 750, 300, mTemperaturePaint);

        canvas.drawText("Alarm Głowica°C", 100, 450, mTextPaint);
        canvas.drawText(main.TmpAlarmuGłowica, 750, 450, mTemperaturePaint);

        // Paint paint = new Paint();
        // paint.setColor(Color.RED);
        // paint.setStrokeWidth(5);
        // canvas.drawRect(ButtonSaveRect, paint);

        if(DrawCircle == true)
        {
            canvas.drawCircle(eventX, eventY, 40, green);
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
                CheckButtons();
                DrawCircle = true;
            return true;

            case MotionEvent.ACTION_MOVE:

            break;

            case MotionEvent.ACTION_UP:

            break;

            default:
            return false;
        }
        invalidate();
        return true;
    }

   /* @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_SPACE
                || keyCode == KeyEvent.KEYCODE_ENTER
                || keyCode == KeyEvent.KEYCODE_PERIOD)
        {
            play(R.raw.beep);
        }
        return super.onKeyUp(keyCode, event);
    }*/

    public static float pxFromDp(final Context context, final float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}
