package com.example.mosaicimageview;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/**
 * 自定义的ImageView控制，可对图片进行多点触控缩放和拖动和打码
 * 
 * @author fangpc
 */
@SuppressLint("NewApi")
public class MosaicImageView extends View {

	/**
	 * 初始化状态常量
	 */
	public static final int STATUS_INIT = 1;

	/**
	 * 图片放大状态常量
	 */
	public static final int STATUS_ZOOM_OUT_AND_MOVE = 2;

	/**
	 * 图片缩小状态常量
	 */
	public static final int STATUS_ZOOM_IN_AND_MOVE = 3;

	/**
	 * 图片拖动状态常量
	 */
	public static final int STATUS_MOVE = 4;

	
	/**
	 * 图片局部显示状态
	 */
	public static final int STATUS_PART = 5;
	
	/**
	 * 手指抬起时图片恢复状态
	 */
	public static final int STATUS_ACTION_UP = 6;
	
	/**
	 * 绘制笔触大小
	 */
	public static final int STATUS_DRAW_STOKE = 7;
	
    /** 
     * 放大镜的半径   
	 */
    private static  int RADIUS = 100;  
    /**  
     * 放大倍数
     */
    private static final int FACTOR = 1;  
	/**	
	 * 触摸公差
	 */
	private static final float TOUCH_TOLERANCE = 4;
	/**
	 * 	笔触大小
	 */
	private static  float PAINT_STROKEWIDTH = 30;
	/**
	 *  当前笔触缩放倍数
	 */
	private float strokeMultiples = 1L;
	/**
	 * 用于对图片进行移动和缩放变换的矩阵
	 */
	private Matrix matrix = new Matrix();

	/**
	 * 待展示的Bitmap对象
	 */
	public Bitmap sourceBitmap;

	public Bitmap sourceBitmapCopy;

	/**
	 * 记录当前操作的状态，可选值为STATUS_INIT、STATUS_ZOOM_OUT、STATUS_ZOOM_IN和STATUS_MOVE
	 */
	private int currentStatus;

	/**
	 * ZoomImageView控件的宽度
	 */
	private int width;

	/**
	 * ZoomImageView控件的高度
	 */
	private int height;

	/**
	 * 记录两指同时放在屏幕上时，中心点的横坐标值
	 */
	private float centerPointX;

	/**
	 * 记录两指同时放在屏幕上时，中心点的纵坐标值
	 */
	private float centerPointY;

	/**
	 * 记录当前图片的宽度，图片被缩放时，这个值会一起变动
	 */
	private float currentBitmapWidth;

	/**
	 * 记录当前图片的高度，图片被缩放时，这个值会一起变动
	 */
	private float currentBitmapHeight;

	/**
	 * 记录上次手指移动时的横坐标
	 */
	private float lastXMove = -1;

	/**
	 * 记录上次手指移动时的纵坐标
	 */
	private float lastYMove = -1;

	/**
	 * 记录手指在横坐标方向上的移动距离
	 */
	private float movedDistanceX;

	/**
	 * 记录手指在纵坐标方向上的移动距离
	 */
	private float movedDistanceY;

	/**
	 * 记录图片在矩阵上的横向偏移值
	 */
	private float totalTranslateX;

	/**
	 * 记录图片在矩阵上的纵向偏移值
	 */
	private float totalTranslateY;

	/**
	 * 记录图片在矩阵上的总缩放比例
	 */
	private float totalRatio;

	/**
	 * 记录手指移动的距离所造成的缩放比例
	 */
	private float scaledRatio;

	/**
	 * 记录图片初始化时的缩放比例
	 */
	private float initRatio;

	/**
	 * 记录上次两指之间的距离
	 */
	private double lastFingerDis;
	
    /**
     * 当前单指点击是X轴坐标
     */
    private float mCurrentX;
    /**
     * 当前单指点击是Y轴坐标
     */
    private float mCurrentY; 
    /**
     * 当前局部显示图是否在左边
     */
    private boolean partIsLeft = true;
    
    /** 
     * 前景图绘制板
     */
	private Canvas mCanvas;
	/**	
	 * 绘画笔
	 */
	private Paint mPaint;
	private Path mPath;
	/**	
	 * 移动时点击位置相对bitmap的X轴坐标
	 */
	private float mX;
	/**
	 * 	移动时点击位置相对bitmap的Y轴坐标
	 */
	private float mY;
	public MosaicImageView(Context context){
		this(context, null);
	}
	/**
	 * ZoomImageView构造函数，将当前操作状态设为STATUS_INIT。
	 * 
	 * @param context
	 * @param attrs
	 */
	public MosaicImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		
	}
	public MosaicImageView(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
		init(context);
	}
	
	private void init(Context context) {
		RADIUS = DensityUtil.dip2px(getContext(), 50f);
		PAINT_STROKEWIDTH = DensityUtil.dip2px(getContext(), 12f);
		mPaint = new Paint();
		mPaint.setAlpha(0);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//取两层绘制交集。显示上层。
		mPaint.setAntiAlias(true);
		
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPath = new Path();
	}
	
	public void setSourceBitmap(Bitmap bitmap){
		currentStatus = STATUS_INIT;
		bitmap = zoomImage(bitmap, DensityUtil.getDisplayWidth(getContext()), DensityUtil.getDisplayHeight(getContext()));
		
		sourceBitmap =  Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		sourceBitmapCopy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		mCanvas = new Canvas(sourceBitmap);
		mCanvas.drawBitmap(bitmap, 0, 0, null);

		Canvas canvas = new Canvas(sourceBitmapCopy);
		canvas.drawBitmap(bitmap, 0, 0, null);
		bitmap.recycle();
		invalidate();
	}
	public void revocation(Bitmap bitmap) {
		sourceBitmap.recycle();
		sourceBitmap = null;
		
		bitmap = zoomImage(bitmap, DensityUtil.getDisplayWidth(getContext()), DensityUtil.getDisplayHeight(getContext()));
		sourceBitmap =  Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		mCanvas = new Canvas(sourceBitmap);
		mCanvas.drawBitmap(bitmap, 0, 0, null);
		bitmap.recycle();
		
		currentStatus = STATUS_ACTION_UP;
		invalidate();
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			// 分别获取到ZoomImageView的宽度和高度
			width = getWidth();
			height = getHeight();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_POINTER_DOWN:
			if (event.getPointerCount() == 2) {
				// 当有两个手指按在屏幕上时，计算两指之间的距离
				lastFingerDis = distanceBetweenFingers(event);
				isMultiTouch = true;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			if (event.getPointerCount() == 1) {
				touch_down((event.getX() - totalTranslateX) / totalRatio, (event.getY() - totalTranslateY) / totalRatio);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getPointerCount() == 1 && !isMultiTouch) {
					// 只有单指按在屏幕上移动时，为查看局部状态
					currentStatus = STATUS_PART;
			        mCurrentX = event.getX();  
			        mCurrentY = event.getY();  
					touch_move((mCurrentX - totalTranslateX) / totalRatio, (mCurrentY - totalTranslateY) / totalRatio);
					invalidate();
			} else if (event.getPointerCount() == 2) {
				//拖动
				float xMove = (event.getX(0) + event.getX(1))/2;
				float yMove = (event.getY(0) + event.getY(1))/2;

				if (lastXMove == -1 && lastYMove == -1) {
					centerMovePointBetweenFingers(event);
				}
				movedDistanceX = xMove - lastXMove;
				movedDistanceY = yMove - lastYMove;
				// 进行边界检查，不允许将图片拖出边界
				if (totalTranslateX + movedDistanceX > 0) {
					movedDistanceX = 0;
				} else if (width - (totalTranslateX + movedDistanceX) > currentBitmapWidth) {
					movedDistanceX = 0;
				}
				if (totalTranslateY + movedDistanceY > 0) {
					movedDistanceY = 0;
				} else if (height - (totalTranslateY + movedDistanceY) > currentBitmapHeight) {
					movedDistanceY = 0;
				}
				
				// 缩放
				Boolean isDrag = false;
				centerPointBetweenFingers(event);
				double fingerDis = distanceBetweenFingers(event);
				if (fingerDis > lastFingerDis) {
					currentStatus = STATUS_ZOOM_OUT_AND_MOVE;
				} else {
					currentStatus = STATUS_ZOOM_IN_AND_MOVE;
				}
				// 进行缩放倍数检查，最大只允许将图片放大4倍，最小可以缩小到初始化比例
				if ((currentStatus == STATUS_ZOOM_OUT_AND_MOVE && totalRatio < 4 * initRatio)
						|| (currentStatus == STATUS_ZOOM_IN_AND_MOVE && totalRatio > initRatio)) {
					scaledRatio = (float) (fingerDis / lastFingerDis);
					totalRatio = totalRatio * scaledRatio;
					if (totalRatio > 4 * initRatio) {
						totalRatio = 4 * initRatio;
					} else if (totalRatio < initRatio) {
						totalRatio = initRatio;
					}

					isDrag = true;
				}else{
					currentStatus = STATUS_MOVE;
				}
				
				// 调用onDraw()方法绘制图片
				invalidate();
				if(isDrag){
					lastFingerDis = fingerDis;
				}
				centerMovePointBetweenFingers(event);
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			if (event.getPointerCount() == 2) {
				currentStatus = STATUS_ACTION_UP;
				invalidate();
				// 手指离开屏幕时将临时值还原
				lastXMove = -1;
				lastYMove = -1;
			}
			break;
		case MotionEvent.ACTION_UP:
			isMultiTouch = false;
			// 手指离开屏幕时将临时值还原
			currentStatus = STATUS_ACTION_UP;
			touch_up();
			invalidate();
			lastXMove = -1;
			lastYMove = -1;
			break;
		default:
			break;
		}
		return true;
	}
	private boolean isMultiTouch;
	/**
	 * 根据currentStatus的值来决定对图片进行什么样的绘制操作。
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if(sourceBitmap == null)
			return;
		super.onDraw(canvas);
		switch (currentStatus) {
			case STATUS_PART:
				part(canvas);
				break;
			case STATUS_ZOOM_OUT_AND_MOVE:
			case STATUS_ZOOM_IN_AND_MOVE:
				move(canvas);
				zoom(canvas);
				break;
			case STATUS_MOVE:
				move(canvas);
				break;
			case STATUS_INIT:
				initBitmap(canvas);
				replyPosition(canvas);
				// 更新界面图片
				break;
			case STATUS_ACTION_UP:
				replyPosition(canvas);
				break;
			case STATUS_DRAW_STOKE:
				drawStrokeSize(canvas);
				break;
			default:
				canvas.drawBitmap(sourceBitmap, matrix, null);
				break;
		}
	}

	
	private void touch_down(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		//mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		mPath.reset();
	}
	
	/**
	 * 局部图片窗口的绘制
	 * @param canvas
	 */
	private void part(Canvas canvas) {
		float bitmapTop = totalTranslateY;//bitmap顶部Y轴值
		float bitmapBottom = bitmapTop + sourceBitmap.getHeight() * totalRatio;//bitmap底部Y轴值
		float bitmapLeft = totalTranslateX;//bitmap左部X轴值
		float bitmapRight = bitmapLeft + sourceBitmap.getWidth() * totalRatio;//bitmap右部X轴值

		float circleCenterX = mCurrentX;//指示点圆心点X轴坐标
		float circleCenterY = mCurrentY;//指示点圆心点Y轴坐标
		
		float partCenterX = mCurrentX;//局部图中心点X轴坐标
		float partCenterY = mCurrentY;//局部图中心点Y轴坐标
		
		
		if( mCurrentX < (RADIUS * 2) && mCurrentY < (RADIUS * 2) && partIsLeft){
			partIsLeft = false;
		}else if((mCurrentX > canvas.getWidth() - (RADIUS * 2) && mCurrentY < (RADIUS * 2)) && !partIsLeft){
			partIsLeft = true;
		}

		if((mCurrentY < bitmapTop + RADIUS) || (mCurrentY > bitmapBottom - RADIUS) || (mCurrentX < bitmapLeft + RADIUS) || (mCurrentX > bitmapRight - RADIUS)){
			if((mCurrentY < bitmapTop + RADIUS)){//准备上方越界
				partCenterY = mCurrentY + ( bitmapTop + RADIUS - mCurrentY);
			}
			if((mCurrentY > bitmapBottom - RADIUS)){//准备下方越界
				partCenterY = mCurrentY - ( mCurrentY - bitmapBottom + RADIUS);
			}
			if(mCurrentX < bitmapLeft + RADIUS){//准备左方越界
				partCenterX = mCurrentX + ( bitmapLeft + RADIUS - mCurrentX);
			}
			if(mCurrentX > bitmapRight - RADIUS){//准备右方越界
				partCenterX = mCurrentX - ( mCurrentX - bitmapRight + RADIUS);
			}
		}
		Path path = new Path();
        path.addRect(0, 0, RADIUS * 2, RADIUS * 2, Direction.CW);
        //底图  
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);
		mCanvas.drawPath(mPath, mPaint);

		//剪切  
		if(partIsLeft){
			canvas.translate(0 , 0);  
		}else{
			canvas.translate(canvas.getWidth() - RADIUS * 2 , 0);  
		}
		canvas.clipPath(path);  
		//画局部图
		canvas.translate(RADIUS - partCenterX * FACTOR, RADIUS - partCenterY * FACTOR);  
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);          
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		//绘制指示点中间
		paint.setColor(getResources().getColor(R.color.mosaicdark));  
		paint.setStyle(Style.FILL);//实心图案
		if(initRatio > 1){
			canvas.drawCircle(circleCenterX, circleCenterY, (PAINT_STROKEWIDTH - 5) / 2 * initRatio * strokeMultiples , paint);
		}else{
			canvas.drawCircle(circleCenterX, circleCenterY, (PAINT_STROKEWIDTH - 5) / 2 / initRatio * strokeMultiples , paint);
		}
		//绘制绘制指示点原形边框
		paint.setColor(getResources().getColor(R.color.mosaicblue));  
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(4f);
		if(totalRatio > 1){
			canvas.drawCircle(circleCenterX, circleCenterY, (PAINT_STROKEWIDTH - 4) / 2 * initRatio * strokeMultiples, paint);
		}else{
			canvas.drawCircle(circleCenterX, circleCenterY, (PAINT_STROKEWIDTH - 4) / 2 / initRatio * strokeMultiples, paint);
		}

		//绘制白色边框
		paint.setStyle(Style.STROKE);//空心图案
		paint.setStrokeWidth(2.5f);
		paint.setColor(Color.WHITE);  
		canvas.drawRect(new RectF(partCenterX - RADIUS + 1, partCenterY - RADIUS + 1, partCenterX + RADIUS - 1, partCenterY + RADIUS - 1), paint); 
		
    }
	
	/**
	 * 对图片进行缩放处理。
	 * 
	 * @param canvas
	 */
	private void zoom(Canvas canvas) {
		mPaint.setStrokeWidth(PAINT_STROKEWIDTH / totalRatio * strokeMultiples);
		matrix.reset();
		// 将图片按总缩放比例进行缩放
		matrix.postScale(totalRatio, totalRatio);
		float scaledWidth = sourceBitmap.getWidth() * totalRatio;
		float scaledHeight = sourceBitmap.getHeight() * totalRatio;
		float translateX = 0f;
		float translateY = 0f;
		// 如果当前图片宽度小于屏幕宽度，则按屏幕中心的横坐标进行水平缩放。否则按两指的中心点的横坐标进行水平缩放
		if (currentBitmapWidth < width) {
			translateX = (width - scaledWidth) / 2f;
		} else {
			translateX = totalTranslateX * scaledRatio + centerPointX * (1 - scaledRatio);
			// 进行边界检查，保证图片缩放后在水平方向上不会偏移出屏幕
			if (translateX > 0) {
				translateX = 0;
			} else if (width - translateX > scaledWidth) {
				translateX = width - scaledWidth;
			}
		}
		// 如果当前图片高度小于屏幕高度，则按屏幕中心的纵坐标进行垂直缩放。否则按两指的中心点的纵坐标进行垂直缩放
		if (currentBitmapHeight < height) {
			translateY = (height - scaledHeight) / 2f;
		} else {
			translateY = totalTranslateY * scaledRatio + centerPointY * (1 - scaledRatio);
			// 进行边界检查，保证图片缩放后在垂直方向上不会偏移出屏幕
			if (translateY > 0) {
				translateY = 0;
			} else if (height - translateY > scaledHeight) {
				translateY = height - scaledHeight;
			}
		}
		// 缩放后对图片进行偏移，以保证缩放后中心点位置不变
		matrix.postTranslate(translateX, translateY);
		totalTranslateX = translateX;
		totalTranslateY = translateY;
		currentBitmapWidth = scaledWidth;
		currentBitmapHeight = scaledHeight;
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);
	}

	/**
	 * 对图片进行平移处理
	 * 
	 * @param canvas
	 */
	private void move(Canvas canvas) {
		matrix.reset();
		// 根据手指移动的距离计算出总偏移值
		float translateX = totalTranslateX + movedDistanceX;
		float translateY = totalTranslateY + movedDistanceY;
		// 先按照已有的缩放比例对图片进行缩放
		matrix.postScale(totalRatio, totalRatio);
		// 再根据移动距离进行偏移
		matrix.postTranslate(translateX, translateY);
		totalTranslateX = translateX;
		totalTranslateY = translateY;
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);
	}
	
	/**
	 * 对图片进行初始化操作，包括让图片居中，以及当图片大于屏幕宽高时对图片进行压缩。
	 * 
	 * @param canvas
	 */
	private void initBitmap(Canvas canvas) {
		if (sourceBitmap != null) {
			matrix.reset();
			int bitmapWidth = sourceBitmap.getWidth();
			int bitmapHeight = sourceBitmap.getHeight();
			if (bitmapWidth > width || bitmapHeight > height) {
				if (bitmapWidth - width > bitmapHeight - height) {
					// 当图片宽度大于屏幕宽度时，将图片等比例压缩，使它可以完全显示出来
					float ratio = width / (bitmapWidth * 1.0f);
					matrix.postScale(ratio, ratio);
					float translateY = (height - (bitmapHeight * ratio)) / 2f;
					// 在纵坐标方向上进行偏移，以保证图片居中显示
					matrix.postTranslate(0, translateY);
					totalTranslateY = translateY;
					totalRatio = initRatio = ratio;
				} else {
					// 当图片高度大于控件高度时，将图片等比例压缩，使它可以完全显示出来
					float ratio = height / (bitmapHeight * 1.0f);	
					matrix.postScale(ratio, ratio);
					float translateX = (width - (bitmapWidth * ratio)) / 2f;
					// 在横坐标方向上进行偏移，以保证图片居中显示
					matrix.postTranslate(translateX, 0);
					totalTranslateX = translateX;
					totalRatio = initRatio = ratio;
				}
				currentBitmapWidth = bitmapWidth * initRatio;
				currentBitmapHeight = bitmapHeight * initRatio;
			} else {

				// 当图片的宽高都小于屏幕宽高时，直接让放大至一边贴边为止
				float ratio = 0;
				if((width / (bitmapWidth * 1.0f)) > (height / (bitmapHeight * 1.0f))){
					ratio = height / (bitmapHeight * 1.0f);
				}else{
					ratio = width / (bitmapWidth * 1.0f);
				}
				matrix.postScale(totalRatio, totalRatio);

				float translateY = (height - (bitmapHeight * ratio)) / 2f;
				float translateX = (width - (bitmapWidth * ratio)) / 2f;
				matrix.postTranslate(translateX, translateY);
				totalRatio = initRatio = ratio;
				totalTranslateX = translateX;
				totalTranslateY = translateY;
				currentBitmapWidth = bitmapWidth * initRatio;;
				currentBitmapHeight = bitmapHeight * initRatio;;
			}
			System.out.println("===================" + totalRatio);
			/*color = new int[sourceBitmapCopy.getWidth()][sourceBitmapCopy.getHeight()];
			newColor = new int[sourceBitmapCopy.getWidth()][sourceBitmapCopy.getHeight()];
			for (int y = 0; y < sourceBitmapCopy.getHeight(); y++) {
				for (int x = 0; x < sourceBitmapCopy.getWidth(); x++) { 
					color[x][y] = sourceBitmapCopy.getPixel(x, y);
				}
			}
			newColor(newColor, color);
			for (int x = 0; x < sourceBitmapCopy.getWidth(); x++) { 
				for (int y = 0; y < sourceBitmapCopy.getHeight(); y++) {
					sourceBitmapCopy.setPixel(x, y, newColor[x][y]);
				}
			}*/
			sourceBitmapCopy = getMosaic(sourceBitmapCopy);
			canvas.drawBitmap(sourceBitmapCopy, matrix, null);
			canvas.drawBitmap(sourceBitmap, matrix, null);
			mPaint.setStrokeWidth(PAINT_STROKEWIDTH / totalRatio * strokeMultiples);
		}
	}
	
	/**
	 * 绘制笔触大小
	 * @param strokeMultiples
	 * @param canvas
	 */
	public void drawStrokeSize(Canvas canvas) {
		// TODO Auto-generated method stub
		//底图  
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);
		mCanvas.drawPath(mPath, mPaint);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		//绘制绘制指示点原形边框
		paint.setColor(getResources().getColor(R.color.mosaicblue));  
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(4f);
		if(totalRatio > 1){
			canvas.drawCircle(width / 2, height / 2, (PAINT_STROKEWIDTH - 4) / 2 * initRatio * strokeMultiples, paint);
		} else {
			canvas.drawCircle(width / 2, height / 2, (PAINT_STROKEWIDTH - 4) / 2 / initRatio * strokeMultiples, paint);
		}
	}
	
	
	public void setStrokeMultiples(float strokeMultiples) {
		this.strokeMultiples = strokeMultiples;
		mPaint.setStrokeWidth(PAINT_STROKEWIDTH / totalRatio * strokeMultiples);
		currentStatus = STATUS_DRAW_STOKE;
		invalidate();
	}
	
	public void removeStrokeView() {
		// TODO Auto-generated method stub
		currentStatus = STATUS_ACTION_UP;
		invalidate();
	}

	/**
	 * 对图片进行恢复
	 * 
	 * @param canvas
	 */
	private void replyPosition(Canvas canvas) {
		matrix.reset();
		// 先按照已有的缩放比例对图片进行缩放
		matrix.postScale(totalRatio, totalRatio);
		// 再根据移动距离进行偏移
		matrix.postTranslate(totalTranslateX, totalTranslateY);
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);
	}
	
	/**
	 * 计算两个手指之间的距离。
	 * 
	 * @param event
	 * @return 两个手指之间的距离
	 */
	private double distanceBetweenFingers(MotionEvent event) {
		float disX = Math.abs(event.getX(0) - event.getX(1));
		float disY = Math.abs(event.getY(0) - event.getY(1));
		return Math.sqrt(disX * disX + disY * disY);
	}

	/**
	 * 计算两个手指之间中心点的坐标。
	 * 
	 * @param event
	 */
	private void centerPointBetweenFingers(MotionEvent event) {
		float xPoint0 = event.getX(0);
		float yPoint0 = event.getY(0);
		float xPoint1 = event.getX(1);
		float yPoint1 = event.getY(1);
		centerPointX = (xPoint0 + xPoint1) / 2;
		centerPointY = (yPoint0 + yPoint1) / 2;
	}
	
	/**
	 * 计算移动时两个手指之间中心点的坐标。
	 * 
	 * @param event
	 */
	private void centerMovePointBetweenFingers(MotionEvent event) {
		float xPoint0 = event.getX(0);
		float yPoint0 = event.getY(0);
		float xPoint1 = event.getX(1);
		float yPoint1 = event.getY(1);
		lastXMove = (xPoint0 + xPoint1) / 2;
		lastYMove = (yPoint0 + yPoint1) / 2;
	}

	/** 
	 * 合并两张bitmap为一张 
	 * @param background 
	 * @param foreground 
	 * @return Bitmap 
	 */  
	public Bitmap combineBitmap(Bitmap background, Bitmap foreground) {  
	    if (background == null) {  
	        return null;  
	    }  
	    int bgWidth = background.getWidth();  
	    int bgHeight = background.getHeight();  
	    int fgWidth = foreground.getWidth();  
	    int fgHeight = foreground.getHeight();  
	    Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);  
	    Canvas canvas = new Canvas(newmap);  
	    canvas.drawBitmap(background, 0, 0, null);  
	    canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2,  
	            (bgHeight - fgHeight) / 2, null);  
	    canvas.save(Canvas.ALL_SAVE_FLAG);  
	    canvas.restore();  
	    return newmap;  
	}  
	
	private Bitmap getimage(int aaa, int newWidth, int newHeight) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = false;
		// 此时返回bm为空
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), aaa);
		System.out.println("Bitmap:" + bitmap);
		bitmap = zoomImage(bitmap, newWidth, newHeight);
		return bitmap;// 压缩好比例大小后再进行质量压缩
	}
	/**
	 * 马赛克效果(Native)
	 * 
	 * @param bitmap
	 *            原图
	 * 
	 * @return 马赛克图片
	 * 
	 */
	public static Bitmap getMosaic(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int radius = width / 30;

		Bitmap mosaicBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(mosaicBitmap);

		int horCount = (int) Math.ceil(width / (float) radius);
		int verCount = (int) Math.ceil(height / (float) radius);

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		for (int horIndex = 0; horIndex < horCount; ++horIndex) {
			for (int verIndex = 0; verIndex < verCount; ++verIndex) {
				int l = radius * horIndex;
				int t = radius * verIndex;
				int r = l + radius;
				if (r > width) {
					r = width;
				}
				int b = t + radius;
				if (b > height) {
					b = height;
				}
				int color = bitmap.getPixel(l, t);
				Rect rect = new Rect(l, t, r, b);
				paint.setColor(color);
				canvas.drawRect(rect, paint);
			}
		}
		canvas.save();

		return mosaicBitmap;
	}
	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
		// 获取这个图片的宽和高
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		// 执行该缩放方法的条件有，有一边小月屏幕的大小或者图片的高度大于长度
		double x = width * newHeight;
		double y = height * newWidth;

		if (x > y) {
			newHeight = (int) (y / (double) width);
		} else if (x < y) {
			newWidth = (int) (x / (double) height);
		}

		if (newWidth > width && newHeight > height) {
			newWidth = width;
			newHeight = height;
		}
		Matrix matrix = new Matrix();
		matrix.reset();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / (float) width;
		float scaleHeight = ((float) newHeight) / (float) height;
		matrix.postScale(scaleWidth, scaleHeight);

		bgimage = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height,
				matrix, true);
		return bgimage;
	}

}