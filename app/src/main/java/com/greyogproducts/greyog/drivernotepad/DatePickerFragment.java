package com.greyogproducts.greyog.drivernotepad;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by greyog on 12/05/17.
 */

public class DatePickerFragment extends DialogFragment implements DialogInterface.OnClickListener{

    private static DatePickerFragment sInstance;
    private DatePicker picker;
    // Use this instance of the interface to deliver action events
    private static DatePickerListener mListener;
    public static DatePickerFragment getInstance(EntryFragment fragment) {
        if (sInstance == null) sInstance = new DatePickerFragment();
        mListener = fragment;
        return sInstance;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case Dialog.BUTTON_POSITIVE :
                Calendar calendar = Calendar.getInstance();
                calendar.set(picker.getYear(),picker.getMonth(),picker.getDayOfMonth());
                mListener.onDatePositiveClick(calendar.getTimeInMillis());
                break;
        }
    }

    /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */
    interface DatePickerListener {
        void onDatePositiveClick(long date);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.saveEntry, this)
                .setNegativeButton(R.string.cancelEntry, null);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.date_picker_layout,null);
        picker = (DatePicker) view.findViewById(R.id.datePicker);
        adb.setView(view);
        return adb.create();
    }

}
