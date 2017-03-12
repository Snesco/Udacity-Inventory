package com.steven.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.steven.inventory.data.InventoryContract.InventoryEntry;

public class ListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private InventoryCursorAdapter inventoryCursorAdapter;

	private final static String LOG_TAG = ListActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		inventoryCursorAdapter = new InventoryCursorAdapter(this, null);

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
				i.setData(ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id));
				startActivity(i);
			}
		});
		listView.setAdapter(inventoryCursorAdapter);
		getLoaderManager().initLoader(1, null, this);
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

	private void deleteAll() {
		getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
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

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this,
			InventoryEntry.CONTENT_URI,
			null,
			null,
			null,
			null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		inventoryCursorAdapter.swapCursor(data);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		inventoryCursorAdapter.swapCursor(null);
	}
}
