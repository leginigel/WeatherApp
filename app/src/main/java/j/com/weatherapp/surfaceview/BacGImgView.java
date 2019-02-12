package j.com.weatherapp.surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import j.com.weatherapp.R;

import static j.com.weatherapp.MainActivity.FIRST_OPEN;

public class BacGImgView extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder mSurfaceHolder;

    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;
    private Bitmap bitmap;
    private RectF targetRect;

    private int x = 0, y = 0;
    private boolean isBlack = false;
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
//        CenterCrop();
        Log.d(this.getClass().getSimpleName(),"onDraw" + bitmap.getWidth());
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bitmap, null, targetRect, mPaint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Move the Main onResume Disable First Open to After Fragment
        if(FIRST_OPEN){
            Log.d(this.getClass().getSimpleName(),"Disable First Run");
            FIRST_OPEN = false;
        }
        Log.d(this.getClass().getSimpleName(),"surfaceCreated" + this.getWidth());
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
//            synchronized (mSurfaceHolder){
//                draw(mCanvas);
//            }
//            draw(mCanvas);
            CenterCrop();
            Log.d(this.getClass().getSimpleName(),"onDraw" + this.getWidth());
            mCanvas.drawColor(Color.BLACK);
            mCanvas.drawBitmap(bitmap, null, targetRect, mPaint);
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

    public void switchBlackScreen(boolean isChanging){
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            if (isChanging) {
                Paint p = new Paint();
                ColorFilter filter = new LightingColorFilter(Color.argb(150,80, 80, 80), 0);
//                p.setColorFilter(filter);
//                p.setAlpha(100);
                mCanvas.drawBitmap(bitmap, null, targetRect, mPaint);
                Shader shader = new LinearGradient(0, 0, 0, getHeight(),
                        Color.argb(255, 1, 50, 128), Color.argb(150,120, 120, 120), Shader.TileMode.CLAMP);
                p.setShader(shader);
                mCanvas.drawRect(0, 0, getWidth(), getHeight(), p);
            } else {
                mCanvas.drawColor(Color.BLACK);
                mCanvas.drawBitmap(bitmap, null, targetRect, mPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null)
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }
}
