package cn.com.youyouparttime.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class HttpUtil {
	public static HttpClient httpClient = new DefaultHttpClient();
	
	public static String getRequest(final String url) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						HttpGet get = new HttpGet(url);
						HttpResponse httpResponse = httpClient.execute(get);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							String result = EntityUtils.toString(httpResponse
									.getEntity());
							return result;
						}

						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}

	public static String postRequst(final String url,
			final JSONObject rawParams) throws Exception {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10*1000);
		HttpConnectionParams.setSoTimeout(httpParams, 10*1000);
		final HttpClient client = new DefaultHttpClient(httpParams);
		
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						HttpPost post = new HttpPost(url);
						
						List<NameValuePair> params = new ArrayList<NameValuePair>();
//						Log.e("raw", rawParams+"");
						params.add(new BasicNameValuePair("jsonobject",rawParams.toString()));
//						Log.e("json", rawParams.toString());
						post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
//						Log.e("entity", new UrlEncodedFormEntity(params).toString());
						
						
//						StringEntity data = new StringEntity(rawParams.toString());
////						data.setContentEncoding("UTF-8");
////						data.setContentType("application/json");
//						post.setEntity(data);
//						Log.e("length", data.getContentLength()+"");
						
						HttpResponse httpResponse = client.execute(post);
						Log.e("aaa", httpResponse.getStatusLine().getStatusCode()+"");
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							String result = EntityUtils.toString(httpResponse
									.getEntity());
							Log.e("111", new JSONObject(result).toString());
							return result;
						}
						Log.e("aaa", httpResponse.getStatusLine().getStatusCode()+"");
						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}
	
	
	public static String postRequst(final String url,
			final JSONObject rawParams,final ProgressDialog dialog) throws Exception {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5*1000);
		HttpConnectionParams.setSoTimeout(httpParams, 5*1000);
		final HttpClient client = new DefaultHttpClient(httpParams);
		CommonUtil.showDialog(dialog);
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						HttpPost post = new HttpPost(url);
						
						List<NameValuePair> params = new ArrayList<NameValuePair>();
//						Log.e("raw", rawParams+"");
						params.add(new BasicNameValuePair("jsonobject",rawParams.toString()));
//						Log.e("json", rawParams.toString());
						post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
//						Log.e("entity", new UrlEncodedFormEntity(params).toString());
						
						
//						StringEntity data = new StringEntity(rawParams.toString());
////						data.setContentEncoding("UTF-8");
////						data.setContentType("application/json");
//						post.setEntity(data);
//						Log.e("length", data.getContentLength()+"");
						
						HttpResponse httpResponse = client.execute(post);
						Log.e("aaa", httpResponse.getStatusLine().getStatusCode()+"");
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							CommonUtil.dismissDialog(dialog);
							String result = EntityUtils.toString(httpResponse
									.getEntity());
							Log.e("111", new JSONObject(result).toString());
							return result;
						}
						Log.e("aaa", httpResponse.getStatusLine().getStatusCode()+"");
						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}
	
	
	
	
	public static String post(final String url)throws Exception{
		
		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
			@Override
			public String call() throws Exception {
				HttpPost post = new HttpPost(url);
//				StringEntity entity = new StringEntity(raw.toString());
//				post.setEntity(entity);
				HttpResponse response = new DefaultHttpClient().execute(post);
				if (response.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(response.getEntity());
					return result;
				}
				return null;
				
				
			}
		});
		new Thread(task).start();
		return task.get();
	}
	
	public static void volleyPost(Context context, String url) throws Exception{
		RequestQueue queue = Volley.newRequestQueue(context);
		String result;
		JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
//				result = response.toString();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				
			}
		});
//		return result;
	}
}
