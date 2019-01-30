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
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import j.com.weatherapp.R;

public class HaloView extends SurfaceView implements SurfaceHolder.Callback, Runnable  {

    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    private Paint mPaintFill;
    private Path mPath;
    private boolean isRunning = false;
    private int x = 0, y = 0, i = 0, j = 0;

    public HaloView(Context context) {
        this(context, null);
    }

    public HaloView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public HaloView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaintFill = new Paint();
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setAntiAlias(true);

        mPath = new Path();
        mPath.moveTo(0, 100);
//        this.setBackgroundResource(R.drawable.background);
//        this.setZOrderOnTop(true);
//        this.setZOrderMediaOverlay(true);
//        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);//使窗口支持透明度
//        mSurfaceHolder.setFormat(PixelFormat.RGBA_8888);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.addCallback(this);
        Log.d(this.getClass().getSimpleName(),"" + this.getWidth());
        Log.d(this.getClass().getSimpleName(),"" + this.getHeight());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(this.getClass().getSimpleName(),"surfaceCreated" + this.getWidth());
        Log.d(this.getClass().getSimpleName(),"surfaceCreated" + this.getHeight());
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
        Log.d(this.getClass().getSimpleName(),"surfaceDestroyed" + this.getWidth());
        Log.d(this.getClass().getSimpleName(),"surfaceDestroyed" + this.getHeight());
        isRunning = false;
        Thread.interrupted();
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
//                Shader shader = new LinearGradient(0, 0, 0, getHeight(),
//                        Color.argb(255, 0, 0, 102), Color.argb(150,50, 50, 50), Shader.TileMode.CLAMP);
//                mPaint.setShader(shader);
//                mCanvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
//                mCanvas.save();
//                mCanvas.translate(100, 100);
//                mCanvas.restore();
                drawUpperHalo();
                drawLowerHalo();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null)
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                SystemClock.sleep(10);
            }

            if (i == 80){
                i = 0;
                j = 0;
            }
            else if (i >= 60){
                j++;
            }
            i++;
            x += 1;
            y = (int) (100 * Math.sin(2 * x * Math.PI / 180) + 400);
            mPath.lineTo(x, y);
//            mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
//            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void drawLowerHalo(){
        int k;
        k=i;
        if (k>20) k =20;
        mPaintFill.setColor(Color.YELLOW);
        mPaintFill.setAlpha(k-j);
        mCanvas.drawCircle(800-i, 800-i, 50, mPaintFill);
        mPaintFill.setColor(Color.YELLOW);
        mPaintFill.setAlpha(k-j);
        mCanvas.drawCircle(840-i, 830-i, 100, mPaintFill);
        mPaintFill.setColor(Color.GREEN);
        mPaintFill.setAlpha(30-j);
        mCanvas.drawCircle(850-i, 800-i, 20, mPaintFill);
        mPaintFill.setColor(Color.GREEN);
        mPaintFill.setAlpha(20-j);
        mCanvas.drawCircle(800-i, 850-i, 30, mPaintFill);
        mPaintFill.setColor(Color.CYAN);
        mPaintFill.setAlpha(30-j);
        mCanvas.drawCircle(900-i, 900-i, 100, mPaintFill);

        mPaintFill.setColor(Color.YELLOW);
        if (i < 20)
            mPaintFill.setAlpha(20 - i);
        else
            mPaintFill.setAlpha(j);
        mCanvas.drawCircle(850, 850, 70, mPaintFill);
        mPaintFill.setColor(Color.CYAN);
        mPaintFill.setAlpha(10+j);
        mCanvas.drawCircle(900, 900, 100, mPaintFill);
    }

    private void drawUpperHalo(){
        mPaintFill.setColor(Color.CYAN);
        if (i <=20)
            mPaintFill.setAlpha(i);
        else
            mPaintFill.setAlpha(20-j);
        mCanvas.drawCircle(100-i, 100-i, 50, mPaintFill);
        mPaintFill.setColor(Color.YELLOW);
        if (i <=20)
            mPaintFill.setAlpha(i);
        else
            mPaintFill.setAlpha(20-j);
        mCanvas.drawCircle(200-2 * i, 200-2 * i, 50, mPaintFill);
        mPaintFill.setColor(Color.RED);
        if (i <=10)
            mPaintFill.setAlpha(i);
        else
            mPaintFill.setAlpha(10 - Math.min(j,10));
        mCanvas.drawCircle(250-2 * i, 250-2 * i, 50, mPaintFill);
    }
}
