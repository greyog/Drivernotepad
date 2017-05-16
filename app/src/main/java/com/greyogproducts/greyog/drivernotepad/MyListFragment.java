package com.greyogproducts.greyog.drivernotepad;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

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
 * Created by greyog on 3/05/17.
 */

public class MyListFragment extends ListFragment {

    private static MyListFragment sInstance;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private SimpleCursorAdapter cursorAdapter;
    private Cursor cursor;
    private MyDbHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    public void onDestroy() {
        cursor.close();
        super.onDestroy();
    }

    public void notifyChanged() {
        if (cursor != null) {
            cursor.close();
        }
        cursor = getQuery(COLUMN_NAME_ODOMETER);
        cursorAdapter.changeCursor(cursor);
        cursorAdapter.notifyDataSetChanged();
    }

    public static ListFragment getInstance(int sectionNumber) {
        if (sInstance == null ) {
            sInstance = new MyListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            sInstance.setArguments(args);
        }
        return sInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDbHelper = MyDbHelper.getInstance(getContext());
        db = mDbHelper.getReadableDatabase();

        cursor = getQuery(COLUMN_NAME_ODOMETER);
        String[] from = {COLUMN_NAME_ENTRY_ID,
                COLUMN_NAME_CAR,
                COLUMN_NAME_FUEL_TYPE,
                COLUMN_NAME_ODOMETER,
                COLUMN_NAME_AMOUNT,
                COLUMN_NAME_PRICE,
                COLUMN_NAME_TOTAL_PRICE,
                COLUMN_NAME_DATE,
                COLUMN_NAME_TIME};
        int[] to = {R.id.tvListEntryID,
                R.id.txListEntryCar,
                R.id.tvListEntryFuelType,
                R.id.tvListEntryOdometer,
                R.id.tvListEntryAmount,
                R.id.tvListEntryPrice,
                R.id.tvListEntryTotalPrice,
                R.id.tvListEntryDate,
                R.id.tvListEntryTime
        };
        cursorAdapter = new SimpleCursorAdapter(getContext()
                , R.layout.entry_list_item_layout
                , cursor
                , from
                , to
        );
        setListAdapter(cursorAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long rowId) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                        .setItems(R.array.edit_entry_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int menuItemIndex) {
                                boolean clicked = false;
                                Log.d(MainActivity.TAG, "onLongClick: "+((TextView) view.findViewById(R.id.tvListEntryID)).getText().toString());
                                switch (menuItemIndex) {
                                    case 0: // launch edit entry fragment
                                        Bundle bundle = new Bundle();
                                        bundle.putString(COLUMN_NAME_ENTRY_ID,((TextView) view.findViewById(R.id.tvListEntryID)).getText().toString());
                                        bundle.putString(COLUMN_NAME_ODOMETER,((TextView) view.findViewById(R.id.tvListEntryOdometer)).getText().toString());
                                        bundle.putString(COLUMN_NAME_AMOUNT,((TextView) view.findViewById(R.id.tvListEntryAmount)).getText().toString());
                                        bundle.putString(COLUMN_NAME_PRICE,((TextView) view.findViewById(R.id.tvListEntryPrice)).getText().toString());
                                        bundle.putString(COLUMN_NAME_TOTAL_PRICE,((TextView) view.findViewById(R.id.tvListEntryTotalPrice)).getText().toString());
                                        bundle.putString(COLUMN_NAME_DATE,((TextView) view.findViewById(R.id.tvListEntryDate)).getText().toString());
                                        bundle.putString(COLUMN_NAME_TIME,((TextView) view.findViewById(R.id.tvListEntryTime)).getText().toString());
                                        EntryFragment.getInstance(((MainActivity) getActivity()),bundle).show(getFragmentManager(),null);
                                        clicked =true;
                                        break;
                                    case 1: //delete entry
                                        db.delete(ENTRIES_TABLE_NAME,
                                                COLUMN_NAME_ENTRY_ID + " = "+((TextView) view.findViewById(R.id.tvListEntryID)).getText().toString(),
                                                null);
                                        clicked = true;
                                        break;
                                }
                                if (clicked) {
                                    notifyChanged();
                                }

                            }
                        });
                adb.create().show();
                return false;
            }
        });
    }

    Cursor getQuery(String sortColumnName) {
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                _ID,
                COLUMN_NAME_ENTRY_ID,
                COLUMN_NAME_CAR,
                COLUMN_NAME_FUEL_TYPE,
                COLUMN_NAME_ODOMETER,
                COLUMN_NAME_AMOUNT,
                COLUMN_NAME_PRICE,
                COLUMN_NAME_TOTAL_PRICE,
                COLUMN_NAME_DATE,
                COLUMN_NAME_TIME
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                sortColumnName + " ASC";

        return db.query(
                ENTRIES_TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
    }

    public static MyListFragment getInstance() {
        return sInstance;
    }
}
