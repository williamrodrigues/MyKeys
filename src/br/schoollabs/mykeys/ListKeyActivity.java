package br.schoollabs.mykeys;

import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.utils.Utils;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ListKeyActivity extends ListActivity {
	private ListKeyAdapter listKeyAdapter;
	private DataDaoSqLite dataDaoSqLite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_key);

		dataDaoSqLite = new DataDaoSqLite();
		listKeyAdapter = new ListKeyAdapter(this);
		setListAdapter(listKeyAdapter);

		registerForContextMenu(this.getListView());

		findKeys((String) getIntent().getExtras().get("idCategory"));
	}

	@Override
	protected void onResume() {
		super.onResume();

		findKeys((String) getIntent().getExtras().get("idCategory"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_key, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_new_key) {
			finish();
			
			Utils.startActivity(this, NewKeyActivity.class);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_key_menu_item, menu);
	}

	private void findKeys(String idCategory) {
		// Seta o nome da categoria
		((TextView) findViewById(R.id.labelListKeyNameCategory)).setText((dataDaoSqLite.find(idCategory)).getContent());
		
		listKeyAdapter.clear();
		listKeyAdapter.addAll(dataDaoSqLite.findAll("category", idCategory.toString()));
	}

}
