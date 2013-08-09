package br.schoollabs.mykeys;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
		if (item.getItemId() == R.id.action_settings) {
			final AlertDialog.Builder editalert = new AlertDialog.Builder(this);

			editalert.setTitle("Digite sua senha:");

			final EditText input = new EditText(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			input.setLayoutParams(lp);
			input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
			input.setTransformationMethod(PasswordTransformationMethod.getInstance());
			editalert.setView(input);

			editalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					if ((dataDaoSqLite.findPWD()).getContent().equals(input.getText().toString())) {
						Utils.startActivity(HomeActivity.this, SettingsActivity.class);
					} else {
						Utils.msg(HomeActivity.this, "Senha inválida!");
					}
				}
			});

			editalert.show();

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

	private void findCategories() {
		listAdapter.clear();
		for (Data data : dataDaoSqLite.findCategories()) {
			listAdapter.add(data);
		}
	}
}
