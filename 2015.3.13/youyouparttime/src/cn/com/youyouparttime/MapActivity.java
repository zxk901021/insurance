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
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		// ���ö�λ������Ϊ��λģʽ����λ��AMap.LOCATION_TYPE_LOCATE�������棨AMap.LOCATION_TYPE_MAP_FOLLOW��
		// ��ͼ������������ת��AMap.LOCATION_TYPE_MAP_ROTATE������ģʽ
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	public void getLatlon(final String name) {
		Log.e("MapActivity", "getLatlon");

		GeocodeQuery query = new GeocodeQuery(name, "022");// ��һ��������ʾ��ַ���ڶ���������ʾ��ѯ���У����Ļ�������ȫƴ��citycode��adcode��
		geocoderSearch.getFromLocationNameAsyn(query);// ����ͬ�������������
	}

	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {
		Log.e("MapActivity", "onBusRouteSearched");
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				busRouteResult = result;
				BusPath busPath = busRouteResult.getPaths().get(0);
				aMap.clear();// ����֮ǰ��ͼ��
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
		progDialog.setMessage("���ڻ�ȡ��ַ");
		progDialog.show();
	}

	/**
	 * ���ؽ������Ի���
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
			// ��ȡλ����Ϣ
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
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ���������ں���ʱ�����removeUpdates()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
			// ����������ʱ��Ϊ-1����λֻ��һ��
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
				addressName = "��γ��ֵ:" + address.getLatLonPoint() + "\nλ������:"
						+ address.getFormatAddress();
				ToastUtil.show(MapActivity.this, addressName);
			} else {
				ToastUtil.show(MapActivity.this, "�Բ���û��������������ݣ�");
			}
		} else if (rCode == 27) {
			ToastUtil.show(MapActivity.this, "����ʧ��,�����������ӣ�");
		} else if (rCode == 32) {
			ToastUtil.show(MapActivity.this, "key��֤��Ч��");
		} else {
			ToastUtil.show(MapActivity.this, "δ֪�������Ժ�����!������Ϊ" + rCode);
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
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
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
