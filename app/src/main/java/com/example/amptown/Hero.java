package com.example.amptown;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class Hero {
    public int positionX;
    public int heroWidth = 125;
    private int positionY;
    private int frameWidth = 100;
    private int frameHeight = 55;
    private int heroHeight = 175;
    public boolean heroRunning = false;
    private int frameCountRun = 6;
    private int frameCountIdle = 8;
    private int currentFrameIdle = 0;
    private int currentFrameRun = 0;
    public int frameLenghtInMilisec = 50;
    public long lastFrametime = 0;
    public boolean turnedLeft = false;

    Rect heroRect;
    Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);

    Bitmap heroRun;
    Bitmap heroIdle;
    Bitmap heroRunFlipped;
    Bitmap heroIdleFlipped;

    public Hero(Context context, int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY - heroHeight;

        heroRun = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero_run);
        heroRun = Bitmap.createScaledBitmap(heroRun, 600, 55, false);

        heroIdle = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero_idle);
        heroIdle = Bitmap.createScaledBitmap(heroIdle, 800, 55, false);

        Matrix matrixRun = new Matrix();
        Matrix matrixIdle = new Matrix();

        matrixRun.postScale(-1, 1, heroRun.getWidth() / 2f, heroRun.getHeight() / 2f);
        heroRunFlipped = Bitmap.createBitmap(heroRun, 0, 0, heroRun.getWidth(), heroRun.getHeight(), matrixRun, true);
        matrixIdle.postScale(-1, 1, heroIdle.getWidth() / 2f, heroIdle.getHeight() / 2f);
        heroIdleFlipped = Bitmap.createBitmap(heroIdle, 0, 0, heroIdle.getWidth(), heroIdle.getHeight(), matrixIdle, true);

        heroRect = new Rect(this.positionX, this.positionY, this.positionX + heroWidth, this.positionY + heroHeight);
    }

    public Rect getHeroRect()
    {
        return heroRect;
    }

    public void manageCurrentFrame() {
        long time = System.currentTimeMillis();
        if (heroRunning) {
            currentFrameIdle = 0;
            if (time > lastFrametime + frameLenghtInMilisec) {
                lastFrametime = time;
                currentFrameRun++;
                if (currentFrameRun >= frameCountRun) {
                    currentFrameRun = 0;
                }
            }
            frameToDraw.left = currentFrameRun * frameWidth;
        } else {
            currentFrameRun = 0;
            if (time > lastFrametime + frameLenghtInMilisec) {
                lastFrametime = time;
                currentFrameIdle++;
                if (currentFrameIdle >= frameCountIdle) {
                    currentFrameIdle = 0;
                }
            }
            frameToDraw.left = currentFrameIdle * frameWidth;
        }
        frameToDraw.right = frameToDraw.left + frameWidth;
    }



    public void draw(Canvas canvas) {
        heroRect.set(positionX, positionY, positionX + heroWidth, positionY + heroHeight);
        if (turnedLeft) {
            if (heroRunning) {
                canvas.drawBitmap(heroRunFlipped, frameToDraw, heroRect, new Paint(Paint.FILTER_BITMAP_FLAG));
            } else {
                canvas.drawBitmap(heroIdleFlipped, frameToDraw, heroRect, new Paint(Paint.FILTER_BITMAP_FLAG));
            }
        } else {
            if (heroRunning) {
                canvas.drawBitmap(heroRun, frameToDraw, heroRect, new Paint(Paint.FILTER_BITMAP_FLAG));
            } else {
                canvas.drawBitmap(heroIdle, frameToDraw, heroRect, new Paint(Paint.FILTER_BITMAP_FLAG));
            }
        }
    }
}
