package com.greyogproducts.greyog.drivernotepad;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;

import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_AMOUNT;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_DATE;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_ENTRY_ID;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_ODOMETER;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_PRICE;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_TIME;
import static com.greyogproducts.greyog.drivernotepad.FuelReaderContract.fuelEntry.COLUMN_NAME_TOTAL_PRICE;
import static com.greyogproducts.greyog.drivernotepad.MainActivity.TAG;

/**
 * Created by greyog on 11/05/17.
 */

public class EntryFragment extends DialogFragment implements DialogInterface.OnClickListener, DatePickerFragment.DatePickerListener, TimePickerFragment.TimePickerListener {

    private static Bundle mBundle;
    private EditText etOdometer;
    private EditText etAmount;
    private EditText etPrice;
    private EditText etTotalPrice;
    private long timeStamp;

    public interface EntryFragmentListener {
        void onEntrySaveClick(ContentValues values);
    }

    private static EntryFragment sInstance;
    private Button btDate;
    private Button btTime;
    private EntryFragmentListener mListener;

    private int odometer = 0;
    private int amount = 0;
    private int price = 0;
    private int totalPrice = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.entryFragmentTitle)
                .setPositiveButton(R.string.saveEntry, this)
                .setNegativeButton(R.string.cancelEntry, this);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.entry_fragment_layout, null);
        btDate = (Button) view.findViewById(R.id.entryDate);
        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment.getInstance(EntryFragment.this).show(getFragmentManager(), "");
            }
        });
        btDate.setText(getDateString(System.currentTimeMillis()));

        btTime = (Button) view.findViewById(R.id.entryTime);
        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment.getInstance(EntryFragment.this).show(getFragmentManager(), "");
            }
        });
        btTime.setText(getTimeString(System.currentTimeMillis()));

        etOdometer = (EditText) view.findViewById(R.id.entryOdometer);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                reCalcValues();
            }
        };
        etOdometer.addTextChangedListener(watcher);
        etAmount = (EditText) view.findViewById(R.id.entryAmount);
        etAmount.addTextChangedListener(watcher);
        etPrice = (EditText) view.findViewById(R.id.entryPrice);
        etPrice.addTextChangedListener(watcher);
        etTotalPrice = (EditText) view.findViewById(R.id.entryTotalPrice);
        if (mBundle == null) {
            timeStamp = System.currentTimeMillis();
            Cursor cursor = MyListFragment.getInstance().getQuery(COLUMN_NAME_ODOMETER);
            cursor.moveToLast();
            mBundle = new Bundle();
            for (int i = 0; i < cursor.getColumnNames().length; i++) {
                mBundle.putString(cursor.getColumnName(i), cursor.getString(i));
            }
            assignBundle(true);
        } else {
            assignBundle(false);
        }
        adb.setView(view);
        etOdometer.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        return adb.create();
    }

    private void assignBundle(boolean isNewEntry) {
        etOdometer.setText(mBundle.getString(COLUMN_NAME_ODOMETER));
        etAmount.setText(mBundle.getString(COLUMN_NAME_AMOUNT));
        etPrice.setText(mBundle.getString(COLUMN_NAME_PRICE));
        etTotalPrice.setText(mBundle.getString(COLUMN_NAME_TOTAL_PRICE));
        if (!isNewEntry) {
            timeStamp = mBundle.getLong(COLUMN_NAME_ENTRY_ID);
            btTime.setText(mBundle.getString(COLUMN_NAME_TIME));
            btDate.setText(mBundle.getString(COLUMN_NAME_DATE));
        }
    }

    private void reCalcValues() {
//        Log.d(TAG, "reCalcValues: ");
        try {
            odometer = Integer.parseInt(etOdometer.getText().toString());
        } catch (NumberFormatException nfe) {
            Log.d(TAG, "reCalcValues: parseInt failed odometer");
        }
        try {
            amount = Integer.parseInt(etAmount.getText().toString());
        } catch (NumberFormatException nfe) {
            Log.d(TAG, "reCalcValues: parseInt failed amount");
        }
        try {
            price = Integer.parseInt(etPrice.getText().toString());
        } catch (NumberFormatException nfe) {
            Log.d(TAG, "reCalcValues: parseInt failed price");
        }
        try {
            totalPrice = Integer.parseInt(etTotalPrice.getText().toString());
        } catch (NumberFormatException nfe) {
            Log.d(TAG, "reCalcValues: parseInt failed totalPrice");
        }
        etTotalPrice.setText(String.valueOf(price * amount));
    }

    public static EntryFragment getInstance(EntryFragmentListener listener, Bundle bundle) {
        if (sInstance == null) sInstance = new EntryFragment();
        sInstance.mListener = listener;
        mBundle = bundle;
        return sInstance;
    }

    private String getDateString(long timeMillis) {

        try {
            DateFormat sdf = DateFormat.getDateInstance();
            Date netDate = (new Date(timeMillis));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    private String getTimeString(long timeMillis) {

        try {
            DateFormat sdf = DateFormat.getTimeInstance(DateFormat.SHORT);
            Date netDate = (new Date(timeMillis));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        int k = 0;
        switch (i) {
            case Dialog.BUTTON_POSITIVE:
                k = R.string.saveEntry;
                reCalcValues();
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_ENTRY_ID, timeStamp);
                values.put(COLUMN_NAME_ODOMETER, odometer);
                values.put(COLUMN_NAME_AMOUNT, amount);
                values.put(COLUMN_NAME_PRICE, price);
                values.put(COLUMN_NAME_TOTAL_PRICE, totalPrice);
                values.put(COLUMN_NAME_DATE, btDate.getText().toString());
                values.put(COLUMN_NAME_TIME, btTime.getText().toString());
                mListener.onEntrySaveClick(values);
                break;
            case Dialog.BUTTON_NEGATIVE:
                k = R.string.cancelEntry;
                break;
            case Dialog.BUTTON_NEUTRAL:
                k = R.string.maybe;
                break;
        }
        if (k > 0)
            Log.d(TAG, "EntryFragment: " + getResources().getString(k));
    }

    @Override
    public void onDatePositiveClick(long date) {
        btDate.setText(getDateString(date));
    }

    @Override
    public void onTimePositiveClick(long time) {
        btTime.setText(getTimeString(time));
    }
}
