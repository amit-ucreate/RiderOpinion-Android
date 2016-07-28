package com.nutsuser.ridersdomain.database;

/**
 * author: amit agnihotri
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	/**
	 * All Static variables Database Version
	 */

	public static final int DATABASE_VERSION = 1;

	// Database Name
	public static final String DATABASE_NAME = "rideropinion.db";

	// Filter Riding Destination table name
	public static final String TABLE_FILTERRIDINGDESTINATION = "_filterridingdestination";

	// all Filter Riding Destination Table Columns names
	public static final String KEY_DISTANCE = "_distance";
	public static final String KEY_USERID_ = "_userid";
	public static final String KEY_TYPE = "_type";
	public static final String KEY_TYPEVALUE = "_typevalue";


	public static String QUERYFILTERRIDINGDESTINATION = "create table "
			+ TABLE_FILTERRIDINGDESTINATION + " ( " + KEY_DISTANCE + " TEXT, "
			+ KEY_TYPE + " TEXT, "+KEY_TYPEVALUE + " TEXT, "+ KEY_USERID_ + "  TEXT)";


	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			Log.e("", "CALLING");

			db.execSQL(QUERYFILTERRIDINGDESTINATION);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Table Exception", e.getMessage());
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("DROP TABLE IF EXISTS " + QUERYFILTERRIDINGDESTINATION);

	}

}
