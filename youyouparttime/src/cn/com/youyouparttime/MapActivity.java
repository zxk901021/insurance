package cn.com.youyouparttime;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.BusRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.util.AMapUtil;
import cn.com.youyouparttime.util.ToastUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MapActivity extends Activity implements OnRouteSearchListener,
		AMapLocationListener, LocationSource, OnGeocodeSearchListener {

	private MapView mapView;
	private AMap aMap;
	private RouteSearch search;
	private FromAndTo fromAndTo;
	private LocationManagerProxy mLocationManagerProxy;
	private OnLocationChangedListener mListener;
	LatLonPoint startLatLonPoint, endLatLonPoint;
	private BusRouteResult busRouteResult;
	private GeocodeSearch geocoderSearch;
	private Marker geoMarker;
	private String addressName;
	private TextView back;
	private ProgressDialog progDialog = null;
	private String address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		address = intent.getStringExtra("address");
		mapView = (MapView) findViewById(R.id.map);
		back = (TextView) findViewById(R.id.map_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mapView.onCreate(savedInstanceState);
		init();
	}
	
	

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 5 * 1000, 15, this);
		mLocationManagerProxy.setGpsEnable(false);
		progDialog = new ProgressDialog(this);
		// fromAndTo = new FromAndTo(arg0, arg1)
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		search = new RouteSearch(this);
		search.setRouteSearchListener(this);
		getLatlon(address);

	}

	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
		// 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	public void getLatlon(final String name) {
		Log.e("MapActivity", "getLatlon");

		GeocodeQuery query = new GeocodeQuery(name, "022");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {
		Log.e("MapActivity", "onBusRouteSearched");
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				busRouteResult = result;
				BusPath busPath = busRouteResult.getPaths().get(0);
				aMap.clear();// 清理之前的图标
				BusRouteOverlay routeOverlay = new BusRouteOverlay(this, aMap,
						busPath, busRouteResult.getStartPos(),
						busRouteResult.getTargetPos());
				routeOverlay.removeFromMap();
				routeOverlay.addToMap();
				routeOverlay.zoomToSpan();
			}
		}

		// fromAndTo = new FromAndTo(startLatLonPoint, endLatLonPoint);
		// Log.e("latlonpoint", startLatLonPoint + "'" + endLatLonPoint);
		// BusRouteQuery query = new BusRouteQuery(fromAndTo,
		// RouteSearch.BusLeaseWalk, "022", 0);
		// search.calculateBusRouteAsyn(query);
	}

	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}
	
	private void stopLocation() {
	    if (mLocationManagerProxy != null) {
	    	mLocationManagerProxy.removeUpdates(this);
	    	mLocationManagerProxy.destory();
	    }
	    mLocationManagerProxy = null;
	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		Log.e("MapActivity", "onLocationChanged");
		dismissDialog();
		if (arg0 != null && arg0.getAMapException().getErrorCode() == 0) {
			// 获取位置信息
			Double geoLat = arg0.getLatitude();
			Double geoLng = arg0.getLongitude();
			startLatLonPoint = new LatLonPoint(geoLat, geoLng);
			Log.e("startLatLonPoint", startLatLonPoint.toString());
			Log.e("location", geoLat + "'" + geoLng);
			mListener.onLocationChanged(arg0);

		}
	}
	

	@Override
	public void activate(OnLocationChangedListener listener) {
		Log.e("MapActivity", "activate");
		showDialog();
		mListener = listener;
		if (mLocationManagerProxy == null) {
			mLocationManagerProxy = LocationManagerProxy.getInstance(this);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用destroy()方法
			// 其中如果间隔时间为-1，则定位只定一次
			mLocationManagerProxy.requestLocationData(
					LocationProviderProxy.AMapNetwork, 5 * 1000, 10, this);
		}

	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.removeUpdates(this);
			mLocationManagerProxy.destroy();
		}
		mLocationManagerProxy = null;
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		Log.e("MapActivity", "onGeocodeSearched");
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
				geoMarker.setPosition(AMapUtil.convertToLatLng(address
						.getLatLonPoint()));
				endLatLonPoint = address.getLatLonPoint();
				addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
						+ address.getFormatAddress();
				ToastUtil.show(MapActivity.this, addressName);
			} else {
				ToastUtil.show(MapActivity.this, "对不起，没有搜索到相关数据！");
			}
		} else if (rCode == 27) {
			ToastUtil.show(MapActivity.this, "搜索失败,请检查网络连接！");
		} else if (rCode == 32) {
			ToastUtil.show(MapActivity.this, "key验证无效！");
		} else {
			ToastUtil.show(MapActivity.this, "未知错误，请稍后重试!错误码为" + rCode);
		}

		fromAndTo = new FromAndTo(startLatLonPoint, endLatLonPoint);
		Log.e("latlonpoint", startLatLonPoint + "'" + endLatLonPoint);
		BusRouteQuery query = new BusRouteQuery(fromAndTo,
				RouteSearch.BusLeaseWalk, "022", 0);
		search.calculateBusRouteAsyn(query);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		stopLocation();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {

	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult arg0, int arg1) {

	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult arg0, int arg1) {

	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
}
