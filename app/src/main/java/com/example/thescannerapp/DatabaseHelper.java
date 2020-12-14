package com.example.thescannerapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.thescannerapp.DatabaseContract.*;

public class DatabaseHelper extends SQLiteOpenHelper{

    // Database creation sql statement
    //Parent Table
    private static final String SQL_CREATE_REGISTRY =
            "CREATE TABLE " + RegistrationDB.TABLE_NAME + " (" +
                    RegistrationDB._ID + " INTEGER PRIMARY KEY," +
                    RegistrationDB.COLUMN_USERNAME+ " TEXT," +
                    RegistrationDB.COLUMN_PASSWORD + " TEXT)";
    private static final String DATABASE_NAME = "register.db";
    private static final int DATABASE_VERSION = 1;

    //constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create the database
        sqLiteDatabase.execSQL(SQL_CREATE_REGISTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        //delete old data
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RegistrationDB.TABLE_NAME);

        //recreate tables
        onCreate(sqLiteDatabase);
    }
    //this will add the new user to the database
    public long addUser(String user, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RegistrationDB.COLUMN_USERNAME,user);
        contentValues.put(RegistrationDB.COLUMN_PASSWORD,password);
        long res = db.insert(RegistrationDB.TABLE_NAME,null,contentValues);
        db.close();
        return  res;
    }
    //this is used as a validation for the login
    public boolean checkUser(String username, String password){
        String[] columns = { RegistrationDB._ID };
        SQLiteDatabase db = getReadableDatabase();
        String selection = RegistrationDB.COLUMN_USERNAME + "=?" + " and " + RegistrationDB.COLUMN_PASSWORD + "=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(RegistrationDB.TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }
}
