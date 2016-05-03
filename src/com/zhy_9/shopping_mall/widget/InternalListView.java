package com.zhy_9.shopping_mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class InternalListView extends ListView {

	public InternalListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public InternalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InternalListView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
}
