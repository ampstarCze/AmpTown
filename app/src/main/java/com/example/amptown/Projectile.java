package com.example.amptown;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class Projectile {

    public int positionX;
    public int positionY;

    private int sizeX=25;
    private int sizeY=50;
    private int speed = 4;

    Bitmap arrowImage;

    Rect projectileRect;
    Rect projectileFrame;


    public Projectile(Context context, int width)
    {
        Random random = new Random();
        positionX = random.nextInt(width) + 1;
        positionY = -60;

        arrowImage = BitmapFactory.decodeResource(context.getResources(),R.drawable.arrow);
        arrowImage = Bitmap.createScaledBitmap(arrowImage, 30, 180, false);

        projectileFrame = new Rect(0,0,30,180);
        projectileRect = new Rect(this.positionX, this.positionY,positionX+sizeX,positionY+sizeY);
    }

    public void move()
    {
        this.positionY += speed;
        projectileRect.set(this.positionX, this.positionY,positionX+sizeX,positionY+sizeY);
    }

    public boolean checkHit(Hero hero)
    {
        return Rect.intersects(projectileRect,hero.getHeroRect());
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(arrowImage, projectileFrame, projectileRect, null);
    }
}
