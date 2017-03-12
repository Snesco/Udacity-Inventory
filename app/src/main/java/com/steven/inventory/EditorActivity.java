package com.steven.inventory;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.steven.inventory.data.InventoryContract.InventoryEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private final static String LOG_TAG = EditorActivity.class.getName();

	private EditText nameEditText;
	private EditText priceEditText;
	private EditText quantityEditText;
	private EditText supplierEditText;

	private Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);


		nameEditText = (EditText) findViewById(R.id.edit_text_name);
		priceEditText = (EditText) findViewById(R.id.edit_text_price);
		priceEditText.setText(String.valueOf(0));
		quantityEditText = (EditText) findViewById(R.id.edit_text_quantity);
		quantityEditText.setText(String.valueOf(1));
		supplierEditText = (EditText) findViewById(R.id.edit_text_supplier);

		uri = getIntent().getData();


		if (uri != null) {
			setTitle(R.string.edit_activity_title);
			nameEditText.setEnabled(false);
			supplierEditText.setEnabled(false);
			getLoaderManager().initLoader(2, null, this);
		} else {
			setTitle(R.string.add_activity_title);
		}

		final ImageButton quantityMinusButton =
			(ImageButton) findViewById(R.id.image_button_quantity_minus);
		ImageButton quantityPlusButton =
			(ImageButton) findViewById(R.id.image_button_quantity_plus);

		quantityMinusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String quantityString = quantityEditText.getText().toString().trim();
				if (!TextUtils.isEmpty(quantityString)) {
					int quantity = Integer.parseInt(quantityString);
					if (quantity > 1) {
						quantity -= 1;
						quantityEditText.setText(String.valueOf(quantity));
					}
				} else {
					quantityEditText.setText(String.valueOf(1));
				}
			}
		});
		quantityPlusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String quantityString = quantityEditText.getText().toString().trim();
				if (!TextUtils.isEmpty(quantityString)) {
					int quantity = Integer.parseInt(quantityString) + 1;
					quantityEditText.setText(String.valueOf(quantity));
				} else {
					quantityEditText.setText(String.valueOf(1));
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_editor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_done:
				saveProduct();
				break;
			case R.id.menu_delete:
				deleteProduct();
		}
		return true;
	}

	private void saveProduct() {

		String name = nameEditText.getText().toString().trim();
		String quantityString = quantityEditText.getText().toString().trim();
		String priceString = priceEditText.getText().toString().trim();
		String supplier = supplierEditText.getText().toString().trim();

		if (TextUtils.isEmpty(name)) name = null;
		int quantity = (TextUtils.isEmpty(quantityString)) ? 1 : Integer.parseInt(quantityString);
		float price = (TextUtils.isEmpty(priceString)) ? 0 : Float.parseFloat(priceString);

		ContentValues contentValues = new ContentValues();
		contentValues.put(InventoryEntry.COLUMN_NAME_TITLE, name);
		contentValues.put(InventoryEntry.COLUMN_NAME_PRICE, price);
		contentValues.put(InventoryEntry.COLUMN_NAME_QUANTITY, quantity);
		contentValues.put(InventoryEntry.COLUMN_NAME_SUPPLIER, supplier);

		if (uri == null) {
			Uri newRowUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);

			if (newRowUri == null) {
				Toast.makeText(this, "Adding item failed", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
				finish();
			}
		} else {
			int rowsAffected = getContentResolver().update(uri, contentValues, null, null);

			if (rowsAffected == 0) {
				Toast.makeText(this, "Editing item failed", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Item edited", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void deleteProduct() {

		if (uri != null) {
			int rowsDeleted = getContentResolver().delete(uri, null, null);
			if (rowsDeleted == 0) {
				Toast.makeText(this, "deleting item failed", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, uri, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		if (data == null || data.getCount() < 1) return;

		data.moveToFirst();

		int nameColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_NAME_TITLE);
		int priceColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_NAME_PRICE);
		int quantityColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_NAME_QUANTITY);
		int supplierColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_NAME_SUPPLIER);

		String productName = data.getString(nameColumnIndex);
		float productPrice = data.getFloat(priceColumnIndex);
		int productQuantity = data.getInt(quantityColumnIndex);
		String productSupplier = data.getString(supplierColumnIndex);

		nameEditText.setText(productName);
		priceEditText.setText(String.valueOf(productPrice));
		quantityEditText.setText(String.valueOf(productQuantity));
		supplierEditText.setText(productSupplier);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}