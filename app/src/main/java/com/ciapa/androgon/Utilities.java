package com.ciapa.androgon;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Surface;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;
import android.os.CountDownTimer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.View;
import android.widget.ImageView;


//import com.androidplot.util.FontUtils;

import androidx.core.app.ActivityCompat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;

public class Utilities
{

    private static Matrix yFlipMatrix;
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    private static final int READ_BUFFER_SIZE = 32 * 1024;  //32KB
    private static final String TAG = "Panoramio";
    private MainActivity mainActivity;
    private int mDestPort = 0;
    private DatagramSocket mSocket;


    public Utilities(MainActivity activity)
    {
        mainActivity = activity;
    }

    public Bitmap drawMultilineTextToBitmap(Context gContext,
                                            int gResId,
                                            String gText) {

        // prepare canvas
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);

        // new antialiased Paint
        TextPaint paint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (14 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // set text width to canvas width minus 16dp padding
        int textWidth = canvas.getWidth() - (int) (16 * scale);

        // init StaticLayout for text
        StaticLayout textLayout = new StaticLayout(
                gText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // get height of multiline text
        int textHeight = textLayout.getHeight();

        // get position of text's top left corner
        float x = (bitmap.getWidth() - textWidth)/2;
        float y = (bitmap.getHeight() - textHeight)/2;

        // draw text to the Canvas center
        canvas.save();
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.restore();

        return bitmap;
    }

    public static Bitmap jpegToBitmap(byte[] jpeg) {
        return BitmapFactory.decodeByteArray(jpeg,0,jpeg.length);
    }

    public static Bitmap pngToBitmap(byte[] png) {
        return BitmapFactory.decodeByteArray(png,0,png.length);
    }

    public static byte[] bitmapToJpeg(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        return out.toByteArray();
    }

    public static byte[] bitmapToPng(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }

    public static Bitmap cropBitmap(Bitmap bitmap, Rect cropRect)
    {
        return Bitmap.createBitmap(bitmap,cropRect.left,cropRect.top,cropRect.width(),cropRect.height());
    }

    public static Bitmap getThumbnailBitmap(Bitmap bitmap,int width,int height) {
        return ThumbnailUtils.extractThumbnail(bitmap,width,height);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    public static Bitmap loadBitmap2(String filepath) {
        Bitmap bitmap = null;
        try {
            FileInputStream fin = new FileInputStream(filepath);
            bitmap = BitmapFactory.decodeStream(fin);
            fin.close();
        }
        catch (FileNotFoundException e) {

        }
        catch (IOException e) {

        }
        return bitmap;
    }

    public static boolean saveBitmap(Bitmap bitmap, String filepath) {
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            bitmap.recycle();
            fos.close();
            return true;
        }
        catch (FileNotFoundException e) {

        }
        catch (IOException e) {

        }
        return false;
    }

    public boolean openSocket(int localport,int destport)
    {
        mDestPort = destport;
        try {
            mSocket = new DatagramSocket(localport);
            mSocket.setBroadcast(true);
            mSocket.setReuseAddress(true);
            return true;
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendPacket(byte[] buffer) {
        try {
            InetAddress addr = getBroadcastAddress(mainActivity);
            DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
            packet.setAddress(addr);
            packet.setPort(mDestPort);
            mSocket.send(packet);
            return true;
        }
        catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean recvPacket(byte[] buffer) {
        DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
        try {
            mSocket.receive(packet);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static InetAddress getBroadcastAddress(Context context) throws UnknownHostException
    {
        if(isWifiApEnabled(context)) {
            return InetAddress.getByName("192.168.43.255");
        }
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if(dhcp==null) {
            return InetAddress.getByName("255.255.255.255");
        }
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++) {
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }
        return InetAddress.getByAddress(quads);
    }

    protected static Boolean isWifiApEnabled(Context context)
    {
        try {
            WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            Method method = manager.getClass().getMethod("isWifiApEnabled");
            return (Boolean)method.invoke(manager);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String compressString (String string)
    {
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {
            int count = 1;
            while (i + 1 < string.length()
                    && string.charAt(i) == string.charAt(i + 1)) {
                count++;
                i++;
            }

            if (count > 1) {
                stringBuffer.append(count);
            }

            stringBuffer.append(string.charAt(i));
        }

        System.out.println("Compressed string: " + stringBuffer);
        return stringBuffer.toString();
    }

    public static byte[] StringCompress(String text)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            OutputStream out = new DeflaterOutputStream(baos);
            out.write(text.getBytes("UTF-8"));
            out.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return baos.toByteArray();
    }

    public static String StringDecompress(byte[] bytes)
    {
        InputStream in = new InflaterInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[8192];
            int len;
            while((len = in.read(buffer))>0)
                baos.write(buffer, 0, len);
            return new String(baos.toByteArray(), "UTF-8");
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static String compress2(String str, String inEncoding) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(inEncoding));
            gzip.close();
            return URLEncoder.encode(out.toString("ISO-8859-1"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decompress2(String str, String outEncoding) {
        if (str == null || str.length() == 0) {
            return str;
        }

        try {
            String decode = URLDecoder.decode(str, "UTF-8");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(decode.getBytes("ISO-8859-1"));
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(outEncoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String compressString2 (String string)
    {
        LinkedHashSet<String> charMap = new LinkedHashSet<String>();
        HashMap<String, Integer> countMap = new HashMap<String, Integer>();
        int count;
        String key;

        for (int i = 0; i < string.length(); i++) {
            key = new String(string.charAt(i) + "");
            charMap.add(key);
            if(countMap.containsKey(key)) {
                count = countMap.get(key);
                countMap.put(key, count + 1);
            }
            else {
                countMap.put(key, 1);
            }
        }

        Iterator<String> iterator = charMap.iterator();
        String resultStr = "";

        while (iterator.hasNext()) {
            key = iterator.next();
            count = countMap.get(key);

            if(count > 1) {
                resultStr = resultStr + count + key;
            }
            else{
                resultStr = resultStr + key;
            }
        }
        return resultStr;
    }


    public String rleEncodeString(String in)
    {
        StringBuilder out = new StringBuilder();
        Pattern p = Pattern.compile("((\\w)\\2*)");
        Matcher m = p.matcher(in);
        while(m.find())
        {
            if(m.group(1).length() > 1)
            {
                out.append(m.group(1).length());
            }
            out.append(m.group(2));
        }
        return out.toString();
    }

    public String compress(String str) throws IOException
    {
        if (str == null || str.length() == 0)
        {
            return str;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(out))
        {
            gzip.write(str.getBytes());
            gzip.close();
        }
        return out.toString("UTF8");
    }



    public static String EncryptDecrypt(String textToEncrypt)
    {
            int key = 48;
            StringBuilder inSb = new StringBuilder(textToEncrypt);
            StringBuilder outSb = new StringBuilder(textToEncrypt.length());
            char c;
            for (int i = 0; i < textToEncrypt.length(); i++)
            {
                c = inSb.charAt(i);
                c = (char)(c ^ key);
                outSb.append(c);
            }
            return outSb.toString();
    }

    public double calculatePercentage(double obtained, double total)
    {
        return obtained * 100 / total;
    }

    public float Percent(float percent, float number )
    {
       return (percent/100.0f) * number;
    }


    public void showErrorDialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity.getApplicationContext());
        View layoutView = mainActivity.getLayoutInflater().inflate(R.layout.dialog_negative_layout, null);
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

    //This method is fully compatible with Android 1 to Android 10
    public static String getDeviceUUID(Context context)
    {
        String serial;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 Bit
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                serial = android.os.Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            //API>=9 Use serial number
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial Need an initialization
            serial = "serial"; // Random initialization
        }
        // 15-digit number cobbled together using hardware information
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


    public Bitmap getBitmapFromAssets(MainActivity mainActivity, String filepath)
    {
        AssetManager assetManager = mainActivity.getAssets();
        InputStream istr = null;
        Bitmap bitmap = null;

        try
        {
            istr = assetManager.open(filepath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException ioe)
        {
            // manage exception
        } finally
        {
            if (istr != null)
            {
                try
                {
                    istr.close();
                } catch (IOException e)
                {
                }
            }
        }

        return bitmap;
    }

    public static void disableRotation(Activity activity)
    {
        final int orientation = activity.getResources().getConfiguration().orientation;
        final int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        // Copied from Android docs, since we don't have these values in Froyo 2.2
        int SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8;
        int SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO)
        {
            SCREEN_ORIENTATION_REVERSE_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            SCREEN_ORIENTATION_REVERSE_PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }

        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
        {
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
        else if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270)
        {
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
        }
    }

    //getting mac address from mobile
    String getMacAddr()
    {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor, Context mContext)
    {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        int pixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textSize, mContext.getResources().getDisplayMetrics());

        String[] str = text.split(",");

        paint.setTextSize(pixel);
        //paint.setTypeface(FontUtils.getRalewayExtraBold(mContext));
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round

        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap((width/str.length)+10, (height*str.length), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);

        for (int i = 0; i < str.length; i++){
            //Center text here
            float textOffset = (canvas.getWidth()-paint.measureText((str[i])))/2;
            canvas.drawText(str[i], textOffset, ((i+1)*baseline), paint);
        }
        return image;
    }

    public static void enableRotation(Activity activity)
    {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    public void lockOrientation()
    {
        if (mainActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        } else {
            mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        }
    }

    public void unlockOrientation()
    {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    public boolean IPvalidate(final String ip)
    {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip.matches(PATTERN);
    }

    public boolean checkWifiConnected(Context context)
    {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnected())
        {
            return true;
        }
    return false;
    }


    public boolean checkWifiOnAndConnected(Context context)
    {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }

    public static boolean CheckConnectivityStatus(Context context)
    {
        String status = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null)
        {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
            {
                status = "Wifi enabled";
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                status = "Mobile data enabled";
                //return status;
            }
        }else
        {
            status = "No internet is available";
            return false;
        }

        return false;
    }


    public String getIpAddress()
    {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    public Bitmap BitmapConvolve(Bitmap original, float[] coefficients)
    {
        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(mainActivity);

        Allocation allocIn = Allocation.createFromBitmap(rs, original);
        Allocation allocOut = Allocation.createFromBitmap(rs, bitmap);

        ScriptIntrinsicConvolve3x3 convolution = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));
        convolution.setInput(allocIn);
        convolution.setCoefficients(coefficients);
        convolution.forEach(allocOut);

        allocOut.copyTo(bitmap);         // { 1, 1, 1,
        rs.destroy();                    //   1, 1, 1,
        return bitmap;                   //   1, 1, 1  } / 9
    }

    public Bitmap BitmapBlur(Bitmap original, float radius)
    {
        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(mainActivity);

        Allocation allocIn = Allocation.createFromBitmap(rs, original);
        Allocation allocOut = Allocation.createFromBitmap(rs, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, Element.U8_4(rs));
        blur.setInput(allocIn);
        blur.setRadius(radius);
        blur.forEach(allocOut);

        allocOut.copyTo(bitmap);
        rs.destroy();
        return bitmap;
    }

    public Bitmap loadBitmap(int resourceID)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap tempBmp = null;
        try{
            tempBmp = BitmapFactory.decodeStream(new BufferedInputStream(mainActivity.getResources().openRawResource(resourceID)), null, options);
        }catch(OutOfMemoryError e){

        }catch (Error e){

        }
        return tempBmp;
    }


    void alertDialog(String message,String title)
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(mainActivity);
        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.setPositiveButton("Rozumiem, ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Toast toast = Toast.makeText(mainActivity.getApplicationContext(), "Dobra dobra...", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        dialog.setNegativeButton("Co ?",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast toast = Toast.makeText(mainActivity.getApplicationContext(),"a bzia bzia...",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    public static Animation createKumoAnimation(Context context)
    {
        AnimationSet set = new AnimationSet(true);

        ScaleAnimation sa = new ScaleAnimation(0.98f, 1.02f, 0.98f, 1.02f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        sa.setDuration(12000);
        sa.setRepeatMode(Animation.REVERSE);
        sa.setRepeatCount(5);

        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -0.05f, Animation.RELATIVE_TO_SELF,
                0.05f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        ta.setDuration(6000);
        ta.setRepeatMode(Animation.REVERSE);
        ta.setRepeatCount(10);

        set.addAnimation(sa);
        set.addAnimation(ta);
        set.setFillAfter(true);

        return set;
    }



    /**
     * Find components of color of the bitmap at x, y.
     * @param x Distance from left border of the View
     * @param y Distance from top of the View
     * @param view Touched surface on screen
     */
    public static int findColor(View view, int x, int y)
            throws NullPointerException {

        int red = 0;
        int green = 0;
        int blue = 0;
        int color = 0;

        int offset = 1; // 3x3 Matrix
        int pixelsNumber = 0;

        int xImage = 0;
        int yImage = 0;

        // Get the bitmap from the view.
        ImageView imageView = (ImageView)view;
        BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
        Bitmap imageBitmap = bitmapDrawable.getBitmap();

        // Calculate the target in the bitmap.
        xImage = (int)(x * ((double)imageBitmap.getWidth() / (double)imageView.getWidth()));
        yImage = (int)(y * ((double)imageBitmap.getHeight() / (double)imageView.getHeight()));

        // Average of pixels color around the center of the touch.
        for (int i = xImage - offset; i <= xImage + offset; i++) {
            for (int j = yImage - offset; j <= yImage + offset; j++) {
                try {
                    color = imageBitmap.getPixel(i, j);
                    red += Color.red(color);
                    green += Color.green(color);
                    blue += Color.blue(color);
                    pixelsNumber += 1;
                } catch(Exception e) {
                    //Log.w(TAG, "Error picking color!");
                }
            }
        }
        red = red / pixelsNumber;
        green = green / pixelsNumber;
        blue = blue / pixelsNumber;

        return Color.rgb(red, green, blue);
    }

    /**
     * Fill hex string with "0" when hexString minor than F.
     * @param hexString
     * @return
     */
    public static String beautyHexString(String hexString) {
        if (hexString.length() < 2) {
            return "0".concat(hexString);
        } else {
            return hexString;
        }
    }


    /**
     * Closes the specified stream.
     *
     * @param stream The stream to close.
     */
    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                android.util.Log.e(TAG, "Could not close stream", e);
            }
        }
    }

    /**
     * Copy the content of the input stream into the output stream, using a
     * temporary byte array buffer whose size is defined by
     * {@link #IO_BUFFER_SIZE}.
     *
     * @param in The input stream to copy from.
     * @param out The output stream to copy to.
     * @throws IOException If any error occurs during the copy.
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }



    /**
     * Loads a bitmap from the specified url. This can take a while, so it should not
     * be called from the UI thread.
     *
     * @param url The location of the bitmap asset
     *
     * @return The bitmap, or null if it could not be loaded
     */
    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (IOException e) {
            Log.e(TAG, "Could not load Bitmap from: " + url);
        } finally {
            closeStream(in);
            closeStream(out);
        }

        return bitmap;
    }


    /**
     * create a transparent bitmap from an existing bitmap by replacing certain
     * color with transparent
     *
     * @param bitmap
     *            the original bitmap with a color you want to replace
     * @return a replaced color immutable bitmap
     */
    public static Bitmap createTransparentBitmapFromBitmap(Bitmap bitmap,int replaceThisColor)
    {
        if (bitmap != null)
        {
            int picw = bitmap.getWidth();
            int pich = bitmap.getHeight();
            int[] pix = new int[picw * pich];
            bitmap.getPixels(pix, 0, picw, 0, 0, picw, pich);

            for (int y = 0; y < pich; y++)
            {
                // from left to right
                for (int x = 0; x < picw; x++)
                {
                    int index = y * picw + x;
                    int r = (pix[index] >> 16) & 0xff;
                    int g = (pix[index] >> 8) & 0xff;
                    int b = pix[index] & 0xff;

                    if (pix[index] == replaceThisColor)
                    {
                        pix[index] = Color.TRANSPARENT;
                    } else {
                        break;
                    }
                }

                // from right to left
                for (int x = picw - 1; x >= 0; x--)
                {
                    int index = y * picw + x;
                    int r = (pix[index] >> 16) & 0xff;
                    int g = (pix[index] >> 8) & 0xff;
                    int b = pix[index] & 0xff;

                    if (pix[index] == replaceThisColor)
                    {
                        pix[index] = Color.TRANSPARENT;
                    } else {
                        break;
                    }
                }
            }

            Bitmap bm = Bitmap.createBitmap(pix, picw, pich,
                    Bitmap.Config.ARGB_4444);

            return bm;
        }
        return null;
    }



    public static Bitmap getTextureFromBitmapResource(Context context, int resourceId)
    {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), yFlipMatrix, false);
        }
        finally  {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /* -----------------------------------------------------------------
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(getResources(), R.id.myimage, options);
    int imageHeight  = options.outHeight;
    int imageWidth   = options.outWidth;
    String imageType = options.outMimeType;
    then:
    imageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));
    ---------------------------------------------------------------------- */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,int reqWidth, int reqHeight)
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public Bitmap resizeBitmap(Bitmap source, int width,int height)
    {
        if(source.getHeight() == height && source.getWidth() == width) return source;
        int maxLength=Math.min(width,height);
        try {
            source=source.copy(source.getConfig(),true);
            if (source.getHeight() <= source.getWidth()) {
                if (source.getHeight() <= maxLength) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (maxLength * aspectRatio);

                return Bitmap.createScaledBitmap(source, targetWidth, maxLength, false);
            } else {

                if (source.getWidth() <= maxLength) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (maxLength * aspectRatio);

                return Bitmap.createScaledBitmap(source, maxLength, targetHeight, false);

            }
        }
        catch (Exception e)
        {
            return source;
        }
    }

    public void changeScreenOrientation()
    {
        int orientation = mainActivity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //showMediaDescription();
        } else {
            mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //hideMediaDescription();
        }
        if (Settings.System.getInt(mainActivity.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }, 4000);
        }
    }


    public boolean showNavigationBar(Resources resources)
    {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    public int getNavigationBarHeight()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            DisplayMetrics metrics = new DisplayMetrics();
            mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            mainActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }


    public Bitmap getProportionalBitmap(Bitmap bitmap,
                                        int newDimensionXorY,
                                        String XorY) {
        if (bitmap == null) {
            return null;
        }

        float xyRatio = 0;
        int newWidth = 0;
        int newHeight = 0;

        if (XorY.toLowerCase().equals("x")) {
            xyRatio = (float) newDimensionXorY / bitmap.getWidth();
            newHeight = (int) (bitmap.getHeight() * xyRatio);
            bitmap = Bitmap.createScaledBitmap(
                    bitmap, newDimensionXorY, newHeight, true);
        } else if (XorY.toLowerCase().equals("y")) {
            xyRatio = (float) newDimensionXorY / bitmap.getHeight();
            newWidth = (int) (bitmap.getWidth() * xyRatio);
            bitmap = Bitmap.createScaledBitmap(
                    bitmap, newWidth, newDimensionXorY, true);
        }
        return bitmap;
    }

    public static Bitmap getOvalCroppedBitmap(Bitmap bitmap, int radius)
    {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
                    false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        RectF oval = new RectF(0, 0, 130, 150);
        canvas.drawOval(oval, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, oval, paint);

        return output;
    }

    public Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius)
    {
        Bitmap finalBitmap;

        if( bitmap.getWidth() != radius || bitmap.getHeight() != radius )
        {
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        } else
        {
            finalBitmap = bitmap;
        }

        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0, finalBitmap.getWidth(), finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));

        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f,
                finalBitmap.getHeight() / 2 + 0.7f,
                finalBitmap.getWidth() / 2 + 0.1f, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

    static Bitmap resize(Bitmap image, int maxWidth, int maxHeight)
    {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
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

    public static float pxFromDp(final Context context, final float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public String XML_Get_Data(String xml,String name)
    {
        if(xml.length() == 0) return "";
        if(xml.contains(name) == true)
        {
            String strTmp;
            int name_pos = xml.indexOf(name);
            int start = xml.indexOf(">",name_pos);
            int stop = xml.indexOf("<",name_pos);
            strTmp = xml.substring(start + 1,stop);
            return strTmp;
        }
        return "";
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

    //1440 - 375
    public static int CenterBitmap(int bmpw, int Screenwidth)
    {
        return (int)((Screenwidth - bmpw)/2f) - (int)(20/2f);
    }

    public static String getStringDate()
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static int dip2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue)
    {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static boolean isDecimal(String string)
    {
        // Check whether string has a leading zero but is not "0"
        if (string.startsWith("0"))
        {
            return string.length() == 1;
        }
        for(char c : string.toCharArray())
        {
            if(c < '0' || c > '9')
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidIPv4(String address)
    {
        if (address.length() == 0)
        {
            return false;
        }

        int octet;
        int octets = 0;

        String temp = address + ".";

        int pos;
        int start = 0;
        while (start < temp.length() && (pos = temp.indexOf('.', start)) > start)
        {
            if (octets == 4)
            {
                return false;
            }
            try
            {
                octet = Integer.parseInt(temp.substring(start, pos));
            } catch (NumberFormatException ex)
            {
                return false;
            }
            if (octet < 0 || octet > 255)
            {
                return false;
            }
            start = pos + 1;
            octets++;
        }

        return octets == 4;
    }








    /**
     *
     * @param path
     * @param sampleSize 1 = 100%, 2 = 50%(1/2), 4 = 25%(1/4), ...
     * @return
     */
    public static Bitmap getBitmapFromLocalPath(String path, int sampleSize)
    {
        try
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            return BitmapFactory.decodeFile(path, options);
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }

        return null;
    }

    /**
     *
     * @param bytes
     * @return
     */
    public static Bitmap getBitmapFromBytes(byte[] bytes)
    {
        try
        {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }

        return null;
    }

    /**
     *
     * @param bitmap
     * @param quality 1 ~ 100
     * @return
     */
    public static byte[] compressBitmap(Bitmap bitmap, int quality)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);

            return baos.toByteArray();
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }

        return null;
    }

    /**
     *
     * @param srcBitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap getResizedBitmap3(Bitmap srcBitmap, int newWidth, int newHeight)
    {
        try
        {
            return Bitmap.createScaledBitmap(srcBitmap, newWidth, newHeight, true);
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }

        return null;
    }

    /**
     * captures given view and converts it to a bitmap
     * @param view
     * @return
     */
    public static Bitmap captureViewToBitmap(View view)
    {
        Bitmap result = null;

        try
        {
            result = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
            view.draw(new Canvas(result));
        }
        catch(Exception e)
        {
            //Logger.e(e.toString());
        }

        return result;
    }

    /**
     *
     * @param original
     * @param format
     * @param quality
     * @param outputLocation
     * @return
     */
    public static boolean saveBitmap(Bitmap original, Bitmap.CompressFormat format, int quality, String outputLocation)
    {
        if(original == null)
            return false;

        try
        {
            return original.compress(format, quality, new FileOutputStream(outputLocation));
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }

        return false;
    }

    /**
     *
     * @param filepath
     * @param widthLimit
     * @param heightLimit
     * @param totalSize
     * @return
     */
    public static Bitmap getResizedBitmap2(String filepath, int widthLimit, int heightLimit, int totalSize)
    {
        int outWidth = 0;
        int outHeight = 0;
        int resize = 1;
        InputStream input = null;

        try
        {
            input = new FileInputStream(new File(filepath));

            BitmapFactory.Options getSizeOpt = new BitmapFactory.Options();
            getSizeOpt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, getSizeOpt);
            outWidth = getSizeOpt.outWidth;
            outHeight = getSizeOpt.outHeight;

            while((outWidth / resize) > widthLimit || (outHeight / resize) > heightLimit)
            {
                resize *= 2;
            }
            resize = resize * (totalSize + 15) / 15;

            BitmapFactory.Options resizeOpt = new BitmapFactory.Options();
            resizeOpt.inSampleSize = resize;

            input.close();
            input = null;

            input = new FileInputStream(new File(filepath));
            Bitmap bitmapImage = BitmapFactory.decodeStream(input, null, resizeOpt);
            return bitmapImage;
        }
        catch(Exception e)
        {
            //  Logger.e(e.toString());
        }
        finally
        {
            if(input != null)
            {
                try
                {
                    input.close();
                }
                catch(IOException e)
                {
                    //  Logger.e(e.toString());
                }
            }
        }
        return null;
    }



    /**
     * generate a blurred bitmap from given one
     *
     * referenced: http://incubator.quasimondo.com/processing/superfastblur.pde
     *
     * @param original
     * @param radius
     * @return
     */
    public Bitmap getBlurredBitmap(Bitmap original, int radius)
    {
        if (radius < 1)
            return null;

        int width = original.getWidth();
        int height = original.getHeight();
        int wm = width - 1;
        int hm = height - 1;
        int wh = width * height;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, p1, p2, yp, yi, yw;
        int vmin[] = new int[Math.max(width, height)];
        int vmax[] = new int[Math.max(width,height)];
        int dv[] = new int[256 * div];
        for (i=0; i<256*div; i++)
            dv[i] = i / div;

        int[] blurredBitmap = new int[wh];
        original.getPixels(blurredBitmap, 0, width, 0, 0, width, height);

        yw = 0;
        yi = 0;

        for (y=0; y<height; y++)
        {
            rsum = 0;
            gsum = 0;
            bsum = 0;
            for(i=-radius; i<=radius; i++)
            {
                p = blurredBitmap[yi + Math.min(wm, Math.max(i,0))];
                rsum += (p & 0xff0000) >> 16;
                gsum += (p & 0x00ff00) >> 8;
                bsum += p & 0x0000ff;
            }
            for (x=0; x<width; x++)
            {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                if(y==0)
                {
                    vmin[x] = Math.min(x + radius+1,wm);
                    vmax[x] = Math.max(x - radius,0);
                }
                p1 = blurredBitmap[yw + vmin[x]];
                p2 = blurredBitmap[yw + vmax[x]];

                rsum += ((p1 & 0xff0000)-(p2 & 0xff0000))>>16;
                gsum += ((p1 & 0x00ff00)-(p2 & 0x00ff00))>>8;
                bsum += (p1 & 0x0000ff)-(p2 & 0x0000ff);
                yi ++;
            }
            yw += width;
        }

        for (x=0; x<width; x++)
        {
            rsum=gsum=bsum=0;
            yp =- radius * width;
            for(i=-radius; i<=radius; i++)
            {
                yi = Math.max(0,yp) + x;
                rsum += r[yi];
                gsum += g[yi];
                bsum += b[yi];
                yp += width;
            }
            yi = x;
            for (y=0; y<height; y++)
            {
                blurredBitmap[yi] = 0xff000000 | (dv[rsum]<<16) | (dv[gsum]<<8) | dv[bsum];
                if(x == 0)
                {
                    vmin[y] = Math.min(y + radius + 1, hm) * width;
                    vmax[y] = Math.max(y - radius, 0) * width;
                }
                p1 = x + vmin[y];
                p2 = x + vmax[y];

                rsum += r[p1] - r[p2];
                gsum += g[p1] - g[p2];
                bsum += b[p1] - b[p2];

                yi += width;
            }
        }

        return Bitmap.createBitmap(blurredBitmap, width, height, Bitmap.Config.RGB_565);
    }

    /**
     * calculate optimal preview size from given parameters
     * <br>
     * referenced: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/graphics/CameraPreview.html
     *
     * @param sizes obtained from camera.getParameters().getSupportedPreviewSizes()
     * @param w
     * @param h
     * @return
     */
    public static Size getOptimalPreviewSize(List<Size> sizes, int w, int h)
    {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


    public class CountDownTimerExt extends CountDownTimer
    {
        public CountDownTimerExt(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            int progress = (int) (millisUntilFinished/1000);
        }

        @Override
        public void onFinish()
        {

        }
    }








}
