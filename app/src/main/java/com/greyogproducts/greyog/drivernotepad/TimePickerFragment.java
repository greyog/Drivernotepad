package com.greyogproducts.greyog.drivernotepad;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by greyog on 12/05/17.
 */

public class TimePickerFragment extends DialogFragment implements DialogInterface.OnClickListener{
    
    private static final TimePickerFragment ourInstance = new TimePickerFragment();
    private TimePicker picker;

    public static TimePickerFragment getInstance(EntryFragment fragment) {
        mListener = fragment;
        return ourInstance;
    }
    /* The activity that creates an instance of this dialog fragment must
             * implement this interface in order to receive event callbacks.
             * Each method passes the DialogFragment in case the host needs to query it. */
    interface TimePickerListener {
        void onTimePositiveClick(long time);
    }

    // Use this instance of the interface to deliver action events
    private static TimePickerListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.saveEntry, this)
                .setNegativeButton(R.string.cancelEntry, this);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.time_picker_layout,null);
        picker = (TimePicker) view.findViewById(R.id.timePicker);
        picker.setIs24HourView(DateFormat.is24HourFormat(getContext()));
        Date netDate = (new Date(System.currentTimeMillis()));
        picker.setCurrentHour(netDate.getHours());
        adb.setView(view);
        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case Dialog.BUTTON_POSITIVE :
                Calendar calendar = Calendar.getInstance();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    calendar.set(0,0,0,picker.getHour(),picker.getMinute());
                } else calendar.set(0,0,0,picker.getCurrentHour(),picker.getCurrentMinute());
                mListener.onTimePositiveClick(calendar.getTimeInMillis());
                break;
        }
    }
}
