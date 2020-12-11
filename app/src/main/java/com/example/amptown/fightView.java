package com.example.amptown;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.LinkedList;

public class fightView extends SurfaceView implements SurfaceHolder.Callback, View.OnClickListener {

    private int width;
    private int height;
    public fightLoop fightLoop;
    private Hero hero;
    private accelerometerControls accelerometerControls;
    private LinkedList<Projectile> projectiles = new LinkedList<>();
    private Projectile tempProjectile;
    private long projectileCooldownInMS = 1200;
    private long projectileCooldownLeft = projectileCooldownInMS;
    private CountDownTimer projectileCooldown;
    private int fightTimeLeft = 60;
    private int DPS = 0;
    private int damageClick = 1;
    private int MaxHP = 1500;
    private int HP = MaxHP;
    private Context context;
    private long frameTime;
    private boolean paused = false;
    private static database db;

    private int banditWood;
    private int banditStone;
    private int banditGold;

    private fightView fightView;

    TextView timeText;
    TextView hpText;
    ImageButton crossedSwords;

    public fightView(Context context) {
        super(context);
        init(context);
    }

    public fightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public fightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void initText() {
        timeText = ((Activity) context).findViewById(R.id.time_left);
        hpText = ((Activity) context).findViewById(R.id.text_hp);
        crossedSwords = ((Activity) context).findViewById(R.id.crossed_swords);
        crossedSwords.setOnClickListener(this);
        fightView = findViewById(R.id.fight_view);
    }

    void init(Context context) {
        SurfaceHolder surfaceHolder = getHolder();
        this.context = context;
        surfaceHolder.addCallback(this);

        db = new database(this.context);

        Cursor cursor = db.getData(game_main.ID);
        cursor.moveToFirst();

        if (game_battle.banditCamp) {
            MaxHP = 2000 + (500 * (game_main.day - 2));
            HP = MaxHP;
            banditWood = cursor.getInt(cursor.getColumnIndex("banditWood"));
            banditStone = cursor.getInt(cursor.getColumnIndex("banditStone"));
            banditGold = cursor.getInt(cursor.getColumnIndex("banditGold"));
        } else {
            for (int i = 1; i < game_battle.DungLVL; i++) {
                MaxHP *= 1.5;
            }
            HP = MaxHP;
        }
        DPS = cursor.getInt(cursor.getColumnIndex("soldiersDPS"));
        int swordLVL = cursor.getInt(cursor.getColumnIndex("swordLVL"));
        for (int i = 1; i <= swordLVL; i++) {
            damageClick *= 2;
        }

        fightLoop = new fightLoop(this, surfaceHolder);
        accelerometerControls = new accelerometerControls(context);
        setFocusable(true);
        frameTime = System.currentTimeMillis();

        projectileCooldown = new CountDownTimer(projectileCooldownLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                projectileCooldownLeft = millisUntilFinished;
            }

            @Override
            public void onFinish() {

                tempProjectile = new Projectile(context, width);
                projectiles.add(tempProjectile);
                projectileCooldownLeft = projectileCooldownInMS;
                projectileCooldown.start();
            }
        }.start();
        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        fightTimerInit();
        resume();
    }

    private void dealDMG(float DMG) {
        HP -= DMG;
        if (HP <= 0) {
            fightTimeLeft = 0;
            fightLoop.stopLoop();
            paused = true;
            hpText.setText("0 / " + MaxHP);
            game_battle.battleFinalCont.setVisibility(VISIBLE);
            game_battle.battleResult.setText("You won!");
            fightView.setVisibility(GONE);

            if (game_battle.banditCamp) {
                game_main.wood += banditWood + (banditWood * 0.2);
                game_main.stone += banditStone + (banditStone * 0.2);
                game_main.gold += banditGold + (banditGold * 0.2);
                game_main.banditNext = game_main.day + 3;
                game_main.banditSpawned = 0;
                map.camp.setVisibility(GONE);
                db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                db.update(game_main.ID, "banditWood", 0);
                db.update(game_main.ID, "banditStone", 0);
                db.update(game_main.ID, "banditGold", 0);
                db.update(game_main.ID, "banditNext", game_main.day + 3);
                db.update(game_main.ID, "banditSpawned", 0);
            } else {
                game_main.dungMaxLvl += 1;
                game_main.wood += game_battle.DungLVL * 120;
                game_main.stone += game_battle.DungLVL * 100;
                game_main.gold += game_battle.DungLVL * 150;
                db.update(game_main.ID, "dungMax", game_battle.DungLVL + 1);
                db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
            }
        } else {
            hpText.setText(HP + " / " + MaxHP);
        }

    }

    private void fightTimerInit() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!paused) {
                    if (fightTimeLeft > 0) {
                        fightTimeLeft--;
                        timeText.setText(String.valueOf(fightTimeLeft));
                        dealDMG(DPS);
                        handler.postDelayed(this, 1000);
                    } else {
                        if (HP > 0) {
                            timeText.setText(String.valueOf(0));
                        }
                        fightLoop.stopLoop();
                        game_battle.battleFinalCont.setVisibility(VISIBLE);
                        game_battle.battleResult.setText("You lose!");
                        fightView.setVisibility(GONE);
                    }
                }
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        hero.draw(canvas);

        for (int i = 0; i < projectiles.size(); i++) {
            tempProjectile = projectiles.get(i);
            tempProjectile.draw(canvas);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if (fightLoop.getState().equals(Thread.State.TERMINATED)) {
            fightLoop = new fightLoop(this, holder);
        }
        fightLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    public void update() {
        if (hero == null) {
            hero = new Hero(getContext(), width / 2, height);
        }

        int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
        frameTime = System.currentTimeMillis();

        hero.manageCurrentFrame();

        float xSpeed = (accelerometerControls.xAccel * width / 2000f) * -1;

        if (Math.abs(xSpeed * elapsedTime) > 5) {
            int lastpostX = hero.positionX;

            hero.positionX += xSpeed * elapsedTime;
            hero.heroRunning = true;
            if ((lastpostX - hero.positionX) < 0) {
                if (hero.turnedLeft) {
                    hero.turnedLeft = false;
                }
            } else {
                if (!hero.turnedLeft) {
                    hero.turnedLeft = true;
                }
            }
        } else {
            hero.heroRunning = false;
        }

        if (hero.positionX < 0) {
            hero.positionX = 0;
        } else if (hero.positionX > width - hero.heroWidth) {
            hero.positionX = width - hero.heroWidth;
        }


        for (int i = 0; i < projectiles.size(); i++) {
            tempProjectile = projectiles.get(i);
            tempProjectile.move();

            if (tempProjectile.checkHit(hero)) {
                removeProjectile(i);
                fightTimeLeft -= 5;
            }

            if (tempProjectile.positionY > height) {
                removeProjectile(i);
            }
        }
    }

    void removeProjectile(int id) {
        projectiles.remove(id);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.crossed_swords) {
            dealDMG(damageClick);
        }

    }

    public void resume() {
        if (paused) {
            paused = false;
            fightTimerInit();
            projectileCooldown.start();
        }
    }

    public void pause() {
        paused = true;
        projectileCooldown.cancel();
        fightLoop.stopLoop();
    }
}
