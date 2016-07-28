package com.nutsuser.ridersdomain.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatasourceHandler {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;


    String all_column[] = {DatabaseHelper.KEY_DISTANCE,
            DatabaseHelper.KEY_TYPE, DatabaseHelper.KEY_TYPEVALUE,
            DatabaseHelper.KEY_USERID_};


    public DatasourceHandler(Context context) {
        try {
            dbHelper = new DatabaseHelper(context);
            database = dbHelper.getWritableDatabase();
            // Log.e("ddddddddddddddddd", "Data Base and table created");
        } catch (Exception e) {
            // TODO: handle exception
            // Log.e("eeeeeeeeeeeeeeeee", e.getMessage());
        }

    }

    public void close() {
        dbHelper.close();
    }


    public boolean updateTypeFilter(String type,String value,String dis, String id) {
        boolean flag = false;
        Log.e("type : ",""+type);
        Log.e("value : ",""+value);
        try {
            Cursor mCursor = database.query(DatabaseHelper.TABLE_FILTERRIDINGDESTINATION, all_column, "_type= ?", new String[]{type}, null, null, null, null);
            Log.e("Count() Filter: ",""+mCursor.getCount());
             if (mCursor.getCount() != 0) {
                do {
                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseHelper.KEY_TYPE, type);
                    cv.put(DatabaseHelper.KEY_TYPEVALUE, value);
                    long row = database.update(
                            DatabaseHelper.TABLE_FILTERRIDINGDESTINATION, cv, "_type"
                                    + "='" + type + "'", null);
                    Log.e("row type filter: ",""+row);
                    if (row > 0) {
                        flag = true;
                    }

                } while (mCursor.moveToNext());

                mCursor.close();

            }

        } catch (Exception e) {
            Log.e("exception:",""+e.getMessage());

        }
        return flag;
    }
    public boolean updateDistance(String type,String value) {
        boolean flag = false;
        try {

            //Cursor mCursor = database.query(DatabaseHelper.TABLE_FILTERRIDINGDESTINATION, all_column, "_userid= ?", new String[]{type}, null, null, null, null);

            Log.e("type:",""+type);
            Log.e("value:",""+value);
            Cursor mCursor = database.query(DatabaseHelper.TABLE_FILTERRIDINGDESTINATION, all_column, DatabaseHelper.KEY_USERID_+"= ?", new String[]{type}, null, null, null, null);


            Log.e("mCursor.getCount(): ",""+mCursor.getCount());
            if (mCursor.getCount() != 0) {
                do {
                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseHelper.KEY_DISTANCE, value);
                    cv.put(DatabaseHelper.KEY_USERID_, type);
                    long row = database.update(
                            DatabaseHelper.TABLE_FILTERRIDINGDESTINATION, cv, "_userid"

                                    + " = '" + type + "'", null);



                    Log.e("row distance: ",""+row);
                    if (row > 0) {
                        flag = true;
                    }

                } while (mCursor.moveToNext());

                mCursor.close();

            }

        } catch (Exception e) {

        }
        return flag;
    }



    public boolean updateFilter(String distance, String type, String userId,String value) {
        boolean flag = false;
        try {
            Cursor mCursor = database.query(DatabaseHelper.TABLE_FILTERRIDINGDESTINATION, all_column, "_type= ?", new String[]{type}, null, null, null, null);
            Log.e("Count()updateFilter ",""+mCursor.getCount());
            if (mCursor.getCount() != 0) {
                do {
                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseHelper.KEY_DISTANCE, distance);
                    cv.put(DatabaseHelper.KEY_TYPE, type);
                    cv.put(DatabaseHelper.KEY_TYPEVALUE, value);
                    cv.put(DatabaseHelper.KEY_USERID_, userId);

                    long row = database.update(
                            DatabaseHelper.TABLE_FILTERRIDINGDESTINATION, cv, "_type"
                                    + "='" + type + "'", null);
                    Log.e("row filter: ",""+row);
                    if (row > 0) {
                        flag = true;
                    }

                } while (mCursor.moveToNext());

                mCursor.close();

            }
            else{
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.KEY_DISTANCE, distance);
                cv.put(DatabaseHelper.KEY_TYPE, type);
                cv.put(DatabaseHelper.KEY_TYPEVALUE, value);
                cv.put(DatabaseHelper.KEY_USERID_, userId);

                long row = database.insert(DatabaseHelper.TABLE_FILTERRIDINGDESTINATION,
                        null, cv);
                if (row > 0) {
                    flag = true;
                }
            }
        } catch (Exception e) {

        }
        return flag;
    }



    // fetch only Profile---------
    public Cursor FilterInfo() throws SQLException {
        Cursor mCursor = database.query(DatabaseHelper.TABLE_FILTERRIDINGDESTINATION,
                all_column, null, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }


}