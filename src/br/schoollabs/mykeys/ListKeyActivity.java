package br.schoollabs.mykeys;

import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.utils.Utils;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

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

			Utils.startActivity(this, NewKeyActivity.class, "idCategory", (String) getIntent().getExtras().get("idCategory"));
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.action_listkey_delete:
			final Data keyDelete = (Data) getListAdapter().getItem(info.position);

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Confirmação");
			dialog.setIcon(R.drawable.content_discard);
			dialog.setMessage("Deseja realmente excluir esta senha?");
			dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dataDaoSqLite.remove(keyDelete);

					findKeys((String) getIntent().getExtras().get("idCategory"));

					Utils.msg(ListKeyActivity.this, "Senha excluída com sucesso!!!");
				}
			});
			dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			dialog.show();
			return true;

		case R.id.action_listkey_edit:
			Data keyEdit = (Data) getListAdapter().getItem(info.position);

			Utils.startActivity(this, NewKeyActivity.class, "idKey", keyEdit.getId().toString());

		default:
			return super.onContextItemSelected(item);
		}
	}

	private void findKeys(String idCategory) {
		// Seta o nome da categoria
		((TextView) findViewById(R.id.labelListKeyNameCategory)).setText((dataDaoSqLite.find(idCategory)).getContent());

		listKeyAdapter.clear();
		for (Data data : dataDaoSqLite.findAll("category", idCategory.toString())) {
			listKeyAdapter.add(data);
		}
	}

}
