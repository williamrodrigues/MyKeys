package br.schoollabs.mykeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
	protected void onRestart() {
		// Atualizar o nome do Usuario quando voltar nesta activity
		((TextView) findViewById(R.id.labelHomeUsername)).setText((dataDaoSqLite.findOwnerName()).getContent());

		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Atualizar o nome do Usuario quando voltar nesta activity
		((TextView) findViewById(R.id.labelHomeUsername)).setText((dataDaoSqLite.findOwnerName()).getContent());

		findCategories();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_home_category_new) {
			Utils.startActivity(this, NewCategoryActivity.class);
			return true;
		}
		if (item.getItemId() == R.id.action_settings) {
			Utils.startActivity(this, SettingsActivity.class);
			// try {
			// backupDatabase();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			return true;
		}
		if(item.getItemId() == R.id.action_backup_restore){
			Utils.startActivity(this, BackupRestoreActivity.class);
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
			final Data categoryDelete = (Data) getListAdapter().getItem(info.position);

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Confirmação");
			dialog.setIcon(R.drawable.content_discard);
			dialog.setMessage("Deseja realmente excluir esta categoria?");
			dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dataDaoSqLite.remove(categoryDelete);

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

		case R.id.action_home_edit:
			Data categoryEdit = (Data) getListAdapter().getItem(info.position);

			Utils.startActivity(this, NewCategoryActivity.class, "idCategory", categoryEdit.getId().toString());

		default:
			return super.onContextItemSelected(item);
		}
	}

	private void findCategories() {
		listAdapter.clear();
		for (Data data : dataDaoSqLite.findCategories()) {
			listAdapter.add(data);
		}
	}

	@SuppressLint("SdCardPath")
	public static void backupDatabase() throws IOException {
		boolean success = true;
		File file = null;
		file = new File(Environment.getExternalStorageDirectory() + "/MyKeys");

		if (file.exists()) {
			success = true;
		} else {
			success = file.mkdir();
		}

		if (success) {
			String inFileName = "/data/data/br.schoollabs.mykeys/databases/mykeys.db";
			File dbFile = new File(inFileName);
			FileInputStream fis = new FileInputStream(dbFile);

			String outFileName = Environment.getExternalStorageDirectory() + "/MyKeys/mykeys.bak";
			// Open the empty db as the output stream
			OutputStream output = new FileOutputStream(outFileName);
			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}

			output.flush();
			output.close();
			fis.close();
		}
	}
}
