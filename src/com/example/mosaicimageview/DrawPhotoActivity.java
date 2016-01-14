package com.example.mosaicimageview;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��ͼƬ���б༭�Ľ���
 * 
 **/

public class DrawPhotoActivity extends Activity {

	public LinearLayout mMosaicViewContent;
	private MosaicView mMosaicView;
	/** ��ɰ�ť **/
	public TextView mFinishBtn;
	/** ���ذ�ť�����Ͻǣ� */
	public ImageButton mBackBtn = null;
	/** �������� **/
	public TextView mCancelBtn;

	/** �ص��ӿ� */
	private SeekBar mMosaicSizeSeekBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draw_photo);
		initView();
		// ���ɻ�ͼ��ͼ
		mMosaicView = new MosaicView(DrawPhotoActivity.this);
		mMosaicView.setSourceBitmap(BitmapFactory.decodeResource(getResources(), R.raw.aaa));
		if (mMosaicView != null) {
			mMosaicViewContent.removeView(mMosaicView);
		}
		mMosaicView.destroyDrawingCache();
		mMosaicViewContent.addView(mMosaicView);

	}

	private void initView() {
		mMosaicViewContent = (LinearLayout) findViewById(R.id.draw_photo_view);
		mBackBtn = (ImageButton) findViewById(R.id.title_bar_left_btn);
		mBackBtn.setVisibility(View.VISIBLE);
		mFinishBtn = (TextView) findViewById(R.id.draw_ok_text);
		mCancelBtn = (TextView) findViewById(R.id.draw_photo_cancel);
		mMosaicSizeSeekBar = (SeekBar) findViewById(R.id.seekBar);
		mMosaicSizeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress = 0;

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (progress > 0 && progress < 12.5) {
					seekBar.setProgress(0);
				} else if (progress > 12.5 && progress < 25) {
					seekBar.setProgress(25);
				} else if (progress > 25 && progress < 37.5) {
					seekBar.setProgress(25);
				} else if (progress > 37.5 && progress < 50) {
					seekBar.setProgress(50);
				} else if (progress > 50 && progress < 62.5) {
					seekBar.setProgress(50);
				} else if (progress > 62.5 && progress < 75) {
					seekBar.setProgress(75);
				} else if (progress > 75 && progress < 87.5) {
					seekBar.setProgress(75);
				} else if (progress > 87.5 && progress < 100) {
					seekBar.setProgress(100);
				}
				mMosaicView.setStrokeMultiples(1 + (float) (progress / 50.0));
				mMosaicView.removeStrokeView();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				this.progress = progress;
				mMosaicView.setStrokeMultiples(1 + (float) (progress / 100.0));
			}
		});
		mBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMosaicView.release();
				finish();
			}
		});
		mFinishBtn.setOnClickListener(new View.OnClickListener() {// ��ɱ༭��ť
			@Override
			public void onClick(View v) {
				mFinishBtn.setEnabled(false);
				// �½�һ���ļ�������Ƭ
				File f = new File(GlobalData.tempImagePaht + "/" + System.currentTimeMillis() + ".jpg");
				if (!f.exists()) {
					try {
						if (!new File(GlobalData.tempImagePaht).exists()) {
							new File(GlobalData.tempImagePaht).mkdirs();
						}
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					Bitmap saveBitmap = mMosaicView.getMosaicBitmap();
					ImageUtil.saveMyBitmap(f, saveBitmap);// ��ͼƬ���´���SD��
					mMosaicView.release();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Toast.makeText(DrawPhotoActivity.this, "�ѱ�����SD��MosaicImageView/TempĿ¼��", Toast.LENGTH_LONG).show();
				finish();
			}
		});

		mCancelBtn.setOnClickListener(new View.OnClickListener() {// ������ť
			@Override
			public void onClick(View v) {
				cancelDrawImage();
			}
		});
	}

	/** �������� **/
	public void cancelDrawImage() {
		mMosaicView.destroyDrawingCache();
		mMosaicView.setSourceBitmap(BitmapFactory.decodeResource(getResources(), R.raw.aaa));
		if (mMosaicViewContent.getChildCount() == 0) {
			mMosaicViewContent.addView(mMosaicView);
		}
	}

}
