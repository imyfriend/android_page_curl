package fi.harism.curl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * EGL View.
 * 
 * @author harism
 */
public class CurlView extends GLSurfaceView {

	// Actual renderer.
	private CurlRenderer mCurlRenderer;
	
	/**
	 * Default constructor.
	 */
	public CurlView(Context ctx) {
		super(ctx);
		init(ctx);
	}
	
	/**
	 * Default constructor.
	 */
	public CurlView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		init(ctx);
	}
	
	/**
	 * Default constructor.
	 */
	public CurlView(Context ctx, AttributeSet attrs, int defStyle) {
		this(ctx, attrs);
	}
	
	/**
	 * Initialize method.
	 */
	private void init(Context ctx) {
		mCurlRenderer = new CurlRenderer();
		setRenderer(mCurlRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		this.setOnTouchListener(new TouchListener());
	}
	
	@Override
	public void onSizeChanged(int w, int h, int ow, int oh) {
		super.onSizeChanged(w, h, ow, oh);
		
		// Create Bitmap for renderer.
		Drawable d = getResources().getDrawable(R.drawable.obama);
		d.setBounds(0, 0, w, h);
		Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		b.eraseColor(0xFFFFFFFF);
		Canvas c = new Canvas(b);
		
		Rect r = c.getClipBounds();
		Paint p = new Paint();
		p.setColor(0x80404040);
		c.drawRect(r, p);
		r.left += 2;
		r.top += 2;
		r.right -= 2;
		r.bottom -= 2;
		d.setBounds(r);		
		d.draw(c);
		
		// Update Bitmap.
		mCurlRenderer.setBitmap(b);
		requestRender();
	}
	
	/**
	 * Touch listener.
	 */
	private class TouchListener implements View.OnTouchListener {
		
		// ACTION_DOWN coordinates.
		private PointF mStartCoords = new PointF();

		@Override
		public boolean onTouch(View v, MotionEvent me) {
			if (me.getAction() == MotionEvent.ACTION_DOWN) {
				mStartCoords.x = me.getX();
				mStartCoords.y = me.getY();
			}
			if (me.getAction() == MotionEvent.ACTION_MOVE) {
				PointF curCoords = new PointF(me.getX(), me.getY());
				mCurlRenderer.curl(mStartCoords, curCoords);
				requestRender();
			}
			return true;
		}
		
	}
	
}
