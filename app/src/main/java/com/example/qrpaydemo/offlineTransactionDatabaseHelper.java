package com.example.qrpaydemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class offlineTransactionDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";


    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "transcactions";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";

    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_AMOUNT = "amount";

    public offlineTransactionDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public offlineTransactionDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_AMOUNT + " REAL" +
                ");";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Recreate the table by calling onCreate
        onCreate(db);
    }

    public long insertTransaction(User transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, transaction.getUserId());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        db.insert(TABLE_NAME, null, values);

        // Insert the transaction data into the database
        long newRowId = db.insert(TABLE_NAME, null, values);

        db.close();

        return newRowId; // Return the newly inserted row ID, or -1 if an error occurred
    }

    public List<User> getAllTransactions() {
        List<User> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String userId = cursor.getString(0);
                double amount = Double.parseDouble(cursor.getString(1));
                transactions.add(new User(userId, amount));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactions;
    }

    public void clearAllTransactions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

   /* public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            User user = new User();
            user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            user.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_BALANCE)));
            user.setUserId(cursor.getString(cursor.getColumnIndex(COLUMN_WALLET_ID)));
            cursor.close();
            return user;
        } else {
            return null;
        }
    }*/
}

