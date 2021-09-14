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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ViewWifi extends View
{
    // Main class & Utilities instance
    public final MainActivity main;
    public final Utilities util;

    float eventX;
    float eventY;

    int ButtonWidth;
    int ButtonHeight;
    int ButtonArrowWidth;
    int ButtonArrowHeight;

    MediaPlayer mp;

    Timer Timer1;
    Timer Timer2;

    boolean DrawCircle = false;


    float StatusXPos;
    float StatusYPos;

    // >BUTTONY<
    CButton ButtonExit;
    CButton ButtonScan;
    CButton ButtonScrolUp;
    CButton ButtonScrolDown;
    CButton ButtonSaveWifiData;

    TextSuper text_list;
    TextSuper text_ssid;
    TextSuper text_pass;

    int ButtonDistance = 50;

    // >Layout coordinates<
    Rect top_box_rect;
    Rect big_box_rect;

    Paint paint;
    List<String> NetworkList;
    Bitmap BitmapWifi = BitmapFactory.decodeResource(getResources(), R.drawable.wifi_icon);
    String stmp;
    String input_ssid;
    String input_pass;
    String XML;

    public ViewWifi(Context context,MainActivity activity)
    {
        super(context);
        main = activity;
        util = main.util;
        paint = new Paint();
        NetworkList = new ArrayList<>();

        text_list = new TextSuper("q",
                Color.rgb(0,255,0),
                this,
                0,
                0,
                15);

        text_ssid = new TextSuper("q",
                Color.rgb(25,20,255),
                this,
                0,
                0,
                20);

        text_pass = new TextSuper("q",
                Color.rgb(25,20,255),
                this,
                0,
                0,
                20);

        NetworkList.clear();

        ButtonWidth = BitmapFactory.decodeResource(getResources(), R.drawable.btstarton).getWidth();
        ButtonHeight = BitmapFactory.decodeResource(getResources(), R.drawable.btstarton).getHeight();
        ButtonArrowWidth = BitmapFactory.decodeResource(getResources(), R.drawable.btarrow_up_off).getWidth();
        ButtonArrowHeight = BitmapFactory.decodeResource(getResources(), R.drawable.btarrow_up_off).getHeight();

        top_box_rect = new Rect((int)(main.screenWidth - util.Percent(99,main.screenWidth)),
                (int)(main.screenHeight - util.Percent(99,main.screenHeight)),
                (int)(main.screenWidth - util.Percent(1,main.screenWidth)),
                (int)(main.screenHeight - util.Percent(80,main.screenHeight)));

        big_box_rect = new Rect((int)(main.screenWidth - util.Percent(99,main.screenWidth)),
                top_box_rect.bottom + 50,
                (int)(main.screenWidth - util.Percent(1,main.screenWidth)),
                (int)(main.screenHeight - util.Percent(1,main.screenHeight)));
                //width: 2560

        //centreX = (canvasWidth  - bitmapWidth) /2
        //centreY = (canvasHeight - bitmapHeight) /2

        Rect result = new Rect();
        String finalVal ="H";
        main.mTextStatusPaint.getTextBounds(finalVal, 0, finalVal.length(), result);
        StatusXPos = big_box_rect.left + result.width()*2;
        StatusYPos = big_box_rect.top + result.height()*2;

        // CENTER
        ButtonScan = new CButton(this,
                (main.screenWidth  - ButtonWidth) * 0.5f,
                main.screenHeight  - ButtonHeight - (ButtonHeight/4),
                R.drawable.btwifiscanon,
                R.drawable.btwifiscanoff,
                R.raw.keyboard,600);

        // RIGHT
        ButtonExit = new CButton(this,
                ButtonScan.ButtonX + ButtonWidth + ButtonDistance,
                main.screenHeight  - ButtonHeight - (ButtonHeight/4),
                R.drawable.btcloseon,
                R.drawable.btcloseoff,
                R.raw.keyboard,600);


        // LEFT
        ButtonSaveWifiData = new CButton(this,
                ButtonScan.ButtonX - ButtonWidth - ButtonDistance,
                main.screenHeight  - ButtonHeight - (ButtonHeight/4),
                R.drawable.btwifisaveon,
                R.drawable.btwifisaveoff,
                R.raw.keyboard,600);

        ButtonScrolUp = new CButton(this,
                  main.screenWidth  - ButtonArrowWidth - (ButtonArrowWidth/4),
                  big_box_rect.top  + ButtonArrowHeight / 3,
                  R.drawable.btarrow_up_on,
                  R.drawable.btarrow_up_off,
                  R.raw.keyboard,600);

        ButtonScrolDown = new CButton(this,
                main.screenWidth  - ButtonArrowWidth - (ButtonArrowWidth/4),
                big_box_rect.bottom  - (ButtonArrowHeight * 2) - ButtonArrowWidth,
                R.drawable.btarrow_down_on,
                R.drawable.btarrow_down_off,
                R.raw.keyboard,600);


        mp = MediaPlayer.create(context, R.raw.beep);

        Timer1 = new Timer();
        Timer1.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Timer1Method();
            }

        }, 0, 200);

        Timer2 = new Timer();
        Timer2.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                //Timer2Method();
            }

        }, 7000, 7000);

    }

    public void UpdateNetworkList()
    {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run()
            {
                GetNetworkList();
                postInvalidate();
            }
        }, 5000);
    }


    // 200 ms = GUI UPDATE
    private void Timer1Method()
    {
        if(this.getVisibility() == VISIBLE)
        {
            postInvalidate();
        }
    }

    // long timer
    private void Timer2Method()
    {
        if(this.getVisibility() == VISIBLE)
        {
            if(NetworkList.size() <= 0)
            {
                //main.JSON_network_scan = "";
                main.webSocketClient.send("WIFISCAN");
                UpdateNetworkList();
            }
        }
    }

    void GetNetworkList()
    {
        if (main.JSON_network_scan.contains("SCAN:") == true)
        {
            NetworkList.clear();
            String stmp = "";
            String[] separated = main.JSON_network_scan.split(":");
            for (int i = 1; i < separated.length; i++) {
                stmp += separated[i];
                stmp += "  S: ";
                stmp += separated[i + 1];
                stmp += " dBm";
                  /* if(main.sSSID.contains(separated[i]) == true)
                    {
                        stmp += " *";
                    }*/
                i += 2;
                NetworkList.add(stmp);
                stmp = "";
            }
         }
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

    void buildXML()
    {
        XML = "<WIFIDATA>";
        if(input_ssid == "")
        {
            XML += "<sSSID>" + "brak" + "</sSSID>";
        }else
        {
            XML += "<sSSID>" + input_ssid + "</sSSID>";
        }
        if(input_pass == "")
        {
            XML += "<sPASS>" + "brak" + "</sPASS>";
         }else
        {
            XML += "<sPASS>" + input_pass + "</sPASS>";
        }
        XML += "</WIFIDATA>";
    }

    // CHECK IF BUTTON IS CLICKED
    void CheckButtons()
    {
        // EXIT X
        if(ButtonExit.Update(eventX,eventY))
        {
            main.JSON_network_scan = "";
             new android.os.Handler(Looper.getMainLooper()).postDelayed(
                     new Runnable()
                     {
                         public void run()
                        {
                            main.viewWifi.setVisibility(View.GONE);
                            main.viewWifi.setAlpha(0f);

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

        // SCAN NETWORK
        if(ButtonScan.Update(eventX,eventY))
        {
            main.webSocketClient.send("WIFISCAN");
            UpdateNetworkList();
            return;
        }

        // SAVE WIFI DATAS TO MICROCONTROLLER
        if(ButtonSaveWifiData.Update(eventX,eventY))
        {
            buildXML();
            play(R.raw.drop_004);
            main.webSocketClient.send(XML);
            return;
        }

        // SCROLL UP
        if(ButtonScrolUp.Update(eventX,eventY))
        {
            ScrollListUp();
            return;
        }

        // SCROLL DOWN
        if(ButtonScrolDown.Update(eventX,eventY))
        {
            ScrollListDown();
            return;
        }

        if(text_ssid.Update(eventX,eventY))
        {
            //Toast.makeText(main.getApplicationContext(),"ssid",Toast.LENGTH_SHORT).show();
            play(R.raw.drop_004);
            showInputDialogSsid();
            return;
        }

        if(text_pass.Update(eventX,eventY))
        {
            play(R.raw.drop_004);
            showInputDialogPass();
            //Toast.makeText(main.getApplicationContext(),"pass",Toast.LENGTH_SHORT).show();
            return;
        }

    }

    protected void showInputDialogPass()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        View promptView = layoutInflater.inflate(R.layout.input_dialog_txt_haslo, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        final EditText editText = promptView.findViewById(R.id.edittext3);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("TAK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        input_pass = editText.getText().toString();
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

    protected void showInputDialogSsid()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        View promptView = layoutInflater.inflate(R.layout.input_dialog_txt_nazwa_sieci, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        final EditText editText = promptView.findViewById(R.id.edittext2);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("TAK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        input_ssid = editText.getText().toString();
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


    }


    void DrawButtons(Canvas canvas)
    {
        ButtonExit.Draw(canvas);
        ButtonScan.Draw(canvas);
        ButtonScrolUp.Draw(canvas);
        ButtonScrolDown.Draw(canvas);
        ButtonSaveWifiData.Draw(canvas);
    }

    void ScrollListUp()
    {
        if(NetworkList.size() > 0)
        {
          Collections.rotate(NetworkList, -1);
        }
    }

    void ScrollListDown()
    {
        if(NetworkList.size() > 0)
        {
          Collections.rotate(NetworkList, 1);
        }
    }

    void DrawList(Canvas canvas)
    {
        float distance_cntr = 3.8f;
        if(NetworkList.size() > 0)
        {
            for (int i = 0; i < NetworkList.size(); i++)
            {
                if(i > 10) break;
                stmp = NetworkList.get(i);
                if(stmp.contains(main.sSSID) == true)
                {
                    text_list.SetText(stmp,StatusXPos,StatusYPos + text_list.TextHeight * distance_cntr + text_list.TextHeight);
                    text_list.SetColor(Color.rgb(250,0,5));
                    text_list.Draw(canvas);

                }else
                {
                    text_list.SetText(stmp,StatusXPos,StatusYPos + text_list.TextHeight * distance_cntr + text_list.TextHeight);
                    text_list.SetColor(Color.rgb(20,255,15));
                    text_list.Draw(canvas);
                }
                distance_cntr = distance_cntr + 1.7f;
            }
        }

    }

    void DrawText(Canvas canvas)
    {
        canvas.drawText("ESP: " + main.SERVER_IP + "  ---  " + "AndroGon v 0.2.0 \uD83D\uDC95", top_box_rect.left + 50, top_box_rect.top + 50, main.mTextIPPaint);

        text_ssid.SetText("Sieć: " + input_ssid,top_box_rect.left + 50,top_box_rect.top + text_ssid.TextHeight * 3);
        text_ssid.SetColor(Color.rgb(25,5,255));
        text_ssid.Draw(canvas);

        text_pass.SetText("Hasło: " + input_pass,top_box_rect.left + 50,top_box_rect.top + text_ssid.TextHeight * 5);
        text_pass.SetColor(Color.rgb(25,5,255));
        text_pass.Draw(canvas);

        canvas.drawText("Obecnie dostępne sieci wifi:    " + NetworkList.size(), StatusXPos, StatusYPos + main.mTextPaintHeight * 2 + main.mTextPaintHeight/2, main.mTextStatusPaint);
        canvas.drawText("Siła sygnału:   " + main.SignalStrength + "  dBm", StatusXPos, StatusYPos, main.mTextStatusPaint);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility)
    {
        super.onVisibilityChanged(changedView, visibility);
            switch (visibility)
            {
                case VISIBLE:
                    main.webSocketClient.send("WIFISCAN");
                    UpdateNetworkList();
                    Toast.makeText(main.getApplicationContext(),"Szukam sieci",Toast.LENGTH_SHORT).show();
                    input_ssid = main.sSSID;
                    input_pass = main.sPASS;
                    break;

                case INVISIBLE:
                    //Toast.makeText(main.getApplicationContext(),"invisible",Toast.LENGTH_SHORT).show();
                break;

                case GONE:
                    //Toast.makeText(main.getApplicationContext(),"gone",Toast.LENGTH_SHORT).show();
                    NetworkList.clear();
                break;
            }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(this.getVisibility() != VISIBLE) return;

        DrawBackground(canvas);
        DrawText(canvas);
        DrawList(canvas);
        DrawButtons(canvas);

        main.mainView.sprite1.DrawTimeOutAnimate(canvas);

       /* canvas.drawRoundRect(text_ssid.TextClickRect.left,
                text_ssid.TextClickRect.top,
                text_ssid.TextClickRect.right,
                text_ssid.TextClickRect.bottom,
                5,
                5,paint);*/


       // toastbox.DrawTimeOut(canvas);
        //canvas.drawBitmap(BitmapWifi, 0, 0, null);

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

