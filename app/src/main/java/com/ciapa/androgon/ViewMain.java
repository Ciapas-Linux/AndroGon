package com.ciapa.androgon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.MotionEvent;
import android.media.MediaPlayer;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

//import pl.droidsonroids.gif.GifDrawable;
//import pl.droidsonroids.gif.GifImageView;


public class ViewMain extends View
{
    // Main class & Utilities instance
    public final MainActivity main;
    public final Utilities util;

    Sprite sprite1;

    MyToastBox toastbox;

    // TŁO
    Bitmap myBitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.bkgnd1);

    // Tuba
    Bitmap myBitmapTuba = BitmapFactory.decodeResource(getResources(), R.raw.tube1);
    //Bitmap myBitmapTuba2 = BitmapFactory.decodeResource(getResources(), R.raw.tube2);

    float eventX;
    float eventY;

    int ButtonDistance = 50;
    int ButtonWidth;
    int ButtonHeight;
    int DiodeWidth;
    int DiodeHeight;

    MediaPlayer mp;


    private long mLastTime = 0;
    private int fps = 0, ifps = 0;

    Timer Timer1;
    Timer Timer2;
    Timer Timer3;
    Timer Timer4;

    float StatusXPos;
    float StatusYPos;

    boolean VoiceConnectedPlayOnce = false;
    boolean VoiceTermostatPlayOnce = false;
    boolean VoiceInteligentnyPlayOnce = false;
    boolean VoiceEtap1PlayOnce = false;
    boolean VoiceEtap2PlayOnce = false;
    boolean VoiceEtap3PlayOnce = false;
    boolean VoiceEtap4PlayOnce = false;
    boolean VoiceEtap5PlayOnce = false;

    boolean DrawCircle = false;


    // >BUTTONY<
    CButton ButtonStart;
    CButton ButtonStop;
    CButton ButtonGrzanie;
    CButton ButtonExit;
    CButton ButtonOpcje;
    CButton ButtonWifi;
    CButton ButtonKolumna;
    CButton ButtonTools;
    CButton ButtonZawor;

    // >DIODY<
    Diode WifiDiode;
    Diode TermostatDiode;
    Diode AutomaticDiode;
    Diode ErrorDiode;

    Diode Pwr1Diode;
    Diode Pwr2Diode;
    Diode Pwr3Diode;
    int PwrDiodeWidth;
    int PwrDiodeHeight;


    // >Layout coordinates<
    Rect top_box_rect;
    Rect big_box_rect;
    Rect diodes_box_rect;

    boolean enable_screen_button = false;



    public ViewMain(Context context,MainActivity activity,Utilities utl)
    {
        super(context);

        main = activity;
        util = utl;

        DiodeWidth = BitmapFactory.decodeResource(getResources(), R.drawable.wifion).getWidth();
        DiodeHeight = BitmapFactory.decodeResource(getResources(), R.drawable.wifion).getHeight();

        PwrDiodeWidth = BitmapFactory.decodeResource(getResources(), R.drawable.g1on2).getWidth();
        PwrDiodeHeight =  BitmapFactory.decodeResource(getResources(), R.drawable.g1on2).getHeight();

        ButtonWidth = BitmapFactory.decodeResource(getResources(), R.drawable.btstarton).getWidth();
        ButtonHeight = BitmapFactory.decodeResource(getResources(), R.drawable.btstarton).getHeight();

        top_box_rect = new Rect((int)(main.screenWidth - util.Percent(99,main.screenWidth)),
                (int)(main.screenHeight - util.Percent(99,main.screenHeight)),
                (int)(main.screenWidth - util.Percent(1,main.screenWidth)),
                (int)(main.screenHeight - util.Percent(86,main.screenHeight)));

        big_box_rect = new Rect(top_box_rect.left,
                top_box_rect.bottom + 50,
                top_box_rect.right,
                //(int)(main.screenHeight / 1.77f ));
                (int)util.Percent(55,main.screenHeight));

        diodes_box_rect = new Rect(big_box_rect.left,
                big_box_rect.bottom - DiodeHeight - 50,
                DiodeWidth * 4,
                big_box_rect.bottom);


        Rect result = new Rect();
        String finalVal ="H";
        main.mTextStatusPaint.getTextBounds(finalVal, 0, finalVal.length(), result);
        StatusXPos = big_box_rect.left + result.width()*2;
        StatusYPos = big_box_rect.top + result.height()*2;


        myBitmapTuba = util.getResizedBitmap(myBitmapTuba,
                (int)(main.screenWidth * 0.6f),
                myBitmapTuba.getHeight()/2);


        // <-FIRST LINE-> first button is center in width of screen
        ButtonStop = new CButton(this,
                (main.screenWidth  - ButtonWidth) * 0.5f,
                big_box_rect.bottom + 25,
                R.drawable.btstopon,
                R.drawable.btstopoff,
                R.raw.keyboard,600);

        ButtonStart = new CButton(this,
                ButtonStop.ButtonX - ButtonWidth - ButtonDistance,
                big_box_rect.bottom + 25,
                R.drawable.btstarton,
                R.drawable.btstartoff,
                R.raw.keyboard,600);

        ButtonGrzanie = new CButton(this,
                ButtonStop.ButtonX + ButtonWidth + ButtonDistance,
                big_box_rect.bottom + 25,
                R.drawable.btgrzanieon,
                R.drawable.btgrzanieoff,
                R.raw.keyboard,600);
                //----------------------------------- end 1 line

        // <-SECOND LINE-> first button is center in width of screen
        ButtonWifi = new CButton(this,
                (main.screenWidth  - ButtonWidth) * 0.5f,
                ButtonStop.ButtonY + ButtonHeight + ButtonDistance,
                R.drawable.btwifion,
                R.drawable.btwifioff,
                R.raw.keyboard,600);

        ButtonOpcje = new CButton(this,
                ButtonWifi.ButtonX + ButtonWidth + ButtonDistance,
                ButtonStop.ButtonY + ButtonHeight + ButtonDistance,
                R.drawable.btopcjeon,
                R.drawable.btopcjeoff,
                R.raw.keyboard,600);

        ButtonKolumna = new CButton(this,
                ButtonWifi.ButtonX - ButtonWidth - ButtonDistance,
                ButtonStop.ButtonY + ButtonHeight + ButtonDistance,
                R.drawable.btkolumnaon,
                R.drawable.btkolumnaoff,
                R.raw.keyboard,600);

        ButtonTools = new CButton(this,
                ButtonWifi.ButtonX - ButtonWidth - ButtonDistance,
                ButtonKolumna.ButtonY + ButtonHeight + ButtonDistance,
                R.drawable.bttoolson,
                R.drawable.bttoolsoff,
                R.raw.keyboard,600);

        ButtonZawor = new CButton(this,
                (main.screenWidth  - ButtonWidth) * 0.5f,
                ButtonStop.ButtonY + (ButtonHeight*2) + (ButtonDistance*2),
                R.drawable.btzaworon,
                R.drawable.btzaworoff,
                R.raw.keyboard,600);

        ButtonExit = new CButton(this,
                ButtonWifi.ButtonX + ButtonWidth + ButtonDistance,
                ButtonStop.ButtonY + (ButtonHeight*2) + (ButtonDistance*2),
                R.drawable.btcloseon,
                R.drawable.btcloseoff,
                R.raw.keyboard,600);



        // D.I.O.D.Y CREATE
        WifiDiode = new Diode(this,diodes_box_rect.left + 10,
                diodes_box_rect.bottom - DiodeHeight - 20,
                40,
                40,
                R.drawable.wifion,
                R.drawable.wifioff);

        TermostatDiode = new Diode(this,diodes_box_rect.left + DiodeWidth + 10,
                diodes_box_rect.bottom - DiodeHeight - 20,
                40,
                40,
                R.drawable.term_diode_on,
                R.drawable.term_diode_off);

        AutomaticDiode = new Diode(this,diodes_box_rect.left + DiodeWidth * 2 + 10,
                diodes_box_rect.bottom - DiodeHeight - 20,
                40,
                40,
                R.drawable.autoonn,
                R.drawable.autooff);

        ErrorDiode = new Diode(this,diodes_box_rect.left + DiodeWidth * 3 + 10,
                diodes_box_rect.bottom - DiodeHeight - 20,
                40,
                40,
                R.drawable.redon,
                R.drawable.redoff);


        Pwr1Diode = new Diode(this,big_box_rect.right - (PwrDiodeWidth) - 5,
                big_box_rect.top + PwrDiodeHeight,
                60,
                40,
                R.drawable.g1on2,
                R.drawable.g1off2);

        Pwr2Diode = new Diode(this,big_box_rect.right - (PwrDiodeWidth) - 5,
                big_box_rect.top + PwrDiodeHeight * 2 + 5,
                60,
                40,
                R.drawable.g2on2,
                R.drawable.g2off2);

        Pwr3Diode = new Diode(this,big_box_rect.right - (PwrDiodeWidth) - 5,
                big_box_rect.top + PwrDiodeHeight * 3 + 5,
                60,
                40,
                R.drawable.g3on2,
                R.drawable.g3off2);


        //:  activity,int resID, int numframes,int cntx,int cnty,int duration,int scale)
        /*sprite1 = new Sprite(main,
                             R.raw.avatar1,
                             52,
                             52,
                             1,
                             200,
                             1);*/

        /*sprite1 = new Sprite(main,
                R.raw.kotsprites,
                5,
                5,
                1,
                200,
                1);*/


        sprite1 = new Sprite(main,
                R.raw.avatar1,
                20,
                5,
                4,
                200,
                1);

        sprite1.SetSpritePosition((main.screenWidth  - sprite1.FRAME_W) * 0.5f,
                main.screenHeight  - (sprite1.FRAME_H * 2.5f));

        sprite1.SetFrameOn(true);
        sprite1.SetBackgroundOn(true,10);

        toastbox = new MyToastBox(main);
        toastbox.SetText("Hej witamy w aplikacji <ANDROGON>  v 0.2.0 właśnie próbuję połączyć się ze sterownikiem !");
        toastbox.SetIcon(R.drawable.happyface);


        //myBitmapBackground = util.decodeSampledBitmapFromResource(this.getResources(),R.drawable.bkgnd1,(int)screenWidth,(int)screenHeight);
        //myBitmapBackground = util.resizeBitmap(myBitmapBackground,(int)screenWidth,(int)screenHeight);
        myBitmapBackground = util.getResizedBitmap(myBitmapBackground,(int)main.screenWidth,(int)main.screenHeight);

        mp = MediaPlayer.create(context, R.raw.beep);

      /*  Timer1 = new Timer();
        Timer1.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
               Timer1Method();
            }

        }, 0, 100);*/

        //CONNECTION
        Timer2 = new Timer();
        Timer2.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
               Timer2Method();
            }

        }, 12000, 25000);

        // TERMOSTAT
        Timer3 = new Timer();
        Timer3.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Timer3Method();
            }

        }, 5000, 7000);

        // ETAP
        Timer4 = new Timer();
        Timer4.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Timer4Method();
            }

        }, 45000, 30000);



       /* Toasty.Config.getInstance()
              .tintIcon(true)
              .setTextSize(19)
              .apply();
        Toasty.info(context, "<< AndroGon v 0.1.6 \uD83D\uDC95 >>", Toast.LENGTH_LONG, true).setGravity(Gravity.CENTER, 0, 0);
*/

        //Toast toast = Toast.makeText(main,"<< ANDROGON v 0.1.7 \uD83D\uDC95 >>", Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        //toast.show();

        // STARTUP SOUND PLAY
        play(R.raw.xpstart);

          // this.animate()
           //    .translationXBy(-360)
            //   .setDuration(3000)
            //   .setListener(null);


        //MyToastBox START display on main view
        toastbox.StartTimeOut(7000);

        // ***********************************
        // enable button in main view ........
        // ***********************************
      /*  new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run()
            {
                enable_screen_button = true;
            }
        }, 10000);*/

        enable_screen_button = true;

    }

    public void showQuitDialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
        alert.setTitle("Czy chcesz zakończyć ?  \uD83E\uDD23");
        alert.setMessage("zamknąć aplikację co ?");
        alert.setCancelable(false);
        alert.setNegativeButton(" NIE !!! \uD83D\uDE42", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast toast = Toast.makeText(main , "ok nie wyłączamy \uD83D\uDE42", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        alert.setPositiveButton(" TAK !!! \uD83D\uDE32", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast toast = Toast.makeText(main, "Do zobaczenia \uD83D\uDE32", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                main.QuitApp();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
        View layoutView = main.getLayoutInflater().inflate(R.layout.dialog_negative_layout, null);
    }

    public void showErrorDialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
        View layoutView = main.getLayoutInflater().inflate(R.layout.dialog_negative_layout, null);
        Button dialogButton = layoutView.findViewById(R.id.btnDialog);
        alert.setView(layoutView);
        AlertDialog dialog = alert.create();
        dialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    // 100 ms
    private void Timer1Method()
    {

    }

    // long timer
    private void Timer2Method()
    {
            if(main.network_state == true)
            {
                if(main.ESP_connected == false)
                {
                    play(R.raw.brakpolaczenia);
                    main.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            VoiceConnectedPlayOnce = false;
                            Toast toast = Toast.makeText(main, "Brak połączenia ze sterownikiem \uD83D\uDE32" + "\n" + "być może trzeba włączyć sterownik!", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            sprite1.StartTimeOut(2500);
                        }
                    });
                }else
                {
                    if(VoiceConnectedPlayOnce == false)
                    {
                        play(R.raw.mampolaczenie);
                        VoiceConnectedPlayOnce = true;
                        main.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast toast = Toast.makeText(main, "Wspaniale połączono ze sterownikiem \uD83D\uDE01", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                sprite1.StartTimeOut(2500);
                            }
                        });
                    }
                }
            }else
            {
                    play(R.raw.brakpolaczeniawifi);
                    main.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            VoiceConnectedPlayOnce = false;
                            Toast toast = Toast.makeText(main, "Połącz się z tą sama siecią WIFI" + "\n" +"co sterownik \uD83D\uDE32", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            sprite1.StartTimeOut(2500);
                        }
                    });
            }


    }

    // 8 sek
    private void Timer3Method()
    {
        if(main.TERMOSTAT == true)
        {
            if(VoiceTermostatPlayOnce == false)
            {
                SetVoiceTimeOut(3000, 10);
                VoiceTermostatPlayOnce = true;
            }
        }

        if(main.AUTO == true)
        {
            if(VoiceInteligentnyPlayOnce == false)
            {
                play(R.raw.inteligentny);
                VoiceInteligentnyPlayOnce = true;
                main.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //Toast toast = Toast.makeText(main,"Tryb inteligentny włączony \uD83D\uDE03", Toast.LENGTH_LONG);
                        //toast.setGravity(Gravity.CENTER, 0, 0);
                        //toast.show();
                        sprite1.StartTimeOut(2500);
                    }
                });
            }
        }
    }

    // LONG TIMER --> 30 sekundowiec
    private void Timer4Method()
    {
        VoiceEtap();
    }

    void VoiceEtap()
    {
        // ETAP VOICE & AVATARES
        if(main.Etap > 0)
        {
            switch(main.Etap)
            {
                case 1:
                    if(VoiceEtap1PlayOnce == false)
                    {
                        VoiceEtap1PlayOnce = true;
                        play(R.raw.etap1);
                        main.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast toast = Toast.makeText(main,"Etap:" + main.Etap + " \uD83D\uDE03", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                sprite1.StartTimeOut(2500);
                            }
                        });
                    }
                    break;

                case 2:
                    if(VoiceEtap2PlayOnce == false)
                    {
                        VoiceEtap2PlayOnce = true;
                        play(R.raw.etap2);
                        main.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast toast = Toast.makeText(main,"Krok:" + main.Etap + " \uD83D\uDE03", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                sprite1.StartTimeOut(2500);
                            }
                        });
                    }
                    break;

                case 3:
                    if(VoiceEtap3PlayOnce == false)
                    {
                        VoiceEtap3PlayOnce = true;
                        play(R.raw.etap3);
                        main.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast toast = Toast.makeText(main,"Krok:" + main.Etap + " \uD83D\uDE03", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                sprite1.StartTimeOut(2500);
                            }
                        });
                    }
                    break;

                case 4:
                    if(VoiceEtap4PlayOnce == false)
                    {
                        VoiceEtap4PlayOnce = true;
                        play(R.raw.etap4);
                        main.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast toast = Toast.makeText(main,"Krok:" + main.Etap + " \uD83D\uDE03", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                sprite1.StartTimeOut(2500);
                            }
                        });
                    }
                    break;

                case 5:
                    if(VoiceEtap5PlayOnce == false)
                    {
                        VoiceEtap5PlayOnce = true;
                        play(R.raw.etap5);
                        main.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast toast = Toast.makeText(main,"Krok:" + main.Etap + " \uD83D\uDE03", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                sprite1.StartTimeOut(2500);
                            }
                        });
                    }
                    break;

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
                    // BT.START
                    case 1:
                        play(R.raw.start);
                        sprite1.StartTimeOut(1000);
                        //Toasty.info(main, "START \uD83D\uDE03", Toast.LENGTH_SHORT, true).show();

                        Toast toast = Toast.makeText(main,"START \uD83D\uDE03", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        break;

                    // BT.grzanie
                    case 2:
                        play(R.raw.grzanie);
                        sprite1.StartTimeOut(2000);

                       // Toast toast2 = Toast.makeText(main,"Włączam grzałkę \uD83D\uDE03", Toast.LENGTH_LONG);
                        //toast2.setGravity(Gravity.CENTER, 0, 0);
                       // toast2.show();

                        break;

                    // BT.ustawienia voice
                    case 3:
                        play(R.raw.zmianaustaw);
                        break;

                    // BT.wifi
                    case 4:
                        play(R.raw.konfwifi);
                        break;

                    // BT.kolumna
                    case 5:
                        play(R.raw.widokkolumny);
                        break;

                    // stop
                    case 6:
                        play(R.raw.stop);
                        sprite1.StartTimeOut(1000);

                        //Toast toast3 = Toast.makeText(main,"STOP \uD83D\uDE03", Toast.LENGTH_LONG);
                        //toast3.setGravity(Gravity.CENTER, 0, 0);
                        //toast3.show();

                        break;

                    // wykresiki
                    case 7:
                        play(R.raw.wykresiki);
                        break;

                    // exitapp
                    case 8:
                        play(R.raw.exitapp);
                        break;

                    // mam połączenie z esp
                    case 9:
                        play(R.raw.mampolaczenie);
                        sprite1.StartTimeOut(2000);
                        break;

                    // termostat
                    case 10:
                        play(R.raw.termostaton);
                        //Toasty.info(main, "Termostat włączony \uD83D\uDE03", Toast.LENGTH_LONG, true).show();

                        Toast toast4 = Toast.makeText(main,"Termostat włączony \uD83D\uDE03", Toast.LENGTH_LONG);
                        toast4.setGravity(Gravity.CENTER, 0, 0);
                        toast4.show();


                        sprite1.StartTimeOut(2000);
                        break;

                    // inteligentny
                    case 11:
                        play(R.raw.inteligentny);
                        sprite1.StartTimeOut(2000);
                        break;

                    // grzanie wyłączone
                    case 12:
                        if(main.Moc == 0)
                        {
                            play(R.raw.grzanieoff);
                            sprite1.StartTimeOut(2000);
                        }
                        break;

                    // termostat wyłączony
                    case 13:
                        if(main.TERMOSTAT == false)
                        {
                            play(R.raw.termostatoff);
                            sprite1.StartTimeOut(2000);
                        }
                    break;

                    // wyłącz termostat guzikiem stop
                    case 14:
                        if(main.TERMOSTAT == true)
                        {
                            play(R.raw.wylaczterm);
                            sprite1.StartTimeOut(3000);
                        }
                    break;

                    // moze bys włączył grzanie ?
                    case 15:
                            play(R.raw.wlaczylgrzanie);
                            sprite1.StartTimeOut(3000);
                    break;

                    // Przełączam zawór
                    case 16:
                        play(R.raw.przzawor);
                        sprite1.StartTimeOut(2000);
                        //ButtonZawor.show(2200);
                    break;

                    // nie klikaj
                    case 17:
                        play(R.raw.nieklikaj);
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
        if(ButtonStart.Update(eventX,eventY))
        {
            if(main.TERMOSTAT == true)
            {
                util.alertDialog("Najpierw wyłącz termostat naciskając STOP", "UWAGA !!!");
                SetVoiceTimeOut(3500,14);
                return;
            }
            if(main.Moc == 0)
            {
                util.alertDialog("Może najpierw włącz grzanie", "UWAGA !!!");
                SetVoiceTimeOut(3500,15);
                return;
            }

            SetVoiceTimeOut(1500,1);
            //main.sendMessage("start");
            main.webSocketClient.send("AUTO");
        return;
        }

        // STOP
        if(ButtonStop.Update(eventX,eventY))
        {
            SetVoiceTimeOut(1500,6);
            SetVoiceTimeOut(5000,13);
            //main.sendMessage("stop");
            main.webSocketClient.send("STOP");

        return;
        }

        // ZAWOR
        if(ButtonZawor.Update(eventX,eventY))
        {
           // new android.os.Handler().postDelayed(
           //         new Runnable() {
           //             public void run()
           //             {
                            //ButtonZawor.hide();
                            SetVoiceTimeOut(300,16);
                            //main.sendMessage("zgon");
                            main.webSocketClient.send("ZGON");
            //            }
            //        },
            //        1000);
        return;
        }

        // GRZANIE
        if(ButtonGrzanie.Update(eventX,eventY))
        {
            SetVoiceTimeOut(1500,2);
            SetVoiceTimeOut(5000,12);
            main.webSocketClient.send("Gxxx");
        return;
        }

        // EXIT
        if(ButtonExit.Update(eventX,eventY))
        {
            showQuitDialog();
            SetVoiceTimeOut(1500,8);
        return;
        }

        if(ButtonOpcje.Update(eventX,eventY))
        {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable() {
                        public void run()
                        {
                            main.mainView.setAlpha(0f);
                            main.mainView.setVisibility(View.GONE);
                            main.viewSettings.bringToFront();
                            main.viewSettings.setVisibility(View.VISIBLE);
                            main.viewSettings.animate()
                                    .alpha(1f)
                                    .setDuration(2500)
                                    .setListener(null);
                            main.viewSettings.postInvalidate();
                        }
                    },
                    1000);
            SetVoiceTimeOut(1500,3);
        return;
        }

        // <WIFI>
        if(ButtonWifi.Update(eventX,eventY))
        {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable() {
                        public void run()
                        {
                            main.mainView.setAlpha(0f);
                            main.mainView.setVisibility(View.GONE);
                            main.viewWifi.setVisibility(View.VISIBLE);
                            main.viewWifi.bringToFront();
                            main.viewWifi.animate()
                                    .alpha(1f)
                                    .setDuration(2500)
                                    .setListener(null);
                            //main.sendMessage("WIFISCAN");
                            //main.webSocketClient.send("WIFISCAN");
                            //main.viewWifi.UpdateNetworkList();

                        }
                    },
                    1000);
            SetVoiceTimeOut(1500,4);
        return;
        }

        // IF CLICKED KOLUMNA
        if(ButtonKolumna.Update(eventX,eventY))
        {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable() {
                        public void run()
                        {
                            main.mainView.setAlpha(0f);
                            main.mainView.setVisibility(View.GONE);

                            main.viewColumn.setVisibility(View.VISIBLE);
                            main.viewColumn.bringToFront();

                            main.viewColumn.animate()
                                    .alpha(1f)
                                    .setDuration(2500)
                                    .setListener(null);

                            main.viewColumn.postInvalidate();
                        }
                    },
                    1000);

            SetVoiceTimeOut(1500,5);
        return;
        }

        // IF CLICKED TOOLS
        if(ButtonTools.Update(eventX,eventY))
        {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable() {
                        public void run()
                        {
                            main.mainView.setAlpha(0f);
                            main.mainView.setVisibility(View.GONE);

                            main.viewTools.setVisibility(View.VISIBLE);
                            main.viewTools.bringToFront();

                            main.viewTools.animate()
                                  .alpha(1f)
                                .setDuration(2500)
                              .setListener(null);
                        }
                    },
                    1000);

            //SetVoiceTimeOut(1500,7);  // wykresiki voice
        return;
        }
    }


    void DrawStatus(Canvas canvas)
    {
        canvas.drawText("UWAGA:  " + main.strStatusText, StatusXPos, StatusYPos, main.mTextStatusPaint);
        canvas.drawText("TMP. DNIA:   " + main.TempDnia, StatusXPos, StatusYPos + main.mTextPaintHeight + main.mTextPaintHeight/2 , main.mTextPaint);
        canvas.drawText("ZAWÓR:        " + main.strZawor, StatusXPos, StatusYPos + (main.mTextPaintHeight*2) + main.mTextPaintHeight, main.mTextPaint);
        canvas.drawText("MOC:         " + main.Moc, StatusXPos, StatusYPos + (main.mTextPaintHeight*3) + main.mTextPaintHeight*1.5f, main.mTextPaintMoc);



        if(main.Etap == 0)
        {
           canvas.drawText("ETAP:         ----  ",
                   StatusXPos,
                   StatusYPos + (main.mTextPaintHeight*4) + main.mTextPaintHeight*2,
                   main.mTextPaint);

        }else
        {
            canvas.drawText("ETAP:            " + Integer.toString(main.Etap),
                    StatusXPos,
                    StatusYPos + (main.mTextPaintHeight*4) + main.mTextPaintHeight*2,
                    main.mTextPaint);
        }

        canvas.drawText("CZAS:   " + main.sCzasProcesuGodz + " : " + main.sCzasProcesuMin + " : " + main.sCzasProcesuSek,
                StatusXPos,
                StatusYPos + (main.mTextPaintHeight*5) + main.mTextPaintHeight*3.5f,
                main.mTextPaintCzas);
     }

    void DrawTemperature(Canvas canvas)
    {
        /* ###########################################################
         samsung screen --->   width: 1440px x height:2560px
         <<<<<<<<<<< TEMPERATURY >>>>>>>>>............................
        */
        canvas.drawText(main.TempZbiornik, main.screenWidth / 9, main.screenHeight / 11, main.mTemperaturePaint);
        canvas.drawText("ZBIORNIK", main.screenWidth / 9, (main.screenHeight / 11) + 70, main.mTextStatusPaint);

        canvas.drawText(main.TempKolumna, (main.screenWidth / 3) + 100, main.screenHeight / 11, main.mTemperaturePaint);
        canvas.drawText("KOLUMNA", (main.screenWidth / 3) + 100, (main.screenHeight / 11) + 70, main.mTextStatusPaint);

        canvas.drawText(main.TempGłowica, ((main.screenWidth / 3) * 2) + 50, main.screenHeight / 11, main.mTemperaturePaint);
        canvas.drawText("GŁOWICA", ((main.screenWidth / 3) * 2) + 50, (main.screenHeight / 11) + 70, main.mTextStatusPaint);
    }

    // {----------------------------------------}
    // {<<<<<<<< ...(B.A.C.K.G.R.O.U.N.D)>>>>>>>}
    // {----------------------------------------}
    void DrawBackground(Canvas canvas)
    {
        canvas.drawBitmap(myBitmapBackground, 0, 0, null);


        Paint paint = new Paint();

       // paint.setColor(Color.rgb(80,80,80));
        //paint.setStrokeWidth(3);
       // canvas.drawRoundRect(10, 10, screenWidth,screenHeight, 50,50,paint);

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

        // Diodes Box
        paint.setColor(Color.argb(120,0,50,10));
        paint.setStrokeWidth(5);
        canvas.drawRoundRect(10,
                diodes_box_rect.top,
                diodes_box_rect.right,
                diodes_box_rect.bottom,
                50,
                50,
                paint);

       // paint.setColor(Color.rgb(0,75,10));
        //paint.setStrokeWidth(3);
       // canvas.drawRoundRect(15, 15, screenWidth - 5,(screenHeight / 8) - 5, 50,50,paint);




    }


    // {----------------------------------------}
    // {<<<<<<<< ...(D.I.O.D.Y)...      >>>>>>>>}
    // {----------------------------------------}
    void DrawDiodes(Canvas canvas)
    {
        //PWR-1 DIODE
        if (main.sPower1.contains("ON") == true)
        {
            Pwr1Diode.Draw(canvas,true);
        }else
        {
            Pwr1Diode.Draw(canvas,false);
        }

        //PWR-2 DIODE
        if (main.sPower2.contains("ON") == true)
        {
            Pwr2Diode.Draw(canvas,true);
        }else
        {
            Pwr2Diode.Draw(canvas,false);
        }

        //PWR-3 DIODE
        if (main.sPower3.contains("ON") == true)
        {
            Pwr3Diode.Draw(canvas,true);
        }else
        {
            Pwr3Diode.Draw(canvas,false);
        }

        // DIODY > WIFI
        if (main.ESP_connected == true)
        {
            WifiDiode.Draw(canvas,true);
        }else
        {
            WifiDiode.Draw(canvas,false);
        }

        // DIODY > TERMOSTACIK
        if (main.TERMOSTAT == true)
        {
            TermostatDiode.Blink(true);
            TermostatDiode.Draw(canvas,true);
        }else
        {
            TermostatDiode.Draw(canvas,false);
        }

        // DIODY > AUTOMATICA
        if (main.AUTO == true)
        {
            AutomaticDiode.Blink(true);
            AutomaticDiode.Draw(canvas,true);
        }else
        {
            AutomaticDiode.Draw(canvas,false);
        }

        // DIODY > ERRORY
        if (main.Error == true)
        {
            ErrorDiode.Blink(true);
            ErrorDiode.Draw(canvas,true);
        }else
        {
            ErrorDiode.Draw(canvas,false);
        }


    }


    // {----------------------------------------}
    // {<<<<<<<< ...(B.U.T.T.O.N.S)... >>>>>>>>>}
    // {----------------------------------------}
    void DrawButtons(Canvas canvas)
    {
        ButtonStart.Draw(canvas);
        ButtonGrzanie.Draw(canvas);
        ButtonStop.Draw(canvas);
        ButtonExit.Draw(canvas);
        ButtonWifi.Draw(canvas);
        ButtonOpcje.Draw(canvas);
        ButtonKolumna.Draw(canvas);
        ButtonTools.Draw(canvas);
        ButtonZawor.Draw(canvas);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility)
    {
        super.onVisibilityChanged(changedView, visibility);
        switch (visibility)
        {
            case VISIBLE:

                break;

            case INVISIBLE:
                //Toast.makeText(main.getApplicationContext(),"invisible",Toast.LENGTH_SHORT).show();
                break;

            case GONE:
                //Toast.makeText(main.getApplicationContext(),"gone",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(this.getVisibility() != VISIBLE) return;

        long now = System.currentTimeMillis();

        DrawBackground(canvas);
        DrawTemperature(canvas);
        DrawStatus(canvas);

        // SAMSUNG 1440x2560 button 105x71   105x3=315 + 60 = 375

        /*canvas.drawBitmap(myBitmapTuba,
                    StatusXPos * 0.65f,
                StatusYPos + (main.mTextPaintHeight*6) + main.mTextPaintHeight*3.5f,
                main.shadowPaint);*/

        DrawButtons(canvas);
        DrawDiodes(canvas);

        canvas.drawText("ESP: " + main.SERVER_IP + "  ---  " + "AndroGon v 0.2.0 \uD83D\uDC95", 50, 80, main.mTextIPPaint);
        //canvas.drawText("MAC: " + main.m_wlanMacAdd, 20, main.screenHeight - 10, main.mTextIPPaint);
        canvas.drawText("ID: " + main.deviceId, 20, main.screenHeight - 10, main.mTextIPPaint);


        // canvas.drawText("scren: " + screenWidth + "  ---  " + screenHeight, 50, 2225, mTextIPPaint);
        // canvas.drawText(XML_Get_Data(XML,"sTermostat"), 50, 2225, mTextIPPaint);
        // canvas.drawText(Float.toString(mTextPaintHeight), 50, 2225, mTextIPPaint);
        // canvas.drawText(Integer.toString(MocG3), 50, 2225, mTextIPPaint);
        // canvas.drawText(XML, 50, 2325, mTextIPPaint);
        // canvas.drawCircle(eventX,eventY,40,green);
        // canvas.drawRect(ButtonOpcjeRect, paint);

        //canvas.drawText("FPS: " + Integer.toString(fps), screenWidth / 1.8f, screenHeight * 0.99f, mTextIPPaint);

        //sprite1.Play(canvas,screenWidth * 0.6f  ,screenHeight * 0.45f );
        sprite1.DrawTimeOutAnimate(canvas);

        toastbox.DrawTimeOut(canvas);


        //gif.draw(canvas);

        ifps++;
        if(now > (mLastTime + 1000))
        {
            mLastTime = now;
            fps = ifps;
            ifps = 0;
        }

        if(DrawCircle == true)
        {
            canvas.drawCircle(eventX, eventY, 30, main.green);
            SetDrawCircleTimeOut(2000);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(enable_screen_button == false)
        {
            SetVoiceTimeOut(1000,17);
            DrawCircle = true;
            invalidate();
            return false;
        }

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

    @Override
    protected void onSizeChanged (int w,int h,int oldw,int oldh)
    {
        main.UpdateScreenSize(w,h);

        DiodeWidth = BitmapFactory.decodeResource(getResources(), R.drawable.wifion).getWidth();
        DiodeHeight = BitmapFactory.decodeResource(getResources(), R.drawable.wifion).getHeight();

        PwrDiodeWidth = BitmapFactory.decodeResource(getResources(), R.drawable.g1on2).getWidth();
        PwrDiodeHeight =  BitmapFactory.decodeResource(getResources(), R.drawable.g1on2).getHeight();

        ButtonWidth = BitmapFactory.decodeResource(getResources(), R.drawable.btstarton).getWidth();
        ButtonHeight = BitmapFactory.decodeResource(getResources(), R.drawable.btstarton).getHeight();

       /* top_box_rect.left = (int)(main.screenWidth - util.Percent(99,main.screenWidth));
        top_box_rect.top = (int)(main.screenHeight - util.Percent(99,main.screenHeight));
        top_box_rect.right = (int)(main.screenWidth - util.Percent(1,main.screenWidth));
        top_box_rect.bottom = (int)(main.screenHeight - util.Percent(86,main.screenHeight));

        big_box_rect.left = top_box_rect.left;
        big_box_rect.top = top_box_rect.bottom + 50;
        big_box_rect.right = top_box_rect.right;
        big_box_rect.bottom = (int)util.Percent(55,main.screenHeight);

        diodes_box_rect.left = big_box_rect.left;
        diodes_box_rect.top = big_box_rect.bottom - DiodeHeight - 50;
        diodes_box_rect.right = DiodeWidth * 4;
        diodes_box_rect.bottom = big_box_rect.bottom;*/

        //StatusXPos = big_box_rect.left + 20;
        //StatusYPos = big_box_rect.top + 20;

    }


}

