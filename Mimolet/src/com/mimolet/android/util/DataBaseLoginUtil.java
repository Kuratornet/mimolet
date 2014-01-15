package com.mimolet.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseLoginUtil extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "mimolet_logdate.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "logintable";
	public static final String UID = "_id";
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ LOGIN + " VARCHAR(255)," + PASSWORD + " VARCHAR(255));";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	public DataBaseLoginUtil(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

}
