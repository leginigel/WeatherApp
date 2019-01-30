package j.com.weatherapp.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SnowfallView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    private Paint mPaintSnow;
    private Set<Snow> mSnowSet;
    private boolean isRunning = false;
    int x = 0, y = 0, count = 20;
    int alphaMin = 100, alphaMax = 100, speedMin = 20 ,speedMax = 40, sizeMin = 10, sizeMax = 10;

    public SnowfallView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaintSnow = new Paint();
        mPaintSnow.setColor(Color.WHITE);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.addCallback(this);

        mSnowSet = new HashSet<>();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initSnow();
        isRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
        Thread.interrupted();
    }

    @Override
    public void run() {

        while (isRunning) {
            try {
                mCanvas = mSurfaceHolder.lockCanvas();
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                snowShield.onDrawShield();

                drawSnow();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null)
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    RainyView.DrawShield snowShield = () -> drawSnowShield();

    private void drawSnow(){
        for (Snow s : mSnowSet) {
            if (s.y > (getHeight()-10) || s.x > getWidth()
                    || s.x < 0) {
                s.reset();
//                Log.d(this.getClass().getSimpleName(), "Reset");
            }
//            if (s.y == 0)
//                s.x = 1 + new Random().nextInt(getWidth()-1);
            mPaintSnow.setAlpha(s.alpha);
            mCanvas.drawCircle(s.x, s.y, s.size, mPaintSnow);
//            s.x += s.speed * Math.sin(60);
            s.y += s.speed * Math.cos(30);
        }
    }

    private void drawSnowShield(){
        Shader shader = new LinearGradient(0, 0, 0, getHeight(),
                Color.argb(255, 70, 100, 128), Color.argb(150,120, 120, 120), Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        mCanvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }

    private void initSnow(){
        mSnowSet.clear();
        for (int i=0;i<count;i++){
            Snow snow = new Snow(
                    1 + new Random().nextInt(getWidth()-1),
                    1 + new Random().nextInt(getHeight()-1),
                    sizeMin + new Random().nextInt(sizeMax),
                    speedMin + new Random().nextInt(speedMax),
                    alphaMin + new Random().nextInt(alphaMax));
            mSnowSet.add(snow);
        }
    }

    private class Snow{
        int x, y, size, speed, alpha;

        Snow(int x, int y, int size, int speed, int alpha) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speed = speed;
            this.alpha = alpha;
        }

        void reset(){
            this.x = 1 + new Random().nextInt(getWidth()-1);
            this.y = 0;
            this.size = sizeMin + new Random().nextInt(sizeMax);
            this.speed = speedMin + new Random().nextInt(speedMax);
            this.alpha = alphaMin + new Random().nextInt(alphaMax);
        }
    }
}
