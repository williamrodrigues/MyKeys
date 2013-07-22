package br.schoollabs.mykeys;

import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import android.app.ListActivity;
import android.os.Bundle;

public class ListKeyActivity extends ListActivity {
	private ListKeyAdapter listKeyAdapter;
	private DataDaoSqLite dataDaoSqLite;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dataDaoSqLite = new DataDaoSqLite();
		listKeyAdapter = new ListKeyAdapter(this);
		setListAdapter(listKeyAdapter);
		
		findKeys();
	}


	@Override
	protected void onResume() {
		super.onResume();
		findKeys();
	}


	private void findKeys() {
		listKeyAdapter.clear();
		listKeyAdapter.addAll(dataDaoSqLite.findAll());
	}

}
