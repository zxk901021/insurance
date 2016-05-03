package cn.com.youyouparttime.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import cn.com.youyouparttime.LoginForStudentsActivity;
import cn.com.youyouparttime.R;
import cn.com.youyouparttime.entity.AreaEntity;
import cn.com.youyouparttime.entity.PartTimeInfo;
import cn.com.youyouparttime.entity.PartTimeType;
import cn.com.youyouparttime.entity.UrlUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class DialogUtil {

	public static void healthDialog(final Activity activity,
			final DialogListener listener) {
		final AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.show();
		dialog.getWindow().setContentView(R.layout.health_dialog);
		TextView has = (TextView) dialog.findViewById(R.id.has);
		TextView hasnot = (TextView) dialog.findViewById(R.id.hasnot);
		has.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String result = "有";
				listener.refreshUI(result);
				dialog.dismiss();
			}
		});
		hasnot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String result = "无";
				listener.refreshUI(result);
				dialog.dismiss();
			}
		});
	}

	public static void showDialog(final Activity activity,
			final DialogListener listener) {
		final AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.show();
		dialog.getWindow().setContentView(R.layout.dialog_layout);
		TextView chooseLocal = (TextView) dialog
				.findViewById(R.id.alert_choose_local);
		TextView photo = (TextView) dialog.findViewById(R.id.photo);
		TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
		chooseLocal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// Intent intent = new Intent(Intent.ACTION_PICK);
				// intent.addCategory(Intent.CATEGORY_OPENABLE);
				// intent.setType("image/*");
				// intent.putExtra("crop", "true");
				// intent.putExtra("aspectX", 1);
				// intent.putExtra("aspectY", 1);
				// intent.putExtra("outputX", 128);
				// intent.putExtra("outputY", 128);
				// intent.putExtra("return-data", true);
				// Log.e("activity", activity.toString());
				// activity.startActivityForResult(intent, 1);
				// // activity.setResult(1, intent);
				String string = "1";
				listener.refreshUI(string);
			}
		});
		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// activity.startActivityForResult(intent, 2);
				// // activity.setResult(2, intent);
				String string = "2";
				listener.refreshUI(string);

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	public static void contactDialog(final Activity activity,
			final String jobid, final String cid, final String uid,
			final String linkPhone, final String name, final String school,
			final String sex, final String title) {
		final AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.contact_dialog);
		window.setGravity(Gravity.BOTTOM);
		TextView ems = (TextView) dialog.findViewById(R.id.contact_ems);
		TextView phone = (TextView) dialog.findViewById(R.id.contact_phone);
		TextView cancel = (TextView) dialog.findViewById(R.id.contact_cancel);
		ems.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyRecord(uid, cid, jobid);
				Uri uri = Uri.parse("smsto:" + linkPhone);
				Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
				intent.putExtra("sms_body", "您好，我是" + school + "的" + name + "，"
						+ sex + "，我在悠兼职上看到您发布的招聘信息，我想应聘“ " + title
						+ "”，希望您能给我这个机会，我一定会努力做好这份工作。");
				activity.startActivity(intent);
			}
		});
		phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				applyRecord(uid, cid, jobid);
				Uri uri = Uri.parse("tel:" + linkPhone);
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				activity.startActivity(intent);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	public static void complainDialog(final Activity activity,
			final int typeCode, final PartTimeInfo info, final String uid,
			final String usertype) {
		final AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.complain_dialog);
		window.setGravity(Gravity.BOTTOM);

		final TextView reason1 = (TextView) dialog
				.findViewById(R.id.complain_reason1);
		final TextView reason2 = (TextView) dialog
				.findViewById(R.id.complain_reason2);
		final TextView reason3 = (TextView) dialog
				.findViewById(R.id.complain_reason3);
		final TextView reason4 = (TextView) dialog
				.findViewById(R.id.complain_reason4);
		reason1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = reason1.getText().toString();
				CommonUtil.gotoComment(typeCode, info, activity, uid, usertype,
						text);
				dialog.dismiss();
			}
		});
		reason2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = reason2.getText().toString();
				CommonUtil.gotoComment(typeCode, info, activity, uid, usertype,
						text);
				dialog.dismiss();
			}
		});
		reason3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = reason3.getText().toString();
				CommonUtil.gotoComment(typeCode, info, activity, uid, usertype,
						text);
				dialog.dismiss();
			}
		});
		reason4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = "";
				CommonUtil.gotoComment(typeCode, info, activity, uid, usertype,
						text);
				dialog.dismiss();
			}
		});

		TextView cancel = (TextView) dialog.findViewById(R.id.complain_cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// resume.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });
		// ems.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Uri uri = Uri.parse("smsto:"+linkPhone);
		// Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		// intent.putExtra("sms_body", "");
		// activity.startActivity(intent);
		// }
		// });
		// phone.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Uri uri = Uri.parse("tel:"+linkPhone);
		// Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		// activity.startActivity(intent);
		// }
		// });
		// cancel.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// }
		// });
	}

	public static void intentDialog(final Context context,
			final DialogListener listener) {
		final List<PartTimeType> list = getList();
		List<String> name = new ArrayList<String>();
		final boolean[] flag = new boolean[list.size()];
		for (int i = 0; i < list.size(); i++) {
			name.add(list.get(i).getTypeName());
		}
		final String[] a = (String[]) name.toArray(new String[name.size()]);
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setMultiChoiceItems(a, flag, new OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						flag[which] = isChecked;
						list.get(which).setChoosed(isChecked);
					}
				})
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						StringBuffer buffer = new StringBuffer();
						StringBuffer keyBuffer = new StringBuffer();
						String result = null;
						String key = null;
						for (int j = 0; j < flag.length; j++) {
							if (flag[j]) {
								buffer.append(a[j] + ",");
								keyBuffer.append(list.get(j).getId() + ",");
								if (buffer == null || keyBuffer == null) {
									Toast.makeText(context, "请选择了再提交",
											Toast.LENGTH_SHORT).show();
									return;
								}
								result = buffer.toString().substring(0,
										buffer.length() - 1);
								key = keyBuffer.toString().substring(0,
										keyBuffer.length() - 1);
								if (listener != null) {
									listener.refreshUI(result, key);
								}
							}
						}
					}
				}).create();
		dialog.show();
	}

	public static void areaDialog(final Context context,
			final DialogListener listener) {
		final List<AreaEntity> list = getArea();
		List<String> name = new ArrayList<String>();
		final boolean[] flag = new boolean[list.size()];
		for (int i = 0; i < list.size(); i++) {
			name.add(list.get(i).getArea());
		}
		final String[] a = (String[]) name.toArray(new String[name.size()]);
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setMultiChoiceItems(a, flag, new OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						flag[which] = isChecked;
						list.get(which).setChoosed(isChecked);
					}
				})
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						StringBuffer buffer = new StringBuffer();
						StringBuffer keyBuffer = new StringBuffer();
						String result = null;
						String key = null;
						for (int j = 0; j < flag.length; j++) {
							if (flag[j]) {
								buffer.append(a[j] + ",");
								keyBuffer.append(list.get(j).getAreaId() + ",");
								if (buffer == null || keyBuffer == null) {
									Toast.makeText(context, "请选择了再提交",
											Toast.LENGTH_SHORT).show();
									return;
								}
								result = buffer.toString().substring(0,
										buffer.length() - 1);
								key = keyBuffer.toString().substring(0,
										keyBuffer.length() - 1);
								if (listener != null) {
									listener.refreshUI(result, key);
								}
							}
						}
					}
				}).create();
		dialog.show();
	}

//	public static List<AreaEntity> getArea() {
//		JSONObject jsonObject = new JSONObject();
//		JSONObject resultJson;
//		List<AreaEntity> list = new ArrayList<AreaEntity>();
//
//		try {
//			jsonObject.put("id", "38");
//			String result = HttpUtil.postRequst(UrlUtil.JOB_CITY_URL,
//					jsonObject);
//			resultJson = new JSONObject(result);
//			JSONObject city = resultJson.getJSONObject("city");
//			@SuppressWarnings("rawtypes")
//			Iterator it = city.keys();
//			while (it.hasNext()) {
//				AreaEntity entity = new AreaEntity();
//				String key = String.valueOf(it.next());
//				String value = city.getString(key);
//				entity.setArea(value);
//				entity.setAreaId(key);
//				list.add(entity);
//			}
//
//			return list;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
	
	public static List<AreaEntity> getArea(){
		JSONObject jsonObject = new JSONObject();
		JSONObject resultJson;
		List<AreaEntity> list = new ArrayList<AreaEntity>();
		try {
			jsonObject.put("id", "38");
			String result = HttpUtil.postRequst(UrlUtil.JOB_CITY_URL,
					jsonObject);
			resultJson = new JSONObject(result);
			JSONArray city = resultJson.getJSONArray("city");
			for (int i = 0; i < city.length(); i++) {
				AreaEntity entity = new AreaEntity();
				String key = city.getJSONObject(i).getString("id");
				String value = city.getJSONObject(i).getString("name");
				String sortStr = city.getJSONObject(i).getString("sort");
				int sort = Integer.parseInt(sortStr);
				entity.setArea(value);
				entity.setAreaId(key);
				entity.setSort(sort);
				list.add(entity);
				Collections.sort(list);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	

	public static void setAreaDialog(Context context,
			final DialogListener listener) {
		final Map<String, String> map = CommonUtil.getData(2, "province");
		List<String> list = new ArrayList<String>(map.values());
		final String[] province = (String[]) list.toArray(new String[list
				.size()]);
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("请选择")
				.setItems(province, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String result = province[which];
						String key = CommonUtil.getKey(map, result).get(0);
						if (listener != null) {
							listener.refreshUI(result, key);
						}
					}
				}).create();
		dialog.show();
	}

	public static void setCityDialog(Context context,
			final DialogListener listener, String url, String key) {
		final Map<String, String> map = CommonUtil.queryCity(url, key);
		List<String> list = new ArrayList<String>(map.values());
		final String[] city = (String[]) list.toArray(new String[list.size()]);
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("请选择")
				.setItems(city, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String result = city[which];
						String key = CommonUtil.getKey(map, result).get(0);
						if (listener != null) {
							listener.refreshUI(result, key);
						}
					}
				}).create();
		dialog.show();
	}

	public static List<PartTimeType> getList() {
		List<PartTimeType> list = new ArrayList<PartTimeType>();

		try {
			String result = HttpUtil.post(UrlUtil.JOB_TYPE_URL);
			JSONObject object = new JSONObject(result);
			Log.e("object", object.toString());
			if (object != null) {
				JSONArray resultJson = object.getJSONArray("jobclass");
				for (int i = 0; i < resultJson.length(); i++) {
					PartTimeType type = new PartTimeType();
					String name = resultJson.getJSONObject(i).getString("name");
					String id = resultJson.getJSONObject(i).getString("id");
					String sortStr = resultJson.getJSONObject(i).getString("sort");
					int sort = Integer.parseInt(sortStr);
					type.setId(id);
					type.setSort(sort);
					type.setTypeName(name);
					list.add(type);
					Collections.sort(list);
				}
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static void dateDialog(final Context context,
			final DialogListener listener) {

		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.date_dialog);
		final StringBuffer buffer = new StringBuffer();
		final DatePicker picker = (DatePicker) alertDialog
				.findViewById(R.id.date_picker);

		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		picker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), null);
		Button ensure = (Button) alertDialog.findViewById(R.id.date_ensure);
		ensure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				buffer.append(String.format("%d-%02d-%02d", picker.getYear(),
						picker.getMonth() + 1, picker.getDayOfMonth()));
				String result = buffer.toString();
				if (listener != null) {
					listener.refreshUI(result);
				}
				alertDialog.dismiss();
			}
		});

	}

	public static void ifLoginDialog(final Context context) {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.getWindow().setContentView(R.layout.login_dialog);
		TextView login = (TextView) dialog
				.findViewById(R.id.login_dialog_login);
		TextView cancel = (TextView) dialog
				.findViewById(R.id.login_dialog_cancel);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						LoginForStudentsActivity.class);
				context.startActivity(intent);
				dialog.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	
	public static void ensureSubmitDialog(final Context context, final DialogListener listener){
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.getWindow().setContentView(R.layout.ensure_submit_dialog);
		TextView ensure = (TextView) dialog.findViewById(R.id.ensure_submit);
		TextView cancel = (TextView) dialog.findViewById(R.id.cancel_submit);
		ensure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.refreshUI("1");
				}
				dialog.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.refreshUI("2");
				}
				dialog.dismiss();
			}
		});
	}

	public static void setImageProperty(Context context,
			final DialogListener listener) {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.getWindow().setContentView(R.layout.image_property);
		TextView set = (TextView) dialog.findViewById(R.id.set_portrait);
		TextView delete = (TextView) dialog.findViewById(R.id.delete_portrait);
		set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (listener != null) {
					listener.refreshUI("1");
				}
				dialog.dismiss();
			}
		});
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (listener != null) {
					listener.refreshUI("2");
				}
				dialog.dismiss();
			}
		});
	}

	public static void uploadApp(final Context context, final String url) {
		new AlertDialog.Builder(context)
				.setTitle("更新信息")
				.setMessage("有新版本更新，是否下载？")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (url != null & !url.equals("")) {
							Intent intent = new Intent(
									context,
									cn.com.youyouparttime.service.DownloadService.class);
							intent.putExtra("url", url);
							intent.setAction("cn.com.youyouparttime.service.DownloadService");
							context.startService(intent);
						}
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}

	public static void applyRecord(String uid, String cid, String jobid) {
		JSONObject object = new JSONObject();
		try {
			object.put("uid", uid);
			object.put("cid", cid);
			object.put("jobid", jobid);
			HttpUtil.postRequst(UrlUtil.APPLY_RECORD_URL, object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static interface DialogListener {
		public void refreshUI(String string);

		public void refreshUI(String string, String key);
	}
}
