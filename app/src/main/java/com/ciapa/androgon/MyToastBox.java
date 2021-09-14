package com.ciapa.androgon;

import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class MyToastBox
{
    long TimeOutTime  = 1000;  // 1 sek
    long start_time = 0;
    private MainActivity main;
    String message;
    float screenWidth;
    float screenHeight;
    Rect toast_box_rect;
    Paint paint;
    Paint mTextPaint;
    TextPaint textPaint;
    float mTextPaintHeight;
    float mTextPaintWidth;
    Rect  mTmpRect;
    float messageX;
    float messageY;
    Context context;
    Bitmap bmpIcon;
    boolean DrawIcon = false;
    float iconX;
    float iconY;
    float toastWidth;
    StaticLayout staticLatout;

    public MyToastBox(MainActivity activity)
    {
        main = activity;
        context = main.getApplicationContext();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        main.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setFakeBoldText(true);

        toast_box_rect = new Rect((int)screenWidth/15,
                (int)screenHeight / 3,
                (int)screenWidth - (int)screenWidth/15,
                (int)(screenHeight / 3)*2);

        toastWidth = screenWidth - (2*(screenWidth/15));

        messageX = toast_box_rect.left + 50;
        messageY = toast_box_rect.top + ((screenHeight / 3)/2);

        mTextPaint = new Paint(Paint.LINEAR_TEXT_FLAG | ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(main.util.pxFromDp(context, 20));
        mTmpRect = new Rect();

        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(main.util.pxFromDp(context, 20));

        // newer API:
        // StaticLayout.Builder builder = StaticLayout.Builder.obtain(text, 0, text.length(), myTextPaint, width);
        // StaticLayout myStaticLayout = builder.build();

    }// end constructor

    public void SetIcon(int resID)
    {
        bmpIcon = BitmapFactory.decodeResource(main.getResources(), resID);
        DrawIcon = true;
        iconX = toast_box_rect.right - bmpIcon.getWidth() - 15;
        iconY = toast_box_rect.bottom - bmpIcon.getHeight() - 15;
    }

    public void SetText(String text)
    {
        message = text;
        mTextPaint.getTextBounds(message,0,2,mTmpRect);
        mTextPaintHeight = mTmpRect.height();
        mTextPaintWidth = mTmpRect.width();
        messageX = toast_box_rect.left + 50;
        messageY = toast_box_rect.top + ((screenHeight / 3)/2) - (mTextPaintHeight * 2);

        staticLatout = null;
        staticLatout = new StaticLayout(message,
                textPaint,
                (int)toastWidth,
                Layout.Alignment.ALIGN_CENTER,
                1,
                1,
                true);
    }

    public void StartTimeOut(int time)
    {
        TimeOutTime  = System.currentTimeMillis() + time;
        start_time = System.currentTimeMillis();
    }

    private float getTextHeight(String text, Paint paint)
    {

        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    public void DrawTimeOut(Canvas canvas)
    {
        if(System.currentTimeMillis() < TimeOutTime)
        {
            paint.setColor(Color.argb(255,255,255,255));
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRoundRect(toast_box_rect.left - 2,
                    toast_box_rect.top - 2,
                    toast_box_rect.right + 2,
                    toast_box_rect.bottom + 2,
                    50,
                    50,
                    paint);
            paint.setColor(Color.argb(200,180,5,1));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(toast_box_rect.left,
                    toast_box_rect.top,
                    toast_box_rect.right,
                    toast_box_rect.bottom,
                    50,
                    50,
                    paint);

            if(DrawIcon)
            {
                canvas.drawBitmap(bmpIcon, iconX, iconY, null);
            }

            float textHeight = getTextHeight(message, textPaint);
            int numberOfTextLines = staticLatout.getLineCount();
            float textYCoordinate = toast_box_rect.exactCenterY() - ((numberOfTextLines * textHeight) / 2);
            float textXCoordinate = toast_box_rect.left;
            canvas.translate(textXCoordinate, textYCoordinate);
            staticLatout.draw(canvas);
            //canvas.drawText(message ,messageX,messageY, mTextPaint);
        }
    }
}
