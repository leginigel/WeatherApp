package j.com.weatherapp.surfaceview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.IntRange;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class CloudView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Paint mPaint, mPaintCloud;
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private List<Cloud> mCloudList;
    private boolean isRunning = false;
    public CloudView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaintCloud = new Paint();

        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.addCallback(this);

        mCloudList = new ArrayList<>();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCloud();
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

    RainyView.DrawShield sunset = () -> drawSunSetShield();

    public void drawSunSetShield(){
        ColorFilter filter = new LightingColorFilter(Color.argb(50,255, 117, 26), Color.WHITE);
        mPaint.setColorFilter(filter);
        mPaint.setColor(Color.TRANSPARENT);
//        Shader shader = new LinearGradient(0, 0, 0, getHeight(),
//                new int[]{Color.argb(255, 230, 46, 0), Color.argb(50,255, 255, 0),
//                        Color.argb(50,255, 117, 26), Color.argb(150,255, 117, 26)},
//                new float[]{0f, 0.1f, .5f, 1},
//                Shader.TileMode.CLAMP);
//        mPaint.setShader(shader);
        mCanvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }

    private void drawCloud(){
//        for (Cloud c : mCloudList){
//            mPaintCloud.setColor(Color.BLACK);
//            mCanvas.drawPoint(c.x, c.whiteLine, mPaintCloud);
//            mCanvas.drawLine(c.x, c.whiteStart, c.x, c.whiteEnd, mPaintCloud);
//            mPaintCloud.setColor(Color.GRAY);
//            mCanvas.drawPoint(c.x, c.grayLine, mPaintCloud);
//            mCanvas.drawLine(c.x, c.grayStart, c.x, c.grayEnd, mPaintCloud);
//        }

        mPaintCloud.setStyle(Paint.Style.FILL);
        mPaintCloud.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL));
        for (int i=0;i<30;i++) {
            Cloud c = mCloudList.get(i);
            mPaintCloud.setColor(c.color);
            mPaintCloud.setAlpha(c.alpha);
            mCanvas.save();
            mCanvas.translate(c.x, c.y);
//        mPaintCloud.setStrokeWidth(10);
//        mPaintCloud.setStrokeCap(Paint.Cap.BUTT);
//        mCanvas.drawPoint(0,0,mPaintCloud);
            mCanvas.rotate(c.angle, 100, 100);
            mCanvas.drawOval(new RectF(0, 0, c.w, c.h), mPaintCloud);
            mCanvas.restore();
            c.x ++;
            c.w += new Random().nextInt(5) - 2;
            c.h += new Random().nextInt(5) - 2;
//            c.angle += new Random().nextInt(9) - 5;
            c.alpha += new Random().nextInt(5) - 2;
        }
    }

    private void initCloud() {
        for (int i=0;i<30;i++){
//            Cloud cloud = new Cloud(
//                    700 - new Random().nextInt(100),
//                     700 + new Random().nextInt(100),
//                     700,
//                    800 - new Random().nextInt(100),
//                    800 + new Random().nextInt(100),
//                    800,
//                    i, 0
//            );
            Cloud c = new Cloud(
                    1 + new Random().nextInt(getWidth()-1),
                    500 + new Random().nextInt(500),100,100,
                    new Random().nextInt(2) - 1,
                    new Random().nextInt(360),
                    100 + new Random().nextInt(125)
            );
            mCloudList.add(c);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                mCanvas = mSurfaceHolder.lockCanvas();
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                sunset.onDrawShield();
                drawCloud();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null)
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private class Cloud{
        int whiteStart, whiteEnd, whiteLine, grayStart, grayEnd, grayLine;
        int x, y, w, h, color, angle, alpha;

        public Cloud(
                @IntRange(from = 0, to = 320) int x,
                @IntRange(from = 500, to = 900) int y,
                @IntRange(from = 100, to = 200) int w,
                @IntRange(from = 100, to = 200) int h,
                @IntRange(from = 0, to = 1) int color,
                @IntRange(from = 0, to = 360) int angle,
                @IntRange(from = 0, to = 255) int alpha) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.color = color == 0 ? Color.WHITE : Color.GRAY;
            this.angle = angle;
            this.alpha = alpha;
        }
    }
}
