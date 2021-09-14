package com.ciapa.androgon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.net.InetAddress;
import java.net.UnknownHostException;
import android.view.View;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.telephony.TelephonyManager;


import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.ciapa.androgon.ViewUpdate.FILEPICKER_PERMISSIONS;

import com.androidplot.Plot;
import com.androidplot.ui.LayoutManager;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;


import egolabsapps.basicodemine.videolayout.VideoLayout;
import tech.gusavila92.websocketclient.WebSocketClient;

public class MainActivity extends AppCompatActivity
{

    FrameLayout  frameLayout;
    ViewMain     mainView;
    ViewSettings viewSettings;
    ViewColumn  viewColumn;
    ViewChart   viewChart;
    ViewTools   viewTools;
    ViewUpdate  viewUpdate;
    ViewWifi  viewWifi;

    private Timer Timer1;
    private Timer Timer2;
    private int SERVER_PORT = 18765;
    String SERVER_IP = "";
    String XML = "";
    String JSON_network_scan = "---";
    int ESP_MILLIS     = 0;
    int ESP_MILLIS_TMP = 0;
    boolean ESP_connected  = false;
    DatagramSocket socket = null;
    private DatagramPacket packetSend;

    private Handler mainThreadHandler;
    private Toast toast;
    private static Context context;

    //WifiManager mainWifiObj;
    boolean network_state = false;


    // PAINTERZY
    Paint mTextStatusPaint;
    Paint mTextPaint;
    Paint mWifiListPaint;
    Paint mWifiListConnectedPaint;
    Paint mTextPaintCzas;
    Paint mTemperaturePaint;
    Paint mTextPaintWifi;
    Paint mTextPaintMoc;
    Paint mTextIPPaint;
    Paint green = new Paint();
    float mTextStatusPaintHeight;
    float mTextPaintHeight;
    float mWifiListPaintHeight;
    Rect  mTmpRect;

    Paint shadowPaint = new Paint();

    float screenWidth;
    float screenHeight;
    int screenWidthDP = 0;
    int screenHeightDP = 0;


    String m_wlanMacAdd;

    /*
     * getDeviceId() returns the unique device ID.
     * For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
     */
    String deviceId;
    /*
     * getSubscriberId() returns the unique subscriber ID,
     * For example, the IMSI for a GSM phone.
     */
    String subscriberId;
    XYPlot plot;
    MediaPlayer mp;
    Utilities util = new Utilities(this);
    private static final String TAG = "MainActivity";


    // *************************************************
    String TempZbiornik = "00.0";
    String TempKolumna  = "00.0";
    String TempGłowica  = "00.0";
    String TempDnia = "00.0";
    String TmpAlarmuZbiornik  = "00.0";
    String TmpAlarmuGłowica = "00.0";
    String TmpKolumnaHisterezaClose = "00.0";
    String TmpKolumnaHisterezaOpen = "00.0";
    String TmpTermostatStart = "00.0";
    String TmpTermostatStop = "00.0";
    String sCzasZakonczenia = "0";
    String MocG1 = "0";
    String MocG2 = "0";
    String MocG3 = "0";
    int Moc = 0;
    String sPower1 = "";
    String sPower2 = "";
    String sPower3 = "";

    String sHeat_K1 = "";
    String sHeat_g1_k1 = "";
    String sHeat_g2_k1 = "";
    String sHeat_g3_k1 = "";

    String sHeat_K2 = "";
    String sHeat_g1_k2 = "";
    String sHeat_g2_k2 = "";
    String sHeat_g3_k2 = "";

    String sHeat_K3 = "";
    String sHeat_g1_k3 = "";
    String sHeat_g2_k3 = "";
    String sHeat_g3_k3 = "";

    String sCzasProcesuGodz = "";
    String sCzasProcesuMin = "";
    String sCzasProcesuSek = "";

    int Etap = 0;
    boolean Koniec = false;
    boolean Error = false;
    boolean AwariaDS = false;
    boolean AlarmTZG = false;
    boolean AlarmTZ = false;
    boolean AlarmTG = false;

    boolean AUTO = false;
    boolean TERMOSTAT = false;
    String strZawor = "";
    String strStatus = "";
    String strStatusText = "";
    String SignalStrength = "---";
    String sSSID = "";
    String sPASS = "";
    // ***************************************************

    WebSocketClient webSocketClient = null;

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.FrameLayout);

        Log.i(TAG, "Start ANDROGON !!!");

        network_state = util.checkWifiConnected(context);
        m_wlanMacAdd = util.getMacAddr();

//        TelephonyManager telephonyManager;
//        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
          deviceId = util.getDeviceUUID(this);
//        subscriberId = telephonyManager.getSubscriberId();


        ConfigureScreenData();
        CreatePaints();
        CreateTextPaints();
        CreateViews();
        CreateButtons();
        CreateGraph();
        CreateTimers();
        CreateSocket();

        mp = MediaPlayer.create(context, R.raw.beep);
    }

    private void CreateWebSocketClient()
    {
        URI uri;
        try
        {
            uri = new URI("ws://" + SERVER_IP + ":81/"); // ws://localhost:8080/test
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen()
            {
                //System.out.println("onOpen");
                webSocketClient.send("Hello");
            }

            @Override
            public void onTextReceived(String message)
            {
                //System.out.println("onTextReceived");
                //System.out.println(message);
                if(message.contains("<?xml version="))
                {
                    XML = message;
                    XML_decoder();

                   // System.out.println(util.compressString2(XML));

                    /*String dataD = "";
                    byte[] bytes = XML.getBytes();
                    dataD = new String(android.util.Base64.encode(bytes, Base64.DEFAULT));
                    System.out.println(dataD);

                    String decoded_message;
                    byte[] str = dataD.getBytes();
                    decoded_message = new String(android.util.Base64.decode(str, Base64.DEFAULT));
                    System.out.println(decoded_message);*/


                  /*  String xmltmp = "";
                    for(int i = 0;i < XML.length();i++)
                    {
                        xmltmp += XML.charAt(i) / 2;
                    }
                    System.out.println(xmltmp);*/
                  
                    return;
                }

                // response on --> main.webSocketClient.send("WIFISCAN");
                if(message.contains("SCAN:"))
                {
                    JSON_network_scan = message;
                    postToastMessage("Otrzymano dane konfiguracji wifi od sterownika");
                    return;
                }

                if(message.contains("wifisaveok"))
                {
                    postToastMessage("STEROWNIK: Udało się zapisać konfigurację wifi");
                    mainView.sprite1.StartTimeOut(2500);
                    play(R.raw.zapisanokonfwifi);
                    return;
                }

                if(message.contains("wifisaveerror"))
                {
                    postToastMessage("STEROWNIK: Nie udało się zapisać konfiguracji wifi");
                    play(R.raw.k_nieudalosie);
                    return;
                }

                if(message.contains("configsaveok"))
                {
                    postToastMessage("STEROWNIK: Zapisano konfigurację!");
                    return;
                }

                if(message.contains("configsaveerror"))
                {
                    postToastMessage("STEROWNIK: Błąd! nie zapisano konfiguracji!");
                    return;
                }

                if(message.contains("zatrzymano"))
                {
                    postToastMessage("STEROWNIK: Zatrzymano STOP!");
                    return;
                }

                if(message.contains("przelaczonozawor"))
                {
                    postToastMessage("STEROWNIK: przełączono zawór!");
                    return;
                }

                if(message.contains("przelaczonogrzanie"))
                {
                    postToastMessage("STEROWNIK: przełączono grzałkę!");
                    return;
                }

                if(message.contains("startauto###"))
                {
                    postToastMessage("STEROWNIK: tryb inteligentny włączony!");
                    return;
                }

                if(message.contains("starttermo###"))
                {
                    postToastMessage("STEROWNIK: termostat włączono!");
                    return;
                }

                if(message.contains("stoptermo###"))
                {
                    postToastMessage("STEROWNIK: termostat został wyłączony!");
                    return;
                }


            }

            @Override
            public void onBinaryReceived(byte[] data)
            {
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data)
            {
                System.out.println("onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data)
            {
                System.out.println("onPongReceived");
            }

            @Override
            public void onException(Exception e)
            {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived()
            {
                System.out.println("onCloseReceived");
            }
        };

        webSocketClient.setConnectTimeout(15000);
        webSocketClient.setReadTimeout(6000);
        webSocketClient.addHeader("Origin", "http://androgon.com");
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void postToastMessage(final String message)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run()
            {
                Toast toast = Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    public void showToast(final String toast)
    {
        runOnUiThread(() -> Toast.makeText(this, toast, Toast.LENGTH_SHORT).show());
    }

    public void UpdateScreenSize(int w, int h)
    {
        screenWidth = w;
        screenHeight = h;
    }

    public void ConfigureScreenData()
    {
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // util.lockOrientation();
        util.disableRotation(this);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //Set full screen after setting layout content

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        }else
            {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();

        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // samsung screen --->   width: 1440px x height:2560px
        //this.getDisplay().getRealMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        if(util.showNavigationBar(getResources()))
        {
            screenHeight += util.getNavigationBarHeight();
        }

        screenHeightDP = Math.round(displayMetrics.heightPixels / displayMetrics.density);

        screenWidthDP = Math.round(displayMetrics.widthPixels / displayMetrics.density);


    }


    public void CreatePaints()
    {
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(Color.WHITE);
        shadowPaint.setTextSize(45.0f);
        shadowPaint.setStrokeWidth(2.0f);
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setShadowLayer(20.0f, 10.0f, 10.0f, Color.BLACK);

        green.setColor(Color.GREEN);
        green.setStrokeWidth(5);
        green.setStyle(Paint.Style.STROKE);
        green.setAntiAlias(true);
    }

    public void CreateTextPaints()
    {
        mTmpRect = new Rect();

        //TEXT OBJ
        // NORMAL TXT
        mTextPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setTextSize(util.pxFromDp(context, 18));
        mTextPaint.setShadowLayer(20, 10.0f, 10.0f, Color.BLACK);
        mTextPaint.getTextBounds("ABC",0,2,mTmpRect);
        mTextPaintHeight = mTmpRect.height();

        // WIFI LIST TXT
        mWifiListPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mWifiListPaint.setColor(Color.GREEN);
        mWifiListPaint.setTextSize(util.pxFromDp(context, 15));
        mWifiListPaint.setShadowLayer(20, 10.0f, 10.0f, Color.rgb(0,12,5));
        mWifiListPaint.getTextBounds("ABC",0,2,mTmpRect);
        mWifiListPaintHeight = mTmpRect.height();

        // WIFI CONNECTED LIST TXT painter
        mWifiListConnectedPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mWifiListConnectedPaint.setColor(Color.rgb(240,25,40));
        mWifiListConnectedPaint.setTextSize(util.pxFromDp(context, 15));
        mWifiListConnectedPaint.setShadowLayer(20, 10.0f, 10.0f, Color.rgb(0,0,0));
        //mWifiListConnectedPaint.getTextBounds("ABC",0,2,mTmpRect);

        // TEMPERATURY DIGITAL FONT
        mTemperaturePaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTemperaturePaint.setColor(Color.YELLOW);
        mTemperaturePaint.setTextSize(util.pxFromDp(context, 30));
        mTemperaturePaint.setFakeBoldText(true);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.digital);
        mTemperaturePaint.setTypeface(typeface);
        mTemperaturePaint.setShadowLayer(20, 10.0f, 10.0f, Color.BLACK);

        // MOC TXT
        mTextPaintMoc = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaintMoc.setColor(Color.rgb(255,107,0));
        mTextPaintMoc.setShadowLayer(20, 7, 7, Color.BLACK);
        mTextPaintMoc.setTextSize(Utilities.pxFromDp(context, 22));

        // CZAS TXT
        mTextPaintCzas = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaintCzas.setColor(Color.RED);
        mTextPaintCzas.setTextSize(Utilities.pxFromDp(context, 35));
        mTextPaintCzas.setFakeBoldText(true);
        mTextPaintCzas.setAntiAlias(true);
        mTextPaintCzas.setTypeface(typeface);
        mTextPaintCzas.setShadowLayer(20, 10.0f, 10.0f, Color.BLACK);

        // ---===STATUS PAINT===---
        mTextStatusPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextStatusPaint.setColor(Color.YELLOW);
        mTextStatusPaint.setFakeBoldText(true);
        mTextStatusPaint.setShadowLayer(20, 7, 7, Color.BLACK);
        mTextStatusPaint.setTextSize(Utilities.pxFromDp(context, 15));
        mTextStatusPaint.getTextBounds("ABC",0,2,mTmpRect);
        mTextStatusPaintHeight = mTmpRect.height();

        // WIFI
        mTextPaintWifi = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextPaintWifi.setColor(Color.rgb(0,94,255));
        mTextPaintWifi.setFakeBoldText(true);
        mTextPaintWifi.setShadowLayer(20, 7, 7, Color.BLACK);
        mTextPaintWifi.setTextSize(Utilities.pxFromDp(context, 20));

        // SMALL TXT IP
        mTextIPPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mTextIPPaint.setColor(Color.GREEN);
        mTextIPPaint.setTextSize(Utilities.pxFromDp(context, 12));
    }

    public void CreateViews()
    {
        //V.I.E.W.S .------------------------------------------------

        viewWifi = new ViewWifi(this,this);
        frameLayout.addView(viewWifi);
        viewWifi.setAlpha(0f);
        viewWifi.setVisibility(View.GONE);

        viewUpdate = new ViewUpdate(this,this);
        frameLayout.addView(viewUpdate);
        viewUpdate.setAlpha(0f);
        viewUpdate.setVisibility(View.GONE);

        viewChart = new ViewChart(context,this,util);
        frameLayout.addView(viewChart);
        viewChart.setAlpha(0f);
        viewChart.setVisibility(View.GONE);

        viewColumn = new ViewColumn(this,this);
        frameLayout.addView(viewColumn);
        viewColumn.setAlpha(0f);
        viewColumn.setVisibility(View.GONE);

        viewSettings = new ViewSettings(this,this);
        frameLayout.addView(viewSettings);
        viewSettings.setAlpha(0f);
        viewSettings.setVisibility(View.GONE);

        viewTools = new ViewTools(this,this);
        frameLayout.addView(viewTools);
        viewTools.setAlpha(0f);
        viewTools.setVisibility(View.GONE);

        // main screen view
        mainView = new ViewMain(this,this,util);
        frameLayout.addView(mainView);
        mainView.setAlpha(0f);
        mainView.setVisibility(VISIBLE);
        //setContentView(myView);
        mainView.setWillNotDraw(false); // для возможности вызова onDraw через invalidate
        final Handler hdlr = new Handler(Looper.getMainLooper());
        hdlr.post(new Runnable()
        {
            @Override
            public void run()
            {
               // if(mainView.getVisibility() == VISIBLE)
               // {
                    mainView.invalidate();
                    hdlr.postDelayed(this, 100);
               // }
            }
        });

       /* myView.animate()
               .rotation(360)
               .setDuration(3000)
               .setListener(null);*/

        //FADE IN MAIN VIEW
        mainView.setAlpha(0f);
        mainView.animate()
                .alpha(1f)
                .setDuration(3000)
                .setListener(null);
    }

    public void CreateButtons()
    {
       // btnX = (Button) findViewById(R.id.angry_btn);
        //btnX.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //btnX.setText("X");
        //btnX.setVisibility(View.GONE);
        //btnX.setWidth(50);
        //btnX.setHeight(50);

        //LinearLayout llayout = findViewById(R.id.rootContainer);

        // llayout.addView(btnX);
        /*btnX.animate()
                .x(screenWidth - 50 -10)
                .y(screenHeight - 50 -10)
                .setDuration(0)
                .start();*/


      /*  btnX.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(MainActivity.this, "Aha to tak...", Toast.LENGTH_LONG).show();
                btnX.setVisibility(View.GONE);
                viewChart.setVisibility(View.GONE);
                viewChart.setAlpha(0f);
                mainView.setVisibility(View.VISIBLE);
                mainView.bringToFront();
                mainView.animate()
                        .alpha(1f)
                        .setDuration(2500)
                        .setListener(null);
            }
        });*/

    }

    public void CreateGraph()
    {
        plot = (XYPlot) findViewById(R.id.plot);
        plot.setVisibility(View.GONE);
        PanZoom.attach(plot);


        // create a couple arrays of y-values to plot:
        final Number[] domainLabels = {1, 2, 3, 6, 7, 8, 9, 10, 13, 14};
        Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};
        Number[] series2Numbers = {5, 2, 10, 5, 20, 10, 40, 20, 80, 40};

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Zbiornik");
        XYSeries series2 = new SimpleXYSeries(
                Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Kolumna");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_2);


        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

    }

    public void CreateTimers()
    {
        Timer1 = new Timer();
        Timer1.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
               Timer1Method();
            }

        }, 7000, 10000);

        Timer2 = new Timer();
        Timer2.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Timer2Method();
            }

        }, 1000, 5000);
    }

    public void CreateSocket()
    {
        try
        {
            socket = new DatagramSocket(SERVER_PORT);
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally {
            if (socket != null)
            {
                //socket.close();
            }
        }
        StartReceiveThread();
    }


    void play(int resid)
    {
        try
        {
            // if (mp.isPlaying())
            // {
            mp.stop();
            mp.release();
          //  mp.reset();
            mp = MediaPlayer.create(this, resid);
            // }
            mp.start();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Handler getMainThreadHandler()
    {
        if (mainThreadHandler == null)
        {
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }
        return mainThreadHandler;
    }

    public static MainActivity getApp()
    {
        return (MainActivity) context;
    }

    /**
     * Thread safe way of displaying toast.
     * @param message
     * @param duration
     */
    public void showToast(final String message, final int duration)
    {
        getMainThreadHandler().post(new Runnable()
        {
            @Override
            public void run()
            {
                if (!TextUtils.isEmpty(message))
                {
                    if (toast != null)
                    {
                        toast.cancel(); //dismiss current toast if visible
                        toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setText(message);
                        //toast.setView(myView);
                    } else {
                        toast = Toast.makeText(context, message, duration);
                    }
                    toast.show();
                }
            }
        });
    }

    /*public void sendMessage(final String message)
    {

        //final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                DatagramSocket ds = null;
                try {
                    ds = new DatagramSocket();
                    // IP Address below is the IP address of that Device where server socket is opened.
                    InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                    DatagramPacket dp;
                    dp = new DatagramPacket(message.getBytes(), message.length(), serverAddr, SERVER_PORT);
                    ds.send(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (ds != null)
                    {
                        ds.close();
                    }
                }

             }
        });

        thread.start();
    }*/


    private void StartReceiveThread()
    {
        Thread thread = new Thread(() ->
        {
                while(true)
                {
                    try
                    {
                        byte[] buffer = new byte[128];
                        DatagramPacket ds = new DatagramPacket(buffer, buffer.length);
                        socket.receive(ds);
                        String message = new String( ds.getData() ).trim();

                        char[] chars = message.toCharArray();

                        if (message.contains("*%$@:") == true)
                        {
                            int i;
                            for( i = 0; i < message.length(); i++ )
                            {
                               if(message.charAt(i) == '!') chars[i] = '1';
                               if(message.charAt(i) == '@') chars[i] = '2';
                               if(message.charAt(i) == '#') chars[i] = '3';
                               if(message.charAt(i) == '%') chars[i] = '4';
                               if(message.charAt(i) == '^') chars[i] = '5';
                               if(message.charAt(i) == '&') chars[i] = '6';
                               if(message.charAt(i) == '*') chars[i] = '7';
                               if(message.charAt(i) == '(') chars[i] = '8';
                               if(message.charAt(i) == ')') chars[i] = '9';
                            }
                            message = String.valueOf(chars);
                            SERVER_IP = message.substring(5);
                            ESP_connected = true;

                            if(util.IPvalidate(SERVER_IP) == true)
                            {
                                if (webSocketClient == null)
                                {
                                    CreateWebSocketClient();
                                }
                            }


                           /* String msgToSender = "connected";
                            ds = new DatagramPacket(msgToSender.getBytes(),
                                             msgToSender.length(),
                                             ds.getAddress(),
                                             ds.getPort());
                            socket.send(ds);*/
                        }


                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    } finally
                    {
                        if (socket != null)
                        {
                            //socket.close();
                        }
                    }
                }
        });
        thread.start();
    }

    // 10000 ms = 10 second CHECK CONNECTION
    private void Timer1Method()
    {
        network_state = util.checkWifiConnected(this);

        if(network_state == false)
        {
            ESP_connected = false;
            SERVER_IP = "";
            return;
        }else
        {
           // if(ESP_connected == true)
           //     ESP_MILLIS = Integer.parseInt((util.XML_Get_Data(XML, "sESP_MILLIS")));
        }

        if(ESP_MILLIS <= ESP_MILLIS_TMP)
        {
            ESP_connected = false;
            SERVER_IP = "";
        }else if(ESP_MILLIS > ESP_MILLIS_TMP)
        {
            ESP_connected = true;
        }
        ESP_MILLIS_TMP = ESP_MILLIS;
    }

    // 5 seconds
    private void Timer2Method()
    {

        if(util.IPvalidate(SERVER_IP) == true)
        {
            if (webSocketClient == null)
            {
                CreateWebSocketClient();
            }
        }
    }

    void XML_decoder()
    {
        // TEMPERATURY
        TempKolumna = util.XML_Get_Data(XML,"tempkolumna");
        TempZbiornik = util.XML_Get_Data(XML,"tempbeczka");
        TempGłowica = util.XML_Get_Data(XML,"tempglowica");
        TempDnia = util.XML_Get_Data(XML,"tempdnia");
        TmpTermostatStart = util.XML_Get_Data(XML,"sTempTermostat_start");
        TmpTermostatStop = util.XML_Get_Data(XML,"sTempTermostat_stop");


        // CZAS PROCESU
        sCzasProcesuGodz = util.XML_Get_Data(XML,"sCzasProcesuGodz");
        sCzasProcesuMin = util.XML_Get_Data(XML,"sCzasProcesuMin");
        sCzasProcesuSek = util.XML_Get_Data(XML,"sCzasProcesuSek");

        // CZAS ZAKONCZENIA
        sCzasZakonczenia = util.XML_Get_Data(XML,"sCzasZakonczenia");

        // ZAWOREK
        strZawor = util.XML_Get_Data(XML,"stan_ZGon");

        // POWER
        MocG1 = util.XML_Get_Data(XML,"sMocGrzaniaG1");
        MocG2 = util.XML_Get_Data(XML,"sMocGrzaniaG2");
        MocG3 = util.XML_Get_Data(XML,"sMocGrzaniaG3");

        // STATUS
        strStatus = util.XML_Get_Data(XML,"sStatus");
        switch(strStatus)
        {
            case "Awaria DS":
                Error = true;
                AwariaDS = true;
                strStatusText = "Awaria czujnika temperatury \uD83D\uDE32";
                break;

            case "Alarm T.ZG":
                Error = true;
                AlarmTZG = true;
                strStatusText = "Alarm temp. zbiornik i głowica \uD83D\uDE32";
                break;

            case "Alarm TZ":
                Error = true;
                AlarmTZ = true;
                strStatusText = "Alarm temp. zbiornik \uD83D\uDE32";
                break;

            case "Alarm TG":
                Error = true;
                AlarmTG = true;
                strStatusText = "Alarm temp. głowica \uD83D\uDE32";
                break;

            case "OK":
                Error = false;
                AlarmTZG = false;
                AlarmTZ  = false;
                AlarmTG  = false;

                strStatusText = "Wszystko dobrze";

                if(TERMOSTAT == true)
                {
                    strStatusText = "Termostat właczony \uD83D\uDE03";
                }else if(AUTO == true)
                {
                    strStatusText = "Tryb inteligentny właczony \uD83D\uDE03";
                }
                break;

            case "KONIEC":
                Koniec = true;
                strStatusText = "ZAKOŃCZONO";
                break;
        }


        // MOC KTÓRA GRZAŁA ILE MA WAT
        sPower1 = util.XML_Get_Data(XML,"sPower1");
        sPower2 = util.XML_Get_Data(XML,"sPower2");
        sPower3 = util.XML_Get_Data(XML,"sPower3");

        // MOC RAZEM JEŚLI KTÓRA WŁĄCZONA
        Moc = 0;
        if(sPower1.contains("ON")) Moc += Integer.parseInt(MocG1);
        if(sPower2.contains("ON")) Moc += Integer.parseInt(MocG2);
        if(sPower3.contains("ON")) Moc += Integer.parseInt(MocG3);

        // TABELKA GRZANIA
        // <sHeat_K1>1</sHeat_K1>
	    // <sHeat_g1_k1>1</sHeat_g1_k1>
	    // <sHeat_g2_k1>0</sHeat_g2_k1>
	    // <sHeat_g3_k1>0</sHeat_g3_k1>
        sHeat_K1 = util.XML_Get_Data(XML,"sHeat_K1");
        sHeat_g1_k1 = util.XML_Get_Data(XML,"sHeat_g1_k1");
        sHeat_g2_k1 = util.XML_Get_Data(XML,"sHeat_g2_k1");
        sHeat_g3_k1 = util.XML_Get_Data(XML,"sHeat_g3_k1");

        sHeat_K2 = util.XML_Get_Data(XML,"sHeat_K2");
        sHeat_g1_k2 = util.XML_Get_Data(XML,"sHeat_g1_k2");
        sHeat_g2_k2 = util.XML_Get_Data(XML,"sHeat_g2_k2");
        sHeat_g3_k2 = util.XML_Get_Data(XML,"sHeat_g3_k2");

        sHeat_K3 = util.XML_Get_Data(XML,"sHeat_K3");
        sHeat_g1_k3 = util.XML_Get_Data(XML,"sHeat_g1_k3");
        sHeat_g2_k3 = util.XML_Get_Data(XML,"sHeat_g2_k3");
        sHeat_g3_k3 = util.XML_Get_Data(XML,"sHeat_g3_k3");


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
            Etap = Integer.parseInt((util.XML_Get_Data(XML,"sKrok"))) + 1;
        }else
        {
            AUTO = false;
        }

        TmpAlarmuZbiornik = util.XML_Get_Data(XML,"sTempAlarmuBeczka");
        TmpAlarmuGłowica = util.XML_Get_Data(XML,"sTempAlarmuGlowica");
        TmpKolumnaHisterezaClose = util.XML_Get_Data(XML,"sHisterezaT_close");
        TmpKolumnaHisterezaOpen = util.XML_Get_Data(XML,"sHisterezaT_open");;


        SignalStrength = util.XML_Get_Data(XML,"sWIFI_SIGNAL");

        sSSID = util.XML_Get_Data(XML,"sSSID");
        sPASS = util.XML_Get_Data(XML,"sPASS");

        ESP_MILLIS = Integer.parseInt((util.XML_Get_Data(XML, "sESP_MILLIS")));

        // Log.i("myTag", XML);

    }

    public void QuitApp()
    {
        //MainActivity.this.finish();
        finishAffinity();
        System.exit(0);
    }



    @Override
    public void onBackPressed()
    {
        //finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
        socket.close();
        //videoLayout.onDestroyVideoLayout();
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //videoLayout.onResumeVideoLayout();
        //Toast.makeText(this, "Witamy ponownie !", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //videoLayout.onPauseVideoLayout();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    /**
     * Callback that handles the status of the permissions request.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case FILEPICKER_PERMISSIONS:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(
                            MainActivity.this,
                            "Dostęp dozwolony! wciśnij przycisk wyboru pliku ponownie.",
                            Toast.LENGTH_SHORT
                    ).show();
                }else
                {
                    Toast.makeText(
                            MainActivity.this,
                            "Dostęp zabroniony :(",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                return;
            }
        }
    }

}
