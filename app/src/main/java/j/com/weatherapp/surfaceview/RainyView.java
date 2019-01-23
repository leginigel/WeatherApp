package j.com.weatherapp.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RainyView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    private Paint mPaintRain;
    private Path mPath;
    private Set<Rain> mRainSet;
    private boolean isRunning = false;
    int x = 0, y = 0, count = 60;

    public RainyView(Context context) {
        this(context, null);
    }

    public RainyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RainyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.addCallback(this);
        mPaint = new Paint();

        mPaintRain = new Paint();
        mPaintRain.setColor(Color.WHITE);
        mPaintRain.setStrokeWidth(3);
        mPaintRain.setAlpha(100);
        mPaintRain.setAntiAlias(true);

        mRainSet = new HashSet<>();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(this.getClass().getSimpleName(),"surfaceCreated" + this.getWidth());
        Log.d(this.getClass().getSimpleName(),"surfaceCreated" + this.getHeight());
        mRainSet.clear();
        for (int i = 0;i < count;i++) {
            Rain rain = new Rain(
                    1 + new Random().nextInt(getWidth()-1),
                    1 + new Random().nextInt(getHeight()-1),
                    20 + new Random().nextInt(10)
            );
            mRainSet.add(rain);
        }
        isRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(this.getClass().getSimpleName(),"surfaceChanged" + this.getWidth());
        Log.d(this.getClass().getSimpleName(),"surfaceChanged" + this.getHeight());

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;

    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        while(isRunning) {
            try {
                mCanvas = mSurfaceHolder.lockCanvas();
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                mCanvas.drawPath(mPath, mPaint);
                mCanvas.drawCircle(x, y, 20, mPaint);
//                mCanvas.save();
//                mCanvas.translate(100, 100);
//                mCanvas.restore();
                drawRainShield();
                drawRain();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null)
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
//                SystemClock.sleep(10);
            }
//            mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
//            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void drawRainShield(){
        Shader shader = new LinearGradient(0, 0, 0, getHeight(),
                Color.argb(255, 1, 50, 128), Color.argb(150,98, 120, 132), Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        mCanvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }

    public void drawRain(){
        int gSpeed = 20;
        for (Rain r : mRainSet){
            if ((r.y + r.length - gSpeed / 2) > getHeight()) r.reset();
            if (r.length == 0) {
                r.length = gSpeed + new Random().nextInt(gSpeed / 2);
                r.y = 0;
                r.x = 1 + new Random().nextInt(getWidth()-1);
            }
            mCanvas.drawLine( r.x, r.y, r.x, r.y + r.length, mPaintRain);
            r.y += gSpeed;
        }
    }

    private class Rain{
        int x = 0;
        int y = 0;
        int length = 0;

        Rain() {
        }

        Rain(int x, int y, int length) {
            this.x = x;
            this.y = y;
            this.length = length;
        }

        void reset(){
            this.x = 0;
            this.y = 0;
            this.length = 0;
        }
    }
}
