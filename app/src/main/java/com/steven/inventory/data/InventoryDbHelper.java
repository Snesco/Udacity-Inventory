package com.steven.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.steven.inventory.data.InventoryContract.InventoryEntry;

/**
 * Created by steven on 2/28/17.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "inventory.db";


	public InventoryDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
				+ InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ InventoryEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, "
				+ InventoryEntry.COLUMN_NAME_PRICE + " REAL DEFAULT 0, "
				+ InventoryEntry.COLUMN_NAME_QUANTITY + " INTEGER DEFAULT 1, "
				+ InventoryEntry.COLUMN_NAME_SUPPLIER + " TEXT, "
				+ InventoryEntry.COLUMN_NAME_IMAGE + "BLOB)";

		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String SQL_CREATE_ENTRIES = "DROP TABLE IF EXISTS " + InventoryEntry.TABLE_NAME;
	}
}
