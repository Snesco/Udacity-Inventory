package com.steven.inventory.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by steven on 2/27/17.
 */

public class InventoryContract {

	public static final String CONTENT_AUTHORITY = "com.steven.inventory.provider";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

	private InventoryContract() {
	}

	public static class InventoryEntry implements BaseColumns {

		public static final String TABLE_NAME = "inventory";

		public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);

		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_PRICE = "price";
		public static final String COLUMN_NAME_QUANTITY = "quantity";
		public static final String COLUMN_NAME_SUPPLIER = "supplier";

		static final String SQL_CREATE_TABLE =
			"CREATE TABLE " + TABLE_NAME + " ("
				+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_NAME_TITLE + " TEXT NOT NULL, "
				+ COLUMN_NAME_PRICE + " REAL DEFAULT 0, "
				+ COLUMN_NAME_QUANTITY + " INTEGER DEFAULT 1, "
				+ COLUMN_NAME_SUPPLIER + " TEXT)";

		static final String SQL_DELETE_TABLE =
			"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}
