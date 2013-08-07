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
import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.dao.sqlite.TypeDaoSqLite;
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.utils.DataSistema;
import br.schoollabs.utils.Utils;

public class BackupRestoreActivity extends ListActivity {
	private DataDaoSqLite dataDaoSqLite;
	private BackupRestoreAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup_restore);

		dataDaoSqLite = new DataDaoSqLite();
		listAdapter = new BackupRestoreAdapter(this);
		setListAdapter(listAdapter);

		registerForContextMenu(this.getListView());

		findBackup();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.backup_restore, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_backup) {

			try {
				Data data = new Data();
				data.setContent(backupDatabase());
				data.setName("Backup");
				data.setType(((TypeDaoSqLite) dataDaoSqLite.instanceDaoSqLite("Type")).find("name", "System"));

				dataDaoSqLite.save(data);

				Utils.msg(this, "Backup criado com sucesso!");

				findBackup();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.backup_restore_menu_item, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.action_backup_delete:
			final Data backupDelete = (Data) getListAdapter().getItem(info.position);

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Confirmação");
			dialog.setIcon(R.drawable.content_discard);
			dialog.setMessage("Deseja realmente excluir este backup?");
			dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dataDaoSqLite.remove(backupDelete);

					findBackup();
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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final Data data = (Data) getListAdapter().getItem(position);

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Confirmação");
		dialog.setIcon(R.drawable.content_discard);
		dialog.setMessage("Deseja restaurar este backup?");
		dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					restoreDatabase(data);
					
					findBackup();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		dialog.show();
	}

	@SuppressLint("SdCardPath")
	private void restoreDatabase(Data data) throws IOException {
		String inFileName = Environment.getExternalStorageDirectory() + "/MyKeys/" + data.getContent();
		File dbFile = new File(inFileName);
		FileInputStream fis = new FileInputStream(dbFile);

		String outFileName = "/data/data/br.schoollabs.mykeys/databases/mykeys.db";
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

	private void findBackup() {
		listAdapter.clear();
		for (Data data : dataDaoSqLite.findBackup()) {
			listAdapter.add(data);
		}
	}

	@SuppressLint("SdCardPath")
	private String backupDatabase() throws IOException {
		String nameFile = "";
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

			nameFile = DataSistema.getDataTimeCorrenteDDMMYYYYHHMMSSSSS() + ".bak";

			String outFileName = Environment.getExternalStorageDirectory() + "/MyKeys/" + nameFile;
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

		return nameFile;
	}
}
