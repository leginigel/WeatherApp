package j.com.weatherapp.surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import j.com.weatherapp.R;

public class BacGImgView extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder mSurfaceHolder;

    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;
    private Bitmap bitmap;
    private RectF targetRect;

    private int x = 0, y = 0;
    public BacGImgView(Context context) {
        this(context, null);
    }

    public BacGImgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BacGImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    private void CenterCrop(){
        float xScale = (float) this.getWidth() / bitmap.getWidth();
        float yScale = (float) this.getHeight() / bitmap.getHeight();
        float scale = Math.max(xScale, yScale);

        float scaleWidth = scale * bitmap.getWidth();
        float scaleHeight = scale * bitmap.getHeight();
        float left = (this.getWidth() - scaleWidth) / 2;
        float top = (this.getHeight() - scaleHeight) / 2;

        targetRect = new RectF(left, top, left + scaleWidth, top + scaleHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        CenterCrop();
        Log.d(this.getClass().getSimpleName(),"onDraw" + this.getWidth());
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bitmap, null, targetRect, mPaint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(this.getClass().getSimpleName(),"surfaceCreated" + this.getWidth());
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            synchronized (mSurfaceHolder){
                draw(mCanvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null)
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
