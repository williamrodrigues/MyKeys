package br.schoollabs.mykeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

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
import br.schoollabs.mykeys.model.Data;
import br.schoollabs.utils.DataSistema;
import br.schoollabs.utils.Utils;

public class BackupRestoreActivity extends ListActivity {
	private BackupRestoreAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup_restore);
		
		listAdapter = new BackupRestoreAdapter(this);
		setListAdapter(listAdapter);

		registerForContextMenu(this.getListView());		

		File arq = new File(Environment.getExternalStorageDirectory() + "/MyKeys/");
		boolean b = arq.mkdir();
		System.out.println(b ? "Diretório criado com sucesso." : "Diretório já existe");

		findBackup();
		String dir = Environment.getExternalStorageDirectory() + "/MyKeys/";

		File diretorio = new File(dir);
		File fList[] = diretorio.listFiles();

		System.out.println("Numero de arquivos no diretorio : " + fList.length);

		for (int i = 0; i < fList.length; i++) {
			System.out.println(fList[i].getName() + " " + new Date(fList[i].lastModified()));
		}
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
				backupDatabase(DataSistema.getDataTimeCorrenteDDMMYYYYHHMMSSSSS() + ".bak");

				Utils.msg(this, "Backup criado com sucesso!");

				findBackup();
			} catch (IOException e) {
				Utils.msg(this, "Erro ao criar o Backup!");
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
					if(removeBackup(backupDelete)){
						Utils.msg(BackupRestoreActivity.this, "Backup excluído com sucesso!");
					}
					else{
						Utils.msg(BackupRestoreActivity.this, "Erro excluir backup!");
					}

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
					
					Utils.msg(BackupRestoreActivity.this, "Backup restaurado com sucesso!");

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

	private void findBackup() {
		listAdapter.clear();

		File diretorio = new File(Environment.getExternalStorageDirectory() + "/MyKeys/");
		File fList[] = diretorio.listFiles();

		System.out.println("Numero de backups do sistema: " + fList.length);

		for (int i = 0; i < fList.length; i++) {
			Data data = new Data();
			data.setContent(fList[i].getName());
			data.setDate(new Date(fList[i].lastModified()));

			listAdapter.add(data);
		}
	}

	@SuppressLint("SdCardPath")
	private void backupDatabase(String nameFile) throws IOException {
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
	
	private Boolean removeBackup(Data data){
		File diretorio = new File(Environment.getExternalStorageDirectory() + "/MyKeys/");
		File fList[] = diretorio.listFiles();

		System.out.println("Numero de backups do sistema: " + fList.length);

		for (int i = 0; i < fList.length; i++) {
			if(data.getContent().equals(fList[i].getName())){
				return fList[i].delete();
			}
		}
		
		return false;
	}
}
