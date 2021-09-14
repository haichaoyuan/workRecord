package com.example.module_commonview.keyboard.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import com.bangcle.safekb.api.PwdEditText;
/**
 * 扩展类：重载原先的控件的绘图行为，实现六宫格密码框
 */
public class SixPwdEditTextV2 extends PwdEditText{
	private Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private float mStrokeWidth = 1.0f;
	private RectF mRect=new RectF();
	public SixPwdEditTextV2(Context mContext) {
		this(mContext,null,0);
	}
	public SixPwdEditTextV2(Context mContext, AttributeSet attrs) {
		this(mContext, attrs, 0);
	}
	public SixPwdEditTextV2(Context mContext, AttributeSet attrs, int defStyleAttr) {
		super(mContext, attrs, 0);
		this.initializeView(mContext);
	}
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public SixPwdEditTextV2(Context mContext, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(mContext, attrs, 0, 0);
		this.initializeView(mContext);
	}
	public void initializeView(Context mContext) {
		super.setBackgroundColor(0x00FFFFFF);
		float mDensity=mContext.getResources().getDisplayMetrics().density;
		this.mStrokeWidth=mDensity;
		super.setCursorVisible(false);
		//背景线框画笔样式
		mBgPaint.setStrokeWidth(mStrokeWidth);
		mBgPaint.setStyle(Paint.Style.STROKE);
		mBgPaint.setColor(0xFFCCD1D7);
		//文本画笔样式
		mTextPaint.setStrokeWidth(mStrokeWidth);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setTextSize(20*mDensity);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
		mTextPaint.setColor(0xFF000000);
	}
	@Override
	protected void onDraw(Canvas mCanvas) {
		int MAX_LEN=mKeyboard.mViewAttr.maxlen();
		//留出四周边距及线宽，求出可用区域
		mRect.set(getPaddingLeft()+mStrokeWidth,getPaddingTop()+mStrokeWidth,getWidth()-getPaddingRight()-mStrokeWidth,getHeight()-getPaddingBottom()-mStrokeWidth);
		//计算单元格边长和实际画图区域
		float mUnit=Math.min(mRect.width()/MAX_LEN,mRect.height());
		float realWidth=mUnit*MAX_LEN;
		mRect.set(mRect.centerX()-realWidth/2,mRect.centerY()-mUnit/2,mRect.centerX()+realWidth/2,mRect.centerY()+mUnit/2);
		//float mUnit=mRect.width()/MAX_LEN;
		mCanvas.drawRect(mRect,mBgPaint);
		float posX=mRect.left;
		//当前文本长度
		int mTextLen=super.getText().length();
		for(int i=0;i<MAX_LEN;i++){
			if(i!=0) {
				mCanvas.drawLine(posX, mRect.top, posX, mRect.bottom, mBgPaint);
			}
			if(i<mTextLen) {
				mCanvas.drawCircle(posX + mUnit / 2, mRect.centerY(), mRect.height() / 6, mTextPaint);
			}
			posX+=mUnit;
		}
	}
}