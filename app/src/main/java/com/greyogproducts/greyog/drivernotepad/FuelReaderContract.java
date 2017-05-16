package com.greyogproducts.greyog.drivernotepad;

import android.provider.BaseColumns;

/**
 * Created by greyog on 11/05/17.
 */

final class FuelReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FuelReaderContract() {}

    /* Inner class that defines the table contents */
    static abstract class fuelEntry implements BaseColumns {
        static final String ENTRIES_TABLE_NAME = "fuel_entries";
        static final String COLUMN_NAME_ENTRY_ID = "entry_id";
        static final String COLUMN_NAME_CAR = "car";
        static final String COLUMN_NAME_FUEL_TYPE = "fuel_type";
        static final String COLUMN_NAME_ODOMETER = "odometer";
        static final String COLUMN_NAME_AMOUNT = "amount";
        static final String COLUMN_NAME_PRICE = "price";
        static final String COLUMN_NAME_TOTAL_PRICE = "total_price";
        static final String COLUMN_NAME_DATE = "date";
        static final String COLUMN_NAME_TIME = "time";

    }
}
