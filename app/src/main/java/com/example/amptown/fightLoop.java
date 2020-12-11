package com.example.amptown;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class fightLoop extends Thread {
    private boolean isRunning = false;
    private final SurfaceHolder surfaceHolder;
    private fightView fightView;

    private static final double MAX_UPS = 30.0;
    private static final double UPS_PERIOD = 1E+3 / MAX_UPS;
    long startTime;
    long elapsedTime;
    long sleepTime;
    int updateCount = 0;
    int frameCount = 0;


    public fightLoop(fightView fightView, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.fightView = fightView;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();
        Canvas canvas = null;

        startTime = System.currentTimeMillis();

        while (isRunning) {
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    fightView.update();
                    updateCount++;
                    fightView.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (sleepTime < 0 && updateCount < MAX_UPS - 1) {
                fightView.update();
                updateCount++;
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= 1000) {
                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
    public void stopLoop()
    {
        isRunning = false;
        try {
            join();
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
