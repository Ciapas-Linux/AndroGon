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

import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.media.MediaPlayer;
import java.util.Timer;
import java.util.TimerTask;

public class ViewColumn extends View
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
    int ButtonWidth = 0;
    int ButtonHeight = 0;
   
    // Main class instance
    private MainActivity mainActivity;

    // T.Ł.O
    Bitmap myBitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.kolumna3);
    Bitmap myBmpCache;



    // B.U.T.T.O.N.S >>>
    Bitmap myBitmapBtExitOn = BitmapFactory.decodeResource(getResources(), R.drawable.btsetoptionson);
    Bitmap myBitmapBtExitOff = BitmapFactory.decodeResource(getResources(), R.drawable.btsetoptionsoff);
    Bitmap myBitmapBtGOn = BitmapFactory.decodeResource(getResources(), R.drawable.btgon);
    Bitmap myBitmapBtGOff = BitmapFactory.decodeResource(getResources(), R.drawable.btgoff);
    Bitmap myBitmapBtZOn = BitmapFactory.decodeResource(getResources(), R.drawable.btzon);
    Bitmap myBitmapBtZOff = BitmapFactory.decodeResource(getResources(), R.drawable.btzoff);
    Bitmap myBitmapBtTOn = BitmapFactory.decodeResource(getResources(), R.drawable.btton);
    Bitmap myBitmapBtTOff = BitmapFactory.decodeResource(getResources(), R.drawable.bttoff);

    // D.I.O.D.Y
    Bitmap myBitmapDiodeWifiOn = BitmapFactory.decodeResource(getResources(), R.drawable.wifion);
    Bitmap myBitmapDiodeWifiOff = BitmapFactory.decodeResource(getResources(), R.drawable.wifioff);

    float eventX;
    float eventY;
    Rect ButtonExitRect;
    Rect ButtonGRect;
    Rect ButtonZRect;
    Rect ButtonTRect;
    static final int  ButtonDistance = 50;
    boolean ButtonExitClicked = false;
    boolean ButtonGClicked = false;
    boolean ButtonTClicked = false;
    boolean ButtonZClicked = false;
    int ViewPadding;
    boolean DrawCircle = false;
    MediaPlayer mp;
    private Timer Timer1;
    boolean update = false;

    int TmpGlowicaX = 0;
    int TmpGlowicaY = 0;
    int TmpKolumnaX = 0;
    int TmpKolumnaY = 0;
    int ZaworX = 0;
    int ZaworY = 0;


    public ViewColumn(Context context,MainActivity activity)
    {
        super(context);

        mainActivity = activity;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        // samsung screen --->   width: 1440px x height:2560px
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        ViewPadding = (screenWidth - (myBitmapBtExitOff.getWidth() * 3) - (ButtonDistance * 2))/2; // first button from left
        ButtonWidth = myBitmapBtExitOff.getWidth();
        ButtonHeight = myBitmapBtExitOff.getHeight();

        ButtonExitRect = new Rect(screenWidth - ButtonWidth,
                screenHeight - ButtonHeight,
                screenWidth,
                screenHeight);

        ButtonGRect = new Rect(screenWidth - (ButtonWidth*2) - ButtonDistance,
                screenHeight - ButtonHeight,
                screenWidth - ButtonWidth - ButtonDistance,
                screenHeight);

        ButtonZRect = new Rect(screenWidth - (ButtonWidth*3) - ButtonDistance*2,
                screenHeight - ButtonHeight,
                screenWidth - (ButtonWidth*2) - ButtonDistance*2,
                screenHeight);

        ButtonTRect = new Rect(screenWidth - (ButtonWidth*4) - ButtonDistance*3,
                screenHeight - ButtonHeight,
                screenWidth - (ButtonWidth*3) - ButtonDistance*3,
                screenHeight);

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


        // MOC
        mTextPaintMoc = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaintMoc.setColor(Color.rgb(221,88,0));
        mTextPaintMoc.setTextSize(pxFromDp(context, 30));
        mTextPaintMoc.setFakeBoldText(true);
        mTextPaintMoc.setTypeface(typeface);

        // WIFI
        mTextPaintWifi = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaintWifi.setColor(Color.rgb(0,94,255));
        mTextPaintWifi.setTextSize(pxFromDp(context, 22));

        // ---===STATUS PAINT===---
        mTextStatusPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextStatusPaint.setColor(Color.YELLOW);
        mTextStatusPaint.setTextSize(pxFromDp(context, 15));

        // ---===SMALL TXT IP===---
        mTextIPPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextIPPaint.setColor(Color.GREEN);
        mTextIPPaint.setTextSize(pxFromDp(context, 12));

        // TEMPERATURY DIGITAL FONT
        mTemperaturePaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTemperaturePaint.setColor(Color.YELLOW);
        mTemperaturePaint.setTextSize(pxFromDp(context, 30));
        mTemperaturePaint.setFakeBoldText(true);
        mTemperaturePaint.setTypeface(typeface);

        green.setColor(Color.GREEN);
        //green.setAntiAlias(false);
        green.setStyle(Paint.Style.STROKE);

        myBitmapBackground = getResizedBitmap(myBitmapBackground, screenWidth, screenHeight);


        myBmpCache = Bitmap.createBitmap(myBitmapBackground.getWidth(),
                                        myBitmapBackground.getHeight(),
                                        Bitmap.Config.ARGB_8888);
        //myBmpCache.eraseColor(0);
        //myBmpCache.



        //CalculateObjectsPosition();

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

        new Thread( new Runnable()
        { @Override public void run()
            {
               
            }
        } ).start();

        postInvalidate();


    }



    public void CalculateObjectsPosition()
    {
        int x = 0;
        int y = 0;
        int element = 0;


        for (int i = 0; i < myBitmapBackground.getWidth(); i++)
        {
            for (int j = 0; j < myBitmapBackground.getHeight(); j++)
            {
                if ( myBitmapBackground.getPixel(i, j) == Color.argb(255,255,0,0) )
                {
                    switch(element)
                    {
                        case 0:
                        {
                            TmpGlowicaX = x;
                            TmpGlowicaY = y;
                            element++;
                            play(R.raw.beep);
                            break;
                        }

                        case 1:
                        {
                            ZaworX = x;
                            ZaworY = y;
                            element++;
                            play(R.raw.beep);
                            break;
                        }

                        case 2:
                        {
                            TmpKolumnaX = x;
                            TmpKolumnaY = y;
                            element++;
                            play(R.raw.beep);
                            break;
                        }
                    }
                }
            }
        }




    }

    /*As you can see, the method takes into account the font typeface
    and font size, as well as the text that you want to draw on screen.
    The widthToFitStringInto is a value in pixels,
    and defines the width of the area that you want to center the text within.
    int headerFontSize = 14;
    Typeface typeface = StateUtils.SCOREBOARD_FONT_SANS;
    String header = "Previous Play Calls";
    int xOffset = getApproxXToCenterText(header, typeface, headerFontSize, statusAreaWidth);
    canvas.drawText(header, xOffset, y, paint);*/
    public static int getApproxXToCenterText(String text, Typeface typeface, int fontSize, int widthToFitStringInto)
    {
        Paint p = new Paint();
        p.setTypeface(typeface);
        p.setTextSize(fontSize);
        float textWidth = p.measureText(text);
        int xOffset = (int)((widthToFitStringInto-textWidth)/2f) - (int)(fontSize/2f);
        return xOffset;
    }

    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
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

                                mainActivity.viewColumn.setVisibility(View.INVISIBLE);
                                mainActivity.viewColumn.setAlpha(0f);

                                mainActivity.viewTools.setVisibility(View.VISIBLE);
                                mainActivity.viewTools.bringToFront();
                                mainActivity.viewTools.animate()
                                        .alpha(1f)
                                        .setDuration(2500)
                                        .setListener(null);

                                break;

                            // Button.G
                            case 2:
                                ButtonGClicked = false;

                                break;

                            // Button.Z
                            case 3:
                                ButtonZClicked = false;

                                break;

                            // Button.T
                            case 4:
                                ButtonTClicked = false;

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

        // IF G CLICKED
        if(ButtonGRect.contains( (int)eventX,(int)eventY) == true)
        {
            ButtonGClicked = true;
            play(R.raw.beep);
            SetButtonTimeOut(600,2);
            return;
        }

        // IF Z CLICKED
        if(ButtonZRect.contains( (int)eventX,(int)eventY) == true)
        {
            ButtonZClicked = true;
            play(R.raw.beep);
            SetButtonTimeOut(600,3);
            return;
        }

        // IF T CLICKED
        if(ButtonTRect.contains( (int)eventX,(int)eventY) == true)
        {
            ButtonTClicked = true;
            play(R.raw.beep);
            SetButtonTimeOut(600,4);
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
                    screenWidth - ButtonWidth,
                    screenHeight - ButtonHeight, null);
        }else
        {
            canvas.drawBitmap(myBitmapBtExitOn,
                    screenWidth - ButtonWidth,
                    screenHeight - ButtonHeight, null);
        }

        // ####### -- G
        if(ButtonGClicked == false)
        {
            canvas.drawBitmap(myBitmapBtGOff,
                    screenWidth - (ButtonWidth*2) - ButtonDistance,
                    screenHeight - ButtonHeight, null);
        }else
        {
            canvas.drawBitmap(myBitmapBtGOn,
                    screenWidth - (ButtonWidth*2) - ButtonDistance,
                    screenHeight - ButtonHeight, null);
        }

        // ####### -- Z
        if(ButtonZClicked == false)
        {
            canvas.drawBitmap(myBitmapBtZOff,
                    screenWidth - (ButtonWidth*3) - ButtonDistance*2,
                    screenHeight - ButtonHeight, null);
        }else
        {
            canvas.drawBitmap(myBitmapBtZOn,
                    screenWidth - (ButtonWidth*3) - ButtonDistance*2,
                    screenHeight - ButtonHeight, null);
        }

        // ####### -- T
        if(ButtonTClicked == false)
        {
            canvas.drawBitmap(myBitmapBtTOff,
                    screenWidth - (ButtonWidth*4) - ButtonDistance*3,
                    screenHeight - ButtonHeight, null);
        }else
        {
            canvas.drawBitmap(myBitmapBtTOn,
                    screenWidth - (ButtonWidth*4) - ButtonDistance*3,
                    screenHeight - ButtonHeight, null);
        }


    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(this.getVisibility() != VISIBLE) return;

        // <<<<< BACKGROUND IMAGE >>>>>>
        canvas.drawBitmap(myBitmapBackground, 0, 0, null);


        canvas.drawText(mainActivity.TempGłowica, TmpGlowicaX, TmpGlowicaY, mTemperaturePaint);
        canvas.drawText(mainActivity.TempKolumna, TmpKolumnaX, TmpKolumnaY, mTemperaturePaint);
        canvas.drawText(mainActivity.TempZbiornik, screenWidth - 270, 2020, mTemperaturePaint);

        canvas.drawText(String.valueOf(mainActivity.Moc) + " W", (screenWidth/2) - 180, 2000, mTextPaintMoc);


        //WIFI DIODE
        if(mainActivity.ESP_connected == true)
        {
            canvas.drawBitmap(myBitmapDiodeWifiOn, 50, 2200, null);
        }else
        {
            canvas.drawBitmap(myBitmapDiodeWifiOff, 50, 2200, null);
        }
       


        DrawButtons(canvas);

        /* ###########################################################
         samsung screen --->   width: 1440px x height:2560px
         <<<<<<<<<<< TEMPERATURY >>>>>>>>>............................
        */

       // canvas.drawText("KOLUMNA:", 100, 100, mTextPaint);

       // canvas.drawText("Alarm zabiornik°C", 100, 300, mTextPaint);
       // canvas.drawText(TmpAlarmuZbiornik, 750, 300, mTemperaturePaint);

       // canvas.drawText("Alarm Głowica°C", 100, 450, mTextPaint);
       // canvas.drawText(TmpAlarmuGłowica, 750, 450, mTemperaturePaint);

        // Paint paint = new Paint();
        // paint.setColor(Color.RED);
        // paint.setStrokeWidth(5);
        // canvas.drawRect(ButtonSaveRect, paint);



        if(DrawCircle == true)
        {
            canvas.drawCircle(eventX, eventY, 40, green);
            SetDrawCircleTimeOut(1500);
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

    public static float pxFromDp(final Context context, final float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}

