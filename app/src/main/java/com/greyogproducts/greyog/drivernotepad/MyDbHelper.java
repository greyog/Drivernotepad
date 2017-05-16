package com.greyogproducts.greyog.drivernotepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static android.provider.BaseColumns._ID;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_AMOUNT;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_CAR;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_DATE;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_ENTRY_ID;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_FUEL_TYPE;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_ODOMETER;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_PRICE;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_TIME;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_TOTAL_PRICE;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.ENTRIES_TABLE_NAME;

/**
 * Created by greyog on 11/05/17.
 */

class MyDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ENTRIES_TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_CAR + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_FUEL_TYPE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_ODOMETER + INT_TYPE + COMMA_SEP +
                    COLUMN_NAME_AMOUNT + INT_TYPE + COMMA_SEP +
                    COLUMN_NAME_PRICE  + INT_TYPE + COMMA_SEP +
                    COLUMN_NAME_TOTAL_PRICE + INT_TYPE + COMMA_SEP +
                    COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_TIME + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ENTRIES_TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "toplivo.db";

    private static MyDbHelper sInstance;

    static synchronized MyDbHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new MyDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: creating database "+ db.toString());
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}