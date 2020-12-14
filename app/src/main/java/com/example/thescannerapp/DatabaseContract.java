package com.example.thescannerapp;

import android.provider.BaseColumns;


public final class DatabaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DatabaseContract(){
    }

    /* Inner class that defines the table contents */
    public static class RegistrationDB implements BaseColumns {
        public static final String TABLE_NAME = "register_user";
        public static final String COLUMN_USERNAME ="username";
        public static final String COLUMN_PASSWORD ="password";

    }

}

