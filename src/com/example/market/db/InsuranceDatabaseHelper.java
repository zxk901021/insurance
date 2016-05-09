package com.example.market.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InsuranceDatabaseHelper extends SQLiteOpenHelper {

	private static final String name = "insurance.db";
	
	private static final int version = 1;
	
	public InsuranceDatabaseHelper(Context context){
		super(context, name, null, version);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists insur (id integer primary key autoincrement, name varchat, unique(name))" );

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

	}

}
