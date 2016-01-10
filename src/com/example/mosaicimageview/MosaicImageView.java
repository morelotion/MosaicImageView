package com.example.mosaicimageview;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/**
 * �Զ����ImageView���ƣ��ɶ�ͼƬ���ж�㴥�����ź��϶��ʹ���
 * 
 * @author fangpc
 */
@SuppressLint("NewApi")
public class MosaicImageView extends View {

	/**
	 * ��ʼ��״̬����
	 */
	public static final int STATUS_INIT = 1;

	/**
	 * ͼƬ�Ŵ�״̬����
	 */
	public static final int STATUS_ZOOM_OUT_AND_MOVE = 2;

	/**
	 * ͼƬ��С״̬����
	 */
	public static final int STATUS_ZOOM_IN_AND_MOVE = 3;

	/**
	 * ͼƬ�϶�״̬����
	 */
	public static final int STATUS_MOVE = 4;

	
	/**
	 * ͼƬ�ֲ���ʾ״̬
	 */
	public static final int STATUS_PART = 5;
	
	/**
	 * ��ָ̧��ʱͼƬ�ָ�״̬
	 */
	public static final int STATUS_ACTION_UP = 6;
	
	/**
	 * ���Ʊʴ���С
	 */
	public static final int STATUS_DRAW_STOKE = 7;
	
    /** 
     * �Ŵ󾵵İ뾶   
	 */
    private static final int RADIUS = 80;  
    /**  
     * �Ŵ���
     */
    private static final int FACTOR = 1;  
	/**	
	 * ��������
	 */
	private static final float TOUCH_TOLERANCE = 4;
	/**
	 * 	�ʴ���С
	 */
	private static final float PAINT_STROKEWIDTH = 20;
	/**
	 *  ��ǰ�ʴ����ű���
	 */
	private float strokeMultiples = 1L;
	/** 
	 * �ָ�Ŀ�Ĵ�С��CUBE*CUBE 
	 */
	private int CUBE = 35;
	/**
	 * ���ڶ�ͼƬ�����ƶ������ű任�ľ���
	 */
	private Matrix matrix = new Matrix();

	/**
	 * ��չʾ��Bitmap����
	 */
	public Bitmap sourceBitmap;

	public Bitmap sourceBitmapCopy;

	/**
	 * ��¼��ǰ������״̬����ѡֵΪSTATUS_INIT��STATUS_ZOOM_OUT��STATUS_ZOOM_IN��STATUS_MOVE
	 */
	private int currentStatus;

	/**
	 * ZoomImageView�ؼ��Ŀ��
	 */
	private int width;

	/**
	 * ZoomImageView�ؼ��ĸ߶�
	 */
	private int height;

	/**
	 * ��¼��ָͬʱ������Ļ��ʱ�����ĵ�ĺ�����ֵ
	 */
	private float centerPointX;

	/**
	 * ��¼��ָͬʱ������Ļ��ʱ�����ĵ��������ֵ
	 */
	private float centerPointY;

	/**
	 * ��¼��ǰͼƬ�Ŀ�ȣ�ͼƬ������ʱ�����ֵ��һ��䶯
	 */
	private float currentBitmapWidth;

	/**
	 * ��¼��ǰͼƬ�ĸ߶ȣ�ͼƬ������ʱ�����ֵ��һ��䶯
	 */
	private float currentBitmapHeight;

	/**
	 * ��¼�ϴ���ָ�ƶ�ʱ�ĺ�����
	 */
	private float lastXMove = -1;

	/**
	 * ��¼�ϴ���ָ�ƶ�ʱ��������
	 */
	private float lastYMove = -1;

	/**
	 * ��¼��ָ�ں����귽���ϵ��ƶ�����
	 */
	private float movedDistanceX;

	/**
	 * ��¼��ָ�������귽���ϵ��ƶ�����
	 */
	private float movedDistanceY;

	/**
	 * ��¼ͼƬ�ھ����ϵĺ���ƫ��ֵ
	 */
	private float totalTranslateX;

	/**
	 * ��¼ͼƬ�ھ����ϵ�����ƫ��ֵ
	 */
	private float totalTranslateY;

	/**
	 * ��¼ͼƬ�ھ����ϵ������ű���
	 */
	private float totalRatio;

	/**
	 * ��¼��ָ�ƶ��ľ�������ɵ����ű���
	 */
	private float scaledRatio;

	/**
	 * ��¼ͼƬ��ʼ��ʱ�����ű���
	 */
	private float initRatio;

	/**
	 * ��¼�ϴ���ָ֮��ľ���
	 */
	private double lastFingerDis;
	
    /**
     * ��ǰ��ָ�����X������
     */
    private float mCurrentX;
    /**
     * ��ǰ��ָ�����Y������
     */
    private float mCurrentY; 
    /**
     * ��ǰ�ֲ���ʾͼ�Ƿ������
     */
    private boolean partIsLeft = true;
    
    /** 
     * �����ı���
     */
    Activity activity;
    
    /** 
     * ǰ��ͼ���ư�
     */
	private Canvas mCanvas;
	/**	
	 * �滭��
	 */
	private Paint mPaint;
	private Path mPath;
	/**	
	 * �ƶ�ʱ���λ�����bitmap��X������
	 */
	private float mX;
	/**
	 * 	�ƶ�ʱ���λ�����bitmap��Y������
	 */
	private float mY;
	/**
	 *  ԭ����ɫ���� 
	 */
	private int[][] color = null;
	/**
	 * 	��������ɫ���� 
	 */
	private int[][] newColor = null;
    
	/**
	 * ZoomImageView���캯��������ǰ����״̬��ΪSTATUS_INIT��
	 * 
	 * @param context
	 * @param attrs
	 */
	public MosaicImageView(Activity activity, AttributeSet attrs, String pathName, int width, int height) {
		super(activity, attrs);
		currentStatus = STATUS_INIT;
		System.out.println("============" + pathName);
		Bitmap bitmap = getimage(pathName, width, height);
		
		sourceBitmap =  Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		sourceBitmapCopy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		mCanvas = new Canvas(sourceBitmap);
		mCanvas.drawBitmap(bitmap, 0, 0, null);

		Canvas canvas = new Canvas(sourceBitmapCopy);
		canvas.drawBitmap(bitmap, 0, 0, null);
		
		this.activity = activity;
		mPaint = new Paint();
		mPaint.setAlpha(0);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mPaint.setAntiAlias(true);
		
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPath = new Path();
		
		bitmap.recycle();
		invalidate();
	}
	
	
	public void revocation(String pathName,int ww, int hh) {
		// TODO Auto-generated method stub
		sourceBitmap.recycle();
		sourceBitmap = null;
		
		Bitmap bitmap = getimage(pathName, ww, hh);
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
			// �ֱ��ȡ��ZoomImageView�Ŀ�Ⱥ͸߶�
			width = getWidth();
			height = getHeight();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_POINTER_DOWN:
			if (event.getPointerCount() == 2) {
				// ����������ָ������Ļ��ʱ��������ָ֮��ľ���
				lastFingerDis = distanceBetweenFingers(event);
			}
			break;
		case MotionEvent.ACTION_DOWN:
			if (event.getPointerCount() == 1) {
				touch_down((event.getX() - totalTranslateX) / totalRatio, (event.getY() - totalTranslateY) / totalRatio);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getPointerCount() == 1) {
					// ֻ�е�ָ������Ļ���ƶ�ʱ��Ϊ�鿴�ֲ�״̬
					currentStatus = STATUS_PART;
			        mCurrentX = event.getX();  
			        mCurrentY = event.getY();  
					touch_move((mCurrentX - totalTranslateX) / totalRatio, (mCurrentY - totalTranslateY) / totalRatio);
					invalidate();
			} else if (event.getPointerCount() == 2) {
				//�϶�
				float xMove = (event.getX(0) + event.getX(1))/2;
				float yMove = (event.getY(0) + event.getY(1))/2;

				if (lastXMove == -1 && lastYMove == -1) {
					centerMovePointBetweenFingers(event);
				}
				movedDistanceX = xMove - lastXMove;
				movedDistanceY = yMove - lastYMove;
				// ���б߽��飬������ͼƬ�ϳ��߽�
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
				
				// ����
				Boolean isDrag = false;
				centerPointBetweenFingers(event);
				double fingerDis = distanceBetweenFingers(event);
				if (fingerDis > lastFingerDis) {
					currentStatus = STATUS_ZOOM_OUT_AND_MOVE;
				} else {
					currentStatus = STATUS_ZOOM_IN_AND_MOVE;
				}
				// �������ű�����飬���ֻ����ͼƬ�Ŵ�4������С������С����ʼ������
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
				
				// ����onDraw()��������ͼƬ
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
				// ��ָ�뿪��Ļʱ����ʱֵ��ԭ
				lastXMove = -1;
				lastYMove = -1;
			}
			break;
		case MotionEvent.ACTION_UP:
			// ��ָ�뿪��Ļʱ����ʱֵ��ԭ
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
	 * ����currentStatus��ֵ��������ͼƬ����ʲô���Ļ��Ʋ�����
	 */
	@Override
	protected void onDraw(Canvas canvas) {
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
				// ���½���ͼƬ
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
		mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		mPath.reset();
	}
	
	/**
	 * �ֲ�ͼƬ���ڵĻ���
	 * @param canvas
	 */
	private void part(Canvas canvas) {
		float bitmapTop = totalTranslateY;//bitmap����Y��ֵ
		float bitmapBottom = bitmapTop + sourceBitmap.getHeight() * totalRatio;//bitmap�ײ�Y��ֵ
		float bitmapLeft = totalTranslateX;//bitmap��X��ֵ
		float bitmapRight = bitmapLeft + sourceBitmap.getWidth() * totalRatio;//bitmap�Ҳ�X��ֵ

		float circleCenterX = mCurrentX;//ָʾ��Բ�ĵ�X������
		float circleCenterY = mCurrentY;//ָʾ��Բ�ĵ�Y������
		
		float partCenterX = mCurrentX;//�ֲ�ͼ���ĵ�X������
		float partCenterY = mCurrentY;//�ֲ�ͼ���ĵ�Y������
		
		
		if( mCurrentX < (RADIUS * 2) && mCurrentY < (RADIUS * 2) && partIsLeft){
			partIsLeft = false;
		}else if((mCurrentX > canvas.getWidth() - (RADIUS * 2) && mCurrentY < (RADIUS * 2)) && !partIsLeft){
			partIsLeft = true;
		}

		if((mCurrentY < bitmapTop + RADIUS) || (mCurrentY > bitmapBottom - RADIUS) || (mCurrentX < bitmapLeft + RADIUS) || (mCurrentX > bitmapRight - RADIUS)){
			if((mCurrentY < bitmapTop + RADIUS)){//׼���Ϸ�Խ��
				partCenterY = mCurrentY + ( bitmapTop + RADIUS - mCurrentY);
			}
			if((mCurrentY > bitmapBottom - RADIUS)){//׼���·�Խ��
				partCenterY = mCurrentY - ( mCurrentY - bitmapBottom + RADIUS);
			}
			if(mCurrentX < bitmapLeft + RADIUS){//׼����Խ��
				partCenterX = mCurrentX + ( bitmapLeft + RADIUS - mCurrentX);
			}
			if(mCurrentX > bitmapRight - RADIUS){//׼���ҷ�Խ��
				partCenterX = mCurrentX - ( mCurrentX - bitmapRight + RADIUS);
			}
		}
		Path path = new Path();
        path.addRect(0, 0, RADIUS * 2, RADIUS * 2, Direction.CW);
        //��ͼ  
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);
		mCanvas.drawPath(mPath, mPaint);

		//����  
		if(partIsLeft){
			canvas.translate(0 , 0);  
		}else{
			canvas.translate(canvas.getWidth() - RADIUS * 2 , 0);  
		}
		canvas.clipPath(path);  
		//���ֲ�ͼ
		canvas.translate(RADIUS - partCenterX * FACTOR, RADIUS - partCenterY * FACTOR);  
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);          
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		//����ָʾ���м�
		paint.setColor(getResources().getColor(R.color.mosaicdark));  
		paint.setStyle(Style.FILL);//ʵ��ͼ��
		if(initRatio > 1){
			canvas.drawCircle(circleCenterX, circleCenterY, (PAINT_STROKEWIDTH - 5) / 2 * initRatio * strokeMultiples , paint);
		}else{
			canvas.drawCircle(circleCenterX, circleCenterY, (PAINT_STROKEWIDTH - 5) / 2 / initRatio * strokeMultiples , paint);
		}
		//���ƻ���ָʾ��ԭ�α߿�
		paint.setColor(getResources().getColor(R.color.mosaicblue));  
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(4f);
		if(totalRatio > 1){
			canvas.drawCircle(circleCenterX, circleCenterY, (PAINT_STROKEWIDTH - 4) / 2 * initRatio * strokeMultiples, paint);
		}else{
			canvas.drawCircle(circleCenterX, circleCenterY, (PAINT_STROKEWIDTH - 4) / 2 / initRatio * strokeMultiples, paint);
		}

		//���ư�ɫ�߿�
		paint.setStyle(Style.STROKE);//����ͼ��
		paint.setStrokeWidth(2.5f);
		paint.setColor(Color.WHITE);  
		canvas.drawRect(new RectF(partCenterX - RADIUS + 1, partCenterY - RADIUS + 1, partCenterX + RADIUS - 1, partCenterY + RADIUS - 1), paint); 
		
    }
	
	/**
	 * ��ͼƬ�������Ŵ���
	 * 
	 * @param canvas
	 */
	private void zoom(Canvas canvas) {
		mPaint.setStrokeWidth(PAINT_STROKEWIDTH / totalRatio * strokeMultiples);
		matrix.reset();
		// ��ͼƬ�������ű�����������
		matrix.postScale(totalRatio, totalRatio);
		float scaledWidth = sourceBitmap.getWidth() * totalRatio;
		float scaledHeight = sourceBitmap.getHeight() * totalRatio;
		float translateX = 0f;
		float translateY = 0f;
		// �����ǰͼƬ���С����Ļ��ȣ�����Ļ���ĵĺ��������ˮƽ���š�������ָ�����ĵ�ĺ��������ˮƽ����
		if (currentBitmapWidth < width) {
			translateX = (width - scaledWidth) / 2f;
		} else {
			translateX = totalTranslateX * scaledRatio + centerPointX * (1 - scaledRatio);
			// ���б߽��飬��֤ͼƬ���ź���ˮƽ�����ϲ���ƫ�Ƴ���Ļ
			if (translateX > 0) {
				translateX = 0;
			} else if (width - translateX > scaledWidth) {
				translateX = width - scaledWidth;
			}
		}
		// �����ǰͼƬ�߶�С����Ļ�߶ȣ�����Ļ���ĵ���������д�ֱ���š�������ָ�����ĵ����������д�ֱ����
		if (currentBitmapHeight < height) {
			translateY = (height - scaledHeight) / 2f;
		} else {
			translateY = totalTranslateY * scaledRatio + centerPointY * (1 - scaledRatio);
			// ���б߽��飬��֤ͼƬ���ź��ڴ�ֱ�����ϲ���ƫ�Ƴ���Ļ
			if (translateY > 0) {
				translateY = 0;
			} else if (height - translateY > scaledHeight) {
				translateY = height - scaledHeight;
			}
		}
		// ���ź��ͼƬ����ƫ�ƣ��Ա�֤���ź����ĵ�λ�ò���
		matrix.postTranslate(translateX, translateY);
		totalTranslateX = translateX;
		totalTranslateY = translateY;
		currentBitmapWidth = scaledWidth;
		currentBitmapHeight = scaledHeight;
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);
	}

	/**
	 * ��ͼƬ����ƽ�ƴ���
	 * 
	 * @param canvas
	 */
	private void move(Canvas canvas) {
		matrix.reset();
		// ������ָ�ƶ��ľ���������ƫ��ֵ
		float translateX = totalTranslateX + movedDistanceX;
		float translateY = totalTranslateY + movedDistanceY;
		// �Ȱ������е����ű�����ͼƬ��������
		matrix.postScale(totalRatio, totalRatio);
		// �ٸ����ƶ��������ƫ��
		matrix.postTranslate(translateX, translateY);
		totalTranslateX = translateX;
		totalTranslateY = translateY;
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);
	}
	
	/**
	 * ��ͼƬ���г�ʼ��������������ͼƬ���У��Լ���ͼƬ������Ļ���ʱ��ͼƬ����ѹ����
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
					// ��ͼƬ��ȴ�����Ļ���ʱ����ͼƬ�ȱ���ѹ����ʹ��������ȫ��ʾ����
					float ratio = width / (bitmapWidth * 1.0f);
					matrix.postScale(ratio, ratio);
					float translateY = (height - (bitmapHeight * ratio)) / 2f;
					// �������귽���Ͻ���ƫ�ƣ��Ա�֤ͼƬ������ʾ
					matrix.postTranslate(0, translateY);
					totalTranslateY = translateY;
					totalRatio = initRatio = ratio;
				} else {
					// ��ͼƬ�߶ȴ��ڿؼ��߶�ʱ����ͼƬ�ȱ���ѹ����ʹ��������ȫ��ʾ����
					float ratio = height / (bitmapHeight * 1.0f);	
					matrix.postScale(ratio, ratio);
					float translateX = (width - (bitmapWidth * ratio)) / 2f;
					// �ں����귽���Ͻ���ƫ�ƣ��Ա�֤ͼƬ������ʾ
					matrix.postTranslate(translateX, 0);
					totalTranslateX = translateX;
					totalRatio = initRatio = ratio;
				}
				currentBitmapWidth = bitmapWidth * initRatio;
				currentBitmapHeight = bitmapHeight * initRatio;
			} else {

				// ��ͼƬ�Ŀ�߶�С����Ļ���ʱ��ֱ���÷Ŵ���һ������Ϊֹ
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
			color = new int[sourceBitmapCopy.getWidth()][sourceBitmapCopy.getHeight()];
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
			}
			canvas.drawBitmap(sourceBitmapCopy, matrix, null);
			canvas.drawBitmap(sourceBitmap, matrix, null);
			mPaint.setStrokeWidth(PAINT_STROKEWIDTH / totalRatio * strokeMultiples);
		}
	}

	/***
	 * ͼƬ�����ŷ���
	 * 
	 * @param bgimage
	 *            ��ԴͼƬ��Դ
	 * @param newWidth
	 *            �����ź���
	 * @param newHeight
	 *            �����ź�߶�
	 * @return
	 */
	public Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
		// ��ȡ���ͼƬ�Ŀ�͸�
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		// ִ�и����ŷ����������У���һ��С����Ļ�Ĵ�С����ͼƬ�ĸ߶ȴ��ڳ���
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
		// ������������
		float scaleWidth = ((float) newWidth) / (float) width;
		float scaleHeight = ((float) newHeight) / (float) height;
		matrix.postScale(scaleWidth, scaleHeight);

		bgimage = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height,
				matrix, true);
		return bgimage;
	}

	
	/**
	 * ���Ʊʴ���С
	 * @param strokeMultiples
	 * @param canvas
	 */
	public void drawStrokeSize(Canvas canvas) {
		// TODO Auto-generated method stub
		//��ͼ  
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);
		mCanvas.drawPath(mPath, mPaint);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		//���ƻ���ָʾ��ԭ�α߿�
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
	 * ��ͼƬ���лָ�
	 * 
	 * @param canvas
	 */
	private void replyPosition(Canvas canvas) {
		matrix.reset();
		// �Ȱ������е����ű�����ͼƬ��������
		matrix.postScale(totalRatio, totalRatio);
		// �ٸ����ƶ��������ƫ��
		matrix.postTranslate(totalTranslateX, totalTranslateY);
		canvas.drawBitmap(sourceBitmapCopy, matrix, null);
		canvas.drawBitmap(sourceBitmap, matrix, null);
		Intent intent = new Intent(DrawPhotoActivity.ACTION_INIT);
		activity.sendBroadcast(intent);
	}
	
	/**
	 * ����������ָ֮��ľ��롣
	 * 
	 * @param event
	 * @return ������ָ֮��ľ���
	 */
	private double distanceBetweenFingers(MotionEvent event) {
		float disX = Math.abs(event.getX(0) - event.getX(1));
		float disY = Math.abs(event.getY(0) - event.getY(1));
		return Math.sqrt(disX * disX + disY * disY);
	}

	/**
	 * ����������ָ֮�����ĵ�����ꡣ
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
	 * �����ƶ�ʱ������ָ֮�����ĵ�����ꡣ
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
	 * �ϲ�����bitmapΪһ�� 
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
	
	private Bitmap getimage(String srcPath, int newWidth, int newHeight) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = false;
		// ��ʱ����bmΪ��
		Bitmap bitmap = ImageUtil.getLoacalBitmap(activity, srcPath);
		System.out.println("Bitmap:" + bitmap);
		bitmap = zoomImage(bitmap, newWidth, newHeight);
		return bitmap;// ѹ���ñ�����С���ٽ�������ѹ��
	}

	
	/**
	 * ���������˺����ɫ���飬����ͼƬ��Ϊ�ܶ�飬ÿ�����ɫֵһ����
	 * 
	 * @param dst
	 *            ������֮�����ɫ����
	 * @param src
	 *            ԭ������ɫ����
	 */
	private void newColor(int[][] dst, int[][] src) {
		
		
		int tmpColorR = 0, tmpColorG = 0, tmpColorB = 0;
		int tmpColor;
		int procTileW = 0, procTileH = 0;
		int bitmapH = sourceBitmapCopy.getHeight();
		int bitmapW = sourceBitmapCopy.getWidth();
		
		for (int row = 0; row < bitmapH;) {
			procTileH = CUBE;
			// ������tileSize��ɫ��
			while (row + procTileH > bitmapH) {
				procTileH--;
			}
			for (int col = 0; col < bitmapW;) {
				tmpColorR = 0;
				tmpColorG = 0;
				tmpColorB = 0;
				procTileW = CUBE;
				// ������tileSize��ɫ��
				while (col + procTileW > bitmapW) {
					procTileW--;
				}
				// ȡ��tileSize*tileSize��С��rgb��ɫֵ
				for (int i = 0; i < procTileH; i++) {
					for (int j = 0; j < procTileW; j++) {
						tmpColorR += Color.red(src[j + col][i + row]);
						tmpColorG += Color.green(src[j + col][i + row]);
						tmpColorB += Color.blue(src[j + col][i + row]);
					}

				}
				tmpColorR /= procTileW * procTileH;
				tmpColorG /= procTileW * procTileH;
				tmpColorB /= procTileW * procTileH;
				tmpColor = Color.rgb(tmpColorR, tmpColorG, tmpColorB);
				// ���tileSize*tileSize��С��������ɫ��
				for (int i = 0; i < procTileH; i++) {
					for (int j = 0; j < procTileW; j++) {
						dst[j + col][i + row] = tmpColor;
					}

				}
				col += procTileW;
			}
			row += procTileH;
		}
	}
	
	/**
	 * ���ͼƬ�������ص�ֵ������
	 * 
	 * @return ��ɫֵ����
	 */
	public int[][] getColor() {
		return color;
	}

	/**
	 * ���ͼƬ�������ص�������Ч�������ֵ
	 * 
	 * @return ��ɫֵ����
	 */
	public int[][] getNewColor() {
		return newColor;
	}

}