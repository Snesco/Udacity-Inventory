package com.steven.inventory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by steven on 3/2/17.
 */

public class InventoryAdapter extends ArrayAdapter<Product> {
	public InventoryAdapter(Context context) {
		super(context, 0);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
				parent,
				false);
		}
		Product currentProduct = getItem(position);

		TextView nameTextView = (TextView) convertView.findViewById(R.id.text_view_name);
		TextView priceTextView = (TextView) convertView.findViewById(R.id.text_view_price);
		TextView quantityTextView = (TextView) convertView.findViewById(R.id.text_view_quantity);
		TextView supplierTextView = (TextView) convertView.findViewById(R.id.text_view_supplier);

		nameTextView.setText(currentProduct.getName());
		priceTextView.setText(String.valueOf(currentProduct.getPrice()) + " $");
		quantityTextView.setText(String.valueOf(currentProduct.getQuantity()));
		supplierTextView.setText(String.valueOf(currentProduct.getSupplier()));

		return convertView;

	}
}
