package com.steven.inventory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.steven.inventory.data.InventoryDbHelper;

public class EditorActivity extends AppCompatActivity {

	private final static String LOG_TAG = EditorActivity.class.getName();

	private InventoryDbHelper inventoryDbHelper;
	private EditText nameEditText;
	private EditText priceEditText;
	private EditText quantityEditText;
	private EditText supplierEditText;

	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		inventoryDbHelper = new InventoryDbHelper(this);

		nameEditText = (EditText) findViewById(R.id.edit_text_name);
		priceEditText = (EditText) findViewById(R.id.edit_text_price);
		priceEditText.setText(String.valueOf(0));
		quantityEditText = (EditText) findViewById(R.id.edit_text_quantity);
		quantityEditText.setText(String.valueOf(1));
		supplierEditText = (EditText) findViewById(R.id.edit_text_supplier);

		id = getIntent().getIntExtra("id", -1);


		if (id != -1) {
			setTitle(R.string.edit_activity_title);
			getProductDetails(id);
			nameEditText.setEnabled(false);
			supplierEditText.setEnabled(false);
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
		}
		return true;
	}

	private void saveProduct() {

		SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();

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

		if (id == -1) {
			long newRowId = db.insert(InventoryEntry.TABLE_NAME,
				null,
				contentValues);

			if (newRowId < 0) {
				Toast.makeText(this, "Adding item failed", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
				finish();
			}
		} else {
			int rowsAffected = db.update(InventoryEntry.TABLE_NAME,
				contentValues,
				InventoryEntry._ID + "=?",
				new String[]{String.valueOf(id)});
			if (rowsAffected == 0) {
				Toast.makeText(this, "Editing item failed", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Item edited", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
		db.close();
	}

	private void getProductDetails(int id) {
		SQLiteDatabase db = inventoryDbHelper.getReadableDatabase();
		Cursor cursor = db.query(InventoryEntry.TABLE_NAME,
			null,
			InventoryEntry._ID + "=?",
			new String[]{String.valueOf(id)},
			null,
			null,
			null);

		cursor.moveToNext();

		int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_TITLE);
		int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_PRICE);
		int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_QUANTITY);
		int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_SUPPLIER);

		String productName = cursor.getString(nameColumnIndex);
		float productPrice = cursor.getFloat(priceColumnIndex);
		int productQuantity = cursor.getInt(quantityColumnIndex);
		String productSupplier = cursor.getString(supplierColumnIndex);

		nameEditText.setText(productName);
		priceEditText.setText(String.valueOf(productPrice));
		quantityEditText.setText(String.valueOf(productQuantity));
		supplierEditText.setText(productSupplier);

		cursor.close();
		db.close();
	}
}