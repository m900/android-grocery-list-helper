package com.luisromero.listmenu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DbHelper - creates the database(s) needed 
 * 
 * @author Luis Romero
 * 
 
 * @param SQLite db - creates tables to insert data on database.
 *
 * 
 **/

public class DbHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME= "list";
	private static final int DATABASE_SCHEMA=1;
	public static final String _ID="_id";
	public static final String PRODUCT="product";
	public static final String LOCATION="location";
	public static final String QUANTITY="quantity";
	public static final String C_LAT="c_lat";
	public static final String C_LON="c_lon";
	public static final String IN_STOCK="in_stock"; //0 for FALSE , 1 for TRUE
	
	public DbHelper(Context context){
		super(context,DATABASE_NAME,null,DATABASE_SCHEMA);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS items (_id INTEGER PRIMARY KEY AUTOINCREMENT, product VARCHAR(35), quantity INTEGER, location VARCHAR(35));");
		//db.execSQL("CREATE TABLE location (_id INTEGER PRIMARY KEY AUTOINCREMENT, location VARCHAR(40), c_lon INTEGER, c_lat INTEGER);");
		//db.execSQL("CREATE TABLE locationStock(_id INTEGER, product VARCHAR(35), quantity INTEGER, inStock INTEGER);");
		Log.d("DbHelper","onCreate - table name created");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS items");
		//db.execSQL("DROP TABLE IF EXISTS location");
		//db.execSQL("DROP TABLE IF EXISTS locationStock");
		Log.d("DbHelper","onUpgrade - drop table");
		this.onCreate(db);
	}
}
