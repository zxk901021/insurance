package cn.com.youyouparttime;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.util.CommonUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends Activity{

	
	/**
	 * 0代表第一次登陆 需选择何种入口
	 * 1代表已登陆过,且该用户为学生，该activity为欢迎界面，跳转找兼职首页
	 * 2代表已登陆过,且该用户为企业，该activity为欢迎界面，跳转雇兼职首页
	 */
	private int loginMode;
	private int winHeight;
	private static boolean isExit = false;
	public static final int COMPANY_MODE = 2;
	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		super.handleMessage(msg);
		isExit = false;
		}
	};
	
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SysApplication.getInstance().addActivity(this);
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        winHeight = manager.getDefaultDisplay().getWidth();
        Log.e("weight", winHeight+"");
        loginMode = CommonUtil.isLogin(this);
        Log.e("loginMode", loginMode+"");
        gotowhichActivity(loginMode);
    }

    public void gotowhichActivity(int loginMode){
    	switch (loginMode) {
		case 0:
			firstLogin();
			break;

		case 1:
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					Intent intent = new Intent(MainActivity.this, PartTimeActivity.class);
					startActivity(intent);
					finish();
				}
			}, 2000);
			
			break;
		case 2:
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					Intent intent = new Intent(MainActivity.this, ReleasePartTimeActivity.class);
					startActivity(intent);
					finish();
				}
			}, 2000);
		}
    }
    
    private Button seekPartTime;
	private Button hirePartTime;
	private LinearLayout btnLayout;
	
	public void firstLogin(){
		seekPartTime = (Button) findViewById(R.id.seek_part_time);
        hirePartTime = (Button) findViewById(R.id.hire_part_time);
        btnLayout = (LinearLayout) findViewById(R.id.btn_layout);
        btnLayout.setVisibility(View.VISIBLE);
//        Animation transAnimation = new TranslateAnimation(0, 0, 0, -(int)(winHeight/5));
//        Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
//        AnimationSet set = new AnimationSet(true);
//        transAnimation.setDuration(2000);
//        transAnimation.setStartOffset(1000);
//        alphaAnimation.setDuration(3000);
//        alphaAnimation.setStartOffset(1500);
//        set.addAnimation(transAnimation);
//        set.addAnimation(alphaAnimation);
//        set.setFillAfter(true);
//        btnLayout.setAnimation(set);
//        
//        transAnimation.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				int left = btnLayout.getLeft();
//				int top = btnLayout.getTop() - (int)(winHeight/5);
//				int width = btnLayout.getWidth();
//				int height = btnLayout.getHeight();
//				btnLayout.clearAnimation();
//				btnLayout.layout(left, top, left + width, top + height);
//			}
//		});
//        set.startNow();
        seekPartTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LoginForStudentsActivity.class);
				startActivity(intent);
			}
		});
        hirePartTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LoginForStudentsActivity.class);
				intent.putExtra("launchMode", COMPANY_MODE);
				startActivity(intent);
			}
		});
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (loginMode != 0) {
    		if (keyCode == KeyEvent.KEYCODE_BACK) {
    			return false;
    		}
		}
    	else {
			exit();
			return false;
		}
    	
    	return super.onKeyDown(keyCode, event);
    }
    
    private void exit() {
        if (!isExit) {
                isExit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                                Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessageDelayed(0, 2000);
        } else {
        	SysApplication.getInstance().exit();
        }
}

}
