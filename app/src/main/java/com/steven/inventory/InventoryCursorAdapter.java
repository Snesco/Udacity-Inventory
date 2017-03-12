package com.steven.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.steven.inventory.data.InventoryContract.InventoryEntry;

/**
 * Created by steven on 3/12/17.
 */

public class InventoryCursorAdapter extends CursorAdapter {
	public InventoryCursorAdapter(Context context, Cursor c) {
		super(context, c, 0);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TextView nameTextView = (TextView) view.findViewById(R.id.text_view_name);
		TextView priceTextView = (TextView) view.findViewById(R.id.text_view_price);
		TextView quantityTextView = (TextView) view.findViewById(R.id.text_view_quantity);
		TextView supplierTextView = (TextView) view.findViewById(R.id.text_view_supplier);

		int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_TITLE);
		int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_PRICE);
		int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_QUANTITY);
		int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_SUPPLIER);

		String productName = cursor.getString(nameColumnIndex);
		float productPrice = cursor.getFloat(priceColumnIndex);
		int productQuantity = cursor.getInt(quantityColumnIndex);
		String productSupplier = cursor.getString(supplierColumnIndex);

		nameTextView.setText(productName);
		priceTextView.setText(String.valueOf(productPrice));
		quantityTextView.setText(String.valueOf(productQuantity));
		supplierTextView.setText(productSupplier);
	}
}
