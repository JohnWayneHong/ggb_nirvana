package com.ggb.nirvanaclub.view.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ControlsRatio extends RelativeLayout {
  private float mRatio;
  
  public ControlsRatio(Context paramContext) {
    this(paramContext, null);
  }
  
  public ControlsRatio(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ControlsRatio(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  

  protected void onMeasure(int paramInt1, int paramInt2) {
    int j = MeasureSpec.getMode(paramInt1);
    int k = MeasureSpec.getSize(paramInt1);
    int i = paramInt2;
    if (j == MeasureSpec.EXACTLY) {
      float f = this.mRatio;
      i = MeasureSpec.makeMeasureSpec((int)(k + 0.5F), MeasureSpec.EXACTLY);

    } 
    super.onMeasure(paramInt1, i);
  }
}