package com.matti.gravity;

/**
 * SharedPrefs används och INTE Bundle savedInstanceState
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by matti on 11/02/16.
 */
public class GravityView2 extends SurfaceView implements SurfaceHolder.Callback, ITickListener
{
    private static String CLASS_NAME = "GravityView2";

    private Context mContext;
    private SurfaceHolder surfaceHolder;
    private int screenWidth = -1;
    private int screenHeight = -1;
    private List<AnimatedSprite> currentHostileSprites;
    private AnimatedSprite[] hostileSprites;
    private AnimatedSprite[] numberSprites;
    private Bitmap arrowUpRegBitmap = null;
    private Bitmap fireBitmap = null;
    private Bitmap[] numberBitmaps = null;
    private Bitmap hostileHitBitmap = null;
    private AnimatedSprite hostileHitSprite = null;
    private AnimatedSprite fireSprite = null;
    private List<AnimatedSprite> currentFireSprites;
    private Bitmap arrowDownRegBitmap = null;
    private Bitmap arrowUpBitmap = null;
    private Bitmap arrowDownBitmap = null;
    private Bitmap arrowUpGlowBitmap = null;
    private Bitmap arrowDownGlowBitmap = null;
    private Bitmap flippedMain = null;
    private Bitmap[] bitmaps;
    private Bitmap[] mainBirdBitmaps;
    private Bitmap[] bitmapsRun;
    private Bitmap[] bitmapsDebug;
    private AnimatedSprite currentHostileSprite;
    private AnimatedSprite explosionSprite;
    private AnimatedSpriteMainBird mainBirdSprite;
    private Bitmap wavesBitmap;
    private Bitmap skyBitmap;
    private Paint paint01;
    private Paint paint02;
    private Paint paint03;
    private GravityThread thread;
    private static final int WAVES_ANIM_INTERVAL_MAX = 30;
    private int wavesAnimIntervalCnt = 0;
    private int mainBirdXDir = 1;
    private int mainBirdX = 1;
    private Resources resources = null;
    public static final int LARGEMENT_FACTOR = 2;
    private int touchActionMainBird = 0;
    private int touchActionFire = 0;
    private boolean crashed = false;
    private boolean crashedTop = false;
    private boolean crashedBottom = false;
    private boolean crashedHostile = false;
    private long ticks = 0;
    private int crashedProgress = 0;
    private int startDragX = 0;
    private int endDragX = 0;
    private float screenDensity = -1.0f;
    private int highScore = -1;
    private SharedPreferences sharedPrefs = null;
    private boolean noCrash = false;
    private int noCrashCounter = 0;
    private float largementFactor = -1.0f;
    private int score = 0;
    private boolean touchInsideUpDown = false;
    private long fireMillis = 0;
    private PointsHandler pointsHandler = null;
    private int pointerIdMainBird = -1;
    private int pointerIdFire = -1;

    /**
     *
     */
    public GravityView2(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.mContext = context;

        Log.d(CLASS_NAME, "GravityView D " + this.getResources().getDisplayMetrics().density + ", this " + this);

        this.screenDensity = this.getResources().getDisplayMetrics().density;

        // register our interest in hearing about changes to our surface
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);

        this.thread = new GravityThread(this);

        this.resources = context.getResources();

        this.largementFactor = this.getFact(this.screenDensity);

        this.pointsHandler = PointsHandler.getInstance();
        this.pointsHandler.setResources(this.resources);
        this.pointsHandler.setLargementFactor(this.largementFactor);
        this.pointsHandler.setScreenDensity(this.screenDensity);
        this.pointsHandler.init();

        this.initBitmaps();

        this.wavesBitmap = this.bitmaps[1];

        this.skyBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.sky13_2);

        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        this.flippedMain = Bitmap.createBitmap(this.mainBirdBitmaps[0], 0, 0, this.mainBirdBitmaps[0].getWidth(), this.mainBirdBitmaps[0].getHeight(), matrix, true);

        this.mainBirdSprite = new AnimatedSpriteMainBird2(new int[]{0, 1}, 80, 80, 0, largementFactor, this.screenDensity);
        this.mainBirdSprite.setBitmap(this.bitmaps[2]);

        this.hostileSprites = new AnimatedSprite[6];
        this.hostileSprites[0] = new AnimatedSprite(new int[]{0, 1, 2, 1}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[0].setBitmap(this.bitmaps[3]);

        this.hostileSprites[1] = new AnimatedSprite(new int[]{0, 1, 2, 1}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[1].setBitmap(this.bitmaps[4]);
// 3
        this.hostileSprites[3] = new AnimatedSprite(new int[]{0, 1, 2, 1}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[3].setBitmap(this.bitmaps[6]);

        this.hostileSprites[4] = new AnimatedSprite(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[4].setBitmap(this.bitmaps[7]);

        this.hostileSprites[2] = new AnimatedSprite(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[2].setBitmap(this.bitmaps[5]);

        this.hostileSprites[5] = new AnimatedSprite(new int[]{0, 1, 2, 3, 4, 5, 6, 6, 5, 4, 3, 2, 1, 0}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[5].setBitmap(this.bitmaps[8]);
        this.hostileSprites[5].isBonus = true;

        this.currentHostileSprite = this.hostileSprites[1];
        this.currentHostileSprites = new ArrayList<AnimatedSprite>();

        this.explosionSprite = new AnimatedSprite(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, 80, 80, 0, largementFactor, this.screenDensity);
        this.explosionSprite.setBitmap(this.bitmaps[0]);
        this.explosionSprite.posX = 100;
        this.explosionSprite.posY = 100;
        this.explosionSprite.ticksToAnim = 3;
        this.explosionSprite.setLoopAnim(false);

        this.fireSprite = null;
        this.currentFireSprites = new ArrayList<AnimatedSprite>();

        this.paint01 = new Paint();
        this.paint01.setAntiAlias(true);
        this.paint01.setARGB(255, 50, 100, 100);

        this.paint02 = new Paint();
        this.paint02.setColor(Color.CYAN);
        this.paint02.setStyle(Paint.Style.FILL);
        this.paint02.setTextSize(40);

        this.paint03 = new Paint();
        this.paint03.setColor(Color.RED);
        this.paint03.setTextSize(40);

        setFocusable(true);

        Log.d(CLASS_NAME, "GravityView SIZE_FACTOR " + this.screenDensity);
    }

    /**
     *
     */
    private void initBitmaps()
    {
        this.mainBirdBitmaps = new Bitmap[3];
        this.mainBirdBitmaps[0] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites01_04_160x80);
        this.mainBirdBitmaps[1] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites01_04_5_160x80);
        this.mainBirdBitmaps[2] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites01_04_2_160x80);

        this.bitmapsRun = new Bitmap[9];
        this.bitmapsRun[0] = BitmapFactory.decodeResource(this.resources, R.drawable.explosion05);
        this.bitmapsRun[1] = BitmapFactory.decodeResource(this.resources, R.drawable.waves03);
        this.bitmapsRun[2] = this.mainBirdBitmaps[0];

        this.bitmapsRun[3] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites03_02_3_240x80);
        this.bitmapsRun[4] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites03_03_3_240x80);
        this.bitmapsRun[5] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites04_05_3_720x80);
        this.bitmapsRun[6] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites02_02_240x80);
        this.bitmapsRun[7] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites04_03_720x80);
        this.bitmapsRun[8] = BitmapFactory.decodeResource(this.resources, R.drawable.hearts07);
//        this.bitmapsRun[8] = BitmapFactory.decodeResource(this.resources, R.drawable.penguin_animation04);

        this.bitmapsDebug = new Bitmap[9];
        this.bitmapsDebug[0] = BitmapFactory.decodeResource(this.resources, R.drawable.explosion05_2);
        this.bitmapsDebug[1] = BitmapFactory.decodeResource(this.resources, R.drawable.waves03);
        this.bitmapsDebug[2] = this.mainBirdBitmaps[2];
//        this.bitmapsDebug[2] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites01_04_2_160x80);
//        this.bitmapsDebug[2] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites01_04_5_160x80);

        this.bitmapsDebug[3] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites03_02_2_240x80);
        this.bitmapsDebug[4] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites03_03_2_240x80);
        this.bitmapsDebug[5] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites04_05_2_720x80);
        this.bitmapsDebug[6] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites02_02_2_240x80);
        this.bitmapsDebug[7] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites04_03_2_720x80);
//        this.bitmapsDebug[8] = BitmapFactory.decodeResource(this.resources, R.drawable.penguin_animation03);
        this.bitmapsDebug[8] = BitmapFactory.decodeResource(this.resources, R.drawable.hearts07);

//        this.bitmaps = this.bitmapsDebug;
        this.bitmaps = this.bitmapsRun;

        for (int i = 0; i < this.bitmaps.length; i++)
        {
            Bitmap bm = this.bitmaps[i];
            Log.d(CLASS_NAME, "initBitmaps this.bitmaps [" + i + "] " + bm);
        }

        this.arrowDownRegBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.arrow_down_01_01);
        this.arrowUpRegBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.arrow_up_01_01);
        this.arrowDownGlowBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.arrow_down_glow_01_02);
        this.arrowUpGlowBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.arrow_up_glow_01_02);
        this.arrowDownBitmap = this.arrowDownRegBitmap;
        this.arrowUpBitmap = this.arrowUpRegBitmap;

        this.fireBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.fire_03_02);
        this.hostileHitBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.fire_03_04);

        this.numberBitmaps = new Bitmap[5];
        this.numberSprites = new AnimatedSprite[5];
        this.numberBitmaps[0] = BitmapFactory.decodeResource(this.resources, R.drawable.one_hundred_01);
        this.numberBitmaps[1] = BitmapFactory.decodeResource(this.resources, R.drawable.two_hundreds_01);
        this.numberBitmaps[2] = BitmapFactory.decodeResource(this.resources, R.drawable.three_hundreds_01);
        this.numberBitmaps[3] = BitmapFactory.decodeResource(this.resources, R.drawable.four_hundreds_01);
        this.numberBitmaps[4] = BitmapFactory.decodeResource(this.resources, R.drawable.five_hundreds_01);
    }

    /**
     *
     */
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        Log.d(CLASS_NAME, "onAttachedToWindow");
    }

    /**
     *
     */
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        Log.d(CLASS_NAME, "onDetachedFromWindow");
    }

    /**
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(CLASS_NAME, "onMeasure w " + widthMeasureSpec + ", h " + heightMeasureSpec);
    }

    /**
     *
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility)
    {
        super.onWindowVisibilityChanged(visibility);
        Log.d(CLASS_NAME, "onWindowVisibilityChanged v " + visibility);
    }

    /**
     *
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus)
    {
        Log.d(CLASS_NAME, "onWindowFocusChanged hasWindowFocus " + hasWindowFocus + ", thread state " +
                ((this.thread == null) ? "NULL" : this.thread.getState().toString()) +
                ", h " + this.getHeight() + ", w " + this.getWidth());

        if (!hasWindowFocus)
        {
            if (this.thread == null)
            {
                Log.e(CLASS_NAME, "onWindowFocusChanged");
                return;
            }
        } else
        {
            Log.d(CLASS_NAME, "onWindowFocusChanged t " + ((this.thread == null) ? "NULL" : this.thread));

            if ((this.thread != null))
            {
                this.killThread();
            }
            this.thread = new GravityThread(this);
            this.thread.start();
        }
    }

    /**
     *
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        Log.d(CLASS_NAME, "surfaceChanged w " + width + ", h " + height + ", d " + this.getResources().getDisplayMetrics().density);

        this.screenHeight = height;
        this.screenWidth = width;

        this.mainBirdSprite.screenTop = 70;
        this.mainBirdSprite.screenBottom = screenHeight - 130;

        int posX = (int) ((this.screenWidth / 2.0) - (this.mainBirdSprite.width * this.screenDensity * this.getFact(this.screenDensity)) / 2.0f);
        posX = 300;
        this.mainBirdSprite.posX = posX;
        this.mainBirdSprite.posY = (int) (this.screenHeight / 2.0);
        this.mainBirdSprite.MAX_SPEED = 20 * ((Math.min(this.screenHeight, this.screenWidth) > 720) ? 2 : 1);
        this.mainBirdSprite.MIN_SPEED = -20 * ((Math.min(this.screenHeight, this.screenWidth) > 720) ? 2 : 1);

        this.mainBirdX = this.mainBirdSprite.posX;
        this.mainBirdXDir = 1;

        for (AnimatedSprite hostileSprite : this.hostileSprites)
        {
            hostileSprite.screenRight = width;
            hostileSprite.screenLeft = 0;
            hostileSprite.screenTop = 70;
            hostileSprite.screenBottom = screenHeight - 130;
            hostileSprite.ticksToAnim = 3;
            hostileSprite.updatePos(this.screenWidth, 200);
        }

        this.currentHostileSprites.add(this.currentHostileSprite.clone());

        Log.d(CLASS_NAME, "surfaceChanged chs " + this.currentHostileSprite.bitmap);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder)
    {
        Log.d(CLASS_NAME, "surfaceCreated");
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.d(CLASS_NAME, "surfaceDestroyed");

        this.killThread();
    }

    /**
     *
     */
    private void handleTouchCrashed(MotionEvent event)
    {
        if (!this.crashed)
        {
            this.crashedTop = false;
            this.crashedBottom = false;
            this.crashedHostile = false;
        } else
        {
            int ta = event.getAction();

            if (this.crashedProgress == 2)
            {
                if (ta == MotionEvent.ACTION_DOWN)
                {
                    this.startDragX = (int) event.getX();
                    this.endDragX = this.startDragX;
                }

                if (ta == MotionEvent.ACTION_UP)
                {
                    this.endDragX = (int) event.getX();
                }

//                Log.d (CLASS_NAME, "onTouchEvent sx " + this.startDragX + ", ex " + this.endDragX);

                if (Math.abs(this.startDragX - this.endDragX) > 200)
                {
                    if (this.startDragX > this.endDragX)
                    {
                        this.reset();
                    } else
                    {
                        Log.d(CLASS_NAME, "onTouchEvent Quit ");
                        this.killThread();
                        ((MainActivity2) this.mContext).finishAffinity();
                    }
                }
            }
        }
    }

    /**
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
//        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
//            Log.d(CLASS_NAME, "onTouchEvent a " + String.format("0x%X", event.getAction()) + ", x " + event.getX() + ", y " + event.getY() + ", tIUD " + this.touchInsideUpDown);
        }

        int touchX = -1;
        int touchY = -1;

        int ta = event.getAction();

        this.handleTouchCrashed(event);

        int index = event.getActionIndex();
        int pId = event.getPointerId(index);
        int action = event.getActionMasked();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                touchX = (int) event.getX();
                touchY = (int) event.getY();
                ta = MotionEvent.ACTION_DOWN;
                break;
            }

            case MotionEvent.ACTION_UP:
            {
                ta = MotionEvent.ACTION_UP;
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN:
            {
                touchX = (int) event.getX(index);
                touchY = (int) event.getY(index);
                ta = MotionEvent.ACTION_DOWN;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
            {
                ta = MotionEvent.ACTION_UP;
                break;
            }
        }

        if (ta == MotionEvent.ACTION_DOWN)
        {
//            Log.d(CLASS_NAME, "onTouchEvent D ");
            if ((touchX > 1651) && (touchX < 1920) && (touchY > 369) && (touchY < 601))
            {
                this.touchActionMainBird = 1;
                this.touchInsideUpDown = true;
                this.arrowUpBitmap = this.arrowUpGlowBitmap;
                this.pointerIdMainBird = pId;
            }

            if ((touchX > 1651) && (touchX < 1920) && (touchY > 700) && (touchY < 950))
            {
                this.touchActionMainBird = -1;
                this.touchInsideUpDown = true;
                this.arrowDownBitmap = this.arrowDownGlowBitmap;
                this.pointerIdMainBird = pId;
            }

            if ((touchX < 1000))
            {
                this.pointerIdFire = pId;
                this.touchActionFire = 2;

                long currentMillis = System.currentTimeMillis();
                if (Math.abs(currentMillis - this.fireMillis) > 500)
                {
                    synchronized (this)
                    {
                        AnimatedSprite newFire = new AnimatedSprite(new int[]{0}, 80, 60, 0, largementFactor / 2, this.screenDensity);
                        newFire.posX = this.mainBirdSprite.posX + 100;
                        newFire.posY = this.mainBirdSprite.posY + 50;
                        newFire.ticksToAnim = 3;
                        newFire.moveStep = 20;
                        newFire.setBitmap(this.fireBitmap);
                        newFire.setLoopAnim(false);
                        newFire.isToLeft = false;
                        newFire.screenRight = this.screenWidth;
                        newFire.screenLeft = 0;
                        newFire.screenTop = 70;
                        newFire.screenBottom = this.screenHeight - 130;
                        this.currentFireSprites.add(newFire);

                        this.fireMillis = currentMillis;
                    }
                }
            }
        }

        if (ta == MotionEvent.ACTION_UP)
        {
            if (pId == this.pointerIdMainBird)
            {
                this.touchActionMainBird = 0;
                this.touchInsideUpDown = false;
            }
        }

        if (ta == MotionEvent.ACTION_MOVE)
        {
        }

        if (!this.touchInsideUpDown)
        {
            this.arrowDownBitmap = this.arrowDownRegBitmap;
            this.arrowUpBitmap = this.arrowUpRegBitmap;
            this.touchActionMainBird = 0;
        }

        return true;
    }

    /**
     *
     */
    private void reset()
    {
        Log.d(CLASS_NAME, "onTouchEvent Restart ");
        this.killThread();

        this.crashed = false;
        this.crashedTop = false;
        this.crashedBottom = false;
        this.crashedHostile = false;
        this.crashedProgress = 0;
        this.ticks = 0;
        this.score = 0;

        this.mainBirdSprite.posX = (int) ((this.screenWidth / 2.0) - (this.mainBirdSprite.width * this.screenDensity * this.getFact(this.screenDensity)) / 2.0f);
        this.mainBirdSprite.posX = 300;
        this.mainBirdSprite.posY = (int) (this.screenHeight / 2.0);
        this.explosionSprite.resetAnimSequence();

        this.mainBirdX = this.mainBirdSprite.posX;
        this.mainBirdXDir = 1;

        for (int i = 0; i < this.hostileSprites.length; i++)
        {
            this.hostileSprites[i].updatePos(this.screenWidth, 300);
        }

        this.currentHostileSprites.clear();
        this.currentHostileSprites.add(this.currentHostileSprite.clone());

        this.thread = new GravityThread(this);
        this.thread.start();

        this.mainBirdSprite.setBitmap(this.mainBirdBitmaps[0]);
    }

    /**
     *
     */
    private void killThread()
    {
        Log.d(CLASS_NAME, "killThread");

        if (this.thread == null)
        {
            Log.e(CLASS_NAME, "killThread thread null");
            return;
        }

        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        this.thread.setRunning(false);
        while (retry)
        {
            try
            {
                Log.d(CLASS_NAME, "killThread t1 " + this.thread + ", ct " + Thread.currentThread() + ", this " + this);
                this.thread.join();
                retry = false;
            } catch (InterruptedException e)
            {
                Log.e(CLASS_NAME, "killThread exc " + e.getMessage());
            }
        }

        this.thread = null;
        Log.d(CLASS_NAME, "killThread t " + ((this.thread == null) ? "NULL" : this.thread.toString()));
    }

    /**
     *
     */
    private void drawRestartText(Canvas canvas, Rect r)
    {
//                Log.d(CLASS_NAME, "drawRestartText");

        if (this.explosionSprite.isAnimSequenceDone())
        {
            float textSize = this.paint02.getTextSize();
            this.paint02.setTextSize(40 * ((Math.min(this.screenHeight, this.screenWidth) > 720) ? 2 : 1));
            this.crashedProgress = 2;

            String text = "<--  Restart          Quit  --> ";
            canvas.getClipBounds(r);
            int cHeight = r.height();
            int cWidth = r.width();
            this.paint02.setTextAlign(Paint.Align.LEFT);
            this.paint02.getTextBounds(text, 0, text.length(), r);
            float x = cWidth / 2f - r.width() / 2f - r.left;
            float y = cHeight / 2f + r.height() / 2f - r.bottom;
            canvas.drawText(text, x, y, this.paint02);

            this.paint02.setTextSize(textSize);
        }
    }

    /**
     *
     */
    private void doDraw(Canvas canvas)
    {
        if (canvas == null)
        {
            Log.d(CLASS_NAME, "doDraw canvas null");

            return;
        }

        if (this.screenWidth == -1)
        {
            Log.d(CLASS_NAME, "doDraw screenWidth -1");

            return;
        }

        int w = 2010;
        int h = 1000;
        int tjo = (int) ((this.ticks * 10) % (w * this.screenDensity));
        Rect src2 = new Rect(tjo, 0, (int) (tjo + w * this.screenDensity), (int) (h * this.screenDensity));
        Rect dst2 = new Rect(0, 0, this.screenWidth, this.screenHeight);
        canvas.drawBitmap(this.skyBitmap, src2, dst2, null);

        Rect src = new Rect();
        Rect dst;

        if (this.currentHostileSprites.size() > 0)
        {
            for (AnimatedSprite animSprite : this.currentHostileSprites)
            {
                animSprite.draw(canvas);
            }
        }

        if (this.crashed)
        {
            this.explosionSprite.draw(canvas);
            this.drawRestartText(canvas, src);
        } else
        {
            this.mainBirdSprite.draw(canvas);
        }

        // Roof
        canvas.drawRect(0, 0, this.screenWidth, 70, this.paint03);

        // Floor
        dst = new Rect(0, this.screenHeight - 100, this.screenWidth, this.screenHeight);
        if (this.wavesAnimIntervalCnt > (WAVES_ANIM_INTERVAL_MAX / 2))
        {
            src = new Rect(30, 0, this.wavesBitmap.getWidth(), this.wavesBitmap.getHeight());
        } else
        {
            src = new Rect(0, 0, this.wavesBitmap.getWidth() - 30, this.wavesBitmap.getHeight());
        }
        canvas.drawBitmap(this.wavesBitmap, src, dst, null);

        Calendar calendar = Calendar.getInstance();
        long secs = calendar.get(Calendar.SECOND);

        canvas.drawText(("Score " + this.score + ", sec " + secs + ", w " + this.screenWidth + ", h " + this.screenHeight + ", d " + this.getResources().getDisplayMetrics().density + ", high " + this.highScore), 10, 40, this.paint02);

        if (this.hostileHitSprite != null)
        {
            this.hostileHitSprite.draw(canvas);
//            Log.d(CLASS_NAME, "doDraw iasd " + this.hostileHitSprite.isAnimSequenceDone());

            if (this.hostileHitSprite.isAnimSequenceDone())
            {
                this.hostileHitSprite = null;
            }
        }

        for (AnimatedSprite fireSpr : this.currentFireSprites)
        {
            fireSpr.draw(canvas);
        }

//        Var på skärmen det hamnar. Man kan förstora och skala
        dst = new Rect(1600, 350,
                (int) (this.largementFactor * 160 * this.screenDensity) + 1700,
                (int) (this.largementFactor * 160 * this.screenDensity) + 350);
        // Var i bitmapen man läser
        src = new Rect(0, 0, (int) (160 * this.screenDensity), (int) (160 * this.screenDensity));
        canvas.drawBitmap(this.arrowUpBitmap, src, dst, null);

        dst = new Rect(1600, 700,
                (int) (this.largementFactor * 160 * this.screenDensity) + 1700,
                (int) (this.largementFactor * 160 * this.screenDensity) + 700);
        src = new Rect(0, 0, (int) (160 * this.screenDensity), (int) (160 * this.screenDensity));
        canvas.drawBitmap(this.arrowDownBitmap, src, dst, null);
    }

    /**
     *
     */
    private void addHostileSprite()
    {
        float random = (float) Math.random();
        int hostileSpriteY = (int) (random * (this.screenHeight - 400) + 100);

        int random2 = ((int) (random * 100)) % this.hostileSprites.length;
        AnimatedSprite animSprite = this.hostileSprites[random2].clone();

        int hostileSpriteX = this.screenWidth;

        if (random2 == 5)
        {
            Log.d(CLASS_NAME, "addHostileSprite r2 " + random2);
            animSprite.moveStep = 6;
            animSprite.isBonus = true;
        } else
        {
            animSprite.moveStep = (int) (2 * 2 * random2 + 6);
            animSprite.isBonus = false;
        }
        animSprite.posX = hostileSpriteX;
        animSprite.posY = hostileSpriteY;
        animSprite.isToLeft = true;

        Log.d(CLASS_NAME, "addHostileSprite r3 ib " + animSprite.isBonus + ", bm " + animSprite.bitmap);

        this.currentHostileSprites.add(animSprite);
    }

    /**
     *
     */
    private void processIntersections()
    {
        for (AnimatedSprite animSprite : this.currentHostileSprites)
        {
            boolean intersects = animSprite.intersectsWith(this.mainBirdSprite);
            if (intersects)
            {
                if (animSprite.isBonus)
                {
                    this.noCrash = true;
                    this.noCrashCounter = 0;
                    this.mainBirdSprite.setBitmap(this.mainBirdBitmaps[1]);
                    this.currentHostileSprites.remove(animSprite);
                } else
                {
                    this.mainBirdSprite.speed = 0;
                    this.crashedHostile = true;
                }

                Log.d(CLASS_NAME, "processIntersections isb " + animSprite.isBonus + ", bm " + animSprite.bitmap);

                break;
            }
        }
    }

    /**
     *
     */
    private void processHostileSpritesOutside(AnimatedSprite animSprite)
    {
        float random = (float) Math.random();
        int hostileSpriteY = (int) (random * (this.screenHeight - 400) + 100);
        int hostileSpriteX = this.screenWidth;

        int random2 = ((int) (random * 100)) % this.hostileSprites.length;

        this.hostileSprites[random2].copy(animSprite);

        animSprite.moveStep = ((random2 == 5) ? 6 : (2 * 2 * random2 + 6));
        animSprite.posX = hostileSpriteX;
        animSprite.posY = hostileSpriteY;
        animSprite.isToLeft = (hostileSpriteX > 2);
        animSprite.isToLeft = true;
        animSprite.isBonus = ((random2 == 5) ? true : false);

//                Log.d(CLASS_NAME, "updatePos r2 " + random + ", r22 " +  + random2 + ", ms " + animSprite.moveStep + ", bm " + this.hostileSprites[(int) random].bitmap.toString());
//        Log.d(CLASS_NAME, "updatePos r2 " + random + ", r22 " +  + random2 + ", ms " + animSprite.moveStep + ", isb " + animSprite.isBonus);
    }

    /**
     *
     */
    private synchronized void processFireSprites(long tickCounter)
    {
        ArrayList<AnimatedSprite> fireSpritesToRemove = new ArrayList<AnimatedSprite>();

        for (AnimatedSprite fireSpr : this.currentFireSprites)
        {
            boolean isOutside = fireSpr.onUpdate(tickCounter);
            if (isOutside)
            {
                fireSpritesToRemove.add(fireSpr);
            }
        }

        for (AnimatedSprite fireSpr : fireSpritesToRemove)
        {
            this.currentFireSprites.remove(fireSpr);
        }

        fireSpritesToRemove.clear();
        for (AnimatedSprite fireSpr : this.currentFireSprites)
        {
            for (AnimatedSprite animSprite : this.currentHostileSprites)
            {
                boolean intersects = animSprite.intersectsWith(fireSpr);
                if (intersects)
                {
//                    this.score += 100*animSprite.moveStep;
                    this.score += 19 * animSprite.moveStep;
                    if (this.score > this.highScore)
                    {
                        this.highScore = this.score;
                    }

                    this.currentHostileSprites.remove(animSprite);
                    fireSpritesToRemove.add(fireSpr);

                    if (animSprite.isBonus)
                    {
                        this.noCrash = true;
                        this.noCrashCounter = 0;
                        this.mainBirdSprite.setBitmap(this.mainBirdBitmaps[1]);
                    }
//                    Log.d(CLASS_NAME, "processFireSprites fireSprite ms " + animSprite.moveStep);

                    int max = 2 * (this.hostileSprites.length - 1) * ((Math.min(this.screenHeight, this.screenWidth) > 720) ? 2 : 1) + 6;
                    int div = max / this.numberBitmaps.length;
                    int numbersIdx = ((animSprite.moveStep - 6) / div);

                    int num = 19 * animSprite.moveStep;
//                    Log.d(CLASS_NAME, "processFireSprites num " + num);
                    Bitmap bitmap = null;
                    bitmap = this.pointsHandler.createNumberBitmap(num);
//                    bitmap = this.createNumberBitmap((numbersIdx + 1)*100);

                    this.hostileHitSprite = new AnimatedSprite(new int[]{0, 0}, 240, 120, 0, (largementFactor / 3), this.screenDensity);
                    this.hostileHitSprite.posX = animSprite.posX;
                    this.hostileHitSprite.posY = animSprite.posY;
                    this.hostileHitSprite.ticksToAnim = 10;
                    this.hostileHitSprite.moveStep = -5;
                    this.hostileHitSprite.setBitmap(bitmap);
                    this.hostileHitSprite.setLoopAnim(false);
                    this.hostileHitSprite.isToLeft = false;
                    this.hostileHitSprite.screenRight = this.screenWidth;
                    this.hostileHitSprite.screenLeft = 0;
                    this.hostileHitSprite.screenTop = 70;
                    this.hostileHitSprite.screenBottom = this.screenHeight - 130;

                    break;
                }
            }
        }

        for (AnimatedSprite fireSpr : fireSpritesToRemove)
        {
            this.currentFireSprites.remove(fireSpr);
        }
    }

    /**
     *
     */
    public boolean onUpdate(long tickCounter)
    {
//        Log.d(CLASS_NAME, "onUpdate tickCounter " + tickCounter + ", ta " + this.touchAction + ", tdt " + this.touchDownTime);

        if (this.screenWidth == -1)
        {
            Log.d(CLASS_NAME, "onUpdate screenWidth -1");

            return true;
        }

        if (this.crashed)
        {
            this.explosionSprite.onUpdate(tickCounter);

            if (this.crashedProgress == 2)
            {
                return false;
            }

            return true;
        }

        this.crashedTop = false;
        this.crashedBottom = false;
        this.crashedHostile = false;

//    Log.d(CLASS_NAME, "onUpdate mbx " + this.mainBirdX + ", mbxd " + this.mainBirdXDir);

        if (((this.ticks + 1) % 50) == 0)
        {
            this.addHostileSprite();
        }

        this.mainBirdSprite.onUpdate(tickCounter, this.touchActionMainBird);
        this.crashedTop = this.mainBirdSprite.crashedTop;
        this.crashedBottom = this.mainBirdSprite.crashedBottom;

        if (!this.crashedTop && !this.crashedBottom)
        {
            this.processIntersections();
            //                Log.d(CLASS_NAME, "onUpdate i " + intersects);
        }

        for (AnimatedSprite animSprite : this.currentHostileSprites)
        {
//                Log.d(CLASS_NAME, "updatePos r5 " + animSprite.posY);
            boolean isOutside = animSprite.onUpdate(tickCounter);
            if (isOutside)
            {
                this.processHostileSpritesOutside(animSprite);
            }
        }
//            Log.d(CLASS_NAME, "updatePos r4 " + this.currentHostileSpr§ites.get(0).posY);

        this.processFireSprites(tickCounter);

        this.wavesAnimIntervalCnt = (((this.wavesAnimIntervalCnt + 1) > WAVES_ANIM_INTERVAL_MAX) ? 0 : (this.wavesAnimIntervalCnt + 1));

        if (this.crashedTop || this.crashedBottom || this.crashedHostile)
        {
            if (this.noCrash)
            {
                this.ticks++;
            } else
            {
                this.ticks = (((this.ticks - 1) < 0) ? 0 : (this.ticks - 1));
                this.crashed = true;
                this.crashedProgress = 1;
            }
        } else
        {
            this.ticks++;
        }

        if (this.noCrashCounter < 200)
        {
            this.noCrashCounter++;
        } else
        {
            if (this.noCrash)
            {
                this.mainBirdSprite.setBitmap(this.mainBirdBitmaps[0]);
            }
//            Log.d(CLASS_NAME, "updatePos r6");

            this.noCrash = false;
        }

        if (this.crashed)
        {
            this.explosionSprite.posX = (int) (this.mainBirdSprite.posX - (this.mainBirdSprite.width / 4.0f));
            this.explosionSprite.posX = (int) (this.mainBirdSprite.posX);
            this.explosionSprite.posY = this.mainBirdSprite.posY;

            this.setHighScore(this.highScore);
        }

        if (this.hostileHitSprite != null)
        {
            this.hostileHitSprite.onUpdate(tickCounter);
        }

        return true;
    }

    /**
     *
     */
    public void onRender(long tickCounter)
    {
//        Log.d(CLASS_NAME, "onRender tickCounter " + tickCounter);

        Canvas c = null;
        try
        {
            c = this.surfaceHolder.lockCanvas(null);
            synchronized (this.surfaceHolder)
            {
                this.doDraw(c);
            }
        } finally
        {
            if (c != null)
            {
                this.surfaceHolder.unlockCanvasAndPost(c);
            }
        }
    }

    /**
     *
     */
    public void onTick(long tickCounter)
    {
//        Log.d(CLASS_NAME, "onTick tickCounter " + tickCounter);
    }

    /**
     *
     */
    private float getFact(float density)
    {
        float fact = -1;
        if (density == 2.625f)
        {
            fact = 0.75f;
        } else if (density == 3)
        {
            fact = 0.60f;
        } else if (density == 1)
        {
            fact = 1.0f;
        } else
        {
            fact = 1.0f;
        }

        return fact;
    }

    /**
     *
     */
    public void setSharedPrefs(SharedPreferences sharedPrefs)
    {
        Log.d(CLASS_NAME, "setSharedPrefs sharedPrefs " + sharedPrefs + " ct " + Thread.currentThread() + ", this " + this);

        this.sharedPrefs = sharedPrefs;
        this.highScore = this.sharedPrefs.getInt("GRAVITY_HIGH_SCORE", -1);

        Log.d(CLASS_NAME, "setSharedPrefs hs " + this.highScore);
    }

    /**
     *
     */
    private void setHighScore(int highScore)
    {
        Log.d(CLASS_NAME, "setHighScore highScore " + highScore);

        if (highScore >= this.highScore)
        {
            Log.d(CLASS_NAME, "setHighScore highScore2 " + highScore);

            this.highScore = highScore;
            SharedPreferences.Editor editor = this.sharedPrefs.edit();
            editor.putInt("GRAVITY_HIGH_SCORE", highScore);
            editor.commit();
        }
    }

    /**
     *
     */
    public void onPause()
    {
        Log.d(CLASS_NAME, "onPause ct " + Thread.currentThread() + ", this " + this);
        this.killThread();
    }
}
