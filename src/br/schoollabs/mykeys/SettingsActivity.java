package br.schoollabs.mykeys;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import br.schoollabs.mykeys.dao.sqlite.DataDaoSqLite;
import br.schoollabs.mykeys.model.Registry;
import br.schoollabs.utils.Utils;

public class SettingsActivity extends ListActivity {
	private DataDaoSqLite dataDaoSqLite;
	private SettingsAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		dataDaoSqLite = new DataDaoSqLite();
		listAdapter = new SettingsAdapter(this);
		setListAdapter(listAdapter);

		registerForContextMenu(this.getListView());

		findSettings();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Utils.startActivity(this, onclickList(((Registry) getListAdapter().getItem(position)).getName() + "Activity"));
	}

	private void findSettings() {
		listAdapter.clear();
		for (Registry registry : (dataDaoSqLite.findSettings()).getRegistries()) {
			listAdapter.add(registry);
		}
	}

	private Class<? extends Activity> onclickList(String nameClass) {
		if (BackupRestoreActivity.class.getSimpleName().equals(nameClass)) {
			return BackupRestoreActivity.class;
		} else if (CategorySettingsActivity.class.getSimpleName().equals(nameClass)) {
			return CategorySettingsActivity.class;
		} else if (UserRegisteringTheInSystemActivity.class.getSimpleName().equals(nameClass)) {
			return UserRegisteringTheInSystemActivity.class;
		} else {
			return null;
		}
	}

}
