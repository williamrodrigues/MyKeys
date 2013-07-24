package br.schoollabs.mykeys;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.utils.Utils;

public class HomeActivity extends ListActivity {
	private DataDaoSqLite dataDaoSqLite;
	private HomeMenuListAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		dataDaoSqLite = new DataDaoSqLite();
		listAdapter = new HomeMenuListAdapter(this);
		setListAdapter(listAdapter);

		registerForContextMenu(this.getListView());

		findCategories();

		DataDaoSqLite dataDaoSqLite = new DataDaoSqLite();
		((TextView) findViewById(R.id.labelHomeUsername)).setText((dataDaoSqLite.findOwnerName()).getContent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		findCategories();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_home_category_new) {
			Utils.startActivity(this, NewCategoryActivity.class);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, ListKeyActivity.class);
		Data data = (Data) getListAdapter().getItem(position);

		// Passando o id da categoria clicada para a tela de senhas
		intent.putExtra("idCategory", data.getId().toString());

		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_menu_item, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.action_home_delete:
			final Data category = (Data) getListAdapter().getItem(info.position);

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Confirmação");
			dialog.setIcon(R.drawable.content_discard);
			dialog.setMessage("Deseja realmente excluir esta categoria?");
			dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {					
					dataDaoSqLite.remove(category);

					findCategories();
					
					Utils.msg(HomeActivity.this, "Categoria excluída com sucesso!!!");
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

		default:
			return super.onContextItemSelected(item);
		}
	}

	private void findCategories() {
		List<Data> datas = dataDaoSqLite.findCategories();

		listAdapter.clear();
		listAdapter.addAll(datas);
	}
}
