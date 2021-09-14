package com.ciapa.androgon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class ViewGame extends SurfaceView implements Runnable
{

    private Thread gameThread;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private Canvas canvas;
    private Bitmap bitmapRunningMan;
    private boolean isMoving;
    private float runSpeedPerSecond;
    private float manXPos, manYPos;
    private int frameWidth;
    private int frameHeight;
    private int frameCount;
    private int currentFrame = 0;
    private long fps;
    private long timeThisFrame;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMillisecond;

    private Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
    private RectF whereToDraw = new RectF(manXPos, manYPos, manXPos + frameWidth, frameHeight);

    public ViewGame(Context context,
                    int resID,
                    int fw,
                    int fh,
                    int fcntr,
                    float runSpeed,
                    int fLengthMillisecond,
                    float mXPos,
                    float mYPos)
    {
        super(context);

        manXPos = mXPos; // 10
        manYPos = mYPos; // 10
        frameLengthInMillisecond = fLengthMillisecond; // 50
        runSpeedPerSecond = runSpeed; //500
        frameWidth = fw;
        frameHeight = fh;
        frameCount = fcntr;

        ourHolder = getHolder();
        bitmapRunningMan = BitmapFactory.decodeResource(getResources(), resID);
        bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * frameCount, frameHeight, false);
    }

    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;

            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    public void update() {
        if (isMoving) {
            manXPos = manXPos + runSpeedPerSecond / fps;

            if (manXPos > getWidth()) {
                manYPos += (int) frameHeight;
                manXPos = 10;
            }

            if (manYPos + frameHeight > getHeight()) {
                manYPos = 10;
            }
        }
    }

    public void manageCurrentFrame() {
        long time = System.currentTimeMillis();

        if (isMoving) {
            if (time > lastFrameChangeTime + frameLengthInMillisecond) {
                lastFrameChangeTime = time;
                currentFrame++;

                if (currentFrame >= frameCount) {
                    currentFrame = 0;
                }
            }
        }

        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;
    }

    public void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            whereToDraw.set((int) manXPos, (int) manYPos, (int) manXPos + frameWidth, (int) manYPos + frameHeight);
            manageCurrentFrame();
            canvas.drawBitmap(bitmapRunningMan, frameToDraw, whereToDraw, null);
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playing = false;

        try {
            gameThread.join();
        } catch(InterruptedException e) {
            Log.e("ERR", "Joining Thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN :
                isMoving = !isMoving;
                break;
        }

        return true;
    }
}
