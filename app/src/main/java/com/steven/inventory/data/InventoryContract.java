package com.steven.inventory.data;

import android.provider.BaseColumns;

/**
 * Created by steven on 2/27/17.
 */

public class InventoryContract {

	public static final String COMMA = ",";
	public static final String TYPE_INTEGER = " INTEGER ";
	public static final String TYPE_TEXT = " TEXT ";
	public static final String TYPE_BLOB = " BLOB ";

	public static final String CONSTRAINT_PRIMARY_KEY = " PRIMARY KEY ";
	public static final String CONSTRAINT_NOT_NULL = " NOT NULL ";

	public static final String KEY_AUTOINCREMENT = "AUTOINCREMENT";

	private InventoryContract() {
	}

	public static class InventoryEntry implements BaseColumns {

		public static final String TABLE_NAME = "inventory";

		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_PRICE = "price";
		public static final String COLUMN_NAME_QUANTITY = "quantity";
		public static final String COLUMN_NAME_IMAGE = "image";
		public static final String COLUMN_NAME_SUPPLIER = "supplier";
	}
}
