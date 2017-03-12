package com.steven.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.steven.inventory.data.InventoryContract.InventoryEntry;

/**
 * Created by steven on 3/9/17.
 */

public class InventoryProvider extends ContentProvider {

	private final static String LOG_TAG = InventoryProvider.class.getName();

	private static final int INVENTORY_LIST = 100;

	private static final int INVENTORY_ID = 101;

	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

	private InventoryDbHelper inventoryDbHelper;
	private SQLiteDatabase db;

	static {

		URI_MATCHER.addURI(InventoryContract.CONTENT_AUTHORITY,
			InventoryEntry.TABLE_NAME,
			INVENTORY_LIST);

		URI_MATCHER.addURI(InventoryContract.CONTENT_AUTHORITY,
			InventoryEntry.TABLE_NAME + "/#",
			INVENTORY_ID);
	}

	@Override
	public boolean onCreate() {
		inventoryDbHelper = new InventoryDbHelper(getContext());
		return true;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
						@Nullable String[] selectionArgs, @Nullable String sortOrder) {

		switch (URI_MATCHER.match(uri)) {
			case INVENTORY_LIST:
				break;
			case INVENTORY_ID:
				selection = InventoryEntry._ID + "=?";
				selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
				break;
			default:
				throw new IllegalArgumentException("Unknown query uri " + uri.toString());
		}
		db = inventoryDbHelper.getReadableDatabase();

		Cursor cursor = db.query(InventoryEntry.TABLE_NAME,
			projection,
			selection,
			selectionArgs,
			null,
			null,
			sortOrder);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);
//		db.close();
		return cursor;

	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
		switch (URI_MATCHER.match(uri)) {
			case INVENTORY_LIST:
				return insertProduct(uri, values);
			default:
				throw new IllegalArgumentException("Unknown insert uri " + uri.toString());
		}
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection,
					  @Nullable String[] selectionArgs) {
		switch (URI_MATCHER.match(uri)) {
			case INVENTORY_LIST:
				return deleteProduct(uri, selection, selectionArgs);
			case INVENTORY_ID:
				selection = InventoryEntry._ID + "=?";
				selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
				return deleteProduct(uri, selection, selectionArgs);
			default:
				throw new IllegalArgumentException("Unknown delete uri " + uri.toString());

		}

	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
					  @Nullable String[] selectionArgs) {
		switch (URI_MATCHER.match(uri)) {
			case INVENTORY_LIST:
				return updateProduct(uri, values, selection, selectionArgs);
			case INVENTORY_ID:
				selection = InventoryEntry._ID + "=?";
				selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
				return updateProduct(uri, values, selection, selectionArgs);
			default:
				throw new IllegalArgumentException("Unknown update uri " + uri.toString());

		}
	}

	private Uri insertProduct(Uri uri, ContentValues values) {

		db = inventoryDbHelper.getWritableDatabase();
		long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);
		if (newRowId == -1) {
			Log.e(LOG_TAG, "Error inserting product");
			return null;
		}
		getContext().getContentResolver().notifyChange(InventoryEntry.CONTENT_URI, null);
//		db.close();
		return ContentUris.withAppendedId(uri, newRowId);
	}

	private int updateProduct(Uri uri, ContentValues values, String selection,
							  String[] selectionArgs) {
		db = inventoryDbHelper.getWritableDatabase();
		int rowsUpdated = db.update(InventoryEntry.TABLE_NAME, values, selection, selectionArgs);
		if (rowsUpdated > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
//		db.close();
		return rowsUpdated;
	}

	private int deleteProduct(Uri uri, String selection, String[] selectionArgs) {
		db = inventoryDbHelper.getWritableDatabase();
		int rowsDeleted = db.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
		if (rowsDeleted > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
//		db.close();
		return rowsDeleted;
	}
}
