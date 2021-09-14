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
import java.util.Calendar;
import java.util.Date;
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



public class ViewChart extends View
{

    int ButtonWidth = 0;
    int ButtonHeight = 0;

    // B.U.T.T.O.N.S >>>
    Bitmap myBitmapBtExitOn = BitmapFactory.decodeResource(getResources(), R.drawable.btsetoptionson);
    Bitmap myBitmapBtExitOff = BitmapFactory.decodeResource(getResources(), R.drawable.btsetoptionsoff);

    float eventX;
    float eventY;
    Rect ButtonExitRect;


    static final int  ButtonDistance = 50;
    boolean ButtonExitClicked = false;

    float ViewPadding;
    boolean DrawCircle = false;
    MediaPlayer mp;
    private Timer Timer1;

    // --------------|
    // ESP: GON DATAS >>>>
    // --------------|
    String XML = "";
    String TempZbiornik = "00.0";
    String TempKolumna  = "00.0";
    String TempGłowica  = "00.0";
    String TmpAlarmuZbiornik = "0.0";
    String TmpAlarmuGłowica = "0.0";
    String TmpKolumnaHistereza = "0.0";
    int MocG1 = 0;
    int MocG2 = 0;
    int MocG3 = 0;
    int Moc = 0;
    String sPower1 = "";
    String sPower2 = "";
    String sPower3 = "";
    String sCzasProcesuGodz = "";
    String sCzasProcesuMin = "";
    String sCzasProcesuSek = "";
    boolean AUTO = false;
    boolean TERMOSTAT = false;
    String strZawor = "";
    String strStatus = "";

    //Initializing integer array list;
    List<Float> TempGlowicaList = new ArrayList<Float>();


    // Main class instance
    private MainActivity main;
    private final Utilities util;


    public ViewChart(Context context,MainActivity activity,Utilities utl)
    {
        super(context);

        main = activity;
        util = utl;

        ViewPadding = (main.screenWidth - (myBitmapBtExitOff.getWidth() * 3) - (ButtonDistance * 2))/2; // first button from left
        ButtonWidth = myBitmapBtExitOff.getWidth();
        ButtonHeight = myBitmapBtExitOff.getHeight();

        ButtonExitRect = new Rect((int)main.screenWidth - ButtonWidth,
                (int)main.screenHeight - ButtonHeight,
                (int)main.screenWidth,
                (int)main.screenHeight);


        //myBitmapBackground = getResizedBitmap(myBitmapBackground, screenWidth, screenHeight - 180);
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
    }


    // 1000 ms = 1 sekunda
    private void Timer1Method()
    {
        if(this.getVisibility() == GONE) return;
       /* if(main.XML != null)
        {
            XML = main.XMLmsg;
            XML_decoder();
            TempGlowicaList.add(Float.parseFloat(TempGłowica));
            // TempGlowicaList.get(i);
            ftime  += 2;
            postInvalidate();
        }*/
    }


    void XML_decoder()
    {
        // TEMPERATURY
        TempKolumna = util.XML_Get_Data(XML,"tempkolumna");
        TempZbiornik = util.XML_Get_Data(XML,"tempbeczka");
        TempGłowica = util.XML_Get_Data(XML,"tempglowica");

        // CZAS PROCESU
        sCzasProcesuGodz = util.XML_Get_Data(XML,"sCzasProcesuGodz");
        sCzasProcesuMin = util.XML_Get_Data(XML,"sCzasProcesuMin");
        sCzasProcesuSek = util.XML_Get_Data(XML,"sCzasProcesuSek");

        // ZAWOREK
        strZawor = util.XML_Get_Data(XML,"stan_ZGon");

        TmpAlarmuZbiornik = util.XML_Get_Data(XML,"sTempAlarmuBeczka");
        TmpAlarmuGłowica = util.XML_Get_Data(XML,"sTempAlarmuGlowica");
        TmpKolumnaHistereza = util.XML_Get_Data(XML,"sHisterezaT_close");

        // POWER
        MocG1 = Integer.parseInt((util.XML_Get_Data(XML,"sMocGrzaniaG1")));
        MocG2 = Integer.parseInt((util.XML_Get_Data(XML,"sMocGrzaniaG2")));
        MocG3 = Integer.parseInt((util.XML_Get_Data(XML,"sMocGrzaniaG3")));

        // STATUS
        strStatus = util.XML_Get_Data(XML,"sStatus");

        // MOC KTÓRA GRZAŁA ILE MA WAT
        sPower1 = util.XML_Get_Data(XML,"sPower1");
        sPower2 = util.XML_Get_Data(XML,"sPower2");
        sPower3 = util.XML_Get_Data(XML,"sPower3");

        // MOC RAZEM JEŚLI KTÓRA WŁĄCZONA
        Moc = 0;
        if(sPower1.contains("ON")) Moc += MocG1;
        if(sPower2.contains("ON")) Moc += MocG2;
        if(sPower3.contains("ON")) Moc += MocG3;

        // ---===(.TERMOSTAT.)===--- "ON" "OFF"
        if(util.XML_Get_Data(XML,"sTermostat").equals("ON") == true)
        {
            TERMOSTAT = true;
        }else
        {
            TERMOSTAT = false;
        }

        // sStart == Start
        if(util.XML_Get_Data(XML,"sStart").equals("Start") == true)
        {
            AUTO = true;
        }else
        {
            AUTO = false;
        }

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
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run()
                    {
                        DrawCircle = false;
                        invalidate();
                    }
                },
                timeout);
    }

    void SetButtonTimeOut(int timeout, final int ButtonNumber)
    {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run()
                    {
                        switch (ButtonNumber)
                        {
                            // Button.exit
                            case 1:
                                ButtonExitClicked = false;

                                main.viewChart.setVisibility(View.GONE);
                                main.viewChart.setAlpha(0f);

                                main.viewTools.setVisibility(View.VISIBLE);
                                main.viewTools.bringToFront();
                                main.viewTools.animate()
                                        .alpha(1f)
                                        .setDuration(2500)
                                        .setListener(null);

                                //main.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                                break;

                            // Button.G
                            case 2:


                                break;

                            // Button.Z
                            case 3:


                                break;

                            // Button.T
                            case 4:


                                break;


                        }
                        invalidate();
                    }
                },
                timeout);
    }


    // CHECK IF BUTTON IS CLICKED
    void CheckButtons()
    {
        // IF CLICKED EXIT
        if(ButtonExitRect.contains( (int)eventX,(int)eventY) == true)
        {
            ButtonExitClicked = true;
            play(R.raw.beep);
            SetButtonTimeOut(600,1);
            return;
        }

    }

    float ftime = 0;
    void DrawChart(Canvas canvas)
    {

        float X = main.screenHeight - 50;
        float Y = main.screenWidth - 50;
        float prevX = main.screenHeight - 50;
        float prevY = main.screenWidth - 50;
        int ListSize = TempGlowicaList.size();
        for(int i = 0; i < ListSize; i++)
        {
            Y = main.screenWidth - 50 - (TempGlowicaList.get(i)*16);
            canvas.drawLine(prevX, prevY, X, Y, main.green);
            prevX = X;
            prevY = Y;
            X -= 10;
        }

    }

    void DrawTemperature(Canvas canvas)
    {
        /* ###########################################################
         samsung screen --->   width: 1440px x height:2560px
         <<<<<<<<<<< TEMPERATURY >>>>>>>>>............................
        */
        canvas.drawText(TempZbiornik, main.screenWidth / 9, main.screenHeight / 11, main.mTemperaturePaint);
        canvas.drawText("ZBIORNIK", main.screenWidth / 9, (main.screenHeight / 11) + 70, main.mTextStatusPaint);

        canvas.drawText(TempKolumna, (main.screenWidth / 3) + 100, main.screenHeight / 11, main.mTemperaturePaint);
        canvas.drawText("KOLUMNA", (main.screenWidth / 3) + 100, (main.screenHeight / 11) + 70, main.mTextStatusPaint);

        canvas.drawText(TempGłowica, ((main.screenWidth / 3) * 2) + 50, main.screenHeight / 11, main.mTemperaturePaint);
        canvas.drawText("GŁOWICA", ((main.screenWidth / 3) * 2) + 50, (main.screenHeight / 11) + 70, main.mTextStatusPaint);
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
                    main.screenWidth - ButtonWidth - 20,
                    main.screenHeight - ButtonHeight - 20, null);
        }else
        {
            canvas.drawBitmap(myBitmapBtExitOn,
                    main.screenWidth - ButtonWidth - 20,
                    main.screenHeight - ButtonHeight - 20, null);
        }


    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(this.getVisibility() != VISIBLE) return;

        canvas.drawText(Integer.toString(TempGlowicaList.size()), 450, 2225, main.mTextIPPaint);


        DrawTemperature(canvas);
        DrawButtons(canvas);
       // DrawChart(canvas);

        //plot.draw(canvas);

        if(DrawCircle == true)
        {
            canvas.drawCircle(eventX, eventY, 40, main.green);
            SetDrawCircleTimeOut(1000);
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

   }

