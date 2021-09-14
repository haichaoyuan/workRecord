package com.example.module_commonview.keyboard.view;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import com.bangcle.safekb.api.PwdEditText;
/**
 * 扩展类：重载原先的控件的绘图行为，实现六宫格密码框
 */
public class SixPwdEditTextV1 extends PwdEditText{
	private Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private float mStrokeWidth = 1.0f;
	private float SPACEING=0;
	private float RADIUS=0;
	private RectF mRect=new RectF();
	public SixPwdEditTextV1(Context mContext) {
		this(mContext,null,0);
	}
	public SixPwdEditTextV1(Context mContext, AttributeSet attrs) {
		this(mContext, attrs, 0);
	}
	public SixPwdEditTextV1(Context mContext, AttributeSet attrs, int defStyleAttr) {
		super(mContext, attrs, 0);
		this.initializeView(mContext);
	}
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public SixPwdEditTextV1(Context mContext, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(mContext, attrs, 0, 0);
		this.initializeView(mContext);
	}
	public void initializeView(Context mContext) {
		super.setBackgroundColor(0x00FFFFFF);
		float mDensity=mContext.getResources().getDisplayMetrics().density;
		this.mStrokeWidth=mDensity;
		this.SPACEING=8*mDensity;
		this.RADIUS=3*mDensity;
		super.setCursorVisible(false);
		//背景线框画笔样式
		mBgPaint.setStrokeWidth(mStrokeWidth);
		mBgPaint.setStyle(Paint.Style.STROKE);
		mBgPaint.setColor(0xFFDDDDDD);
		//文本画笔样式
		mTextPaint.setStrokeWidth(mStrokeWidth);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setTextSize(20*mDensity);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
		mTextPaint.setColor(0xFF333333);
	}
	@Override
	protected void onDraw(Canvas mCanvas) {
		int MAX_LEN=mKeyboard.mViewAttr.maxlen();
		//mRect对象设置为当前有效绘图区域（留出四周边距及线宽）
		mRect.set(getPaddingLeft()+mStrokeWidth,getPaddingTop()+mStrokeWidth,getWidth()-getPaddingRight()-mStrokeWidth,getHeight()-getPaddingBottom()-mStrokeWidth);
		//1、计算单元格之间空白的和
		float totalSpacing=SPACEING*(MAX_LEN-1);
		//2、最小值法：计算正方形单元格的最终边长
		float mUnit=Math.min((mRect.width()-totalSpacing)/MAX_LEN,mRect.height());
		//3、实际绘图区域总长度 = 单元格边长 * 单元格个数 + 所有单元格与单元格之间空白的总宽度
		//4、实际绘图区域的高度 = 单元格边长
		float realWidth=mUnit*MAX_LEN+totalSpacing;
		//计算绘图起始坐标，并将mRect对象重置为第一个单元格区域
		float startX=mRect.centerX()-realWidth/2;
		mRect.set(startX,mRect.centerY()-mUnit/2,startX+mUnit,mRect.centerY()+mUnit/2);
		//当前字符个数
		int mTextLen=super.getText().length();
		for(int i=0;i<MAX_LEN;i++){
			//绘制方框
			mBgPaint.setColor(0xFFCCCCCC);
			mCanvas.drawRoundRect(mRect,RADIUS,RADIUS,mBgPaint);
			if(i<mTextLen) {
				//绘制圆点
				mTextPaint.setColor(0xFF000000);
				mCanvas.drawCircle(mRect.centerX(),mRect.centerY(),mRect.height()/6,mTextPaint);
			}
			//X坐标右移一个单元格加空白的距离
			mRect.offset(mUnit+SPACEING,0);
		}
	}
}