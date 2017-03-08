package com.steven.inventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.steven.inventory.data.InventoryDbHelper;
import com.steven.inventory.data.InventoryContract.InventoryEntry;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

	private InventoryDbHelper inventoryDbHelper;
	private InventoryAdapter inventoryAdapter;

	private final static String LOG_TAG = ListActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		inventoryAdapter = new InventoryAdapter(this);

		inventoryDbHelper = new InventoryDbHelper(this);
		ListView listView = (ListView) findViewById(R.id.list);
		FloatingActionButton addFAB = (FloatingActionButton) findViewById(R.id.fab_add);
		addFAB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ListActivity.this, EditorActivity.class);
				startActivity(i);
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(ListActivity.this, EditorActivity.class);
				i.putExtra("id", inventoryAdapter.getItem(position).getId());
				startActivity(i);
			}
		});
		listView.setAdapter(inventoryAdapter);
		displayInventory();
	}

	@Override
	protected void onStart() {
		super.onStart();
		displayInventory();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_delete_all:
				showDeleteAllConfirmationDialog();
		}
		return true;
	}

	private void displayInventory() {
		inventoryAdapter.clear();

		SQLiteDatabase db = inventoryDbHelper.getReadableDatabase();

		ArrayList<Product> productArrayList = new ArrayList<>();

		Cursor cursor = db.query(InventoryEntry.TABLE_NAME,
			null,
			null,
			null,
			null,
			null,
			InventoryEntry._ID + " DESC");

		while (cursor.moveToNext()) {
			int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
			int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_TITLE);
			int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_PRICE);
			int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_QUANTITY);
			int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME_SUPPLIER);

			int productId = cursor.getInt(idColumnIndex);
			String productName = cursor.getString(nameColumnIndex);
			float productPrice = cursor.getFloat(priceColumnIndex);
			int productQuantity = cursor.getInt(quantityColumnIndex);
			String productSupplier = cursor.getString(supplierColumnIndex);

			Product newProduct = new Product(productId,
				productName,
				productPrice,
				productQuantity,
				productSupplier);

			productArrayList.add(newProduct);
		}

		cursor.close();
		db.close();
		inventoryAdapter.addAll(productArrayList);
	}

	private void deleteAll() {
		SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();
		db.delete(InventoryEntry.TABLE_NAME, null, null);
		db.close();
		displayInventory();
	}

	private void showDeleteAllConfirmationDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.list_delete_all_title);
		alertDialogBuilder.setMessage(R.string.list_delete_all_message);

		alertDialogBuilder.setPositiveButton(R.string.list_delete_all_positive,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteAll();
				}
			});

		alertDialogBuilder.setNegativeButton(R.string.list_delete_all_negative,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}
