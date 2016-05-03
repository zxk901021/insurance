package cn.com.youyouparttime.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.v4.view.ViewPager.LayoutParams;
//import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ViewPagerAdapter extends PagerAdapter {
	private ImageView[] mImageView;

	public ViewPagerAdapter(ImageView[] mImageView) {
		super();
		this.mImageView = mImageView;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		try {
			((ViewPager) container).addView(
					mImageView[position % mImageView.length], 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
			return mImageView[position % mImageView.length];
		
		
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		if (mImageView.length <= 3) {
//			((ViewPager) container).removeView(mImageView[position%mImageView.length]);
		} else {
//			((ViewPager) container).removeView((View) object);
			((ViewPager) container).removeView(mImageView[position%mImageView.length]);
		}
	}
}
