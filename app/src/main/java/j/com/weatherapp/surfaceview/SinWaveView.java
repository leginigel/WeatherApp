package j.com.weatherapp.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SinWaveView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mSurfaceHolder;

    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;
    private int x = 0, y = 0;

    public SinWaveView(Context context) {
        this(context, null);
    }

    public SinWaveView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SinWaveView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);

        mPath = new Path();
        mPath.moveTo(0, 100);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        Log.d(this.getClass().getSimpleName(),"" + this.getWidth());
        Log.d(this.getClass().getSimpleName(),"" + this.getHeight());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(this.getClass().getSimpleName(),"surfaceCreated" + this.getWidth());
        Log.d(this.getClass().getSimpleName(),"surfaceCreated" + this.getHeight());
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(this.getClass().getSimpleName(),"surfaceChanged" + this.getWidth());
        Log.d(this.getClass().getSimpleName(),"surfaceChanged" + this.getHeight());

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
        while(true) {
            try {
                mCanvas = mSurfaceHolder.lockCanvas();
                mCanvas.drawColor(Color.RED);
                mCanvas.drawPath(mPath, mPaint);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null)
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }

            x += 1;
            y = (int) (100 * Math.sin(2 * x * Math.PI / 180) + 400);
            mPath.lineTo(x, y);
        }
    }
}
