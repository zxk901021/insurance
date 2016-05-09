package com.example.market.db;

import java.util.ArrayList;
import java.util.List;

import com.example.market.bean.Insurance;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class InsuranceSQLiteDatabase {

	private Context context;
	
	private static SQLiteDatabase db;
	
	private static InsuranceDatabaseHelper helper;
	
	private static final String table = "insur";
	
	
	public static void connDB() {
		if (db == null) {
			db = helper.getWritableDatabase();
		}
	}
	
	public static void closeDB(){
		if (db != null) {
			db.close();
			db = null;
		}
	}
	
	public InsuranceSQLiteDatabase(Context context){
		super();
		helper = new InsuranceDatabaseHelper(context);
		db = helper.getWritableDatabase();
	}
	
	public void addTable(Insurance insurance){
		connDB();
		try {
			db.execSQL("insert into " + table + " values (null, ?)", new Object[]{insurance.getName()});
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB();
	}
	
	public List<Insurance> query(){
		connDB();
		List<Insurance> ins = new ArrayList<Insurance>();
		Cursor c = db.rawQuery("select * from " + table, null);
		while (c.moveToNext()) {
			String name = c.getString(c.getColumnIndex("name"));
			Insurance insurance = new Insurance();
			insurance.setName(name);
			ins.add(insurance);
		}
		c.close();
		closeDB();
		return ins;
	}
	
	public boolean hasCollected(String name){
		connDB();
		boolean flag;
		Cursor c = db.rawQuery("select * from " + table + " where name = ?", new String[]{name});
		int count = c.getCount();
		if (count == 0) {
			flag = false;
		}else {
			flag = true;
		}
		c.close();
		closeDB();
		return flag;
	}
	
	public void deleteCollect(String name){
		connDB();
		try {
			db.execSQL("delete from " + table + " where name = ?", new String[]{name});
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeDB();
	}
	
}
