package cn.com.youyouparttime.service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.com.youyouparttime.NotificationActivity;
import cn.com.youyouparttime.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

@SuppressLint("ShowToast") 
public class DownloadService extends Service{
	private NotificationManager notificationMrg;
	public static Map<String, MyThread> threadcache = new HashMap<String, MyThread>();
	private Holder holder;
	int notifyflag = 1;

	private ConnectivityManager connectivityManager;
	private NetworkInfo info;

	public void onCreate() {
		super.onCreate();
		notificationMrg = (NotificationManager) this
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		// ע���������������
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mFilter.addAction("android.basic.notification.click.pause");
		mFilter.addAction("android.basic.notification.click.cancel");
		mFilter.addAction("android.basic.notification.click.restart");
		registerReceiver(stateReceiver, mFilter);
		// ע������㲥������


	}

	@SuppressLint("NewApi") @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		System.out.println("Get intent:" + intent);
		final String apkurl = intent.getStringExtra("url");
//		System.out.println("Get url from intent:" + apkurl);
//		Log.e("Get url from intent:", apkurl);
		Intent notificationIntent = new Intent(getApplicationContext(),
				NotificationActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		notificationIntent.putExtra("url", apkurl);
		Bundle bundle = new Bundle();
		bundle.putInt("flag", notifyflag);
		Log.i("avi", "loadfile�е�flag     " + notifyflag);
		notificationIntent.putExtras(bundle);
		// addflag������ת����
		PendingIntent contentIntent1 = PendingIntent.getActivity(
				getApplicationContext(), notifyflag, notificationIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// ����Notifcation��������ͼ�꣬��ʾ����
		@SuppressWarnings("deprecation")
		Notification notification = new Notification(R.drawable.p8,
				"���ؽ���", System.currentTimeMillis());// �趨Notification����ʱ��������һ�㲻�����Զ���
		// System.currentTimeMillis()
		notification.flags = Notification.FLAG_ONGOING_EVENT
				| Notification.FLAG_NO_CLEAR;// ������ ���������еġ���Ŀ����
		// notification.flags |= Notification.FLAG_NO_CLEAR;// ������Ĭ�������ťʧЧ
		RemoteViews contentView1 = new RemoteViews(getPackageName(),
				R.layout.notification_version);
		contentView1.setTextViewText(R.id.n_title, "׼������");
		contentView1.setTextViewText(R.id.n_text, "��ǰ���ȣ�" + 0 + "% ");
		contentView1.setProgressBar(R.id.n_progress, 100, 0, false);
		notification.contentView = contentView1;
		notification.contentIntent = contentIntent1;

		final MyThread thread = new MyThread(notification, apkurl, notifyflag++);

		thread.setchanger(true);
		threadcache.put(apkurl, thread);
		thread.start();

		return START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(stateReceiver);
	}

	// ״̬����ͼ����
	private Notification displayNotificationMessage(Notification notification,
			int count, int flag, String url, String filename) {
		notification.contentIntent = receiveMsmIntent(flag, url);
		RemoteViews contentView1 = notification.contentView;
		Log.i("TAG", "updata   flag==  " + flag);
		Log.i("TAG", "updata   count==  " + count);
		Log.i("TAG", "updata   filename==  " + filename);
		contentView1.setTextViewText(R.id.n_title, filename);
		contentView1.setTextViewText(R.id.n_text, "��ǰ���ȣ�" + count + "% ");
		contentView1.setProgressBar(R.id.n_progress, 100, count, false);
		Log.i("temp", "diaplaynotification   " + flag);
		notification.contentView = contentView1;
		// �ύһ��֪ͨ��״̬������ʾ�����ӵ����ͬ��ǩ����ͬid��֪ͨ�Ѿ����ύ����û�б��Ƴ����÷������ø��µ���Ϣ���滻֮ǰ��֪ͨ��
		notificationMrg.notify(flag, notification);
		return notification;

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void sendMsg(int what, int c, String url,
			Notification notification, int flag, Uri uri, String filename,
			boolean changer) {
		Message msg = new Message();
		msg.what = what;// ����ʶ������Ϣ������
		msg.arg1 = 0;
		holder = new Holder();
		holder.count = c;
		holder.url = url;
		holder.flag = flag;
		holder.notify = notification;
		holder.Uri = uri;
		holder.filename = filename;
		holder.changer = changer;
		msg.obj = holder;// ��Ϣ���ݵ��Զ��������Ϣ
		handler.sendMessage(msg);
	}

	// ����һ��Handler�����ڴ��������߳������̼߳�ͨѶ
	private Handler handler = new Handler() {
		@SuppressLint("ShowToast") @Override
		public void handleMessage(Message msg) {
			final Holder data = (Holder) msg.obj;
			if (!Thread.currentThread().isInterrupted()) {
				// �ж������߳��Ƿ��ж�
				switch (msg.what) {
				case 1:
					Log.i("TAG", "handlemessage�е� case 1: data.count     "
							+ data.count);
					Log.i("TAG", "handlemessage�е� case 1: flag          "
							+ data.flag);
					if (data.count >= 99) {
						notificationMrg.cancel(data.flag);
						break;
					}

					if (threadcache.containsKey(data.url)) {
						// ÿ�θ���ʱ������key��ɨ��hashmap���������ȡ������
						Notification notification;
						MyThread thread;
						thread = threadcache.get(data.url);
						notification = thread.notification;
						notification = displayNotificationMessage(notification,
								data.count, data.flag, data.url, data.filename);
						thread.notification = notification;
						threadcache.put(data.url, thread);
					}
					break;
				case 2:
					Log.i("avi", "case 2��filename ��  " + data.filename);
					Log.i("avi", "case 2��filename ��  " + data.changer);
					if (data.changer) {
						openfile(data.Uri);
					} else {
						Log.i("avi", "ȡ������");
						Toast.makeText(getApplicationContext(),
								data.filename + "����ȡ��", 1).show();
					}
					threadcache.remove(data.url);
					Intent cancel =new Intent().setAction("android.basic.button.click.cancel");
					cancel.putExtra("url", data.url);
					sendBroadcast(cancel);
					break;
				case -1:
					String error = msg.getData().getString("error");
					Toast.makeText(getApplication(), data.filename + "�����쳣��ֹ",
							1).show();
					Intent cancel2 =new Intent().setAction("android.basic.button.click.cancel");
					cancel2.putExtra("url", data.url);
					sendBroadcast(cancel2);
					break;
				// �������������ʾ
				}
			}
			super.handleMessage(msg);
		}
	};

	public void openfile(Uri url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		// ������Ĭ������������activityջ���Ѿ����ڵ�activity�����֮ǰ������������û�б�destroy�Ļ������������Ƿ���ڣ������������µ�activity
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(url, "application/vnd.android.package-archive");
		startActivity(intent);
	}

	public class Holder {
		Notification notify;
		String url;
		int count;
		int flag;
		Uri Uri;
		String filename;
		boolean changer;
	}

	private BroadcastReceiver stateReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Log.d("mark", "����״̬�Ѿ��ı�");
				connectivityManager = (ConnectivityManager)

				getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					String name = info.getTypeName();
					Log.d("mark", "��ǰ�������ƣ�" + name);
				} else {
					Log.d("mark", "û�п�������,���ط���ֹͣ");
					while (notifyflag > 0) {
						notificationMrg.cancel(notifyflag);
						notifyflag -= 1;
					}
					Toast.makeText(getApplicationContext(), "�����쳣�����ط���ֹͣ��", 1)
							.show();
					onDestroy();
				}
			}
			
			if (action.equals("android.basic.notification.click.cancel")) {
				Bundle bundle = intent.getExtras();
				int m = bundle.getInt("flag");
				String url = intent.getStringExtra("url");
				Log.i("avi", "cancel   clickbroadcast��         ���յ��� flag: " + m);
				Log.i("avi", "cancel   clickbroadcast��         ���յ��� url: "
						+ url);
				notificationMrg.cancel(m);
				if (threadcache.containsKey(url)) {
					MyThread onethread = threadcache.get(url);
					Log.i("avi", "cancel  clickbroadcast�� thread �߳�      url: "
							+ onethread.geturl());
					onethread.setchanger(false);
					Log.i("avi",
							"cancel   clickbroadcast�� thread �߳�      url: "
									+ onethread.getchanger());
				}
			}
			
			if (action.equals("android.basic.notification.click.pause")) {
				Bundle bundle = intent.getExtras();
				int m = bundle.getInt("flag");
				String url = intent.getStringExtra("url");
				Log.i("avi", "pause  clickbroadcast��         ���յ��� flag: " + m);
				Log.i("avi", "pause  clickbroadcast��         ���յ��� url: " + url);
				// notificationMrg.cancel(m);
				if (threadcache.containsKey(url)) {
					MyThread onethread = threadcache.get(url);
					Log.i("avi", "pause   clickbroadcast�� thread �߳�      url: "
							+ onethread.geturl());
					onethread.setSuspend(true);
					// ��ǰ�߳���ͣ/ �ȴ�
				}
			}
			
			if (action.equals("android.basic.notification.click.restart")) {
				Bundle bundle = intent.getExtras();
				int m = bundle.getInt("flag");
				String url = intent.getStringExtra("url");
				Log.i("avi", "restart  clickbroadcast��         ���յ��� flag: " + m);
				Log.i("avi", "restart  clickbroadcast��         ���յ��� url: "
						+ url);
				// notificationMrg.cancel(m);
				if (threadcache.containsKey(url)) {
					MyThread onethread = threadcache.get(url);
					Log.i("avi",
							"restart   clickbroadcast�� thread �߳�      url: "
									+ onethread.geturl());
					onethread.setSuspend(false);
					// ����setSuspend(false)�õ�ǰ�ָ̻߳�/����
				}
			}
		}
	};
	

	private PendingIntent receiveMsmIntent(int flag, String url) {
		Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("url", url);
		Bundle bundle = new Bundle();
		bundle.putInt("flag", flag);
		intent.putExtras(bundle);
		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), flag, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		return contentIntent;
	}

	public class MyThread extends Thread {
		private boolean changer;
		private String url;
		private int flag;
		private boolean suspend = false;
		private String control = ""; // ֻ����Ҫһ��������ѣ��������û��ʵ������
		private Notification notification;

		public MyThread(Notification notification, String apkurl, int notifyflag) {
			super();
			this.notification = notification;
			this.url = apkurl;
			this.flag = notifyflag;
		}

		@SuppressLint("DefaultLocale") public void run() {
			System.out.println("�����еķ���");
			String url = geturl();
			int flag = getflag();
			String filename;
			int first = url.lastIndexOf("/") + 1;
			filename = url.substring(first, url.length()).toLowerCase();
			Log.i("filename", "��url�н�ȡ��filename  " + filename);
			double m = 0.0;

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response;
			try {
				response = client.execute(get);
				HttpEntity entity = response.getEntity();
				double length = entity.getContentLength();
				InputStream is = entity.getContent();
				// ʹ��InputStream���ļ����ж�ȡ�������ֽ���������
				FileOutputStream fileOutputStream = null;
				File file = null;
				Log.i("avi", "����״̬      " + getchanger());
				if (is != null && getchanger()) {
					file = new File(Environment.getExternalStorageDirectory(),
							filename);
					fileOutputStream = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int ch = -1;
					float count = 0;
					// ch�д�Ŵ�buf�ֽ������ж�ȡ�����ֽڸ���
					while ((ch = is.read(buf)) != -1 && getchanger()) {

						synchronized (control) {
							if (getSuspend()) {
								try {
									Log.i("avi", "��ͣ��������                "
											+ getSuspend());
									Intent pause =new Intent().setAction("android.basic.button.click.pause");
									pause.putExtra("url", url);
									sendBroadcast(pause);
									control.wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}

						fileOutputStream.write(buf, 0, ch);
						count += ch;
						// ���ֽ������ȡ����read(buf)�󣬷��أ���ȡ�ĸ�����count�б��棬�����ص������ֽ���
						double temp = count / length;
						if (temp >= m) {
							Log.i("TAG", "��ȡ�ֽ�ѭ���е�count" + temp);
							m += 0.1;
							int load = (int) (count * 100 / length);
							sendMsg(1, load, url, notification, flag, null,
									filename, true);
						}
						// ��������handler������Ϣ
					}
				}
				// �ļ������Ϊ�գ����ʾ������ɣ���װapk�ļ�
				Uri Url = Uri.fromFile(file);
				Log.i("TAG", "������ɣ������ļ�λ��Url  " + Url);

				sendMsg(2, 0, url, notification, 0, Url, filename, getchanger());
				fileOutputStream.flush();
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (Exception e) {
				sendMsg(-1, 0, url, notification, 0, null, filename, true);
			}

		}

		@SuppressLint("ShowToast") public void setSuspend(boolean suspend) {
			this.suspend = suspend;
			if (!suspend) {
				synchronized (control) {
					String filename=geturlfilename(url);
					Log.i("avi", filename+"��������   :   " + getSuspend());
					Toast.makeText(getApplicationContext(), filename+"��������---", 1).show();
					Intent restart=new Intent();
					restart.setAction("android.basic.button.click.restart");
					restart.putExtra("url", url);
					sendBroadcast(restart);
					control.notify();
				}
			}

		}

		public boolean getSuspend() {
			return this.suspend;
		}

		// ���ÿ���״̬
		public void setchanger(boolean changer) {
			this.changer = changer;
		}

		public boolean getchanger() {
			return this.changer;
		}

		public int getflag() {
			return this.flag;
		}

		public String geturl() {
			return this.url;
		}
		
		@SuppressLint("DefaultLocale") public String geturlfilename(String url){
			
			String filename;
			int first = url.lastIndexOf("/") + 1;
			filename = url.substring(first, url.length()).toLowerCase();
			return filename;
		}

	}

}
