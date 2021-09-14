package com.example.module_commonview.keyboard.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import com.bangcle.safekb.api.InputListener;
import com.bangcle.safekb.api.PwdEditText;
/**
 * 扩展类：重载原先的控件的绘图行为，4个字符一组
 */
public class SplitNumEditText extends PwdEditText implements InputListener{
	public SplitNumEditText(Context context) {
		super(context);
		super.setInputListener(this);
	}
	public SplitNumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		super.setInputListener(this);
	}
	public SplitNumEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		super.setInputListener(this);
	}
	public SplitNumEditText(Context mContext, AttributeSet attrs) {
		super(mContext, attrs);
		super.setInputListener(this);
	}
	@Override
	public void onInputChanged(CharSequence txtValue,CharSequence encValue, int currLen, int maxLen) {
		Editable mText=super.getText();
		for(int i=0;i<mText.length();i++){
			AutoSplitSpan spans[]=mText.getSpans(i,i+1, AutoSplitSpan.class);
			if(spans!=null&&spans.length>0) continue;
			mText.setSpan(new AutoSplitSpan(),i,i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
	}
	public class AutoSplitSpan extends ReplacementSpan {
		private final int SPLIT_UNIT=4;
		private float SPACE=0;
		@Override
		public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
			if(SPACE==0){
				//计算单个字符的宽度
				SPACE=paint.measureText(" ")*1.5f;
			}
			float retValue=paint.measureText(text,start,end);
			if(start>0&&start%SPLIT_UNIT==0){
				retValue+=SPACE;
			}
			return (int)(retValue+0.5);
		}
		@Override
		public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
			if(start>0&&start%SPLIT_UNIT==0){
				canvas.drawText(text, start, end,x+SPACE,y,paint);
			}else{
				canvas.drawText(text, start, end,x,y,paint);
			}
		}
	}
}
