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
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
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

public class ViewSettings extends View
{
    // Main class & Utilities instance
    public final MainActivity main;
    public final Utilities util;

    float eventX;
    float eventY;

    int ButtonWidth;
    int ButtonHeight;

    int CheckBoxWidth;
    int CheckBoxHeight;

    MediaPlayer mp;

    Timer Timer1;
    Timer Timer2;

    boolean DrawCircle = false;


    // >BUTTONY<
    CButton ButtonExit;
    CButton ButtonHelp;
    CButton ButtonSaveEspData;
    //CButton CheckBox1Klik;
    //CButton CheckBox2Klik;
    //CButton CheckBox3Klik;

    TextSuper text_TempalArmuZbiornik;
    TextSuper text_TempAlarmuGlowica;
    TextSuper text_KolumnaHisterezaClose;
    TextSuper text_KolumnaHisterezaOpen;
    TextSuper text_TempTermostatStart;
    TextSuper text_TempTermostatStop;
    TextSuper text_CzasZakonczenia;
    TextSuper text_MocG1;
    TextSuper text_MocG2;
    TextSuper text_MocG3;

    TextSuper text_Klik1;
    TextSuper text_Klik1_G1;
    TextSuper text_Klik1_G2;
    TextSuper text_Klik1_G3;

    TextSuper text_Klik2;
    TextSuper text_Klik2_G1;
    TextSuper text_Klik2_G2;
    TextSuper text_Klik2_G3;

    TextSuper text_Klik3;
    TextSuper text_Klik3_G1;
    TextSuper text_Klik3_G2;
    TextSuper text_Klik3_G3;

    int ButtonDistance = 50;

    // >Layout coordinates<
    Rect big_box_rect;

    RoundBox alarm_box;
    RoundBox power_box;
    RoundBox histereza_box;
    RoundBox termostat_box;
    RoundBox endtime_box;
    RoundBox ok_box;

    int menu_number = 0;

    Paint paint;

    String input_TempalArmuZbiornik;
    String input_TempAlarmuGlowica;
    String input_KolumnaHisterezaClose;
    String input_KolumnaHisterezaOpen;
    String input_TempTermostatStart;
    String input_TempTermostatStop;
    String input_sCzasZakonczenia;
    String input_MocG1;
    String input_MocG2;
    String input_MocG3;

    String sHeat_K1 = "0";
    String sHeat_g1_k1 = "0";
    String sHeat_g2_k1 = "0";
    String sHeat_g3_k1 = "0";

    String sHeat_K2 = "0";
    String sHeat_g1_k2 = "0";
    String sHeat_g2_k2 = "0";
    String sHeat_g3_k2 = "0";

    String sHeat_K3 = "0";
    String sHeat_g1_k3 = "0";
    String sHeat_g2_k3 = "0";
    String sHeat_g3_k3 = "0";

    String XML;

    public ViewSettings(Context context,MainActivity activity)
    {
        super(context);
        main = activity;
        util = main.util;
        paint = new Paint();

        text_TempalArmuZbiornik = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);
        text_TempAlarmuGlowica = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);
        text_KolumnaHisterezaClose = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                25);

        text_KolumnaHisterezaOpen = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                25);

        text_TempTermostatStart = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                25);

        text_TempTermostatStop = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                25);

        text_CzasZakonczenia = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                25);

        text_MocG1 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                25);

        text_MocG2 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                25);

        text_MocG3 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                25);

        text_Klik1 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);

        text_Klik1_G1 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);

        text_Klik1_G2 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);

        text_Klik1_G3 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);


        text_Klik2 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);

        text_Klik2_G1 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);

        text_Klik2_G2 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);

        text_Klik2_G3 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);

        text_Klik3 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);

        text_Klik3_G1 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);

        text_Klik3_G2 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);

        text_Klik3_G3 = new TextSuper("q",
                Color.rgb(0,255,55),
                this,
                0,
                0,
                30);

        ButtonWidth = BitmapFactory.decodeResource(getResources(), R.drawable.btstarton).getWidth();
        ButtonHeight = BitmapFactory.decodeResource(getResources(), R.drawable.btstarton).getHeight();

        CheckBoxWidth = BitmapFactory.decodeResource(getResources(), R.drawable.btcheckon).getWidth();
        CheckBoxHeight = BitmapFactory.decodeResource(getResources(), R.drawable.btcheckon).getHeight();

        // main screen box background
        big_box_rect = new Rect(
                (int)util.Percent(1,main.screenWidth),
                (int)util.Percent(1,main.screenHeight),
                (int)(main.screenWidth - util.Percent(1,main.screenWidth)),
                (int)(main.screenHeight - util.Percent(1,main.screenHeight))
        );

        // main menu alarm box
        alarm_box = new RoundBox(this,
                big_box_rect.left + util.Percent(5,main.screenWidth),
                big_box_rect.top + util.Percent(5,main.screenHeight),
                big_box_rect.right - util.Percent(60,main.screenWidth),
                big_box_rect.bottom - util.Percent(90,main.screenHeight),
                25,
                R.raw.error_002);
        alarm_box.SetText("ALARMY",Color.rgb(0,255,55),18);

        // main menu power box
        power_box = new RoundBox(this,
                alarm_box.BoxX,
                alarm_box.BoxY + alarm_box.BoxHeight + alarm_box.TextHeight,
                alarm_box.BoxWidth,
                alarm_box.BoxHeight,
                25,
                R.raw.error_002);
        power_box.SetText("MOC GRZANIA",Color.rgb(0,255,55),18);

        // main menu histereza box
        histereza_box = new RoundBox(this,
                power_box.BoxX,
                power_box.BoxY + power_box.BoxHeight + power_box.TextHeight,
                power_box.BoxWidth,
                power_box.BoxHeight,
                25,
                R.raw.error_002);
        histereza_box.SetText("HISTEREZA",Color.rgb(0,255,55),18);

        // main menu termostat box
        termostat_box = new RoundBox(this,
                histereza_box.BoxX,
                histereza_box.BoxY + histereza_box.BoxHeight + histereza_box.TextHeight,
                histereza_box.BoxWidth,
                histereza_box.BoxHeight,
                25,
                R.raw.error_002);
        termostat_box.SetText("TERMOSTAT",Color.rgb(0,255,55),18);

        // main menu endtime box
        endtime_box = new RoundBox(this,
                termostat_box.BoxX,
                termostat_box.BoxY + termostat_box.BoxHeight + termostat_box.TextHeight,
                termostat_box.BoxWidth,
                termostat_box.BoxHeight,
                25,
                R.raw.error_002);
        endtime_box.SetText("ZAKOŃCZENIE",Color.rgb(0,255,55),18);

        // ok box
        ok_box = new RoundBox(this,
                (int)util.Percent(75,main.screenWidth),
                (int)util.Percent(70,main.screenHeight),
                250,
                200,
                25,
                R.raw.error_002);
        ok_box.SetText("TAK",Color.rgb(0,255,55),18);


         // CENTER
        ButtonHelp = new CButton(this,
                (main.screenWidth  - ButtonWidth) * 0.5f,
                main.screenHeight  - ButtonHeight - (ButtonHeight/4),
                R.drawable.bthelpon,
                R.drawable.bthelpoff,
                R.raw.keyboard,600);

        // RIGHT
        ButtonExit = new CButton(this,
                ButtonHelp.ButtonX + ButtonWidth + ButtonDistance,
                main.screenHeight  - ButtonHeight - (ButtonHeight/4),
                R.drawable.btcloseon,
                R.drawable.btcloseoff,
                R.raw.keyboard,600);


        // LEFT
        ButtonSaveEspData = new CButton(this,
                ButtonHelp.ButtonX - ButtonWidth - ButtonDistance,
                main.screenHeight  - ButtonHeight - (ButtonHeight/4),
                R.drawable.btwifisaveon,
                R.drawable.btwifisaveoff,
                R.raw.keyboard,600);

       /* // Check box tables
        CheckBox1Klik = new CButton(this,
                (main.screenWidth  - CheckBoxWidth) * 0.05f,
                (main.screenHeight  - CheckBoxHeight) * 0.35f,
                R.drawable.btcheckon,
                R.drawable.btcheckoff,
                R.raw.keyboard,600);*/




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

    protected void showInputDialogTemp(int input_number)
    {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        View promptView = layoutInflater.inflate(R.layout.intut_dialog_temperature, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editText = promptView.findViewById(R.id.edittext4);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        KeyListener keyListener = DigitsKeyListener.getInstance("1234567890.");
        editText.setKeyListener(keyListener);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("TAK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        switch (input_number)
                        {
                            case 1:
                                main.TmpAlarmuZbiornik = editText.getText().toString();
                                input_TempalArmuZbiornik  = main.TmpAlarmuZbiornik;
                                play(R.raw.beep);
                                break;

                            case 2:
                                main.TmpAlarmuGłowica = editText.getText().toString();
                                input_TempAlarmuGlowica = main.TmpAlarmuGłowica;
                                play(R.raw.beep);
                                break;

                            case 3:
                                main.TmpKolumnaHisterezaClose = editText.getText().toString();
                                input_KolumnaHisterezaClose = main.TmpKolumnaHisterezaClose;
                                play(R.raw.beep);
                                break;

                            case 4:
                                main.TmpKolumnaHisterezaOpen = editText.getText().toString();
                                input_KolumnaHisterezaOpen = main.TmpKolumnaHisterezaOpen;
                                play(R.raw.beep);
                                break;

                            case 5:
                                main.TmpTermostatStart = editText.getText().toString();
                                input_TempTermostatStart = main.TmpTermostatStart;
                                play(R.raw.beep);
                                break;

                            case 6:
                                main.TmpTermostatStop = editText.getText().toString();
                                input_TempTermostatStop = main.TmpTermostatStop;
                                play(R.raw.beep);
                                break;

                            case 7:
                                main.sCzasZakonczenia = editText.getText().toString();
                                input_sCzasZakonczenia = main.sCzasZakonczenia;
                                play(R.raw.beep);
                                break;

                            case 8:
                                main.MocG1 = editText.getText().toString();
                                input_MocG1 = main.MocG1;
                                play(R.raw.beep);
                                break;

                            case 9:
                                main.MocG2 = editText.getText().toString();
                                input_MocG2 = main.MocG2;
                                play(R.raw.beep);
                                break;

                            case 10:
                                main.MocG3 = editText.getText().toString();
                                input_MocG3 = main.MocG3;
                                play(R.raw.beep);
                                break;

                        }

                    }
                })
                .setNegativeButton("NIE",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                                play(R.raw.error_001);
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }


    void buildXML()
    {
        XML =  "<?xmlconfig>";
        XML += "<sTempAlarmuGlowica>" + input_TempAlarmuGlowica + "</sTempAlarmuGlowica>";
        XML += "<sTempAlarmuBeczka>" + input_TempalArmuZbiornik + "</sTempAlarmuBeczka>";

        float input = Float.parseFloat(input_KolumnaHisterezaClose);
        if(input < 0.1 || input > 2) {
            input_KolumnaHisterezaClose = "0.3";
        }
        XML += "<sHisterezaT_close>" + input_KolumnaHisterezaClose  + "</sHisterezaT_close>";

        input = Float.parseFloat(input_KolumnaHisterezaOpen);
        if(input < 0 || input > 5) {
            input_KolumnaHisterezaOpen = "0.5";
        }
        XML += "<sHisterezaT_open>" + input_KolumnaHisterezaOpen  + "</sHisterezaT_open>";

        // TERMOSTAT
        input = Float.parseFloat(input_TempTermostatStart);
        if(input < 0 || input > 120)
            input_TempTermostatStart = "40";
        XML += "<sTempTermostat_start>" + input_TempTermostatStart  + "</sTempTermostat_start>";

        input = Float.parseFloat(input_TempTermostatStop);
        if(input < 0 || input > 120)
            input_TempTermostatStop = "60";
        XML += "<sTempTermostat_stop>" + input_TempTermostatStop  + "</sTempTermostat_stop>";


        XML += "<sMocGrzaniaG1>" + input_MocG1  + "</sMocGrzaniaG1>";
        XML += "<sMocGrzaniaG2>" + input_MocG2  + "</sMocGrzaniaG2>";
        XML += "<sMocGrzaniaG3>" + input_MocG3  + "</sMocGrzaniaG3>";

        XML += "<sCzasZakonczenia>" + input_sCzasZakonczenia  + "</sCzasZakonczenia>";


        //<!-- KLIK I -->
        //<!-- ############################################################# -->
           XML += "<sHeat_K1>" + sHeat_K1  + "</sHeat_K1>";
           XML += "<sHeat_g1_k1>" + sHeat_g1_k1  + "</sHeat_g1_k1>";
           XML += "<sHeat_g2_k1>" + sHeat_g2_k1  + "</sHeat_g2_k1>";
           XML += "<sHeat_g3_k1>" + sHeat_g3_k1  + "</sHeat_g3_k1>";
        //<!-- ############################################################# -->

        //<!-- KLIK II -->
        //<!-- ############################################################# -->
            XML += "<sHeat_K2>" + sHeat_K2  + "</sHeat_K2>";
            XML += "<sHeat_g1_k2>" + sHeat_g1_k2  + "</sHeat_g1_k2>";
            XML += "<sHeat_g2_k2>" + sHeat_g2_k2  + "</sHeat_g2_k2>";
            XML += "<sHeat_g3_k2>" + sHeat_g3_k2  + "</sHeat_g3_k2>";
        //<!-- ############################################################# -->

        //<!-- KLIK III -->
        //<!-- ############################################################# -->
        XML += "<sHeat_K3>" + sHeat_K3  + "</sHeat_K3>";
        XML += "<sHeat_g1_k3>" + sHeat_g1_k3  + "</sHeat_g1_k3>";
        XML += "<sHeat_g2_k3>" + sHeat_g2_k3  + "</sHeat_g2_k3>";
        XML += "<sHeat_g3_k3>" + sHeat_g3_k3  + "</sHeat_g3_k3>";
        //<!-- ############################################################# -->


    }

    // CHECK IF BUTTON IS CLICKED
    void CheckTouchEvents()
    {
        // EXIT X
        if(ButtonExit.Update(eventX,eventY))
        {
           menu_number = 0;
           new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable()
                    {
                        public void run()
                        {
                            main.viewSettings.setVisibility(View.GONE);
                            main.viewSettings.setAlpha(0f);

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

        // HELP
        if(ButtonHelp.Update(eventX,eventY))
        {

            return;
        }

        // MENUS
        if(menu_number == 0) {
            if (alarm_box.Update(eventX, eventY)) {
                menu_number = 1;
                return;
            }

            if (power_box.Update(eventX, eventY)) {
                menu_number = 2;
                return;
            }

            if (histereza_box.Update(eventX, eventY)) {
                menu_number = 3;
                return;
            }

            if (termostat_box.Update(eventX, eventY)) {
                menu_number = 4;
                return;
            }

            if (endtime_box.Update(eventX, eventY)) {
                menu_number = 5;
                return;
            }
        }

        if(menu_number > 0) {
            if (ok_box.Update(eventX, eventY)) {
                menu_number = 0;
                return;
            }
        }


        // alarm menu
        if(menu_number == 1) {
            if (text_TempAlarmuGlowica.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                showInputDialogTemp(2);
                return;
            }

            if (text_TempalArmuZbiornik.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                showInputDialogTemp(1);
                return;
            }
        }

        // power menu
        if(menu_number == 2) {
            if (text_MocG1.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                showInputDialogTemp(8);
                return;
            }

            if (text_MocG2.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                showInputDialogTemp(9);
                return;
            }

            if (text_MocG3.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                showInputDialogTemp(10);
                return;
            }

            // --------- K1
            if (text_Klik1.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                if(sHeat_K1.equals("0"))
                {
                    sHeat_K1 = "1";
                }else
                {
                    sHeat_K1 = "0";
                }
                return;
            }

            if (text_Klik1_G1.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                if(sHeat_g1_k1.equals("0"))
                {
                    sHeat_g1_k1 = "1";
                }else
                {
                    sHeat_g1_k1 = "0";
                }
                return;
            }

            if (text_Klik1_G2.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                if(sHeat_g2_k1.equals("0"))
                {
                    sHeat_g2_k1 = "1";
                }else
                {
                    sHeat_g2_k1 = "0";
                }
                return;
            }

            if (text_Klik1_G3.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                if(sHeat_g3_k1.equals("0"))
                {
                    sHeat_g3_k1 = "1";
                }else
                {
                    sHeat_g3_k1 = "0";
                }
                return;
            } // end -------- K1




            // -------------- K2
            if (text_Klik2.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                if(sHeat_K2.equals("0"))
                {
                    sHeat_K2 = "1";
                }else
                {
                    sHeat_K2 = "0";
                }
                return;
            }

            if (text_Klik2_G1.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                if(sHeat_g1_k2.equals("0"))
                {
                    sHeat_g1_k2 = "1";
                }else
                {
                    sHeat_g1_k2 = "0";
                }
                return;
            }

            if (text_Klik2_G2.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                if(sHeat_g2_k2.equals("0"))
                {
                    sHeat_g2_k2 = "1";
                }else
                {
                    sHeat_g2_k2 = "0";
                }
                return;
            }

            if (text_Klik2_G3.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                if(sHeat_g3_k2.equals("0"))
                {
                    sHeat_g3_k2 = "1";
                }else
                {
                    sHeat_g3_k2 = "0";
                }
                return;
            } // end ------------- K2


        }

        // histereza menu
        if(menu_number == 3) {
            if (text_KolumnaHisterezaClose.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                showInputDialogTemp(3);
                return;
            }
            if (text_KolumnaHisterezaOpen.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                showInputDialogTemp(4);
                return;
            }
        }

        // termostat menu
        if(menu_number == 4) {
            if (text_TempTermostatStart.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                showInputDialogTemp(5);
                return;
            }
            if (text_TempTermostatStop.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                showInputDialogTemp(6);
                return;
            }
        }

        // czas zakończenia menu
        if(menu_number == 5) {
            if (text_CzasZakonczenia.Update(eventX, eventY)) {
                play(R.raw.drop_004);
                showInputDialogTemp(7);
                return;
            }
        }

        // SAVE SETTINGS DATAS TO MICROCONTROLLER
        if(ButtonSaveEspData.Update(eventX,eventY))
        {
            buildXML();
            //play(R.raw.drop_004);
            //main.webSocketClient.send(XML);
            return;
        }

    }


  /*  private void drawRectText(String text, Canvas canvas, Rect r) {

        textPaint.setTextSize(20);
        textPaint.setTextAlign(Paint.Align.CENTER);
        int width = r.width();
        int numOfChars = textPaint.breakText(text,true,width,null);
        int start = (text.length()-numOfChars)/2;
        canvas.drawText(text,start,start+numOfChars,r.exactCenterX(),r.exactCenterY(),textPaint);
    }*/

    void DrawBackground(Canvas canvas)
    {
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

        canvas.drawText("ESP: " + main.SERVER_IP + "  ---  " + "AndroGon v 0.2.0 \uD83D\uDC95", big_box_rect.left + 50, big_box_rect.top + 50, main.mTextIPPaint);

    }


    void DrawButtons(Canvas canvas)
    {
        ButtonExit.Draw(canvas);
        ButtonHelp.Draw(canvas);
        ButtonSaveEspData.Draw(canvas);
    }


    void DrawAlarmMenu(Canvas canvas)
    {
        text_TempalArmuZbiornik.SetText("Alarm zbiornik: " + input_TempalArmuZbiornik,big_box_rect.left + 50,big_box_rect.top + text_TempalArmuZbiornik.TextHeight * 5);
        text_TempalArmuZbiornik.SetColor(Color.rgb(255,0,5));
        text_TempalArmuZbiornik.Draw(canvas);

        text_TempAlarmuGlowica.SetText("Alarm głowica: " + input_TempAlarmuGlowica,big_box_rect.left + 50,big_box_rect.top + text_TempAlarmuGlowica.TextHeight * 7);
        text_TempAlarmuGlowica.SetColor(Color.rgb(255,0,5));
        text_TempAlarmuGlowica.Draw(canvas);


    }

    void DrawHisterezaMenu(Canvas canvas)
    {
        text_KolumnaHisterezaClose.SetText("Zamknięcie 0,1-2.0 °C:    " + input_KolumnaHisterezaClose,big_box_rect.left + 50,big_box_rect.top + text_KolumnaHisterezaClose.TextHeight * 5);
        text_KolumnaHisterezaClose.SetColor(Color.rgb(255,0,5));
        text_KolumnaHisterezaClose.Draw(canvas);

        text_KolumnaHisterezaOpen.SetText("Otwarcie 0,1-2.0 °C:    " + input_KolumnaHisterezaOpen,big_box_rect.left + 50,big_box_rect.top + text_KolumnaHisterezaOpen.TextHeight * 8);
        text_KolumnaHisterezaOpen.SetColor(Color.rgb(255,0,5));
        text_KolumnaHisterezaOpen.Draw(canvas);
    }

    void DrawTermostatMenu(Canvas canvas)
    {
        text_TempTermostatStart.SetText("Termostat start °C:    " + input_TempTermostatStart,big_box_rect.left + 50,big_box_rect.top + text_TempTermostatStart.TextHeight * 5);
        text_TempTermostatStart.SetColor(Color.rgb(255,0,5));
        text_TempTermostatStart.Draw(canvas);

        text_TempTermostatStop.SetText("Termostat stop °C:    " + input_TempTermostatStop,big_box_rect.left + 50,big_box_rect.top + text_TempTermostatStop.TextHeight * 8);
        text_TempTermostatStop.SetColor(Color.rgb(255,0,5));
        text_TempTermostatStop.Draw(canvas);
    }

    void DrawCzasZakonczenia(Canvas canvas)
    {
        text_CzasZakonczenia.SetText("Czas zakończenia min:    " + input_sCzasZakonczenia,big_box_rect.left + 50,big_box_rect.top + text_CzasZakonczenia.TextHeight * 5);
        text_CzasZakonczenia.SetColor(Color.rgb(255,0,5));
        text_CzasZakonczenia.Draw(canvas);
    }

    void DrawPower(Canvas canvas)
    {
        text_MocG1.SetText("Moc G1 w:     " + input_MocG1,big_box_rect.left + 50,big_box_rect.top + text_MocG1.TextHeight * 5);
        text_MocG1.SetColor(Color.rgb(255,0,5));
        text_MocG1.Draw(canvas);

        text_MocG2.SetText("Moc G2 w:     " + input_MocG2,big_box_rect.left + 50,big_box_rect.top + text_MocG2.TextHeight * 7);
        text_MocG2.SetColor(Color.rgb(255,0,5));
        text_MocG2.Draw(canvas);

        text_MocG3.SetText("Moc G3 w:     " + input_MocG3,big_box_rect.left + 50,big_box_rect.top + text_MocG3.TextHeight * 9);
        text_MocG3.SetColor(Color.rgb(255,0,5));
        text_MocG3.Draw(canvas);

        // KLIK 1
        // -----------------------------------------------------------------------------------------
        text_Klik1.SetText("Klik 1",big_box_rect.left + 50,main.screenHeight * 0.45f);
        if(sHeat_K1.equals("0")) {
            text_Klik1.SetColor(Color.rgb(100, 100, 100));
        }else
        {
            text_Klik1.SetColor(Color.rgb(255, 100, 0));
        }
        text_Klik1.Draw(canvas);

        text_Klik1_G1.SetText("G1",big_box_rect.left + 10 + (text_Klik1.TextWidth * 2),main.screenHeight * 0.45f);
        if(sHeat_g1_k1.equals("0")) {
            text_Klik1_G1.SetColor(Color.rgb(100, 100, 100));
        }else
        {
            text_Klik1_G1.SetColor(Color.rgb(255, 100, 0));
        }
        text_Klik1_G1.Draw(canvas);

        text_Klik1_G2.SetText("G2",big_box_rect.left + 10 + (text_Klik1.TextWidth * 3),main.screenHeight * 0.45f);
        if(sHeat_g2_k1.equals("0")) {
            text_Klik1_G2.SetColor(Color.rgb(100, 100, 100));
        }else
        {
            text_Klik1_G2.SetColor(Color.rgb(255, 100, 0));
        }
        text_Klik1_G2.Draw(canvas);

        text_Klik1_G3.SetText("G3",big_box_rect.left + 10 + (text_Klik1.TextWidth * 4),main.screenHeight * 0.45f);
        if(sHeat_g3_k1.equals("0")) {
            text_Klik1_G3.SetColor(Color.rgb(100, 100, 100));
        }else
        {
            text_Klik1_G3.SetColor(Color.rgb(255, 100, 0));
        }
        text_Klik1_G3.Draw(canvas);
        // -----------------------------------------------------------------------------------------


        // KLIK 2
        // -----------------------------------------------------------------------------------------
        text_Klik2.SetText("Klik 2",big_box_rect.left + 50,(main.screenHeight * 0.45f) + (text_Klik1.TextHeight*2));
        if(sHeat_K2.equals("0")) {
            text_Klik2.SetColor(Color.rgb(100, 100, 100));
        }else
        {
            text_Klik2.SetColor(Color.rgb(255, 100, 0));
        }
        text_Klik2.Draw(canvas);

        // G1
        text_Klik2_G1.SetText("G1",big_box_rect.left + 10 + (text_Klik1.TextWidth * 2),(main.screenHeight * 0.45f) + (text_Klik1.TextHeight*2));
        if(sHeat_g1_k2.equals("0")) {
            text_Klik2_G1.SetColor(Color.rgb(100, 100, 100));
        }else
        {
            text_Klik2_G1.SetColor(Color.rgb(255, 100, 0));
        }
        text_Klik2_G1.Draw(canvas);

        // G2
        text_Klik2_G2.SetText("G2",big_box_rect.left + 10 + (text_Klik1.TextWidth * 3),(main.screenHeight * 0.45f) + (text_Klik1.TextHeight*2));
        if(sHeat_g2_k2.equals("0")) {
            text_Klik2_G2.SetColor(Color.rgb(100, 100, 100));
        }else
        {
            text_Klik2_G2.SetColor(Color.rgb(255, 100, 0));
        }
        text_Klik2_G2.Draw(canvas);

        // G3
        text_Klik2_G3.SetText("G3",big_box_rect.left + 10 + (text_Klik1.TextWidth * 4),(main.screenHeight * 0.45f) + (text_Klik1.TextHeight*2));
        if(sHeat_g3_k2.equals("0")) {
            text_Klik2_G3.SetColor(Color.rgb(100, 100, 100));
        }else
        {
            text_Klik2_G3.SetColor(Color.rgb(255, 100, 0));
        }
        text_Klik2_G3.Draw(canvas);
        // -----------------------------------------------------------------------------------------




        // KLIK 3
        // -----------------------------------------------------------------------------------------
        text_Klik3.SetText("Klik 3",big_box_rect.left + 50,(main.screenHeight * 0.45f) + (text_Klik1.TextHeight*4));
        if(sHeat_K3.equals("0")) {
            text_Klik3.SetColor(Color.rgb(100, 100, 100));
        }else
        {
            text_Klik3.SetColor(Color.rgb(255, 100, 0));
        }
        text_Klik3.Draw(canvas);
        // -----------------------------------------------------------------------------------------



    }

    void DrawMainMenu(Canvas canvas)
    {
        alarm_box.Draw(canvas);
        power_box.Draw(canvas);
        histereza_box.Draw(canvas);
        termostat_box.Draw(canvas);
        endtime_box.Draw(canvas);
    }


    public void LoadSettings()
    {
         input_TempalArmuZbiornik  = main.TmpAlarmuZbiornik;
         input_TempAlarmuGlowica   = main.TmpAlarmuGłowica;
         input_KolumnaHisterezaClose   = main.TmpKolumnaHisterezaClose;
         input_KolumnaHisterezaOpen    = main.TmpKolumnaHisterezaOpen;
         input_TempTermostatStart = main.TmpTermostatStart;
         input_TempTermostatStop = main.TmpTermostatStop;
         input_sCzasZakonczenia = main.sCzasZakonczenia;

         input_MocG1 = main.MocG1;
         input_MocG2 = main.MocG2;
         input_MocG3 = main.MocG3;

         sHeat_K1 = main.sHeat_K1;
         sHeat_g1_k1 = main.sHeat_g1_k1;
         sHeat_g2_k1 = main.sHeat_g2_k1;
         sHeat_g3_k1 = main.sHeat_g3_k1;

         sHeat_K2 = main.sHeat_K2;
         sHeat_g1_k2 = main.sHeat_g1_k2;
         sHeat_g2_k2 = main.sHeat_g2_k2;
         sHeat_g3_k2 = main.sHeat_g3_k2;

         sHeat_K3 = main.sHeat_K3;
         sHeat_g1_k3 = main.sHeat_g1_k3;
         sHeat_g2_k3 = main.sHeat_g2_k3;
         sHeat_g3_k3 = main.sHeat_g3_k3;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility)
    {
        super.onVisibilityChanged(changedView, visibility);
        switch (visibility)
        {
            case VISIBLE:
                    LoadSettings();
                break;

            case INVISIBLE:
                break;

            case GONE:
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(this.getVisibility() != VISIBLE) return;

        DrawBackground(canvas);

        switch (menu_number)
        {
            case 0:
                DrawMainMenu(canvas);
                break;

            case 1: // alarma
                DrawAlarmMenu(canvas);
                ok_box.Draw(canvas);
                break;

            case 2: // moc
                DrawPower(canvas);
                ok_box.Draw(canvas);
                break;

            case 3: // histereza
                DrawHisterezaMenu(canvas);
                ok_box.Draw(canvas);
                break;

            case 4: // termostat
                DrawTermostatMenu(canvas);
                ok_box.Draw(canvas);
                break;

            case 5: // czas zakończenia
                DrawCzasZakonczenia(canvas);
                ok_box.Draw(canvas);
                break;

        }





        DrawButtons(canvas);

        main.mainView.sprite1.DrawTimeOutAnimate(canvas);


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
                CheckTouchEvents();
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

