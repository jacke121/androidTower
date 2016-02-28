package my.JSB.android;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

import com.lbg.yan01.R;

/************************
 *自定义的EditText样式
 */
public class LinedEditText extends EditText {
	private Paint linePaint;
	     private float margin;
	     private int paperColor;
	 
	     public LinedEditText(Context paramContext, AttributeSet paramAttributeSet) {
	         super(paramContext, paramAttributeSet);
	         this.linePaint = new Paint();
	         this.linePaint.setColor(getContext().getResources().getColor(R.color.grey));
	     }
	 
	     protected void onDraw(Canvas paramCanvas) {
	         paramCanvas.drawColor(this.paperColor);
	         int i = getLineCount();
	         int j = getHeight();
	         int k = getLineHeight();
	         int m = 1 + j / k;
	         if (i < m)
	             i = m;
	         int n = getCompoundPaddingTop();
	         paramCanvas.drawLine(0.0F, n, getRight(), n, this.linePaint);
	         for (int i2 = 0;; i2++) {
	             if (i2 >= i) {
	                 setPadding(10 + (int) this.margin, 0, 10, 0);
	                 super.onDraw(paramCanvas);
	                 paramCanvas.restore();
	                 return;
	             }
	             n += k;
	             paramCanvas.drawLine(5.0F, n, getRight()-5.0F, n, this.linePaint);
	             paramCanvas.save();
	         }
	    }

}
