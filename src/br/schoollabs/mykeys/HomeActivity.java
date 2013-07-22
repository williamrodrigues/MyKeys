package br.schoollabs.mykeys;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
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

		findTypes();

		DataDaoSqLite dataDaoSqLite = new DataDaoSqLite();
		((TextView) findViewById(R.id.labelHomeUsername)).setText((dataDaoSqLite.findOwnerName()).getContent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Data data = (Data) getListAdapter().getItem(position);
		
		if("Nova Senha".equals(data.getContent())){
			Utils.startActivity(this, NewKeyActivity.class);
		}
		else if("Listar Senhas".equals(data.getContent())){
			Utils.startActivity(this, ListKeyActivity.class);
		}
		else if("Categorias".equals(data.getContent())){
			Utils.startActivity(this, CategoriesActivity.class);
		}
	}

	private void findTypes() {
		List<Data> datas = dataDaoSqLite.findAllHomeMenus();

		listAdapter.clear();
		listAdapter.addAll(datas);
	}
}
